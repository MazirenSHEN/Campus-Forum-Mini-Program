package com.nuoquan.controllerTest;

import com.nuoquan.test.BasicTest;

/**
 * articleController 的单元测试类，检测接口功能是否正常
 * @author jerrio
 *
 */
public class articleControllerTest extends BasicTest{

//	@Test
//	@Transactional //不污染数据库
//	public void userCollectArticle() throws Exception {
//		String test_userId = "test1";
//		String test_articleId = "1912128SBG07Z9P0";
//		articleController.userCollectArticle(test_userId, test_articleId, null);
//		Assert.assertEquals("用户收藏文章出错",true, articleService.isUserCollectArticle(test_userId, test_articleId));
//
//		articleController.userUncollectArticle(test_userId, test_articleId, null);
//		Assert.assertEquals("用户取消收藏文章出错",false, articleService.isUserCollectArticle(test_userId, test_articleId));
//	}
//
//	@Test
//	public void queryCollectArticle() {
//		String test_userId = "test1";
//		String test_targetId = "test1";
//		JSONResult result= articleController.queryCollectArticle(1, 3, test_userId, test_targetId);
//		@SuppressWarnings("unchecked")
//		Page<ArticleVO> data = (Page<ArticleVO>)((PagedResult) result.getData()).getRows();
//		for(ArticleVO articleVO : data.getResult()) {
//			Assert.assertEquals("收藏列表中含有未收藏文章", true, articleVO.getIsCollect());
//		}
//	}
	
//	@Test
//	public void cosClientTest() {
//		System.out.println("开始COS测试...");
//		resourceService.cosClientTest();
//		System.out.println("COS测试通过");
//	}
}
