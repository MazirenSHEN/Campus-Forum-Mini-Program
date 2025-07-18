<template>
	<view
		style="
			height:100%;
			background:rgba(236,236,236,1);
			opacity:1;
			border-radius:8px;
			margin-left: 8.53%;
			padding: 4px 12px 4px 12px;"
		v-if="subComment.subCommentNum > 0"
	>
		<block v-for="(subComment, index) in subComment.subCommentList" :key="index">
			<!-- 仅显示前两条 -->
			<view class="reComment" v-if="index<2"> 
				<view :style="{ 'border-top': index == 0 ? '' : '1px solid #DCDCDC' }" class="reCommentContent" @tap="goToCommentDetail()">
					<text selectable="true" class="contentarea">{{ subComment.nickname }}</text>
					<text selectable="true" style="color:#000000;font-size: 12px;display: inline-block;margin: 0 5px 0 0;">{{lang.reply}}</text>
					<text selectable="true" class="contentarea">@{{ subComment.toNickname }}</text>
					{{ subComment.comment }}
				</view>

				<!-- <view class="bottombar">
					<view style="width:70%;display:inline-block;">
						<image :src="pathFilter(subComment.faceImg)" class="touxiang" @tap="goToPersonPublic"></image>
						<text class="time">{{ subComment.timeAgo }}</text>
					</view>
					<view class="icons" @tap="swLikeSubComment()">
						<!- 点赞按钮 ->
						<image v-if='!subComment.isLike' class="icon" src="../../../static/icon/like.png"></image>
						<image v-if='subComment.isLike' class="icon" src="../../../static/icon/liked.png"></image>
						<text class="icom" :class="{'liked':subComment.isLike}">{{ subComment.likeNum }}</text>
					</view>
				</view> -->
				<!-- <view style="border-top:1px solid #DCDCDC"></view> -->
			</view>
		</block>
		<view style="width: 100%;
			position: relative;
			margin-top: 8px;
			margin-bottom: 8px;
			height: 20px;
			line-height: 20px;" v-if="subComment.subCommentNum >= 3">	
			<view class="showMoreSubComment" @tap="goToCommentDetail()">{{lang.showMoreComments}}</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
	
export default {
	props: {
		subComment: ''
	},
	data() {
		return {
			userInfo: this.getGlobalUserInfo()
		};
	},
	mounted() {
		// console.log(this.subComment)
		// this.getSubComments(1);
		// uni.$on('flashSubComment', (underCommentId) => {
		// 	this.getSubComments(1);
		// })
	},
	computed: {
		...mapState(['lang'])
	},
	methods: {
		goToCommentDetail() {
			this.$emit("goToCommentDetail");
		}
	}
};
</script>

<style>
.contentarea {
	display: inline-block;
	font-size: 12px;
	line-height: 17px;
	margin-right: 5px;
	color: #888888;
}

.reCommentContent {
	font-size: 12px;
	color: #000000;
	line-height: 17px;
	max-height: 28px;
	padding: 8px 0 8px 0;
	
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	text-overflow: ellipsis;
	white-space: pre-wrap;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2;
	overflow: hidden;
}

.bottombar {
	position: relative;
	border-radius: 20px;
	height: 25px;
	margin-top: 10px;
	display: flex;
	justify-content: space-between;
	padding-bottom: 5px;
}

.touxiang {
	width: 20px;
	height: 20px;
	border-radius: 20px;
	display: inline-block;
	vertical-align: middle;
	margin-right: 5px;
}

.time,
.name {
	font-size: 10px;
	margin-right: 10px;
	color: #888888;
	max-width: 85px;
	text-overflow: ellipsis;
}

.icons {
	justify-content: flex-end;
	display: inline-flex;
	align-items: flex-end;
	width: 30%;
	font-size: 10px;
}

.icon {
	width: 12px;
	height: 12px;
	font-size: 2px;
	padding-right: 4px;
}

.icom {
	align-items: flex-end;
	padding-right: 5px;
}

.liked {
	color: #fdd041;
}

.sub-comment-content-in-reComment {
	padding-top: 12px;
	padding-bottom: 8px;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 18px;
	color: rgba(53, 53, 53, 1);
}

.showMoreSubComment {
	position: absolute;
	right: 0;
	padding: 2px 10px;
	/* margin-top: 8px;
	margin-bottom: 8px; */
	min-width: 97px;
	height: 20px;
	line-height: 20px;
	background: rgba(209, 209, 209, 1);
	box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
	border-radius: 36px;

	text-align: center;
	font-size: 12px;
	font-weight: 500;
	color: rgba(53, 53, 53, 1);
}
</style>
