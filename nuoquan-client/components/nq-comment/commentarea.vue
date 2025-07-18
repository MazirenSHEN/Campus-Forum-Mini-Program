<template>
	<!--评论区域-->
	<view :style="{ width: areaWidth, margin: areaMargin, opacity: commentList.length != undefined ? 1 : 0 }">
		<!--评论数，排序方式-->
		<view class="comments-menu">
			<view class="comments-num">{{ commentNum }}{{ lang.commentNum }}</view>
			<nqSwitch
				v-if="lang.langType == 'zh-CN'"
				style="margin-top: 3px;"
				:bgSwitchLeft="'-13px'"
				:bgSwitchRight="'41px'"
				:options="[lang.time, lang.hotcomment]"
				@onChange="change_comment_order"
			></nqSwitch>
			<nqSwitch v-else style="margin-top: 3px;" :bgSwitchLeft="'-11px'" :bgSwitchRight="'41px'" :options="[lang.time, lang.hot]" @onChange="change_comment_order"></nqSwitch>
			<!-- <view class="comments-order">
                   <view class="order-in-time" :class="{ chosen : order == 0}" @tap="change_comment_order(0)">
					   时间
				   </view>
				   <view class="order-in-hot" :class="{ chosen : order != 0}" @tap="change_comment_order(1)">
				   	   热度
				   </view>
				   <view class="bg-of-order" :style="{'left':order == 0 ? '-13px;' :'41px' ,}"></view>
			</view> -->
		</view>

		<!--循环评论卡片-->
		<view class="comments">
			<block v-for="(comment, index) in commentList" :key="index">
				<view class="comment">
					<view class="comment-info">
						<image :src="pathFilter(comment.mainComment.faceImg)" @tap="goToPersonPublic(comment.mainComment.fromUserId)"></image>
						<view class="name_text" style="display: flex;">
							<text selectable="true">{{ comment.mainComment.nickname }}</text>
							<image v-if="comment.mainComment.fromUserAuthType == 2" src="../../static/icon/auth_red.png" style="height: 18px;width: 18px;margin-left: 3px;"></image>
							<image v-if="comment.mainComment.fromUserAuthType == 1" src="../../static/icon/auth_yellow.png" style="height: 18px;width: 18px;margin-left: 3px;"></image>
						</view>
						<view class="time_text">{{ timeDeal(comment.mainComment.createDate) }}</view>
					</view>
					<text selectable="true" class="comment-content" @longpress="onLongpress(comment.mainComment.id)" @tap="goToCommentDetail(comment.mainComment)">{{ comment.mainComment.comment }}</text>
					<reComment :subComment="comment.subComment" @goToCommentDetail="goToCommentDetail(comment.mainComment)" style="width: 100%;height:100%;"></reComment>
					<view class="comment-menu">
						<view class="operationBar column_center">
							<nqCmt @click.native="goToCommentDetail(comment.mainComment)" :number="comment.mainComment.commentNum"></nqCmt>
							<nqLike
								style="margin-left: 11px;"
								@click.native="swLikeComment(comment, index)"
								:status="comment.mainComment.isLike"
								:number="comment.mainComment.likeNum"
							></nqLike>
						</view>
					</view>
				</view>
			</block>
		</view>
	</view>
</template>

<script>
import nqSwitch from '@/components/nq-switch.vue';
import reComment from './reComment.vue';
import { mapState, mapMutations } from 'vuex';
import nqLike from '@/components/nq-button/nq-likeButton.vue';
import nqCmt from '@/components/nq-button/nq-cmtButton.vue';

export default {
	props: {
		/** commentList数据结构
		 * commentList:[
		 * 		mainComment:
		 * 		subComment:{
		 * 			subCommentList,
		 * 			subCommentNum
		 * 		}
		 * ]
		 */
		articleId:'',
		commentList: '',
		commentNum: '',
		areaWidth: '100%',
		areaMargin: '',
		userInfo: '',
	},
	components: {
		nqSwitch,
		reComment,
		nqLike,
		nqCmt
	},
	computed: {
		...mapState(['lang'])
	},
	created() {
		uni.$on('updateComment', comment => {
			console.log('on update');
			// from comment-detail || comment-vote-detail
			for (var i = 0; i < this.commentList.length; i++) {
				if (this.commentList[i].mainComment.id == comment.mainComment.id) {
					this.commentList[i].mainComment = comment.mainComment;
					this.commentList[i].subComment = comment.subComment;
					console.log('update 第' + i + '条评论');
				}
			}
		});
	},

	data() {
		return {
			// commentList: this.commentList,
			order: 0 ,//评论排序方式 0：按时间查询, 1：按热度查询
			mainCommentId:'',
		};
	},
	onLoad(options) {
		//获取全局用户信息
		// var userInfo = this.getGlobalUserInfo();
		// if (!this.isNull(userInfo)) {
		// 	this.userInfo = this.getGlobalUserInfo();
		// 	console.log("userinfo++"+this.userInfo.id);
		// } else {
		// 	uni.redirectTo({
		// 		url: '../signin/signin'
		// 	});
		// 	return;
		// }
	},
	methods: {
		// Date: Aug 15, 2022
		// Author: Yifei
		// Description: 之前在删除收藏后的刷新会有点问题，就写一个刷新页面的方法吧
		refresh(){
			// 页面重载
		    const pages = getCurrentPages()
		    // 声明一个pages使用getCurrentPages方法
			const curPage = pages[pages.length - 1]
		    // 声明一个当前页面
		    curPage.onLoad(curPage.options) // 传入参数
		    curPage.onShow()
			curPage.onReady()
		    // 执行刷新
		},
		swLikeComment(comment, index) {
			// var userInfo = this.getGlobalUserInfo();
			// if(this.isNull(userInfo.email)){
			// 	uni.showToast({
			// 		icon:'error',
			// 		title:'未绑定邮箱'
			// 	})
			// }else{
			// 	console.log('click like');
			// 	this.$emit('like', comment, index);
			// }
			console.log('click like');
			this.$emit('like', comment, index);
		},
		onLongpress(e) {
			console.log('触发长按操作,复制或者是快速回复');
			console.log(e);
			var that=this;
			uni.showActionSheet({
				itemList: ['举报', '删除'],
				success: function (res) {
					console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
					that.mainCommentId=e;
					if(res.tapIndex==0){
						that.reportComment();
					}else{
						that.deleteComment();
					}
				},
				fail: function (res) {
					console.log(res.errMsg);
				}
			});
		},
		reportComment(){
			console.log('举报评论');
			var that = this;
			uni.request({
				method: 'POST',
				url: that.$serverUrl + '/Report/reportPublished',
				data: {
					userId: that.userInfo.id,
					targetId:that.mainCommentId,
					targetType: "COMMENT",
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					uni.showToast({
						title:'举报成功',
						icon:'success',
						duration:1000,
					});
				}
			});
		},
		deleteComment(){
			console.log('删除评论');
			var that = this;
			uni.request({
				method: 'POST',
				url: that.$serverUrl + '/social/fDeleteComment',
				data: {
					commentId:that.mainCommentId,
					userId: that.userInfo.id,
					targetId:that.articleId,
					targetType: "ARTICLE",
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res.data.msg);
					if(res.data.msg=="OK"){
						uni.showToast({
							title:'删除成功',
							icon:'success',
							duration:1000,
						});
						this.refresh();
					}else{
						uni.showModal({
							title: '提示',
							showCancel:false,
							content: '你不是文章发布者或评论发布者，无法删除',
							success: function (res) {
								if (res.confirm) {
									console.log('用户点击确定');
								}
							}
						});
					}
				},
			});
		},
		goToCommentDetail(mainComment) {
			// var userInfo = this.getGlobalUserInfo();
			// if(this.isNull(userInfo.email)){
			// 	uni.showToast({
			// 		icon:'error',
			// 		title:'未绑定邮箱'
			// 	})
			// }else{
			// 	this.$emit('goToCommentDetail', mainComment);
			// }
			this.$emit('goToCommentDetail', mainComment);
			// uni.navigateTo({
			// 	url: '/pages/comment-detail/comment-detail?data=' + JSON.stringify(mainComment)
			// });
		},
		change_comment_order(e) {
			this.$emit('onChange', {
				type: e.status
			});
		},
		goToPersonPublic(userId) {
			// router.goToPersonPublic(); // 全局方法
			uni.navigateTo({
				url: '/pagesSubA/personpublic/personpublic?userId=' + userId
			});
		}
	} //method
};
</script>

<style>
@import url('./oneComment.css');

.comments-menu {
	height: 28px;
	padding-top: 13px;
}

.comments-num {
	height: 17px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 17px;
	color: rgba(155, 155, 155, 1);
	padding-left: 4px;
	display: inline-block;
}

/* .comments-order {
		margin-top:3px;
		height: 22px;
		background: #ECECEC;
		border-radius: 75px;
		width: 82px;
		float: right;
		line-height: 28px;
		display: flex;
		vertical-align: middle;
		position: relative;
		justify-content: space-between;
		
	}
	.order-in-hot ,.order-in-time{
		color:#9B9B9B;
		font-size: 10px;
		line-height: 22px;
		display: inline-block;
		align-items: center;
		padding-right:10.5px;
		padding-left: 10.5px;
		z-index: 30;
		width:28px;
		text-align: center;
		transition: padding-left 200ms,
		padding-right 200ms,
		font-size  200ms,
		color 200ms;
		 transition-delay: 0ms;
	}
	.bg-of-order{
		height:28px;
		width:54px;
		background: #FFFFFF;
		border-radius: 75px;
		box-shadow: 0px 0px 10px rgba(0,0,0,0.16);
		z-index: 20;
		position: absolute;
		left:-13px;
		top:-3px;
		transition: left 500ms ease;
	}
	.chosen{
		font-size: 14px;
		color:#353535;
		width:28px;
		padding-left: 0;
		padding-right: 0;
		transition: padding-left 300ms,
		padding-right 300ms,
		font-size  300ms,
		color 300ms;
		 transition-delay: 100ms;
	} */
</style>
