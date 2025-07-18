package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.PostType;
import com.nuoquan.enums.RedisKeys;
import com.nuoquan.enums.ReputeWeight;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.*;
import com.nuoquan.pojo.*;
import com.nuoquan.pojo.vo.*;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.RedisOperator;
import com.nuoquan.utils.SensitiveFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SocialServiceImpl implements SocialService {
    @Autowired
    private Sid sid;
    @Autowired
    public RedisOperator redis;
    @Autowired
    private UserCollectMapper userCollectMapper;
    @Autowired
    private UserCommentMapper userCommentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    @Lazy
    private ArticleService articleService;
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private LongarticleMapper longarticleMapper;
    @Autowired
    @Lazy
    private LongarticleService longarticleService;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;
    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserVO getPostAuthor(PostType targetType, String targetId) {
        String authorId = "";
        switch (targetType) {
            case COMMENT:
                authorId = getCommentById(targetId, "").getFromUserId();
                break;
            case ARTICLE:
                authorId = articleMapper.selectByPrimaryKey(targetId).getUserId();
                break;
            case LONGARTICLE:
                authorId = longarticleMapper.selectByPrimaryKey(targetId).getUserId();
                break;
            case VOTE:
                authorId = voteMapper.selectByPrimaryKey(targetId).getUserId();
                break;
        }
        return userService.getUserById(authorId);
    }

    public UserCommentVO composeComment(String userId, UserComment userComment) {
        UserCommentVO userCommentVO = new UserCommentVO();
        BeanUtils.copyProperties(userComment, userCommentVO);
        // 查询并设置关于用户的点赞关系
        userCommentVO.setIsLike(isUserLike(userId, PostType.COMMENT, userCommentVO.getId()));
        // 查询并设置评论人头像昵称
        UserVO fromUser = userService.getUserById(userCommentVO.getFromUserId());
        userCommentVO.setNickname(fromUser.getNickname());
        userCommentVO.setFaceImg(fromUser.getFaceImg());
        if (authenticatedUserService.checkUserIsAuth(userCommentVO.getFromUserId())) {
            AuthenticatedUserVO fromAuthenticatedUserVO = authenticatedUserService
                    .getAuthUserByUserId(userCommentVO.getFromUserId());
            userCommentVO.setFromUserAuthType(fromAuthenticatedUserVO.getType());
        } else {
            userCommentVO.setFromUserAuthType(0);
        }
        // 查询并设置toNickName
        UserVO toUser = userService.getUserById(userCommentVO.getToUserId());
        userCommentVO.setToNickname(toUser.getNickname());

        if (authenticatedUserService.checkUserIsAuth(userCommentVO.getToUserId())) {
            AuthenticatedUserVO fromAuthenticatedUserVO = authenticatedUserService
                    .getAuthUserByUserId(userCommentVO.getToUserId());
            userCommentVO.setToUserAuthType(fromAuthenticatedUserVO.getType());
        } else {
            userCommentVO.setToUserAuthType(0);
        }
        // 检查是否有屏蔽词并替换
        userCommentVO.setComment(sensitiveFilterUtil.filter(userCommentVO.getComment()));
        return userCommentVO;
    }

    public UserLikeVO composeLike(String userId, UserLike userLike) {
        UserLikeVO userLikeVO = new UserLikeVO();
        BeanUtils.copyProperties(userLike, userLikeVO);
        // 查询并设置点赞人头像昵称
        UserVO fromUser = userService.getUserById(userLikeVO.getUserId());
        userLikeVO.setNickname(fromUser.getNickname());
        userLikeVO.setFaceImg(fromUser.getFaceImg());

        if (authenticatedUserService.checkUserIsAuth(userLikeVO.getUserId())) {
            AuthenticatedUserVO fromAuthenticatedUserVO = authenticatedUserService
                    .getAuthUserByUserId(userLikeVO.getUserId());
            userLikeVO.setAuthType(fromAuthenticatedUserVO.getType());
        } else {
            userLikeVO.setAuthType(0);
        }
        return userLikeVO;
    }

    /**
     * 上传评论到数据库
     * 
     * @param fromUserId     评论人
     * @param toUserId       被评论人
     * @param targetType     评论对象类型 不能是comment, 如果是article的子评论此字段也填article
     * @param targetId       评论对象Id
     * @param comment        评论内容
     * @param underCommentId 归属主评论，无填null
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String insertComment(String fromUserId,
            String toUserId,
            PostType targetType,
            String targetId,
            String comment,
            String underCommentId) {

        UserComment userComment = new UserComment();
        String id = sid.nextShort();
        userComment.setId(id);
        userComment.setFromUserId(fromUserId);
        userComment.setToUserId(toUserId); // 看看是不是多余字段 @Jerrio
        userComment.setTargetType(targetType.getValue());
        userComment.setTargetId(targetId);
        userComment.setComment(comment);
        userComment.setUnderCommentId(underCommentId);
        userComment.setDislikeNum(0);
        userComment.setLikeNum(0);
        userComment.setCommentNum(0);
        userComment.setCreateDate(new Date());
        userComment.setStatus(StatusEnum.READABLE.getType());
        userCommentMapper.insertSelective(userComment);
        // 评论对象评论数累加
        addTargetCommentCount(targetType.getValue(), targetId);

        if (!StringUtils.isBlank(underCommentId)) {
            // 主评论的评论数累加
            addTargetCommentCount(PostType.COMMENT.value, underCommentId);
        }
        // 被评论人影响力++
        if (toUserId != fromUserId) {
            userService.updateReputation(toUserId, ReputeWeight.COMMENT.weight, 1);
        }
        return id;
    }

    private void addTargetCommentCount(String targetType, String targetId) {
        if (targetType.equals(PostType.COMMENT.value)) {
            userCommentMapper.addCommentCount(targetId);
        } else if (targetType.equals(PostType.ARTICLE.value)) {
            articleMapper.addCommentCount(targetId);
        } else if (targetType.equals(PostType.LONGARTICLE.value)) {
            longarticleMapper.addCommentCount(targetId);
        } else if (targetType.equals(PostType.VOTE.value)) {
            // TODO:投票评论数累加
        }
    }

    private void reduceTargetCommentCount(String targetType, String targetId, String commentId) {
        if (targetType.equals(PostType.COMMENT.value)) {
            userCommentMapper.reduceCommentCount(targetId);
            articleMapper.reduceCommentCount(userCommentMapper.selectByPrimaryKey(targetId).getTargetId());
        } else if (targetType.equals(PostType.ARTICLE.value)) {
            // 如果此评论有子评论的话，查询所有子评论数之和，所以新评论数=旧评论数-子评论数之和-1
            if (userCommentMapper.querySubCommentNum(commentId) != 0) {
                int subCommentNum = userCommentMapper.selectByPrimaryKey(commentId).getCommentNum();
                int articleCommentNum = articleMapper.selectArticleCommentNum(targetId);
                int newCommentNum = articleCommentNum - subCommentNum - 1;
                articleMapper.reduceCommentCountSpecific(targetId, newCommentNum);
            }
            // 如果此评论没有子评论的话，直接评论数-1
            else
                articleMapper.reduceCommentCount(targetId);
        } else if (targetType.equals(PostType.LONGARTICLE.value)) {
            longarticleMapper.reduceCommentCount(targetId);
        } else if (targetType.equals(PostType.VOTE.value)) {
            // TODO:投票评论数累加
        }
    }

    /**
     * 分页获取父评论
     * 
     * @param page
     * @param pageSize
     * @param type       0 -- 按时间查询, 1 -- 按热度查询
     * @param targetType
     * @param targetId
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getMainComments(Integer page,
            Integer pageSize,
            Integer type,
            PostType targetType,
            String targetId,
            String userId) {

        Example example = new Example(UserComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("targetType", targetType.getValue());
        criteria.andEqualTo("targetId", targetId);
        criteria.andEqualTo("status", StatusEnum.READABLE.getType());
        criteria.andIsNull("underCommentId");
        if (type == 0) {
            example.setOrderByClause("create_date asc");
        }

        if (type == 1) {
            example.setOrderByClause("like_num desc");
        }
        PageHelper.startPage(page, pageSize);
        List<UserComment> list = userCommentMapper.selectByExample(example);
        PageInfo<UserComment> pageInfo = new PageInfo<>(list);
        PageInfo<UserCommentVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
        List<UserCommentVO> listVO = new ArrayList<>();
        for (UserComment c : list) {
            int articleStatus = articleService.getArticleById(c.getTargetId(), userId).getStatus();
            if (articleStatus != 0)
                listVO.add(composeComment(userId, c));
        }
        pageInfoVO.setList(listVO);

        // 为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult(pageInfoVO);

        return pagedResult;
    }

    /**
     * 分页获取子评论
     * 
     * @param page
     * @param pageSize
     * @param type           0 -- 按时间查询, 1 -- 按热度查询
     * @param underCommentId
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getSubComments(Integer page, Integer pageSize, Integer type, String underCommentId,
            String userId) {

        Example example = new Example(UserComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", StatusEnum.READABLE.getType());
        criteria.andEqualTo("underCommentId", underCommentId);
        if (type == 0) {
            example.setOrderByClause("create_date asc");
        }

        if (type == 1) {
            example.setOrderByClause("like_num desc");
        }

        PageHelper.startPage(page, pageSize);
        List<UserComment> list = userCommentMapper.selectByExample(example);
        PageInfo<UserComment> pageInfo = new PageInfo<>(list);
        PageInfo<UserCommentVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
        List<UserCommentVO> listVO = new ArrayList<>();
        for (UserComment c : list) {
            listVO.add(composeComment(userId, c));
        }
        pageInfoVO.setList(listVO);

        // 为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult(pageInfoVO);

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserCommentVO getCommentById(String commentId, String userId) {
        // Example example = new Example(UserComment.class);
        // example.createCriteria().andEqualTo("id",commentId);
        // UserComment comment = userCommentMapper.selectOneByExample(example);
        // 会报错，待解决；java.lang.String cannot be cast to java.util.Date 原因是POJO对象未定义@Id主键
        UserComment comment = userCommentMapper.selectByPrimaryKey(commentId);
        UserCommentVO commentVO = composeComment(userId, comment);
        return commentVO;
    }

    /**
     * 删除评论
     * 
     * @param commentId
     * @return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int fDeleteComment(String commentId, String userId, String targetId, PostType targetType) {
        if (userId
                .equals(articleMapper.selectByPrimaryKey(userCommentMapper.selectByPrimaryKey(commentId).getTargetId())
                        .getUserId())
                || userId.equals(userCommentMapper.selectByPrimaryKey(commentId).getFromUserId())
                || userId == "AdminUser") {
            Example example = new Example(UserComment.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", commentId);
            UserComment c = new UserComment();
            c.setStatus(StatusEnum.DELETED.type);
            userCommentMapper.updateByExampleSelective(c, example);
            reduceTargetCommentCount(targetType.getValue(), targetId, commentId);
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * 用户点赞对象
     * 
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String userLike(String userId, PostType targetType, String targetId) {
        boolean isLike = isUserLike(userId, targetType, targetId);
        if (!isLike) {
            // 保存用户和文章的点赞关联关系表
            String id = sid.nextShort();
            UserLike userLike = new UserLike();
            userLike.setId(id);
            userLike.setUserId(userId);
            userLike.setTargetType(targetType.getValue());
            userLike.setTargetId(targetId);
            userLike.setCreateDate(new Date());

            userLikeMapper.insertSelective(userLike);
            // 点赞数量累加
            switch (targetType) {
                case COMMENT:
                    userCommentMapper.addLikeCount(targetId);
                    break;
                case ARTICLE:
                    articleMapper.addLikeCount(targetId);
                    break;
                case LONGARTICLE:
                    longarticleMapper.addLikeCount(targetId);
                    break;
                case VOTE:
                    break;
            }

            String authorId = getPostAuthor(targetType, targetId).getId();
            // 作者受喜欢数量的累加
            userService.addReceiveLikeCount(authorId);
            // 作者影响力++
            if (!userId.equals(authorId)) {
                userService.updateReputation(authorId, ReputeWeight.LIKE.weight, 1);
            }
            return id;
        }
        return "";
    }

    /**
     * 用户取消点赞
     *
     * @param userId
     * @param targetType
     * @param targetId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLike(String userId, PostType targetType, String targetId) {
        boolean isLike = isUserLike(userId, targetType, targetId);
        if (isLike) {
            // 1.删除用户和评论的点赞关联关系表
            Example example = new Example(UserLike.class);
            // 创造条件
            Example.Criteria criteria = example.createCriteria();
            // 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
            criteria.andEqualTo("userId", userId);
            criteria.andEqualTo("targetType", targetType);
            criteria.andEqualTo("targetId", targetId);

            userLikeMapper.deleteByExample(example);

            // 2.喜欢数量累减
            switch (targetType) {
                case COMMENT:
                    userCommentMapper.reduceLikeCount(targetId);
                    break;
                case ARTICLE:
                    articleMapper.reduceLikeCount(targetId);
                    break;
                case LONGARTICLE:
                    longarticleMapper.reduceLikeCount(targetId);
                    break;
            }
            String authorId = getPostAuthor(targetType, targetId).getId();
            // 3.作者受喜欢数量的累减
            userService.reduceReceiveLikeCount(authorId);
            // 作者影响力--
            if (!userId.equals(authorId)) {
                userService.updateReputation(authorId, ReputeWeight.LIKE.weight, -1);
            }
        }
    }

    /**
     * 查询用户是否点赞了对象
     *
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLike(String userId, PostType targetType, String targetId) {
        Example example = new Example(UserLike.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("targetType", targetType.getValue());
        criteria.andEqualTo("targetId", targetId);

        List<UserLike> list = userLikeMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 用户收藏对象
     *
     * @param userId
     * @param targetType
     * @param targetId
     */
    @Override
    public void userCollect(String userId, PostType targetType, String targetId) {
        // 保存用户和文章的收藏关联关系表
        String id = sid.nextShort();
        UserCollect userCollect = new UserCollect();
        userCollect.setId(id);
        userCollect.setUserId(userId);
        userCollect.setTargetType(targetType.getValue());
        userCollect.setTargetId(targetId);
        userCollect.setCreateDate(new Date());

        userCollectMapper.insertSelective(userCollect);
        // 收藏数量++
        switch (targetType) {
            case ARTICLE:
                articleMapper.addCollectCount(targetId);
                break;
            case LONGARTICLE:
                longarticleMapper.addCollectCount(targetId);
                break;
        }
    }

    /**
     * 用户取消收藏
     * 
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    @Override
    public String userUncollect(String userId, PostType targetType, String targetId) {
        boolean isCollect = isUserCollect(userId, targetType, targetId);
        if (isCollect) {
            Example example = new Example(UserCollect.class);
            // 创造条件
            Example.Criteria criteria = example.createCriteria();
            // 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
            criteria.andEqualTo("userId", userId);
            criteria.andEqualTo("targetType", targetType.getValue());
            criteria.andEqualTo("targetId", targetId);
            System.out.println(userId);
            System.out.println(targetId);

            userCollectMapper.deleteByExample(example);

            // 2.收藏数量累减--
            switch (targetType) {
                case ARTICLE:
                    articleMapper.reduceCollectCount(targetId);
                    break;
                case LONGARTICLE:
                    longarticleMapper.reduceCollectCount(targetId);
                    break;
            } // end switch
        } // end if(isCollect)
        return targetId;
    }

    /**
     * 查询用户是否收藏了对象
     *
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    @Override
    public boolean isUserCollect(String userId, PostType targetType, String targetId) {
        Example example = new Example(UserCollect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("targetType", targetType.getValue());
        criteria.andEqualTo("targetId", targetId);

        List<UserCollect> list = userCollectMapper.selectByExample(example);
        return list != null && !list.isEmpty();
    }

    /**
     * 查询用户收藏的对象
     *
     * @param page
     * @param pageSize
     * @param userId   //查询者
     * @return
     */
    @Override
    public PagedResult queryCollect(Integer page, Integer pageSize, String userId) {
        Example example = new Example(UserCollect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.orderBy("createDate").desc();

        PageHelper.startPage(page, pageSize);
        List<UserCollect> list = userCollectMapper.selectByExample(example);

        PageInfo<UserCollect> pageInfo = new PageInfo<>(list);
        PageInfo<UserCollectVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
        List<UserCollectVO> listVO = new ArrayList<>();
        for (UserCollect c : list) {
            int articleStatus = articleMapper.selectArticleStatus(c.getTargetId());
            if (articleStatus != 0)
                listVO.add(composeCollect(userId, c));
        }
        pageInfoVO.setList(listVO);

        // 为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult(pageInfoVO);
        return pagedResult;
    }

    private UserCollectVO composeCollect(String userId, UserCollect collect) {
        UserCollectVO collectVO = new UserCollectVO();
        BeanUtils.copyProperties(collect, collectVO);
        String targetId = collectVO.getTargetId();
        Object target = null;
        if (collectVO.getTargetType().equals(PostType.ARTICLE.value)) {
            ArticleVO article = articleService.getArticleById(targetId, userId);
            // 状态是否可读
            if (article.getStatus() == StatusEnum.READABLE.type) {
                target = article;
            }
        } else if (collectVO.getTargetType().equals(PostType.LONGARTICLE.value)) {
            LongarticleVO longarticleVO = longarticleService.getArticleById(targetId, userId);
            if (longarticleVO.getStatus() == StatusEnum.READABLE.type) {
                target = longarticleVO;
            }
        } else if (collectVO.getTargetType().equals(PostType.VOTE.value)) {
            // Do nothing
        }
        collectVO.setTarget(target);
        return collectVO;
    }

    /**
     * 用户Fetch对象为之增加浏览量
     * 
     * @param userId
     * @param targetId
     * @param targetType
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addViewCount(String userId, PostType targetType, String targetId) {
        String key = RedisKeys.USER_FETCH.value;
        key = key.replace("userId", userId).replace("targetType", targetType.value).replace("targetId", targetId);
        String value = redis.get(key);
        if (StringUtils.isBlank(value)) {
            redis.set(key, "ture", 86400); // 两小时内不重复计算浏览量

            switch (targetType) {
                case ARTICLE:
                    articleMapper.addViewCount(targetId);
                    break;
                case LONGARTICLE:
                    longarticleMapper.addViewCount(targetId);
                    break;
            }
            // 作者影响力++
            String authorId = getPostAuthor(targetType, targetId).getId();
            userService.updateReputation(authorId, ReputeWeight.VIEW.weight, 1);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userRead(String userId, PostType targetType, String targetId) {
        // TODO:记录用户阅读行为
    }

    @Override
    public PagedResult getAllCommentToMe(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page, pageSize);
        List<UserComment> list = userCommentMapper.queryCommentToMe(userId);
        PageInfo<UserComment> pageInfo = new PageInfo<>(list);
        PageInfo<UserCommentVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
        List<UserCommentVO> listVO = new ArrayList<>();
        for (UserComment c : list) {
            int articleStatus = articleService.getArticleById(c.getTargetId(), userId).getStatus();
            if (articleStatus != 0)
                listVO.add(composeComment(userId, c));
        }
        pageInfoVO.setList(listVO);

        // 为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult(pageInfoVO);

        return pagedResult;

    }

    // 此查询有问题，因为点赞数据库中没有to_user_id,后续如果需要的话可以更改数据库后使用此方法
    @Override
    public PagedResult getAllLikeToMe(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page, pageSize);
        List<UserLike> list = userLikeMapper.queryLikeToMe(userId);
        PageInfo<UserLike> pageInfo = new PageInfo<>(list);
        PageInfo<UserLikeVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
        List<UserLikeVO> listVO = new ArrayList<>();
        for (UserLike c : list) {
            int articleStatus = articleService.getArticleById(c.getTargetId(), userId).getStatus();
            if (articleStatus != 0)
                listVO.add(composeLike(userId, c));
        }
        pageInfoVO.setList(listVO);

        // 为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult(pageInfoVO);

        return pagedResult;
    }

}
