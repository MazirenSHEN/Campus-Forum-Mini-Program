package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.UserComment;
import com.nuoquan.pojo.UserLike;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikeMapper extends MyMapper<UserLike> {
    /**
     * 查询所有给我的赞
     * @param userid
     * @return
     */
    public List<UserLike> queryLikeToMe(String userid);
}