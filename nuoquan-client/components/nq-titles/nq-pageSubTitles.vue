<!-- 
	Author: Guetta
	Last update: 2020年8月20日14点50分
	Intro: 常用页面内副标题组件，附带右侧“查看全部”按钮，功能暂没写
	传值: pageTitle, withShowAll
 -->
<template>
	<view class="pagegSubTitleBox" :style="{width: screenWidth + 'px'}">
		<text class="leftText">{{pageTitle}}</text>
		<view v-if="withShowAll" class="rightButton" @click="pageJump">
			{{lang.viewAllHot}}
		</view>
	</view>
</template>

<script>
	import msgcount from '../nq-msgcount/nq-msgcount.vue';
	import { mapState, mapMutations } from 'vuex';

	export default {
		components: {
			msgcount	
		},
		computed: {
			...mapState(['lang'])
		},
		props:{
			pageTitle: '',
			withShowAll: '',
			url: '',
		},
		
		data() {
			return {
				screenWidth: '',
			// 	title: '',
			// 	isShow: false,
			 }
		},
		created() {
			uni.getSystemInfo({
				success(res) {
					this.screenWidth = res.screenWidth;
				}
			})
		},
		// watch:{
		// 	pageTitle:function(val, oldValue){
		// 		this.title = val;
		// 		console.log(val);
		// 	},
		// 	withShowAll:function(val, oldValue){
		// 		this.isShow = val;
		// 		console.log(val);
		// 	},
		// },
		methods: {
			pageJump(){
				uni.navigateTo({
					url: this.url
				})
			},
		}

	}
	
</script>

<style>
	.pagegSubTitleBox{
		display: flex;
		justify-content: space-between;
		padding: 14px 16px 12px 16px;
	}
	
	.leftText{
		height:16px;
		font-size:18px;
		font-family:Source Han Sans CN;
		font-weight:550;
		line-height:16px;
		color:rgba(74,74,74,1);
		opacity:1;
	}
	
	.rightButton{
		height:16px;
		font-size:13px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color:rgba(154,154,154,1);
		opacity:0.64;
	}
	
</style>
