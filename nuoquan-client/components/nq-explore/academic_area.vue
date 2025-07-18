<!--
Description:  学术页面头部三块分割区域
Author: yaoyao
Date: 17 Aug, 2020
-->
<template>
	<view class="academic_area">
		<view class="left_card">
			<view class="card_title">校内课程讨论</view>
			<image class="card_image" src='../../static/AD/20200524AD1.jpg' mode="aspectFill"></image>
		</view>
		<view class="right_card">
			<view class="item">
				<view class="card_title">语言成绩</view>
				<image class="card_image" src='../../static/AD/20200524AD1.jpg' mode="aspectFill"></image>
			</view>
			<view class="item">
				<view class="card_title"> 申研准备</view>
				<image class="card_image" src='../../static/AD/20200524AD1.jpg' mode="aspectFill"></image>
			</view>
		</view>

	</view>
</template>
<script>
	export default {
		props: {

		},
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>
<style scoped>
	.academic_area {
		width: calc(100% - 26px);
		margin: auto;
		height: 159px;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.16);
		border-radius: 5px;
	}

	/* 左边卡片部分 */
	.academic_area .left_card {
		position: relative;
		display: inline-block;
		height: 138px;
		width: calc(49% - 20px);
		padding: 10px;
	}

	.left_card::after {
		/**中间竖线**/
		position: absolute;
		content: "";
		width: 1px;
		height: 151px;
		right: 0;
		top: 4px;
		background: #ECECEC;
	}

	.left_card .card_title {
		margin-bottom: 8px;
		display: inline-block;
		padding-right: 20px;
		position: relative;
		font-size: 18px;
		font-weight: bold;
		height: 18px;
		line-height: 18px;
		color: rgba(74, 74, 74, 1);
		max-width: calc(100% - 20px);
		overflow: hidden;
	}

	.left_card .card_image {
		width: 100%;
		height: 112px;
	}

	/* 右边卡片部分 */
	.academic_area .right_card {
		position: relative;
		display: inline-block;
		height: 100%;
		width: 51%;
		vertical-align: top;
	}

	.right_card::after {
		/**中间横线**/
		position: absolute;
		content: "";
		width: calc(100% - 8px);
		height: 1px;
		right: 4px;
		top: 50%;
		background: #ECECEC;
	}

	.right_card .item {
		display: flex;
		width: calc(100% - 20px);
		height: calc(50% - 28px);
		padding: 14px 10px;
		justify-content: space-between;
		align-items: center;
	}

	.right_card .card_title {
		padding-right: 20px;
		position: relative;
		display: inline-block;
		font-size: 18px;
		font-weight: bold;
		height: 18px;
		line-height: 18px;
		color: rgba(74, 74, 74, 1);
		max-width: calc(100% - 40px);
		overflow: hidden;
	}

	.right_card .card_image {
		width: 58px;
		height: 51px;
	}

	/**标题后箭头**/
	.card_title::after {
		content: "";
		position: absolute;
		right: 0;
		bottom: -1px;
		width: 20px;
		height: 20px;
		background-image: url(../../static/icon/angle-right-888888.png);
		background-repeat: no-repeat;
		background-size: 20px 20px;
		background-position: center;
	}
</style>
