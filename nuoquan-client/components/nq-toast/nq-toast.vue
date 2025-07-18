/*
	#通用弹窗组件
	#Author: Guetta
	#Update Log: Guetta 2020年9月16日21点45分 v2.0.2
*/
<template>
	<view class="toastBox column_center" :style="{'height': screenHeight + 'px', 'width': screenWidth + 'px'}">
		<view class="toastCard" :style="{'width': screenWidth - 40 + 'px'}">
			<view style="position: relative;width: 100%;height: 100%;">
				<view class="duckduck">
					<image src="https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/duckduck.png" mode="aspectFit"></image>
				</view>
				<!-- 关闭按钮 -->
				<view class="killToast" @click="killToast">
					<image src="../../static/icon/delete.png" mode="aspectFit"></image>
				</view>
				<!-- 文字部分 -->
				<view class="midLine">
					<image v-if="nowStatus == 200" src="../../static/icon/confirm-circle.png" mode="aspectFit"></image>
					<image v-else src="../../static/icon/cry-colorful.png" mode="aspectFit"></image>
					<text v-if="nowStatus == 200">已成功放入投票箱</text>
					<text v-if="nowStatus == 500" style="max-width: 60%;">您今天已经没有票数啦，明天再来吧</text>
					<text v-if="nowStatus !== 500 && nowStatus !== 200">糟糕，网络出错了。。</text>
				</view>
				<!-- 按钮部分 -->
				<view v-if="nowStatus == 200" class="bottomLine super_center">
					<!-- <view @click="updateVote" class="oneButton superCenter" style="background: #FAFAFA;">
						<view hover-class="hoverColor" style="color: #FCC041;border: 1px solid #DCDCDC;">再来一票！</view>
					</view> -->
					<view class="oneButton superCenter">
						<view @click="sharePoster" hover-class="hoverColorYellow">帮TA分享一下鸭~</view>
					</view>
				</view>
				<view v-if="nowStatus == 500 && isNull(userInfo.email) " class="bottomLine super_center">
					<view class="oneButton superCenter">
						<view @click="goProfile" hover-class="hoverColorYellow">认证邮箱获得额外2票~</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex';
	
	export default {
		props:{
			voteStatus: '',
			cardInfo: '',
		},
		data() {
			return {
				// 界面显示层
				screenWidth: '',
				screenHeight: '',
				clickTimes: 0,
				// 数据控制层
				userInfo: '', // userInfo
				nowStatus: this.voteStatus, // 本地化弹窗状态
				thisCard: this.cardInfo, // 本地化卡片数据
			}
		},
		computed: {
			...mapState(['lang'])
		},
		watch:{
			
		},
		created() {
			// 视图层
			var that = this;
			console.log(that.userInfo.email);
			// debugger
			console.log("tost cardInfo" + that.cardInfo);
			uni.getSystemInfo({
				success: (res) => {
					that.screenHeight = res.screenHeight;
					that.screenWidth = res.screenWidth;
				}
			})
			
			
			// 数据层
			var userInfo = that.getGlobalUserInfo();
			if (that.isNull(userInfo)) {
				uni.redirectTo({
					url: '../../pages/signin/signin'
				});
				return;
			} else {
				that.userInfo = userInfo; // 刷去默认值(若有)
			}
		},
			
		
		methods: {
			killToast(){
				this.$emit('killToast', {
					showDetailCardstatus: '',
					voteStatus: 100,
					refreshList: true,
					share: false,
					teamDetail:{
						title: '',
						intro: '',
						thisCard: '',
					},
				})
			},
			
			sharePoster(){
				var that = this;
				console.log(that.thisCard);
				this.$emit('sharePoster', {
					showDetailCardstatus: '',
					voteStatus: 100,
					refreshList: false,
					share: true,
					teamDetail:{
						title: '',
						intro: '',
						// 由于传入 tosat 的 thisCard 比 teamDetail 中的多一层
						// 所以这里传回两层
						// TODO：优化这里代码，统一格式
						thisCard: that.thisCard.thisCard,
					},
				})
			},
			
			goProfile(){
				uni.navigateTo({
					url: "../../pages/profile/profile"
				})
			}
			// updateVote(){
			// 	var that = this;
			// 		// debugger
			// 	uni.request({
			// 		url: that.$serverUrl + '/vote/selectOption',
			// 		method: 'POST',
			// 		data: {
			// 			optionId: that.thisCard.thisCard.id,
			// 			userId: that.userInfo.id,
			// 			voteId: "2009133STNGWZTR4",
			// 		},
			// 		header: {
			// 			'content-type': 'application/x-www-form-urlencoded'
			// 		},
			// 		success: (res) => {
			// 			uni.hideLoading();
			// 			console.log("succes");
			// 			// 修改卡片状态
			// 			that.nowStatus = res.data.status;
			// 		},
			// 		fail: (res) => {
			// 			console.log('Page unirequest fail');
			// 			console.log(res);
			// 		}
			// 	})
			// }
		}
	}
</script>

<style>
	.toastBox{
		position: fixed;
		top: 0;
		z-index: 99999;
		background-color: rgba(0,0,0,0.25);
	}
	
	.toastCard{
		position: absolute;
		left: 20px;
		height: 186px;
		background-color: #FAFAFA;
		border-radius: 12px;
	}
	
	.duckduck{
		position: absolute;
		top: -44px;
		left: 30px;
		height: 60px;
		width: 60px;
	}
	
	
	.duckduck image{
		height: 100%;
		width: 100%;
		background-color: rgba(255,255,255,0);
	}
	
	.killToast{
		position: absolute;
		top: 16px;
		right: 16px;
		height: 20px;
		width: 20px;
	}
	
	.killToast image{
		width: 100%;
		height: 100%;
	}
	
	.midLine{
		position: absolute;
		top: 40px;
		display: flex;
		width: 100%;
		padding: 20px 20px;
	}
	
	.midLine image{
		height: 35px;
		width: 35px;
	}
	
	.midLine text{
		margin-left: 10px;
		height: 35px;
		font-size: 20px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 35px;
		color: #353535;
		opacity: 1;
	}
	
	.bottomLine{
		position: absolute;
		width: 100%;
		height: 40px;
		bottom: 20px;
	}
	
	.oneButton{
		background: #FCC041;
		height: 40px;
		border-radius: 8px;
	}
	
	.oneButton view{
		padding: 12px 20px;
		border-radius: 8px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: #FFFFFF;
		opacity: 1;
	}
</style>
