package com.nuoquan.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nuoquan.enums.PostType;
import com.nuoquan.enums.ReputeWeight;
import com.nuoquan.mapper.nq1.*;
import com.nuoquan.pojo.*;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.utils.RedisOperator;
import com.nuoquan.utils.SensitiveFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private Sid sid;
	@Autowired
	public RedisOperator redis;
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private ArticleMapperCustom articleMapperCustom;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SearchRecordMapper searchRecordMapper;
	@Autowired
	private ArticleImageMapper articleImageMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFansMapper userFansMapper;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private SocialService socialService;
	@Autowired
	private SensitiveFilterUtil sensitiveFilterUtil;
	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	@Transactional(propagation = Propagation.SUPPORTS)
	public ArticleVO addArticleImgs(ArticleVO articleVO) {
		List<ArticleImage> images = articleImageMapper.getArticleImgs(articleVO.getId());
		// 为图片url添加前缀
		for (ArticleImage image : images) {
			image.setImagePath(resourceService.composeUrl(image.getImagePath()));
		}
		// 添加图片列表
		articleVO.setImgList(images);
		return articleVO;
	}

	/**
	 * 组装文章VO对象（注意：后台调用composeVO时userId为空）
	 * 
	 * @param articleVO
	 * @param userId    操作者（我）
	 * @return
	 */
	public ArticleVO composeArticleVO(ArticleVO articleVO, String userId) {
		// 组合作者头像url
		UserVO userVO = userService.getUserById(articleVO.getUserId());
		articleVO.setNickname(userVO.getNickname());
		articleVO.setFaceImg(userVO.getFaceImg());
		articleVO.setFaceImgThumb(userVO.getFaceImgThumb());
		if (authenticatedUserService.checkUserIsAuth(articleVO.getUserId())) {
			AuthenticatedUserVO authenticatedUserVO = authenticatedUserService
					.getAuthUserByUserId(articleVO.getUserId());
			articleVO.setAuthType(authenticatedUserVO.getType());
		} else {
			articleVO.setAuthType(0);
		}
		// 添加图片列表
		articleVO = addArticleImgs(articleVO);
		if (StringUtils.isNotBlank(userId)) {
			// 添加和关于用户的点赞关系
			articleVO.setIsLike(socialService.isUserLike(userId, PostType.ARTICLE, articleVO.getId()));
			// 添加和关于用户的收藏关系
			articleVO.setIsCollect(socialService.isUserCollect(userId, PostType.ARTICLE, articleVO.getId()));
		}
		// 添加标签列表
		if (!StringUtils.isBlank(articleVO.getTags())) {
			String[] tagList = articleVO.getTags().split("#");
			List<String> finalTagList = new ArrayList<String>();
			for (String tag : tagList) {
				if (!StringUtils.isBlank(tag)) {
					finalTagList.add(tag);
				}
			}
			articleVO.setTagList(finalTagList);
		}

		// 检查是否有屏蔽词并替换
		articleVO.setArticleContent(sensitiveFilterUtil.filter(articleVO.getArticleContent()));
		articleVO.setArticleTitle(sensitiveFilterUtil.filter(articleVO.getArticleTitle()));

		return articleVO;
	}

	/**
	 * 把 Article 转换为 ArticleVO, 组装文章VO对象
	 * 
	 * @param article
	 * @param userId  操作者（我）
	 * @return
	 */
	public ArticleVO composeArticleVO(Article article, String userId) {
		ArticleVO articleVO = new ArticleVO();
		BeanUtils.copyProperties(article, articleVO);
		return composeArticleVO(articleVO, userId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult list(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		Example articleExample = new Example(Article.class);
		articleExample.setOrderByClause("create_date desc");
		Criteria criteria = articleExample.createCriteria();
		criteria.andNotEqualTo("status", 0);
		List<Article> list = articleMapper.selectByExample(articleExample);
		List<ArticleVO> newList = new ArrayList<ArticleVO>();
		for (Article a : list) {
			ArticleVO articleVO = composeArticleVO(a, "");
			newList.add(articleVO);
		}

		PageInfo<ArticleVO> pageList = new PageInfo<>(newList);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(newList);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult listCheckOnly(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);
		Example example = new Example(Article.class);
		example.setOrderByClause("create_date desc");
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("status", 2);
		List<Article> list = articleMapper.selectByExample(example);
		List<ArticleVO> newList = new ArrayList<ArticleVO>();
		for (Article a : list) {
			ArticleVO articleVO = composeArticleVO(a, "");
			newList.add(articleVO);
		}

		PageInfo<ArticleVO> pageList = new PageInfo<>(newList);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(newList);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	@Deprecated
	public PagedResult getAllArticles(Integer page, Integer pageSize, String userId) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.queryAllArticles();
		for (ArticleVO a : list) {
			a = composeArticleVO(a, userId);
		}

		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ArticleVO getArticleById(String articleId, String userId) {
		Article article = articleMapper.selectByPrimaryKey(articleId);
		ArticleVO articleVO = composeArticleVO(article, userId);
		if (StringUtils.isNotBlank(userId) && !userId.equals("AdminUser") && !userId.equals(article.getUserId())) {
			socialService.addViewCount(userId, PostType.ARTICLE, articleVO.getId());
		}
		return articleVO;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryCollectArticle(Integer page, Integer pageSize, String userId, String targetId) {
		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.queryCollectArticle(targetId);
		for (ArticleVO a : list) {
			a = composeArticleVO(a, userId);
		}

		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult searchArticleYang(Integer isSaveRecord, Integer page, Integer pageSize,
			String searchText, String userId) {

		String[] texts = searchText.split(" ");
		// 保存热搜词
		if (isSaveRecord != null && isSaveRecord == 1) {
			for (String text : texts) {
				SearchRecord record = new SearchRecord();
				String recordId = sid.nextShort();
				record.setId(recordId);
				record.setContent(text);
				searchRecordMapper.insert(record);
			}
		}

		// 开启分页查询并转换为vo对象
		// 在Example中的每一个Criteria相当于一个括号，把里面的内容当成一个整体
		Example articleExample = new Example(Article.class);
		articleExample.setOrderByClause("create_date desc");
		Criteria criteria = articleExample.createCriteria();
		for (String text : texts) {
			criteria.orLike("articleTitle", "%" + text + "%");
		}

		Criteria criteria2 = articleExample.createCriteria();
		for (String text : texts) {
			criteria.orLike("articleContent", "%" + text + "%");
			criteria.orLike("tags", "%#" + text + "%");
		}

		Criteria criteria3 = articleExample.createCriteria();
		criteria3.andEqualTo("status", StatusEnum.READABLE.type);
		articleExample.and(criteria2);
		articleExample.and(criteria3);

		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapper.selectByExample(articleExample);
		PageInfo<Article> pageInfo = new PageInfo<>(list);
		PageInfo<ArticleVO> pageInfoVo = PageUtils.PageInfo2PageInfoVo(pageInfo);

		List<ArticleVO> listVo = new ArrayList<>();
		for (Article a : list) {
			ArticleVO av = new ArticleVO();
			BeanUtils.copyProperties(a, av); // 转换对象
			// 添加作者信息
			User user = userMapper.selectByPrimaryKey(av.getUserId());
			if (user != null) {
				av.setNickname(user.getNickname());
				av.setFaceImg(user.getFaceImg());
				av.setFaceImgThumb(user.getFaceImgThumb());
			}
			av = composeArticleVO(av, userId);
			listVo.add(av);
		}
		pageInfoVo.setList(listVo);

		// 为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVo.getPageNum());
		pagedResult.setTotal(pageInfoVo.getPages());
		pagedResult.setRows(pageInfoVo.getList());
		pagedResult.setRecords(pageInfoVo.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {

		return searchRecordMapper.getHotWords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveArticle(Article article) {
		String id = sid.nextShort();
		article.setId(id);
		articleMapper.insertSelective(article);

		return id;
	}

	// @Transactional(propagation = Propagation.REQUIRED)
	// @Override
	// public void deleteArticle(String articleId, String userId) {
	// // 1. 删除文章图片数据库路径
	// // 1.1

	// // 2. 删除文章评论
	// // 2.1 删除文章评论的点赞
	// // 3. 删除文章的点赞
	// // 4. 删除文章
	//
	// // 删除文章图片
	// String fileSpace = "/Users/xudeyan/Desktop/JumboX/nuoquanTmp";
	// String uploadDB = "/" + userId + "/article" + "/" + articleId;
	// String path = fileSpace + uploadDB;
	// String path1 =
	// "/Users/xudeyan/Desktop/JUMBOX/nuoquanTmp/oDwsO5Btm6HsoGie6E8qB7WN9aYQ/article/191003BSB10GYWM8/0.jpg";
	// File file = new File(path1);
	// deletefile(file);
	//
	// // 删除数据库中的路径
	// Example exampleDelImg = new Example(ArticleImage.class);
	// Criteria criteria1 = exampleDelImg.createCriteria();
	// criteria1.andEqualTo("articleId", articleId);
	// articleImageMapper.deleteByExample(exampleDelImg);
	//
	//// System.out.println(articleId);
	//
	// // 删除文章评论的点赞
	// // 先在userArticleComment里找到articleId对应的评论id, 该id为userLikeComment中的commentId
	// Example exampleToFindCommentId = new Example(UserArticleComment.class);
	// Criteria criteriaToFindCommentId = exampleToFindCommentId.createCriteria();
	// criteriaToFindCommentId.andEqualTo("articleId", articleId);
	// List<UserArticleComment> list =
	// userArticleCommentMapper.selectByExample(exampleToFindCommentId);
	// for (UserArticleComment c : list) {
	// // 得到commentId
	// String commentId = c.getId();
	// // 在userLikeComment中查询
	// Example exampleToDelCommentLikeExample = new Example(UserLikeComment.class);
	// Criteria criteria = exampleToDelCommentLikeExample.createCriteria();
	// criteria.andEqualTo("commentId", commentId);
	// userLikeCommentMapper.deleteByExample(exampleToDelCommentLikeExample);
	// }
	// // 删除目标文章所有评论
	// Example exampleDelComment = new Example(UserArticleComment.class);
	// Criteria criteria2 = exampleDelComment.createCriteria();
	// criteria2.andEqualTo("articleId", articleId);
	// userArticleCommentMapper.deleteByExample(exampleToFindCommentId);
	//
	// // 删除文章的点赞
	// Example exampleDelArticleLike = new Example(UserLikeArticle.class);
	// Criteria criteria4 = exampleDelArticleLike.createCriteria();
	// criteria4.andEqualTo("articleId", articleId);
	// userLikeArticleMapper.deleteByExample(exampleDelArticleLike);
	//
	// // 删除文章
	// Example exampleDelArticle = new Example(Article.class);
	// Criteria criteria3 = exampleDelArticle.createCriteria();
	// criteria3.andEqualTo("id", articleId);
	// articleMapper.deleteByExample(exampleDelArticle);
	// }

	// private static void deletefile(File file) {
	// // TODO Auto-generated method stub
	// if(file.isDirectory()) {
	// File[] files = file.listFiles();
	// for (File key : files) {
	// if (key.isFile()) {
	// key.delete();
	// } else {
	// deletefile(key);
	// }
	// }
	// }
	// file.delete();
	// }

	private void deletefile(File file) {

		System.out.println(file.getAbsolutePath());
		if (file.isFile()) {
			// 判断是否为文件--Y
			// System.out.println(file.getAbsolutePath());
			// file.setExecutable(true);
			file.setWritable(true);
			// file.exists();
			// file.canExecute();
			// file.canWrite();
			// file.getParent();
			boolean t = file.delete();
			System.out.println(t);
			System.out.println("exist? " + file.exists());
			System.out.println("canExecute? " + file.canExecute());
			System.out.println("canWrite? " + file.canWrite());
			System.out.println("parent is " + file.getParent());
		} else {
			String[] childFilePathStrings = file.list();

			System.out.println(childFilePathStrings[1]);
			System.out.println(childFilePathStrings.length);

			for (String path : childFilePathStrings) {
				System.out.println(path);
				File childFile = new File(file.getAbsoluteFile() + "/" + path);
				deletefile(childFile);
			}
			System.out.println(file.getAbsolutePath());
			file.delete();
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveArticleImages(ArticleImage articleImage) {

		String id = sid.nextShort();
		articleImage.setId(id);
		articleImageMapper.insertSelective(articleImage);
	}

	/**
	 * 根据公式更新文章热度
	 * 当前公式：24小时内的点赞数和评论数之和
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updatePopByFunction() {
		// TODO:测试java代码实现和sql语句实现更新效率对比
		// Example likeExample = new Example(UserLike.class);
		// Criteria likeCriteria = likeExample.createCriteria();
		// likeCriteria.andGreaterThan();
		// userLikeMapper
		articleMapperCustom.updatePopByFunction();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getArticleByPopularity(Integer page, Integer pageSize, String userId) {
		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.getArticleByPopularity();
		for (ArticleVO a : list) {
			a = composeArticleVO(a, userId);
		}
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void fDeleteArticle(String articleId) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);
		Article a = new Article();
		a.setStatus(StatusEnum.DELETED.type);
		articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllMyHisArticle(Integer page, Integer pageSize, String userId) {

		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.queryAllMyHisArticle(userId);
		for (ArticleVO a : list) {
			a = composeArticleVO(a, userId);
		}
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getOthersLegalHisArticle(Integer page, Integer pageSize, String userId, String targetId) {

		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.queryOthersLegalHisArticle(targetId);
		for (ArticleVO a : list) {
			a = composeArticleVO(a, userId);
		}
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Deprecated
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void addViewCount(String articleId, String userId) {
		String key = "VIEW_COUNT:" + userId + articleId;
		String value = redis.get(key);
		if (StringUtils.isBlank(value)) {
			redis.set(key, "ture", 7200); // 两小时内不重复计算浏览量
			articleMapperCustom.addViewCount(articleId);
			// 作者影响力++
			String author = getArticleById(articleId, null).getUserId();
			userService.updateReputation(author, ReputeWeight.VIEW.weight, 1);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int updateArticleStatus(String articleIds, int status) {
		if (StringUtils.isEmpty(articleIds)) {
			return -1;
		}

		List<String> listId = Convert.toListStrArray(articleIds);

		Example example = new Example(Article.class);
		example.createCriteria().andIn("id", listId);
		Article a = new Article();
		a.setStatus(status);
		return articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public PagedResult queryArticleByExample(Example articleExample, String userId) {
		// 通过条件，返回pagedResult
		List<Article> list = articleMapper.selectByExample(articleExample);
		PageInfo<Article> pageInfo = new PageInfo<>(list);
		PageInfo<ArticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);

		List<ArticleVO> listVO = new ArrayList<>();
		for (Article a : list) {
			listVO.add(composeArticleVO(a, userId));
		}
		pageInfoVO.setList(listVO);

		// 为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVO.getPageNum());
		pagedResult.setTotal(pageInfoVO.getPages());
		pagedResult.setRows(pageInfoVO.getList());
		pagedResult.setRecords(pageInfoVO.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryArticle(Integer page, Integer pageSize, Integer queryType, String userId,
			String selectedTag) {
		Example articleExample = new Example(Article.class);
		// 是否关注的, queryType: 0为全部，1为关注
		// 查询全部我(操作者)关注的用户
		if (queryType == 1) {
			Example example = new Example(UserFans.class);
			Criteria criteria = example.createCriteria();
			// 此处userId为操作者id
			criteria.andEqualTo("fansId", userId);
			List<UserFans> userList = userFansMapper.selectByExample(example);
			if (userList.size() == 0 || userList == null) {
				return null;
			}
			// 查找 我关注的人的
			Criteria followCriteria = articleExample.createCriteria();
			for (UserFans userFans : userList) {
				// System.out.println(userFans.getUserId());
				followCriteria.orEqualTo("userId", userFans.getUserId());
			}
			articleExample.and(followCriteria);
		}
		// 是否有标签
		if (!StringUtils.isBlank(selectedTag)) {
			// 在这些文章中找到符合搜索的标签的文章
			String[] texts = selectedTag.split(" ");
			Criteria tagCriteria = articleExample.createCriteria();
			for (String text : texts) {
				tagCriteria.orLike("tags", "%" + text + "%");
			}
			articleExample.and(tagCriteria);
		}
		// 在这些文章中找到状态为可读的文章
		Criteria statusCriteria = articleExample.createCriteria();
		statusCriteria.andEqualTo("status", StatusEnum.READABLE.type);
		articleExample.and(statusCriteria);
		articleExample.setOrderByClause("create_date desc");

		PageHelper.startPage(page, pageSize);
		return queryArticleByExample(articleExample, userId);
	}

}
