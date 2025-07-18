<template>
	<view>
		<view v-if="!status" class="likeBar column_center">
			<image src="../../static/icon/heart_353535.png" mode="aspectFit"></image>
			<text>{{ number }}</text>
		</view>
		
		<view v-else class="likedBar column_center">
			<image src="../../static/icon/heart_ffffff.png" mode="aspectFit"></image>
			<text>{{ number }}</text>
		</view>
	</view>
</template>


<script>
export default {
	name: 'nqLike',
	props: {
		number: {
			type: Number,
			default: 0
		},
		status: {
			type: Boolean,
			default: false
		}
	},
};
</script>


<style>
	.likeBar{
		/* margin-left: 19px; */
		height:22px;
		opacity:1;
		border-radius:50px;
	}
	
	.likeBar image{
		width:13px;
		height:12px;
		margin-left: 8px;
	}
	
	.likeBar text{
		margin-left: 8px;
		margin-right: 8px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:23px;
		color:rgba(53,53,53,1);
	}
	
	.likedBar{
		/* margin-left: 19px; */
		height:22px;
		background:linear-gradient(131deg,rgba(255,161,161,1) 0%,rgba(245,60,60,1) 100%);
		opacity:1;
		border-radius:50px;
	}
	
	.likedBar image{
		width:13px;
		height:12px;
		margin-left: 8px;
	}
	
	.likedBar text{
		margin-left: 8px;
		margin-right: 8px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:23px;
		color:rgba(255,255,255,1);
	}
</style>
