<!--
 * Description: 悬浮菜单组件
-->
<template>
	<view class="menu-content">
		<view class=" add_block_button" :class="{'menuOpen':openFlag}" @click="emitOpenFlag()" >
			+
		</view>
		<!-- <img class="menu icon"  :class="{'menuOpen':openFlag}" src="/static/hch-menu/menu.png"
		 alt="" srcset=""> -->
		<image class="icon" :class="{'open open1':openFlag}" src="../static/icon/text.png" @tap="selectType('text')">
		<image class="icon" :class="{'open open2':openFlag}" src="../static/icon/pic.png" @tap="selectType('image')"></image>
		<image class="icon" :class="{'open open3':openFlag}" src="../static/icon/video.png" @tap="selectType('video')"></image>
	</view>
</template>

<script>
	export default {
		props: {
			//fatherOpenFlag: false,
		},
		data() {
			return {
				openFlag:false, //是否展开菜单
			}
		},
		methods: {
			emitOpenFlag(){
				this.openFlag = !this.openFlag;
				this.$emit('selectionStart',this.openFlag);
			},
			selectType(type) {
				this.$emit('selectionFinish', type);
				this.emitOpenFlag();
			},
		}
	}
</script>

<style lang="scss" scoped>
	.menu-content {
		position: relative;
		width: 50px;
		height: 50px;
		margin: 30px auto;
		.add_block_button {
			width: 50px;
			height: 50px;
			background: rgba(252, 192, 65, 1);
			border-radius: 50%;
			text-align: center;
			color: #FFFFFF;
			font-size: 49px;
			line-height: 40px;
			position: absolute;
			z-index:10;
			transform: rotate(0deg);
			transition: all 0.5s ease-out 0s;
		}

		.icon {
			position: absolute;
			bottom: 0rpx;
			right: 0rpx;
			width: 50px;
			height: 50px;
			opacity: 0;
			transform: rotate(0deg);
			transition: all 0.5s ease-out 0s;
		}

		.menu {
			z-index: 1;
			opacity: 1;
		}

		.menuOpen {
			transform: rotate(180deg);
			animation-fill-mode: forwards;
			transition: all 0.5s ease-out 0s;
		}

		.open {
			opacity: 1;
			transform: rotate(360deg);
			animation-fill-mode: forwards;
			transition: all 0.5s ease-out 0s;
		}

		.open1 {
			left:-60px;
			bottom:-60px;
		}

		.open2 {
			left:0px;
			bottom:-60px;
		}

		.open3 {
			right: -60px;
			bottom: -60px;
		}
	}
</style>
