<template>
	<view style="width: 100%;height: 100%;">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.myPublish" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>

		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }" style="width: 100%;"></view>
		<scroll-view scroll-y="true" class="scrollPage" @scrolltolower="loadMore()" :style="{ height: scrollHeight}" style="pointer-events: auto;">

			<view class="swiperMenu">
				<view :class="[swiperViewing == 'article' ? 'swiperChoosen' : 'swiperNormal']" @tap="switchSwiper('article')">{{lang.article}}
					{{ myArticleList.length }}</view>
				<!-- 
				 Author: Yifei
				 Date: July 6, 2022
				 Description: 发文章现在没了（longarticle）所以也不用查看我发布的文章了
				 -->
				<!-- <view :class="[swiperViewing == 'longArticle' ? 'swiperChoosen' : 'swiperNormal']" @tap="switchSwiper('longArticle')">{{lang.longArticle}}
					{{ myLongArticleList.length }}
				</view> -->
				<text class="deleteHint">{{lang.deleteHint}}</text>
			</view>
			<view>
				<!-- <swiper style="width:100%;height:100%;" :current-item-id="swiperViewing" disable-touch="true" @touchmove.prevent="stopTouch">
				<swiper-item style="width: 100%;" item-id="article" @touchmove.prevent="stopTouch"> -->
				<!-- <view class="mainbody articleArea" v-show="swiperViewing == 'article'">
					<view style="height:20px;width:100%;"></view>
					<modify-article v-for="article in myArticleList" :key="article.id" :thisArticle="article" :lang="lang"
					 @modifySwipedId="receiveSwiped" :messageIndex="messageIndex">
					</modify-article>
				</view> -->
				<view class="mainbody articleArea">
					<view style="height:20px;width:100%;"></view>
					<modify-article v-for="article in myArticleList" :key="article.id" :thisArticle="article" :lang="lang"
					 @modifySwipedId="receiveSwiped" :messageIndex="messageIndex">
					</modify-article>
				</view>
				<!-- </swiper-item>
				<swiper-item style="width: 100%;" item-id="vote" @touchmove.prevent="stopTouch"> -->

				<!-- <view class="mainbody voteArea" v-show="swiperViewing=='longArticle'">
					<view style="height:20px;width:100%;"></view> -->
					<!-- 					<modify-vote :lang="lang" v-for="vote in myVoteList" :key="vote.id" :vote="vote" :messageIndex="messageIndex"></modify-vote> -->
					<!-- <modify-long :lang="lang" v-for="longArticle in myLongArticleList" :key="longArticle.id" :thisArticle="longArticle"
					@modifySwipedId="receiveSwiped" :messageIndex="messageIndex" ></modify-long>

				</view> -->

				<!-- 				</swiper-item>-->
				<!-- 			</swiper> -->
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import modifyArticle from '../../components/nq-card/modify-article.vue';
	import modifyLong from '../../components/nq-card/modify-longArticle.vue'
	//import modifyVote from '../../components/nq-card/modify-vote.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';

	var loadArticleFlag = false;
	var loadLAFlag = false;
	export default {
		components: {
			modifyArticle,
			modifyLong,
			uniNavBar
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				pageTitle: '我的发布',
				userInfo: '',
				binNum: '12',
				//帖子
				totalPage: 1,
				currentPage: 1,
				totalNum: '0',
				myArticleList: [],
				messageIndex: "",
				//投票
				totalPageVote: 1,
				currentPageVote: 1,
				totalNumVote: '0',
				myVoteList: [],
				//长文章
				totalPageLA: 1,
				currentPageLA: 1,
				totalNumLA: '0',
				myLongArticleList: [],
				messageIndexLA: "",

				swiperViewing: 'article',
				isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮

				scrollHeight: "",
			};
		},

		onLoad() {
			// var that = this;
			var userInfo = this.getGlobalUserInfo();
			// console.log(userInfo);
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../signin/signin'
				});
				return;
			} else {
				this.userInfo = userInfo; // 刷去默认值(若有)
			}

			this.mySocket.init(); // 初始化 Socket, 离线调试请注释掉
			var page = this.currentPage;
			this.showArticles(page);
			//this.showVotes();
			// this.showLA(this.currentPageLA);

			uni.$on('refresh', () => {
				this.showArticles(1);
				// this.showLA(1);
			});
			console.log(this.getnavbarHeight());


			//计算滚动部分高度
			uni.getSystemInfo({
				success: function(res) {
					console.log(res);
					this.scrollHeight = res.windowHeight - this.getnavbarHeight().bottom - 5 + 'px';
					console.log(that.scrollHeight);
				}
			});
		},

		methods: {
			stopTouch(e) {
				console.log(e);
				return false;
			},
			loadMore(mode) {
				if (this.swiperViewing == 'article') {
					this.loadMoreArticle();
				} else if (this.swiperViewing == "vote") {
					this.loadMoreVote();
				}
			},
			// 锁
			showArticles: function(page) {
				if (loadArticleFlag) {
					loadArticleFlag = false;
				}

				loadArticleFlag = true;

				uni.showLoading({
					title: '加载中...'
				});
				setTimeout(() => {
					if (loadArticleFlag) {
						loadArticleFlag = false; //解锁
						uni.hideLoading();
						uni.showToast({
							title: '网络未知错误',
							icon: 'none',
							duration: 1000
						});
					}
				}, 5000); //延时五秒timeout

				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/queryPublishHistory',
					method: 'POST',
					data: {
						page: page,
						userId: that.userInfo.id,
						targetId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						setTimeout(() => {
							//延时加载
							uni.hideLoading();
							loadArticleFlag = false;
							
							// debugger
							console.log(res);
							if (page == 1) {
								that.myArticleList = [];
							}
							var newArticleList = res.data.data.rows;
							var oldArticleList = that.myArticleList;
							that.myArticleList = oldArticleList.concat(newArticleList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
							that.totalNum = res.data.data.records;
							console.log(that.totalNum);
						}, 300);
					},
					fail: res => {
						uni.hideLoading();
						loadArticleFlag = false;
						console.log('index unirequest fail');
					}
				});
			},
			loadMoreArticle: function() {
				var that = this;
				var currentPage = that.currentPage;
				var totalPage = that.totalPage;
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// that.showArticles(1);
					uni.showToast({
						title: '没有更多文章了',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					that.showArticles(page);
				}
			},
			// showVotes: function(page) {
			// 	var that = this;
			// 	uni.request({
			// 		url: that.$serverUrl + '/vote/queryPublishedVoteHistory',
			// 		method: 'POST',
			// 		data: {
			// 			page: 1,
			// 			userId: that.userInfo.id,
			// 			targetId: that.userInfo.id,

			// 		},
			// 		header: {
			// 			'content-type': 'application/x-www-form-urlencoded'
			// 		},
			// 		success: res => {

			// 			setTimeout(() => {
			// 				//延时加载
			// 				console.log(res);
			// 				if (page == 1) {
			// 					that.myVoteList = [];
			// 				}
			// 				var newVoteList = res.data.data.rows;
			// 				var oldVoteList = that.myVoteList;
			// 				that.myVoteList = oldVoteList.concat(newVoteList);
			// 				that.currentPageVote = page;
			// 				that.totalPageVote = res.data.data.total;
			// 				that.totalNumVote = res.data.data.records;
			// 				console.log(that.totalNum);
			// 			}, 300);
			// 		},
			// 		fail: res => {
			// 			uni.hideLoading();
			// 			loadArticleFlag = false;

			// 			console.log('index unirequest fail');
			// 			console.log(res);
			// 		}
			// 	});
			// },
			// loadMoreVote: function() {
			// 	var that = this;
			// 	var currentPage = that.currentPageVote;
			// 	var totalPage = that.totalPageVote;
			// 	// 判断当前页数和总页数是否相等
			// 	if (currentPage == totalPage) {
			// 		// that.showArticles(1);
			// 		uni.showToast({
			// 			title: '没有更多文章了',
			// 			icon: 'none',
			// 			duration: 1000
			// 		});
			// 	} else {
			// 		var page = currentPageVote + 1;
			// 		that.showVotes(page);
			// 	}
			// },

			// Describer: Yifei
			// Date: 13 Aug. 2022
			// Description: 3.0上线时 long article暂时删除，注释掉了
			//长文章
			// showLA: function(page) {
			// 	if (loadLAFlag) {
			// 		loadLAFlag = false;
			// 	}

			// 	loadLAFlag = true;

			// 	uni.showLoading({
			// 		title: '加载中...'
			// 	});
			// 	setTimeout(() => {
			// 		if (loadLAFlag) {
			// 			loadLAFlag = false; //解锁
			// 			uni.hideLoading();
			// 			uni.showToast({
			// 				title: '网络未知错误',
			// 				icon: 'none',
			// 				duration: 1000
			// 			});
			// 		}
			// 	}, 5000); //延时五秒timeout

			// 	var that = this;
			// 	uni.request({
			// 		url: that.$serverUrl + '/article/queryPublishHistory',
			// 		method: 'POST',
			// 		data: {
			// 			page: 1,
			// 			userId: that.userInfo.id,
			// 			targetId: that.userInfo.id,

			// 		},
			// 		header: {
			// 			'content-type': 'application/x-www-form-urlencoded'
			// 		},
			// 		success: res => {
			// 			console.log(res);
			// 			setTimeout(() => {
			// 				//延时加载
							
			// 				if (page == 1) {
			// 					that.myLongArticleList = [];
			// 				}
			// 				var newLAList = res.data.data.rows;
			// 				var oldLAList = that.myVoteList;
			// 				that.myLongArticleList = oldLAList.concat(newLAList);
			// 				that.currentPageLA = page;
			// 				that.totalPageLA = res.data.data.total;
			// 				that.totalNumLA = res.data.data.records;
			// 				console.log(that.totalNum);
			// 			}, 300);
			// 		},
			// 		fail: res => {
			// 			uni.hideLoading();
			// 			loadLAFlag = false;

			// 			console.log('index unirequest fail');
			// 			console.log(res);
			// 		}
			// 	});
			// },
			// loadMoreLA: function() {
			// 	var that = this;
			// 	var currentPage = that.currentPageVote;
			// 	var totalPage = that.totalPageVote;
			// 	// 判断当前页数和总页数是否相等
			// 	if (currentPage == totalPage) {
			// 		// that.showArticles(1);
			// 		uni.showToast({
			// 			title: '没有更多文章了',
			// 			icon: 'none',
			// 			duration: 1000
			// 		});
			// 	} else {
			// 		var page = currentPageVote + 1;
			// 		that.showVotes()(page);
			// 	}
			// },
			switchSwiper(a) {
				this.swiperViewing = a;
			},
			receiveSwiped(newId) {
				this.messageIndex = newId;
				console.log("refresh swiped ID = " + newId);
			},
			receiveSwipedLA(newId) {
				this.messageIndexLA = newId;
				console.log("refresh swiped ID = " + newId);
			}
		}
	};
</script>

<style>
	page {
		background: #ffffff;
		width: 100%;
		height: 100%;
		/* background: #f8f8f8; */
	}

	.top-bar {
		width: calc(100% - 58px);
		height: 30px;
		padding: 24px 0;
		display: flex;
		justify-content: space-between;
		font: Source Han Sans CN;
		margin: auto;
	}

	.totalNum {
		color: #888888;
		font-size: 18px;
		text-spacing: 80;
	}

	.swiperMenu {
		padding: 17px 0 17px 13px;
	}

	.swiperNormal {
		display: inline-block;
		height: 14px;
		width: 70px;
		margin: 0 16px;
		font-size: 11px;
		font-weight: 500;
		color: rgba(136, 136, 136, 1);
		text-align: center;
	}

	.swiperChoosen {
		display: inline-block;
		height: 14px;
		width: 70px;
		text-align: center;
		font-size: 11px;
		font-weight: 500;
		color: #ffffff;
		background: #ffc95a;
		padding: 5px 12px;
		margin: 0 4px;
		border-radius: 25px;
	}

	.scrollPage {
		width: 100%;
		/*height: calc(100% - 28px);*/
		height: 100%;
		background: #FFFFFF;
	}

	/* .bin{
	position: relative;
	display: inline-flex;
	background: #888888;
	border-radius: 10px;
	height: 30px;
	width: 108px;
	box-shadow: ;
}
.bin image {
	position: absolute;
	width: 14px;
	height: 15px;
	top: 7.5px;
	left: 9px;
	align-items: center;
}
.bin view {
	position: absolute;
	right: 14px;
	color: #ffffff;
	font-size: 14px;
	text-spacing: 45;
	align-items: center;
	line-height: 30px;
} */
	.mainbody {
		width: calc(100% - 26px);
		margin: auto;
	}
	
	.deleteHint {
		color: rgba(136, 136, 136, 1);
		font-size: 14px;
		position: absolute;
		right: 20px;
	}
</style>
