<template>
	<view style="width:100%;">

		<view class="oneArticle" v-if="thisArticle.status != 0">
			<view class="swipe-contain" :style="{ transform: messageIndex == thisArticle.id ? transformX : 'translateX(0px)' }"
			 :data-index="thisArticle.id" @touchstart="touchStart" @touchmove="touchMove" @touchend="touchEnd" @touchcancel="touchEnd"
			 @click="goToDetail(thisArticle)" hover-class="hoverColor">
				<longarticle :longArticle="thisArticle"></longarticle>
				<!-- 审核状态 -->
				<view class="status pass" style="min-width:49px;background: #09BB07;" v-if="thisArticle.status == 1">{{lang.passCheck}}</view>
				<view class="status pending" style="min-width:36px;background: #3370FF;" v-if="thisArticle.status == 2">{{lang.underCheck}}</view>
				<view class="status fail" style="min-width:49px;background: #888888;" v-if="thisArticle.status == 3">{{lang.failCheck}}</view>
			</view>

			<view class="menu-area" v-if="messageIndex == thisArticle.id">
				<view style="background: #FE5F55;" @click="fDeleteArticle(thisArticle.id)">
					<image src="../../static/icon/bin.png"></image>
					<text>{{lang.delete}}</text>
				</view>
				<view style="background: #FCC041;" @click="closeSwipe">
					<image src="../../static/icon/arrow-right-FFFFFF.png"></image>
					<text>{{lang.pullUp}}</text>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import longarticle from './nq-hotCard.vue'
	export default {
		props: {
			messageIndex: {
				default: '-1',
			},
			thisArticle: {},
			lang: '',
		},
		components: {
			longarticle,
		},
		data() {
			return {
				serverUrl: this.$serverUrl,
				transformX: 'translateX(0px)',
				direction: '',

			};
		},
		onPageScroll() {
			this.$emit('modifySwipedId', -1);
		},
		mounted() {
			this.parsedResult = markdownParse.parsePureText(this.thisArticle.content);
			console.log(parsedResult);
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
			//控制滑动结束，以下控制跳转detail
			goToDetail(thisArticle) {
				//thisArticle用函数传入，因为v-for使用了ID为键名，所以无法筛选数据，就直接block传进来好了
				if (thisArticle.isJump == 0) { //0，跳转详情
					uni.navigateTo({
						url: '../../pagesSubA/longArticleDetail/longArticleDetail?data=' + thisArticle.id,
					});
				} else if(thisArticle.isJump == 1){
					var encodeData = encodeURIComponent(thisArticle.link);
					uni.navigateTo({
						url: '/pages/adWebPage/adWebPage?url=' + encodeData,
					})
				}
			},

			//收起
			closeSwipe() {
				this.$emit('modifySwipedId', -1);
			},

			fDeleteArticle(articleId) {
				uni.request({
					url: this.$serverUrl + '/longarticle/deleteArticle',
					method: 'POST',
					data: {
						articleId: articleId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
					},
					fail: res => {

					}
				});
				uni.$emit("refresh");
			}
		}
	};
</script>

<style scoped>
	.oneArticle {
		width: 100%;
		position: relative;
	}

	.swipe-contain {
		position: relative;
		width: clac(100% - 32px);
		padding-left: 16px;
		padding-right: 16px;
		padding-top: 10px;
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		margin-bottom: 20px;
		z-index: 10;
		pointer-events: auto;
	}

	.title {
		font-size: 17px;
		color: ##4a4a4a;
		padding-top: 19px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.cardBody {
		position: relative;
		width: 100%;
		/* height:83px; */
		padding-top: 12px;
		vertical-align: top;
		height: 70px;
	}

	.picArea {
		display: inline-block;
		width: 61px;
		height: 61px;
		position: absolute;
		top: 12px;
		left: 0;
		vertical-align: top;
	}

	.picArea image {
		position: absolute;
		left: 0;
		top: 0;
		width: 61px;
		height: 61px;
	}

	.left-body {
		width: 100%;
		height: 70px;
	}

	.leftBodyWithPic {
		width: calc(100% - 73px);
		position: absolute;
		left: 73px;
		bottom: 0;
	}

	.content {
		height: 39px;
		width: 100%;
		font-size: 12px;
		line-height: 13px;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.bottomBar {
		width: 100%;
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
		right: 40px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.like {
		position: absolute;
		bottom: 12px;
		right: 1px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.view {
		position: absolute;
		bottom: 12px;
		right: 79px;
		width: 25px;
		height: 11px;
	}

	.comment image,
	.like image,
	.view image {
		width: 11px;
		height: 11px;
		right: 14px;
		position: absolute;
	}

	.like view,
	.comment view,
	.view view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}

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
</style>
