<template>
	<view class="oneVotePlusMenu">
		<view class="voteCard"
		:style="{ transform: messageIndex == vote.id ? transformX : 'translateX(0px)' }"
		:data-index="vote.id"
		@touchstart="touchStart"
		@touchmove="touchMove"
		@touchend="touchEnd"
		@touchcancel="touchEnd"
		@click="goToDetail(vote)"
		hover-class="hoverColor"
		>
			<view class="voteProjectName">{{vote.voteTitle}}</view>
			<block class="voteOptions" v-for="option in vote.optionList" :key="option.index">
				<view class="options">
					<view class="optionText">{{option.optionContent}}</view>
					<view class="voteNum">{{option.count}}</view>
					<view class="voteRate">{{reserveTwoDecimal(option.percent * 100)}}%</view>
					<view class="progressBar" :style="{width: option.percent*90/ maxPercent +'%'}"></view>
					
				</view>
			</block>


			<view class="bottomBar">
				<view class="time">{{timeDeal(vote.createDate)}}</view>
				<view class="comment">
					<image src="../../static/icon/comment-alt.png"></image>
					<view>{{vote.commentNum}}</view>
				</view>
				<view class="view">
					<image src="../../static/icon/eye-888888.png"></image>
					<view>{{vote.viewNum}}</view>
				</view>
			</view>
			<!-- 审核状态 -->
			<view class="status pass" style="min-width:49px;background: #09BB07;" v-if="vote.status == 1">{{lang.passCheck}}</view>
			<view class="status pending" style="min-width:36px;background: #3370FF;" v-if="vote.status == 2">{{lang.underCheck}}</view>
			<view class="status fail" style="min-width:49px;background: #888888;" v-if="vote.status == 3">{{lang.failCheck}}</view>

		</view>
		<view class="menu-area" v-if="messageIndex == vote.id">
			<view style="background: #FE5F55;" @click="fDeleteArticle(vote.id)">
				<image src="../../static/icon/bin.png"></image>
				<text>{{lang.delete}}</text>
			</view>
			<view style="background: #FCC041;" @click="closeSwipe">
				<image src="../../static/icon/arrow-right-FFFFFF.png"></image>
				<text>{{lang.pullUp}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			messageIndex:{
				default:'-1',
			},
			vote: "",
			lang: '',
		},
		data() {
			return {
				maxPercent: 0,
				serverUrl: this.$serverUrl,
				transformX: 'translateX(0px)',
				direction: '',
			};
		},
		methods: {
			//以下方程为控制左滑删除的部分 ref： swipe-acton in messageLish
			touchStart(event) {
				this.startX = event.touches[0].pageX;
				this.startY = event.touches[0].pageY;
			},
			touchMove(event) {
				if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
					this.direction = '';
					return;
				}
				var moveY = event.touches[0].pageY - this.startY,
					//  moveX用于判断方向
					moveX = event.touches[0].pageX - this.startX;
				// console.log("moveX");
				// console.log(moveX);
				// console.log("moveY");
				// console.log(moveY);
				if (Math.abs(moveY) > Math.abs(moveX) || Math.abs(moveY) > 100 || Math.abs(moveX) < 50) {
					//纵向滑动//参数100与50可调节侧滑灵敏度
					this.direction = 'Y';
					console.log('direction is Y ');
					this.$emit('modifySwipedId', -1);
					return;
				}
				// 移动距离
				this.direction = moveX > 0 ? 'right' : 'left';
				// 输出方向
				var emitSwipedId = moveX < 0 ? event.currentTarget.dataset.index : -1;
				this.$emit('modifySwipedId', emitSwipedId);
			},

			touchEnd(event) {
				if (this.direction !== 'right' && this.direction !== 'left') {
					this.direction = '';
					return;
				}
				if (this.direction == 'right') {
					this.$emit('modifySwipedId', -1);
				}
				this.endMove(event);
			},

			endMove(event) {
				if (this.direction === 'Y') {
					this.direction = '';
					this.$emit('modifySwipedId', -1);
					return;
				}
				if (this.messageIndex !== -1) {
					this.transformX = `translateX(${-58}px)`;
				} else {
					this.transformX = 'translateX(0px)';
				}
				this.direction = '';
			},
			//收起
			closeSwipe(){
				this.$emit('modifySwipedId',-1);
			},
			
			

		},
		mounted() {
			for (var i = 0; i < this.vote.optionList.length; i++) {
				if (this.vote.optionList[i].percent> this.maxPercent) {
					this.maxPercent = this.vote.optionList[i].percent ;
					console.log(this.maxPercent);
					console.log(this.vote.optionList[i].percent);
				}

			}
		}
	};
</script>

<style>
	.oneVotePlusMenu{
	width: 100%;
	position: relative;
	}
	.voteCard {
		width: calc(100% - 26px);
		margin: auto;
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		margin-top: 20px;
		position: relative;
	}

	.voteProjectName {
		padding: 20px 16px 12px;
		font-size: 17px;
		font-weight: 500;
		color: rgba(74, 74, 74, 1);

	}

	.options {
		padding-bottom: 17px;
		position: relative;
		margin-left: 24px;
		margin-right: 19px;
	}

	.optionText {
		font-size: 12px;
		line-height: 16px;
		color: rgba(136, 136, 136, 1);
		max-width: calc(100% - 67px);
	}

	.voteRate {
		width: 42px;
		height: 16px;
		font-size: 12px;
		line-height: 16px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		line-height: 16px;
		right: 3px;
		bottom: 17px;
		text-align: right;
	}

	.voteNum {
		width: 30px;
		height: 16px;
		font-size: 12px;
		color: rgba(136, 136, 136, 1);
		position: absolute;
		right: 54px;
		bottom: 17px;
		text-align: right;
	}

	.progressBar {
		border: 1px solid rgba(155, 155, 155, 1);
		border-radius: 10px;
		position: absolute;
		right: 0;
		bottom: 9px;
	}




	.bottomBar {
		width: calc(100% - 32px);
		margin: auto;
		height: 10px;
		padding-top: 12px;
		padding-bottom: 12px;
		position: relative;
		color: #9b9b9b;

	}

	.time {
		width: 84px;
		/* 暂时,到  月-日 时:分*/
		height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 0;
		bottom: 12px;
	}

	.comment {
		position: absolute;
		bottom: 12px;
		/* bottom, 对于comment time like view ,数值应等于menu-area的padding-bottom: ; */
		right: 0;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.view {
		position: absolute;
		bottom: 12px;
		right: 39px;
		width: 25px;
		height: 11px;
	}

	.comment image,
	.view image {
		width: 11px;
		height: 11px;
		right: 14px;
		position: absolute;
	}

	.comment view,
	.view view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}

	.status {
		padding: 4px 14px 4px 35px;
		font-size: 12px;
		height: 12px;
		line-height: 12px;
		position: absolute;
		top: -10px;
		left: 16px;
		color: #ffffff;
		border-radius: 25px;
		box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
	}

	.pass::after {
		content: '';
		position: absolute;
		width: 14px;
		height: 12px;
		background: url(../../static/icon/check-pass.png);
		background-size: contain;
		background-repeat: no-repeat;
		top: 4px;
		left: 14px;
	}

	.pending::after {
		content: '';
		position: absolute;
		width: 16px;
		height: 4px;
		background: url(../../static/icon/check-pending.png);
		background-repeat: no-repeat;
		background-size: contain;
		top: 8px;
		left: 14px;
	}

	.fail::after {
		content: '';
		position: absolute;
		width: 12px;
		height: 12px;
		background: url(../../static/icon/check-fail.png);
		background-size: contain;
		background-repeat: no-repeat;
		top: 4px;
		left: 14px;
	}
	
/* 	滑动的菜单 */
	.menu-area {
		width: 60px;
		height: 100%;
		position: absolute;
		right: 0;
		top: 0;
	}
	.menu-area view {
		height: 55px;
		width: 48px;
		background: #e80080;
		border-radius: 8px;
		font-size: 10px;
		text-align: center;
		margin-bottom: 11px;
		margin-left: 12px;
	}
	.menu-area view image {
		width: 18px;
		height: 18px;
		display: block;
		margin: auto;
		padding-top: 10px;
		padding-bottom: 4px;
	}
	.menu-area view text {
		min-width: 24px;
		height: 12px;
		font-size: 12px;
		line-height: 20px;
		color: rgba(255, 255, 255, 1);
	}
	/* 	滑动的菜单 END*/
</style>
