package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.AdPositionEnum;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.enums.StreamType;
import com.nuoquan.mapper.nq1.AdvertMapper;
import com.nuoquan.pojo.Advert;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.SensitiveFilterUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;

@Service
public class AdvertServiceImpl implements AdvertService {
    @Autowired
    private Sid sid;
    @Autowired
    private AdvertMapper advertMapper;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;

    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "nq1TransactionManager")
    @Override
    public void uploadAd(String advertiser, AdPositionEnum position, StreamType streamType, String content, Integer isJump, String link, String resourceUrl, Integer duration, String period) {

        Advert advert = new Advert();
        String id = sid.nextShort();
        advert.setId(id);
        advert.setAdvertiser(advertiser);
        advert.setPosition(position.value);
        advert.setStreamType(streamType.value);
        advert.setContent(content);
        advert.setIsJump(isJump);
        advert.setLink(link);
        advert.setResourceUrl(resourceUrl);

        Date date = new Date();
        advert.setCreateDate(date);
        //Calculate expireDate
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, duration);
        Date expireDate = ca.getTime();
        advert.setExpireDate(expireDate);

        advert.setPeriod(period);
        advert.setStatus(StatusEnum.READABLE.type);

        advertMapper.insertSelective(advert);
    }

    @Transactional(propagation = Propagation.SUPPORTS, transactionManager = "nq1TransactionManager")
    @Override
    public PagedResult getAd(Integer page, Integer pageSize, String userId) {
        Example example = new Example(Advert.class);
        example.orderBy("createDate").desc();
        Criteria criteria = example.createCriteria();
        addValidity(criteria);

        PageHelper.startPage(page, pageSize);
        List<Advert> advertList = advertMapper.selectByExample(example);

        // 进行敏感词检测
        for (Advert a : advertList){
            a.setContent(sensitiveFilterUtil.filter(a.getContent()));
        }

        // TODO:转换VO对象
        PageInfo<Advert> pageInfo = new PageInfo<>(advertList);
//        PageInfo<AdvertVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
//        List<ArticleVO> listVO = new ArrayList<>();
//        for (Article a : list) {
//            listVO.add(composeArticleVO(a, userId));
//        }
//        pageInfoVO.setList(listVO);
        PagedResult pagedResult = new PagedResult(pageInfo);

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, transactionManager = "nq1TransactionManager")
    @Override
    public Advert getAdById(String adId, String userId) {
        Advert advert = advertMapper.selectByPrimaryKey(adId);
        // 进行敏感词检测
        advert.setContent(sensitiveFilterUtil.filter(advert.getContent()));
        // TODO:转化VO对象
        return advert;

    }

    /**
     * 查询某位置的广告
     * @param position 位置
     * @param num 数量
     * @param userId 用户id
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, transactionManager = "nq1TransactionManager")
    @Override
    public List<Advert> getAdByPosition(AdPositionEnum position, Integer num, String userId){
        if(num <= 0){
            num = 1;
        }

        Example example = new Example(Advert.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("position", position.value);
        addValidity(criteria);
        List<Advert> advertList =  advertMapper.selectByExample(example);
        if (num > advertList.size()){
            num = advertList.size();
        }

        List<Advert> newList = new ArrayList<>();
        Random random = new Random();
        for (int i=num; i>0; i--){
            int r = random.nextInt(advertList.size());
            newList.add(advertList.remove(r));
        }
        return newList;
    }

    /**
     * 添加有效性条件（过期时间内+状态为Readable）
     * @param criteria
     * @return
     */
    private Criteria addValidity(Criteria criteria){
        Date date = new Date();
        criteria.andEqualTo("status", StatusEnum.READABLE.type);
        criteria.andGreaterThan("expireDate", date);
        return criteria;
    }
}
