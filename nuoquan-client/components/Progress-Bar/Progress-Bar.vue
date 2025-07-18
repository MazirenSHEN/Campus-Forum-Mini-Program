<template>
	<view style="width: 100%;height: 8px;">
		<view class="progress" 
			:style="{ border: Type == 'aqua' ? '1px solid rgba(174,174,174,1)' : '1px solid rgba(72,177,233,1);' }" 
			:class="Type">
			<view class="progress-bar" :style="{height: 8 + 'px',width: thisWidth + '%', 'max-width': 100 + '%'}">
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		// 进度条百分比
		progessBarWidth: {
			type: Number,
			default: 0
		},
		// 进度条样式
		
			// aqua
			// aquachosen
		
		Type: {
			type: String,
			default: 'aqua',
		},
		// 主进度显示百分比
		Main: {
			type: Boolean,
			default: true,
		},
		// 副进度显示百分比
		Vice: {
			type: Boolean,
			default: true,
		},
		
	},
	data() {
		return {
			thisWidth: 0
		};
	},
	mounted(){
		this.thisWidth = this.progessBarWidth;
	},
	methods: {}
};
</script>

<style>
@import url('Progress-Bar.css');
</style>
