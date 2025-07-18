<template>
	<view class="form" hover-class="hoverColor">
		<view class="form-fold" @click="control">
			<view class="title">{{obj.name}}</view>
			<image hover-class="hoverColor" v-if="contentShow" src="../static/icon/angle-down.png"></image>
			<image hover-class="hoverColor" v-if="!contentShow" src="../static/icon/angle-up.png"></image>
		</view>
		<view class="form-expand" v-if="!contentShow">
			<view class="subtitle">{{obj.subtitle}}</view>
			<view class="body">{{obj.body}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'dropdown',
		props: {
			objList: {},
			lang:"",		
		},
	data() {
		return {	
			contentShow: true,
			obj: this.objList, 
		};
	},
	methods: {
		control() {
			this.contentShow = !this.contentShow;
		}
	}
	
}
</script>

<style>
	.form {
		border-radius: 10px;
		box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
		margin-top: 5px;
		margin-left: 20px;
		margin-right: 30px;
		margin-bottom: 12px;
	}
	.form-fold {
		line-height: 35px;
		width: 100%;
		height: 35px;
		display: flex;
		align-items:center;
		justify-content: space-between;
	}
	.form-fold image {
		width: 30px;
		height: 35px;
		vertical-align: middle;
	}
	.title {
		height: 35px;
		display: inline-block;
		font-family: 'Source Han Sans CN';
		font-size: 17px;
		color: #888888;
		opacity: 1;
		margin-left: 16px;
	}
	.subtitle {
		font-family: 'Source Han Sans CN';
		font-size: 14px;
		font-weight: 600;
		color: #353535;
		margin-left: 10px;
		margin-bottom: 10px;
		opacity: 1;
	}
	.body {
		font-family: 'Source Han Sans CN';
		font-size: 14px;
		color: #353535;
		margin-left: 10px;
		opacity: 1;
		line-height: 20px;
		max-height: 120px;
		overflow: scroll;
		/* 保证文章正常显示 */
		word-break: break-all;
		white-space: pre-line;
		text-overflow: ellipsis;
	
	}
</style>
