<template>
	<view class="plus_view" :style="{top:topHeight + 'px'}" >
		<!-- 加号 -->
		<view class="ic super_center" @tap='controlShowPop()'>
		<!--
		Author:Yifei
		Date: July 6, 2022
		Description: 原来有发帖子和发文章两个功能，现在只留了发帖子功能，点击加号就不用让图标旋转并给出选项，直接进入发帖页面就行了
		 -->
		<!-- <image :class="[showPop ? 'midIcon' : 'midIconBack']" :style="{ width: '18px', height: '18px', transition: 'all 500ms linear 200ms', transform: 'rotate(' + degree + 'deg)' }" -->
		<image :class="[showPop ? 'midIcon' : 'midIconBack']" :style="{ width: '18px', height: '18px', transition: 'all 500ms linear 200ms'}"
		 src="../static/icon/plus_tab.png" mode="aspectFit" @tap="creatArticle('short')"></image>
		</view>
		<view v-show="degree==45" style="position: fixed;width: 100%;height:100%;top:0;left: 0;z-index: 30;" @tap="controlShowPop"></view> <!-- 透明底板 -->
		<!-- 展开的菜单 -->
		<!-- 
		Author:Yifei
		Date: July 6, 2022
		Description: 原来有发帖子和发文章两个功能，现在只留了发帖子功能，所以点击加号直接进入发帖页面就行不需要给选项了
		 -->
		<!-- <view v-show="degree == 45" class="popMenu" :style="{top:this.getnavbarHeight().bottom + 20 + 'px'}">
			<view class="super_center" @tap="creatArticle('short')"><text>{{lang.postArticle}}</text></view>
			<view style="height:1px;width:83px;background:rgba(236,236,236,1);margin-left:16px;"></view>
			<view class="super_center" @tap="creatArticle('long')"><text>{{lang.postLongArticle}}</text></view>
		</view> -->
	</view>
</template>
<script>
	import {
		mapState
	} from 'vuex';
	export default {
		props: {
			topHeight: "",
		},
		data() {
			return {
				showPop: false,
				degree: 0,
			}
		},
		computed: {
			...mapState(['lang'])
		},
		methods: {
			controlShowSearch(a) {
				// console.log('this is controlShowSearch, receive data will be appied');
				// console.log(a);
				this.showSearch = a;
			},
			controlShowPop() {
				this.degree = Math.abs(this.degree - 45);
				this.rotateStatus = !this.rotateStatus;
			},
			creatArticle(type){
				if(type == 'short'){
					uni.navigateTo({
					//新建帖子	
						url: '../../pages/submit/submit'
					})
				}else{
					uni.navigateTo({
						url: '../../pages/submit/submitLongArticle'
					})
				};
				this.controlShowPop();
			}
		}
	}
</script>
<style>
	.plus_view {
		/** 新建加号整体位置，基于上一级 .top-bar绝对定位**/
		position: absolute;
		right: 101px;
	}

	.ic {
		/**加号背景样式**/
		border-radius: 50%;
		text-align: center;
		width: 32px;
		height: 32px;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		opacity: 1;
		z-index: 40;
	}

	.midIcon {
		animation: midIcon 200ms linear 1;
	}

	/* 
      midIcon : 定义的动画名称
      1s : 动画时间
      linear : 动画以何种运行轨迹完成一个周期
      infinite :规定动画应该无限次播放
     */
	@keyframes midIcon {
		0% {
			-webkit-transform: rotate(0deg);
		}

		25% {
			-webkit-transform: rotate(11.25deg);
		}

		50% {
			-webkit-transform: rotate(22.5deg);
		}

		75% {
			-webkit-transform: rotate(33.75deg);
		}

		100% {
			-webkit-transform: rotate(45deg);
		}
	}

	.midIconBack {
		animation: midIconBack 200ms linear 1;
	}

	/* 
      midIcon : 定义的动画名称
      1s : 动画时间
      linear : 动画以何种运行轨迹完成一个周期
      infinite :规定动画应该无限次播放
     */
	@keyframes midIconBack {
		0% {
			-webkit-transform: rotate(45deg);
		}

		25% {
			-webkit-transform: rotate(33.75deg);
		}

		50% {
			-webkit-transform: rotate(22.5deg);
		}

		75% {
			-webkit-transform: rotate(11.25deg);
		}

		100% {
			-webkit-transform: rotate(0deg);
		}
	}

	/**新建菜单相关**/
	.popMenu {
		position: fixed;
		right: calc(101px + 16px - 115px/2);
		background: rgba(0, 0, 0, 0.5);
		/* width: 115px; */
		padding: 0 18px;
		height: 100px;
		display: flex;
		flex-direction: column;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
		border-radius: 12px;
		z-index: 50;
	}

	.popMenu view {
		height: 50%;
	}
	.angle_dec{
		position: absolute;
		
		width: 0;
		    height: 0;
		    border-width: 0 40px 40px;
		    border-style: solid;
		    border-color: transparent transparent red;
	}
</style>
