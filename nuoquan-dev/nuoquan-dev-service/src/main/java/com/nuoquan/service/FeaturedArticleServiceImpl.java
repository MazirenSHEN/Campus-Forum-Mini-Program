package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.FeaturedArticleMapper;
import com.nuoquan.pojo.FeaturedArticle;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.pojo.vo.FeaturedArticleVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.SensitiveFilterUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章加精业务模型
 * @author BoyuanYE
 * @date 2022/08/10
 */
@Service
public class FeaturedArticleServiceImpl implements FeaturedArticleService {

    @Autowired
    private Sid sid;
    @Autowired
    private FeaturedArticleMapper featuredArticleMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;
    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    public FeaturedArticleVO composeFeaturedArticleVO(FeaturedArticleVO featuredArticleVO, String userId) {
        ArticleVO articleVO = articleService.getArticleById(featuredArticleVO.getArticleId(), userId);
        UserVO userVO = userService.getUserById(articleVO.getUserId());
        // 添加文章标题
        featuredArticleVO.setArticleTitle(articleVO.getArticleTitle());
        // 添加文章点赞数量
        featuredArticleVO.setLikeNum(articleVO.getLikeNum());
        // 添加用户头像
        featuredArticleVO.setFaceImg(userVO.getFaceImg());
        featuredArticleVO.setFaceImgThumb(userVO.getFaceImgThumb());
        // 添加用户昵称
        featuredArticleVO.setNickname(userVO.getNickname());
        if (authenticatedUserService.checkUserIsAuth(articleVO.getUserId())){
            AuthenticatedUserVO authenticatedUserVO = authenticatedUserService.getAuthUserByUserId(articleVO.getUserId());
            featuredArticleVO.setAuthType(authenticatedUserVO.getType());
        } else {
            featuredArticleVO.setAuthType(0);
        }
        // 如果此加精文章没有被设置封面图，则默认使用第一张文章图片作为封面图，若文章没有图片，默认设置为一张特殊图片
        if (featuredArticleVO.getCoverPath() == null) {
            if(articleVO.getImgList() != null) {
                featuredArticleVO.setCoverPath(articleVO.getImgList().get(0).getImagePath());
            }
            else {
                featuredArticleVO.setCoverPath("");
            }
        }

        // 检查是否有屏蔽词并替换
        featuredArticleVO.setNickname(sensitiveFilterUtil.filter(featuredArticleVO.getNickname()));
        featuredArticleVO.setArticleTitle(sensitiveFilterUtil.filter(featuredArticleVO.getArticleTitle()));

        return featuredArticleVO;
    }

    /**
     * 把 FeaturedArticle 转换为 FeaturedArticleVO, 组装加精文章VO对象
     * @param featuredArticle FeaturedArticle类的实例
     * @param userId 操作者（我）
     * @return
     */
    public FeaturedArticleVO composeFeaturedArticleVO(FeaturedArticle featuredArticle, String userId) {
        FeaturedArticleVO featuredArticleVO = new FeaturedArticleVO();
        BeanUtils.copyProperties(featuredArticle, featuredArticleVO);
        return composeFeaturedArticleVO(featuredArticleVO, userId);
    }


    /**
     * 获取全部加精文章
     * @param page 前端页数
     * @param pageSize 前端页面大小
     * @param userId 操作者id
     * @return 页面结果
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryFeaturedArticle(Integer page, Integer pageSize, String userId) {
        Example featuredArticleExample = new Example(FeaturedArticle.class);
        Example.Criteria statusCriteria = featuredArticleExample.createCriteria();
        statusCriteria.andEqualTo("status", StatusEnum.READABLE.type);
        featuredArticleExample.and(statusCriteria);
        featuredArticleExample.setOrderByClause("create_date desc");

        PageHelper.startPage(page, pageSize);
        return queryFeaturedArticleByExample(featuredArticleExample, userId);
//        List<FeaturedArticleVO> list = featuredArticleMapper.queryAllFeaturedArticles();
//        for (FeaturedArticleVO a : list) {
//            a = composeFeaturedArticleVO(a, userId);
//        }
//
//        PageInfo<FeaturedArticleVO> pageList = new PageInfo<>(list);
//        PagedResult pagedResult = new PagedResult();
//        pagedResult.setPage(page);
//        pagedResult.setTotal(pageList.getPages());
//        pagedResult.setRows(list);
//        pagedResult.setRecords(pageList.getTotal());
//
//        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult queryFeaturedArticleByExample(Example featuredArticleExample, String userId) {
        //通过条件，返回pagedResult
        List<FeaturedArticle> list = featuredArticleMapper.selectByExample(featuredArticleExample);
        PageInfo<FeaturedArticle> pageInfo = new PageInfo<>(list);
        PageInfo<FeaturedArticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);

        List<FeaturedArticleVO> listVO = new ArrayList<>();
        for (FeaturedArticle a : list) {
            listVO.add(composeFeaturedArticleVO(a, userId));
        }
        pageInfoVO.setList(listVO);

        //为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfoVO.getPageNum());
        pagedResult.setTotal(pageInfoVO.getPages());
        pagedResult.setRows(pageInfoVO.getList());
        pagedResult.setRecords(pageInfoVO.getTotal());

        return pagedResult;
    }

    /**
     * 使用文章加精id获取文章
     *
     * @param featuredArticleId 文章加精id
     * @param userId            操作者id
     * @return 页面结果
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public FeaturedArticleVO getFeaturedArticleById(String featuredArticleId, String userId) {
        FeaturedArticle featuredArticle = featuredArticleMapper.selectByPrimaryKey(featuredArticleId);
        FeaturedArticleVO featuredArticleVO = composeFeaturedArticleVO(featuredArticle, userId);
        return featuredArticleVO;
    }

    /**
     * 查询文章是否已经加精
     *
     * @param articleId 文章id
     * @return Integer 0-从未加精；1-已加精；2-曾加精但已取消加精（即数据库中有记录但客户端不显示）
     */
    @Override
    public Boolean isArticleFeatured(String articleId) {
        Example example = new Example(FeaturedArticle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("articleId", articleId);

        List<FeaturedArticle> list = featuredArticleMapper.selectByExample(example);
        return list != null && !list.isEmpty();
    }
    /**
     * 文章加精
     *
     * @param featuredArticle 精选文章类的实例
     * @return 文章加精id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String setToFeaturedArticle(FeaturedArticle featuredArticle) {
        String id = sid.nextShort();
        featuredArticle.setId(id);
        featuredArticleMapper.insertSelective(featuredArticle);

        return id;
    }

    /**
     * 取消文章加精
     *
     * @param featuredArticleId 文章加精id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void removeFeaturedArticle(String featuredArticleId) {
        Example example = new Example(FeaturedArticle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", featuredArticleId);
        FeaturedArticle fa = new FeaturedArticle();
        fa.setStatus(StatusEnum.DELETED.type);
        featuredArticleMapper.updateByExampleSelective(fa, example);
    }

}
