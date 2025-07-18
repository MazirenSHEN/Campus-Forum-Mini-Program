<template>
	<view style="width: 100%;height: 100%;">
		<!-- 导航栏 -->
		<uni-nav-bar
			class="navigationBar"
			:style="{ height: this.getnavbarHeight() + 'px' }"
			:showLeftIcon="true"
			:isNavHome="isNavHome"
			:left-text="lang.back"
			:title="lang.myCollection"
			:height="this.getnavbarHeight().bottom + 5"
		></uni-nav-bar>

		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }" style="width: 100%;"></view>

		<view class="mainbody">
			<view class="myCollectionText"> {{ myArticleList.length }} {{lang.collectionNumSuffix}}</view>
		</view>		
		<!-- <scroll-view scroll-y="true" class="scrollPage"> -->
			<view class="mainbody_scroll" v-for="myArticle in myArticleList" :key="myArticle.id">
				<collection-card
					:articleDelieverer="myArticle" 
					:lang="lang" @modifySwipedId="receiveSwiped" 
					:swipedArticleId="swipedArticleId"
					:userInfo="userInfo">
				</collection-card>
			</view>
		<!-- </scroll-view> -->
	</view>
</template>

<script>
import collectionCard from '../../components/nq-card/collection-card.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import { mapState, mapMutations } from 'vuex';

var loadArticleFlag = false;
export default {
	components: {
		collectionCard,
		uniNavBar
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			userInfo: '',

			totalPage: 1,
			currentPage: 1,
			totalNum: '0',
			myArticleList: [],

			isNavHome: getApp().globalData.isNavHome ,//判断导航栏左侧是否显示home按钮
			swipedArticleId:"",
		};
	},

	onLoad() {
		var userInfo = this.getGlobalUserInfo();
		console.log(userInfo);
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
		uni.$on('refresh', () => {
			this.showArticles(1);
			// this.refresh();
		});
	},

	methods: {
		// Date: Aug 15, 2022
		// Author: Yifei
		// Description: 之前在删除收藏后的刷新会有点问题，就写一个刷新页面的方法吧
		refresh: function(){
			// 页面重载
		    const pages = getCurrentPages()
		    // 声明一个pages使用getCurrentPages方法
			const curPage = pages[pages.length - 1]
	        // 声明一个当前页面
		    curPage.onLoad(curPage.options) // 传入参数
	        curPage.onShow()
			curPage.onReady()
	        // 执行刷新
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
				url: that.$serverUrl + '/social/queryCollect',
				method: 'POST',
				data: {
					page: page,
					userId: that.userInfo.id,
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					setTimeout(() => {
						//延时加载
						uni.hideLoading();
						loadArticleFlag = false;
		
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
		loadMore: function() {
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

		receiveSwiped(newId){
			this.swipedArticleId = newId;
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

.myCollectionText{
	font-size:17px;
	font-weight: 600;
	color:rgba(136,136,136,1);
}

.scrollPage{
	width:100%;
	height: calc(100% - 28px);
	background: #FFFFFF;
}

.mainbody {
	width: calc(100% - 26px);
	margin-top: 8px;
	margin-bottom: 8px;
	margin-left: auto;
	margin-right: auto;
}

.mainbody_scroll{
	width: calc(100% - 26px);
	margin: auto;
}
</style>
