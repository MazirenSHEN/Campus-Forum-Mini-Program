package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.AuthenticatedUser;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticatedUserMapper extends MyMapper<AuthenticatedUser> {

    /**
     * @Description: 直接列出所有官方认证用户
     * @return
     */
    public List<AuthenticatedUserVO> list();

    /**
     * 按 userId 获取认证用户
     * @param userId
     * @return
     */
    @Deprecated
    public AuthenticatedUserVO getAuthUserById(String userId);
}
