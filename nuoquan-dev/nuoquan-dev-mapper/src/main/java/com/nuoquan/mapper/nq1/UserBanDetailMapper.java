package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.UserBanDetail;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;


@Repository
public interface UserBanDetailMapper extends MyMapper<UserBanDetail> {
    /**
     * 查询此用户结束禁言最晚的时间
     * @param userId
     * @return
     */
    String selectEndDate(String userId);
}
