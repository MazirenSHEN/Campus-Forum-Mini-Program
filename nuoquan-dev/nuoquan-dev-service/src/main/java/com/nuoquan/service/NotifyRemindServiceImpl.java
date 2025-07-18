package com.nuoquan.service;


import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.enums.PostType;
import com.nuoquan.enums.SignFlagEnum;
import com.nuoquan.mapper.nq1.NotifyRemindMapper;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.NotifyRemind;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.pojo.vo.NotifyRemindVO;
import com.nuoquan.pojo.vo.UserCommentVO;
import com.nuoquan.pojo.vo.UserVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 通知提醒服务实现类
 * @Author:  Jerrio
 * @Date: 2020.8.28
 */
@Service
public class NotifyRemindServiceImpl implements NotifyRemindService{
	@Autowired
	private Sid sid;
	@Autowired
	private NotifyRemindMapper notifyRemindMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private SocialService socialService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LongarticleService longarticleService;
	@Autowired
	private AuthenticatedUserService authenticatedUserService;
	/**
	 * 插入一条通知
	 * @param senderId		//操作者Id
	 * @param senderAction	//操作者行为
	 * @param sourceId		//动作源对象，如评论内容
	 * @param targetType	//被操作对象类型，如文章，评论，投票
	 * @param targetId		//被操作对象Id
	 * @param recipientId	//消息接收者，可能是对象的所有者或订阅者；
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void insert(String senderId,
							   SenderAction senderAction,
							   String sourceId,
							   PostType targetType,
							   String targetId,
							   String recipientId) {
		String id = sid.nextShort();
		NotifyRemind notifyRemind = new NotifyRemind();
		notifyRemind.setId(id);
		notifyRemind.setSenderId(senderId);
		notifyRemind.setSenderAction(senderAction.value);
		notifyRemind.setSourceId(sourceId);
		notifyRemind.setTargetType(targetType.getValue());
		notifyRemind.setTargetId(targetId);
		notifyRemind.setSignFlag(SignFlagEnum.UNSIGN.getType());
		notifyRemind.setCreateDate(new Date());

		//查询接收者,目前只发送给作者。若有多个接收者可添加订阅表
//		String recipientId = "";
//		switch (targetType) {
//			case COMMENT:
//				recipientId = commentService.getCommentById(targetId,"").getFromUserId();
//				break;
//			case ARTICLE:
//				recipientId = articleService.getArticleById(targetId,"").getUserId();
//				break;
//			case LONGARTICLE:
//				recipientId = longarticleService.getArticleById(targetId,"").getUserId();
//				break;
//			case VOTE:
//				break;
//		}
		notifyRemind.setRecipientId(recipientId);
		notifyRemindMapper.insertSelective(notifyRemind);

		// 推送通知
		NotifyRemindVO notifyRemindVO = composeNotifyVO(notifyRemind);
		DataContent dataContent = new DataContent();
		dataContent.setAction(MsgActionEnum.NOTIFY.type);
		dataContent.setData(notifyRemindVO);
		MsgHandler.sendMsgTo(recipientId,dataContent);
	}

	/**
	 * 批量签收通知
	 *
	 * @param notifyIds
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int batchSign(List<String> notifyIds) {
		Example example = new Example(NotifyRemind.class);
		example.createCriteria().andIn("id", notifyIds);
		NotifyRemind notifyRemind = new NotifyRemind();
		notifyRemind.setSignFlag(SignFlagEnum.SIGNED.getType());
		return notifyRemindMapper.updateByExampleSelective(notifyRemind, example);
	}

	/**
	 * 拼接通知VO对象
	 * @param notifyRemind
	 * @return
	 */
	private NotifyRemindVO composeNotifyVO(NotifyRemind notifyRemind){
		NotifyRemindVO notifyRemindVO = new NotifyRemindVO();
		BeanUtils.copyProperties(notifyRemind,notifyRemindVO);
		//拼接sender头像昵称
		UserVO sender = userService.getUserById(notifyRemindVO.getSenderId());
		notifyRemindVO.setNickname(sender.getNickname());
		notifyRemindVO.setFaceImg(sender.getFaceImg());
		notifyRemindVO.setFaceImgThumb(sender.getFaceImgThumb());
		if (authenticatedUserService.checkUserIsAuth(notifyRemind.getSenderId())){
			AuthenticatedUserVO authenticatedUserVO = authenticatedUserService.getAuthUserByUserId(notifyRemind.getSenderId());
			notifyRemindVO.setAuthType(authenticatedUserVO.getType());
		} else {
			notifyRemindVO.setAuthType(0);
		}
		//拼接源对象，暂时只有评论操作需要源对象
		String senderAction = notifyRemindVO.getSenderAction();
		if (senderAction.equalsIgnoreCase(SenderAction.COMMENT.value)){
			UserCommentVO comment = socialService.getCommentById(notifyRemindVO.getSourceId(),
					notifyRemindVO.getRecipientId());
			notifyRemindVO.setSource(comment);
		}
		//拼接目标对象
		String targetType = notifyRemindVO.getTargetType();
		Object target = null;
		if (targetType.equalsIgnoreCase(PostType.COMMENT.value)){
			target = socialService.getCommentById(notifyRemindVO.getTargetId(),
					notifyRemindVO.getRecipientId());
		}else if (targetType.equalsIgnoreCase(PostType.ARTICLE.value)){
			target = articleService.getArticleById(notifyRemindVO.getTargetId(),
					notifyRemindVO.getRecipientId());
		}else if (targetType.equalsIgnoreCase(PostType.LONGARTICLE.value)){
			target = longarticleService.getArticleById(notifyRemindVO.getTargetId(),
					notifyRemindVO.getRecipientId());
		}
		notifyRemindVO.setTarget(target);
		return notifyRemindVO;
	}

	/**
	 * 获取用户未签收通知
	 *
	 * @param recipientId
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<NotifyRemindVO> getUnsignedNotif(Integer page, Integer pageSize, String recipientId) {
		// 暂不做分页，观察如遇性能问题，可做分页优化 @Jerrio
		Example example = new Example(NotifyRemind.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("recipientId", recipientId);
		criteria.andEqualTo("signFlag", SignFlagEnum.UNSIGN.getType());
		example.orderBy("createDate").desc();
		List<NotifyRemind> list = notifyRemindMapper.selectByExample(example);
		List<NotifyRemindVO> listVO = new ArrayList<>();
		for (NotifyRemind notify : list){
			listVO.add(composeNotifyVO(notify));
		}
		return listVO;
	}
}
