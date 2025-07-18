package com.nuoquan.scheduler;

import java.time.LocalDateTime;
import java.util.*;

import com.jupiter.mapper.LongarticleMapper;
import com.jupiter.pojo.Longarticle;
import com.nuoquan.enums.SignFlagEnum;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.SourceMapMapper;
import com.nuoquan.pojo.SourceMap;
import com.nuoquan.service.LongarticleService;
import com.nuoquan.utils.StringToDateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nuoquan.service.ArticleService;
import com.nuoquan.service.VoteService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 热度计算定时任务
 * @author jerrio
 */
@Component
@Configuration      // 主要用于标记配置类，兼备 Component 的效果。
@EnableScheduling   // 开启定时任务
@EnableAsync        // 开启多线程
public class MySchedular {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private com.jupiter.service.LongarticleService longarticleServiceJp;
	@Autowired
	private LongarticleService longarticleServiceNq;
	@Autowired
	private SourceMapMapper sourceMapMapper;

	@Async
	@Scheduled(cron = "0 */10 * * * ?") // 间隔10分钟
	public void articlePopularityAutoUpdate() {
//		System.err.println("按公式更新文章热度值... " + LocalDateTime.now());
		articleService.updatePopByFunction();
	}
	
	@Async
	@Scheduled(cron = "0 */8 * * * ?")
	public void autoUpdateVoteStatus(){
//		System.err.println("自动判断投票是否过期... " + LocalDateTime.now());
		voteService.autoUpdateVoteStatus();
	}

	@Async
	@Scheduled(cron = "0 */15 9,12,18,19,20,21,23 * * ?") // 每天 9，18，21点每隔15分钟
	public void fetchLongarticleFromJupiter() throws Exception{
		System.err.println("Start fetching longarticle from Jupiter... " + LocalDateTime.now());

		List<Longarticle> list = longarticleServiceJp.getUnsignArticle();
		for (Longarticle longarticleJp : list){
			com.nuoquan.pojo.Longarticle longarticleNq = new com.nuoquan.pojo.Longarticle();
			longarticleNq.setTitle(longarticleJp.getTitle());
			longarticleNq.setSubTitle(longarticleJp.getSubTitle());
			longarticleNq.setCoverImage(longarticleJp.getCoverImage());
			longarticleNq.setOriginal(0); // 设置成非原创
			longarticleNq.setTags(longarticleJp.getTags());
			longarticleNq.setContent(longarticleJp.getContent());
			longarticleNq.setStatus(StatusEnum.CHECKING.type); // 设置成待审核
			longarticleNq.setSource(longarticleJp.getSource());
			longarticleNq.setLink(longarticleJp.getLink());
			if (longarticleServiceNq.isRelatedOA(longarticleJp.getSource())){
				longarticleNq.setIsJump(1);
			} else {
				longarticleNq.setIsJump(0);
			}

			// Parse createDate, if format don't match set now.
			Date date = StringToDateUtil.stringToDate(longarticleJp.getCreateDate());
			if (date == null){
				date = new Date();
			}
			longarticleNq.setCreateDate(date);
			longarticleNq.setUserId(assignAuthor(longarticleJp.getSource())); // 设置运营预分配账号

//			System.out.println(longarticleJp.getId() + ", " + StringToDateUtil.stringToDate(longarticleJp.getCreateDate()));
			longarticleServiceNq.saveArticle(longarticleNq);

			//签收
			longarticleJp.setSignFlag(SignFlagEnum.SIGNED.type);
			longarticleServiceJp.updateByPrimaryKeySelective(longarticleJp);
		}

		System.err.println("Fetching longarticle from Jupiter completed." + LocalDateTime.now());
	}

	static Random random = new Random();

	@Transactional(propagation = Propagation.SUPPORTS)
	public String assignAuthor(String source){
		Example example = new Example(SourceMap.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("source", source);
		SourceMap sourceMap = sourceMapMapper.selectOneByExample(example);
		if (sourceMap == null){
			Example example2 = new Example(SourceMap.class);
			Example.Criteria criteria2 = example2.createCriteria();
			criteria2.andEqualTo("source", "default");
			sourceMap = sourceMapMapper.selectOneByExample(example2);
		}
		String ids = sourceMap.getAuthorId();
		String[] list = ids.split(",");
		String author = "";
		int size = list.length;
		if (size == 1){
			author = list[0];
		}else if (size > 1){
			int r = random.nextInt(size);
			author = list[r];
		}
		return author;
	}

//	 Example Code...
//	 @Async
//     @Scheduled(fixedDelay = 1000)  //间隔1秒
//     public void first() throws InterruptedException {
//         System.out.println("第一个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//         System.out.println();
//         Thread.sleep(1000 * 10);
//     }
//
//     @Async
//     @Scheduled(fixedDelay = 2000)
//     public void second() {
//         System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//         System.out.println();
//     }

}
