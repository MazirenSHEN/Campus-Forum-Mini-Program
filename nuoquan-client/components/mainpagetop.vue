<template>
	<view class="mainPageTopAll" style="height:100%;">
		<!-- 盛放top功能区，height屏幕高度，宽度0 -->
		<!-- 待删除清理 <view class="bottomLayerOfLeft" v-if="showMainPageLeft" @click="controlShowLeft(0)" @touchmove="controlShowLeft(0)">
			<mainpageleft :userInfo="userInfo"></mainpageleft>
		</view> -->

		<searchpage v-if="showSearch" class="searchPage" @exitSearchSignal="controlShowSearch"></searchpage>

		<view class="mainPageTop" :style="{ height: roleup == false ? height + 'px' : height_roled + 'px' }">
			<!-- 搜索行 -->
			<!-- <view class="topBar" :style="{'margin-top': this.getnavbarHeight().top - 2 + 'px'}"> -->
			<view class="topBar" :style="{ height: this.getnavbarHeight().top + 40 + 'px'}">
				<view class="topBarSearch" :style="{'margin-top': this.getnavbarHeight().top + 2 + 'px'}" @click="controlShowSearch(1)">
					<image src="../static/icon/search_B79144.png" mode="aspectFit"></image>
				</view>
				<!-- <view class="topBarwaiting"></view> -->
				<!-- <creatarticle :topHeight='this.getnavbarHeight().top'></creatarticle> -->
			</view>
			<!-- 标题行 -->
			<view v-if="!roleup" class="titleLine">
				<view class="titleLine_left">
					<text>{{lang.hot}}</text>
				</view>
				<view @click="jumpTohot" class="titleLine_right">
					<text>{{lang.viewAll}} ></text>
				</view>
			</view>
			<!-- 热门卡片 -->
			<view class="hotestBox" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
				<view :class="[roleup == false ? 'hotestCard' : 'hotestCard_roled']">
					<!-- 左侧图标 -->
					<view class="iconBox" @click="jumpTohot" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
						<image v-if="roleup == false" src="../static/BG/hotest.png" mode="scaleToFill" class="fireIcon"></image>
						<image :class="[roleup == false ? 'hotText' : 'hotText_roled']" src="../static/icon/hotText.png" mode="aspectFit"></image>
					</view>

					<!-- 分割线 -->
					<view v-if="roleup == false" class="grayline"></view>

					<!-- 热门标题 -->
					<swiper :class="[roleup == false ? 'swiperCard' : 'swiperCard_roled']" :indicator-dots="false" :autoplay="true"
					 :interval="3000" :vertical="true" :duration="1000" :circular="true">
						<view>
							<swiper-item @click="goToDetail(item.id)" v-for="(item, index) in topArticles" :key="index">
								<view class="itemCard" :style="{'height':roleup == false ? '62px;' :'33px' ,}">
									<view v-if="!isNull(item.articleTitle)" class="hotTitle" :style="{'margin-top': roleup ? '8px': '12px'}">{{ item.articleTitle }}</view>
									<view v-else-if="isNull(item.articleTitle)" class="hotTitle" :style="{'margin-top': roleup ? '8px': '12px'}">{{item.articleContent}}</view>
									<view v-if="roleup == false" class="userInfo">
										<image :src="pathFilter(item.faceImg)" style="width: 17px;height: 17px;border-radius: 50%;"></image>
										<view class="userid_mainpagetop" style="display: flex;">
											<text>{{item.nickname}}</text>
											<image v-if="item.authType == 2" src="../static/icon/auth_red.png" style="width: 17px; height: 17px;z-index: 1000;margin-left: 3px;"></image>
											<image v-if="item.authType == 1" src="../static/icon/auth_yellow.png" style="width: 17px; height: 17px;z-index: 1000;margin-left: 3px;"></image>
										</view>
										</view>
								</view>
							</swiper-item>
						</view>
					</swiper>
				</view>
			</view>
			<!-- 标签和筛选行 -->
			<view v-if="roleup == false" class="tagsLine column_center">
				<!-- 左侧打开标签选择按钮 -->
				<view v-if="!selectedTag" @click="toggleShowTag" class="tagsButton">
					<view class="text">{{lang.tagSelected}}</view>
					<view class="icon super_center">
						<image v-if="!showTagBox" src="../static/icon/tag-white.png" mode="aspectFit"></image>
						<image v-else src="../static/icon/angle-up-white.png" mode="aspectFit"></image>
					</view>
				</view>
				<!-- 选择和替换为所选标签 -->
				<tagSelected v-if="selectedTag" style="margin-left: 4.5%;" :tag='selectedTag' @click="deleteTag"></tagSelected>
				<!-- 右侧排列筛选 -->
				<nqSwitch v-if="lang.langType == 'zh-CN'" :bgSwitchLeft="'-13px'" :bgSwitchRight="'41px'" :options='[lang.all, lang.follow]'
				 :initStatus='iniStatus1' @onChange="change_article_order1" style="position:absolute;right: 6.5%;">
				</nqSwitch>
				<nqSwitch v-else :bgSwitchLeft="'-13px'" :bgSwitchRight="'47px'" :options='[lang.all, lang.follow]' :initStatus='iniStatus1'
				 @onChange="change_article_order1" style="position:absolute;right: 4.5%;">
				</nqSwitch>
			</view>
		</view>
		<tagSelectBox :style="{position: 'fixed', 'z-index': '40' ,  width: 100 + '%' , top: height - 11 + 'px' }" :lang="lang"
		 :tagList="tagList" @selected="getSelectedTag" v-if="showTagBox">
		</tagSelectBox>
		<!-- 点击蒙版，收起标签选择框 -->
		<view :style="{position: 'fixed',
			'z-index': 30 ,
			width : windowWidth + 'px', 
			height: windowHeight + 'px'}" 
			@click="toggleShowTag"	
			v-if="showTagBox">
		</view>
	</view>
</template>

<script>
	import mainpageleft from '@/components/mainpageleft.vue';
	import searchpage from '../pages/search/search';
	import nqSwitch from '@/components/nq-switch.vue'
	import tagSelectBox from '@/components/nq-tag/tagSelectBox.vue'
	import tagSelected from '@/components/nq-tag/tagSelected.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import creatarticle from './newArticle+.vue'

	export default {
		components: {
			mainpageleft,
			searchpage,
			nqSwitch,
			tagSelectBox,
			tagSelected,
			creatarticle,
		},
		props: {
			// 渲染时候替换默认值会被替换
			userInfo: {
				faceImg: '../static/touxiang.jpg'
			},
			topArticles: '',
			tagList: '', //标签列表
			roleup: false,
			height: 0,
			height_roled: 0,
		},
		data() {
			return {
				serverUrl: this.$serverUrl,
				showMainPageLeft: 0,
				showSearch: 0,
				showTagBox: false,
				iniStatus1: 0, //为switch组件设置初始值
				iniStatus2: 0, //为switch组件设置初始值
				selectedTag: '', //已选择的标签
				windowWidth: 0, //屏幕宽度，用于蒙版
			windowHeight: 0, //屏幕高度，用于蒙版
			};
		},
		computed: {
			...mapState(['lang'])
		},
		watch: {
			roleup(newValue, oldValue) {
				this.showTagBox = false;
			}
		},
		created(){
		var that = this;
		uni.getSystemInfo({
			success: function (res) {
			        that.windowWidth = res.screenWidth;
					console.log('width' + that.windowWidth);
					that.windowHeight = res.screenHeight;
					console.log('height' + that.windowHeight);
			    }
			});
		},

		methods: {
			toggleShowTag() {
				this.showTagBox = !this.showTagBox
			},
			controlShowLeft(a) {
				this.showMainPageLeft = a;
				// console.log(this.showMainPageLeft);
			},
			controlShowSearch(a) {
				console.log('this is controlShowSearch, receive data will be appied');
				console.log(a);
				this.showSearch = a;
			},
			jumpToSubmit() {
				uni.navigateTo({
					url: '/pages/submit/submit'
				});
			},
			goToDetail(articleIndex) {
				var id = articleIndex;
				console.log(id);
				uni.navigateTo({
					url: '/pagesSubA/detail/detail?data=' + id
				});
			},
			jumpTohot() {
				uni.navigateTo({
					url: '/pagesSubA/hot/hot'
				})
			},

			change_article_order1(e) {
				// console.log(e.status);
				this.iniStatus1 = e.status;
				this.$emit('transQueryType', e.status);
			},

			getSelectedTag(tag) {
				this.selectedTag = tag; //传值给渲染层
				this.showTagBox = false;
				this.$emit('selectedTag', tag);
			},

			deleteTag() {
				this.selectedTag = '';
				this.$emit('deleteTag');
			}
		}
	};
</script>
<style>
	page {
		width: 100%;
	}
</style>
<style scoped>
	.searchPage {
		height: 100%;
		width: 100%;
		position: fixed;
		top: 0;
		left: 0;
		background: #ffffff;
		z-index: 30;
	}

	.bottomLayerOfLeft {
		position: fixed;
		width: 750upx;
		height: 1000px;
		z-index: 3;
	}

	.mainPageTop {
		/* height: 139px; */
		position: fixed;
		left: 0;
		top: 0;
		overflow: hidden;
		width: 100%;
		background-color: white;
		border-bottom: 4px solid rgba(236, 236, 236, 1);
	}

	/* .mainPageTop_roled{
	padding-top: 4px;
	height: 110px;
	position: fixed;
	left: 0;
	top: 0;
	overflow: hidden;
	width: 100%;
	background-size: 100% 100%;
	box-shadow: 0px 0px 10px 1px #c0c0c0;
	background-image: linear-gradient(#ffc95a, #f89d4d);
}
 */
	.topBar {
		width: 100%;
		background-color: rgb(255,255,255,1);
		/* height: 30px; */
		/* margin-top: 23px; */
		/* 此处需要兼容性处理 47px */
	}

	/* 头像,备用
	.topBarTouxiang {
		width: 30px;
		height: 30px;
		border-radius: 30px;
		display: inline-block;
		vertical-align: middle;
		margin-left: 12px;
	} */

	.topBarSearch {
		height: 30px;
		background: rgba(230, 230, 230, 1);
		opacity: 1;
		border-radius: 10px;
		/* width: calc(100% - 3.47% - 101px - 32px - 7px); */
		width: calc(100% - 3.47% - 101px - 7px);
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

	/* 热门标题 */
	.titleLine {
		height: 44px;
		width: 100%;
		position: relative;
	}

	.titleLine_left {
		position: absolute;
		left: 25px;
		top: 12px;
		height: 17px;
	}

	.titleLine_left text {
		height: 17px;
		font-size: 17px;
		font-weight: 550;
		line-height: 21px;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
	}

	.titleLine_right {
		position: absolute;
		right: 22px;
		top: 13.5px;
		height: 14px;
	}

	.titleLine_right text {
		height: 14px;
		font-size: 14px;
		font-weight: 400;
		line-height: 21px;
		color: rgba(177, 177, 177, 1);
		opacity: 1;
	}

	.topBarPlus {
		font-size: 25px;
		background: #ffffff;
		display: inline-block;
		height: 25px;
		width: 25px;
		vertical-align: middle;
		text-align: center;
		margin-left: 10px;
		border-radius: 3px;
		line-height: 23px;
	}

	/* 热门话题卡片 */
	.hotestBox {
		width: 100%;
	}

	.hotestCard {
		display: flex;
		width: 91%;
		height: 63px;
		border-radius: 8px;
		margin-left: 4.5%;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		opacity: 1;
	}

	.hotestCard_roled {
		display: flex;
		width: 93.07%;
		background: rgba(252, 252, 252, 1);
		box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
		opacity: 1;
		margin-top: 12px;
		margin-left: 3.47%;
		height: 33px;
		border-radius: 8px;
	}

	.iconBox {
		position: relative;
		width: 62px;
	}

	.fireIcon {
		position: absolute;
		z-index: 10;
		height: 50px;
		width: 55px;
		bottom: -1px;
	}

	.hotText {
		position: absolute;
		top: 8px;
		left: 29px;
		height: 14px;
		width: 26px;
		z-index: 20;
	}

	.hotText_roled {
		position: absolute;
		top: 11px;
		left: 12px;
		height: 14px;
		width: 26px;
		z-index: 20;
	}

	.tagsLine {
		height: 44px;
		width: 100%;
		position: relative;
		margin-top: 12px;
	}

	.tagsButton {
		position: absolute;
		display: flex;
		left: 4.5%;
		width: 140px;
		height: 30px;
		background: rgba(255, 238, 201, 1);
		opacity: 1;
		border-radius: 54px;
	}

	.tagsButton .text {
		width: 110px;
		height: 30px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 30px;
		text-align: center;
		color: rgba(252, 192, 65, 1);
		opacity: 1;
	}

	.tagsButton .icon {
		width: 30px;
		height: 30px;
		background: rgba(252, 192, 65, 1);
		border-radius: 50%;
		opacity: 1;
	}

	.tagsButton .icon image {
		width: 13px;
		height: 14px;
		opacity: 1;
	}

	.grayline {
		height: 41px;
		width: 2px;
		background-color: #ececec;
		margin-top: 14px;
	}

	.swiperCard {
		width: 74.21%;
		height: 62px;
		margin-left: 11px;
	}

	.swiperCard_roled {
		width: 74.21%;
		height: 33px;
		margin-left: 11px;
	}

	.itemCard {
		width: 100%;
	}

	.userInfo {
		display: flex;
		position: absolute;
		width: 100%;
		height: 17px;
		margin-top: 8px;
	}

	/* .userInfo image {
		position: absolute;
		width: 17px;
		height: 17px;
		border-radius: 50%;
	} */

	.userid_mainpagetop {
		position: absolute;
		left: 25px;
		height: 14px;
		width: 100%;
		line-height: 14px;
		font-size: 12px;
		font-weight: 400;
		color: rgba(155, 155, 155, 1);
		opacity: 1;
	}

	.hotTitle {
		width: 100%;
		height: 21px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 21px;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}

	.optionLine_mpt {
		display: flex;
		justify-content: space-between;
		width: 93.07%;
		left: 3.47%;
		height: 30px;
		position: fixed;
		z-index: 20;
	}

	.optionLinebg_mpt {
		position: fixed;
		z-index: 10;
		width: 100%;
		height: 41px;
		background-color: #fdfdfd;
	}

	.tagchoose {
		width: 74px;
		height: 26px;
		background: rgba(236, 236, 236, 1);
		opacity: 1;
		/* margin-left: 3.47%; */
		border-radius: 20px;
	}

	.tagchoose text {
		margin-left: 12px;
		height: 26px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 28px;
		color: rgba(53, 53, 53, 1);
		opacity: 1;
	}

	.tagicon {
		margin-left: 3px;
		width: 24px;
		height: 16px;
		opacity: 1;
	}

	.comments-order {
		/* margin-left: 14.8%; */
		height: 22px;
		background: #ECECEC;
		border-radius: 75px;
		width: 82px;
		line-height: 28px;
		display: flex;
		vertical-align: middle;
		position: relative;
		justify-content: space-between;

	}

	.order-in-hot,
	.order-in-time {
		color: #9B9B9B;
		font-size: 10px;
		line-height: 22px;
		display: inline-block;
		align-items: center;
		padding-right: 10.5px;
		padding-left: 10.5px;
		z-index: 30;
		width: 28px;
		text-align: center;
		transition: padding-left 200ms,
			padding-right 200ms,
			font-size 200ms,
			color 200ms;
		transition-delay: 0ms;
	}

	.bg-of-order {
		height: 28px;
		width: 54px;
		background: #FFFFFF;
		border-radius: 75px;
		box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.16);
		z-index: 20;
		position: absolute;
		left: -13px;
		top: -3px;
		transition: left 500ms ease;
	}

	.chosen {
		font-size: 14px;
		color: #353535;
		width: 28px;
		padding-left: 0;
		padding-right: 0;
		transition: padding-left 300ms,
			padding-right 300ms,
			font-size 300ms,
			color 300ms;
		transition-delay: 100ms;
	}
</style>
