<!--
Description:  学术页面 下部卡片区域单个卡片，有academicCardFlow调用
Author: yaoyao
Date: 17 Aug, 2020
-->
<template>
	<view class="academicCardFlow">
		<view class="cover_media">
			<image v-if="mediaInfo.type == 'image'" :src="mediaInfo.url" @load="singleImgeFit" :style="{width:mediaSize.width + 'px',height:mediaSize.height + 'px'}"></image>
			<video v-if="mediaInfo.type == 'video'" :src="mediaInfo.url"></video>
		</view>
		<view class="title text_ellipsis">
			标题标题标题标题标题标题标题标题标题题标题标题
		</view>
		<view class="content text_ellipsis" v-if="mediaInfo.type == 'text'">
			内容内容内容两岸三地课件撒货到付款就和空间的是否还是快乐的事裂缝宽度十六分量可达十六客服的是否能士大夫你是离开负能量卡尼萨洛克生命中覅u你申请
		</view>
		<view class="info_bar">
			<image class="touxiang" src="../../static/BG/detailBG.png" mode="aspectFill"></image>
			<view class="nickname text_ellipsis">昵称最多八个汉字</view>
			<view class="like">33</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			longArticle: {},
		},
		data() {
			return {
				mediaInfo: {
					type: "image",
					url:"https://www.checkchack.cn:8443/nottinghome/test10207/article/200813FSTMMP06FW/0.gif",
				},
				mediaSize:{
					width:'0',
					height:'0',
				}
			}
		},
		methods: {
			singleImgeFit(e) {
				var that = this;
				var height = e.detail.height;
				var width = e.detail.width;
				let info = uni.createSelectorQuery().in(this).select(".cover_media");
				var max_width = 0;
				info.boundingClientRect(function(data) { //data - 各种参数
				console.log(data);
					 max_width = data.width; // 获取元素宽度
					// 高度限制在宽度的[3/4,4/3]
					 if(height > (width*4/3)){
					 	that.mediaSize.height = max_width * 4/3;
					 	that.mediaSize.width = max_width;
					 }else if(height < width*3/4){
					 	that.mediaSize.height = max_width * 3/4;
					 	that.mediaSize.width = max_width;
					 }else{
						 that.mediaSize.height = max_width*height/width;
						 that.mediaSize.width = max_width;
					 };
					 console.log(this.mediaSize);
				}).exec();
			},
		}
	}
</script>

<style>
	.academicCardFlow {
		width: 100%;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
		border-radius: 5px;
		overflow: hidden;
		margin-bottom: 10px;
	}

	.cover_media {
		width: 100%;
	}

	.cover_media image,
	.cover_media video {
		width: 100%;
	}

	.academicCardFlow .title {
		min-height: 24px;
		max-height: 48px;
		margin: 15px 8px 4px;
		font-size: 16px;
		line-height: 24px;
		font-weight: bold;
		-webkit-line-clamp:2;
	}

	.academicCardFlow .content {
		font-size: 12px;
		color: #4A4A4A;
		margin: 0 8px 12px;
		line-height: 16px;
		max-height: 96px;
		-webkit-line-clamp:6;
	}
	.text_ellipsis{
		display: -webkit-box; 
		-webkit-box-orient:vertical;
		overflow:hidden; 
		text-overflow: ellipsis;
		word-wrap: break-word;
	}
	
	.info_bar{
		height: 32px;
		position: relative;
		padding-bottom: 15px;
	}
	.touxiang{
		margin-left: 10px;
		width: 32px;
		height: 32px;
		vertical-align: bottom;
		border-radius: 16px;
		display: inline-block;

	}
	.nickname{
		height: 10px;
		font-size: 10px;
		line-height: 10px;
		padding:11px 6px;
		display: inline-block;
		vertical-align: bottom;
		max-width: calc(100% - 92px);
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.like{
		font-size: 14px;
		height: 14px;
		line-height: 14px;
		width:16px;
		text-align: right;
		color: #FF684A;
		position: absolute;
		right:8px;
		top:9px;
	}
	.like::after{
		content: "";
		width:18px;
		height: 18px;
		background: url(../../static/icon/heart-full-ff7070.png);
		background-repeat: no-repeat;
		background-size: 16px 16px;
		position: absolute;
		left: -20px;
		top:-1px;
	}
</style>
