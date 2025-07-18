<template>
	<view id="public-container">
		<!-- 占位块 -->
		<view :style="{ width: 100 + '%', height: this.getnavbarHeight().bottom + 5 + 'px' , 'background-color': 'white' }">
		</view>
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar"
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		:left-text="lang.back"
		:title="lang.personalHome" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>				
		
		<!-- 简介块 -->
		<view id="public-infoBox" class="column_center">
			<!-- 基本信息内容 -->
			<view id="idCard" class="idCard" :style="{ width: windowWidth - 26  + 'px'}">
				<!-- 头像 -->
				<view style="width: 100%;height: 74px;margin-top: -46px;" class="super_center">
					<image class="publicTouxiang" mode="aspectFill" :src="pathFilter(thisUserInfo.faceImg)"></image>
				</view>
				<!-- ID -->
				<view class="nameBox super_center">
					<text class="name-text">{{ thisUserInfo.nickname }}</text>
					<image v-if="thisUserInfo.authType == 2" style="width: 20px;height: 20px;margin-left: 5px;" src="../../static/icon/auth_red.png"></image>
					<image v-if="thisUserInfo.authType == 1" style="width: 20px;height: 20px;margin-left: 5px;" src="../../static/icon/auth_yellow.png"></image>
				</view>
				<!-- 个人简介 -->
				<view v-if="thisUserInfo.signature == null" class="introBox super_center"><text class="introBox-text">{{ lang.lazyNoSignature }}</text></view>
				<view v-else class="introBox super_center"><text class="introBox-text">{{ thisUserInfo.signature }}</text></view>
				<!-- 判断是否为本人主页 -->
				<view class="guanzhuBox super_center" v-if="!myPublic">
					<view class="guanzhuLine super_center">
						<view style="height: 100%;display: flex;">
							<!-- 关注&已关注 -->
							<view 
								class="guanzhuButton" 
								@tap="addFollow(thisUserInfo.id)" 
								v-if="thisUserInfo.follow == false">
								<view style="height: 100%;width: 100%;border-radius: 4px;" class="super_center" hover-class="hoverColorYellow">
									<text class="guanzhuButton-text">{{lang.follow}}</text>
								</view>
							</view>
							<view
								class="guanzhuButton-after super_center"
								style="border:1px solid rgba(255,201,90,1);"
								@tap="cancelFollow(thisUserInfo.id)"
								v-if="thisUserInfo.follow == true"
							>
								<view style="height: 100%;width: 100%;border-radius: 4px" class="super_center" hover-class="hoverColor">
									<text class="guanzhuButton-text-after">{{lang.followed}}</text>
								</view>
							</view>
							<!-- 私信 -->
							<view class="messageButton super_center" @tap="goToChatPage">
								<image
									src="../../static/icon/comment-personpublic.png"
									mode="scaleToFill"
									style="width:18px;
								height:18px;
								opacity:1;"
								></image>
							</view>
						</view>
					</view>
				</view>
				<!-- 操作行 -->
				<view class="operationLine">
					<!-- 粉丝 -->
					<view class="operationCard" hover-class="hoverColor" @tap="goToFansFollow(1)">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.fansNum }}</text>
						</view>
						<view class="operationTitle super_center"><text class="operationTitle-text">{{lang.fans}}</text></view>
					</view>
					<!-- 影响力 -->
					<view class="operationCard">
						<view class="operationNum super_center"><text class="operationNum-text" style="color:rgba(254,95,85,1);">{{ thisUserInfo.reputation }}</text></view>
						<view class="operationTitle super_center"><text class="operationTitle-text">{{lang.reputation}}</text></view>
					</view>
					<!-- 关注 -->
					<view class="operationCard" hover-class="hoverColor" @tap="goToFansFollow(0)">
						<view class="operationNum super_center">
							<text class="operationNum-text" style="color:color:rgba(53,53,53,1);">{{ thisUserInfo.followNum }}</text>
						</view>
						<view class="operationTitle super_center"><text class="operationTitle-text">{{lang.follow}}</text></view>
					</view>
				</view>
			</view>
		</view>
		<!-- 信息块标题 -->
		<view id="public-titleBox"><text class="public-title-text">个人信息</text></view>
		<!-- 信息块 -->
		<view id="publix-profileLine" :style="{ width: windowWidth - 26  + 'px'}">
			<view class="profileCard">
				<!-- 检测是否设置 -->
				<text v-if="thisUserInfo.graduationYear != null" class="profile-content-text">{{ thisUserInfo.graduationYear }}</text>
				<text v-else class="profile-content-text">未知</text>
				<text class="profile-title-text">毕业年份</text>
			</view>
			<view class="profileCard">
				<!-- 检测是否设置 -->
				<text v-if="thisUserInfo.major != null" class="profile-content-text">{{ thisUserInfo.major }}</text>
				<text v-else class="profile-content-text">未知</text>
				<text class="profile-title-text">专业</text>
			</view>
		</view>
		<!-- 发布块标题 -->
		<view id="public-titleBox"><text class="public-title-text">发布文章</text></view>	
		<!-- 发布内容块 -->
		<view :style="{ width: windowWidth - 26  + 'px'}" v-bind:myArticleList="myArticleList">
			<view id="public-articleCard" :style="{ width: windowWidth - 26  + 'px'}" v-for="thisArticle in myArticleList" :key="thisArticle.id" @click="jumpToDetail(thisArticle)">
				<view style="height: 100%;width: 100%;border-radius: 8px;padding: 12px 0px;" hover-class="hoverColor">
					<view v-if="!isNull(thisArticle.articleTitle)" class="articleTitle">{{ thisArticle.articleTitle }}</view>
					<!-- 发布内容行 -->
					<view class="articleContentLine">
						<!-- 文字部分卡片 -->
						<!-- 有图跟无图只有 teamareaCard 这一个 class 的区别，判断只需要更改这一个 class -->
						<view :class="[thisArticle.imgList.length > 0?'textareaCard-img':'textareaCard']">
							<view class="textContent">
								{{thisArticle.articleContent}}
							</view>
							<!-- 底部栏 -->
							<view class="bottomBar">
								<view style="position: relative;width: 100%;height: 100%;">
									<view class="bottom-time column_center"><text>{{timeDeal(thisArticle.createDate)}}</text></view>
									<!-- <view class="view column_center">
										<image src="../../static/icon/eye-888888.png" mode="aspectFill"></image>
										<text>{{thisArticle.viewNum}}</text>
									</view> -->
									<view class="comment column_center">
										<image src="../../static/icon/comment-alt.png" mode="aspectFill"></image>
										<text>{{thisArticle.commentNum}}</text>
									</view>
									<view class="like column_center">
										<image src="../../static/icon/like.png" mode="aspectFill"></image>
										<text>{{thisArticle.likeNum}}</text>
									</view>
								</view>
							</view>
						</view>
						<!-- 图片部分盒子 -->
						<view class="imgBox" v-if="thisArticle.imgList.length > 0"><image mode="aspectFill" :src="pathFilter(thisArticle.imgList[0].imagePath)"></image></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 占位块 -->
		<view style="height: 30px;" :style="{ width: windowWidth - 26  + 'px'}"></view>
	</view>
</template>

<script>
import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"
import { mapState, mapMutations } from 'vuex';

var me;
var userId; // this user's id
var loadArticleFlag = false;
export default {
	components:{
		uniNavBar
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			screenWidth: 350,
			serverUrl: this.$serverUrl,

			thisUserInfo: '',
			// myUserInfo:'',
			myPublic: false,
			windowHeight: 0,
			windowWidth: 0,
			yellowBottom: '',
			ifhaveImg: 0,

			totalPage: 1,
			currentPage: 1,
			totalNum: '0',
			myArticleList: '',
			isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
		};
	},

	onLoad(opt) {
		// console.log(opt);
		userId = opt.userId;
		me = this.getGlobalUserInfo();
		if (this.isNull(me)) {
			uni.redirectTo({
				url: '../../pages/signin/signin'
			})
			return;
		} else{
			this.me = me;
		}
		if (me.id == userId) {
			// 如果打开自己的页面，屏蔽关注和发私信按钮
			this.myPublic = true;
		}

		var screenWidth = uni.getSystemInfoSync().screenWidth;
		this.screenWidth = screenWidth;

		// 获取当前分页
		var page = this.page;

		// 获取这个人的信息, TODO: 更新本地用户信息缓存
		this.queryUserWithFollow(userId);

		//获取屏幕宽高
		var that = this;
		uni.getSystemInfo({
			success: function(res) {
				that.windowHeight = res.screenHeight;
				that.windowWidth = res.screenWidth;
			}
		});

		this.showArticles(1);
	},

	onPullDownRefresh() {
		console.log('refresh');
		setTimeout(function() {
			uni.stopPullDownRefresh();
		}, 1000);
	},

	methods: {
		/**
		 * 添加关注
		 */
		addFollow: function(userId) {
			console.log('加关注...');
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/follow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if (res.data.status == 200) {
						// 刷新用户信息
						that.queryUserWithFollow(userId);
					}
				}
			});
		},
		/**
		 * 取消关注
		 */
		cancelFollow: function(userId) {
			console.log('取关...');
			var that = this;
			uni.request({
				url: that.$serverUrl + '/user/dontFollow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if (res.data.status == 200) {
						// 刷新用户信息
						that.queryUserWithFollow(userId);
					}
				}
			});
		},

		goToChatPage: function() {
			var userInfo = this.getGlobalUserInfo();
			if(this.isNull(userInfo.email)){
				uni.showToast({
					icon:'error',
					title:'未绑定邮箱'
				})
			}else{
				var encodeData = encodeURIComponent(JSON.stringify(this.thisUserInfo)); // 对数据字符串化并转码，防止特殊字符影响传参
				uni.navigateTo({
					url: '/pages/chatpage/chatpage?friendInfo=' + encodeData,
				});
			}
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
				url: '../followlist/followlist?data=' + encodeData,
			});
		},

		/**
		 * 查询该用户信息和我是否关注该用户
		 * @param {Object} userId 该用户 id
		 */
		queryUserWithFollow: function(userId) {
			var that = this;
			// console.log(userId);
			uni.request({
				url: that.$serverUrl + '/user/queryUserWithFollow',
				method: 'POST',
				data: {
					userId: userId,
					fanId: me.id
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					// console.log(res)
					if (res.data.status == 200) {
						console.log(res);
						that.thisUserInfo = res.data.data;
						that.setUserInfoToUserList(res.data.data); //更新缓存
						// console.log(res.data.data)
						// console.log(that.getUserInfoFromUserList(res.data.data.id))
						// console.log(that.getListByKey("userList"))

						// 设置title
						// that.pageTitle = that.thisUserInfo.nickname + '的主页'
					}
				}
			});
		},

		showArticles: function(page) {
			console.log(loadArticleFlag);

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
			// console.log(that.thisUserInfo);
			uni.request({
				url: that.$serverUrl + '/article/queryPublishHistory',
				method: 'POST',
				data: {
					page: page,
					userId: me.id,
					targetId: userId
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
		
		jumpToDetail(thisArticle) {
			uni.navigateTo({
				url: '/pagesSubA/detail/detail?data=' + thisArticle.id
			});
		},
	}
};
</script>

<style>
page {
	width: 100%;
}

#public-container {
	height: 100%;
	width: 100%;
	overflow: hidden;
}

/* 简介块 */
#public-infoBox {
	width: 100%;
}

.publicTouxiang {
	width: 74px;
	height: 74px;
	border-radius: 120px;
	border: 4px solid white;
	display: inline-block;
	vertical-align: middle;
}

.idCard {
	margin-top: 64px;
	margin-left: 13px;
	border-radius: 8px;
	min-height: 140px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}

.nameBox {
	margin-top: 8px;
	width: 100%;
}

.nameBox .name-text {
	font-size: 17px;
	font-weight: 600;
}

.introBox {
	margin-top: 12px;
	width: 100%;
}

.introBox-text {
	font-size: 12px;
	font-weight: 400;
	line-height: 13px;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
	word-break: keep-all;
	word-wrap: break-word;
}

.guanzhuLine {
	margin-top: 17px;
	height: 26px;
	width: 100%;
	display: flex;
}

.guanzhuButton {
	/* margin-left: 35.2%; */
	margin-right: 0px;
	width: 68px;
	height: 26px;
	background-color: rgba(255, 201, 90, 1);
	box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 4px;
}

.guanzhuButton-after {
	/* margin-left: 31.2%; */
	margin-right: 0px;
	width: 82px;
	height: 26px;
	opacity: 1;
	border-radius: 4px;
}

.guanzhuButton-text {
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(255, 255, 255, 1);
	opacity: 1;
}

.guanzhuButton-text-after {
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 13px;
	color: rgba(255, 201, 90, 1);
	opacity: 1;
}

.messageButton {
	margin-left: 8px;
	margin-top: -1px;
	width: 28px;
	height: 28px;
	background: rgba(255, 241, 213, 1);
	border-radius: 14px;
	opacity: 1;
}

.operationLine {
	margin: 17px 6%;
	width: 88%;
	height: 43px;
	display: flex;
	justify-content: space-between;
}

.operationCard {
	width: 30%;
	height: 43px;
	border-radius: 6px;
}

.operationNum {
	width: 100%;
	height: 17px;
	overflow: visible;
}

.operationNum-text {
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 800;
	opacity: 1;
}

.operationTitle {
	margin-top: 11px;
	width: 100%;
	height: 11px;
	overflow: visible;
}

.operationTitle-text {
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(178, 178, 178, 1);
	opacity: 1;
}

/* 标题 */
#public-titleBox {
	width: 100%;
	margin-top: 20px;
	margin-bottom: 12px;
}

.public-title-text {
	margin-left: 13px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: bold;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
	letter-spacing: 1px;
}

/* 他的信息块 */
#publix-profileLine {
	margin-left: 13px;
	border-radius: 8px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
	display: flex;
	justify-content: space-between;
}

.profileCard {
	width: 50%;
	display: flex;
	flex-direction: column;
}

.profile-content-text {
	margin-top: 16px;
	margin-left: 38px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(53, 53, 53, 1);
	opacity: 1;
}

.profile-title-text {
	margin-bottom: 16px;
	margin-top: 8px;
	margin-left: 38px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	color: rgba(178, 178, 178, 1);
	opacity: 1;
}

/* 他的发布文章块 */
#public-articleCard {
	margin-bottom: 8px;
	max-height: 114px;
	margin-left: 13px;
	border-radius: 8px;
	background-color: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
}

.articleTitle {
	width: calc(100% - 32px);
	margin-left: 16px;
	font-size: 15px;
	font-family: Source Han Sans CN;
	font-weight: 550;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	line-height: 17px;
}

.articleContentLine {
	margin-top: 12px;
	width: calc(100% - 32px);
	margin-left: 16px;
	display: flex;
}

/* 定高 */
.textareaCard {
	position: relative;
	height: 63px;
	width: 100%;
}

.textareaCard-img {
	position: relative;
	height: 63px;
	width: calc(100% - 61px);
	margin-right: 12px;
}

.textContent {
	position: absolute;
	top: 0;
	width: 100%;
	max-height: 39px;
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 13px;
	color: rgba(53, 53, 53, 1);
	opacity: 1;

	word-break: break-all;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
}

.bottomBar {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 12px;
}

.bottomBar image {
	height: 12px;
	width: 12px;
}

.bottomBar text {
	margin-left: 4px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(136, 136, 136, 1);
	opacity: 1;
}

.bottom-time {
	position: absolute;
	width: 84px;
	height: 12px;
}

.bottomBar .bottom-time text {
	margin-left: 0px;
	font-size: 10px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
	white-space: nowrap;
}

.view {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 86px;
}

.comment {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 43px;
}

.like {
	position: absolute;
	width: 43px;
	height: 12px;
	right: 0;
}

.imgBox {
	width: 61px;
	height: 61px;
}

.imgBox image {
	width: 61px;
	height: 61px;
}
</style>
