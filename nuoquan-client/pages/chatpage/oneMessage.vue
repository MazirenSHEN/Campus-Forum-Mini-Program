<template>
	<view>
		<view>
			<view class="send" v-if="thisMessage.flag == ME">
				<view @tap="goToPersonPublic(userInfo.id)">
					<image class="touxiang-send" :src="pathFilter(userInfo.faceImg)"></image>
				</view>
				<view class="content">
					<text selectable="true" class="contentText-send">
						{{thisMessage.msg}}
					</text>
<!-- 					<image src="../../static/icon/logo_name.png" mode="widthFix" class="picMessage"></image> -->	
				<view class="bottomBar">
						<view class="time-send">
							{{ timeDeal(thisMessage.createDate)}}
						</view>
						<!-- 					
					<view class="status-send" v-if="!thisMessage.isRead == UNREAD">
						...
					</view>
					<view class="status-send" style="color:#3FC24A" v-if="thisMessage.messageStatus">
						√
					</view>  代码保留，目前没有精力加后端 -->
					</view>
				</view>
			</view>
		</view>

		<view>
			<view class="recieve" v-if="thisMessage.flag == FRIEND">
				<view @tap="goToPersonPublic(friendInfo.id)">
					<image class="touxiang" :src="pathFilter(friendInfo.faceImg)"></image>
				</view>
				<view class="content">
					<text selectable="true" class="contentText-recieve">
						{{thisMessage.msg}}
					</text>
<!-- 					<image src="../../static/icon/logo_name.png" mode="widthFix" class="picMessage"></image>
 -->				<view class="bottomBar">
						<view class="time-recieve">
							{{timeDeal(thisMessage.createDate)}}
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			userInfo: '',
			friendInfo: '',
			thisMessage: {},
		},
		data() {
			return {
				ME: this.chat.ME,
				FRIEND: this.chat.FRIEND,
			};
		},
		onReady: function() {
			// console.log(this.chat);
			// console.log(this.thisMessage);
		},
		methods: {
			goToPersonPublic(userId) {
				uni.redirectTo({ // 关闭页面再跳转，防止页面栈满, 无法返回
					url: '/pagesSubA/personpublic/personpublic?userId=' + userId,
				});
			}
		},
	};
</script>

<style scoped>
	.touxiang {
		width: 60upx;
		height: 60upx;
		border-radius: 60upx;
	}
    .picMessage{
		max-width: 428upx;
	}
	.bottomBar {
		position: relative;
		display: flex;
		min-width: 30px;
		align-items: center;
		height: 18px;
		justify-content: inherit;
	}
</style>
<style>
	.send {
		display: flex;
		flex-direction: row-reverse;
		margin-right: 20upx;
		position: relative;
		margin-bottom: 12px;
	}

	.touxiang-send {
		width: 60upx;
		height: 60upx;
		border-radius: 60upx;
		position: absolute;
		bottom: 0;
		right: 0;
	}

	.send .content {
		display: inline-flex;
		flex-direction: column;
		max-width: 428upx;
		background: rgba(254,240,231,1);
		border-radius: 8pt 8pt 0pt 8pt;
		margin-right: 80upx;
		margin-bottom: 30upx;
		padding: 16upx 16upx 0upx 16upx;
	}

	.time-send {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		max-width: 100px;
		margin-right: 6px;
		color: #939393;
	}

	.status-send {
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		margin-right: 6px;
		color: white;
	}

	.contentText-send {
		min-width: 100upx;
		font-size: 12px;
		word-break: break-all;
		white-space: pre-line;
	}
</style>

<style>
	.recieve {
		display: flex;
		flex-direction: row;
		margin-left: 20upx;
		margin-bottom: 12px;
	}

	.recieve .content {
		display: inline-flex;
		flex-direction: column;
		max-width: 428upx;
		background: #FFF4DE;
		border-radius: 0pt 8pt 8pt 8pt;
		margin-top: 30upx;
		margin-left: 20upx;
		padding: 16upx 16upx 0upx 16upx;
		justify-content: flex-end;
	}

	.time-recieve {
		position: absolute;
		right: 0;
		display: inline-flex;
		font-size: 7px;
		font-weight: light;
		height: 18px;
		line-height: 18px;
		max-width: 100px;
		color: #939393;
	}

	.contentText-recieve {
		min-width: 100upx;
		max-width: 400upx;
		font-size: 12px;
		word-break: break-all;
		white-space: pre-line;
	}
</style>
