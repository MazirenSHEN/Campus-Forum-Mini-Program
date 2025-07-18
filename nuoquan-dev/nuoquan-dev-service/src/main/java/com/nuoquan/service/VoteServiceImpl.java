package com.nuoquan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import com.nuoquan.pojo.vo.AuthenticatedUserVO;
import com.nuoquan.pojo.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.enums.VoteStatusEnum;
import com.nuoquan.mapper.nq1.UserMapper;
import com.nuoquan.mapper.nq1.VoteImageMapper;
import com.nuoquan.mapper.nq1.VoteMapper;
import com.nuoquan.mapper.nq1.VoteMapperCustom;
import com.nuoquan.mapper.nq1.VoteOptionMapper;
import com.nuoquan.mapper.nq1.VoteUserMapper;
import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.VoteUser;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private Sid sid;
	
	@Autowired
	private VoteMapper voteMapper;
	
	@Autowired
	private VoteImageMapper voteImageMapper;
	
	@Autowired
	private VoteOptionMapper voteOptionMapper;
	
	@Autowired
	private VoteMapperCustom voteMapperCustom;

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private VoteUserMapper voteUserMapper;
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVote(Vote vote) {
		String id = sid.nextShort();
		vote.setId(id);
		voteMapper.insertSelective(vote);
		
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveVoteImages(VoteImage voteImage) {
		String id = sid.nextShort();
		voteImage.setId(id);
		voteImageMapper.insertSelective(voteImage);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveVoteOption(VoteOption voteOption) {
		String id = sid.nextShort();
		voteOption.setId(id);
		voteOptionMapper.insertSelective(voteOption);
	}
	

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult list(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<VoteVO> list = voteMapperCustom.list();
		for (VoteVO v : list) {
			// 为每个文章添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult listCheckOnly(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<VoteVO> list = voteMapperCustom.listCheckOnly();
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllVotes(Integer page, Integer pageSize, String userId) {
		
		// 从controller中获取page和pageSize(经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);
		
		List<VoteVO> list = voteMapperCustom.queryAllVotes();
		for(VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
			// 为每个投票添加用户是否投过票的布尔值
			v.setIsUserVoted(isUserVoted(userId, v.getId()));
			// 如果该用户投过票, 查询 选择的 选项id
			if (v.getIsUserVoted()) {
				v.setSelectedOptId(selectedOptionId(userId, v.getId()));
			}
		}
		
		PageInfo<VoteVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectedOptionId(String userId, String voteId) {
		String selectedOptId = null;
		Example example = new Example(VoteUser.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("voteId", voteId);
		List<VoteUser> vo = voteUserMapper.selectByExample(example);
		for (VoteUser voteUser : vo) {
			selectedOptId = voteUser.getOptionId();
		}
		return selectedOptId;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserVoted(String userId, String voteId) {
		Example example = new Example(VoteUser.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("voteId", voteId);
		List<VoteUser> list = voteUserMapper.selectByExample(example);

		return list != null && !list.isEmpty();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public VoteVO composeVote (Vote vote, String userId){
		VoteVO voteVO = new VoteVO();
		BeanUtils.copyProperties(vote, voteVO);
		// 添加作者信息
		UserVO userVO = userService.getUserById(userId);
		voteVO.setNickname(userVO.getNickname());
		voteVO.setFaceImg(userVO.getFaceImg());
		voteVO.setFaceImgThumb(userVO.getFaceImgThumb());

		List<VoteImage> images = voteImageMapper.getVoteImgs(voteVO.getId());
		List<VoteOption> options = voteOptionMapper.getOptions(voteVO.getId());
		// 添加图片列表
		if (!images.isEmpty()) {
			voteVO.setImgList(images);
		}
		// 添加选项列表
		voteVO.setOptionList(options);
		// 为每个投票添加用户是否投过票的布尔值
		voteVO.setIsUserVoted(isUserVoted(userId, voteVO.getId()));
		// 如果该用户投过票, 查询 选择的 选项id
		if (voteVO.getIsUserVoted()) {
			voteVO.setSelectedOptId(selectedOptionId(userId, voteVO.getId()));
		}
		return voteVO;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public VoteVO getVoteById(String voteId, String userId) {

		Vote vote = voteMapper.selectByPrimaryKey(voteId);
		return composeVote(vote, userId);
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public VoteVO getSingleVote(String userId, String voteId) {

		VoteVO v = voteMapperCustom.getSpecifiedVote(voteId);
		// 为每个投票添加图片列表
		v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
		// 为每个投票添加选项列表
		v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		// 为每个投票添加用户是否投过票的布尔值
		v.setIsUserVoted(isUserVoted(userId, v.getId()));
		// 如果该用户投过票, 查询 选择的 选项id
		if (v.getIsUserVoted()) {
			v.setSelectedOptId(selectedOptionId(userId, v.getId()));
		}
		return v;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.VOTABLE.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void fDeleteVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.UNVOTEABLE.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void banVote(String voteId) {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", voteId);
		Vote vote = new Vote();
		vote.setStatus(VoteStatusEnum.UNQUALIFIED.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int updateVoteStatus(String voteIds, int status) {
		if (StringUtils.isEmpty(voteIds)) {
			return -1;
		}
		
		List<String> listId = Convert.toListStrArray(voteIds);
		
		Example example = new Example(Vote.class);
		example.createCriteria().andIn("id", listId);
		Vote a = new Vote();
		a.setStatus(status);
		return voteMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void selectOption(String userId, String optionId, String voteId) {
		VoteUser voteUser = new VoteUser();
		// 1.将投票信息存入数据库
		String id = sid.nextShort();
		voteUser.setCreateDate(new Date());
		voteUser.setId(id);
		voteUser.setOptionId(optionId);
		voteUser.setUserId(userId);
		voteUser.setVoteId(voteId);
		voteUserMapper.insertSelective(voteUser);
		// 2.1 vote的sum_vote(总投票数)+1
		voteMapper.addTotalVoteNum(voteUser.getVoteId());
		// 2.2 得到vote中的总票
		VoteVO voteVO = voteMapperCustom.getVoteById(voteUser.getVoteId());
		Integer totalVoteNum = voteVO.getSumVote();
//		System.out.println(totalVoteNum);
		// 3.vote_option表中的对应选项的count+1
		voteOptionMapper.addCorrespondingOptionCount(voteUser.getOptionId());
		// 4.更新vote_option表中该投票的每个选项的percent值
		Example example = new Example(VoteOption.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("voteId", voteUser.getVoteId());
		List<VoteOption> list = voteOptionMapper.selectByExample(example);
		for (VoteOption vu : list) {
			Integer optionTotalCount = vu.getCount();
//			System.out.println(optionTotalCount);
			Double percent =((double)optionTotalCount/totalVoteNum);
//			System.out.println(percent);
			voteOptionMapper.updatePercent(vu.getId(), percent);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllMyHisVote(Integer page, Integer pageSize, String userId) {

		PageHelper.startPage(page, pageSize);
		
		List<VoteVO> list = voteMapperCustom.queryAllMyHisVote(userId);
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		PageInfo<VoteVO> voteList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(voteList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(voteList.getTotal());

		return pagedResult;
		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getOtherslegalHisVote(Integer page, Integer pageSize, String userId, String targetId) {
		
		PageHelper.startPage(page, pageSize);
		List<VoteVO> list = voteMapperCustom.queryOthersLegalHisVote(targetId);
		for (VoteVO v : list) {
			// 为每个投票添加图片列表
			v.setImgList(voteImageMapper.getVoteImgs(v.getId()));
			// 为每个投票添加选项列表
			v.setOptionList(voteOptionMapper.getOptions(v.getId()));
		}
		PageInfo<VoteVO> voteList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(voteList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(voteList.getTotal());

		return pagedResult;
		
	}

	/**
	 * 每8分钟跟新投票状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void autoUpdateVoteStatus() {
		Example example = new Example(Vote.class);
		Criteria criteria = example.createCriteria();
		criteria.andLessThan("expiryDate", new Date());

		Vote vote = new Vote();
		vote.setStatus(StatusEnum.DELETED.type);
		voteMapper.updateByExampleSelective(vote, example);
	}

	/**
	 * 判断用户可否投票（红墙投票业务）
	 * @param userId
	 * @return 用户投票剩余票数
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public int userLeftVote(String voteId, String optionId, String userId) {
		//获取当前日期
		Date now = new Date( );
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		String today = ft.format(now);
//		System.out.println("当前时间为: " + today);
		Date expire = null;
		try {
			expire = ft.parse("2020-09-22");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//投票过期时间
		if (now.getTime() > expire.getTime()){
			return -100;
		}


		Example example = new Example(VoteUser.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("voteId", voteId);
		criteria.andEqualTo("userId", userId);
		criteria.andGreaterThan("createDate", today);
		int size = voteUserMapper.selectByExample(example).size();

		int voteNum = 0;
		//查询用户所属组
		if (StringUtils.isBlank(userService.getUserById(userId).getEmail())){
			//无邮箱，游客
			voteNum = 1;
		}else{ //有邮箱，认证用户
			voteNum = 3;
		}

		//临时补票
		boolean isDayTrue = today.equals("2020-09-20");
		if (optionId.equals("20200916option22" ) && isDayTrue){
			voteNum = 2*voteNum;
		}

		return voteNum - size;
	}
}











