package com.nuoquan.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class WSServer {
	
	private static class SingletonWSSever { // 单例
		static final WSServer instance = new WSServer();
	}
	
	public static WSServer getInstance() {
		return SingletonWSSever.instance;
	}
	
	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;
	private ServerBootstrap server;
	private ChannelFuture future;
	
	public WSServer() {
		parentGroup = new NioEventLoopGroup();
		childGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		server.group(parentGroup, childGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new WSServerInitializer());
		
	}
	
	public void start() {
		int inetPort = 8088; //正式版端口
//		int inetPort = 8014; //温肯版端口
//		int inetPort = 8099; //测试服端口
		this.future = server.bind(inetPort);
		System.err.println("netty webSocket server 启动完毕，Binding Port："+ inetPort);
	}
	
	// 其实已经把 netty 依托给了 Springboot，此方法无需使用
	public void close() {
		this.parentGroup.shutdownGracefully();
		this.childGroup.shutdownGracefully();
	}
	
//	以下为测试方法。
//	public static void main(String[] args) throws Exception{
//		
//		EventLoopGroup parentGroup = new NioEventLoopGroup();
//		EventLoopGroup childGroup = new NioEventLoopGroup();
//		
//		try {
//			ServerBootstrap server = new ServerBootstrap();
//			server.group(parentGroup, childGroup)
//				.channel(NioServerSocketChannel.class)
//				.childHandler(new WSServerInitializer());
//			
//			ChannelFuture future = server.bind(8088).sync();
//			
//			future.channel().closeFuture().sync();
//		} finally {
//			parentGroup.shutdownGracefully();
//			childGroup.shutdownGracefully();
//		}
//		
//	}
}
