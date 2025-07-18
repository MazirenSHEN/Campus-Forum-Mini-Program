<!-- 
	I-week日程预览卡片
	Author: Yifei
	Date: July 25, 2022
 -->
<template>
	<view class="scheduleCard"ref="scheduleCard" @click="showDetail(scheduleCard)">
		<!--<view class="placeholder">
			<view class="topic">
				<text>{{thisSchedule.eventTitle}}</text>
			</view>
			<view class="time">
				<text>{{thisSchedule.eventTime}}</text>
			</view>
			<view class="location">
				<text>{{thisSchedule.eventVenue}}</text>
			</view>
		</view>-->
		
		<view v-if="tag == 1" class="placeholder1">
			<view class="topic">
				<text>{{thisSchedule.eventTitle}}</text>
			</view>
			<view class="time">
				<text>{{thisSchedule.eventTime}}</text>
			</view>
			<view class="location">
				<text>{{thisSchedule.eventVenue}}</text>
			</view>
		</view>
				
		<view v-else-if="tag == 2" class="placeholder2">
			<view class="topic">
				<text>{{thisSchedule.eventTitle}}</text>
			</view>
			<view class="time">
				<text>{{thisSchedule.eventTime}}</text>
			</view>
			<view class="location">
				<text>{{thisSchedule.eventVenue}}</text>
			</view>
		</view>
				
		<view v-else-if="tag == 3" class="placeholder3">
			<view class="topic">
				<text>{{thisSchedule.eventTitle}}</text>
			</view>
			<view class="time">
				<text>{{thisSchedule.eventTime}}</text>
			</view>
			<view class="location">
				<text>{{thisSchedule.eventVenue}}</text>
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		name: 'scheduleCard',
		props: {
			scheduleCard: {},
			tag:'',
		},
		data() {
			return {
				thisSchedule: this.scheduleCard,
				event: {},
			}
		},
		onLoad(){
			console.log(thisSchedule);
		},
		methods: {
			showDetail(e) {
				this.$emit("event",e);
				// console.log('1111111');
			}
		}
	}
</script>

<style>
	.scheduleCard {
		width: 90%;
		height: 100upx;
		border: 1px solid #b1b1b1;
		border-radius: 10px;
		margin-left: 4%;
		background-color: rgba(255,255,255,1);
		opacity: 1;
		margin-top: 20upx;
		margin-bottom: 20upx;
		box-shadow: 1px 2px #CCCCCC;
	}
	
	/*.placeholder {
		position: absolute;
		background-size: 10px;
		background-color: #f9c466;
		width: 2.6%;
		height: 100upx;
		border-radius:8px 0 0 8px;
		margin-bottom: 0;
	}*/
	
	.placeholder1 {
		position: absolute;
		background-size: 10px;
		background-color: #f9c466;
		width: 2.6%;
		height: 100upx;
		border-radius:8px 0 0 8px;
		margin-bottom: 0;
	}
		
	.placeholder2 {
		position: absolute;
		background-size: 10px;
		background-color: #ff9257;
		width: 2.6%;
		height: 100upx;
		border-radius:8px 0 0 8px;
		margin-bottom: 0;	
	}
		
	.placeholder3 {
		position: absolute;
		background-size: 10px;
		background-color: #fb7676;
		width: 2.6%;
		height: 100upx;
		border-radius:8px 0 0 8px;
		margin-bottom: 0;	
	}
	
	.topic {
		font-size: 12px;
		font-weight: bold;
		width: 500upx;
		margin-left: 20px;
		margin-top: 10upx;
		
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
	.time {
		font-size: 11px;
		width: 200upx;
		margin-left: 20px;
		margin-top: 15upx;
		
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
	.location {
		font-size: 11px;
		width: 400upx;
		margin-left: 130px;
		margin-top: -35upx;
		
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
</style>
