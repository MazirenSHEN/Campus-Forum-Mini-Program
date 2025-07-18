<!--
Description:  新建长文章页面循环的单个元素，兼容文本图片视频
Author: yaoyao
Date: 17 Aug, 2020
-->
<template>
	<view style="width: 100%;position: relative;" class="longAricleComponent">
		<view style="white-space: pre-wrap;word-wrap: break-word;" v-if="block.mode == 'text'" >
		{{block.content}}
		</view>
		<image v-if="block.mode == 'image'" :src="pathFilter(block.content)" @load="adjustSize" :style="{width:mediaSize.width + 'px',height:mediaSize.height + 'px'}">
		</image>
		<video v-if="block.mode == 'video'" :src="pathFilter(block.content)" @loadedmetadata="adjustSize"  style="width: 100%;">
			
		</video>
		<image v-if='editable' src="../../static/icon/delete.png" class="delete_button" @click="deleteComponent"></image>
	</view>
</template>

<script>
	export default{
		props:{
			item:"",
			editable:false,  //长文章详情或新建长文章，控制是否带删除按钮
			order:'',
		},
		data(){
			return{
				block:this.item,
				mediaSize:{
					height:0,
					width:0,
				},
			}
		},
		mounted() {
			
		},
		methods:{
			adjustSize(e){
					var that = this;
					var height = e.detail.height;
					var width = e.detail.width;
					var rate = height/width;//高宽比
					var max_width = 0;
					
					let info = uni.createSelectorQuery().in(this).select(".longAricleComponent");
					info.boundingClientRect(function(data) { //data - 各种参数
					//console.log(data);
						 max_width = data.width; // 获取元素宽度
					// 高度限制在宽度的[3/4,4/3]
					if(rate <3/4){
						that.mediaSize.height = max_width*3/4;
						that.mediaSize.width = max_width;
					}else{
						that.mediaSize.height = max_width*rate;
						that.mediaSize.width = max_width;
					}
					}).exec();
				
			},
			deleteComponent(){
				//console.log('delete  ' + this.order);
				this.$emit('deleteBlock',this.order);
			},
		},
	}
</script>

<style scoped>
	.delete_button{
		position: absolute;
		width: 20px;
		height: 20px;
		z-index: 20;
		right: 0;
		top:0;
	}
</style>
