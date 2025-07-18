package com.nuoquan.netty;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.nuoquan.service.WeChatService;
import org.apache.commons.lang3.StringUtils;

import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.pojo.netty.ChatMessage;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.service.ArticleService;
import com.nuoquan.service.UserService;
import com.nuoquan.utils.JsonUtils;
import com.nuoquan.utils.SpringUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description: 处理消息的handler TextWebSocketFrame：
 *               在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class MsgHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于记录和管理所有客户端的 channel
	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	// handle 无法直接通过 Service 注入数据库，
	// 所以用 SpringUtil 手动获取被 Spring 管理的 bean 对象。
	// (使用普通的java类调用托管给spring的service)
	UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
	ArticleService articleService = (ArticleService) SpringUtil.getBean("articleServiceImpl");

//	WeChatService weChatService = (WeChatService) SpringUtil.getBean("WeChatServiceImpl");

	// 模板id常量
	public static final String MESSAGE = "";
	public static final String COMMENT = "";

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

		String wsContent = msg.text();
		Channel currentChannel = ctx.channel();

		// 1. 获取客户端传过来的消息
		DataContent dataContent = JsonUtils.jsonToPojo(wsContent, DataContent.class);
		Integer action = dataContent.getAction();

		// 2. 判断消息类型
		if (action == MsgActionEnum.CONNECT.type) {
			// 2.1 当 websocket 第一次连接时，初始化Channel，把用的Channel和userid关联起来。
			String senderId = dataContent.getExtend();
			UserChannelRel.put(senderId, currentChannel);

			// 测试
//			for (Channel c : clients) {
//				System.out.println(c.id().asLongText());
//			}
			UserChannelRel.output();
		} else if (action == MsgActionEnum.CHAT.type) {
			// 2.2 聊天类型的消息，把聊天记录保存到数据库（加密/解密），
			// 同时标记签收状态。[未签收]
			@SuppressWarnings("unchecked")
			ChatMessage chatMessage = objectToChatMessage((LinkedHashMap<Object, Object>) dataContent.getData()); // 对象是
			// java.util.LinkedHashMap
			String receiverId = chatMessage.getReceiverId();

			String msgId = userService.saveMsg(chatMessage); // 保存后获取返回的 msgId
			chatMessage.setMsgId(msgId);
			dataContent.setData(chatMessage);
			// 发送消息给对方
			sendMsgTo(receiverId, dataContent);

		} else if (action == MsgActionEnum.SIGNED.type) {
			// 2.3 签收消息类型，修改数据库对应消息的签收状态[已签收]
			// 扩展字段在 signed 类型的消息中，代表需要去签收的消息 id，逗号间隔
			String msgIdsStr = dataContent.getExtend();
			String[] msgIds = msgIdsStr.split(",");

			List<String> msgIdList = new ArrayList<>();
			for (String mid : msgIds) {
				if (StringUtils.isNotBlank(mid)) {
					msgIdList.add(mid);
				}
			}

//			System.out.println("签收消息id：" + msgIdList.toString());

			if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
				// 批量签收
				userService.updateChatSigned(msgIdList);
			}
//		} else if (action == MsgActionEnum.LIKEARTICLE_SIGN.type) {
//			// 2.4 签收点赞文章消息类型，修改数据库对应消息的签收状态[已签收]
//			// 扩展字段在 signed 类型的消息中，代表需要去签收的消息 id，逗号间隔
//			String msgIdsStr = dataContent.getExtend();
//			String[] msgIds = msgIdsStr.split(",");
//
//			List<String> msgIdList = new ArrayList<>();
//			for (String mid : msgIds) {
//				if (StringUtils.isNotBlank(mid)) {
//					msgIdList.add(mid);
//				}
//			}
//
////			System.out.println("签收点赞文章消息id：" + msgIdList.toString());
//
//			if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
//				// 批量签收
//				userService.updateLikeArticleSigned(msgIdList);
//			}
//		} else if (action == MsgActionEnum.LIKECOMMENT_SIGN.type) {
//			// 2.5 签收点赞评论消息类型，修改数据库对应消息的签收状态[已签收]
//			// 扩展字段在 signed 类型的消息中，代表需要去签收的消息 id，逗号间隔
//			String msgIdsStr = dataContent.getExtend();
//			String[] msgIds = msgIdsStr.split(",");
//
//			List<String> msgIdList = new ArrayList<>();
//			for (String mid : msgIds) {
//				if (StringUtils.isNotBlank(mid)) {
//					msgIdList.add(mid);
//				}
//			}
//
////			System.out.println("签收点赞评论消息id：" + msgIdList.toString());
//
//			if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
//				// 批量签收
//				userService.updateLikeCommentSigned(msgIdList);
//			}
//		} else if (action == MsgActionEnum.COMMENT_SIGN.type) {
//			// 2.6 签收评论消息类型，修改数据库对应消息的签收状态[已签收]
//			// 扩展字段在 signed 类型的消息中，代表需要去签收的消息 id，逗号间隔
//			String msgIdsStr = dataContent.getExtend();
//			String[] msgIds = msgIdsStr.split(",");
//
//			List<String> msgIdList = new ArrayList<>();
//			for (String mid : msgIds) {
//				if (StringUtils.isNotBlank(mid)) {
//					msgIdList.add(mid);
//				}
//			}
//
////			System.out.println("签收评论消息id：" + msgIdList.toString());
//
//			if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
//				// 批量签收
//				userService.updateCommentSigned(msgIdList);
//			}
		} else if (action == MsgActionEnum.KEEPALIVE.type) {
			// 2.4 心跳类型消息
//			System.out.println("收到来自 channel 为[" + currentChannel + "]的心跳包...");
		}

		// 把消息发到所有的客户端
//		clients.writeAndFlush(
//				new TextWebSocketFrame("[服务器在" + LocalDateTime.now() 
//										+ "接收到消息]：" + content));
	}

	/**
	 * 当客户端连接服务器之后（打开连接） 获取该客户端对应 channel, 并放到 ChannelGroup 中管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
//		System.out.println("客户端端口，channel id = " + ctx.channel().id());
//		System.out.println("客户端端口，channel 长id = " + ctx.channel().id().asLongText());
//		System.out.println("客户端端口，channel 短id = " + ctx.channel().id().asShortText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 当触发 handlerRemoved 时，ChannelGroup 会自动移除对应客户端的 Channel
		clients.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();

		System.out.println("连接发生异常");
		// 发生异常后关闭连接（Channel），随后从 ChannelGroup 中移除.
		ctx.channel().close();
		clients.remove(ctx.channel());
	}

	/**
	 * 给指定用户发送消息
	 * 
	 * @param receiverId
	 * @param dataContent
	 */
	public static void sendMsgTo(String receiverId, DataContent dataContent) {
		// 从全局用户 Channel 关系中获取接收方的 channel
		Channel receiverChannel = UserChannelRel.get(receiverId);
		if (receiverChannel == null) {
			// TODO 用户离线，推送消息（JPush，个推，小米推送）
		} else {
			// 确认一下 ChannelGroup 中 channel 是否存在
			Channel findChannel = clients.find(receiverChannel.id());
			if (findChannel != null) {
				// 用户在线
				receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
			} else {
				// TODO 用户离线，推送
			}
		}
	}

	public ChatMessage objectToChatMessage(LinkedHashMap<Object, Object> data) {

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSenderId((String) data.get("senderId"));
		chatMessage.setReceiverId((String) data.get("receiverId"));
		chatMessage.setMsg((String) data.get("msg"));
		chatMessage.setMsgId((String) data.get("msgId"));
		Date createDate = new Date((long) data.get("createDate"));
		chatMessage.setCreateDate(createDate);
		return chatMessage;
	}

}
