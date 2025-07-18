<template>
	<view class="swiper-date" style="text-align: center;">
		<view class="unselected" v-for="(item,index) in dateList" :key = "index" :class="{'selected': nowIndex === index }" @click="getDate(item);changeIndex(index)">
			<text>{{item.date}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				index: 0,
				nowIndex: 0,
				date:'',
				curDate:'',
				dateList:[
					{
						date: 12,
						selected: false,
					},
					{
						date: 13,
						selected: false,
					},
					{
						date: 14,
						selected: false,
					},
					{
						date: 15,
						selected: false,
					},
					{
						date: 16,
						selected: false,
					},
					{
						date: 17,
						selected: false,
					},
					{
						date: 18,
						selected: false,
					}
				]
			}
		},
		mounted() {
			this.getCurrentDate();
			if (this.curDate < 12){
				this.getDate(this.dateList[0]);
				this.changeIndex(0);
			}else if(this.curDate<=18){
				this.getDate(this.dateList[this.curDate-12]);
				this.changeIndex(this.curDate-12);
			}else {
				this.getDate(this.dateList[12]);
				this.changeIndex(12)
			}
		},
		methods: {
			getDate(e){
				this.date = e.date;
				this.$emit("date",this.date);
			},
			changeIndex(e){
				this.nowIndex = e
			},
			getCurrentDate(){
				var d = new Date();
				this.curDate = d.getDate();
				// 测试用，修改日期返回
				// this.curDate = 20;
				console.log(this.curDate);
			}
		}
	}
</script>

<style scoped>
	.swiper-date {
		margin: 0 0 40upx 0;
	}
	.unselected {
		display: inline-block;
		text-align: center;
		width: 8%;
		height: auto;
		border: 1upx solid #ECECEC;
		border-radius: 100%;
		margin: 20upx 20upx 0 20upx;
		font-weight: bold;
		font-size: 14px;
		height: 30px;
		line-height: 30px;
		color: #000000;
		background-color: #FFFFFF;
		aspect-ratio: 1/1;
	}
	.selected {
		display: inline-block;
		text-align: center;
		border: 1upx solid #ffffff;
		border-radius: 100%;
		margin: 20upx 20upx 0 20upx;
		font-weight: bold;
		font-size: 14px;
		width: 8%;
		height: auto;
		line-height: 30px;
		color: #FFFFFF;
		background-color: #f9c466;
		aspect-ratio: 1/1;
	}
</style>
