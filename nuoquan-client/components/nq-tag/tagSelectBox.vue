<template>
	<view class="tagsArea">
		<!-- TO DO 为啥有俩标签这个东西，目前用着同一个列表，是不是还需要改，我们提供标签的那个请求-->
		<!-- 展示待选标签区域 -->
		<text>{{lang.allTags}}</text>
		<view class="tag" v-for="(item, index) in tagList" 
		:key="index" 
		:style="{ background: tagColorList[index] }" 
		@tap="selectedTag(item) ">{{ item.tag }}</view>
		<!-- <text>最近选择</text>
		<view class="tag" v-for="(item,index) in tagList" :key="index" :style="{background: tagColorList[index]}" @tap="addTag(item)">{{item}}</view> -->
		<!-- 			<view style="width: 750upx;height: 1000px;position: absolute;top:-1000px;z-index: 50;" @click="editTag(false)" v-if="editingTag"></view>
		<view style="width: 750upx;height: 1000px;position: absolute;bottom:-1000px;z-index: 50;" @click="editTag(false)" v-if="editingTag"></view> -->
	</view>
</template>

<script>
export default {
	props: {
		tagList: '',
		lang: '',
	},
	data() {
		return {
			tagColorList: [], // 储存每个备选tag的颜色
		};
	},
	created() {
		// 随机生成颜色
		var tagColors = this.tagColors;
		for (var i = 0; i < this.tagList.length; i++) {
			var random_1 = Math.floor(Math.random() * tagColors.length);
			// 0~tagColors.length-1
			this.tagColorList.push(tagColors[random_1]);
		}
	},

	methods: {
		selectedTag(item) {
			this.$emit('selected' , item);
		},
	}
};
</script>

<style>
.tagsArea {
	padding: 0 4.5% 16px 4.5%;
	vertical-align: bottom;
	min-height: 130px;
	max-height: 192px;
	overflow: scroll;
	position: relative;
	border-bottom: 4px solid rgba(236,236,236,1);
	background-color: white;
}

.tagsArea text {
	display: block;
	height: 12px;
	font-size: 12px;
	color: rgba(155, 155, 155, 1);
	padding-top: 16px;
	padding-left: 12px;
	padding-bottom: 8px;
}

.tag {
	display: inline-block;
	vertical-align: bottom;
	color: #ffffff;
	font-size: 14px;
	line-height: 30px;
	height: 30px;

	position: relative;
	margin-left: 8px;
	margin-top: 6px;
	padding-right: 12px;
	padding-left: 12px;
	/* 用于解决空格换行问题 */
	white-space: nowrap;

	border-radius: 20px;
}
</style>
