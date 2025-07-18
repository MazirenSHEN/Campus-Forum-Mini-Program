<!-- 
	Date: July 20, 2022
	Author: Yifei
	Description: I-week新生界面的四项功能，分别为：社团组织，我的课程，校园地图和吃喝玩乐
 -->
<template>
	<view class="function-list">
		<view class="functions" v-for="(item, index) in functionList" :key="index" @tap="onClick(functionList[index])">
			<view>
				<image :src="item.icon" class="function-image" style="background-position: center;"></image>
				<view v-if="lang.functionList[index] == 'Organizations'" style="font-size: 12px;">
					{{lang.functionList[index]}}
				</view>
				<view v-else class="text">
					{{lang.functionList[index]}}
				</view>
				
			</view>
		</view>
	</view>
</template>

<script>
	import { mapState, mapMutations } from 'vuex';
	export default {
		data(){
			return {
				functionList:'',
				// 小程序跳转锁，跳转完成后解开
				jumpLock : false,
				encode:'',
			};
		},
		computed: {
			...mapState(['myMsgCount','lang'])
		},
		created() {
			this.functionList = [
				{
					type: 0,
					icon: '/static/icon/iweek/eat.png',
					name: '学生组织',
					url: '/pages/Iweek/organizationList/organizationList?type=1',
				},
				{
					type: 0,
					icon: '/static/icon/iweek/society.png',
					name: '学生社团',
					url: '/pages/Iweek/organizationList/organizationList?type=2'
				},
				{
					type: 1,
					icon: '/static/icon/iweek/ucourse.jpeg',
					name: '我的课程',
					appid: 'wxf91a3d7a60c2de7e',
					url: 'pages/index/index'
				},
				{
					type: 1,
					icon: '/static/icon/iweek/map.png',
					name: '校园地图',
					appid: 'wx785bfd9dbf7823ea',
					url: 'pages/index/index'
				}
			]
		},
		methods: {
			onClick(e){
				// debugger
				console.log(e.name)
				// 若点击社团组织或吃喝玩乐，在小程序内跳转
				// 若点击map或course，根据appid和path跳转至对应小程序
				if (e.type == 1) {
					if (this.jumpLock == true){
						return;
					}
					// 上锁
					this.jumpLock = true;
					uni.navigateToMiniProgram({
						appId: e.appid,
						path: e.url,
						envVersion: "release",
						
						success:res => {
							console.log("打开成功", res);
							this.jumpLock = false;
						},
						fail: err => {
							console.log(err);
							this.jumpLock = false;
						}
					})
				} else if(e.type == 0){
					uni.navigateTo({
						url:e.url,
					});
				}
			},
			jumpToWeb(){
				var url = 'https://mp.weixin.qq.com/s/GZRAB4aCFlKL9Q8yK74B9w';
				var encodeData = encodeURIComponent(url);
				uni.navigateTo({
					url:'../../pages/adWebPage/adWebPage?url=' + encodeData,
				})	
			}
		}
	}
</script>

<style>
	.function-list {
		display: flex; 
		flex-direction: row; 
		flex-wrap: wrap; 
		margin: 0 5%;
	}
	.functions{
		width: 25%;
		margin-top: 16upx;
	}
	.function-image{
		display: bloc;
		width: 50px; 
		height: 50px;
		margin: 10% 15% 10% 15%;
		border: 1px solid #b1b1b1;
		border-radius: 50%;
	}
	.text {
		display: inline-flex;
		text-align: center;
		font-size: 14px;
		margin-bottom: 15%;
		margin-left: 15%;
		margin-right: 15%;
	}
</style>
