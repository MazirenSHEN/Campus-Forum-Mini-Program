/*
	#主页文章预览卡片
	#Author: Yaoyao
	#Update Log: Guetta 2020.6.6 - Update to UI v1.2.2 
	#Update Log: Guetta 2020.6.28 - Update to UI v1.3.0
*/

<template>
	<view class="articlecard" ref="articleCard" @click="goToDetail()">
		<!-- 用户信息行 -->
		<view class="userLine hor_center">
			<image :src="pathFilter(thisArticle.faceImg)" class="touxiang" @tap.stop="goToPersonPublic(thisArticle.userId)"></image>
			<view class="name">
				<view @tap.stop="goToPersonPublic(thisArticle.userId)">{{ thisArticle.nickname }}</view>
				<image v-if="thisArticle.authType == 2" src="../static/icon/auth_red.png" class="authLogo" ></image>
				<image v-if="thisArticle.authType == 1" src="../static/icon/auth_yellow.png" class="authLogo" ></image>
			</view>
			<view class="time" :style="timeLeft">{{ timeDeal(thisArticle.createDate) }}</view>
		</view>
		<!-- 标题 -->
		<view>
			<text v-if="!isNull(thisArticle.articleTitle)" selectable="true" class="title">{{ thisArticle.articleTitle }}</text>
			<!-- <view class="briefarticleCard">{{ thisArticle.articleContent }}</view> -->
		</view>
		<!-- 标签行 -->
		<view class="tagsLine">
			<view class="tag" :style="{ background: tagColorList[index] }" v-for="(i, index) in thisArticle.tagList" v-bind:key="index">{{ i }}</view>
		</view>
		<!-- 内容 -->
		<text selectable="true" class="briefarticleCard">{{ thisArticle.articleContent }}</text>
		<view :class="[thisArticle.imgList.length == 1 ? 'picturearea-one' : 'picturearea-mul']">
			<!-- *******这里是文章配图的位置*******-->

			<view @click.stop="goToDetail()">
				<!-- 下面两个 view 分别为蒙版背景层和数字层，都是由 margin-left = 67.5% 精准推至第三张图位置上的 -->
				<view v-if="thisArticle.imgList.length > 3" class="imgCoverText super_center">
					<view style="color: white;font-weight: 500;font-size: 17px;z-index: 20;">{{ thisArticle.imgList.length }}</view>
				</view>
				<view
					v-if="thisArticle.imgList.length > 3"
					class="imgCover"
				></view>
			</view>

			<!-- 宽高和 image 保持一致 -->
			<!-- 单图显示 -->
			<view style="width: 100%;max-height: 400upx;" v-if="thisArticle.imgList.length == 1">
				<!-- 高 ＞ 宽 普通 -->
				<view v-if="singleImgState == 0">
					<!-- :style在编译到微信小程序时，不会将'upx'编译为'rpx'，故 width 失效 -Guetta -->
					<image
						mode="aspectFit"
						:style="{ height: singleImgHeight + 'rpx', width: singleImgWidth + 'rpx' }"
						:src="pathFilter(thisArticle.imgList[0].imagePath)"
						@load="singleImgeFit"
						@tap.stop="previewImage(0)"
					></image>
				</view>
				
				<!-- 高 ＞ 宽 超长 -->
				<view v-else-if="singleImgState == 2" style="width: 100%;">
					<image
						mode="aspectFill"
						:style="{ height: singleImgHeight + 'rpx', width: singleImgWidth + 'rpx' }"
						:src="pathFilter(thisArticle.imgList[0].imagePath)"
						@load="singleImgeFit"
						@tap.stop="previewImage(0)"
					></image>
				</view>
				<!-- 宽 > 高 -->
				<view v-else style="width: 100%;">
					<image
						mode="aspectFit"
						style="margin-left: 10upx;"
						:style="{ height: singleImgHeight + 'rpx', width: singleImgWidth + 'rpx' }"
						:src="pathFilter(thisArticle.imgList[0].imagePath)"
						@load="singleImgeFit"
						@tap.stop="previewImage(0)"
					></image>
				</view>
			</view>
			<!-- 多图显示 -->

			<view style="width:30%;height: 200upx;margin-left: 2.5%;display: flex;background-color: #D1D1D1;" v-else v-for="(item, index) in imgList" :key="index">
				<image mode="aspectFill" style="height: 200upx" :src="pathFilter(item.imagePath)" @tap.stop="previewImage(index)"></image>
			</view>
		</view>
		<!-- 操作行 -->
		<view class="menubar">
			<view class="menubar_rel">
				<!-- 搞笑大赛 -->
				<!-- <image v-if="isfunCom" class="menubar_left" style="width: 75px;height: 36px;" src="../static/BG/funCom.png" mode="aspectFit"></image> -->
				<!-- 分享 -->
				<!-- <image class="menubar_left" style="width:18px;height:18px;" src="../static/icon/share-alt-353535.png" mode="aspectFit"></image> -->
				<!-- 评论和点赞 -->
				<view class="operationBar column_center">
					<nqCmt @click.native.stop="goToDetail()" :number="thisArticle.commentNum"></nqCmt>
					<nqLike style="margin-left: 11px;" @click.native.stop="swLikeArticle" :status="thisArticle.isLike" :number="thisArticle.likeNum"></nqLike>
				</view>
			</view> 
		</view>
	</view>
</template>

<script>
import nqLike from '../components/nq-button/nq-likeButton.vue';
import nqCmt from '../components/nq-button/nq-cmtButton.vue';


export default {
	name: 'articlebrief',
	props: {
		articleCard: {}
	},
	components:{
		nqLike,
		nqCmt
	},
	data() {
		return {
			serverUrl: this.$serverUrl,
			userInfo: this.getGlobalUserInfo(),
			singleImgState: '0',
			singleImgHeight: 0,
			singleImgWidth: 0,
			heightWidthRate: 0,
			imgList: [],
			thisArticle: this.articleCard, // 转为局部变量
			articleUser: '',
			tagColorList: [], // 储存每个tag的颜色
			timeLeft: '',
			isfunCom: false,//显示搞笑大赛图标
		};
	},

	created() {
		// queryUser()
		
		// console.log(this.thisArticle);
		if (this.thisArticle.imgList.length > 3) {
			// 只取前三
			for (var i = 0; i < 3; i++) {
				this.imgList.push(this.thisArticle.imgList[i]);
			}
		} else {
			this.imgList = this.thisArticle.imgList;
		}

		// 随机生成颜色
		if (!this.isNull(this.thisArticle.tagList)) {
			var tagColors = this.tagColors;
			for (var i = 0; i < this.thisArticle.tagList.length; i++) {
				var random = Math.floor(Math.random() * tagColors.length); // 0~tagColors.length-1
				this.tagColorList.push(tagColors[random]);
			}
		}
		
		// 捕获特殊标签做特殊处理
		// this.catchSpecialTag();

		uni.$on('updateArticle', article => {
			// from detail
			if (article.id == this.thisArticle.id) {
				console.log('get');
				this.thisArticle = article;
			}
		});
	},

	methods: {
		singleImgeFit(e) {
			var height = e.detail.height;
			var width = e.detail.width;
			var rate;
			// 纵向图片
			if (height >= width) {
				rate = width / height;
				// 一般纵向图片，宽/高 >= 1/6，singleImgState = 0
				if (rate >= 1/6){
					this.singleImgState = 0;
					this.singleImgHeight = 400;
					this.heightWidthRate = rate;
					this.singleImgWidth = 400 * rate;
				} 
				// 超长纵向图片，singleImgState = 2
				else {
					this.singleImgState = 2;
					this.singleImgHeight = 400;
					this.singleImgWidth = 240;
				}
				// console.log(this.singleImgState);
				// console.log(rate);
				// console.log(this.singleImgHeight);
				// console.log(this.singleImgWidth);
			} 
				// 横向图片，singleImgState = 1
			else {
				this.singleImgState = 1;
				this.singleImgWidth = 400;
				rate = height / width;
				this.heightWidthRate = rate;
				this.singleImgHeight = 400 * rate;
				// console.log(this.singleImgState);
				// console.log(rate);
				// console.log(this.singleImgHeight);
				// console.log(this.singleImgWidth);
			}
			// console.log(e.detail);
		},
		
		// 搞笑大赛
		// catchSpecialTag(){
		// 	if (this.thisArticle.tagList != null) {
		// 		for (var tag of this.thisArticle.tagList) {
		// 			//搞笑大赛
		// 			if (tag == 'UNNC搞笑大赛'){
		// 				this.isfunCom = true;
		// 			}
		// 		}
		// 	}
		// },
		switchIsLike(){
			this.thisArticle.isLike = !this.thisArticle.isLike;
		},
		
		swLikeArticle() {
			// var userInfo = this.getGlobalUserInfo();
			// if(this.isNull(userInfo.email)){
			// 	uni.showToast({
			// 		icon:'error',
			// 		title:'未绑定邮箱'
			// 	})
			// }else{
			// 	if (this.thisArticle.isLike) {
			// 		this.unLikeArticle();
			// 	} else {
			// 		this.likeArticle();
			// 	}
			// }
			if (this.thisArticle.isLike) {
				this.unLikeArticle();
			} else {
				this.likeArticle();
			}
		},

		likeArticle() {
			console.log('点赞文章');
			var that = this;
			uni.request({
				method: 'POST',
				url: that.$serverUrl + '/social/userLike',
				data: {
					userId: that.userInfo.id,
					targetType: "ARTICLE",
					targetId: that.thisArticle.id,
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					if (res.data.status == 200){
						this.switchIsLike();
						this.thisArticle.likeNum++;
					}
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
					userId: that.userInfo.id,
					targetType: "ARTICLE",
					targetId: that.thisArticle.id,
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					if (res.data.status == 200){
						this.switchIsLike();
						this.thisArticle.likeNum--;
					}
				}
			});
		},
		goToDetail() {
			// var encodeData = encodeURIComponent(JSON.stringify(this.thisArticle)); // 对数据字符串化并转码，防止特殊字符影响传参
			//传入跳转文章id和是否为搞笑大赛文章
			uni.navigateTo({
				url: '/pagesSubA/detail/detail?data=' + this.thisArticle.id
			});
		},
		goToPersonPublic(userId) {
			uni.navigateTo({
				url: '/pagesSubA/personpublic/personpublic?userId=' + userId
			});
		},

		previewImage: function(index) {
			var imgIndex = index;
			// console.log(res)
			// 获取全部图片路径
			var imgList = this.thisArticle.imgList;
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

		buttomCaculation(timeWidth) {
			var bottmWidth = this.$refs.articleCard.offsetWidth;
			console.log(bottmWidth);
		},
		
		queryUser(){
			console.log('query user');
			var that = this;
			uni.request({
				method:'POST',
				url:that.$serverUrl + '/queryUser',
				data:{
					// userId = that.thisArticle.userId,
					userId: that.thisArticle.userId,
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					console.log(res);
					if (res.data.status == 200){
						
					}
				}
			})
		}
	}
};
</script>

<style>
image {
	border: none;
	outline: none;
	max-height: 360upx;
	/* margin: auto; */
}
/* image {
		
	} */
</style>
<style scoped>
.articlecard {
	width: 100%;
	border-radius: 8px;
	margin: 3.47% auto 0;
	background-color: rgba(255, 255, 255, 1);
	/* box-shadow: 0px 0px 7px rgba(0, 0, 0, 0.16); */
	opacity: 1;
	border-bottom: 1px solid rgba(236,236,236,1);
}

.title {
	width: calc(100% - 34px);
	font-size: 17px;
	font-family: Source Han Sans CN;
	line-height: 24px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
	font-weight: 550;
	margin: 8px 17px 10px 17px;
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2;
	overflow: hidden;
}

.briefarticleCard {
	width: calc(100% - 34px);
	font-size: 14px;
	font-family: Source Han Sans CN;
	opacity: 1;
	font-weight: 400;
	margin: 12px 17px 12px 17px;
	color: rgba(53, 53, 53, 1);
	line-height:22px;
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 15;
	overflow: hidden;
}

.tagsLine {
	width: calc(100% - 34px);
	margin-left: 17px;
	margin-top: 12px;
}

.tag {
	display: inline-block;
	border-radius: 20px;
	color: #40a792;
	font-size: 11px;
	height: 11px;
	line-height: 11px;
	padding: 6px 10px;
	background: #621e81;
	vertical-align: middle;
	margin-right: 9px;
	color: #ffffff;
}

.tag-empty {
	margin-left: 10px;
	height: 15px;
	width: auto;
	background-color: white;
}

.userLine {
	position: relative;
	width: 100%;
	height: 32px;
	margin-top: 16px;
}

.touxiang {
	position: absolute;
	/* left: 4.53%; */
	left: 17px;
	border-radius: 32px;
	width: 32px;
	height: 32px;
	vertical-align: middle;
}
/* .touxiang::after{
		content: "";
		position: absolute;
		height:30px;
		width:30px;
		left:-5px;
		top:0;
	} */

.name {
	display: flex;
	position: absolute;
	left: 57px;
	top: 0;
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:500;
	line-height:19px;
	color:rgba(53,53,53,1);
	opacity:1;
	/* max-width: 24%; */
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.time {
	position: absolute;
	left: 57px;
	top: 20px;
	height:12px;
	font-size:12px;
	font-family:Source Han Sans CN;
	font-weight:400;
	line-height:12px;
	color:rgba(155,155,155,1);
	opacity:1;
}

.menubar {
	height: 36px;
	width: 93.12%;
	margin: 16px 0 0 3.44%;
}

.menubar_rel {
	position: relative;
	width: 100%;
	height: 100%;
}

.menubar_left{
	position: absolute;
	left: 0;
	bottom: 12px;
}

.operationBar{
	position: absolute;
	right: 0;
	min-width: 94px;
	display: flex;
	height: 18px;
}

.commentBar{
	height: 100%;
}

.commentBar image{
	width:13px;
	height:12px;
	margin-right: 8px;
}

.commentBar text{
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:400;
	line-height:23px;
	color:rgba(53,53,53,1);
}

.picturearea-one {
	/* margin: auto; */
	display: flex;
	width: 95.6%;
	margin-left: 2.2%;
}

.picturearea-mul {
	position: relative;
	margin: auto;
	display: flex;
	/* 在此设置图片区域宽度 */
	width: 95.6%;
	margin-left: 2.2%;
}

.imgCoverText{
	right: 14px;
	bottom: 4px;
	margin-top: ;
	position: absolute;
	width: 28px;
	height: 28px;
	border-radius: 100%;
}

.imgCover{
	right: 14px;
	bottom: 4px;
	position: absolute;
	width: 28px;
	height: 28px;
	border-radius: 100%;
	background-color: rgba(74,74,74,1);
	opacity: 0.5;
	z-index: 10;
}
.authLogo {
	width: 17px;
	height: 17px;
	margin-left: 5px;
}

</style>
