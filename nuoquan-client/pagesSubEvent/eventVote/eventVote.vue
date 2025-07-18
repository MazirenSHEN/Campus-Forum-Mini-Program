/*
	#投票活动落地页
	#Author: Guetta
	#Update Log: Guetta 2020.9.13 v2.0.2
*/
<template>
	<view class="eventVote">
		<nqBanner :origin="'redWallEvent'"></nqBanner>		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :left-text="lang.back" :title="lang.redWallEvent" :showLeftIcon="true" :isNavHome="isNavHome"
		 :height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }" style="width: 100%;"></view>
		<!-- 标题 -->
		<view class="eventTitle">
			<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E6%A0%87%E9%A2%98%E5%B0%8F.png" mode="aspectFit"></image>
		</view>
		<!-- 小组标题 -->
		<view class="contentBox">
			<text class="bigSizeTitle">大赛奖品</text>
		</view>
		<!-- 红墙奖品 -->
		<view class="eventGift">
			<image class="image1" src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E5%A5%96%E5%93%81%E6%96%B0.png" mode="aspectFit"></image>
		</view>
		<!-- 小组标题 -->
		<view class="contentBox">
			<text class="bigSizeTitle">TOP 3 Teams</text>
		</view>
		<!-- 前三名 -->
		<view class="topThreeTeam">
			<!-- 
				宽度= （屏幕宽 - 两边页面边距32px - 卡片间边距 8 px * 3）/ 3
			 -->
			<view class="oneTopTeam" v-for="(topTeam , i) in topThreeList" :key="i" v-bind:topThreeList="topTeam"
				:style="{ 'background-color': i == 1 ? 'rgba(213,91,80,0.8);' : 'rgba(255,255,255,0.8);',
				 'width': topTeamCardWidth + 'px;',
				 'height': i == 1 ? topTeamCardWidth * 210/103 + 'px;' : topTeamCardWidth * 190/103 + 'px;'}"
			>
				<view class="topCount" v-if="i == 0" :style="{width: 40 + 'px',height: 40 + 'px',top: -10 + 'px'}" >
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No2.PNG" mode="aspectFit"></image>
				</view>
				<view class="topCount" v-else-if=" i == 1" :style="{width: 48 + 'px',height: 48 + 'px',top: -24 + 'px'}" >
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No1.PNG" mode="aspectFit"></image>
				</view>
				<view class="topCount" v-else :style="{width: 40 + 'px',height: 40 + 'px',top: -10 + 'px'}">
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No3.PNG" mode="aspectFit"></image>
				</view>
				<!-- 照片 -->
				<view class="photeBox">
					<!-- <image :src="pathFilter(topTeam.img)" mode="aspectFill"></image> -->
					<image :src="pathFilter(topTeam.img)" mode="aspectFill"></image>
				</view>
				<!-- 组名 -->
				<view class="teamName super_center">
					<text>{{topTeam.title}}</text>
					<text style="font-size: 16px;font-weight: 550;height: 16px;line-height: 16px;margin-top: 6px;">{{topTeam.count}} {{lang.vote}}</text>
				</view>
				<!-- jiangbei -->
				<view class="winnerCup" v-if="i == 0">
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No2winnerCup.png" mode="aspectFit"></image>
				</view>
				<view class="winnerCup" style="height: 136upx;width: 100%;" v-if="i == 1">
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No1winnerCup.png" mode="aspectFit"></image>
				</view>
				<view class="winnerCup" style="height: 112upx;width: 100%;" v-if="i == 2">
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/No3winnerCup.png" mode="aspectFit"></image>
				</view>
				
			</view>
		</view>
		<!-- 小组标题 -->
		<view class="contentBox" style="position: relative;">
			<text class="bigSizeTitle" style="position: absolute;top: -20px;left: 0;width: auto;">参赛小组</text>
			<!-- Refresh Button -->
			<button class="topright" @click="getTeamList()">
				<text class="refresh">{{lang.refresh}}</text>
				<image class="icon" src="../../static/icon/refresh-ffc041.png"></image>
			</button>
		</view>
		<!-- 投票卡片 -->
		<view class="cardsList">
			<voteCard class="super_center" @eventData="dataControl" v-for="(cardInfo,index) in teamList" :key="index" v-bind:cardInfo="cardInfo"
			:cardWidth="cardWidth" :cardHeight="cardHeight"></voteCard>
		</view>
		<!-- 投票规则 -->
		<view class="contentBox">
			<view class="content" v-for="i in eventRules" :key="i">
				{{i}}
			</view>
		</view>
		<!-- 赞助商广告 -->
		<view class="contentBox">
			<text class="bigSizeTitle">特别鸣谢</text>
			<view class="adImages hor_center" v-for="(thisAd,index) in adLists" :key="index" v-bind:thisAd="thisAd">
				<image :src="thisAd.src" mode="aspectFit"></image>
				<text>{{thisAd.title}}</text>
			</view>
		</view>
		<!-- 尾部图 -->
		<view class="bottomBox"></view>
		<!-- 分享海报 -->
		<view v-if="share">
			<redWallPoster :team="thisCard" @unShow="toggleShare"></redWallPoster>
		</view>
		<!-- 队伍详情弹窗 -->
		<teamDetailCard v-if="showDetailCard" @eventData="dataControl" :cardInfo ="teamDetail"></teamDetailCard>
		<!-- 确认提示弹窗 -->
		<nqToast v-if="voteStatus == 500 || voteStatus == 200 || voteStatus == 415" :cardInfo ="teamDetail" :voteStatus="voteStatus" @killToast="dataControl" @sharePoster="dataControl"></nqToast>
	</view>
</template>

<script>
	import uniNavBar from '../../components/uni-nav-bar/uni-nav-bar.vue';
	import nqBanner from '@/components/nq-banner/nq-banner.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import voteCard from './picVoteCard.vue';
	import teamDetailCard from './teamDetailCard.vue';
	import nqToast from '../../components/nq-toast/nq-toast.vue';
	import redWallPoster from '@/components/shareposter/redwallposter.vue';
	
	
	var loadVoteOptionFlag = false; // 为加载加锁
	
	const voteParse = {
		parseObject: {
			title: /\[(.*)\]/,
			intro: /\{(.*)\}/,
		},
		parseTitle(str) {
			var stra = "";
			stra = voteParse.parseObject.title.exec(str);
			reture;
		}
	};
	
	export default {
		components:{
			uniNavBar,
			nqBanner,
			voteCard,
			teamDetailCard,
			nqToast,
			redWallPoster
		},
		data() {
			return {
				// 界面显示控制
				isNavHome: true, //判断导航栏左侧是否显示home按钮
				navbarHeight: 0, // 一次性储存navbarHeight
				screenWidth: 0, // 屏幕宽度
				screenHeight: 0, // 屏幕高度
				topTeamCardWidth: 0, // 前三名卡片宽度
				cardWidth: 0, // 投票卡片宽度
				cardHeight: 0, // 投票卡片高度
				showDetailCard: false, // 控制是否展示队伍详情弹窗
				share: false, // control share team picture
				// 活动规则
				eventRules: [
					"活动规则：",
					"1. 本次红墙拍照地点在思源报告厅二楼平台红墙。每参赛小组上传一张照片，可以配合一段文字说明，可使用PS对参赛作品进行创意修改，但注意保留照片中的红墙元素。",
					"2. 活动名次通过大众在Nottinghome平台上的投票评选得出。投票通道于9月18日晚19:00开启，9月21日23:59分关闭。",
					"3. 正式用户每人每天可投三票（自由分配），游客每人每天可投一票。",
					"4. 可生成一张分享卡片至微信朋友圈或发给朋友前来助力。",
					"5. 若出现刷票情况，取消此队伍参赛资格。"
				],
				// 广告列表
				adLists: [
					{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/houmianbao.jpg",
						title: "(猴面包摄影工作室)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/utopia1.jpg",
						title: "(Utopia 理想国)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/utopia2.jpg",
						title: "(Utopia 理想国)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/bar.jpg",
						title: "(Beer in Cup 精酿啤酒吧)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/yingka.png",
						title: "(忆佳影咖)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/yecha.png",
						title: "(爺茶)"
					},{
						src: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/yimei.jpg",
						title: "(童颜整形)"
					}
				],
				// 数据控制
				teamList:[], // 队伍列表
				userInfo: '', // 用户信息
				teamDetail: [], // From picVoteCard, parsed and sent to eventVote page
				topThreeList:[], // 前三名
				sortedList: [], //大到小排序过后的list
				voteStatus: 100, // 由 teamDetailCard 传来控制弹窗状态，默认值 100 为关闭
				thisCard: [], // from teamDetailCard, send to share component
			}
			
		},
		computed: {
			...mapState(['lang'])
		},
		onLoad(e) {
			var that = this;
			/**
			 * 界面显示控制
			*/
			
			// 一次性储存navbarHeight
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			uni.getSystemInfo({
				success: (res) => {
					that.screenWidth = res.screenWidth;
					that.screenHeight = res.screenHeight;
					// 计算前三名卡片宽度
					this.topTeamCardWidth = (that.screenWidth - 32 - 24)/3
					// 计算投票卡片宽高
					that.cardWidth = (that.screenWidth - 52)/2;
					that.cardHeight = that.cardWidth * 1.36;
				}
			})
			
			/**
			 * 数据控制
			*/
		   // 获取用户信息
		   var userInfo = this.getGlobalUserInfo();
		   if (this.isNull(userInfo)) {
		   	uni.redirectTo({
		   		url: '../../pages/signin/signin?origin=../../pagesSubEvent/eventVote/eventVote'
		   	});
		   	return;
		   } else {
		   	this.userInfo = userInfo; // 刷去默认值(若有)
		   }
		   // 获取队伍列表
			this.getTeamList();
			
			/** 
			 * 监听来自 picVoteCard 和 teamDetailCard 传入的 
			 * cardStatus，用于改变窗口状态
			*/
		},
		onShareAppMessage(res) {
			if (res.from === 'menu') {
				// 来自右上角菜单的分享
				return {
					title: '快来给' + this.userInfo.nickname + '的小组投票吧',
					path: '/pagesSubEvent/eventVote/eventVote?data='
				};
			}
		},
		onshow(){
			var userInfo = this.getGlobalUserInfo(); // 查看用户是否登录
			if (!this.isNull(userInfo)) {
				// 设置 userInfo 传给 mainpagetop 组件
				// 更新用户信息缓存... 查询用户信息，并分割邮箱更新到缓存
				this.queryUserInfo(userInfo.id);
			}
		},
		onHide() {
			this.voteStatus = 100; // 页面隐藏，关闭弹窗
		},
		
		methods: {
			// 获取队伍信息
			getTeamList(){
				var that = this;
				if (loadVoteOptionFlag) {
					return;
				}
				loadVoteOptionFlag = true;
				
				uni.showLoading({
					title: '加载中...'
				});
				setTimeout(() => {
					if (loadVoteOptionFlag) {
						loadVoteOptionFlag = false; // 解锁
						uni.hideLoading();
						uni.showToast({
							title: '网络未知错误',
							icon: 'none',
							duration: 1000
						});
					}
				}, 5000); // 延时5s timeout
				
				uni.request({
					url: that.$serverUrl + '/vote/getVoteById',
					method: 'POST',
					data: {
						voteId:"20200916",
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// debugger
						uni.hideLoading();
						loadVoteOptionFlag = false;
						// 清空列表
						that.teamList = [] 
						this.$nextTick(() => {
							that.teamList = res.data.data.optionList;
							// 计算top3, 插入排序(Insertion Sort) 大->小
							var sorted = JSON.parse(JSON.stringify(that.teamList)); //深拷贝
							for(var i=0; i<sorted.length-1;i++){
							   for(var j=i+1; j>0; j--){
								   if(sorted[j].count > sorted[j-1].count){
									   var temp = sorted[j-1];
									   sorted[j-1] = sorted[j];
									   sorted[j] = temp;
								   }else{//不需要交换
									   break;
								   }
							   }
							}
							// 2-1-3排列
							that.topThreeList[0] = sorted[1]
							that.topThreeList[0].title = voteParse.parseObject.title.exec(that.topThreeList[0].content)[1];
							that.topThreeList[1] = sorted[0]
							that.topThreeList[1].title = voteParse.parseObject.title.exec(that.topThreeList[1].content)[1];
							that.topThreeList[2] = sorted[2]
							that.topThreeList[2].title = voteParse.parseObject.title.exec(that.topThreeList[2].content)[1];
							
							that.sortedList = sorted
						})
					},
					fail: res => {
						uni.hideLoading();
						loadVoteOptionFlag = false;
						console.log('Page unirequest fail');
						console.log(res);
					}
				});
			},
			// 更改投票下方弹窗状态
			dataControl(object){
				var that = this;
				// debugger
				that.showDetailCard = object.showDetailCardstatus;
				// edit team information
				that.teamDetail = object.teamDetail;
				// compute gap
				for(var i=0; i<that.sortedList.length; i++){
					if(object.teamDetail.thisCard.id == that.sortedList[i].id){
						if(i==0){
							object.teamDetail.gap = 0;
						}else{
							object.teamDetail.gap = that.sortedList[i-1].count - that.sortedList[i].count;
						}
						break;
					}
				}
				// control toast status
				that.voteStatus = object.voteStatus;
				// refresh
				if(object.refreshList){
					this.getTeamList();
				}
				// control share picture
				console.log(object);
				if(object.share !== null){
					uni.hideLoading()
					that.share = object.share;
					that.thisCard = object.teamDetail.thisCard;
				}
			},
			
			
			//控制是否显示分享海报
			toggleShare() { 
				this.share = !this.share;
			},
		}
	}
</script>

<style>
	page{
		background-color:  #fafafa;
		width: 100%;
	}
	
	.eventVote{
		width: 100%;
	}
	
	/* 标题 */
	.eventTitle{
		width: 100%;
		max-height: 162px;
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF_%E5%A4%B4%E9%83%A8.jpg);
	}
	
	.eventTitle image{
		width: 82%;
		margin-left: 9%;
	}
	
	/* 前三名 */
	.topThreeTeam{
		display: flex;
		vertical-align: bottom;
		align-items: flex-end;
		width: calc(100% - 32px);
		padding: 0 16px;
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF%E9%87%8D%E5%A4%8D%E5%9B%BE_short.jpg);
	}
	
	.oneTopTeam{
		position: relative;
		margin: 0 4px 12px 4px;
		border-radius: 5px;
	}
	
	.topCount{
		position: absolute;
		left: 50%;
		right: auto;
		margin-left: auto;
		margin-right: auto;
		height: 40px;
	}
	
	.topCount image{
		width: 100%;
		height: 100%;
	}
	
	.photeBox{
		position: absolute;
		width: calc(100% - 8px);
		height: 128upx;
		top: 20px;
		left: 4px;
	}
	
	.photeBox image{
		width: 100%;
		height: 100%;
	}
	
	.teamName{
		position: absolute;
		display: flex;
		flex-direction: column;
		top: 182upx;
		width: calc(100% - 8px);
		left: 4px;
	}
	
	.teamName text{
		height: 14px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: #4A4A4A;
		opacity: 1;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		text-overflow: ellipsis;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
	
	.winnerCup{
		position: absolute;
		bottom: 0;
		height: 120upx;
		width: 100%;
	}
	
	.winnerCup image{
		height: 100%;
		width: 100%;
	}
	
	/* 奖品 */
	.eventGift{
		width: 100%;
		height: auto;
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF%E9%87%8D%E5%A4%8D%E5%9B%BE_short.jpg);
	}
	
	.eventGift .image1{
		width: calc(100% - 32px);
		margin-left: 16px;
		height: 680upx;
		background-color: rgba(236,236,236,0.8);
	}

	
	/* 循环卡片 */
	.cardsList{
		width: calc(100% - 16px);
		padding: 16px 8px;
		/* 网格布局 */
		display: grid;
		grid-template-columns: repeat( 2 , 50% );
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF%E9%87%8D%E5%A4%8D%E5%9B%BE_short.jpg);
	}
	
	.contentBox{
		width: calc(100% - 40px);
		padding: 10px 20px;
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF%E9%87%8D%E5%A4%8D%E5%9B%BE_short.jpg);
	}
	
	.adImages{
		margin-top: 20px;
		width: 100%;
		height: 760upx;
		display: flex;
		flex-direction: column;
	}
	
	.adImages image{
		width: 100%;
		height: 100%;
	}
	
	.adImages text{
		height: 14px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 14px;
		color: #4A4A4A;
		opacity: 1;

	}	
	
	.contentBox .content{
		padding: 10px 20px;
		font-size: 17px;
		line-height: 17px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		color: #4A4A4A;
		opacity: 1;
		background-color: rgba(236,236,236,0.8);
	}
	
	.bigSizeTitle{
		width: 100%;
		text-align: center;
		padding: 20px 20px;
		height: 29px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 550;
		line-height: 29px;
		color: #4A4A4A;
		letter-spacing: 3px;
		opacity: 1;
	}
	
	.topright {
		position: absolute;
		right: 16px;
		top: 0;
		min-width: 82px;
		height: 26px;
		line-height:20px;
		background-color: #FAFAFA;
		opacity: 0.9;
		border-width: 1upx;
		border-radius: 4px;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: 10%;
		margin-right: 10px;
	}
	
	.refresh{
		font-size:14px;
		color: #888888;
	}
	
	.icon{
		width: 14px;
		height: 14px;
		padding-left: 8px;
	}
	
	.bottomBox{
		width: calc(100% - 40px);
		height: 920upx;
		padding: 10px 20px;
		background-size: 100% auto;
		background-image: url(https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/%E6%8A%95%E7%A5%A8_%E8%83%8C%E6%99%AF_%E5%B0%BE%E9%83%A8.jpg);
	}
		
</style>
