<template>
	<view id="container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}"
		:title="lang.myMessage" :showLeftIcon="false"
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }" style="width: 100%;"></view>
		
		<!-- 点赞和评论数量 -->
		<view id="msglist-likecommentnum">
			<view class="msglist-like column_center" hover-class="hoverColor" @tap="goToCmtLikeDetail(0)">
				<view class="msglist-like-bg super_center" style="background:rgba(255,93,93,1);">
					<image src="../../static/icon/thumbs_up_white.png" class="msglist-like-icon" mode=""></image>
				</view>
				<text class="msglist-top-text font-family">{{lang.like}}</text>
				<view class="msglist-top-num super_center" v-if="likeMsgCount>0">
					{{likeMsgCount}}
				</view>
				<!-- :class="[currentTab==index ? 'menu-one-act' : 'menu-one']" -->
			</view>
			<!-- 分割线 -->
			<view class="msglist-likecommentnum-border">
			</view>
			<view class="msglist-comment column_center" hover-class="hoverColor" @tap="goToCmtLikeDetail(1)">
				<!-- Comment background -->
				<view class="msglist-comment-bg super_center" style="background:rgba(0,132,233,1);">
					<image src="../../static/icon/comment-alt-ffffff.png" class="msglist-comment-icon" mode=""></image>
				</view>
				<text class="msglist-top-text font-family">{{lang.comment}}</text>
				<view class="msglist-top-num super_center " v-if="commentMsgCount>0">
					{{commentMsgCount}}
				</view>
			</view>
			<!-- 消息内容滑块区 -->
			<!-- <scroll-view scroll-y="true" class="messagesBox" :style="{ top: this.getnavbarHeight().bottom + 143 + 'px'}"> -->
				<!-- 新消息卡片 -->
				<!-- <view>
					<view class="message-list" :style="{ top: this.getnavbarHeight().bottom + 143 + 'px'}">
						<swipe-action :lang="lang" :options="options" :messagesList="chatSnapShotList" @tapCard="goToChatpage()" @tapDelete="deleteChat()"></swipe-action>
					</view>
				</view>
			</scroll-view> -->
		</view>
		
		<view class="message-list messagesBox" :style="{ top: this.getnavbarHeight().bottom + 143 + 'px'}">
			<swipe-action :lang="lang" :options="options" :messagesList="chatSnapShotList" @tapCard="goToChatpage()" @tapDelete="deleteChat()"></swipe-action>
			<view style="width: 100%;height: 100px;"></view>
		</view>
		<tab-bar :current="3"></tab-bar>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import swipeAction from '../../components/swipe-action.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';

var userInfo;

var socketTask;
var socketOpen = false;

export default {
	components: {
		swipeAction,
		uniNavBar,
		tabBar
	},
	data() {
		return {
			focus: false,
			isShowView: true,

			msgicon: [],
			chatSnapShotList: '',

			READ: this.chat.READ,
			UNREAD: this.chat.UNREAD,
		};
	},

	computed: {
		...mapState(['chatMessageCard', 'likeMsgCount', 'commentMsgCount', 'lang'])
	},

	watch: {
		chatMessageCard(newVal, oldVal) {
			// 重载快照
			this.loadingChatSnapshot();
			// console.log("newVal:");
			// console.log(newVal);
		},
	},

	onLoad: function() {
		uni.setNavigationBarTitle({
			title: '消息列表'
		});

		userInfo = this.getGlobalUserInfo();
		if (this.isNull(userInfo)) {
			console.log('No userInfo!!');
			return;
		}

		// [测试代码块]
		// this.mySocket.init();
		// console.log(this.uLikeMsgCount);
	},

	onShow() {
		this.loadingChatSnapshot(); // 载入聊天快照
	},

	methods: {
		...mapMutations(['changeLang']), //引用vuex中的方法

		/**
		 * 展示聊天快照，渲染列表.
		 */
		loadingChatSnapshot() {
			var chatSnapShotList = this.chat.getUserChatSnapShot(userInfo.id);
			// 提前渲染
			this.chatSnapShotList = chatSnapShotList;
			// 拼接信息: 根据 friendId 获取用户信息
			var sendCount = 0; // 网络请求为异步，计数返回结果判断是否全部完成
			var receiveCount = 0;
			console.log(chatSnapShotList);
			for (var i = 0; i < chatSnapShotList.length; i++) {
				var thisFrindId = chatSnapShotList[i].friendId;
				// 查看缓存
				var thisFriendInfo = this.getUserInfoFromUserList(thisFrindId);
				if (this.isNull(thisFriendInfo)) {
					// 不在缓存中, 向服务器请求
					sendCount++;
					var that = this;
					console.log(thisFrindId);
					uni.request({
						url: that.$serverUrl + '/user/queryUser',
						method: 'POST',
						data: {
							userId: thisFrindId
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							// console.log(res)
							if (res.data.status == 200) {
								// 获取返回的用户信息 写到缓存里
								thisFriendInfo = res.data.data;
								that.setUserInfoToUserList(thisFriendInfo);

								// 查看 sent & receive 是否相等
								receiveCount++;
								console.log('sendCount=' + sendCount + ' receiveCount=' + receiveCount);
								if (sendCount == receiveCount) {
									// 再次加载渲染
									that.loadingChatSnapshot();
								}
							}
						}
					});
				} else {
					// 添加朋友信息到快照列表对象
					chatSnapShotList[i].friendInfo = thisFriendInfo;
				}
			}
		},

		/**
		 * 右滑删除, TODO: 施工中...未测试
		 */
		deleteChat(e) {
			// 获取朋友 id
			var frindId = e.friendId;
			// 1. 删除我和朋友的聊天记录
			this.chat.deletUserChatHistory(userInfo.id, frindId);
			// 2. 删除快照
			this.chat.deletUserChatSnapShot(userInfo.id, frindId);
			// 重载快照
			this.loadingChatSnapshot();
		},

		goToChatpage(e) {
			// console.log(e)
			var userInfo = this.getGlobalUserInfo();
			if(this.isNull(userInfo.email)){
				uni.showToast({
					icon:'error',
					title:'未绑定邮箱'
				})
			}else{
				var myId = e.myId;
				var friendId = e.friendId;
				var msg = e.msg;
				var friendInfo = e.friendInfo;
				// 覆盖快照，设为已读 (myId, friendId, msg, isRead)
				this.chat.readUserChatSnapShot(myId, friendId);
				// this.chat.saveUserChatSnapshot(e.myId, e.friendId, e.msg, this.chat.READ);

				var encodeData = encodeURIComponent(JSON.stringify(friendInfo)); // 对数据字符串化并转码，防止特殊字符影响传参
				// console.log(decodeURIComponent(encodeData));
				uni.navigateTo({
					url: '../chatpage/chatpage?friendInfo=' + encodeData
				});
			}
		},

		/**
		 * @param {Object} currentTab 0: 点赞 1: 评论
		 */
		goToCmtLikeDetail(currentTab) {
			// 计数值归零
			if (currentTab == 0) {
				this.$store.commit('setLikeMsgCount', 0);
			} else {
				this.$store.commit('setCommentMsgCount', 0);
			}

			uni.navigateTo({
				url: '../cmt-likedetail/cmt-likedetail?currentTab=' + JSON.stringify(currentTab)
			});
		}
	}
};
</script>

<style>
	page {
		width: 100%;
		height: 100%;
	}

	#container {
		width: 100%;
		height: 100%;
		background-color: #FEFBFF;
	}

	#msglist-yellowbg {
		position: fixed;
		width: 3100upx;
		height: 3100upx;
		border-radius: 2000upx;
		background-color: #FDD041;
		top: -2860upx;
		left: -1520upx;
	}
	
	#msglist-yellowshadowbg image{
		width: 100%;
		height: 200px;
	}

	#msglist-likecommentnum {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		position: relative;
		width: 90.4%;
		left: 4.8%;
		height: 129px;
		background-color: white;
		margin-top: 8px;
		height:129px;
		box-shadow:0px 0px 6px rgba(0,0,0,0.16);
		opacity:1;
		border-radius:8px;
	}

	.msglist-like {
		position: relative;
		width: 100%;
		height: 49%;
		border-radius: 8px;
	}

	.msglist-like-bg,.msglist-comment-bg {
		position: absolute;
		left: 23px;
		width:36px;
		height:36px;
		border-radius:50%;
		opacity:1;
	}

	.msglist-like-icon,.msglist-comment-icon {
		width:18px;
		height:18px;
		opacity:1;
	}

	.msglist-likecommentnum-border {
		width: 90%;
		height: 1px;
		background-color: rgba(236,236,236,1);
		margin-left: 5%;
	}

	.msglist-comment {
		width: 100%;
		height: 49%;
		border-radius: 8px;
	}

	.msglist-top-text {
		position: absolute;
		left: 75px;
		height:23px;
		font-size:17px;
		font-family:Source Han Sans CN;
		font-weight:500;
		line-height:23px;
		color:rgba(53,53,53,1);
		opacity:1;
	}

	.msglist-top-num {
		position: absolute;
		padding: 1px 3px;
		right: 22px;
		min-width:18px;
		height:17px;
		background:rgba(242,94,94,1);
		opacity:1;
		border-radius:4px;
		font-size:11px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:22px;
		color:rgba(255,255,255,1);
		opacity:1;
	}

	.messagesBox{
		/* position: fixed; */
		left: 0;
		width: 100%;
	}
	
	.msglist-card {
		width: 88%;
		margin-left: 6%;
		margin-top: 8upx;
		height: 120upx;
		background-color: white;
		border-radius: 12upx;
		box-shadow: 0upx 0upx 14upx 0upx #B2B2B2;
	}

	.msglist-card-read {
		width: 88%;
		margin-left: 6%;
		margin-top: 8upx;
		height: 120upx;
		background-color: white;
		border-radius: 12upx;
	}

	.msglist-Touxiang {
		margin-left: 24upx;
		width: 76upx;
		height: 76upx;
		border-radius: 999upx;
		display: inline-block;
		vertical-align: middle;
	}

	.msglist-content {
		margin-left: 20upx;
		width: 480upx;
		height: 70upx;
		/* display: flex; */
		display: inline-block;
	}

	.msglist-id {
		width: 100%;
		height: 32upx;
		font-size: x-small;
		font-weight: 550;
		color: #353535;
	}

	.msglist-id-read {
		width: 100%;
		height: 32upx;
		font-size: x-small;
		font-weight: 500;
		color: #9b9b9b;
	}

	.msglist-brief {
		width: 100%;
		font-weight: 500;
		height: 32upx;
		font-size: xx-small;
		color: #6f6f6f;
		margin-top: 6upx;
	}

	.msglist-brief-read {
		width: 100%;
		height: 32upx;
		font-size: xx-small;
		color: #9b9b9b;
		margin-top: 6upx;
	}

	.time-numicon {
		position: relative;
		margin-left: 2upx;
		width: 48upx;
		height: 76upx;
		
	}

	.msglist-time {
		position: absolute;
		right: 0;
		height: 36upx;
		width: 100%;
		font-size: 17upx;
	}

	.msglist-icon {
		position: absolute;
		right: 0upx;
		bottom: 0upx;
		height: 17px;
		width: 17px;
		color: white;
		background-color: #FDD453;
		font-size: 20upx;
		border-radius: 999upx;
	}
</style>
