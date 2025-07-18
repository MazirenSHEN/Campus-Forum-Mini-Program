<!--
Description:  探索板块探索页面框架，学术/热门以组件形式引入切换
Author: yaoyao & jerrio
Date: 17 Aug, 2020
-->
<template>
	<view id="<public-contai></public-contai>ner">
		<searchpage v-if="showSearch" class="searchPage" @exitSearchSignal="controlShowSearch"></searchpage>

		<!-- 导航栏 -->
		<!-- 		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.explore" :height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }"></view> -->
		
		<!--头部栏, 搜索框和新建按钮-->
		<view class="topBar" :style="{ height: this.getnavbarHeight().top + 40 + 'px'}">
			<view class="topBarSearch" :style="{'margin-top': this.getnavbarHeight().top + 2 + 'px'}" @click="controlShowSearch(1)">
				<image src="../../static/icon/search_B79144.png" mode="aspectFit"></image>
			</view>
			<creatarticle :topHeight='this.getnavbarHeight().top'></creatarticle>
		</view>

		<!--切换菜单  学术/热门-->
		<view id="public-message-futherbox">
			<scroll-view class="top-menu-view" scroll-x="true" scroll-left="scrollLeft">
				<!-- <block v-for="(menuTab, index) in [{ name: lang.hotDoor }, { name: lang.academic }]" :key="index"> -->
				<block v-for="(menuTab, index) in [{ name: lang.hotDoor }]" :key="index">
					<view class="menu-one-view" v-bind:id="'tabNum' + index" @click="swichMenu(index)">
						<view :class="[currentTab == index ? 'menu-one-act' : 'menu-one']">
							<view class="menu-one-txt" @tap="goTop">{{ menuTab.name }}</view>
							<view class="menu-one-bottom">
								<view class="menu-one-bottom-color1" v-if="index == 0"></view>
								<view class="menu-one-bottom-color2" v-else></view>
							</view>
						</view>
					</view>
				</block>
			</scroll-view>
			<view style="height: 4px;width:100%;background: #fafafa;"></view> <!-- 占位留白 -->
			<swiper :current="currentTab" :style="{ height: windowHeight - (this.getnavbarHeight().top + 40) - 46 + 'px' }" class="swiper-box-list" duration="300" @change="swiperChange">
				
				<!-- 热门版块 -->
				<swiper-item class="swiper-box">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" enable-back-to-top="true" @scrolltolower="loadMore">
						<hotNewsCard v-if="!headlines.length.isNull" :headlines="headlines" :commentList="headlinesComments"></hotNewsCard>
						<view v-if="articleListOA.length > 0">
							<pageSubTitle :pageTitle="lang.officialAccountList" :withShowAll="true" :url="'../officialAccountList/officialAccountList'"></pageSubTitle>
							<campusNews :articleList="articleListOA"></campusNews>
						</view>
					
						<pageSubTitle :pageTitle="lang.recommendation" :withShowAll="false"></pageSubTitle>
						<recommendationCard
						v-for="i in longArticleList" 
						:key="i.index"
						v-bind:longArticle="i"></recommendationCard>
						<view style="height: 63px;width: 100%;"></view>
					</scroll-view>
				</swiper-item>
				
				<!-- 学术版块 -->
				<!-- <swiper-item class="swiper-box">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" enable-back-to-top="true">
						<academicarea></academicarea>
						<academiccardflow></academiccardflow>
						<view style="height: 63px;width: 100%;"></view>
					</scroll-view>
				</swiper-item> -->
			</swiper>
		</view>
		<tab-bar :current="1" @clickTab="onClickTab"></tab-bar>
	</view>
</template>

<script>
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	// 通用引用
	import pageSubTitle from '../../components/nq-titles/nq-pageSubTitles.vue'
	// 热门区引用
	import hotNewsCard from '../../components/nq-explore-hotNews/nq-explore-hotNews.vue'
	import campusNews from '../../components/nq-campusNews/nq-campusNews.vue'
	import recommendationCard from '../../components/nq-card/nq-hotCard.vue'
	// 学术区引用
	import academicarea from '../../components/nq-explore/academic_area.vue'
	import creatarticle from '../../components/newArticle+.vue'
	import searchpage from '../search/search.vue'
	import academiccardflow from '../../components/nq-explore/academicCardFlow.vue'
	var loadArticleFlag = false // 为文章加锁

	export default {
		data() {
			return {
				windowHeight: 0, // 屏幕高
				windowWidth: 0, // 屏幕宽
				loadArticleFlag: false, // 为文章加锁
				showSearch: 0,
				currentTab: 0,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				scrollLeft: 0,
				userInfo: '', // 当前用户信息
				// 文章流
				currentPage: 1, // 当前分页
				totalPage: 1, // 总页数
				queryType: 0, // 文章流排列方式
				selectedTag: '',
				
				headlines: {}, 			// 头条
				headlinesComments: {}, 	// 头条评论
				articleListOA: {},		// 公众号文章
				currentPageOA: 1, // 当前分页
				totalPageOA: 1, // 总页数
				longArticleList: [], 	// 全部文章信息
			};
		},
		
		components: {
			uniNavBar,
			tabBar,
			pageSubTitle, //页面副标题
			hotNewsCard, //热门头部卡片
			campusNews, //热门校园资讯版块
			recommendationCard, //热门文章流文章卡片
			academicarea, //学术板块
			creatarticle, //新建长文章
			searchpage, //搜索
			academiccardflow, //学术文章流
		},
		
		computed: {
			...mapState(['lang'])
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
			
			this.flash() //获取刷新内容
			
			// 这里命名为 windowHeight 以和组件中的 screenHeight 区分开
			var that = this;
			uni.getSystemInfo({
				success(res) {
					that.windowHeight = res.screenHeight;
					console.log(that.windowHeight);
					that.windowWidth = res.screenWidth;
				}
			})
		},
		
		onShow() {
			var userInfo = this.getGlobalUserInfo(); // 查看用户是否登录
		},
		
		methods: {
			/**
			 * 刷新方法
			 */
			flash: function(){
				this.getHeadlines(this.userInfo.id); // 获取头条
				this.getArticlesFromOA(1, this.userInfo.id); // 获取公众号news
				this.showLongArticles(1); // 获取推荐文章流
			},
			onClickTab(e) {
				//刷新
				if (e.url == "/" + this.getCurrentPage().route) {
					setTimeout(() => {
						this.goTop()
					}, 200)
					
					this.flash() //刷新
				}
			},
			
			controlShowSearch(a) {
				this.showSearch = a;
			},
			
			// 返回顶部 + 刷新
			goTop: function(e) {
				this.scrollTop = this.old.scrollTop;
				this.$nextTick(function() {
					this.scrollTop = 0;
				});
				setTimeout(() => {
					this.scrollTop = -1;
				}, 200)
			},
			
			// 获取推荐文章流 -Guetta
			showLongArticles: function(page){
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
								title: '网络未知错误',
								icon: 'none',
								duration: 1000
							});
						}
					}, 5000); // 延时5s timeout
				
					var that = this;
					uni.request({
						url: that.$serverUrl + '/longarticle/queryArticles',
						method: 'POST',
						data: {
							userId: that.userInfo.id,
							page: page,
							pageSize: '',
							queryType: that.queryType,
							selectedTag: ''
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							uni.hideLoading();
							loadArticleFlag = false;
				
							console.log("长文章 res", res.data.data.rows);
							// 判断当前页是不是第一页，如果是第一页，那么设置 longArticleList 为空
							if (page == 1) {
								that.longArticleList = [];
							}
							this.$nextTick(() => {
								var newLongArticleList = res.data.data.rows;
								var oldLongArticleList = that.longArticleList;
								that.longArticleList = oldLongArticleList.concat(newLongArticleList);
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
			
			// 切换热门、学术版块 - Yaoyao
			swichMenu: async function(current) {
				//点击其中一个 menu
				if (this.currentTab == current) {
					return false;
				} 
					this.currentTab = current;
					this.setScrollLeft(current);
				
			},
			
			/**
			 * 获取头条
			 * @param {Object} userId 操作用户
			 */
			getHeadlines: function(userId){
				uni.request({
					url: this.$serverUrl + '/longarticle/getHeadlines',
					method: 'POST',
					data: {
						userId: userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if(res.data.status == 200){
							this.headlines = res.data.data
							this.getComment(this.headlines, this.userInfo.id)
							console.log(this.headlines)
						}
					},
					fail: res => {
					
					}
				});
			},
			
			/**
			 * 获取对象评论
			 * @param {Object} object
			 * @param {Object} userId
			 */
			getComment: function(object, userId){
				//获取热评
				uni.request({
					url: this.$serverUrl + '/social/getMainComments',
					method: 'POST',
					data: {
						page: 1,
						pageSize: 5,
						type: 1, //0=按时间查询, 1=按热度查询
						targetType: "LONGARTICLE",
						targetId: object.id,
						userId:	userId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if(res.data.status == 200){
							this.headlinesComments = res.data.data.rows;
							console.log('headline 评论');
							console.log(this.headlinesComments);
						}
					},
					fail: res => {
					
					}
				});
			},
			
			/**
			 * 获取公众号文章
			 * @param {Object} userId
			 */
			getArticlesFromOA: function(page, userId){
				uni.request({
					url: this.$serverUrl + '/longarticle/getArticlesFromOA',
					method: 'POST',
					data: {
						page: 1,
						pageSize: 10,
						userId: userId,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if(res.data.status == 200){
							// console.log(this.articleListOA);
							// 判断当前页是不是第一页，如果是第一页，那么设置 列表 为空
							if (page == 1) {
								this.articleListOA = [];
							}
							this.$nextTick(() => {
								var newList = res.data.data.rows;
								var oldList = this.articleListOA;
								this.articleListOA = oldList.concat(newList);
								this.currentPageOA = page;
								this.totalPageOA = res.data.data.total;
							})
						}
					},
					fail: res => {
					
					}
				});
			},
			// 公众号板块，加载更多
			loadMoreOA: function() {
				var currentPage = this.currentPageOA;
				console.log(currentPage);
				var totalPage = this.totalPageOA;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// this.showLongArticles(1);
					uni.showToast({
						title: '没有更多内容了',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					this.getArticlesFromOA(page, this.userInfo.id);
				}
			},
			
			// 热门版块，加载更多
			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (currentPage == totalPage) {
					// that.showLongArticles(1);
					uni.showToast({
						title: '没有更多文章了',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					that.showLongArticles(page);
				}
			},
			
			swiperChange: async function(e) {
				let index = e.target.current;
				this.setScrollLeft(index);
				this.currentTab = index;
			},
			setScrollLeft: async function(tabIndex) {
				let leftWidthSum = 0;
				for (var i = 0; i <= tabIndex; i++) {
					let nowElement = await this.getWidth('tabNum' + i);
					leftWidthSum = leftWidthSum + nowElement.width;
					console.log(leftWidthSum);
				}
				let winWidth = uni.getSystemInfoSync().windowWidth;
				this.scrollLeft = leftWidthSum > winWidth ? leftWidthSum - winWidth : 0;
			},
			getWidth: function(id) {
				//得到元素的宽高
				return new Promise((res, rej) => {
					uni.createSelectorQuery()
						.select('#' + id)
						.fields({
								size: true,
								scrollOffset: true
							},
							data => {
								res(data);
							}
						)
						.exec();
				});
			},
		}

	}
</script>

<style>
	page{
		background-color: #fafafa;
	}
	
	#public-container {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: #FFFFFF;
	}

	#public-message-futherbox

	/* 这里是帖子块最高级父组件*/
		{
		/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
		position: fixed;
		width: 100%;
		height: 100%;
	}

	/** 头部导航栏，搜索 加号**/
	.topBar {
		width: 100%;
		background-color: #fafafa;
		position: relative;
		/* height: 30px; */
		/* margin-top: 23px; */
		/* 此处需要兼容性处理 47px */
	}

	.topBarSearch {
		height: 32px;
		background: rgba(230,230,230,1);
		opacity: 1;
		border-radius: 10px;
		width: calc(100% - 3.47% - 101px - 32px - 7px);
		font-size: 15px;
		display: inline-block;
		vertical-align: middle;
		margin-left: 3.47%;
	}

	.topBarSearch image {
		width: 20px;
		height: 20px;
		opacity: 1;
		margin: 5px 13px;
	}




	/** 菜单切换 热门/学术**/
	.top-menu-view {
		display: flex;
		justify-content: space-around;
		background-color: #fafafa;;
		width: 100%;
		height: 46px;
		/* 在这里设置导航条高度 */
	}

	.menu-one-view {
		display: inline-block;
		white-space: nowrap;
		height: 100%;
		margin-left: 16px;
	}

	.top-menu-view .menu-one-view .menu-one {
		/* 在这里写 单个按钮样式 */
		margin-left: 8%;
		position: relative;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}

	.top-menu-view .menu-one-view .menu-one .menu-one-txt {
		height: 40upx;
		font-size: 18px;
		color: #888888;
		line-height: 40upx;
	}

	.top-menu-view .menu-one-view .menu-one .menu-one-bottom {
		position: absolute;
		bottom: 0;
		width: 100%;
	}

	.top-menu-view .menu-one-view .menu-one .menu-one-bottom .menu-one-bottom-color {
		width: 60%;
		height: 4upx;
	}

	.top-menu-view .menu-one-view .menu-one-act {
		/* 在这里写 单个按钮样式 */
		position: relative;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-txt {
		height: 40upx;
		font-size: 22px;
		font-weight: bold;
		color: #353535;
		line-height: 40upx;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom {
		/* 在这里设置底部横条宽度 */
		position: absolute;
		bottom: 5px;
		width: 5px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 2px;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color1 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
		border-radius: 2px;
	}

	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color2 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
		border-radius: 2px;
	}

	.swiper-box-list {
		flex: 1;
		width: 100%;
		background:#fafafa;
	}

	/* swiper */

	.swiper {
		height: 360upx;
	}

	.scroll-test {
		height: 100%;
	}
	
	.swiper-box {
		width: 100%;
		height: 100%;
	}
</style>
