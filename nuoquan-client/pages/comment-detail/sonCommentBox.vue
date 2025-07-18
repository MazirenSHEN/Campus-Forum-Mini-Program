<template>
	<view>
<!--开始 		 此BLOCK,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖      -->
		<view class="comment">
			<view class="comment-info">
				<image :src="pathFilter(commentDetail.faceImg)" @tap="goToPersonPublic(commentDetail.fromUserId)"></image>
				<view style="display: flex;">
					<text selectable="true" class="name_text">{{ commentDetail.nickname }}</text>
					<image v-if="commentDetail.fromUserAuthType == 2" src="../../static/icon/auth_red.png" style="width: 15px; height: 15px;margin-top: 2px;margin-left: 3px;"></image>
					<image v-if="commentDetail.fromUserAuthType == 1" src="../../static/icon/auth_yellow.png" style="width: 15px; height: 15px;margin-top: 2px;margin-left: 3px;"></image>
				</view>
				<view class="replyTo" style="display: flex;" v-if="commentDetail.toNickname != null && commentDetail.toNickname != undefined">
					<text>{{commentDetail.toNickname}}</text>
					<image v-if="commentDetail.toUserAuthType == 2" src="../../static/icon/auth_red.png" style="width: 15px; height: 15px;margin-left: 3px;margin-top: 2px;"></image>
					<image v-if="commentDetail.toUserAuthType == 1" src="../../static/icon/auth_yellow.png" style="width: 15px; height: 15px;margin-left: 3px;margin-top: 2px;"></image>
				</view>
				<view class="time_text">{{ timeDeal(commentDetail.createDate) }}</view>
			</view>
			<text selectable="true" class="comment-content" @tap="controlInputInSonCommentBox()">{{ commentDetail.comment }}</text>
			<view class="comment-menu">
				<view class="operationBar column_center">
						<!-- <nqCmt @click.native="controlInputInSonCommentBox()" :number="commentDetail.commentNum"></nqCmt> -->
						<nqLike style="position: absolute;right: 0;" @click.native="swLikeCommentDetail(commentDetail)" :status="commentDetail.isLike" :number="commentDetail.likeNum"></nqLike>
				</view>
			</view>
		</view>
<!--结束 		 此BLOCK,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖      -->
		
		
	</view>
</template>

<script>
import nqLike from '@/components/nq-button/nq-likeButton.vue';
import nqCmt from '@/components/nq-button/nq-cmtButton.vue';
	
	export default{
		props:{
		  reCommentDetail: '', //沿用之前组件名字了，可改，记得本组件内全替换
		},
		components: {
				nqLike,
				nqCmt,
		},
		data(){
			return{
				commentDetail: this.reCommentDetail,
			}
		},
		
		created() {
			console.log(this.reCommentDetail);
		},
		
		methods:{
			controlInputInSonCommentBox(){
				this.$emit('controlInputSignal',this.reCommentDetail);
				console.log("sonCB请求C-D");
			},
			
			swLikeCommentDetail(){
				this.$emit('swLikeComment',this.commentDetail);
			},
			
			goToPersonPublic(){
				this.$emit("goToPersonPublic", this.commentDetail.fromUserId);
			},
		},
		
		
	}
	
	
</script>

<style scoped>
@import url("../../components/nq-comment/oneComment.css");
/* 此文件,detail的评论,comment-detail,son-Comment卡片统一样式,所以统一引入,后期更改请同步------妖
 */	
.replyTo{
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 24px;
	height: 24px;
	color: #9B9B9B;
	display: inline-block;
	/* position: absolute;
	top: 0;
	left: 144px; */
	position: relative;
	left:48px;
	width: 96px;
}
.replyTo::after{
	content: "回复  @";
	font-size: 12px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 24px;
	height: 24px;
	color: #9B9B9B;
	display: inline-block;
	position: absolute;
	left:-40px;
	width:40px;
	}
</style>
