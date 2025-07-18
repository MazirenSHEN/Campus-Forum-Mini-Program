package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.UserComment;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentMapper extends MyMapper<UserComment> {
    /**
     * @Description: 对评论喜欢的数量进行累加
     * @param commentId
     */
    public void addLikeCount(String commentId);

    /**
     * @description: 对评论喜欢的数量进行累减
     * @param commentId
     */
    public void reduceLikeCount(String commentId);

    /**
     * @description: 对评论的评论的数量进行累加
     * @param commentId
     */
    public void addCommentCount(String commentId);

    /**
     * @description: 对评论的评论的数量进行累减
     * @param commentId
     */
    public void reduceCommentCount(String commentId);

    /**
     * @description: 对评论的举报数量进行累加
     * @param commentId
     */
    public void addReportedCount(String commentId);

    /**
     * 根据创建时间降序查找所有被举报的评论
     * @return
     */
    public List<UserComment> queryReportedCommentByCreateTime();

    /**
     * 根据举报数量降序查找所有被举报的评论
     * @return
     */
    public List<UserComment> queryReportedCommentByReportedNum();

    /**
     * 查询所有给我的评论
     * @param userid
     * @return
     */
    public List<UserComment> queryCommentToMe(String userid);

    /**
     * 查询此评论的子评论数
     * @param commentId
     * @return
     */
    public Integer querySubCommentNum(String commentId);

    /**
     * 查询此主评论的所有子评论
     * @param commentId
     * @return
     */
    public List<UserComment> queryAllSubComment(String commentId);
}