package com.nuoquan.service;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.PostType;
import com.nuoquan.mapper.nq1.*;
import com.nuoquan.pojo.*;
import com.nuoquan.pojo.vo.*;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminUserMapper adminUserMapper;

	@Autowired
	private AdminNoticeMapper adminNoticeMapper;

	@Autowired
	private AdminUserNoticeMapper adminUserNoticeMapper;

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private UserCommentMapper userCommentMapper;

	@Autowired
	private LongarticleMapper longarticleMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private SocialServiceImpl socialService;

	@Autowired
	private ArticleServiceImpl articleService;

	@Autowired
	private LongarticleServiceImpl longarticleService;

	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	// 查询全部被举报的发布的东西这里很冗长很不优雅，实在是尽力了，后辈们加油！
	private PagedResult getReportedComment(Integer page, Integer pageSize, String userId, Integer queryType) {
		PageHelper.startPage(page, pageSize);

		List<UserComment> list = null;
		if (queryType == 0){
			list = userCommentMapper.queryReportedCommentByCreateTime();
		}
		else if (queryType == 1){
			list = userCommentMapper.queryReportedCommentByReportedNum();
		}

		//TODO:是否需要抛出异常如果list为null
		PageInfo<UserComment> pageInfo = new PageInfo<>(list);
		PageInfo<UserCommentVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
		List<UserCommentVO> listVO = new ArrayList<>();
		for (UserComment c : list) {
			listVO.add(socialService.composeComment(c.getFromUserId(), c));
		}
		pageInfoVO.setList(listVO);

		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVO.getPageNum());
		pagedResult.setTotal(pageInfoVO.getPages());
		pagedResult.setRows(pageInfoVO.getList());
		pagedResult.setRecords(pageInfoVO.getTotal());
		return pagedResult;
	}

	private PagedResult getReportedArticle(Integer page, Integer pageSize, String userId, Integer queryType) {
		PageHelper.startPage(page, pageSize);

		List<Article> list = null;
		if (queryType == 0){
			list = articleMapper.queryReportedArticleByCreateTime();
		}
		else if (queryType == 1){
			list = articleMapper.queryReportedArticleByReportedNum();
		}

		//TODO:是否需要抛出异常如果list为null
		PageInfo<Article> pageInfo = new PageInfo<>(list);
		PageInfo<ArticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
		List<ArticleVO> listVO = new ArrayList<>();
		for (Article c : list) {
			listVO.add(articleService.composeArticleVO(c, c.getUserId()));
		}
		pageInfoVO.setList(listVO);

		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVO.getPageNum());
		pagedResult.setTotal(pageInfoVO.getPages());
		pagedResult.setRows(pageInfoVO.getList());
		pagedResult.setRecords(pageInfoVO.getTotal());
		return pagedResult;
	}

	private PagedResult getReportedLongArticle(Integer page, Integer pageSize, String userId, Integer queryType) {
		PageHelper.startPage(page, pageSize);

		List<Longarticle> list = null;
		if (queryType == 0){
			list = longarticleMapper.queryReportedLongArticleByCreatTime();
		}
		else if (queryType == 1){
			list = longarticleMapper.queryReportedLongArticleByReportedNum();
		}

		//TODO:是否需要抛出异常如果list为null
		PageInfo<Longarticle> pageInfo = new PageInfo<>(list);
		PageInfo<LongarticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
		List<LongarticleVO> listVO = new ArrayList<>();
		for (Longarticle c : list) {
			listVO.add(longarticleService.composeArticleVO(c, c.getUserId()));
		}
		pageInfoVO.setList(listVO);

		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVO.getPageNum());
		pagedResult.setTotal(pageInfoVO.getPages());
		pagedResult.setRows(pageInfoVO.getList());
		pagedResult.setRecords(pageInfoVO.getTotal());
		return pagedResult;
	}

	private PagedResult judgeTargetType(Integer page, Integer pageSize, String userId, PostType targetType, Integer queryType) {
		switch (targetType) {
			case COMMENT:
				return getReportedComment(page, pageSize, userId, queryType);
			case ARTICLE:
				return getReportedArticle(page, pageSize, userId, queryType);
			case LONGARTICLE:
				return getReportedLongArticle(page, pageSize, userId, queryType);
		}
		return null;
	}

	private ArticleVO composeArticleVOAdmin(ArticleVO articleVO, String userId) {
		// 组合作者头像url
		UserVO userVO = userService.getUserById(articleVO.getUserId());
		articleVO.setNickname(userVO.getNickname());
		articleVO.setFaceImg(userVO.getFaceImg());
		articleVO.setFaceImgThumb(userVO.getFaceImgThumb());
		if (authenticatedUserService.checkUserIsAuth(articleVO.getUserId())){
			AuthenticatedUserVO authenticatedUserVO = authenticatedUserService.getAuthUserByUserId(articleVO.getUserId());
			articleVO.setAuthType(authenticatedUserVO.getType());
		} else {
			articleVO.setAuthType(0);
		}
		// 添加图片列表
		articleVO = articleService.addArticleImgs(articleVO);
		// 添加和关于用户的点赞关系
		articleVO.setIsLike(socialService.isUserLike(userId, PostType.ARTICLE, articleVO.getId()));
		// 添加和关于用户的收藏关系
		articleVO.setIsCollect(socialService.isUserCollect(userId, PostType.ARTICLE, articleVO.getId()));
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

		return articleVO;
	}

	/**
	 * 把 Article 转换为 ArticleVO, 组装文章VO对象
	 *
	 * @param article
	 * @param userId  操作者（我）
	 * @return
	 */
	private ArticleVO composeArticleVOAdmin(Article article, String userId) {
		ArticleVO articleVO = new ArticleVO();
		BeanUtils.copyProperties(article, articleVO);
		return composeArticleVOAdmin(articleVO, userId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public AdminUser queryAdminUserName(String username) {
		Example example = new Example(AdminUser.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", username);

		List<AdminUser> result = adminUserMapper.selectByExample(example);
		if (result.size() >= 0) {
			return result.get(0); // Return the first one, although it should have only one.
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<AdminNotice> getUserNotice(AdminUser adminUser, int state) {
		List<AdminNotice> notices = new ArrayList<>();
		//查询公告用户外键
		Example userNoticeExample = new Example(AdminUserNotice.class);
		Criteria criteria = userNoticeExample.createCriteria();
		criteria.andEqualTo("userId", adminUser.getId());
		if (-1 != state) {
			criteria.andEqualTo("state", state);
		}
		List<AdminUserNotice> userNotices = adminUserNoticeMapper.selectByExample(userNoticeExample);
		if (userNotices != null && userNotices.size() > 0) {
			//查询对应的公告列表
			List<String> ids = new ArrayList<String>();
			for (AdminUserNotice userNotice : userNotices) {
				ids.add(userNotice.getNoticeId());
			}
			Example noticeExample = new Example(AdminNotice.class);
			noticeExample.createCriteria().andEqualTo("id", ids);
			notices = adminNoticeMapper.selectByExample(noticeExample);
		}
		return notices;
	}

	@Override
	public ArticleVO getArticleByIdAdmin(String articleId, String userId) {
		Article article = articleMapper.selectByPrimaryKey(articleId);
		ArticleVO articleVO = composeArticleVOAdmin(article, userId);
		return articleVO;
	}

	@Override
	public PagedResult getReportedPublished(Integer page, Integer pageSize, String userId, PostType targetType, Integer queryType) {
		// queryType: 0为按创建时间排序，1为按被举报数量排序
		if (queryType == 0) {
			return judgeTargetType(page, pageSize, userId, targetType, queryType);
		} else if (queryType == 1) {
			return judgeTargetType(page, pageSize, userId, targetType, queryType);
		}
		return null;
	}




}
