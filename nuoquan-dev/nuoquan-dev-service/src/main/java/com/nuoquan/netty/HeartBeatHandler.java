package com.nuoquan.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Description: 用于检测 channel 的心跳 handle
 * 继承 ChannelInboundHandlerAdapter 从而不需要实现 channelRead0 方法
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 用户事件触发
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		// 判断 evt 是否是 IdleStateEvent（用于触发用户事件，包括读空闲/写空闲/读写空闲）
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent)evt; //强制类型转换
			
			if(event.state() == IdleState.ALL_IDLE) {
				Channel channel = ctx.channel();
				// 关闭无用的 channel 以防资源浪费
				channel.close();
				System.out.println("Channel 关闭后，clients 的数量为 " + MsgHandler.clients.size());
			}
		}
	}
	
}
