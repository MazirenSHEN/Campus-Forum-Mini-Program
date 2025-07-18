<template>
	<view v-if="show" class="scheduleContainer">
		<view class="scheduleBox">
			<view class="content">
				<view class="concrete_topic title" style="margin-bottom: 20px;font-size: 17px;line-height: 25px;font-weight:500;margin-left: 10px;">
					<text>{{event.eventTitle}}</text>
				</view>
				<view class="date" style="margin-bottom: 15px; display: flex;margin-left: 10px;">
					<view style="font-weight: bold;font-size: 15px;margin-right: 10px;">
						<text>Date:</text>
					</view>
					<view style="font-size: 13px;">
						<text>{{event.strEventDate}}</text>
					</view>
				</view>
				
				<view style="margin-bottom: 15px;display: flex;margin-left: 10px;">
					<view style="font-weight: bold; font-size: 15px;margin-right: 10px;">
						<text>Time:</text>
					</view>
					<view style="font-size: 13px;">
						<text>{{event.eventTime}}</text>
					</view>
				</view>
				
				<view class="venue" style="margin-bottom: 15px; display: flex;margin-left: 10px;">
					<view style="font-weight: bold; font-size: 15px;margin-right: 10px;">
						<text>Venue:</text>
					</view>
					<view style="font-size: 13px;">
						<!-- <text>Grass Pitch close to Sir Colin Campbell Building (Sports Center)</text> -->
						<text>{{event.eventVenue}}</text>
					</view>
				</view>
				
				<view class="target" style="margin-bottom: 15px; display: flex;margin-left: 10px;">
					<view style="font-weight: bold; font-size: 15px;margin-right: 10px;">
						<text>Target Student:</text>
					</view>
					<view style="font-size: 13px;">
						<text>{{event.strFaculty}}, {{event.strDegree}}</text>
					</view>
				</view>
				
				<view v-if="event.description" class="description" style="margin-bottom: 15px; display: flex;margin-left: 10px;">
					<view style="font-weight: bold; font-size: 15px;margin-right: 10px;">
						<text>Description:</text>
					</view>
					<view style="font-size: 13px;">
						<text>{{event.description}}</text>
					</view>
				</view>
			</view>
			<view class="killIcon super_center" @click="killAd">
				<image src="../../../static/icon/delete.png" mode="aspectFit"></image>
			</view>
		</view>
		<view style="height: 100%;width: 100%;background-color: #000000;opacity: 0.5;"></view>
	</view>
</template>

<script>
	import { mapState, mapMutations } from 'vuex';
	export default {
		props:{
			event:'',
			show:'',
		},
		data() {
			return {
				// event:''
				// show: false,
			}
		},
		computed: {
			...mapState(['lang'])
		},
		methods: {
			killAd(){
				this.$emit('close',false);
			}
		}
	}
</script>

<style>
	.scheduleContainer {
		height: 100%;
		width: 100%;
		position: fixed;
		/* 第五层 */
		z-index: 50; 
		display: flex;
	}
	.scheduleBox {
		max-height: 700upx;
		max-width: 600upx;
		position: fixed;
		/* 第六层 */
		z-index: 60;
		background-color: white;
		opacity: 1;
		border-radius: 15px;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		margin-top: auto;
		margin-bottom: auto;
		margin-left: auto;
		margin-right: auto;
	}
	.killIcon{
		position: absolute;
		bottom: -70upx;
		right: 275upx;
		width: 50upx;
		height: 50upx;
		border-radius: 50%;
		background-color: #e6e6e6;
	}
	
	.killIcon image{
		width: 20px;
		height: 20px;
	}
	
	.content {
		margin: 40px 10px 0 10px;
	}
	
	.detail {
		margin-top: -18px;
		margin-left: 40px;
	}
</style>
