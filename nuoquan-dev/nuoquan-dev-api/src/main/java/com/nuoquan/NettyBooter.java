package com.nuoquan;

import com.nuoquan.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 启动 netty 服务
 * @author jerrio
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			try {
				WSServer.getInstance().start();
				com.nuoquan.netty.WSServer.getInstance().start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
