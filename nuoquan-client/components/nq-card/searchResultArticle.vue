<template>
	<view class="articlecard" id="'+articleCard.id+'" @click="goToDetail()" hover-class="hoverColor">
		<!-- <view class="title" v-html="$markdownParse.parse(articleCard.articleTitle)"></view> -->
		<view v-if="!isNull(articleCard.articleTitle)" class="title" v-html="articleCard.articleTitle"></view>
		<view v-else class="title" style="padding: 15px;"></view>
		
		<view class="picturearea" v-if="articleCard.imgList.length">
			<image :src="serverUrl + articleCard.imgList[0].imagePath"></image>
			<view class="picNum" v-if="articleCard.imgList.length > 1">{{articleCard.imgList.length}}</view>
		</view>
		
		<view class="right-body" :class="{ rightBodyWithPic: articleCard.imgList.length != 0 }">
			<view class="briefarticleCard" v-html="articleCard.articleContent"></view>
			<view class="menubar">
				<image :src="pathFilter(articleCard.faceImg)" class="touxiang"></image>
				<view class="name">{{ articleCard.nickname }}</view>
				<image v-if="articleCard.authType == 2" src="../../static/icon/auth_red.png" style="width: 18px; height: 18px;z-index: 1000;margin-left: 3px;margin-top: 2px;"></image>
				<image v-else-if="articleCard.authType == 1" src="../../static/icon/auth_yellow.png" style="width: 18px; height: 18px;z-index: 1000;margin-left: 3px;margin-top: 2px;"></image>
				<view class="time">{{ timeDeal(articleCard.createDate) }}</view>
			</view>	
		</view>

		<!-- 		<view class="tags">
			<view class="tag" :style="{background: tagColorList[index]}" v-for="(i, index) in articleCard.tagList" v-bind:key="index">{{ i}}</view>
		</view> -->

		<view style="margin:10px 0 0 0;border-top:1px solid #DCDCDC;"></view>
	</view>
</template>

<script>
	export default {
		name: 'aticlebrief',
		props: {
			articleCard: {}
		},
		data() {
			return {
				serverUrl: this.$resServerUrl,
				tagColorList: [],
			};
		},
		created() {
			// 随机生成颜色
			if (!this.isNull(this.articleCard.tagList)) {
				var tagColors = this.tagColors;
				for (var i = 0; i < this.articleCard.tagList.length; i++) {
					var random = Math.floor(Math.random() * tagColors.length);
					this.tagColorList.push(tagColors[random]);
				}
			}
		},

		methods: {
			goToDetail() {
				uni.navigateTo({
					url: '/pagesSubA/detail/detail?data=' + this.articleCard.id
				});
			}
		},
	};
</script>

<style>
	image {
		border: none;
		outline: none;
	}
</style>
<style>
	.articlecard {
		border-radius: 8px;
		margin: 0 15px;
	}

	.title {
		margin: 0px;
		font-size: 17px;
		max-height: 38px;
		font: MicrosoftYaHei;
		font-weight: bold;
		line-height: 19px;
		padding-top: 10px;
		padding-bottom: 12px;
		word-break: break-all;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.briefarticleCard {
		height: 33px;
		margin: 0;
		font-size: 14px;
		line-height: 17px;
		margin-bottom: 8px;
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;

	}

	.tags {
		margin-left: 21px;
	}

	.tag {
		display: inline-block;
		border-radius: 4px;
		padding-left: 5px;
		padding-right: 5px;
		margin-left: 5px;
		height: 15px;
		color: #ffffff;
		font-size: 10px;
		background: #621E81;
	}

	.menubar {
		display: flex;
		height:20px;
		position: relative;
		vertical-align: middle;
		margin: 0;
	}

	.touxiang {
		/* position: absolute; */
		left:0;
		bottom: 0;
		border-radius: 30px;
		width: 20px;
		height: 20px;
		margin-right: 5px;
		vertical-align: bottom;
	}

	.name {
		/* position: absolute; */
		left:28px;
		height:20px;
		line-height: 20px;
		display: inline-block;
		font-size: 10px;
		margin-left: 7px;
		color: #888888;
		padding-bottom: 5px;
		max-width: 85px;
		text-overflow: ellipsis;
	}

	.time {
		position: absolute;
		right: 0;
		bottom: 0px;
		font-size: 10px;
		height:20px;
		line-height: 20px;;
		color: #888888;
		max-width: 84px;
		text-overflow: ellipsis;
	}

	.picturearea {
		width: 61px;
		height: 61px;
		margin: auto;
		display: inline-flex;
		justify-content: center;
		position: relative;
		vertical-align: top;
	}
	.picNum{
		position: absolute;
		right: 4px;
		bottom: 4px;
		width:24px;
		height:24px;
		background:rgba(53,53,53,1);
		border-radius:50%;
		opacity:0.85;
		font-size:14px;
		line-height:24px;
		color:rgba(255,255,255,1);
		opacity:1;		
		text-align: center;
	}
	.picturearea image {
	position: absolute;
	right: 0;
	top: 0;
	width: 61px;
	height: 61px;
}

.rightBodyWithPic{
	vertical-align: top;
	display: inline-block;
	width:calc(100% - 69px);
	margin-left: 8px;
	height:61px;
}

</style>
