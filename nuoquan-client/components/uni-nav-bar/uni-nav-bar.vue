<template>
	<view class="uni-navbar" :style="{ height: height + 'px' }">
		<!-- 在这里修改navigationBar整体样式 -->
		<view
			:class="{ 'uni-navbar--fixed': fixed, 'uni-navbar--shadow': shadow }"
			:style="{ 'background-color': backgroundColor, height: height + 'px' }"
			class="uni-navbar__content"
		>
			<uni-status-bar v-if="statusBar" />
			<view :style="{ color: color, backgroundColor: backgroundColor }" class="navigationBar_Line">
				<!-- 左侧区域 -->
				<view v-if="showLeftIcon && !isNavHome" @tap="onClickLeft" style="height: 38px;" class="uni-navbar__header-btns uni-navbar__header-btns-left uni-navbar__content_view">
					<!-- icon -->
					<view class="uni-navbar__content_view"><image style="height: 14px;width: 24px;" src="../../static/icon/angle-left-353535.png" mode="aspectFit"></image></view>
					<!-- text -->
					<view :class="{ 'uni-navbar-btn-icon-left': !leftIcon.length }" class="uni-navbar-btn-text uni-navbar__content_view super_center" v-if="leftText.length">
						<text :style="{ color: color, fontSize: '14px' }">{{ leftText }}</text>
					</view>
					<slot name="left" />
				</view>
				<view v-else-if="showLeftIcon && isNavHome" @tap="goIndex" style="height: 38px;" class="uni-navbar__header-btns uni-navbar__header-btns-left uni-navbar__content_view">
					<image style="width: 24px;height: 24px;margin-left: 14px;" src="../../static/BG/tabbarBg/home.png" mode="aspectFit"></image>
				</view>
				<view v-else style="height: 38px;width: 160upx;"></view>
				<!-- 标题区域 -->
				<view class="uni-navbar__header-container uni-navbar__content_view" style="height: 38px;">
					<view class="uni-navbar__header-container-inner uni-navbar__content_view" v-if="title.length">
						<text class="uni-nav-bar-text" :style="{ color: color, 'font-weight': 550 }">{{ title }}</text>
					</view>
					<!-- 标题插槽 -->
					<slot />
				</view>
				<!-- 右侧区域 -->
				<view
					style="height: 38px;"
					:class="title.length ? 'uni-navbar__header-btns-right' : ''"
					@tap="onClickRight"
					class="uni-navbar__header-btns uni-navbar__content_view"
				>
					<view class="uni-navbar__content_view" v-if="rightIcon.length"><uni-icons :color="color" :type="rightIcon" size="24" /></view>
					<!-- 优先显示图标 -->
					<view class="uni-navbar-btn-text uni-navbar__content_view" v-if="rightText.length && !rightIcon.length">
						<text class="uni-nav-bar-right-text">{{ rightText }}</text>
					</view>
					<slot name="right" />
				</view>
			</view>
		</view>
		<view class="uni-navbar__placeholder" v-if="fixed">
			<uni-status-bar v-if="statusBar" />
			<view class="uni-navbar__placeholder-view" />
		</view>
	</view>
</template>

<script>
import uniStatusBar from '../uni-status-bar/uni-status-bar.vue';
import uniIcons from '../uni-icons/uni-icons.vue';

export default {
	name: 'UniNavBar',
	components: {
		uniStatusBar,
		uniIcons
	},
	props: {
		height: 0,
		showLeftIcon:{
			type: Boolean,
			default: true
		},
		title: {
			type: String,
			default: ''
		},
		leftText: {
			type: String,
			default: ''
		},
		rightText: {
			type: String,
			default: ''
		},
		isNavHome: {
			type: Boolean,
			default: false
		},
		rightIcon: {
			type: String,
			default: ''
		},
		fixed: {
			type: [Boolean, String],
			default: false
		},
		color: {
			type: String,
			default: 'black'
		},
		backgroundColor: {
			type: String,
			default: 'rgba(255,255,255,1)'
		},
		statusBar: {
			type: [Boolean, String],
			default: false
		},
		shadow: {
			type: [String, Boolean],
			default: false
		},
		border: {
			type: [String, Boolean],
			default: true
		}
	},
	mounted() {
		if (uni.report && this.title !== '') {
			uni.report('title', this.title);
		}
		console.log(this.isNavHome);
	},
	methods: {
		onClickLeft() {
			var current=getCurrentPages()
			console.log(current.length+"currentpage");
			if(current.length!=1){
				uni.navigateBack({
					delta: 1
				});
			}else{
				uni.reLaunch({
					url:'../../pages/tabPages/index'
				})
			}
		},
		goIndex(){
			getApp().globalData.isNavHome = false; 
			uni.reLaunch({
				url:'../../pages/tabPages/index'
			})
		},
		onClickRight() {
			this.$emit('clickRight');
		}
	}
};
</script>

<style lang="scss" scoped>
$nav-height: 44px;
.uni-nav-bar-text {
	/* #ifdef APP-PLUS */
	font-size: 17px;
	/* #endif */
	/* #ifndef APP-PLUS */
	font-size: 17px;
	/* #endif */
}
.uni-nav-bar-right-text {
	font-size: $uni-font-size-base;
}

.uni-navbar {
	width: 750rpx;
}

.uni-navbar__content {
	position: relative;
	width: 750rpx;
	background-color: $uni-bg-color;
	overflow: hidden;
	border-bottom:1px solid rgba(236,236,236,1);
	opacity:1;
}

.uni-navbar__content_view {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	align-items: center;
	flex-direction: row;
	height: 24px;
	// background-color: #FFFFFF;
}

.uni-navbar__header {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	width: 100%;
	height: $nav-height;
	line-height: $nav-height;
	font-size: 16px;
	// background-color: #ffffff;
}

.uni-navbar__header-btns {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-wrap: nowrap;
	width: 120rpx;
	justify-content: center;
	align-items: center;
}

.uni-navbar__header-btns-left {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	width: 160rpx;
	justify-content: flex-start;
}

.uni-navbar__header-btns-right {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	width: 160rpx;
	justify-content: flex-end;
}

.uni-navbar__header-container {
	flex: 1;
	margin-left: auto;
	margin-right: auto;
}

.uni-navbar__header-container-inner {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex: 1;
	align-items: center;
	justify-content: center;
	font-size: $uni-font-size-base;
}

.uni-navbar__placeholder-view {
	height: $nav-height;
}

.uni-navbar--fixed {
	position: fixed;
	z-index: 998;
}

.uni-navbar--shadow {
	/* #ifndef APP-NVUE */
	box-shadow: 0 1px 6px #ccc;
	/* #endif */
}

.uni-navbar--border {
	border-bottom-width: 1rpx;
	border-bottom-style: solid;
	border-bottom-color: $uni-border-color;
}
</style>
<style>
.navigationBar_Line {
	position: absolute;
	width: 100%;
	bottom: 0;
	height: 38px;
	display: flex;
}
</style>
