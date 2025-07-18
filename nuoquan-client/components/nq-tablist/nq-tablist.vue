<template>
	<view>
		<view class="tabList_box">
			<view class="tabList">
				<view class="super_center" hover-class="hoverColor" style="width: 100%;height: 50px;" @click="gosubmit">
					<text>{{lang.tabList[0]}}</text>
				</view>
				<view
					style="width: 83px;
					margin-left: 8.5px;
					height: 1px;
					background-color:rgba(236,236,236,1) ;"
				></view>
				<view class="super_center" hover-class="hoverColor" style="width: 100%;height: 50px;" @click="gosubmitVote">
					<text>{{lang.tabList[1]}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
	
export default {
	data() {
		return {};
	},
	computed: {
		...mapState(['lang'])
	},
	created() {
		
	},
	methods: {
		gosubmit() {
			console.log('click in tablist');
			uni.navigateTo({
				url: '../../pages/submit/submit'
			});
		},
		gosubmitVote() {
			console.log('click in tablist');
			uni.navigateTo({
				url: '../../pages/submitVote/submitVote'
			});
		}
	}
};
</script>

<style>
.tabList_box {
	position: fixed;
	width: 115px;
	z-index: 40;
	left: 0;
	right: 0;
	bottom: 87px;
	margin-left: auto;
	margin-right: auto;
}

.tabList {
	margin-left: auto;
	margin-right: auto;
	width: 115px;
	height: 101px;
	display: flex;
	flex-direction: column;
	background: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 12px;
}

.tabList_box text {
	height: 21px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 500;
	line-height: 21px;
	color: rgba(74, 74, 74, 1);
	opacity: 1;
}
</style>
