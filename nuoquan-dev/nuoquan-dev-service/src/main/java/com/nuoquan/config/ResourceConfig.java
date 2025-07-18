package com.nuoquan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description: 连接 properties 的类
 * @Author: jerrio
 * @Date: 
 */
@Configuration
@ConfigurationProperties(prefix="com.jumbox")
//@PropertySource(value="classpath:resource-dev.properties") // 默认是application.properties; 用默认的就好了，这里留着提醒可以分开写。
public class ResourceConfig {

	public class SslConfig {
		private String type;
		
		private String password;
		
		private String path;
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}
	
	private SslConfig ssl = new SslConfig();
	
	private String name;
	
	private String version;
	
	private String copyrightYear;
	
	private String fileSpace;

	private String demoEnabled;
	
	private Boolean rollVerification;
	
	private Boolean autoCheckArticle;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCopyrightYear() {
		return copyrightYear;
	}

	public void setCopyrightYear(String copyrightYear) {
		this.copyrightYear = copyrightYear;
	}

	public String getFileSpace() {
		return fileSpace;
	}

	public void setFileSpace(String fileSpace) {
		this.fileSpace = fileSpace;
	}

	public String getDemoEnabled() {
		return demoEnabled;
	}

	public void setDemoEnabled(String demoEnabled) {
		this.demoEnabled = demoEnabled;
	}

	public Boolean getRollVerification() {
		return rollVerification;
	}

	public void setRollVerification(Boolean rollVerification) {
		this.rollVerification = rollVerification;
	}

	public Boolean getAutoCheckArticle() {
		return autoCheckArticle;
	}

	public void setAutoCheckArticle(Boolean autoCheckArticle) {
		this.autoCheckArticle = autoCheckArticle;
	}

	public SslConfig getSsl() {
		return ssl;
	}

	public void setSsl(SslConfig ssl) {
		this.ssl = ssl;
	}
}
