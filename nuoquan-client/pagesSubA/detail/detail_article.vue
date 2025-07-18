<template>
	<view>
		<view style="margin-top: 14px;display: flex;flex-direction: column;">
			<!--作者信息，头像名字时间-->
			<view class="author-info-bar">
				<image :src="pathFilter(articleCard.faceImg)" class="touxiang" @click="goToPersonPublic()"></image>
				<view class="name">
					<text selectable="true" @tap="goToPersonPublic()">{{ articleCard.nickname }}</text>
					<image v-if="articleCard.authType == 2" src="../../static/icon/auth_red.png" style="width: 15px;height: 15px;margin-left: 5px;"></image>
					<image v-if="articleCard.authType == 1" src="../../static/icon/auth_yellow.png" style="width: 15px;height: 15px;margin-left: 5px;"></image>
				</view>
				<view class="time">{{ timeDeal(articleCard.createDate) }}</view>
			</view>
			<!--标题-->
			<text v-if="!isNull(articleCard.articleTitle)" selectable="true" class="detail-title" :style="{width : '100%'}">{{ articleCard.articleTitle }}</text>
			<text v-else style="padding: 15px;"></text>
			<!-- <view class="detail-title" :style="{width : isfunCom ? 'calc(100%-75px)' : '100%'}">{{ articleCard.articleTitle }}</view> -->
			<!-- 搞笑大赛 -->
			<!-- <image v-if="isfunCom" src="../../static/BG/funCom.png" mode="aspectFit" 
				style="width: 75px;height: 36px;margin-top: 10px;">
			</image> -->
		</view>

		<!--标签-->
		<view class="detail-tags">
			<view class="tag" v-if="articleCard.tagList.length != 0 && articleCard.tagList != null" :style="{ background: tagColorList[index] }" v-for="(i, index) in articleCard.tagList" v-bind:key="index">{{ i }}</view>
		</view>
		<!--内容-->
		<text selectable="true" class="detailcontent" style="user-select: text;">{{ articleCard.articleContent }}</text>
		<!--图片区域-->
		<view>
			<!-- 单图显示 -->
			<view v-if="articleCard.imgList.length == 1" class="detailpics" style="width:100%;height:124px;display: flex;margin-left: 0;">
				<image
					v-for="(i, index) in articleCard.imgList"
					:key="index"
					:src="pathFilter(i.imagePath)"
					mode="aspectFill"
					:style="{ width: singleImgWidth + 'px', height: '124px' }"
					@tap="previewImg(index)"
					@longpress="aboutImg(index)"
					@load="singleImgeFit"
				></image>
			</view>
			<!-- 其他数量 -->
			<view v-else-if="articleCard.imgList.length == 4" class="detailpics" style="max-width: 508upx;margin-left: 0;">
				<image
				style="width: 248upx;height:248upx;"
					class="detailpic"
					v-for="(i, index) in articleCard.imgList"
					:key="index"
					:src="pathFilter(i.imagePath)"
					mode="aspectFill"
					@tap="previewImg(index)"
					@longpress="aboutImg(index)"
				></image>
			</view>

			<view v-else class="detailpics">
				<image
					class="detailpic"
					v-for="(i, index) in articleCard.imgList"
					:key="index"
					:src="pathFilter(i.imagePath)"
					mode="aspectFill"
					@tap="previewImg(index)"
					@longpress="aboutImg(index)"
				></image>
				<view v-if="articleCard.imgList.length == 2 || articleCard.imgList.length == 5 || articleCard.imgList.length == 8" style="width: 190upx;height: 190upx;margin: 6px 0;"></view>
			</view>
		</view>
<!-- 		<view class="menu-bar">
			<view :class="[articleCard.isLike ? 'liked' : 'like']"  @tap="swLikeArticle()">{{ articleCard.likeNum }}</view>
			<view class="comment" @tap="controlInputInDetailArticle">{{ articleCard.commentNum }}</view>
			<view class="menu-more"></view>
		</view> -->
		
		<!--4个ICON, 点赞评论分享返回-->
<!-- 		<view class="menu-bar">
			<view :class="[articleCard.isLike ? 'liked' : 'like']"  @tap="swLikeArticle()">{{ articleCard.likeNum }}</view>
			<view class="comment" @tap="controlInputInDetailArticle">{{ articleCard.commentNum }}</view>
			<view class="share" @tap="toggleShare()"></view>
			<view class="back" @tap="menu_back()"></view>
		</view> -->

		<!-- <view v-if="share"><mySharePoster :articleCard="articleCard" @unShow="toggleShare"></mySharePoster></view> -->
	</view>
</template>

<script>
// import mySharePoster from 'components/shareposter/myshareposter.vue';

export default {
	name: 'detail_1_article',
	props: {
		articleCard: '',
		userInfo: '',
	},
	components: {
		// mySharePoster
	},
	data() {
		return {
			serverUrl: this.$serverUrl,
			singleImgWidth: '', //一图调整宽度
			tagColorList: [],
			share: false ,// 隐藏/显示share画布
			// isfunCom: false, //搞笑大赛
		};
	},
	
	watch: {
		articleCard(newVal, oldVal){
			// 随机生成颜色
			if (this.articleCard.tagList != null) {
				var tagColors = this.tagColors;
				for (var i = 0; i < this.articleCard.tagList.length; i++) {
					var random = Math.floor(Math.random() * tagColors.length); // 0~tagColors.length-1
					this.tagColorList.push(tagColors[random]);
				}
			}
			
			// 捕获特殊标签做特殊处理
			// 为 compaign 等预留图标
			// this.catchSpecialTag();
		}
	},

	methods: {
		/**
		 * 捕获特殊标签做特殊处理
		 */
		// catchSpecialTag(){
		// 	if (this.articleCard.tagList != null) {
		// 		for (var tag of this.articleCard.tagList) {
		// 			//搞笑大赛
		// 			if (tag == 'UNNC搞笑大赛'){
		// 				this.isfunCom = true;
		// 			}
		// 		}
		// 	}
		// },
		
		
		singleImgeFit(e) {
			var height = e.detail.height;
			var width = e.detail.width;
			var rateWith = (124 * width) / height;
			if (rateWith <= 186) {
				this.singleImgWidth = rateWith;
			} else {
				this.singleImgWidth = 186;
			}
			// console.log(e.detail);
		},
		goToPersonPublic() {
			uni.navigateTo({
				url: '../personpublic/personpublic?userId=' + this.articleCard.userId
			});
		},
		

		previewImg: function(index) {
			var imgIndex = index;
			// 获取全部图片路径
			var imgList = this.articleCard.imgList;
			var arr = [];
			var path;
			for (var i = 0; i < imgList.length; i++) {
				// console.log(imgList[i].imagePath);
				path = this.pathFilter(imgList[i].imagePath);
				arr = arr.concat(path);
			}
			// console.log(arr);

			uni.previewImage({
				current: index,
				urls: arr
			});
		},

		aboutImg: function(index) {
			var that = this;
			console.log(this.articleCard.imgList[index].imagePath);
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
							url: that.pathFilter(that.articleCard.imgList[index].imagePath),
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

	} //method
};
</script>

<style>
.detail-title {
	/* width: calc(100% - 75px); */
	color: #4a4a4a;
	font-size: 17px;
	line-height: 21px;
	/* margin: auto; */
	font-weight: 550;
	word-break: break-all;
	word-wrap: break-word;
	margin-top: 16px;
	margin-bottom: 12px;
	max-height: 42px;
	text-align: justify;
}

.author-info-bar {
	height: 32px;
	width: 100%;
	position: relative;
}

.touxiang {
	width: 32px;
	height: 32px;
	border-radius: 12px;
}

.name {
	display: flex;
	font-size: 14px;
	/* height: 16px; */
	position: absolute;
	top: 2px;
	left: 40px;
	min-width: 96px;
	font-weight:500;
	height: 16px;
	line-height:16px;
	color:rgba(53,53,53,1);
}

.time {
	position: absolute;
	left:40px;
	bottom:0px;
	text-align: left;
	font-size: 12px;
	color: #9b9b9b;
	line-height: 14px;
	width: 102px;
}

.detail-tags {
	max-height: 35px;
	line-height: 15px;
	width: 100%;
}

.tag {
	display: inline-block;
	border-radius: 20px;
	font-size: 11px;
	height: 11px;
	line-height: 11px;
	padding: 6px 10px;
	background: #621e81;
	vertical-align: middle;
	margin-right: 9px;
	color: #ffffff;
	margin-bottom: 10px;
}

.detailcontent {
	word-break: break-all;
	word-wrap: break-word;
	white-space: pre-line;
	font-size: 14px;
	line-height: 20px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 18px;
	color: rgba(53, 53, 53, 1);
	margin-bottom: 10px;
}

.detailpics {
	display: flex;
	justify-content: space-between;
	flex-wrap: wrap;
	flex: 0 0 auto;
	align-items: center;
	flex-wrap: wrap;
	width: calc(648upx + 12px);
	margin: 0 auto;
	margin-bottom: 9px;
}

.detailpic {
	/* 			width: calc((100% - 12px) / 3);*/
	width: 216upx;
	height: 216upx;
	margin: 6px 0;
}
/* 
.menu-bar {
	height: 68px;
	width: calc(176px + 144upx);
	margin: auto;
	display: flex;
	justify-content: space-between;
	padding-bottom: 3px;
}

.comment,
.like,
.share,
.back {
	width: 12px;
	height: 11px;
	font-size: 11px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 11px;
	text-align: center;
	color: #353535;
	padding: 26px 16px 7px;
	border-radius: 22px;
	background: #f6e3ba;
	position: relative;
}
.liked {
	background: #fe5f55;

	width: 12px;
	height: 11px;
	font-size: 11px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 11px;
	text-align: center;
	color: #FFFFFF;
	padding: 26px 16px 7px;
	border-radius: 22px;
	position: relative;
}
.like::after {
	content: '';
	width: 16px;
	height: 16px;
	background: url(../../static/icon/heart_353535.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left: 14px;
	top: 8px;
}
.liked::after {
	content: '';
	width: 16px;
	height: 16px;
	background: url(../../static/icon/heart_ffffff.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left: 14px;
	top: 8px;
}
.comment::after {
	content: '';
	width: 16px;
	height: 16px;
	background: url(../../static/icon/comment-alt-353535.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left: 14px;
	top: 8px;
}
.share::after {
	content: '';
	width: 16px;
	height: 16px;
	background: url(../../static/icon/share-alt-353535.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left: 14px;
	top: 14px;
}
.back::after {
	content: '';
	width: 12px;
	height: 12px;
	background: url(../../static/icon/angle-left-353535.png);
	background-repeat: no-repeat;
	background-size: contain;
	position: absolute;
	left: 16px;
	top: 16px;
} */


</style>
