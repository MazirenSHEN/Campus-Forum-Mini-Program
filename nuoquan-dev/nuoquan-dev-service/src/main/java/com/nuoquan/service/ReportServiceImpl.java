package com.nuoquan.service;

import com.nuoquan.enums.PostType;
import com.nuoquan.mapper.nq1.ArticleMapper;
import com.nuoquan.mapper.nq1.LongarticleMapper;
import com.nuoquan.mapper.nq1.UserCommentMapper;
import com.nuoquan.mapper.nq1.UserReportMapper;
import com.nuoquan.pojo.UserCollect;
import com.nuoquan.pojo.UserLike;
import com.nuoquan.pojo.UserReport;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private Sid sid;
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private LongarticleMapper longarticleMapper;

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private UserReportMapper userReportMapper;


    @Override
    public boolean isUserReport(String userId, PostType targetType, String targetId) {
        Example example = new Example(UserReport.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("targetType",targetType.getValue());
        criteria.andEqualTo("targetId", targetId);

        List<UserReport> list = userReportMapper.selectByExample(example);
        return list != null && !list.isEmpty();
    }

    @Override
    public void reportPublished(String userId, PostType targetType, String targetId) {
        // 判断此用户是否以及举报过该对象
        boolean isReport = isUserReport(userId, targetType, targetId);
        System.out.println("--------" + isReport);

        if (!isReport){
            // 保存用户和文章的点赞关联关系表
            String id = sid.nextShort();
            UserReport userReport = new UserReport();
            userReport.setId(id);
            userReport.setUserId(userId);
            userReport.setTargetType(targetType.getValue());
            userReport.setTargetId(targetId);
            userReport.setCreateDate(new Date());
            userReportMapper.insertSelective(userReport);

            switch (targetType) {
                case COMMENT:
                    userCommentMapper.addReportedCount(targetId);
                    break;
                case ARTICLE:
                    articleMapper.addReportedCount(targetId);
                    break;
                case LONGARTICLE:
                    longarticleMapper.addReportedCount(targetId);
            }
        }


    }
}
