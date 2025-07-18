//package com.nuoquan.service;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.nuoquan.pojo.admin.Tablepar;
//import com.nuoquan.support.Convert;
//import com.nuoquan.utils.PagedResult;
//import com.nuoquan.utils.StringUtils;
//import org.n3r.idworker.Sid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import tk.mybatis.mapper.entity.Example;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class LongArticleMgtService {
//
//    @Autowired
//    private Sid sid;
//
//    @Autowired
//    private HotArticleMgtMapper hotArticleMgtMapper;
//
//    /**
//     * TODO： 转换对象为VO对象
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public PagedResult list(Integer page, Integer pageSize) {
//
//        PageHelper.startPage(page, pageSize);
//
//        Example example = new Example(HotArticleMgt.class);
//        example.setOrderByClause("create_date desc");
//        List<HotArticleMgt> list = hotArticleMgtMapper.selectByExample(example);
//
//        PageInfo<HotArticleMgt> pageList = new PageInfo<>(list);
//
//        PagedResult pagedResult = new PagedResult();
//        pagedResult.setPage(page);
//        pagedResult.setTotal(pageList.getPages());
//        pagedResult.setRows(list);
//        pagedResult.setRecords(pageList.getTotal());
//
//        return pagedResult;
//    }
//
//    /**
//     * 查询全部头条文章集合
//     * @return
//     */
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public List<HotArticleMgt> queryList(){
//        Example example = new Example(HotArticleMgt.class);
//        return hotArticleMgtMapper.selectByExample(example);
//    }
//
//    /**
//     * 添加头条文章
//     * @param article
//     * @return
//     */
//    @Transactional(propagation = Propagation.REQUIRED)
//    public int insertHotArticle(HotArticleMgt article) {
//        // 默认操作者为运行账号
//        String operator = "admin1";
//        String id = sid.nextShort();
//        article.setId(id);
//        article.setCreateDate(new Date());
//        article.setOperator(operator);
//
//        return hotArticleMgtMapper.insertSelective(article);
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    public int deleteHotArticle(String ids) {
//        List<String> lista = Convert.toListStrArray(ids);
//        Example example = new Example(HotArticleMgt.class);
//        example.createCriteria().andIn("id", lista);
//        return hotArticleMgtMapper.deleteByExample(example);
//    }
//
//}
