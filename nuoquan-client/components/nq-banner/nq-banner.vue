<template>
	<view v-if="showAd" class="banner-container">
		<view class="posterBox">
			<image class="adPoster" :src="adImgSrc" mode="aspectFit" @click="jumpToWeb"></image>
			<view class="killIcon super_center" @click="killAd">
				<image src="../../static/icon/delete.png" mode="aspectFit"></image>
			</view>
		</view>
		<view style="height: 100%;width: 100%;background-color: #000000;opacity: 0.5;"></view>
	</view>
</template>

<script>
	export default {
		name: 'nqBanner',
		props:{
			origin: '', // 调用源
		},
		data() {
			return {	
				showAd: false,
				// bannerInterval: 10,		// 测试数据，每分钟都能看到广告啦！
				bannerInterval: 432000000,  //毫秒，时间戳差值  目前值 12h 
				fromSrc: this.origin, // 本地化
				adImgSrc: '',
			};
		},
		created() {
			console.log("this.origin " + this.origin);
			console.log("this.fromSrc " + this.fromSrc);
			if(this.fromSrc == "index"){
				console.log("首页banner");
				this.adImgSrc = "https://nuoquan-1308006370.cos.ap-shanghai.myqcloud.com/nqprod/ad/ad_haochi.jpg"
			}
			
			var userInfo = this.getGlobalUserInfo();
			var currentTime = (new Date()).getTime();
			var that = this;
			uni.getStorage({
				key:userInfo.id + ':bannerVisitTime',
				success:function(res){
					console.log('成功获取上次banner展示时间，为' + that.timeDeal(res.data));
					if (currentTime - res.data > that.bannerInterval){
						console.log('超出设定展示间隔，展示banner并更新记录');
						that.showBanner_update(userInfo.id,currentTime);
					}else{
						console.log('banner不需展示');
					}
				},
				fail() {
					console.log('无记录，写入记录并初次展示banner');
					that.showBanner_update(userInfo.id,currentTime);
				}
			})
		},
		methods: {
			//跳转广告页
			jumpToWeb(){
				if(this.fromSrc == "index"){
					var url = 'https://mp.weixin.qq.com/s/6xnMR_qFJyeEtrVSMnTccg'
					var encodeData = encodeURIComponent(url);
					uni.navigateTo({
						url:'../../pages/adWebPage/adWebPage?url=' + encodeData,
					})	
				}
			},
			//组件内伪关闭广告
			killAd() {
				this.showAd = !this.showAd;
			},
			showBanner_update(id,currentTime){
				// 这里设置成false，则广告永不出现
				this.showAd = true;
				uni.setStorage({
				    key: id + ':bannerVisitTime',
				    data: currentTime,
				    success: function () {
				        console.log('更新banner成功');
				    },
					fail:function(){
						console.log( '更新banner失败');
					}
				});
			},
		},
	}
</script>

<style>
	.banner-container{
		height: 100%;
		width: 100%;
		position: fixed;
		/* 第五层 */
		z-index: 50; 
	}
	
	.posterBox{
		height: 800upx;
		width: 600upx;
		position: fixed;
		/* 第六层 */
		z-index: 60;
		background-color: white;
		opacity: 1;
		border-radius: 8px;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		margin-top: auto;
		margin-bottom: auto;
		margin-left: auto;
		margin-right: auto;
	}
	
	.posterBox image{
		height: 100%;
		width: 100%;
		border-radius: 8px;
	}
	
	.adPoster{
		max-height: 500px;
	}
	
	
	.killIcon{
		position: absolute;
		/* top: 2px; */
		bottom: -100upx;
		right: 260upx;
		width: 80upx;
		height: 80upx;
		border-radius: 50%;
		background-color: #e6e6e6;
	}
	
	.killIcon image{
		width: 20px;
		height: 20px;
	}

</style>
