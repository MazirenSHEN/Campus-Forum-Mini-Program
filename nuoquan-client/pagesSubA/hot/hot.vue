<template>
	<view style="width:100%;height:100%;margin:auto;">
		<uni-nav-bar class="navigationBar"
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		:left-text="lang.back"
		:title="lang.hotTitle" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>				
		
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }"></view>
		<view class="top">
			<text class="topleft">{{lang.nextRefresh}} {{minute+lang.min+second+lang.second}}</text>
			<button class="topright" @click="reload()">
				<text class="refresh">{{lang.refresh}}</text>
				<image class="icon" src="../../static/icon/refresh-ffffff.png"></image>
			</button>
		</view>
		<view class="mainbody"><articleInfo :myArticleList="myArticleList"></articleInfo></view>
	</view>
</template>

<script>
import articleInfo from './articleInfo.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import { mapState, mapMutations } from 'vuex';

export default {
	components: {
		articleInfo,
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
			myArticleList: '',
			isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
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
		}
	}
};
</script>

<style>
	page{
		background: #F8F8F8;
		position:relative;
	}
		
	.top {
		width:95%;
		height:40px;
		display:flex;
		align-items:center;
		justify-content: space-between;
		position:static;
		margin-bottom: 10rpx;
		margin-top: 20rpx;
		margin-left: auto;
		margin-right: auto;
	}
	.topleft{
		font-size:14px;
		margin-left:30px;
		margin-right: 10%;
		height: 20px;
		width: 40%;
	}
.mainbody{
	width:calc(100% - 26px);
	margin: auto;
}
.topright {
		min-width: 82px;
		height: 26px;
		line-height:20px;
		background-color: #FFCF3C;
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
		color: white;

	}
.icon{
	width: 14px;
	height: 14px;
	padding-left: 8px;
	

}
</style>
