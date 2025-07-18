<!-- 
	Date: July 12, 2022
	Editor: Yifei
	Description: 用于messageList，对话消息左滑拉出删除键
 -->
<template>
	<view class="message-list">
		<block v-for="(it, i) in messagesList" :key="i">
			<view class="uni-swipe-action">
				<view
					class="uni-swipe-action__container"
					style="width: 90.6%;
						margin-left: 4.8%;
						margin-top: 6px;
						margin-bottom: 6px;
						height: 64px;
						background-color: white;"
					@touchstart="touchStart"
					@touchmove="touchMove"
					@touchend="touchEnd"
					@touchcancel="touchEnd"
					:style="{
						transform: messageIndex == i ? transformX : 'translateX(0px)',
						'-webkit-transform': messageIndex == i ? transformX : 'translateX(0px)',
						'box-shadow': it.unreadCount > 0 ? '0upx 0upx 12upx 1upx #D1D1D1' : ''
					}"
					:data-index="i"
					:data-disabled="it.disabled"
				>
					<view class="uni-swipe-action__content" style="box-shadow:0px 0px 6px rgba(0,0,0,0.16);" hover-class="hoverColor" @click="toMessageDetail(i, it.type)">
						<view class="item" :class="it.stick ? 'stick' : ''" @tap="tapCard(it)">
							<block>
								<view class="item-left">
									<!-- <avator-group :type="it.type" :avator="it.url"></avator-group> -->
									<view class="avator"><image class="img" :src="pathFilter(it.friendInfo.faceImg)"></image></view>
								</view>
								<!-- 中间区域 -->
								<view class="item-middle">
									<view style="display: flex;">
										<text :class="it.unreadCount > 0 ? 'title' : 'title-read'">{{ it.friendInfo.nickname }}</text>
										<image v-if="it.friendInfo.authType == 2" src="../static/icon/auth_red.png" style="width: 18px; height: 15px;margin-top: 3px;margin-left: 3px;"></image>
										<image v-if="it.friendInfo.authType == 1" src="../static/icon/auth_yellow.png" style="width: 18px; height: 15px;margin-top: 3px;margin-left: 3px;"></image>
									</view>
									<text :class="it.unreadCount > 0 ? 'message' : 'message-read'">{{ it.msg }}</text>
								</view>

								<view class="item-right">
									<view class="time">{{ timeDeal(it.createDate) }}</view>
									<!-- <view class="mark" v-if="it.unreadCount > 0">{{ it.count }}</view> -->
									<msgcount style="position: absolute;z-index: 40;right: 0;bottom: 0;" :count="it.unreadCount"></msgcount>
								</view>
							</block>
						</view>
					</view>
				</view>
				<view class="delete-button super_center" v-if="showdelete == 1 && messageIndex == i" @tap="tapDelete(it)">
					<text style="color: white;text-align: center;font-size: small;">{{lang.delete}}</text>
				</view>
			</view>
		</block>
	</view>
</template>

<script>
import msgcount from './nq-msgcount/nq-msgcount.vue';
export default {
	name: 'swipe-action',
	props: {
		messagesList: '',
		lang: '',
	},
	components:{
		msgcount
	},
	data() {
		const elId = `Uni_${Math.ceil(Math.random() * 10e5).toString(36)}`;
		return {
			elId: elId,
			transformX: 'translateX(0px)',
			messageIndex: -1,
			showdelete: 0,

			READ: this.chat.READ,
			UNREAD: this.chat.UNREAD
		};
	},
	created() {
		this.direction = '';
		this.startX = 0;
		this.startY = 0;
		this.btnGroupWidth = 0;
		this.isMoving = false;
	},
	methods: {
		toMessageDetail(i, type) {
			if (type == 2) {
				console.log('进入到聊天界面' + i);
				uni.navigateTo({
					url: '../message_info/message_info'
				});
			} else if (type == 1) {
				console.log('进入系统消息界面' + i);
			}
		},
		bindClickBtn(item, index) {
			this.messageIndex = -1;
			console.log(item.text + 'message第' + index + '项');
		},
		touchStart(event) {
			if (event.currentTarget.dataset.disabled === true) {
				return;
			}
			this.startX = event.touches[0].pageX;
			this.startY = event.touches[0].pageY;
		},
		touchMove(event) {
			if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
				this.direction = '';
				return;
			}
			var moveY = event.touches[0].pageY - this.startY,
				//  moveX用于判断方向，向右滑动为负，向左滑动为正
				moveX = event.touches[0].pageX - this.startX;
			if ((!this.isMoving && Math.abs(moveY) > Math.abs(moveX)) || Math.abs(moveY) > 100 || Math.abs(moveX) < 50) {
				//纵向滑动//参数100与50可调节侧滑灵敏度
				this.direction = 'Y';
				return;
			}
			// 移动距离
			console.log(moveX);
			this.direction = moveX > 0 ? 'right' : 'left';
			// this.direction = moveX > 0 ? 'left' : 'right';
			// 输出方向
			console.log(this.direction);
			this.messageIndex = moveX < 0 ? event.currentTarget.dataset.index : -1;
			this.isMoving = true;
		},
		touchEnd(event) {
			this.isMoving = false;
			if (this.direction !== 'right' && this.direction !== 'left') {
				this.direction = '';
				return;
			}

			// right -> left
			if (this.direction == 'right') {
				this.messageIndex = -1;
			}
			console.log(this.messageIndex);
			this.endMove(event);
		},
		endMove(event) {
			if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
				this.direction = '';
				return;
			}
			if (this.messageIndex !== -1) {
				// 负号控制块左右移动
				// this.transformX = `translateX(${0}px)`;
				this.transformX = `translateX(${-18}px)`;
				this.$emit('opened');
				this.showdelete = 1;
			} else {
				this.transformX = 'translateX(0px)';
				this.$emit('closed');
				this.showdelete = 0;
			}
			this.direction = '';
		},

		tapCard(it) {
			this.$emit('tapCard', it);
		},
		tapDelete(it) {
			this.$emit('tapDelete', it);
			this.showdelete = 0;
			this.transformX = 'translateX(0px)';
		}
	}
};
</script>

<style lang="scss">
.delete-button {
	background-color: #007aff;
	width: 50px;
	height: 26px;
	margin-top: 26px;
	margin-right: 26px;
	background-color: #fccf41;
	border-radius: 4px;
}

// .msglist-card {
// 	width: 88%;
// 	margin-left: 6%;
// 	margin-top: 8upx;
// 	height: 120upx;
// 	background-color: white;
// 	border-radius: 12upx;
// 	box-shadow: 0upx 0upx 14upx 0upx #B2B2B2;
// }
//
// .msglist-card-read {
// 	width: 88%;
// 	margin-left: 6%;
// 	margin-top: 8upx;
// 	height: 120upx;
// 	background-color: white;
// 	border-radius: 12upx;
// }

.uni-swipe-action {
	width: 100%;
	overflow: hidden;
	display: flex;

	&__container {
		background-color: #ffffff;
		width: 200%;
		display: flex;
		float: left;
		flex-direction: row;
		flex-wrap: wrap;
		transition: transform 350ms cubic-bezier(0.165, 0.84, 0.44, 1);
	}

	&__content {
		width: 100%;
	}

	&__btn-group {
		display: flex;
		flex-direction: row;
		align-items: center;
		background-color: #4191ea;
	}

	&--btn {
		height: 50%;
		padding: 0 32upx;
		color: #ffffff;
		background-color: #fccf41;
		font-size: 28upx;
		display: inline-flex;
		text-align: center;
		flex-direction: row;
		align-items: center;
		margin-left: 20upx;
		margin-right: 20upx;
		border-radius: 25upx;
	}
}

.item {
	display: flex;
	padding: 10upx 16upx;
	height: 54px;
	width: calc(100% - 16px);

	&.stick {
		background-color: rgba(243, 236, 221, 0.5);
	}

	.item-left {
		display: flex;
		align-items: center;
		text-align: center;

		.image {
			width: 40px;
			height: 40px;
			border-radius: 50upx;
			background-color: #eee;
		}

		.avator {
			width: 40px;
			height: 40px;
			border-radius: 50%;
			overflow: hiideen;
			background: #f1f1f1;
			.img {
				width: 100%;
				height: 100%;
				border-radius: 50%;
			}
			.iconfont {
				width: 100upx;
				height: 100upx;
				text-align: center;
				line-height: 100upx;
				color: #fff;
				font-size: 40upx;
			}
		}
	}

	.item-middle {
		display: flex;
		flex: 1;
		flex-wrap: wrap;
		flex-direction: row;
		justify-content: flex-start;
		margin-left: 8px;
		overflow: hidden;

		.title {
			width: 100%;
			height: 23px;
			font-size: 14px;
			font-family: Source Han Sans CN;
			font-weight: bold;
			line-height: 23px;
			color: rgba(53, 53, 53, 1);
			opacity: 1;
			overflow: hidden;
			/*自动隐藏文字*/
			text-overflow: ellipsis;
			/*文字隐藏后添加省略号*/
			white-space: nowrap;
			/**强制不换行*/
		}

		.title-read {
			width: 100%;
			height: 23px;
			font-size: 14px;
			font-family: Source Han Sans CN;
			font-weight: bold;
			line-height: 23px;
			color: rgba(136, 136, 136, 1);
			opacity: 1;
			overflow: hidden;
			/*自动隐藏文字*/
			text-overflow: ellipsis;
			/*文字隐藏后添加省略号*/
			white-space: nowrap;
			/**强制不换行*/
		}

		.message {
			width: 100%;
			height: 23px;
			font-size: 11px;
			font-family: Source Han Sans CN;
			font-weight: 400;
			line-height: 23px;
			color: rgba(53, 53, 53, 1);
			opacity: 1;
			overflow: hidden;
			/**自动隐藏文字*/
			text-overflow: ellipsis;
			/**文字隐藏后添加省略号*/
			white-space: nowrap;
			/**强制不换行*/
		}

		.message-read {
			width: 100%;
			height: 23px;
			font-size: 11px;
			font-family: Source Han Sans CN;
			font-weight: 400;
			line-height: 23px;
			color: rgba(136, 136, 136, 1);
			opacity: 1;
			overflow: hidden;
			/**自动隐藏文字*/
			text-overflow: ellipsis;
			/**文字隐藏后添加省略号*/
			white-space: nowrap;
			/**强制不换行*/
		}
	}

	.item-right {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;

		.time {
			margin-top: -3px;
			color: rgba(136, 136, 136, 1);
			font-size: 11px;
			height: 30px;
			line-height: 30px;
			max-width: 85px;
			text-overflow: ellipsis;
		}

		.mark {
			position: absolute;
			right: 3px;
			bottom: 5px;
			background-color: #fdd453;
			line-height: 17.5px;
			text-align: center;
			font-size: 11px;
			min-width: 7.5px;
			min-height: 7.5px;
			border-radius: 50%;
			color: #fff;
		}
	}
}
</style>
