package com.nuoquan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

import com.nuoquan.config.ResourceConfig;
import com.nuoquan.controller.interceptor.MyInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	
	@Autowired
	private ResourceConfig resourceconfig;

	// 设置虚拟路径
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/META-INF/resources/") // 解决swagger无法访问
				.addResourceLocations("classpath:/META-INF/resources/webjars/") // 解决swagger的js文件无法访问
				.addResourceLocations("file:"+resourceconfig.getFileSpace()+"/");
	}
	
	// 注册 MiniInterceptor
	@Bean
	public MyInterceptor myInterceptor() {
		return new MyInterceptor();
	}
//	@Bean
//	public UploadInterceptor uploadInterceptor() {
//		return new UploadInterceptor();
//	}
//
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
		
		super.addInterceptors(registry);
	}
	
}
