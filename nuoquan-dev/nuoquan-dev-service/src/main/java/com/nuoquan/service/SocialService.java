package com.nuoquan.service;

import com.nuoquan.enums.PostType;
import com.nuoquan.pojo.vo.UserCommentVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.utils.PagedResult;

/**
 * @Description: 收藏服务类，支持对不同对象的收藏
 * @Author: jerrio
 * @Date: 2020.8.31
 */
public interface SocialService {

    /**
     * 获取对象的作者
     * @param targetType
     * @param targetId
     * @return
     */
    public UserVO getPostAuthor(PostType targetType, String targetId);

    /**
     * 上传评论到数据库
     * @param fromUserId 评论人
     * @param toUserId   被评论人
     * @param targetType 评论对象类型
     * @param targetId   评论对象Id
     * @param comment    评论内容
     * @param underCommentId  归属主评论，无填null
     */
    public String insertComment(String fromUserId,
                              String toUserId,
                              PostType targetType,
                              String targetId,
                              String comment,
                              String underCommentId);

    /**
     * 分页获取父评论
     * @param page
     * @param pageSize
     * @param type
     * @param targetType
     * @param targetId
     * @param userId
     * @return
     */
    public PagedResult getMainComments(Integer page,
                                       Integer pageSize,
                                       Integer type,
                                       PostType targetType,
                                       String targetId,
                                       String userId);

    /**
     * 分页获取子评论
     * @param page
     * @param pageSize
     * @param type
     * @param underCommentId
     * @param userId
     * @return
     */
    public PagedResult getSubComments(Integer page, Integer pageSize, Integer type, String underCommentId, String userId);

    /**
     * 根据commentId获取评论VO对象
     * @param commentId
     * @param userId
     * @return
     */
    public UserCommentVO getCommentById(String commentId, String userId);

    /**
     * Set the status to unreadable
     * @param commentId
     */
    public int fDeleteComment(String commentId, String userId, String targetId, PostType targetType);

    /**
     * 用户点赞对象
     * @param userId
     * @param targetType
     * @param targetId
     * @return likeId
     */
    public String userLike(String userId, PostType targetType, String targetId);

    /**
     * 用户取消点赞
     * @param userId
     * @param targetType
     * @param targetId
     */
    public void userUnLike(String userId, PostType targetType, String targetId);

    /**
     * 查询用户是否点赞了对象
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    public boolean isUserLike(String userId, PostType targetType, String targetId);

    /**
     * 用户收藏对象
     * @param userId
     * @param targetType
     * @param targetId
     */
    public void userCollect(String userId, PostType targetType, String targetId);

    /**
     * 用户取消收藏
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    public String userUncollect(String userId, PostType targetType, String targetId);

    /**
     * 查询用户是否收藏了对象
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    public boolean isUserCollect(String userId, PostType targetType, String targetId);

    /**
     * 查询用户收藏的对象
     * @param page
     * @param pageSize
     * @param userId //查询者
     * @return
     */
    public PagedResult queryCollect(Integer page, Integer pageSize, String userId);

    /**
     * 用户Fetch对象为之增加浏览量
     * @param userId
     * @param targetId
     * @param targetType
     */
    public void addViewCount(String userId, PostType targetType, String targetId);

    /**
     * 用户阅读了对象
     * @param userId
     * @param targetId
     * @param targetType
     */
    public void userRead(String userId, PostType targetType, String targetId);

    /**
     * 查询全部给我的评论
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public PagedResult getAllCommentToMe(Integer page, Integer pageSize, String userId);

    /**
     * 查询全部给我的赞
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public PagedResult getAllLikeToMe(Integer page, Integer pageSize, String userId);

}
