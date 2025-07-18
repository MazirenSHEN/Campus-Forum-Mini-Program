package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.nq1.AuthenticatedUserMapper;
import com.nuoquan.pojo.AuthenticatedUser;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService{

    @Autowired
    private Sid sid;
    @Autowired
    public RedisOperator redis;
    @Autowired
    private AuthenticatedUserMapper authenticatedUserMapper;

    /**
     * 把 AuthenticatedUser 转换为 AuthenticatedUserVO, 组装VO对象
     * @param authenticatedUser
     * @param userId 操作者
     * @return
     */
    public AuthenticatedUserVO composeAuthUserVO(AuthenticatedUser authenticatedUser, String userId) {
        AuthenticatedUserVO authenticatedUserVO = new AuthenticatedUserVO();
        BeanUtils.copyProperties(authenticatedUser, authenticatedUserVO);
        return authenticatedUserVO;
    }

    /**
     * 分页根据类别查询认证用户
     *
     * @param page 页数
     * @param pageSize 页面大小
     */
    @Override
    public PagedResult listAllAuthUsers(Integer page, Integer pageSize) {

        // 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
        PageHelper.startPage(page, pageSize);

        Example authExample = new Example(AuthenticatedUser.class);
        authExample.setOrderByClause("create_date desc");
        List<AuthenticatedUser> list = authenticatedUserMapper.selectByExample(authExample);
        List<AuthenticatedUserVO> newList = new ArrayList<>();
        for (AuthenticatedUser a : list) {
            AuthenticatedUserVO authenticatedUserVO = composeAuthUserVO(a, "");
            newList.add(authenticatedUserVO);
        }

        PageInfo<AuthenticatedUserVO> pageList = new PageInfo<>(newList);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(newList);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }


    /**
     * 分页根据类别查询认证用户
     *
     * @param page 页数
     * @param pageSize 页面大小
     * @param type 类别
     */
    @Override
    public PagedResult listByType(Integer page, Integer pageSize, Integer type) {

        // 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
        PageHelper.startPage(page, pageSize);
        Example example = new Example(AuthenticatedUser.class);
        example.setOrderByClause("create_date desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type);
        List<AuthenticatedUser> list = authenticatedUserMapper.selectByExample(example);
        List<AuthenticatedUserVO> newList = new ArrayList<>();
        for (AuthenticatedUser au : list) {
            AuthenticatedUserVO authenticatedUserVO = composeAuthUserVO(au, "");
            newList.add(authenticatedUserVO);
        }

        PageInfo<AuthenticatedUserVO> pageList = new PageInfo<>(newList);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(newList);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    /**
     * 按 userId 获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public AuthenticatedUserVO getAuthUserByUserId(String userId) {
        AuthenticatedUser authUser = new AuthenticatedUser();
        // 条件
        authUser.setUserId(userId);
        //判断result是否为空
        AuthenticatedUser authenticatedUser = authenticatedUserMapper.selectOne(authUser);
        AuthenticatedUserVO authenticatedUserVO = composeAuthUserVO(authenticatedUser, userId);
        return authenticatedUserVO;
    }

    /**
     * 按 userId 获取用户是否认证
     *
     * @param userId
     * @return
     */
    @Override
    public boolean checkUserIsAuth(String userId) {
        AuthenticatedUser authUser = new AuthenticatedUser();
        // 条件
        authUser.setUserId(userId);
        //判断result是否为空
        AuthenticatedUser result = authenticatedUserMapper.selectOne(authUser);
        return result == null ? false : true;
    }

    /**
     * 使用email对用户进行认证
     *
     * @param email
     * @param type
     * @param userId
     * @return
     */
    @Override
    public String saveAuth(String email, Integer type, String userId) {
        String id = sid.nextShort();
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUserId(userId);
        authenticatedUser.setType(type);
        authenticatedUser.setCreateDate(new Date());
        authenticatedUser.setId(id);
        authenticatedUserMapper.insertSelective(authenticatedUser);
        return id;

    }
}
