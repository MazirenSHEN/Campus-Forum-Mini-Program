<template>
	<view class="index">
		<!-- <nqBanner :origin="'index'"></nqBanner>
 -->
		<!-- Main page top bar -->
		<mainpagetop @transQueryType="changeQueryType" @transOrderType="changeOrderType" @selectedTag="getSelectedTag"
		 @deleteTag="deleteTag" :userInfo="userInfo" :topArticles="topArticles" :tagList="tagList" :roleup="roleup" :height="capsuleButton.bottom + 176"
		 :height_roled="capsuleButton.bottom + 64" style="position: fixed;z-index: 30;height:100%;">
		</mainpagetop>
		<!-- <button type="primary" @click="goTop" style="position: fixed;tsssop: 200spx;z-index: 88;">gotop</button> -->
		<view class="indexSelf" style="height:100%;">
			<scroll-view @scroll="linkageWithTop" class="indexArticleArea" :scroll-top="scrollTop" scroll-y="true"
			 @scrolltolower="loadMore" @scrolltoupper="refreshArticle" upper-threshold="5">
				<!-- <view :style="{height:capsuleButton.bottom + 111 + 'px',width: 100 + '%' }"></view> -->
				<view :style="{height:capsuleButton.bottom + 176 + 'px',width: 100 + '%' }"></view>

				<articlebrief v-for="i in showlist" :key="i.id" v-bind:articleCard="i"></articlebrief>
				<!-- 用于添加底部空白 by Guetta 9.10 -->
				<view class="marginHelper"></view>
			</scroll-view>
		</view>
		<tab-bar @clickTab="onClickTab"></tab-bar>
	</view>
</template>

<script>
	import articlebrief from '../../components/articlebrief.vue';
	import mainpagetop from '../../components/mainpagetop.vue';
	import mainpageleft from '@/components/mainpageleft.vue';
	import {
		mapState
	} from 'vuex';
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import nqBanner from '@/components/nq-banner/nq-banner.vue';

	var loadArticleFlag = false; // 为加载文章加锁
	var timer = null; // 为头部做定时器收起
	export default {
		data() {
			return {
				title: 'Hello',
				hottitlelist: ['热门标题1', '热门标题2', '热门标题3'],
				showlist: [],
				tagList: '',
				topArticles: '',
				roleup: false,

				queryType: 0,
				orderType: 0,

				userInfo: '',
				totalPage: 1,
				currentPage: 1,
				scrollTop: -1,
				old: {
					scrollTop: 0
				},
				capsuleButton: '',

				selectedTag: '',
			};
		},
		components: {
			articlebrief,
			mainpagetop,
			mainpageleft,
			tabBar,
			nqBanner
		},

		onLoad() {
			console.log('this.getnavbarHeight()=' + this.getnavbarHeight());
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../signin/signin'
				});
				return;
			} else {
				this.userInfo = userInfo; // 刷去默认值(若有)
			}
			this.updateLatestLoginTime(); //更新登陆时间

			this.mySocket.init(); // 初始化 Socket, 离线调试请注释掉

			this.capsuleButton = this.getnavbarHeight(); //获取胶囊按钮信息

			this.showArticles(this.currentPage); // 显示文章流

			uni.$on('flash', () => {
				// from submit
				this.refreshArticle();
			});

			this.getTagsList(); //获取标签列表
			// [测试代码块]
			uni.getStorageInfo({
				success: function(res) {
					console.log("缓存使用情况：")
					console.log(res.keys);
					console.log("currentSize=" + res.currentSize);
					console.log("limitSize=" + res.limitSize);
				}
			});
		},
		
		onShareAppMessage(res) {
			if (res.from === 'menu'){
				return{
					title: '来轮滑看看吧',
					path: '/pages/tabPages/index'
				}
			}
		},
		
		onShareTimeline(res){
			if (res.from === 'menu'){
				return{
					title: '来轮滑看看吧',
					path: '/pages/tabPages/index'
				}
			}
		},
		
		onUnload() {
			// 移除监听刷新事件
			uni.$off('flash');
		},

		onShow() {
			var userInfo = this.getGlobalUserInfo(); // 查看用户是否登录
			if (!this.isNull(userInfo)) {
				// 设置 userInfo 传给 mainpagetop 组件
				// 更新用户信息缓存... 查询用户信息，并分割邮箱更新到缓存
				this.queryUserInfo(userInfo.id);
			}

			this.getTop10Articles(); // 获取热度榜（刷新）
		},

		// onPullDownRefresh() {
		// 	console.log("监听到下拉动作")
		// },

		methods: {
			onClickTab(e) {
				//刷新
				if (e.url == "/" + this.getCurrentPage().route) {
					setTimeout(() => {
						this.goTop()
					}, 200)
					
					this.showArticles(1);
				}
			},

			showArticles: function(page) {
				if (loadArticleFlag) {
					return;
				}
				loadArticleFlag = true;

				uni.showLoading({
					title: '加载中...'
				});
				setTimeout(() => {
					if (loadArticleFlag) {
						loadArticleFlag = false; // 解锁
						uni.hideLoading();
						uni.showToast({
							title: this.lang.networkError,
							icon: 'none',
							duration: 1000
						});
					}
				}, 5000); // 延时5s timeout

				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/queryArticles',
					method: 'POST',
					data: {
						page: page,
						// pageSize: '',
						userId: that.userInfo.id,
						queryType: that.queryType,
						orderType: that.orderType,
						selectedTag: that.selectedTag
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						uni.hideLoading();
						loadArticleFlag = false;

						console.log(res);
						// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
						if (page == 1) {
							that.showlist = [];
						}
						this.$nextTick(() => {
							var newArticleList = res.data.data.rows;
							var oldArticleList = that.showlist;
							that.showlist = oldArticleList.concat(newArticleList);
							that.currentPage = page;
							that.totalPage = res.data.data.total;
						})
					},
					fail: res => {
						uni.hideLoading();
						loadArticleFlag = false;

						console.log('index unirequest fail');
						console.log(res);
					}
				});
			},

			loadMore: function() {
				// nq-tabbar.selectIcon = '/static/icon/arrow-up-FFFFF.png'
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// that.showArticles(1);
					uni.showToast({
						title: '没有更多文章咯',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					that.showArticles(page);
				}
			},
			refreshArticle: function() {
				uni.showNavigationBarLoading();
				this.showArticles(1);
				uni.hideNavigationBarLoading();
			},
			getTop10Articles() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getHotTop10',
					method: 'POST',
					data: {
						userId: that.userInfo.id,
						page: 1,
						pageSize: 10
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log('top articles:');
						console.log(res);
						that.topArticles = res.data.data.rows;
					}
				});
			},

			/**
			 * 查询用户信息，并分割邮箱更新到缓存
			 */
			queryUserInfo(userId) {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/queryUser',
					method: 'POST',
					data: {
						userId: userId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						// console.log("用户信息");
						// console.log(res);
						if (res.data.status == 200) {
							var user = res.data.data;
							var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
							this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
							this.userInfo = finalUser; // 更新页面用户数据
							// console.log(this.userInfo);
						}
					}
				});
			},

			updateLatestLoginTime() {
				uni.request({
					url: this.$serverUrl + '/user/updateLatestLoginTime',
					method: 'POST',
					data: {
						userId: this.userInfo.id
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log("更新用户最近登录时间");
						console.log(res);
					}
				})
			},

			/**
			 * 获取标签列表
			 */
			getTagsList() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getTagsList',
					method: 'POST',
					data: {},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if (res.data.status == 200) {
							that.tagList = res.data.data;
							console.log(that.tagList);
						}
					}
				});
			},

			linkageWithTop: async function(e) {
				var y = e.detail.scrollTop; //获取 scrollTop
				// console.log( y + "scrollTop" )
				// console.log(timer + "//  timer");
				var that = this;
				// 当下滑超过160后，顶部热门讨论及标签部分全部向上隐藏
				if (y >= 160) {
					that.roleup = true;
					// console.log(that.roleup);
				} else {
					that.roleup = false;
					// console.log(that.roleup);
				}
			},

			goTop: function(e) {
				this.scrollTop = this.old.scrollTop;
				this.$nextTick(function() {
					this.scrollTop = 0;
				});
				setTimeout(() => {
					this.scrollTop = -1;
				}, 200)
			},

			// 接收mainpageTop传过来的queryType并赋值, 一旦调用此方法, 重新刷新页面并获取文章.
			changeQueryType: function(queryType) {
				this.queryType = queryType;
				console.log('queryType:' + this.queryType);
				this.showArticles(1);
			},
			// 接收mainpageTop传过来的orderType并赋值, 一旦调用此方法, 重新刷新页面并获取文章.
			changeOrderType: function(orderType) {
				this.orderType = orderType;
				console.log('orderType:' + this.orderType);
				this.showArticles(1);
			},
			getSelectedTag(tag) {
				this.selectedTag = tag.tag;
				console.log(tag)
				this.showArticles(1);
			},
			deleteTag() {
				this.selectedTag = '';
				this.showArticles(1);
			}
		}
	};
</script>
<style>
	page {
		height: 100%;
		width: 100%;
	}
</style>

<style scoped>
	.index {
		/* 页面高度由内容扩充，最低值为100%（page 定义的）- by Guetta */
		height: 100%;
		width: 100%;
		background-color: #fdfdfd;
	}

	.indexArticleArea {
		height: 100%;
	}

	image {
		width: 60px;
		height: 60px;
		display: inline-block;
	}

	.touxiang {
		border-radius: 30px;
	}

	.search {
		display: inline-block;
		width: 70%;
		box-shadow: inset 0 0 20px #ccc;
		border-radius: 20upx;
		margin-left: 50upx;
	}

	.hot {
		background-color: #e4e4e4;
		width: 100%;
	}

	.hotitem {
		margin-bottom: 20px;
		background: white;
		height: 27px;
		border-color: rgb(255, 255, 255);
		box-shadow: rgb(170, 170, 170) 0px 0px 5px 0px;
		font-size: 14px;
		padding: 0px;
		border-width: 1px;
		border-style: solid;
		text-align: left;
		line-height: 20px;
		font-weight: normal;
		font-style: normal;
	}

	.arrow {
		width: 100upx;
		height: 60upx;
	}

	.marginHelper {
		height: 15upx;
		margin-top: 15upx;
		width: 100%;
		background-color: #f3f3f3;
	}
</style>
