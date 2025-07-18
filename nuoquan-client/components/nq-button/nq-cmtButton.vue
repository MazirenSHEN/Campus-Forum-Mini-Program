<!-- 
	Author: Guetta
	Latest Update: ???
	Intro: 通用评论按钮组件
	备注:
 -->

<template>
	<view>
		<view v-if="number == 0" class="commentBar column_center">
			<image src="../../static/icon/comment-alt-353535.png" mode="aspectFit"></image>
			<text>{{ number }}</text>
		</view>
		
		<view v-else class="commentedBar column_center">
			<image src="../../static/icon/comment-alt-ffffff.png" mode="aspectFit"></image>
			<text>{{ number }}</text>
		</view>
	</view>
</template>


<script>
export default {
	name: 'nqCmt',
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
	created() {
		// console.log(this.number)
	}
};
</script>


<style>
.commentBar{
	height:22px;
	opacity:1;
	border-radius:50px;
}

.commentBar image{
	width:13px;
	height:12px;
	margin-left: 8px;
}

.commentBar text{
	margin-left: 8px;
	margin-right: 8px;
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:400;
	line-height:23px;
	color:rgba(53,53,53,1);
}


.commentedBar{
	height:22px;
	opacity:1;
	border-radius:50px;
	background: linear-gradient(313deg,rgba(255,184,32,0.84) 0%,rgba(240,240,122,1) 100%);
}

.commentedBar image{
	width:13px;
	height:12px;
	margin-left: 8px;
}

.commentedBar text{
	margin-left: 8px;
	margin-right: 8px;
	font-size:14px;
	font-family:Source Han Sans CN;
	font-weight:400;
	line-height:23px;
	color:rgba(255,255,255,1);
}
</style>
