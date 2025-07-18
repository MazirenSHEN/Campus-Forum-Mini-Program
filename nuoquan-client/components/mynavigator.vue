<!-- 
 Description: 导航列表组件
 Author: Jerrio & 子可
 Date: June 23, 2020
-->
<template>
	<view>
		<view v-for="(obj, index) in objList" :key="index" hover-class="hoverColor" @click="receiveClick(obj, index)">
			<view class="generic_item">
				<view class="left_icon" :style="{ background: obj.style, display: 'inline-block', width: '28px', height: '28px', borderRadius: '50%' }">
					<image :src="'../static/icon/' + obj.lefticon_src + '.png'" mode="scaleToFill"></image>
				</view>
				<view class="page">{{ obj.name }}</view>
				<view class="right_icon0" v-if="obj.type == 0"><image :src="'../static/icon/' + obj.righticon_src + '.png'" mode="scaleToFill"></image></view>
				<view class="right_icon1" v-else>
					<nqSwitch
						:bgSwitchLeft="'-13px'"
						:bgSwitchRight="'41px'"
						:options=obj.options
						:initStatus=obj.initStatus
						@onChange="receiveSwitch()"
					></nqSwitch>
				</view>
			</view>
			<view class="line" v-if="index != objList.length - 1"></view>
		</view>
	</view>
</template>

<script>
import nqSwitch from '@/components/nq-switch.vue';

export default {
	name: 'pagejump',
	components: {
		nqSwitch
	},
	props: {
		objList: {}, // type: 0 跳转，1 switch
	},
	data() {
		return {};
	},
	created() {
		this.$emit('test', 0);
	},
	methods: {
		receiveSwitch(event) {
			// console.log(event)
			var eventStruct = {
				action: 'switchLang',
				status: event.status
			};
			this.$emit('trigger', eventStruct);
		},

		receiveClick(obj, index) {
			var eventStruct = {
				action: 'goto',
				obj: obj,
				index: index
			};
			console.log(eventStruct);
			this.$emit('trigger', eventStruct);
		}
	}
};
</script>

<style>
.generic_item {
	position: relative;
	height: 50px;
	display: flex;
	align-items: center;
	align-content: center;
}

.left_icon image {
	margin: auto;
	margin-top: 6px;
	width: 16px;
	height: 16px;
}
.page {
	position: absolute;
	left: 15%;
	display: inline-block;
	font-size: 18px;
}
.right_icon0 {
	position: absolute;
	right: 0;
	display: inline-block;
	width: 20px;
	height: 20px;
	opacity: 1;
}
.right_icon1 {
	position: absolute;
	right: 0;
	display: inline-block;
	width: 58px;
	height: 24px;
	opacity: 1;
}
.line {
	margin-left: auto;
	margin-right: auto;
	height: 0px;
	/* 高度 1 像素 */
	border: 0.5px solid rgba(236, 236, 236, 1);
	opacity: 1;
}
</style>
