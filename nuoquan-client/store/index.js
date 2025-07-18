import Vue from 'vue'
import Vuex from 'vuex'
import userUtil from '../common/userUtil.js'
import showModal from '../components/showModal.js'		//全局弹窗模块
import request from '../components/request-API.js'

Vue.use(Vuex)

/**
 * 如果为空返回0，否则返回本身
 * @param {Object} str
 */
var isNullThenZero = function(str) {
	if (str == null || str == "" || str == undefined) {
		return 0;
	}
	return str;
}
var userInfo = userUtil.getGlobalUserInfo();
// 消息提示相关变量
var myMsgCountKey = userInfo.id + ":myMsgCount";
var totalChatMsgCountKey = userInfo.id + ":totalChatMsgCount"
var likeMsgCountKey = userInfo.id + ":likeMsgCount"
var commentMsgCountKey = userInfo.id + ":commentMsgCount" 
// 国际化相关代码
try {
	var langList = ['简体中文', 'English'];
	var langCode = ['zh-CN', 'en'];
	// 1. 分析用户已经选择的语言 
	var userLang = uni.getStorageSync("userLang");
	// 2. 如果用户没有选择过获取用户手机的语言
	if (!userLang) {
		const sys = uni.getSystemInfoSync();
		userLang = sys.language;
	}
	console.log(userLang);
	// 以中英文切换为例, 其他语言请使用 getSystemInfoSync 获取语言对应的字符串
	// 然后扩展语言包即可
	if (userLang.substring(0, 2) == 'zh') {
		var lang = require('../common/language/zh.js');
	} else {
		var lang = require('../common/language/en.js');
	}
} catch (e) {
	// error
}
function initialRequest(){
	// updateLoginTime
	// getUserInfo
	
}

const store = new Vuex.Store({

	state: {
		lang: lang ,//语言
		
		chatMessageCard: '', // 暂存一条socket接收的聊天消息 & 刷新消息列表的条件
		// flashChatPage: "doFlash", // 作为触发 chatPage 刷新的条件
		myMsgCount: isNullThenZero(uni.getStorageSync(myMsgCountKey)), // 所有消息计数
		totalChatMsgCount: isNullThenZero(uni.getStorageSync(totalChatMsgCountKey)), //聊天消息总计数
		likeMsgCount: isNullThenZero(uni.getStorageSync(likeMsgCountKey)), // 点赞未读消息计数
		commentMsgCount: isNullThenZero(uni.getStorageSync(commentMsgCountKey)), // 评论未读消息计数
	},
	mutations: {
		
		/**
		 * 更改语言包
		 * @param {Object} state
		 */
		changeLang: function(state,index) {
			console.log(state);
			console.log(index);
			if (index == 0) {
				lang = require('../common/language/zh.js');
			} else {
				lang = require('../common/language/en.js');
			}
			uni.setStorageSync('userLang', langCode[index]);
			state.lang = lang;
		},
		
		// 消息提示相关
		setChatMessageCard(state, value) {
			state.chatMessageCard = value.newValue;
			//修改totalChatMsgCount
			if (value.newValue.unreadCount == 0) {
				state.totalChatMsgCount = state.totalChatMsgCount - value.oldValue.unreadCount;
			} else {
				state.totalChatMsgCount++
			}
			uni.setStorageSync(totalChatMsgCountKey, state.totalChatMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
		
		setLikeMsgCount(state, value) {
			if (value == undefined) {
				state.likeMsgCount++;
			} else {
				state.likeMsgCount = value;
			}
			uni.setStorageSync(likeMsgCountKey, state.likeMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
		
		setCommentMsgCount(state, value) {
			if (value == undefined) {
				state.commentMsgCount++;
			} else {
				state.commentMsgCount = value;
			}
			uni.setStorageSync(commentMsgCountKey, state.commentMsgCount);
			//计算总数
			state.myMsgCount = state.totalChatMsgCount + state.likeMsgCount + state.commentMsgCount;
			uni.setStorageSync(myMsgCountKey, state.myMsgCount);
		},
	},
	modules:{
		showModal:showModal,	//全局弹窗模块
		request:request,
	},
})

export default store;

