<!-- 
	Author: Yifei
	Date: July 21, 2022
	Description: 新生I-week页面
 -->
<template>
	<view class="iweek">
		<schedule-detail :event="detail" :show = "show" @close = "closeEvent"></schedule-detail>
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :style="{height: this.getnavbarHeight() + 'px'}"
		:title="lang.iweek" :showLeftIcon="false"
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }" style="width: 100%;"></view>
		<!-- 占位 -->
		<view style="height: 8px; background-color: #FFFFFF;"></view>
		<view class="topHalf">
		<!-- 轮播图 如果不想让他动的话就只放一张图 -->
		<swiper class="top-swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000" circular="true">
			<swiper-item v-for="(item,index) in swipers" :key="index">
				<image :src="item.url" mode="aspectFit" @click="jumpToWeb(item.articleUrl)"></image>
			</swiper-item>
		</swiper>
		
		<!-- 四项功能的图标及文字 -->
		<functionList class="functionList"></functionList>
		</view>
		
		<view class="downHalf">
			<!-- <view class="second_line" @click="toggleIsEditFaculty"></view> -->
			<schedule @event="showDetail" class="schedule"></schedule>
		</view>
		
		<!-- 底部选项卡 -->
		<tab-bar :current="1"></tab-bar>
	</view>
</template>

<script>
	import scheduleDetail from '@/components/iweek-components/schedule-detail/schedule-detail.vue';
	import schedule from '@/components/iweek-components/schedule/schedule.vue';
	import functionList from '@/components/iweek-components/function-list/function-list.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import tabBar from '@/components/nq-tabbar/nq-tabbar.vue';
	import { mapState, mapMutations } from 'vuex';
	export default {
		components:{
			uniNavBar,
			tabBar,
			functionList,
			schedule,
			scheduleDetail
		},
		data(){
			return{
				event: {},
				title: 'Hello',
				swipers:[],
				detail: '',
				show: false,
			}
		},
		computed:{
			...mapState(['lang'])
		},
		onLoad(){
			this.swipers = [
			{
				url:'https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/hq3.jpeg',
				articleUrl:'https://mp.weixin.qq.com/s/98yl6ahLE5Z-GmxWe_LTzw',
			},
			{
				url:'https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/hq.jpg',
				articleUrl:'no',
			},
			{
				url:'https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/hq2.png',
				articleUrl:'https://mp.weixin.qq.com/s/XPt4FWfevGnx6E6FQ51OQA',
			}
			]
			
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../signin/signin'
				});
				return;
			} else {
				this.userInfo = userInfo; // 刷去默认值(若有)
			}
		},
		methods:{
			getEvent(data){
				this.event = data
				console.log(this.event)
			},
			showDetail(e){
				this.detail = e;
				this.show = true;
				// console.log(this.detail);
			},
			closeEvent(e){
				this.show = e;
			},
			jumpToWeb(index){
				if (index == 'no'){
					console.log('no jump link');
					return;
				}else{
					var encodeData = encodeURIComponent(index);
					uni.navigateTo({
						url:'../adWebPage/adWebPage?url=' + encodeData,
						fail() {
							console.log(res);
						}
					})
				}
			},
		}
	}
</script>

<style>
	.top-swiper {
		margin-left: 5%;
		margin-right: 5%; 
		/* left: 5%; */
		/* width: 90%; */
		height: 400rpx;
		background-color: #F7F7F7;
	}
	.top-swiper image{
		width: 100%;
		height: 100%;
	}
	
	.topHalf {
		background-color: #FFFFFF;
		/* margin-top: 8px; */
	}
	
	.functionList {
		margin-top: 500upx;
		width: 100%;
		height: auto;
	}
	
	.downHalf {
		margin-top: 2%;
		background-color: #FFFFFF;
	}
	
	.text_topic {
		padding-top: 20upx;
		padding-left: 4%;
		font-size: 16px;
		font-weight: bold;
	}
</style>
