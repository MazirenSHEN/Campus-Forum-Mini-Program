<template>
	<view  style="width:100%;">
		<view class="pics">
<!-- 			<image src="../../static/icon/1.png"></image>
			<image src="../../static/icon/2.png"></image>
			<image src="../../static/icon/3.png"></image>
			<image src="../../static/icon/4.png"></image>
			<image src="../../static/icon/5.png"></image>
			<image src="../../static/icon/6.png"></image>
			<image src="../../static/icon/7.png"></image>
			<image src="../../static/icon/8.png"></image>
			<image src="../../static/icon/9.png"></image>
			<image src="../../static/icon/10.png"></image> -->
		</view>	
		<block v-for="(thisArticle,index) in myArticleList" :key="thisArticle.id">
			<view class="oneArticle" @click="goToDetail(thisArticle)">
				<image class="index" :src="'../../static/icon/' + (index+1) + '.png'"></image>
				<view v-if="!isNull(thisArticle.articleTitle)" class="title"> {{ thisArticle.articleTitle }}</view>
				<view v-else style="padding: 20px;"></view>
				<view class="cardBody">
					<view class="left-body" :class="{ leftBodyWithPic : thisArticle.imgList.length!=0 }">
						<view class="content">{{ thisArticle.articleContent }}</view>
						<view class="bottomBar">
							<view class="time">{{timeDeal(thisArticle.createDate)}}</view>
							<view class="comment">
								<image src="../../static/icon/comment-alt-353535.png"></image>
								<view> {{thisArticle.commentNum}}</view>
							</view>
							<view class="like">
								<image v-if="!thisArticle.isLike"  src="../../static/icon/like.png" ></image>
								<image v-if="thisArticle.isLike"  src="../../static/icon/liked-red.png" ></image>
								<view>{{thisArticle.likeNum}}</view>
							</view>
							<!-- <view class="view">
								<image src="../../static/icon/eye-888888.png"></image>
								<view>{{thisArticle.viewNum}}</view>
							</view> -->
						</view>
					</view>
					<view class="picArea" v-if="thisArticle.imgList.length">
						<image :src="resServerUrl + thisArticle.imgList[0].imagePath" mode="aspectFill"></image>
					</view>	
				</view>	
			</view>
		</block>
	</view>
</template>

<script>
	export default {
		props: {
			/*thisArticle:{}*/
			myArticleList: {}
		},
		data() {
			return {
				resServerUrl: this.$resServerUrl,
			};
		},
	
		methods:{
				goToDetail:function(thisArticle){
					//thisArticle用函数传入，因为v-for使用了ID为键名，所以无法筛选数据，就直接block传进来好了
					uni.navigateTo({
						url: '/pagesSubA/detail/detail?data=' + thisArticle.id
					});
				},
			
		}
}
</script>

<style>
	.pics{
		display: none;
	}
	.oneArticle {
		width: 98%;
		height: 136.4px;
		position:relative;
		box-shadow:0px 0px 4px rgba(0,0,0,0.16);
		opacity:1;
		border-radius:8px;
		margin-bottom: 8px;
		margin-left: auto;
		margin-right: auto;
		
}
.index {
	width: 24px;
	height: 58.78px;
	position:absolute;
	
	left:16px;
	
}
	.title {
		/*font-size: 15px;
		color: #4A4A4A;
		margin-top: 10px;
		padding-top: 10px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;*/
		font-weight: 550;
		font-size: 17px;
		margin-left: 17px;
		color: #4a4a4a;
		padding-top: 19px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		font-weight: 600;
	}

	.cardBody {
		/*position: relative;
		width: 100%;
		/* height:83px; */
		/*padding-top:10px;*/
		position: relative;
		height:83px; 
		padding-top: 12px;
		margin-left: 16px;
		margin-right: 16px;
		font-weight: 400;
	}

	.picArea {
		/*position: absolute;
		right: 10px;
		top: -10px;
		width: 61px;
		height: 61px;*/
		display: inline-block;
		width: 61px;
		height: 61px;
		position: absolute;
		top: 12px;
		right: 4px;
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
	}

	.content {
		/*height: 40px;
		width: 70%;
		font-size: 12px;
		line-height: 13px;
		overflow: hidden;
		text-overflow: ellipsis;*/
		height: 39px;
		width: 100%;
		font-size: 12px;
		line-height: 13px;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.bottomBar {
		/*width: 100%;
		height: 15px;
		margin-bottom:15px;
		color:#9B9B9B;*/
		width: 100%;
		height: 10px;
		padding-top: 12px;
		padding-bottom: 12px;
		position: relative;
		color: #9b9b9b;
	}

	.time {
		/*width: 85px;
		/* 暂时,到  月-日 时:分*/
		/*height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 5px;
		bottom: 12px;
		width: 77px;
		/* 暂时,到  月-日 时:分*/
		height: 10px;
		line-height: 10px;
		font-size: 10px;
		position: absolute;
		left: 0;
		bottom: 12px;
	}

	.comment {
		/*position: absolute;
		bottom: 12px; /* bottom, 对于comment time like view ,数值应等于menu-area的padding-bottom: ; */
		/*right: 40px;
		width: 25px;
		height: 10px;
		line-height: 10px;*/
		position: absolute;
		bottom: 12px; /* bottom, 对于comment time like view ,数值应等于menu-area的padding-bottom: ; */
		right: 40px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}

	.like {
		/*position: absolute;
		bottom: 12px;
		right: 1px;
		width: 25px;
		height: 10px;
		line-height: 10px;*/
		position: absolute;
		bottom: 12px;
		right: 1px;
		width: 25px;
		height: 10px;
		line-height: 10px;
	}
    .view {
    	/*position: absolute;
    	bottom: 12px;
    	right: 79px;
    	width: 25px;
    	height: 11px;*/
		position: absolute;
		bottom: 12px;
		right: 79px;
		width: 25px;
		height: 11px;
    }
	.comment image,.like image, .view image {
		width: 11px;
		height: 11px;
		right:14px;
		position: absolute;
	
	}

	.like view,.comment view,.view view {
		position: absolute;
		right: 0;
		bottom: 0;
		font-size: 8px;
		width: 9px;
		display: inline-block;
	}

</style>
