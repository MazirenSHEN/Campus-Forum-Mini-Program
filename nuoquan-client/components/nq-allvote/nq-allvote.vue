<template>
	<view class="allvote_container">
		<view class="allvote_box">
			<!-- 顶部栏 -->
			<view class="allvote_topLine" :style="{ height: this.getnavbarHeight().bottom + 5 + 'px' }">
				<view class="navbarLine column_center"><text>所有投票</text></view>
			</view>
			<!-- 卡片循环 -->
			<scroll-view scroll-y="true" class="allvote_list">
				<view v-for="(item, index) in list" :key="index" class="oneVote">
					<view class="voteTitle">这是一个标题</view>
					<view class="endtime">三天后结束</view>
					<view class="participants_allvote">1231</view>
				</view>
			</scroll-view>
			<!-- 底部栏 -->
			<view class="allvote_bottomLine super_center"><image src="../../static/icon/angle-double-up.png" mode="aspectFit"></image></view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'nqallvote',
	props: {
		height: 0,
	},

	data(){
		return{
			list:[1,2,3,4,5]
		};
	},

	methods: {

	}
};
</script>

<style>
.allvote_container {
	position: fixed;
	top: 0;
	z-index: 50;
	height: 100%;
	width: 100%;
	border-bottom-left-radius: 15px;
	border-bottom-right-radius: 15px;
}

.allvote_box {
	width: 100%;
	border-bottom-left-radius: 15px;
	border-bottom-right-radius: 15px;
	background-color: #eea52a;
}

.allvote_topLine {
	position: relative;
	width: 100%;
}

.allvote_list {
	height: 241px;
	width: 100%;
}

.allvote_bottomLine {
	height: 44px;
	width: 100%;
}

.allvote_bottomLine image {
	width: 30px;
	height: 30px;
	opacity: 1;
}

.navbarLine {
	position: absolute;
	width: 100%;
	height: 38px;
	bottom: 0;
}

.navbarLine text {
	position: absolute;
	text-align: center;
	font-size: 17px;
	line-height: 23px;
	color: white;
	font-weight: 550;
	height: 23px;
	width: 100%;
	left: 0;
	right: 0;
	margin-left: auto;
	margin-right: auto;
	overflow: inherit;
}

.oneVote {
	background: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
	opacity: 1;
	width: 93.07%;
	position: relative;
	height: 67px;
	margin-top: 12px;
	margin-left: 3.47%;
	border-radius: 8px;
}

.voteTitle {
	position: absolute;
	width: 87.97%;
	top: 12px;
	left: 5.73%;
	height: 21px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 21px;
	color: rgba(53, 53, 53, 1);
	opacity: 1;
	/* 保证文章正常显示 */
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
	text-overflow: ellipsis;
	/**文字隐藏后添加省略号*/
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 1;
	overflow: hidden;
}

.endtime {
	position: absolute;
	bottom: 12px;
	left: 5.73%;
	min-width: 42px;
	height: 16px;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 16px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
}

.participants_allvote {
	position: absolute;
	bottom: 12px;
	right: 5.73%;
	min-width: 60px;
	height: 16px;
	font-size: 14px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 16px;
	color: rgba(155, 155, 155, 1);
	opacity: 1;
}
</style>
