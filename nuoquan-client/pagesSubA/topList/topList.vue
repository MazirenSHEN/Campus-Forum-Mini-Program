<template>
	<view style="width:100%;height:100%;margin:auto;">
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.hotTitle" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>

		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }"></view>
		<view class="top">
			<text class="topleft">{{lang.nextRefresh}} {{minute+lang.min+second+lang.second}}</text>
			<button class="topright" @click="reload()">
				<text class="refresh">{{lang.refresh}}</text>
				<image class="icon" src="../../static/icon/refresh-ffffff.png"></image>
			</button>
		</view>
		<view class="mainbody">
			<block v-for="(thisArticle,index) in myTopicList" :key="thisArticle.id">
				<view class="oneArticle" @click="goToDetail(thisArticle)">

					<view class="title"> {{ thisArticle.articleTitle }}</view>
					<view class="cardBody">
						<view class="left-body">
							<view class="content">{{ thisArticle.articleContent }}</view>
							<view class="bottomBar">
								<view class="time">{{timeDeal(thisArticle.createDate)}}</view>
								<view class="like">
									
									<image src="../../static/icon/eye-888888.png"></image>
									<view>{{thisArticle.likeNum}}</view>
								</view>
								<view class="write">
									<image src="../../static/icon/edit-2c2c2c.png"></image>
									<view>{{thisArticle.likeNum}}</view>
								</view>
							</view>
						</view>
					</view>
					<view class="index">{{index}}</view>
				</view>
			</block>
		</view>
	</view>
</template>

<script>
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';

	export default {
		components: {
			uniNavBar
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				minute: '',
				second: '',
				totalPage: 1,
				currentPage: 1,
				loadArticleFlag: false,
				userInfo: '',
				myTopicList: '',
				isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮
			};
		},
		onLoad() {
			var userInfo = this.getGlobalUserInfo();
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
			this.nextRefresh();
		},
		methods: {
			nextRefresh: function() {
				var now = new Date();
				this.minute = 9 - (now.getMinutes() % 10);
				this.second = 59 - now.getSeconds();
				if (now.getMinutes() % 10 == 0 && now.getSeconds() == 0) {
					this.reload();
				}
				setTimeout(() => {
					this.nextRefresh();
				}, 1000);
			},
			reload: function() {
				this.showArticles(1);
			},
			// 锁
			showArticles: function(page) {
				console.log(this.loadArticleFlag);

				if (this.loadArticleFlag) {
					loadArticleFlag = false;
				}

				this.loadArticleFlag = true;

				uni.showLoading({
					title: '加载中...'
				});
				setTimeout(() => {
					if (this.loadArticleFlag) {
						this.loadArticleFlag = false; //解锁
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
					url: that.$serverUrl + '/article/getHotTop10',
					method: 'POST',
					data: {
						page: 1,
						pageSize: 10,
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						setTimeout(() => {
							//延时加载
							uni.hideLoading();
							this.loadArticleFlag = false;

							console.log(res);
							if (page == 1) {
								that.myTopicList = [];
							}
							var newTopicList = res.data.data.rows;
							var oldTopicList = that.myTopicList;
							that.myTopicList = oldTopicList.concat(newTopicList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
							that.totalNum = res.data.data.records;
							console.log(that.totalNum);
						}, 300);
					},
					fail: res => {
						uni.hideLoading();
						this.loadArticleFlag = false;

						console.log('index unirequest fail');
						console.log(res);
					}
				});
			},
			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
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
			backToMainPage: function() {
				uni.navigateBack({});
			},
			goToDetail: function(thisArticle) {
				//thisArticle用函数传入，因为v-for使用了ID为键名，所以无法筛选数据，就直接block传进来好了
				uni.navigateTo({
					url: '/pagesSubA/detail/detail?data=' + thisArticle.id
				});
			},
		}
	}
</script>
<!-- <style>
	page {
		background: #F8F8F8;
		position: relative;
	}
</style> -->

<style scoped>
	page {
		background: #F8F8F8;
		position: relative;
	}

	.top {
		width: 95%;
		height: 40px;
		display: flex;
		align-items: center;
		justify-content: space-between;
		position: static;
		margin-bottom: 10rpx;
		margin-top: 20rpx;
		margin-left: auto;
		margin-right: auto;
	}

	.topleft {
		font-size: 14px;
		margin-left: 30px;
		margin-right: 10%;
		height: 20px;
		width: 40%;
	}

	.mainbody {
		width: calc(100% - 26px);
		margin: auto;
	}

	.topright {
		min-width: 82px;
		height: 26px;
		line-height: 20px;
		background-color: #FFCF3C;
		border-width: 1upx;
		border-radius: 4px;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: 10%;
		margin-right: 10px;
	}

	.refresh {
		font-size: 14px;
		color: white;

	}

	.icon {
		width: 14px;
		height: 14px;
		padding-left: 8px;


	}

	/* 以下是单一话题卡片样式 */
	.pics {
		display: none;
	}

	.oneArticle {
		width: 98%;
		height: 136.4px;
		position: relative;
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		opacity: 1;
		border-radius: 8px;
		margin-bottom: 8px;
		margin-left: auto;
		margin-right: auto;
		z-index: 10;
	}

	.index {
		position: absolute;
		left: 16px;
		font-size: 48px;
		top: 10px;
		height: 48px;
		color: #eecacf;
		opacity: 1;
		z-index: 0;
		font-style: italic;
	}

	.title {
		position: relative;
		font-weight: 550;
		font-size: 17px;
		margin-left: 17px;
		color: #4a4a4a;
		padding-top: 19px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		font-weight: 600;
		z-index:10;
	}

	.cardBody {
		position: relative;
		/*position: relative;
			width: 100%;
			/* height:83px; */
		/*padding-top:10px;*/
		position: relative;
		height: 83px;
		padding-top: 12px;
		margin-left: 16px;
		margin-right: 16px;
		font-weight: 400;
	}

	.picArea {
		/*position: absolute;
			right: 10px;
			top: -10px;
			width: 61px;
			height: 61px;*/
		display: inline-block;
		width: 61px;
		height: 61px;
		position: absolute;
		top: 12px;
		right: 4px;
	}

	.picArea image {
		position: absolute;
		right: 0;
		top: 0;
		width: 61px;
		height: 61px;
	}

	.left-body {
		width: 100%;
		height: 70px;
	}


	.content {
		/*height: 40px;
			width: 70%;
			font-size: 12px;
			line-height: 13px;
			overflow: hidden;
			text-overflow: ellipsis;*/
		height: 39px;
		width: 100%;
		font-size: 12px;
		line-height: 13px;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.bottomBar {
		/*width: 100%;
			height: 15px;
			margin-bottom:15px;
			color:#9B9B9B;*/
		width: 100%;
		height: 10px;
		padding-top: 12px;
		padding-bottom: 12px;
		position: relative;
		color: #9b9b9b;
	}

	.time {
		/*width: 85px;
			/* 暂时,到  月-日 时:分*/
		/*height: 10px;
			line-height: 10px;
			font-size: 10px;
			position: absolute;
			left: 5px;
			bottom: 12px;
			width: 77px;
			/* 暂时,到  月-日 时:分*/
		height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 0;
		bottom: 12px;
	}


	.like {
		position: absolute;
		bottom: 12px;
		right: 35px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.write {
		position: absolute;
		bottom: 12px;
		right: 1px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}



	.like image,
	.write image {
		width: 11px;
		height: 11px;
		right: 0px;
		position: absolute;
		left: 0;
	}

	.like view,
	.write view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}
</style>
