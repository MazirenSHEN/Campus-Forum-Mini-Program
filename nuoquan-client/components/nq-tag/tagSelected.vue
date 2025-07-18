<!--周围彩色光圈的标签-->
<template>
	<view class="selectedTag" :style="{ 'box-shadow': '0px 0px 6px ' + tagColor }" @click="onClick">{{ tag.tag }}</view>
</template>

<script>

import tagSelected from './tagSelected';
	
export default {
	props: {
		tag: ''
	},
	data() {
		return {
			tagColor: ''
		};
	},
	created() {
		// 随机生成颜色
		var tagColors = this.tagColors;
		var random = Math.floor(Math.random() * tagColors.length);
		// 0~tagColors.length-1
		this.tagColor = tagColors[random];
	},

	methods: {
		onClick(){
			this.$emit("click");
		}
		
	}
};
</script>

<style>
.selectedTag {
	display: inline-block;
	vertical-align: bottom;
	color: #000000;
	font-size: 14px;
	line-height: 26px;
	height: 26px;

	position: relative;
	margin-right: 9px;
	padding-right: 27px;
	padding-left: 12px;
	pointer-events: none;
	/* 用于解决空格换行问题 */
	white-space: nowrap;
	border-radius: 20px;
}
.selectedTag::after {
	position: absolute;
	content: '✕';
	right: 10px;
	margin-left: 4px;
	color: #000000;
	font-size: 13px;
	pointer-events: auto;
	font-weight: 900;
	white-space: nowrap;
}
</style>
