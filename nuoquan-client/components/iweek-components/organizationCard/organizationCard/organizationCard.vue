<!-- 
	Date: July 23, 2022
	Author: Zhuoli
	Description: 社团组织卡片
 -->
<template>
	<view class="organizationCard" @click="choose(detail)">
		<image :src="pathFilter(detail.logoPath)" mode="aspectFill" class="orgImage"></image>
		<view class="organizationCard-text" >
			<view class="orgTitle">{{detail.name}}</view>
<!-- 			<view class="orgContent" v-html="$markdownParse.parse(detail.intro)"></view> -->
		</view>
	</view>
</template>

<script>
	import { mapState, mapMutations } from 'vuex';
	export default {
		props:{
			detail: '',
		},
		data() {
			return {
				// screenWidth:312,
				// showWidth:0,
			}
		},
		onLoad() {
			// var that = this;
			// uni.getSystemInfo({
			//     success : function(res) {
			// 		that.screenWidth = res.screenWidth;
			// 		console.log("screenWidth = " + that.screenWidth);
			//     }
			// });
			// that.showWidth=that.screenWidth-'330px';
			// console.log(that.showWidth+"====ssss");
		},
		methods: {
			choose(detail){
				uni.navigateTo({
					url: '../organizationDetail/organizationDetail?detail=' + encodeURIComponent(JSON.stringify(detail))
				})
			},
		}
	}
</script>

<style>
	.organizationCard{
		width: 90%;
		height: 80px;
		display: flex;
		margin:5px 5% 10px 5%;
		border-radius: 12px;
		box-shadow: 0 2px 5px #babacd;
	}
	.orgImage{
		width: 60px;
		height: 60px;
		border: 0.5px solid gray;
		border-radius: 50%;
		margin-left: 20px;
		margin-top: 10px;
	}
	
	.organizationCard-text{
		width:70%;
		margin-top: 30px;
		margin-left: 15px;
	}
	.orgTitle{
		font-size: 17px;
		font-weight:bold;
		line-height: 20px;
	}
	.orgContent{
		max-width: 90%;
		margin-top: 5px;
		font-size: 14px;
		line-height: 17px;
		display: -webkit-box; /*弹性伸缩盒子模型显示*/
		-webkit-box-orient: vertical; /*排列方式*/ 
		-webkit-line-clamp: 2; /*显示文本行数(这里控制多少行隐藏)*/
		overflow: hidden;
		text-overflow:ellipsis;
		word-wrap: break-word;
	}
</style>
