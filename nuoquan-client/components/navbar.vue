<template name="navbar">
	<view class="navbarBox" :style="{height: height + 'px'}">
		<view class="nav-title">title</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			height: 0,
		};
	},
	methods: {
	
	}
};	
</script>

<style>
	.navbarBox{
		position: fixed;
		z-index: 9999;//最高层级
		width: 300px;
		height: 60px;
		background-color: green;
		top: 0;
	}
	
	.nav-title {
	  position: absolute;
	  text-align: center;
	  max-width: 400rpx;
	  overflow: hidden;
	  text-overflow: ellipsis;
	  white-space: nowrap;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0;
	  margin: auto;
	  font-size: 36rpx;
	  color: #2c2b2b;
	  font-weight: 600;
	}
</style>
