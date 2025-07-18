<!-- 
	Author: Cora
	Last Update: 2020年8月26日15:30
	Intro：组件345
	备注：目前存在问题已全部修改 (目前??? @jerrio)
-->
<template>
	<view class="longArticleCard" ref="articleList" @click="goToLongArticleDetail()">
		<!-- 标题行 -->
		<view v-if="thisLongArticle.title !== null" class="topLine">
			<view class="titleLine">
				{{ thisLongArticle.title }}
			</view>
			<!-- 时间戳，调用全局方法 timeDeal() -->
			<view class="postTime">
				{{lang.postTimeDeal}}{{ timeDeal(thisLongArticle.createDate) }}
			</view>
		</view>



		<!-- 文章概览行 -->
		<view class="midLine" :style="{'margin-top' : thisLongArticle.title == null ? 21 + 'px' : 7 + 'px'}">
			<!--有封面，则只看封面-->
			<view v-if="longArticle.coverImage !== null && longArticle.coverImage !== ''">
				<view  class="singleImg">
					<image :src="pathFilter(thisLongArticle.coverImage)" mode="aspectFill" style="{width: screenWidth - 32 + 'px'}">
					</image>
				</view>
			</view>
			<!--没有封面，则接着判断-->
			<view v-else>
				<!--没有封面 有视频，则显示视频-->
				<view v-if="parsedContent.includeVideo.status == true" class="singleVideo">
					<video :src="pathFilter(parsedContent.includeVideo.url)" >
					</video>
				</view>
				<!-- 无封面 无视频， 则继续判断 -->
				<view v-else >
					<!-- 只剩大文本处理结果， 有文字-->
					<view v-if="parsedContent.withoutMedia !== '' " class="txtAndImg">
						<!-- 有无图片都保持一样的高度 -->
						<!-- （1）有文字 + content有图片 -->
						<text :style="{width: parsedContent.firstImage.status ? 'calc(100% - 94px)' : '100%'}">{{ parsedContent.withoutMedia }}</text>
						<!-- （2）有文字 -->
						<image v-if="parsedContent.firstImage.status" :src="pathFilter(parsedContent.firstImage.url)" mode="aspectFill"></image>
					</view>
					<!-- 无文字（3）有图片 -->
					<view v-else class="singleImg">
						<image :src="pathFilter(parsedContent.firstImage.url)" mode="aspectFill" style="{width: screenWidth - 32 + 'px'}">
						</image>
					</view>
				</view>

			</view>
			<!-- 无标题时时间戳 -->
			<view v-if="thisLongArticle.title == null" class="postTime">
				{{lang.postTimeDeal}}{{ timeDeal(thisLongArticle.createDate) }}
			</view>
		</view>







		<!-- 作者信息行 -->
		<view class="bottomLine column_center">
			<view class="userInfo-Box column_center">
				<!-- 头像，通过用户角色判断是否含有头像，临时账号无头像 | 真实账号有头像 -->
				<image @tap.stop="goToPersonPublic(longArticle.userId)" class="profileImg" v-if="thisLongArticle.userId != null"
				 :src="thisLongArticle.faceImg" mode="aspectFit"></image>
				<!-- 昵称 -->
				<text class="idText">{{ thisLongArticle.nickname }}</text>
			</view>
			<!-- 点赞、评论 -->
			<view class="likeAndCommentNum-Box">
				<view class="likeAndCommentNumb">
					<nqCmt @click.native.stop="goToLongArticleDetail()" :number="thisLongArticle.commentNum"></nqCmt>
					<nqLike style="margin-left: 11px;" @click.native.stop="swLikeLongArticle" :status="thisLongArticle.isLike" :number="thisLongArticle.likeNum"></nqLike>
				</view>
			</view>
		</view>
		<view class="gapLine">
		</view>
	</view>
</template>

<script>
	import nqLike from '../nq-button/nq-likeButton.vue';
	import nqCmt from '../nq-button/nq-cmtButton.vue';
	// import nq_parse from '@/pagesSubA/longArticleDetail/parse.js'
	import {
		mapState,
		mapMutations
	} from 'vuex';

	export default {
		data() {
			return {
				screenWidth: 375, //屏幕宽度
				thisLongArticle: this.longArticle, // 转换为局部变量
				parsedContent: '', // 解析后的内容
				serverUrl: this.$serverUrl,
				userInfo: this.getGlobalUserInfo(),
				displayMedia: { //决定是否有图片/视频，以及地址
					type: '',
					url: '',
				}
			};
		},
		props: {
			longArticle: {}
		},
		components: {
			nqLike,
			nqCmt
		},
		computed: {
			...mapState(['lang'])
		},
		created() {
			var that = this;
			uni.getSystemInfo({
				success(res) {
					that.screenWidth = res.screenWidth
				}
			})

			// 全局监听由 longArticleDetail 传来的文章内容 object，更新点赞评论数据 -Guetta 2020年9月8日04点34分
			uni.$on('updateLongArticle', dataFromLongArticleDetail => {
				// from longArticleDetail
				if (dataFromLongArticleDetail.id == this.thisLongArticle.id) {
					console.log('get');
					this.thisLongArticle = dataFromLongArticleDetail;
				}
			});

			// 解析由 votePage 传来的 thisLongArticle，将第一张图片用作无封面图卡片的封面图
			that.parseForCoverImg(that.thisLongArticle);
		},
		mounted() {
			// console.log(this.thisLongArticle);
			// this.parsedContent = nq_parse.parsePureText(this.thisLongArticle.content);
			//console.log(this.thisLongArticle.content);
			//console.log(this.parsedContent.firstImage.url);
			//debugger;

		},
		methods: {
			// 解析
			parseForCoverImg(content) {
				var contentAfter = '';
				// contentAfter = nq_parse.parseMedia(content.content).data;
				// console.log("contentAfter=" + contentAfter);
			},

			swLikeLongArticle() {
				if (this.thisLongArticle.isLike) {
					this.unLikeArticle();
					this.thisLongArticle.likeNum--;
				} else {
					this.likeArticle();
					this.thisLongArticle.likeNum++;
				}
				this.thisLongArticle.isLike = !this.thisLongArticle.isLike;
			},

			// TODO: 点赞和取消点赞的方法整合为一个方法 -Guetta 2020年9月8日02点35分
			likeArticle() {
				console.log('点赞文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userLike',
					data: {
						targetType: "LONGARTICLE",
						userId: that.userInfo.id,
						targetId: that.thisLongArticle.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
					}
				});
			},

			unLikeArticle() {
				console.log('取消点赞文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userUnLike',
					data: {
						targetType: "LONGARTICLE",
						userId: that.userInfo.id,
						targetId: that.thisLongArticle.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
					}
				});
			},

			// 跳转长文章详情页，传入文章 id 查
			goToLongArticleDetail() {
				var isJump = this.longArticle.isJump;
				if (isJump == 0) { //0，跳转详情
					uni.navigateTo({
						url: '../../pagesSubA/longArticleDetail/longArticleDetail?data=' + this.thisLongArticle.id,
					});
				} else if(isJump == 1){
					var encodeData = encodeURIComponent(this.thisLongArticle.link);
					uni.navigateTo({
						url: '/pages/adWebPage/adWebPage?url=' + encodeData,
					})
				}
			},
			goToPersonPublic(userId) {
				uni.navigateTo({
					url: '../../pagesSubA/personpublic/personpublic?userId=' + userId
				});
			},
		}
	}
</script>

<style>
	.longArticleCard {
		width: calc(100% - 32px);
		margin-left: 16px;
	}

	.topLine .midLine .bottomLine {
		margin-left: 14px;
		width: calc(100% - 28px);
	}

	.titleLine {
		width: 100%;
		margin-top: 16px;
		font-size: 16px;
		line-height: 24px;
		font-weight: 500;
		font-family: Source Han Sans CN;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
	}

	.postTime {
		width: 100%;
		margin-top: 6px;
		height: 15px;
		font-size: 10px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
		color: rgba(154, 154, 154, 1);
		opacity: 1;
	}

	.midLine {
		position: relative;
		width: 100%;
	}

	.txtAndImg {
		position: relative;
		display: flex;
		width: calc(100% - 22px);
		height: 52px;
		padding: 0px 11px 0px 11px;
	}

	.txtAndImg text {
		position: absolute;
		left: 0;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		line-height: 16px;
		max-height: 52px;
		-webkit-line-clamp: 3;
		overflow: hidden;
		font-size: 12px;
		color: rgba(74, 74, 74, 1);
		text-overflow: ellipsis;
		word-wrap: break-word;
	}

	.txtAndImg image {
		position: absolute;
		right: 0;
		width: 81px;
		height: 52px;
	}

	.singleImg .singleVideo {
		display: flex;
		width: 100%;
	}

	.singleImg image {
		position: relative;
		height: 200px;
		width: 100%;
	}

	.singleVideo video {
		position: relative;
		object-fit: scale-down;
		width: 100%;
		/* height: 200px; */
	}

	.bottomLine {
		position: relative;
		height: 29px;
		width: 100%;
		margin-top: 15px;
		margin-bottom: 21px;
	}

	.bottomLine .userInfo-Box {
		position: absolute;
		display: flex;
		left: 0;
	}

	.bottomLine .userInfo-Box .profileImg {
		width: 28px;
		height: 28px;
		border-radius: 50%;
		margin-right: 8px;
	}

	.bottomLine .userInfo-Box .idText {
		max-width: 160px;
		height: 18px;
		line-height: 18px;
		/* margin-top: 7px; */
		font-size: 12px;
		font-weight: 400;
		color: rgba(74, 74, 74, 1);
		opacity: 1;
		/* 保证文章正常显示 */
		word-wrap: break-word;
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
		overflow: hidden;
	}

	.bottomLine .likeAndCommentNum-Box {
		position: absolute;
		top: 7px;
		right: 0;
	}

	.bottomLine .likeAndCommentNum-Box .likeAndCommentNumb {
		position: absolute;
		display: flex;
		right: 0;
	}

	.gapLine {
		width: calc(100% - 18px);
		margin-left: 9px;
		border: 1px solid rgba(236, 236, 236, 1);
		opacity: 1;
	}
</style>
