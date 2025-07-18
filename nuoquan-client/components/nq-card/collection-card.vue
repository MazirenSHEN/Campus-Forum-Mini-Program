<template>
	<view style="width:100%;">

		<view class="oneArticle" v-if="thisArticle.status != 0">
			<!--帖子状态-->
			<view v-if="articleDelieverer.targetType == 'article'" class="swipe-contain" :style="{ transform: swipedArticleId == thisArticle.id ? transformX : 'translateX(0px)' }"
			 :data-index="thisArticle.id" @touchstart="touchStart" @touchmove="touchMove" @touchend="touchEnd" @touchcancel="touchEnd"
			 @click="goToDetail(thisArticle)" hover-class="hoverColor">
				<view v-if="!isNull(thisArticle.articleTitle)" class="title">{{thisArticle.articleTitle}}</view>
				<view v-else-if="isNull(thisArticle.articleTitle)" style="padding: 20px;"></view>
				<view class="cardBody">
					<view class="picArea" v-if="thisArticle.imgList.length">
						<image :src="pathFilter(thisArticle.imgList[0].imagePath)"></image>
					</view>
					<view class="left-body" :class="{ leftBodyWithPic: thisArticle.imgList.length != 0 && thisArticle.imgList != null}">
						<view class="content">{{thisArticle.articleContent}}</view>
						<view class="bottomBar">
							<view class="time">{{timeDeal(thisArticle.createDate)}}</view>
							<view class="comment">
								<image src="../../static/icon/comment-alt.png"></image>
								<view>{{ thisArticle.commentNum }}</view>
							</view>
							<view class="like">
								<image v-if="!thisArticle.isLike" src="../../static/icon/like.png"></image>
								<image v-if="thisArticle.isLike" src="../../static/icon/liked-red.png"></image>
								<view>{{ thisArticle.likeNum }}</view>
							</view>
							<!-- <view class="view">
								<image src="../../static/icon/eye-888888.png"></image>
								<view>{{ thisArticle.viewNum }}</view>
							</view> -->
						</view>
					</view>
				</view>
			</view>
			<!--长文章状态-->
			<!-- <view v-if="articleDelieverer.targetType == 'longarticle'" class="swipe-contain-LA" :style="{ transform: swipedArticleId == thisArticle.id ? transformX : 'translateX(0px)' }"
			 :data-index="thisArticle.id" @touchstart="touchStart" @touchmove="touchMove" @touchend="touchEnd" @touchcancel="touchEnd"
			 @click="goToDetail(thisArticle)" hover-class="hoverColor">
				<longarticle :longArticle="thisArticle"></longarticle>
			</view> -->
			<view class="menu-area" v-if="swipedArticleId == thisArticle.id" @click="unCollectArticle()">
				<view>
					<image src="../../static/icon/bin.png"></image>
					<text>{{lang.uncollectText}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import longarticle from './nq-hotCard.vue'
	export default {
		components:{
			longarticle,
		},
		props: {
			swipedArticleId: {
				default: '-1',
			},
			articleDelieverer: {},
			lang: '',
			userInfo: "",

		},
		data() {
			return {
				serverUrl: this.$serverUrl,
				transformX: 'translateX(0px)',
				direction: '',
				thisArticle: this.articleDelieverer.target,
			};
		},
		onPageScroll() {
			this.$emit('modifySwipedId', -1);
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
				if (this.swipedArticleId !== -1) {
					this.transformX = `translateX(${-58}px)`;
				} else {
					this.transformX = 'translateX(0px)';
				}
				this.direction = '';
			},
			//控制滑动结束，以下控制跳转detail
			goToDetail(thisArticle) {
				//thisArticle用函数传入，因为v-for使用了ID为键名，所以无法筛选数据，就直接block传进来好了
				if (thisArticle.isJump == 0 ) { //0，跳转长文章详情
					uni.navigateTo({
						url: '../../pagesSubA/longArticleDetail/longArticleDetail?data=' + thisArticle.id,
					});
				} else if(thisArticle.isJump == 1){
					var encodeData = encodeURIComponent(thisArticle.link);
					uni.navigateTo({
						url: '/pages/adWebPage/adWebPage?url=' + encodeData,
					})
				} else if(this.articleDelieverer.targetType == 'article'){ //帖子，详情页
					uni.navigateTo({
						url: '/pagesSubA/detail/detail?data=' + thisArticle.id
					});
					
				}
			},

			//收起
			closeSwipe() {
				this.$emit('modifySwipedId', -1);
			},
			unCollectArticle() {
				console.log('取消收藏文章');
				var that = this;
				// console.log(that.thisArticle.userId);
				// debugger;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userUncollect',
					data: {
						// Modifier: Yifei
						// Date: Aug 15, 2022
						// Description: 最好别让我知道是谁这么写的不然我杀了他
						// userId: that.userInfo.id,
						// articleId: that.thisArticle.id,
						// articleCreaterId: that.thisArticle.userId,
						userId: that.userInfo.id,
						targetType: 'ARTICLE',
						targetId: that.thisArticle.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						uni.$emit("refresh");
					}
				});
			},
		}
	};
</script>

<style>
	.oneArticle {
		width: 100%;
		position: relative;
	}

	.swipe-contain {
		position: relative;
		width: clac(100% - 32px);
		padding-left: 16px;
		padding-right: 16px;
		height: 121px;
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		margin-bottom: 20px;
		z-index: 10;
	}
	.swipe-contain-LA {
		padding-top: 3px;
		position: relative;
		width: clac(100% - 32px);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		margin-bottom: 20px;
		z-index: 10;
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
		right: 0;
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
		width: 48px;
		height: 100%;
		position: absolute;
		right: 0;
		top: 0;
		background: rgba(177, 177, 177, 1);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.menu-area image {
		margin: auto;
		display: block;
		width: 16px;
		height: 16px;
	}

	.menu-area text {
		margin: auto;
		left: 4px;
		width: 40px;
		word-wrap: break-word;
		font-size: 14px;
		color: #FFFFFF;
		display: block;
		text-align: center;
	}
</style>
