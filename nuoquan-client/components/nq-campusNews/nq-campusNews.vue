<!-- 
	Author: Guetta 2020年8月18日14点49分
	Version: v2.0.0
	状态: 切图完成
	TODO: 待接口开发完毕，填入功能和数据
	备注: 探索-热门 头部头条消息带评论组件
	*为与探索页内所用相同滑动框架做区分，搜索 CampusNews 可找到全部单独更改了变量名的变量
 -->
<template>
	<view id="campusNews-Box">
		<scroll-view class="campusNews-top-menu-view" :style="{width: this.screenWidth + 'px'}" show-scrollbar="false"
		 scroll-x="true" scroll-left="scrollLeftofCampusNews" scroll-with-animation="true">
			<block v-for="(menuTab, index) in articleList" :key="index">
				<view class="menu-one-view" v-bind:id="'tabNumofCampusNews' + index" @click="swichMenuofCampusNews(index)">
					<view :class="[currentTab == index ? 'menu-one-act' : 'menu-one']">
						<view class="menu-one-imgBox">
							<image class="menu-one-img" :src="pathFilter(menuTab.faceImg)" mode="aspectFit"></image>
						</view>
						<view class="menu-one-bottom">
							<view class="menu-one-bottom-color1" v-if="index == 0"></view>
							<view class="menu-one-bottom-color2" v-else></view>
						</view>
					</view>
				</view>
			</block>
		</scroll-view>
		<!-- <view style="height: 13px;width:100%;background: #FAFAFA;"></view> -->
		<!-- 占位留白 -->
		<swiper :current="currentTab" :vertical="false" class="swiper-box-list" duration="300" next-margin="60" @change="swiperChangeofCampusNews">
			<swiper-item class="swiper-box column_center" v-for="(articleCard, index) in articleList" :key="index">
				<view class="swiperArticleCard" @tap="goToWebPage(articleCard.isJump,articleCard.id,articleCard.link)">
					<image class="big_coverImage" v-if="articleCard.coverImage !== null" :src="pathFilter(articleCard.coverImage)"
					 mode="aspectFill"></image>
					<text class="swiperArticleCard-title">
						{{articleCard.title}}
					</text>
					<view class="subTitle" v-if="articleCard.coverImage == null">
						<text style="min-width: calc(100% - 84px);" class="text_ellipsis">{{localParse(articleCard.content)}}</text>
					</view>
					<view class="campusNews-bottomLine" v-if="articleCard.coverImage == null">
						<text class="campusNews-time">{{timeDeal(articleCard.createDate)}}</text>
						<view class="likeAndCommentNumb">
							<image class="campusNews-icon" src="../../static/icon/heart_353535.png" mode="aspectFit"></image>
							<text class="campusNews-number">{{articleCard.likeNum}}</text>
							<image class="campusNews-icon" src="../../static/icon/comment-alt-353535.png" mode="aspectFit"></image>
							<text class="campusNews-number">{{articleCard.commentNum}}</text>
						</view>
					</view>
				</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	// import nq_parse from '../../pagesSubA/longArticleDetail/parse.js'

	export default {
		components: {

		},
		computed: {
			...mapState(['lang']),

		},
		computed: {

		},
		props: {
			articleList: {},
			// {
			// 	profilePic: "../../static/AD/20200524AD1.jpg",
			// 	title: '1示例文本共二十个文字示例文本共二十个文字',
			// 	sub_title: '1示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	content: '1示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	like_num: '99',
			// 	comment_num: '99',
			// 	postTime: '2020.12.31 23:59'
			// },{
			// 	profilePic: "../../static/AD/20200524AD1.jpg",
			// 	title: '2示例文本共二十个文字示例文本共二十个文字',
			// 	sub_title: '2示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	content: '2示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	like_num: '99',
			// 	comment_num: '99',
			// 	postTime: '2020.12.31 23:59'
			// },{
			// 	profilePic: "../../static/AD/20200524AD1.jpg",
			// 	title: '2示例文本共二十个文字示例文本共二十个文字',
			// 	sub_title: '2示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	content: '2示例文本共三十个文字示例文本共三十个文字示例文本共三十个文字',
			// 	like_num: '99',
			// 	comment_num: '99',
			// 	postTime: '2020.12.31 23:59'
			// }
		},
		data() {
			return {
				showSearch: 0,
				currentTab: 0,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				scrollLeftofCampusNews: 0,
				screenWidth: '',
				parsedArticleContent: '', //解析过后的文章内容，本地存储
			};
		},
		create() {
			uni.getSystemInfo({
				success(res) {
					this.screenWidth = res.screenWidth;
				}
			})
		},
		methods: {
			localParse(unParsed) { //本地解析，引入的js在HTML似乎没法直接用,没有时间琢磨的 --yaoyao
				// return nq_parse.parsePureText(unParsed).withoutMedia;
			},
			controlShowSearch(a) {
				this.showSearch = a;
			},
			goTop: function(e) {
				this.scrollTop = this.old.scrollTop;
				this.$nextTick(function() {
					this.scrollTop = 0;
				});
			},
			swichMenuofCampusNews: async function(current) {
				//点击其中一个 menu
				if (this.currentTab == current) {
					return false;
				} else {
					if (current == 0) {
						// console.log("点了点赞"); 刷新 list 并设置计数值
						this.likePage = 1;
						this.likeList = this.notification.getLikeMsg(this.likePage);
						this.$store.commit('setLikeMsgCount', 0);
					} else {
						// console.log("点了评论");
						this.commentPage = 1;
						this.commentList = this.notification.getCommentMsg(this.commentPage);
						this.$store.commit('setCommentMsgCount', 0);
					}

					this.currentTab = current;
					this.setScrollLeftofCampusNews(current);
				}
			},
			swiperChangeofCampusNews: async function(e) {
				let index = e.target.current;
				this.setScrollLeftofCampusNews(index);
				this.currentTab = index;
			},
			setScrollLeftofCampusNews: async function(tabIndex) {
				console.log(tabIndex);
				let leftWidthSum = 0;
				for (var i = 0; i <= tabIndex; i++) {
					console.log("i= " + i);
					let nowElement = await this.getWidthofCampusNews('tabNumofCampusNews' + i);
					console.log("nowElement= " + nowElement.width);
					leftWidthSum = leftWidthSum + nowElement.width;
					console.log("leftWidthSum= " + leftWidthSum);
				}
				let winWidth = uni.getSystemInfoSync().windowWidth;
				this.scrollLeftofCampusNews = leftWidthSum > winWidth ? leftWidthSum - winWidth : 0;
			},
			getWidthofCampusNews: function(id) {
				//得到元素的宽高
				console.log(id);
				return new Promise((res, rej) => {
					uni.createSelectorQuery()
						.select('#' + id)
						.fields({
								size: true,
								scrollOffset: true
							},
							data => {
								res(data);
							})
						.campusNews - top - menu - viewxec();
					console.log(res.width);
				});
			},
			goToWebPage(isJump,id,link) {
				if (isJump == 0) { //0，跳转详情
					uni.navigateTo({
						url: '../../pagesSubA/longArticleDetail/longArticleDetail?data=' + id,
					});
				} else if(isJump == 1){
					var encodeData = encodeURIComponent(link);
					uni.navigateTo({
						url: '/pages/adWebPage/adWebPage?url=' + encodeData,
					})
				}
			}
		},


	}
</script>

<style>
	#public-container {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: #FFFFFF;
	}

	#campusNews-Box

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
		background-color: rgb(252, 192, 65);
		position: relative;
		/* height: 30px; */
		/* margin-top: 23px; */
		/* 此处需要兼容性处理 47px */
	}

	.topBarSearch {
		height: 30px;
		background: rgba(255, 255, 255, 1);
		opacity: 1;
		border-radius: 75px;
		width: calc(100% - 3.47% - 101px - 32px - 7px);
		border-radius: 75px;
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




	/** 公众号切换**/
	.campusNews-top-menu-view {
		white-space: nowrap;
		justify-content: space-around;
		height: 76px;
		padding: 0 18px 0 0;
		/* 在这里设置导航条高度 */
	}

	.menu-one-view {
		display: inline-block;
		white-space: nowrap;
		height: 100%;
		margin-left: 18px;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one {
		/* 在这里写 非选中状态 单个按钮样式 */
		/* position: relative; */
		height: calc(100% - 6px);
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		overflow: visible;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one .menu-one-imgBox {
		width: 52px;
		height: 56px;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one .menu-one-imgBox .menu-one-img {
		height: 52px;
		width: 52px;
		border-radius: 50%;
		border: 1px solid #3D3D3D;
		opacity: 0.5;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act {
		/* 在这里写 选中状态 单个按钮样式 */
		/* position: relative; */
		height: calc(100% - 6px);
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		overflow: visible;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act .menu-one-imgBox {
		width: 60px;
		height: 64px;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act .menu-one-imgBox .menu-one-img {
		height: 60px;
		width: 60px;
		border-radius: 50%;
		border: 1px solid #3D3D3D;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act .menu-one-bottom {
		/* 在这里设置底部横条宽度 */
		position: absolute;
		/* bottom 为 0 在模拟器上会有错位，但在手机上显示正常 */
		bottom: 0;
		margin-top: 4px;
		width: 5px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 2px;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color1 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
		border-radius: 2px;
	}

	.campusNews-top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color2 {
		/* 在这里设置底部横条高度和颜色 */
		width: 5px;
		height: 5px;
		background: #FCC041;
		border-radius: 5px;
	}

	.swiper-box-list {
		flex: 1;
		height: calc(100% - 46px);
		width: 100%;
		background: #fafafa;
		height: 188px;
	}

	/* swiper */

	.swiper {
		height: 158px;
	}

	.scroll-test {
		height: 100%;
	}

	.swiper-box {
		width: 542upx;
		height: 188px;
		margin-left: 12px;
		padding: 5px 0 5px 0;
	}

	/* 高度用 px，不随屏幕改变 */
	.swiperArticleCard {
		height: 158px;
		width: 542upx;
		padding: 10px 15px 0 15px;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.1);
		border-radius: 5px;
	}

	.big_coverImage {
		height: calc(100% - 48px);
		width: calc(100% + 30px);
		margin-left: -15px;
		margin-top: -10px;
		padding-bottom: 10px;
	}

	.swiperArticleCard .swiperArticleCard-title {
		width: 100%;
		height: 48px;
		font-size: 16px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 24px;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
	}

	.swiperArticleCard .subTitle {
		display: flex;
		margin-top: 6px;
		height: 50px;
	}

	.swiperArticleCard .subTitle text {
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
	}

	.swiperArticleCard .subTitle image {
		max-width: 70px;
		height: 50px;
		border-radius: 5px;
		margin-left: 14px;
	}

	.swiperArticleCard .campusNews-bottomLine {
		width: 100%;
		height: 16px;
		margin-top: 15px;
		position: relative;
	}

	.swiperArticleCard .campusNews-time {
		position: absolute;
		left: 0;
		height: 15px;
		font-size: 10px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(154, 154, 154, 1);
		opacity: 1;
	}

	.swiperArticleCard .campusNews-bottomLine .likeAndCommentNumb {
		position: absolute;
		display: flex;
		right: 0;
	}

	.campusNews-number {
		height: 15px;
		margin-left: 4px;
		font-size: 10px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(154, 154, 154, 1);
		opacity: 1;
	}

	.campusNews-icon {
		margin-left: 4px;
		width: 16px;
		height: 16px;
	}

	.text_ellipsis {
		/* 之后这个可以做成全局样式, 文字溢出省略号显示 yaoyao  9.19*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		overflow: hidden;
		text-overflow: ellipsis;
		word-wrap: break-word;
	}
</style>
