package com.nuoquan;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Description: 继承 SpringBootServletInitializer , 相当于使用 web.xml 的形式去启动部署
 */
public class WarStartApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 使用 web.xml 运行应用程序，指向 Application，最后启动 springboot
		return builder.sources(Application.class);
	}
	
}
