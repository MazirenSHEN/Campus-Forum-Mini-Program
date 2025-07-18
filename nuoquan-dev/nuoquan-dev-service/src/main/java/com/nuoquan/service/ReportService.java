package com.nuoquan.service;

import com.nuoquan.enums.PostType;

public interface ReportService {
    /**
     * 查询用户是否举报了该对象
     * @param userId
     * @param targetType
     * @param targetId
     * @return
     */
    boolean isUserReport(String userId, PostType targetType, String targetId);

    /**
     * 举报发布的文章/长文章/评论
     * @param userId
     * @param targetId
     * @param targetType
     */
    void reportPublished(String userId, PostType targetType, String targetId);
}
