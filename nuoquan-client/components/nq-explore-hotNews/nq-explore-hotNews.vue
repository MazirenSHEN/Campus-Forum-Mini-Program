<!-- 
	Description: 头条展示卡片
	Author: Guetta & Jerrio
	Date: 2020年8月18日14点49分
	Version: v2.0.0
	状态: 切图完成
	TODO: 待接口开发完毕，填入功能和数据
	备注: 探索-热门 头部头条消息带评论组件
 -->
<template>
	<view class="hotNewsBox" @click="goToLongArticleDetail()">
		<image class="bgPic" :src="pathFilter(headlines.coverImage)" mode="aspectFill"></image>
		<view class="mengban"></view>
		<view class="leftBox">
			<view class="title">
				<view class="titleIcon">
					热
				</view>
				<text>{{headlines.title}}</text>
			</view>
			<view class="writerInfo">
				<view class="profilePic super_center">
					<image :src="pathFilter(headlines.faceImg)" mode="aspectFit"></image>
				</view>
				<view class="writerId">
					<text>{{headlines.nickname}}</text>
					<view class="sendTime">
						{{timeDeal(headlines.createDate)}}
					</view>
				</view>
			</view>
		</view>
		<swiper class="hotComments" 
			:vertical="true" 
			:indicator-dots="false" 
			:autoplay="true" 
			:interval="3000" 
			:duration="1000" 
			:circular="true"
		>
			<swiper-item v-for="(comment, index) in commentList" :key="index">
				<view class="commentCard">
					<view class="hotTag super_center">
						<image src="../../static/functionalIcon/biaoqian.png" mode="aspectFit"></image>
						<text>热评</text>
					</view>
					<view class="textAndId">
						<text class="commentText">{{comment.comment}}</text>
						<view class="comment-idLine column_center">
							<image :src="pathFilter(comment.faceImg)" mode="aspectFit"></image>
							<text class="idText">{{comment.nickname}}</text>
						</view>
					</view>
					<view class="participate">
						参与讨论
					</view>
				</view>
			</swiper-item>
		</swiper>
	</view>
	
</template>

<script>
	export default{
		props: {
			headlines: {},
			commentList: {}
		},
		data(){
			return {

			};
		},
		methods:{
			goToLongArticleDetail() {
				var isJump = this.headlines.isJump;
				if (isJump == 0) { //0，跳转详情
					uni.navigateTo({
						url: '../../pagesSubA/longArticleDetail/longArticleDetail?data=' + this.headlines.id,
					});
				} else if(isJump == 1){
					var encodeData = encodeURIComponent(this.headlines.link);
					uni.navigateTo({
						url: '/pages/adWebPage/adWebPage?url=' + encodeData,
					})
				}
			},
		},
	}
</script>

<style>
	.hotNewsBox{
		width: calc(750upx - 68upx);
		height: 340upx;
		margin-left: 34upx;
		position: relative;
		box-shadow: 0px 3px 5px rgba(0,0,0,0.16);
		border-radius: 8px;
	}
	
	.bgPic{
		position: absolute;
		z-index: 10;
		width: 100%;
		height: 100%;
		border-radius: 8px;
	}
	
	.mengban{
		position: absolute;
		z-index: 11;
		background-color: rgba(0,0,0,0.4);
		width: 100%;
		height: 100%;
		border-radius: 8px;
	}
	
	.leftBox{
		position: absolute;
		top: 14px;
		left: 8px;
		max-width: 60%;
		z-index: 20;
	}
	
	.title{
		display: flex;
	}
	
	.title text{
		margin-left: 8px;
		font-size:18px;
		font-family:Source Han Sans CN;
		font-weight:500;
		line-height:24px;
		color:#fafafa;
		opacity:1;
	}
	
	.titleIcon{
		margin-top: 2px;
		height:20px;
		padding: 0px 4px;
		background:rgba(252,192,65,1);
		opacity:1;
		border-radius:2px;
		/* 字体部分 */
		font-size:10px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:18px;
		color:rgba(252,252,252,1);
	}
	
	.writerInfo{
		display: flex;
		margin-top: 10px;
	}
	
	.profilePic{
		width: 32px;
		height: 32px;
	}
	
	.profilePic image{
		width: 27px;
		height: 27px;
		border-radius: 50%;
	}
	
	.writerId{
		display: flex;
		flex-direction: column;
	}
	
	.writerId text{
		height:15px;
		font-size:10px;
		font-family:Source Han Sans CN;
		font-weight:500;
		line-height:16px;
		color:#fafafa;
		opacity:1;
	}
	
	.sendTime{
		height:15px;
		font-size:10px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color:#fafafa;
		opacity:1;
	}
	
	.hotComments{
		position: absolute;
		z-index: 20;
		right: 8px;
		width:35%;
		height:100%;
	}
	
	.commentCard{
		position: relative;
		margin-top: 9px;
		width: 100%;
		height: calc(100% - 18px);
		background:rgba(240,240,240,0.69);
		border-radius:3px;
	}
	
	.textAndId{
		position: absolute;
		top: 25px;
		left:8px;
		width:calc(100% - 16px);
	}
	
	.commentText{
		font-size:8px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color:rgba(74,74,74,1);
		opacity:1;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
	
	.comment-idLine{
		display: flex;
		margin-top: 7px;
		width: 100%;
	}
	
	.comment-idLine image{
		width: 16px;
		height: 16px;
		border-radius: 50%;
	}
	
	.comment-idLine .idText{
		margin-left: 7px;
		font-size:8px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color:rgba(74,74,74,1);
		opacity:1;
		text-overflow: ellipsis;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
	
	.hotTag{
		position: absolute;
		top: 0;
		right: 1px;
		height: 20px;
		width: 20px;
	}
	
	.hotTag image{
		position: absolute;
		width: 20px;
		height: 20px;
	}
	
	.hotTag text{
		position: absolute;
		height:10px;
		font-size:7px;
		font-family:Source Han Sans CN;
		font-weight:bold;
		line-height:10px;
		color:rgba(252,252,252,1);
		opacity:1;
	}
	
	.participate{
		position: absolute;
		bottom: 12px;
		right: 8px;
		padding: 0px 4px;
		background:rgba(252,192,65,1);
		opacity:1;
		border-radius:2px;
		font-size:7px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:18px;
		color:rgba(252,252,252,1);
		opacity:1;
	}
	
</style>
