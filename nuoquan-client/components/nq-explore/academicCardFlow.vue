<!--
Description:  学术页面下部卡片区域，负责数据源和两竖列分配
Author: yaoyao
Date: 17 Aug, 2020
-->
<template>
	<view class="academicCardFlow">
		<view class="column">
			<academiccardbrief v-for="(longArticle,index1) in leftColumn" :key="index1" :longArticle="longArticle"></academiccardbrief>
		</view>
		<view class="column">
			<academiccardbrief v-for="(longArticle,index2) in rightColumn" :key="index2" :longArticle="longArticle"></academiccardbrief>
		</view>
	</view>
	
</template>

<script>
	import academiccardbrief from '../nq-card/academicCardBrief.vue'
	
	export default{
		components:{
			academiccardbrief,
		},
		data(){
			return {
				leftColumn:[1,2,3,4,5,6,7],
				rightColumn:[1,2,3,4,5,6,7],
			};
		},
		methods:{
			
		},
	}
</script>

<style>
	.academicCardFlow{
		display: flex;
		width:calc(100% - 24px);
		margin: auto;
		justify-content: space-between;
	}
	.column{
		width:calc((100% - 10px)/2);
	}
</style>
