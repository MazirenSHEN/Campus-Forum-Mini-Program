package com.nuoquan.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nuoquan.controller.ArticleController;
import com.nuoquan.service.ArticleService;
import com.nuoquan.service.ResourceService;

/**
 * 测试类基础模板，供同层其他测试类引用
 * @author jerrio
 * @since v1.2.0
 * @Date 2020/4/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class BasicTest {
	@Autowired
	public ArticleController articleController;
	@Autowired
	public ArticleService articleService;
	@Autowired
	public ResourceService resourceService;
//	@Before
//	public void init() {
//		System.out.println("开始测试-----------------");
//	}
//	
//	@Test
//	public void sampleTest() throws Exception {
//		System.out.println("示例测试通过-----------------");
//	}
//	
//	@After
//  public void after() {
//        System.out.println("测试结束-----------------");
//	}
	
}
