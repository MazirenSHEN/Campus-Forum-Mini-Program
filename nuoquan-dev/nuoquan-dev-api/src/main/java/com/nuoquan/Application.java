package com.nuoquan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.nuoquan.config.COSProperties;
import com.nuoquan.utils.SpringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //取消加载数据源自动配置
//@MapperScan(basePackages="com.nuoquan.mapper")
@ComponentScan(basePackages= {"com.nuoquan", "com.jupiter", "org.n3r.idworker"})
public class Application {
	
	@Autowired
	private COSProperties cosProperties;
	
	@Bean
	public SpringUtil springUtil() {
		return new SpringUtil();
	}
	
	@Bean
	/**
	 * 生成腾讯COS对象储存客户端Bean
	 */
	public COSClient cosClient() {
		// 1 初始化用户身份信息（secretId, secretKey）。
		COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
		// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		Region region = new Region(cosProperties.getRegion());
		ClientConfig clientConfig = new ClientConfig(region);
		// 3 生成 cos 客户端。
		System.out.println("生成 cos 客户端");
		return new COSClient(cred, clientConfig);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
