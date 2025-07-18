<template>
	<!-- 	<view class="bottomLayerCoverScreen" >
 -->
	<view class="mainPageLeft" @click.stop="">
		<view class="topInfoArea">
			<view class="topLeft">
				<image class="touxiang" :src='pathFilter(userInfo.faceImg)' @tap="goToProfile"></image>
			</view>
			<view class="topRight">
				<view style="font-size: 13px;margin-bottom:9px;width:100%;vertical-align: bottom;overflow:hidden; text-overflow: ellipsis;max-height: 20px;white-space: nowrap;">{{userInfo.nickname}}</view>
				<view style="font-size:8px ;font-weight: bold;margin-bottom:3px;width:100%;vertical-align: bottom;">{{userInfo.emailPrefix}}</view>
				<view style="color:#B2B2B2;font-size: 6px;width:80px;position: relative;left: 10px;width:100%;vertical-align: bottom;">{{userInfo.emailSuffix}}</view>
			</view>
		</view>
		<view class="personData">
			<!-- <view class="hotNum">
				-
			</view> -->
			<button class="yourFocus" @tap="goToFansFollow(0)" hover-class="customHover">
				{{userInfo.followNum}}
			</button>
			<button class="focusYou" @tap="goToFansFollow(1)" hover-class="customHover">
				{{userInfo.fansNum}}
			</button>
		</view>
		<view class="personPageList">
			<!-- TODO: 最好把 view 都改成 button 并加上样式 
											  by Jerrio -->
			<!-- <button class="pageLine" @click="goToMyPublish"> -->
			<button class="pageLine" @click=" goToMessageMyPoblish">
				<image src="../static/icon/write.png"></image>
				<view>我的发布</view>
				<!-- <view class="noticeNum">3</view> -->
			</button>
			<button class="pageLine" @click="goToMessageListPage">
				<image src="../static/icon/message.png"></image>
				<view>我的消息</view>
				<view class="noticeNum" v-if="unreadMsgCount>0 && unreadMsgCount<=99"> {{unreadMsgCount}} </view>
				<view class="noticeNum" v-else-if="unreadMsgCount>99"> 99+ </view>
			</button>
			<button class="pageLine" @click="showToast()">
				<image src="../static/icon/star.png"></image>
				<view>我的收藏</view>
				<!-- <view class="noticeNum">23</view> -->

			</button>
			<button class="pageLine" @click="showToast()">
				<image src="../static/icon/report.png"></image>
				<view>举报投诉</view>
				<!-- <view class="noticeNum">3</view> -->

			</button>
			<button class="pageLine" @click="goToAboutPage">
				<!-- <image src="../static/icon/about.png"></image> -->
				<view>关于</view>

			</button>
		</view>
	</view>
</template>

<script>
	import {mapState} from 'vuex';
	
	name: 'mainpageleft'
	export default {
		props: {
			// 渲染时候替换默认值会被替换
			userInfo: {
				faceImg: '../static/touxiang.jpg',
				emailPrefix: 'test123',
				emailSuffix: '@nottingham.edu.cn'
			},
		},
		data() {
			return {
				unreadMsgCount: uni.getStorageSync('myMsgCount'), // 从缓存中获取初始值
			};
		},
		
		computed: {
			...mapState([
				'myMsgCount',
			])
		},
		
		watch: {
			myMsgCount(newVal, oldVal) { // 左侧栏”我的消息“未读通知
				this.unreadMsgCount = newVal;
			}
		},
		
		methods: {
			showToast() {
				uni.showToast({
					// title: '⠀⠀⠀⠀⠀under⠀⠀⠀⠀⠀development',//不是空格，是特殊符号，莫删
					title: this.lang.developing,
					duration: 2000,
					icon: 'none',
				})
			},
			goToMessageMyPoblish() {
				uni.navigateTo({
					url: '../myPublish/myPublish',
				});
			},
			
			goToMessageListPage() {
				console.log("going to messageList");
				
				// this.$store.commit('setMyMsgCount', 0);
				uni.switchTab({
				      url:'./messagelist',
				})

			},
			
			goToAboutPage() {
				uni.navigateTo({
					url: '../about/about',
				});
			},
			
			goToProfile() {
				uni.navigateTo({
					url: '../profile/profile',
				});
			},
			
			/**
			 * @param {Object} currentTab 0: 关注 1: 粉丝
			 */
			goToFansFollow(currentTab) {
				var data = {
					currentTab: currentTab,
					thisUserInfo: this.getGlobalUserInfo()
				}
				uni.navigateTo({
					url: '../followlist/followlist?data=' + JSON.stringify(data),
				});
			},
			
			goToMyPublish() {
				uni.navigateTo({
					url: '../myPublish/myPublish',
				});
			},
		},

	};
</script>

<style scoped>
	@keyframes slidefromleft {
		from {
			display: none;
		}

		to {
			display: block;
		}
	}

	.bottomLayerCoverScreen {
		position: fixed;
		width: 750upx;
		height: 100%;
		z-index: 5;
	}

	.mainPageLeft {
		background: #fcfcfc;
		animation: slidefromleft 3s;
		width: 174px;
		height: 100%;
		position: fixed;
		left: 0;
		top: 0;
		box-shadow: 1px 0px 5px #a6a6a6;
		border-bottom-right-radius: 25px;
	}

	.topInfoArea {
		height: 60px;
		margin-top: 29px;
	}

	.topLeft {
		margin-left: 20px;
		display: inline-block;
		width: 60px;
		vertical-align: top;
	}

	.touxiang {
		height: 60px;
		width: 60px;
		border-radius: 60px;
		display: block;
	}

	.topRight {
		display: inline-block;
		margin-left: 8px;
		height: 60px;
		width: 80px;
		vertical-align: bottom;
	}

	.personData {
		padding-top: 17px;
	}

	.hotNum {
		position: relative;
		padding-left: 33px;
		font-size: 15px;
		color: #E15249;
		font-weight: bold;
		font-style: italic;

	}

	.hotNum::after {
		content: "影响力";
		font-weight: normal;
		font-style: normal;
		font-size: 8px;
		color: #000000;
		padding-left: 9px;

	}

	.yourFocus {
		position: relative;
		width:71px;
		height:43;
		line-height: 10px;
		padding: 19px 28px 14px;
		display: inline-block;
		font-size: 13px;
		font-weight: bold;
	}

	.yourFocus::before {
		content: "你关注的";
		position: absolute;
		top:unset;
		bottom: 0px;
		left: 28px;
		font-size: 7px;
		color: #B2B2B2;
		font-weight: 300;
		width: 32px;
		height:8px;
		line-height: 8px;
		vertical-align: bottom;
		

	}

	.focusYou {
	position: relative;
	width:71px;
	height:43;
	line-height: 10px;
	padding: 19px 28px 14px;
	display: inline-block;
	font-size: 13px;
	font-weight: bold;
	}

	.focusYou::before {
		content: "关注你的";
		position: absolute;
		top:unset;
		bottom: 0px;
		left: 28px;
		font-size: 7px;
		color: #B2B2B2;
		font-weight: 300;
		width: 32px;
		height:8px;
		line-height: 8px;
		vertical-align: bottom;
	}
.customHover{
	background: #F4F5F6;
}
	.personPageList {
		margin-top: 56px;
	}

	.pageLine {
		position: relative;
	}

	.noticeNum {
		position: absolute;
		right: 11px;
		top: 16px;
		background: #FDD041;
		color: #FFFFFF;
		font-size: 9px;
		line-height: 18px;
		border-radius: 3px;
		padding: 0 2px;

	}

	button {
		background: #fcfcfc;
		border-radius: 0;
		text-align: left;
		margin: 0;
		padding: 0 33px;
	}

	button::after {
		border: none;
	}

	button-hover {
		background: #000000;
	}

	.pageLine image {
		width: 20px;
		height: 20px;
		display: inline-block;
		vertical-align: middle;
	}

	.pageLine view {
		display: inline-block;
		font-size: 13px;
		font-weight: bold;
		margin-left: 17px;
	}
</style>
