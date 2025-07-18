<!-- 本页面的 websocket 应写在 messagelist 里-->
<template>
	<view style="height:100%;width:100%;">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="pageTitle" :height="navbarHeight"></uni-nav-bar>
		<view :style="{height: navbarHeight + 'px'}"></view>
		<scroll-view id="scrollview" class="messageArea" :style="{ height: textareaHeight }" scroll-y="true" @scrolltoupper="loadMore"
		 :scroll-top="scrollTop" :scroll-into-view="scrollToView">
			<!-- 占位，防止 navbar 盖住内容 -->
			<onemessage v-for="(item, index) in chatContent" :key="index" :thisMessage="item" :userInfo="userInfo" :friendInfo="friendInfo"
			 :id="item.id"></onemessage>
			<view class="marginHelper" :style="{ height : marginHelperHeight }"></view>
		</scroll-view>
		<chat-input id="chatarea" :inputMessageFromPage="textMsg" :maxlength="maxTextLength" @send="sendText()" @change="change()" @recover="recover()" :windowWidth="windowWidth" :lang="lang"
		 style="position: fixed;bottom:0;width:100%;"></chat-input>
	</view>
</template>

<script>
	import onemessage from './oneMessage';
	import {
		mapState
	} from 'vuex';
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"
	import chatInput from "@/components/chatInput.vue"

	var page = 1; // PS: 非显示属性不放在渲染层
	var chatKey;
	var chatHistory;
	export default {
		components: {
			onemessage,
			uniNavBar,
			chatInput,
		},
		data() {
			return {
				/**
					 * ChatMessageCard example
					ChatMessageCard = {
						this.senderId = senderId;
						this.receiverId = receiverId;
						this.msg = msg;		// 显示
						this.msgId = msgId; // 前端不需要用到
						this.creatTime = creatTime; // 显示 flag
					},
					*/
				pageTitle: 'Message', //这里要换成聊天对象的昵称
				chatContent: [],

				socketMsgQueue: [], // 未发送的消息队列
				textMsg: '', // 输入框中的text
				windowHeight: '',
				windowWidth: '',
				scrollToView: '',
				scrollTop: 0,

				userInfo: '',
				friendInfo: '',
				textareaHeight: '', //聊天内容高度
				textareaWidth: 0, //聊天框宽度
				mitemHeight: 0,
				maxTextLength: 150, 	// 聊天输入最长长度

				keyboardHeight: '',
				showEmojiFlag: false,

				isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮

				navbarHeight: 0 //一次性储存 navbarheight
			};
		},

		computed: {
			...mapState(['chatMessageCard', 'flashChatPage', 'lang'])
		},

		watch: {
			// 监听收到的消息
			chatMessageCard(newVal, oldVal) {
				// console.log("newVal:");
				console.log(newVal);
				// 渲染到窗口
				if (newVal.friendId == this.friendInfo.id) {
					newVal.id = this.generateId(0);
					this.chatContent.push(newVal);
					this.scrollToBottom();
				}
			},
			
			// 监听发送的消息
			// flashChatPage(newVal, oldVal) {
			// 	// 载入最后一条消息
			// 	var list = this.getListByKey(chatKey);
			// 	var msg = list[list.length - 1];
			// 	msg.id = this.generateId(0);
			// 	// this.chatContent.push(msg);
			// 	this.scrollToBottom();
			// }
		},

		onLoad(opt) {
			// 一次性储存 navbar 高度
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			// console.log(opt);
			page = 1; //初始化page,
			// 获取界面传参
			// console.log(opt.friendInfo)
			this.friendInfo = JSON.parse(decodeURIComponent(opt.friendInfo)); //解码

			// 设置页面tittle
			this.pageTitle = this.friendInfo.nickname
			// 获取我的信息
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				console.log('No userInfo!!');
				return;
			}
			this.userInfo = userInfo;

			// 获取屏幕高度
			var that = this;
			uni.getSystemInfo({
				success: function(res) {
					that.windowHeight = res.windowHeight;
					that.windowWidth = res.windowWidth;
				}
			});
			
			// 获取与该用户的聊天历史记录
			chatKey = 'chat-' + this.userInfo.id + '-' + this.friendInfo.id;
			chatHistory = this.getListByKey(chatKey);

			this.getChatHistory();

		},
		onReady() {
			var that = this;
			//动态改变聊天内容高度
			//获取输入框高度
			var query = uni.createSelectorQuery();
			query.select('#chatarea').boundingClientRect();
			query.exec(function(res) {
				console.log(res);
				console.log(res[0].height);

				that.keyboardHeight = res[0].height + 'px';

				//聊天卡片区域 = 屏幕高度 - 输入框高度 - 头部导航栏高度，赋值给聊天内容
				that.textareaHeight = that.windowHeight - res[0].height - that.navbarHeight + 'px';

				console.log(that.textareaHeight);
				that.$nextTick(async function() {
					
					await that.scrollToBottom();
					that.scrollTop = that.textareaHeight;
					console.log("re bottom");
				});
			});

			
		},

		methods: {


			scroll(e) {
				// console.log(e.detail);
			},
			
			sendText(msgToSend) {
				this.textMsg = msgToSend
				if (!this.textMsg) {
					return;
				}
				this.mySocket.sendObj(this.netty.CHAT, this.friendInfo.id, this.textMsg, null);
				this.textMsg = ''; //清空输入框

				// 渲染到窗口
				// var list = this.getListByKey(chatKey);
				// this.chatContent.push(list[list.length-1]);
				// this.scrollToBottom();
			},
			change(height){
				this.textareaHeight=this.textareaHeight-height
			},
			recover(height){
				this.textareaHeight=this.textareaHeight+height
			},
			// 进入获取聊天
			getChatHistory() {
				var localChatHistory = this.getChatHistoryPage(page);
				// 为每个消息设置唯一id
				var that = this;
				localChatHistory.forEach(function(item, index) {
					item.id = that.generateId(index);
				});
				this.chatContent = localChatHistory;
				this.$nextTick(function() {
					this.scrollToBottom();
				});
				// console.log(this.chatContent);
			},

			loadMore() {
				if (this.isHistoryLoading) {
					return;
				}
				this.isHistoryLoading = true; //参数作为进入请求标识，防止重复请求
				// this.scrollAnimation = false;//关闭滑动动画
				const viewId = this.chatContent[0].id; //记住第一个信息ID
				uni.showLoading({
					title: '加载中'
				});
				var p = page + 1;
				var list = this.chat.getUserChatHistory(this.userInfo.id, this.friendInfo.id, p);
				setTimeout(() => {
					if (list != null) {
						var that = this;
						list.forEach(function(item, index) {
							item.id = that.generateId(index);
						});
						this.chatContent = list.concat(this.chatContent);
						page++;
					}
					uni.hideLoading();

					//稳住页面，这段代码很重要，不然每次加载历史数据都会跳到顶部
					this.$nextTick(function() {
						this.scrollToView = viewId; //跳转上次的第一行信息位置
						// this.$nextTick(function() {
						// 	this.scrollAnimation = true;//恢复滚动动画
						// });
					});
					this.isHistoryLoading = false;
				}, 300);
			},

			/**
			 * 分页获取聊天历史，从列表尾部开始读取(反取) 静态历史
			 * @param {Object} myId
			 * @param {Object} friendId
			 * @param {Object} page
			 */
			getChatHistoryPage(page) {
				var list = chatHistory.reverse();
				var size = 20;
				var start = (page - 1) * size;
				var newList = [];
				if (list.length < start) {
					return null;
				} else {
					for (var i = 0; i < size; i++) {
						if (!this.isNull(list[start + i])) {
							newList.unshift(list[start + i]);
						}
					}
					return newList;
				}
			},

			generateId(index) {
				var id = 'm' + Math.floor(Math.random() * 1000) + '-' + index + new Date().valueOf();
				return id;
			},

			scrollToBottom() {
				// 将页面滚动到底部，
				console.log("scrollToBottom")
				if (this.chatContent.length > 0) {
					this.scrollToView = this.chatContent[this.chatContent.length - 1].id;
				}
				console.log(this.scrollToView);

				// var that = this;
				// var query = uni.createSelectorQuery();
				// query.selectAll('send').boundingClientRect();
				// query.select('#scrollview').boundingClientRect();
				// console.info(query);
				// query.exec(function (res) {
				// 	console.info(res);
				// 	that.mitemHeight = 0;
				// 	// res.forEach((i)=>{
				// 	// 	if(i)
				// 	// })
				// 	res[0].forEach(function (rect) {
				// 		console.info(rect.height);
				// 		that.mitemHeight = that.mitemHeight + rect.height + 20;
				// 	});

				// 	if (that.mitemHeight > that.textareaHeight) {
				// 		that.scrollTop = that.mitemHeight - that.textareaHeight;
				// 	}
				// 	console.log(that.mitemHeight)
				// 	console.log(that.textareaHeight)
				// 	console.log(that.scrollTop)
				// });
				// this.scrollTop = 99999;
			}
		}
	};
</script>
<style>
	page {
		height: 100%;
		width: 100%;
		background-color: #FEFBFF;
	}
</style>

<style scoped>
.messageArea {
	display: flex;
	/* margin-top: 30upx; */
	margin-bottom: 90upx;
	width: 100%;
	height: 94%;
}

.marginHelper {
	width: 100%;
	background-color: #FEFBFF;
}
</style>
