/*
	#投票活动落地页,队伍详情卡片
	#Author: Guetta
	#Update Log: Guetta 2020.9.13 v2.0.2
*/
<template>
	<view class="teamDetailCard" :style="{'height': screenHeight + 'px', 'width': screenWidth + 'px'}">
		<!-- 关闭按钮 -->
		<view style="position: absolute;top: 168px;right: 20px;z-index: 20;" @click="closeCard(100)">
			<image style="width: 20px;height: 20px;" src="../../static/icon/check-fail.png" mode="aspectFit"></image>
		</view>
		<!-- 白色区域 -->
		<view class="teamDetailInfo" :style="{ 'height': screenHeight - 200 + 'px'}">
			<!-- 队伍图片 高度400upx -->
			<view class="imgBox_teamDetail">
				<image @tap="previewImg" @longpress="aboutImg" :src="pathFilter(thisCard.thisCard.img)" mode="aspectFill"></image>
			</view>
			<!-- 下方文字区域 -->
			<!-- 背景作底 -->
			<view class="teamIntroArea">
				<view class="contentArea">
					<!-- 按钮行，buttonLine用来辅助居中 -->
					<view v-if="voteAvailable" class="buttonLine super_center">
						<view class="twoButton">
							<!-- 单个按钮 -->
							<view v-for="(button,index) in buttonList" :key="index" v-bind:button="button" 
							:class="[index == 0 ? 'leftButton' : 'rightButton']" hover-class="hoverColor" @click="clickButton(index)">
								<image class="iconImg" :src="button.iconSrc" mode="aspectFit"></image>
								<text class="detailText">{{button.text}}</text>
							</view>
						</view>
					</view>
					<!-- 组名 -->
					<view class="textLine">
						<text class="teamName-detailCard" selectable="true">{{thisCard.title}}</text>
					</view>
					<!-- 票数差距 -->
					<view v-if="voteAvailable" class="voteToNext">
						{{thisCard.thisCard.count}} {{lang.voteCount}}{{lang.voteToLastTeam}} {{thisCard.gap}} {{lang.voteCount}}
					</view>
					<view v-else class="voteToNext">
						活动已经结束啦，但 Nottinghome 将一直陪伴大家的大学生活~ 
						点击左上角的小房子，来“轮滑广场”溜达溜达吧！
						也可以点击一下右上角的三个点点，并添加小程序，支持一下我们 (●'◡'●)
					</view>
					<!-- 小组简介 -->
					<view v-if="stage == 1 && voteAvailable" class="voteToNext" style="
						word-wrap: break-word;
						word-break: break-all;
						white-space: pre-line;
						">
						{{thisCard.intro}}
					</view>
					<view v-if="stage == 2 && userVoteLeft > 0" class="voteToNext">
						当前剩余票数{{userVoteLeft}}票，
					</view>
					<view v-if="stage == 2 && userVoteLeft > 0" class="voteToNext">
						确定投票给该组吗?
					</view>
					<view v-if="stage == 2 && userVoteLeft <= 0" class="voteToNext">
						你今天的票数已经用完啦，邮箱验证用户有 3 票，游客用户只有一票哦~
						邮箱验证请点击“确认”，根据提示前往验证。
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
			cardInfo: {},
		},
		data() {
			return {
				// 界面显示控制
				screenWidth: 0, // 屏幕宽度
				screenHeight: 0, // 屏幕高度
				navbarHeight: 0, // 一次性储存navbarHeight
				buttonList:[{
					iconSrc:'../../static/icon/wechatMoment.png',
					text: '分享',
				},{
					iconSrc:'../../static/icon/redWallEvent-vote.png',
					text: '投票',
				}],
				stage: 1, // 控制小组详情弹窗状态
				voteAvailable: true, // 控制活动结束
				// 数据控制
				userInfo: '',
				thisCard: this.cardInfo, // 转为局部变量
				userVoteLeft: '', // 用户的剩余票数
				
				share: false, //控制分享
			}
		},
		components: {
			
		},
		computed: {
			...mapState(['lang'])
			
		},
		created() {
			this.userInfo = this.getGlobalUserInfo();
			var that = this;
			// 一次性储存navbarHeight
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			uni.getSystemInfo({
				success: (res) => {
					that.screenWidth = res.screenWidth;
					that.screenHeight = res.screenHeight;
				}
			})
			// 控制活动结束
			var currentTime = new Date().getTime();
			if(1600703998000 <= currentTime){
				console.log('currentTime' + currentTime);
				that.voteAvailable = false;
			}
			
			// 查询剩余票数
			this.getVoteLeft();
			
		},
		
		methods: {
			
			// 关闭窗口
			closeCard(status){
				var that = this;
				/**
				* status=200: success vote
				* status=500: no vote left
				* status=100: just kill the mdfk card plz
				*/
				that.$emit('eventData', {
					showDetailCardstatus: false,
					voteStatus: status,
					refreshList: '',
					share: false,
					teamDetail:{
						title: '',
						intro: '',
						thisCard: that.thisCard,
					},
				});
				return
			},
			
			getVoteLeft(){
				var that = this;
				uni.request({
					url: that.$serverUrl + '/vote/userLeftVote',
					method: "POST",
					data: {
						voteId: "20200916",
						optionId: that.thisCard.thisCard.id,
						userId: that.userInfo.id
					},
					header:{
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						// debugger
						that.userVoteLeft = res.data.data;
					}
				})
			},
			
			// 点击按钮
			clickButton(index){
				var that = this;
				if (index == 1  && that.stage == 1){
					// 点击投票按钮后，更改状态值为2
					that.stage = 2;
					that.buttonList = [{
						iconSrc:'../../static/icon/delete.png',
						text: '取消',
					},{
						iconSrc:'../../static/icon/confirm-circle.png',
						text: '确定',
					}]
					return
				};
				
				if (index == 0  && that.stage == 1) {
					console.log("生成分享图片");
					that.$emit('eventData', {
						showDetailCardstatus: false,
						voteStatus: '',
						refreshList: '',
						share: true,
						teamDetail:{
							title: '',
							intro: '',
							thisCard: that.thisCard,
						},
					});
					
					return
				};
				if (index == 1 && that.stage == 2){
					console.log("点击确认");
					// 选择该选项
					that.updateVote();
					return
				};
				if (index == 0 && that.stage == 2){
					console.log("点击取消");
					that.stage = 1; // 切换为状态1 
					that.buttonList = [{
						iconSrc:'../../static/icon/wechatMoment.png',
						text: '分享',
					},{
						iconSrc:'../../static/icon/redWallEvent-vote.png',
						text: '投票',
					}]
					return
				}
			},
			
			// 上传选择
			updateVote(){
				var that = this;
					// debugger
				uni.request({
					url: that.$serverUrl + '/vote/selectOption',
					method: 'POST',
					data: {
						optionId: that.thisCard.thisCard.id,
						userId: that.userInfo.id,
						voteId: "20200916",
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						uni.hideLoading();
						console.log("succes");
						console.log(res);
						that.closeCard(res.data.status);
					},
					fail: (res) => {
						console.log('Page unirequest fail');
						console.log(res);
					}
				})
			},
			
			// 预览图片
			previewImg(){
				var that = this;
				console.log("预览图片");
				// debugger
				uni.previewImage({
					current: 0,
					urls: [that.pathFilter(that.thisCard.thisCard.img)],
				})
			},
			
			// 长按保存
			aboutImg: function(index) {
				var that = this;
				console.log(that.thisCard.thisCard.img);
				uni.showActionSheet({
					itemList: ['保存图片到本地'],
					success: function(res) {
						console.log(res.tapIndex);
						// 保存图片至本地
						if (res.tapIndex == 0) {
							uni.showLoading({
								title: '下载中...'
							});
							uni.downloadFile({
								url: that.pathFilter(that.thisCard.thisCard.img),
								success: function(res) {
									if (res.statusCode == 200) {
										uni.saveImageToPhotosAlbum({
											filePath: res.tempFilePath,
											success: function() {
												console.log('save success');
												uni.hideLoading();
											},
											fail: function() {
												console.log('save failed');
												uni.hideLoading();
												uni.showToast({
													title: '保存失败',
													icon: 'none',
													duration: 1000
												});
											}
										});
									}
								}
							});
						}
					}
				});
			},
			
			
		}
	}
</script>

<style>
	.teamDetailCard{
		position: fixed;
		top: 0;
		z-index: 50;
		background-color: rgba(0,0,0,0.25);
		overflow: scroll;
	}
	
	.teamDetailInfo{
		position: absolute;
		bottom: 0;
		z-index: 60;
		width: 100%;
		background-color: #FAFAFA;
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
	}
	
	.imgBox_teamDetail{
		width: 100%;
		height: 480upx;
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
	}
	
	.imgBox_teamDetail image{
		height: 100%;
		width: 100%;
		border-top-left-radius: 20px;
		border-top-right-radius: 20px;
	}
	
	.teamIntroArea{
		position: relative;
		height: 100%;
		width: 100%;
		/* background-color: rgba(240,235,231,1); */
		background-color: rgba(213,91,80,0.9);
	}
	
	.contentArea{
		position: absolute;
		width: calc(100% - 32px);
		height: calc(100% - 16px);
		left: 16px;
		top: 8px;
		background-color: #FAFAFA;
		overflow: scroll;
	}
	
	.voteLeft{
		position: absolute;
		bottom: 40px;
		right: 26px;
	}
	
	/* 在这里控制整行 */
	.buttonLine{
		margin-top: 16px;
		width: 100%;
		height: 48px;
	}
	/* 两个按钮样式 */
	.twoButton{
		display: flex;
		width: 210px;
		height: 48px;
		box-shadow: 1px 0px 5px rgba(0,0,0,0.3);
	}
	/* 左右圆角 */
	.twoButton, .leftButton {
		border-top-left-radius: 24px;
		border-bottom-left-radius: 24px;
	}
	
	.twoButton, .rightButton {
		border-top-right-radius: 24px;
		border-bottom-right-radius: 24px;
	}
	/* 按钮本身样式在这里定义 */
	.leftButton, .rightButton{
		position: relative;
		width: 50%;
		height: 48px;
	}
	
	.iconImg{
		position: absolute;
		left: 16px;
		top: 14px;
		width: 20px;
		height: 20px;
	}
	
	.detailText{
		position: absolute;
		left: 44px;
		top: 14px;
		height: 20px;
		width: 54px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 20px;
		color: #4A4A4A;
		opacity: 1;

	}
	
	/* 边框放在右侧按钮上 */
	.rightButton{
		border-left: 1px solid rgba(0,0,0,0.3);
	}
	
	.textLine{
		padding: 16px 0px 16px 16px;
	}
	
	.teamName-detailCard{
		height: 20px;
		font-size: 20px;
		font-family: Source Han Sans CN;
		font-weight: 550;
		line-height: 20px;
		color: #4A4A4A;
		opacity: 1;
	}
	
	.voteToNext{
		width: calc(100% - 32px);
		padding: 0 0 16px 16px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 20px;
		color: #4A4A4A;
		opacity: 1;
		
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
	}
	
</style>
