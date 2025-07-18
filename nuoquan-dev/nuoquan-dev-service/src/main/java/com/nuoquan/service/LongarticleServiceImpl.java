package com.nuoquan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nuoquan.enums.PostType;
import com.nuoquan.mapper.nq1.*;
import com.nuoquan.pojo.*;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.pojo.vo.UserVO;
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
import com.nuoquan.pojo.vo.LongarticleVO;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class LongarticleServiceImpl implements LongarticleService {
	
	@Autowired
	private Sid sid;
	@Autowired
	private LongarticleMapper longarticleMapper;
	@Autowired
	private UserMapper userMapper;
	//搜索标签的文章标签的mapper 应重写个搜索长文章的标签的
	@Autowired
	private SearchRecordMapper searchRecordMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFansMapper userFansMapper;
	@Autowired
	private SocialService socialService;
	@Autowired
	private OfficialAccountMapper officialAccountMapper;
	@Autowired
	private HeadlinesMapper headlinesMapper;
	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	/**
	 * 组装文章VO对象
	 * @param longarticleVO
	 * @param userId 操作者（我）
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public LongarticleVO composeArticleVO(LongarticleVO longarticleVO, String userId) {
		// 组合作者头像url
		UserVO userVO = userService.getUserById(longarticleVO.getUserId());
		longarticleVO.setNickname(userVO.getNickname());
		longarticleVO.setFaceImg(userVO.getFaceImg());
		longarticleVO.setFaceImgThumb(userVO.getFaceImgThumb());
		if (authenticatedUserService.checkUserIsAuth(longarticleVO.getUserId())){
			AuthenticatedUserVO authenticatedUserVO = authenticatedUserService.getAuthUserByUserId(longarticleVO.getUserId());
			longarticleVO.setAuthType(authenticatedUserVO.getType());
		} else {
			longarticleVO.setAuthType(0);
		}
		// 添加和关于用户的点赞关系
		longarticleVO.setIsLike(socialService.isUserLike(userId, PostType.LONGARTICLE, longarticleVO.getId()));
		// 添加和关于用户的收藏关系
		longarticleVO.setIsCollect(socialService.isUserCollect(userId, PostType.LONGARTICLE, longarticleVO.getId()));
		// 添加标签列表
		if (!StringUtils.isBlank(longarticleVO.getTags())) {
			String[] tagList = longarticleVO.getTags().split("#");
			List<String> finalTagList = new ArrayList<String>();
			for (String tag : tagList) {
				if (!StringUtils.isBlank(tag)) {
					finalTagList.add(tag);
				}
			}
			longarticleVO.setTagList(finalTagList);
		}

		socialService.addViewCount(userId, PostType.LONGARTICLE, longarticleVO.getId());
		return longarticleVO;
	}

	/**
	 * 组装文章VO对象
	 * @param article
	 * @param userId 操作者（我）
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public LongarticleVO composeArticleVO(Longarticle article, String userId) {
		LongarticleVO articleVO = new LongarticleVO();
		BeanUtils.copyProperties(article, articleVO);
		return composeArticleVO(articleVO, userId);
	}
	
	@Override
	public PagedResult list(Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult listCheckOnly(Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public LongarticleVO getArticleById(String articleId, String userId) {
		//需要在service层查询 之后要改
		Longarticle longarticle = longarticleMapper.selectByPrimaryKey(articleId);
		return composeArticleVO(longarticle, userId);
	}

	@Override
	public String saveArticle(Longarticle article) {
		String id = sid.nextShort();
		article.setId(id);
		longarticleMapper.insertSelective(article);

		return id;
	}

	@Override
	public String saveArticle(String title,
							  String subTitle,
							  String coverImage,
							  String content,
							  String userId,
							  String tags,
							  StatusEnum status) {
		String id = sid.nextShort();

		Longarticle article = new Longarticle();
		article.setId(id);
		article.setTitle(title);
		article.setSubTitle(subTitle);
		article.setCoverImage(coverImage);
		article.setContent(content);
		article.setUserId(userId);
		article.setTags(tags);
		article.setStatus(status.type);
		article.setCreateDate(new Date());
		longarticleMapper.insertSelective(article);

		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteArticle(String articleId, String userId) {
		// 删除文章评论的点赞
		/* 评论的部分先搁置
		 * 
		// 先在userArticleComment里找到articleId对应的评论id, 该id为userLikeComment中的commentId
		Example exampleToFindCommentId = new Example(UserArticleComment.class);
		Criteria criteriaToFindCommentId = exampleToFindCommentId.createCriteria();
		criteriaToFindCommentId.andEqualTo("articleId", articleId);
		List<UserArticleComment> list = userArticleCommentMapper.selectByExample(exampleToFindCommentId);
		for (UserArticleComment c : list) {
			// 得到commentId
			String commentId = c.getId();
			// 在userLikeComment中查询
			Example exampleToDelCommentLikeExample = new Example(UserLikeComment.class);
			Criteria criteria = exampleToDelCommentLikeExample.createCriteria();
			criteria.andEqualTo("commentId", commentId);
			userLikeCommentMapper.deleteByExample(exampleToDelCommentLikeExample);
		}
		// 删除目标文章所有评论
		Example exampleDelComment = new Example(UserArticleComment.class);
		Criteria criteria2 = exampleDelComment.createCriteria();
		criteria2.andEqualTo("articleId", articleId);
		userArticleCommentMapper.deleteByExample(exampleToFindCommentId);

		// 删除文章的点赞
		Example exampleDelArticleLike = new Example(UserLikeArticle.class);
		Criteria criteria4 = exampleDelArticleLike.createCriteria();
		criteria4.andEqualTo("articleId", articleId);
		userLikeArticleMapper.deleteByExample(exampleDelArticleLike);
		*/
		// 删除文章
		Example exampleDelArticle = new Example(Longarticle.class);
		Criteria criteria3 = exampleDelArticle.createCriteria();
		criteria3.andEqualTo("id", articleId);
		longarticleMapper.deleteByExample(exampleDelArticle);
		
	}

	@Override
	public void updatePopByFunction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PagedResult getArticleByPopularity(Integer page, Integer pageSize, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllMyHis(Integer page, Integer pageSize, String userId) {

		Example example = new Example(Longarticle.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andNotEqualTo("status", StatusEnum.DELETED.type);
		example.orderBy("createDate").desc();

		PageHelper.startPage(page, pageSize);
		return queryArticleByExample(example, userId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult gerOthersLegalHis(Integer page, Integer pageSize, String userId, String targetId) {

		Example example = new Example(Longarticle.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", targetId);
		criteria.andEqualTo("status", StatusEnum.READABLE.type);
		example.orderBy("createDate").desc();

		PageHelper.startPage(page, pageSize);
		return queryArticleByExample(example, userId);
	}

	@Override
	public void addViewCount(String articleId) {
		// TODO Auto-generated method stub
	}

	@Override
	public int updateArticleStatus(String articleIds, int status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getArticles(Integer page,
								   Integer pageSize,
								   Integer queryType,
								   String userId,
								   String selectedTag,
								   String category,
								   String source) {
		Example articleExample = new Example(Longarticle.class);
		// 是否关注的, queryType: 0为全部，1为关注
		// 查询全部我(操作者)关注的用户
		if(queryType == 1) {
			Example mySubscribedUserExample = new Example(UserFans.class);
			Criteria criteria = mySubscribedUserExample.createCriteria();
			// 此处userId为操作者id
			criteria.andEqualTo("fansId", userId);
			List<UserFans> userList = userFansMapper.selectByExample(mySubscribedUserExample);
			if (userList.size() == 0 || userList == null) {
				return null;
			}
			// 查找 我关注的人的
			Criteria followCriteria = articleExample.createCriteria();
			for (UserFans userFans : userList) {
//				System.out.println(userFans.getUserId());
				followCriteria.orEqualTo("userId", userFans.getUserId());
			}
			articleExample.and(followCriteria);
		}
		//是否有标签
		if(!StringUtils.isBlank(selectedTag)) {
			// 在这些文章中找到符合搜索的标签的文章
			String[] texts = selectedTag.split(" ");
			Criteria tagCriteria = articleExample.createCriteria();
			for(String text : texts) {
				tagCriteria.orLike("tags", "%" + text + "%");
			}
			articleExample.and(tagCriteria);
		}
		//是否有标签
		if(!StringUtils.isBlank(category)) {
			// 在这些文章中找到符合搜索的分类的文章
			Criteria categoryCriteria = articleExample.createCriteria();
			categoryCriteria.andLike("category", "%" + category + "%");
			articleExample.and(categoryCriteria);
		}
		// 在这些文章中找到状态为可读的文章
		Criteria statusCriteria = articleExample.createCriteria();
		statusCriteria.andEqualTo("status", StatusEnum.READABLE.type);
		articleExample.and(statusCriteria);
		articleExample.setOrderByClause("create_date desc");

		PageHelper.startPage(page, pageSize);
		return queryArticleByExample(articleExample, userId);
	}

	/**
	 * @param articleExample
	 * @param userId 查询者
	 * @return
	 */
	private PagedResult queryArticleByExample(Example articleExample, String userId) {
		//通过条件，返回pagedResult
		List<Longarticle> list = longarticleMapper.selectByExample(articleExample);
		PageInfo<Longarticle> pageInfo = new PageInfo<>(list);
		PageInfo<LongarticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
		
		List<LongarticleVO> listVO = new ArrayList<>();
		for (Longarticle a : list) {
			listVO.add(composeArticleVO(a, userId));
		}
		pageInfoVO.setList(listVO);
		
		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult(pageInfoVO);
		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getArticlesFromOA(Integer page,
										 Integer pageSize,
										 String userId){

		Example example = new Example(Longarticle.class);
		example.setOrderByClause("create_date desc");
		// 判断可读状态的文章
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("status", StatusEnum.READABLE.type);
		// 判断是否为关联的公众号
		Criteria criteria2 = example.createCriteria();
		List<OfficialAccount> oas = officialAccountMapper.selectAll();
		for (OfficialAccount oa : oas){
			criteria2.orEqualTo("source", oa.getName());
		}
		example.and(criteria2);
//		long time = new Date().getTime() - (24*60*60*1000);//单位毫秒,24小时前
//		Date date = new Date(time);
//		criteria.andGreaterThan("createDate", date);

		PageHelper.startPage(page, pageSize);
		List<Longarticle> list = longarticleMapper.selectByExample(example);
		PageInfo<Longarticle> pageInfo = new PageInfo<>(list);
		PageInfo<LongarticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);

		List<LongarticleVO> listVO = new ArrayList<>();
		for (Longarticle longarticle : list) {
			listVO.add(composeArticleVO(longarticle, userId));
		}
		pageInfoVO.setList(listVO);

		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult(pageInfoVO);
		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isRelatedOA(String source){
		// 查询是否为关联的公众号
		List<OfficialAccount> oas = officialAccountMapper.selectAll();
		for (OfficialAccount oa : oas){
			if (oa.getName().equals(source)){
				return true;
			}
		}
		return false;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Object getHeadlines(String userId){
		Example example = new Example(Headlines.class);
		example.setOrderByClause("RAND() LIMIT 0,1");
		Headlines headlines = headlinesMapper.selectOneByExample(example);
		if (headlines == null){
			return null;
		}
		if (headlines.getTargetType().equals(PostType.LONGARTICLE.value)){
			return getArticleById(headlines.getTargetId(),userId);
		}
		return null;
	}
}
