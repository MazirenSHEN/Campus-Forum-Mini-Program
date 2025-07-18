<template>
	<view id="public-container">
		<!-- 占位块 -->
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }"></view>
		<view class="topbox">
			<view class="header">
				<!-- 头像 -->
				<view class="touxiang">
					<avatar
						class="image"
						:avatarSrc="pathFilter(thisUserInfo.faceImg)"
						selWidth="200px"
						selHeight="200px"
						@upload="uploadFace"
						avatarStyle="width: 80px; height: 80px; border-radius: 100%;"
						:pageId="thisUserInfo.id"
					></avatar>
				</view>
				<!-- 右侧信息快 -->
				<view class="person_info">
					<!-- 名字 -->
					<view class="nameBox">
						<text class="name-text">{{ thisUserInfo.nickname }}</text>
						<image v-if="thisUserInfo.authType == 2" style="width: 17px;height: 17px;margin-left: 5px;" src="../../static/icon/auth_red.png"></image>
						<image v-if="thisUserInfo.authType == 1" style="width: 17px;height: 17px;margin-left: 5px;" src="../../static/icon/auth_yellow.png"></image>
					</view>
					<!-- 粉丝 关注 影响力 -->
					<view class="operationTitle">
						<view class="operationTitle-text">{{ lang.fans }}</view>
						<view class="operationTitle-text">{{ lang.follow }}</view>
						<view class="operationTitle-text">{{ lang.reputation }}</view>
					</view>
					<!-- 数量 -->
					<view class="operationNum">
						<view class="operationNum-text" style="color:rgba(53,53,53,1);" @tap="goToFansFollow(1)">{{ thisUserInfo.fansNum }}</view>
						<view class="operationNum-text" style="color:rgba(53,53,53,1);" @tap="goToFansFollow(0)">{{ thisUserInfo.followNum }}</view>
						<view class="operationNum-text" style="color:rgba(254,95,85,1);">{{ thisUserInfo.reputation }}</view>
					</view>
				</view>
			</view>
			<!-- 文字介绍 -->
			<view class="introBox">
				<text v-if="thisUserInfo.signature == null" class="introBox-text">{{ lang.editSignature }}</text>
				<text v-else class="introBox-text">{{ thisUserInfo.signature }}</text>
			</view>
		</view>
		
		<view class="shadow1"></view>
		<view class="shadow2"></view>

		<!-- 02 号内嵌广告位 -->
		<!-- <swiper class="top-swiper" :indicator-dots="false" :autoplay="true" :interval="4000" :duration="1000" circular="true">
			<swiper-item v-for="(item,index) in adverts" :key="index">
				<image class="guanggao" :src="item.url" mode="aspectFit" @click="jumpToWeb(item.jumpUrl)"></image>
			</swiper-item>
		</swiper> -->
		<!-- <image class="guanggao" :src="adverts[0].url" mode="aspectFill"></image> -->
		<!-- <swiper class="guanggao" v-if="!isNull(adverts)" circular="true" :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval" :duration="duration"> -->
			<!-- 图片比例1:3 -->
			<!-- <swiper-item v-for="(item, index) in adverts" :key="index"> -->
				<!-- 广告位背景，之后的广告图需与此保持同样尺寸 -->
				<!-- <view class="swiper-item swiperBg"></view> -->
				<!-- <image class="swiperBg" :src="item.url" mode="aspectFill" style="z-index: 1000;margin-left: 5%;margin-right: 5%;"></image> -->
			<!-- </swiper-item> -->
		<!-- </swiper> -->

		<view class="navigator_box">
			<mynavigator :objList="dataList" @trigger="navigatorEvent()"></mynavigator>
		</view>

		<tab-bar :current="4"></tab-bar>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
import mynavigator from '@/components/mynavigator.vue';
import avatar from '@/components/yq-avatar/yq-avatar.vue';

export default {
	data() {
		return {
			background: ['color1', 'color2', 'color3'],
			indicatorDots: false,
			autoplay: true,
			interval: 4000,
			duration: 500,
			screenWidth: 350,
			thisUserInfo: '',
			windowHeight: 0,
			windowWidth: 0,
			cardWidth: '',
			dataList: '',
			adverts:[],
			userInfo:{},
		};
	},
	components: {
		tabBar,
		mynavigator,
		avatar
	},
	computed: {
		...mapState(['lang'])
	},
	watch: {
		lang(newVal, oldVal){
			this.updateDataList();
		}
	},
	onLoad() {
		this.adverts = [
			{
				id: 1,
				url:'https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/ad_wenmo.png',
				jumpUrl: 'https://mp.weixin.qq.com/s/lFf-jjg04ajYyoV3Ea5Ipg',
			},
			{
				id:2,
				url:'https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/ad_haochi.png',
				jumpUrl: 'https://mp.weixin.qq.com/s/6xnMR_qFJyeEtrVSMnTccg',
			}
		]
		
		this.thisUserInfo = this.getGlobalUserInfo();
		var screenWidth = uni.getSystemInfoSync().screenWidth;
		this.screenWidth = screenWidth;
		// 获取全局用户信息
		var userInfo = this.getGlobalUserInfo();
		
		if (!this.isNull(userInfo)) {
			this.userInfo = this.getGlobalUserInfo();
		} else {
			uni.redirectTo({
				url: '../signin/signin'
			});
			return;
		}
		
		// 获取当前分页
		var page = this.page;

		//获取屏幕宽高
		var that = this;
		uni.getSystemInfo({
			success: function(res) {
				that.windowHeight = res.windowHeight;
				that.windowWidth = res.windowWidth;
			}
		});

		// 获取卡片宽度
		this.cardWidth = this.windowWidth - 26 + 'px';
		
		this.updateDataList();
		
		// 加载广告
		// Editor: Yifei
		// Date: Sept 9, 2022
		// TODO: 原本的广告接口可以研究一下打开，现在写成静态的了
		// this.getAdByPosition("MINE",5,this.thisUserInfo.id);
	},

	onShareAppMessage(res){
		if (res.from === 'menu'){
			return {
				title: '速来围观' + this.userInfo.nickname + '的分享',
				path: '/pagesSubA/personpublic/personpublic?data=' + this.userInfo.id,
			};
		}
	},
	
	onShareTimeline(res){
		if (res.from === 'menu'){
			return {
			title: '速来围观' + this.userInfo.nickname + '的分享',
			path: '/pagesSubA/personpublic/personpublic?data=' + this.userInfo.id,
			};
		}
	},

	onShow() {
		//更新用户数据
		console.log('更新用户数据');
		this.queryUserInfo(this.thisUserInfo.id);
	},

	onPullDownRefresh() {
		console.log('refresh');
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 1000);
	},

	methods: {
		...mapMutations(['changeLang']),
		
		updateDataList(){
			this.dataList = [
				{
					type: 0,
					lefticon_src: 'file-alt-5E49E9',
					righticon_src: 'angle-right-888888',
					style: 'rgba(227,223,248,1)',
					name: this.lang.profile,
					url: '../profile/profile',
				},
				{
					type: 0,
					lefticon_src: 'notes-25d223',
					righticon_src: 'angle-right-888888',
					style: 'rgba(216,248,215,1)',
					name: this.lang.myPublish,
					url: '../myPublish/myPublish',
				},
				{
					type: 0,
					lefticon_src: 'star-full-fcc041',
					righticon_src: 'angle-right-888888',
					style: 'rgba(255,245,219,1)',
					name: this.lang.myCollection,
					url: '../myCollection/myCollection',
				},
				{
					type: 1,
					lefticon_src: 'language-5d88EB',
					righticon_src: 'angle-right-888888',
					style: 'rgba(226,235,255,1)',
					name: this.lang.changeLang,
					options: [this.lang.chinese, this.lang.english],
					initStatus: this.lang.langType == 'zh-CN' ? 0 : 1
				},
				{
					type: 0,
					lefticon_src: 'exclamation-circle-888888',
					righticon_src: 'angle-right-888888',
					style: 'rgba(240,240,240,1)',
					name: this.lang.about,
					url: '../about/about',
				}
			];
		},
		
		uploadFace(rsp) {
			var path = rsp.path;
			uni.uploadFile({
				url: this.$serverUrl + '/user/uploadFace',
				filePath: path,
				name: 'file',
				formData: {
					userId: this.thisUserInfo.id
				},
				success: res => {
					// uploadFile 返回的 res.data 是 String
					var data = JSON.parse(res.data);
					if (data.status == 200) {
						this.thisUserInfo.faceImg = data.data;
					}
				}
			});
		},

		editTouXiang() {},
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
					console.log(res);
					if (res.data.status == 200) {
						var user = res.data.data;
						var finalUser = this.myUser(user); // 分割邮箱地址, 重构 user
						this.setGlobalUserInfo(finalUser); // 把用户信息写入缓存
						this.thisUserInfo = finalUser; // 更新页面用户数据
						// console.log(this.thisUserInfo);
					}
				}
			});
		},
		navigatorEvent(e) {
			if (e.action == 'switchLang') {
				this.changeLang(e.status);
				console.log(e.status);
			} else if (e.action == 'goto') {
				uni.navigateTo({
					url: e.obj.url
				});
			}
		},

		//粉丝数是否改变
		isFansNumChange() {
			//TODO: 多了就加小红点获其他动效
		},

		goToChatPage: function() {
			var encodeData = encodeURIComponent(JSON.stringify(this.thisUserInfo)); // 对数据字符串化并转码，防止特殊字符影响传参
			uni.navigateTo({
				url: '../chatpage/chatpage?friendInfo=' + encodeData
			});
		},

		/**
		 * @param {Object} currentTab 0: 关注 1: 粉丝
		 */
		goToFansFollow: function(currentTab) {
			console.log('goToFansFollow...');
			var data = {
				currentTab: currentTab,
				thisUserInfo: this.thisUserInfo
			};
			var encodeData = encodeURIComponent(JSON.stringify(data)); // 对数据字符串化并转码，防止特殊字符影响传参
			uni.navigateTo({
				url: '../followlist/followlist?data=' + encodeData
			});
		},
		
		jumpToWeb(jumpUrl) {
			// TODO: 兼容网页跳转和小程序内跳转
			console.log(jumpUrl);
			var encodeData = encodeURIComponent(jumpUrl);
			uni.navigateTo({
				url: '../adWebPage/adWebPage?url=' + encodeData
			});
		},
		
		/**
		 * 获取页面广告
		 */
		getAdByPosition(position, num, userId) {
			var that = this;
			uni.request({
				url: that.$serverUrl + '/advert/getAdByPosition',
				method: 'POST',
				data: {
					position: position,
					num: num,
					userId: userId
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					if (res.data.status == 200) {
						this.adverts = res.data.data;
					}
				}
			});
		},
	}
};
</script>

<style>
page {
	width: 100%;
	height: 100%;
}

#public-container {
	height: 100%;
	width: calc(100% - 50px);
	margin: auto;
}

.topbox {
	width: 100%;
}

.header {
	display: flex;
	align-items: center;
	align-content: center;
}

.touxiang {
	display: inline-block;
	width: 80px;
	height: 80px;
	position: relative;
}

.image {
	width: 72px;
	height: 72px;
	background: rgba(255, 255, 255, 1);
	border-radius: 50%;
}

.person_info {
	margin-left: 24px;
	display: inline-block;
	width: calc(100% - 80px - 25px);
	height: 80px;
}

.nameBox {
	display: flex;
	margin-top: 12px;
	height: 17px;
	line-height: 17px;
}

.nameBox .name-text {
	font-size: 17px;
	font-weight: 600;
	height: 17px;
	line-height: 17px;
}

.operationTitle {
	margin-top: 8px;
	height: 12px;
	line-height: 12px;
}

.operationNum {
	margin-top: 7px;
	height: 14px;
	line-height: 14px;
}

.operationTitle-text {
	vertical-align: top;
	display: inline-block;
	width: 30%;
	height: 12px;
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 12px;
	color: rgba(178, 178, 178, 1);
}

.operationNum-text {
	display: inline-block;
	vertical-align: top;
	width: 30%;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 800;
	opacity: 1;
	margin-left: 3px;
}

.shadow1 {
	width: 100%;
	height: 2.5px;
	box-shadow: 5px 5px 10px #d8d8d8 inset;
	margin-left: -25px;
	padding-right: 50px;
}

.shadow2 {
	width: 100%;
	height: 2.5px;
	box-shadow: 5px 5px 10px #ececec inset;
	margin-left: -25px;
	padding-right: 50px;
}

.introBox {
	margin-top: 15px;
	margin-bottom: 15px;
}

.introBox-text {
	max-width: 61%;
	font-size: 12px;
	line-height: 13px;
	opacity: 1;
	word-wrap: break-word;
}

.guanggao {
	width: 100%;
	height: 150px;
}

.swiperBg {
	/* background: rgba(136, 136, 136, 1); */
	width: 100%;
	height: calc(100% - 40px);
	border-radius: 4px;
	margin-top: 20px;
	margin-bottom: 20px;
}
</style>
