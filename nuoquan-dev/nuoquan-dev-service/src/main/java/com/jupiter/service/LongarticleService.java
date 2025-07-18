package com.jupiter.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jupiter.mapper.LongarticleMapper;
import com.jupiter.pojo.Longarticle;
import com.nuoquan.enums.PostType;
import com.nuoquan.enums.SignFlagEnum;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.*;
import com.nuoquan.pojo.UserFans;
import com.nuoquan.pojo.vo.LongarticleVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.service.SocialService;
import com.nuoquan.service.UserService;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("LongarticleServiceJp")
public class LongarticleService{
	@Autowired
	private LongarticleMapper longarticleMapperJp;

	@Transactional(propagation = Propagation.REQUIRED, transactionManager = "jpTransactionManager")
	public void updateByPrimaryKeySelective(Longarticle longarticleJp) throws Exception{
		longarticleMapperJp.updateByPrimaryKeySelective(longarticleJp);
//		throw new Exception("子数据源事务测试");
	}

	@Transactional(propagation = Propagation.SUPPORTS, transactionManager = "jpTransactionManager")
	public List<Longarticle> getUnsignArticle(){
		Example example = new Example(Longarticle.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("signFlag", SignFlagEnum.UNSIGN);
		return longarticleMapperJp.selectByExample(example);
	}
}
