<template>
	<view class="switch">
		
			<view class="switch-left" :class="{ chosen : status == 0}" 
			@tap="changeStatus(0)">
				{{options[0]?options[0]:"关"}}
			</view>
			
			<view class="switch-right" :class="{ chosen : status != 0}" 
			@tap="changeStatus(1)">
				{{options[1]?options[1]:"开"}}
			</view>
			
			<!-- <view class="bg-of-switch" :style="{'left':status == 0 ? '-13px;' :'41px' ,}"></view> -->
			<view class="bg-of-switch" :style="{'left':status == 0 ? bgSwitchLeft : bgSwitchRight ,}"></view>
	</view>
</template>

<script>
	export default {
		props: {
			options: '', //非必填，默认关/开
			initStatus: '', //非必填，默认0
			bgSwitchLeft: '',
			bgSwitchRight: '',
		},
		data() {
			return {
				status: 0,
			};
		},
		created(){
			// 创建后再赋值
			this.isNull(this.initStatus)?
				this.status = 0 : this.status = this.initStatus
		},
		methods:{
			/**
			 * @onChange status 有效值 0/1
			 */
			changeStatus(status){
				if(this.status != status ){
					this.status = status ;
					this.$emit("onChange", {status: status});
				}
			},
		}
		
	}
</script>

<style>
.switch {
		height: 22px;
		background: #ECECEC;
		border-radius: 75px;
		width: 82px;
		float: right;
		line-height: 28px;
		display: flex;
		vertical-align: middle;
		position: relative;
		justify-content: space-between;
	}
	.switch-right ,.switch-left{
		color:#9B9B9B;
		font-size: 10px;
		font-weight: 400;
		line-height: 22px;
		display: inline-block;
		align-items: center;
		padding-right:10.5px;
		padding-left: 10.5px;
		z-index: 30;
		width:28px;
		text-align: center;
		transition: padding-left 200ms,
		padding-right 200ms,
		font-size  200ms,
		font-weight  200ms,
		color 200ms;
		transition-delay: 0ms;
	}
	.bg-of-switch{
		height:28px;
		width:54px;
		background: #FFFFFF;
		border-radius: 75px;
		box-shadow: 0px 0px 10px rgba(0,0,0,0.16);
		z-index: 20;
		position: absolute;
		left:-13px;
		top:-3px;
		transition: left 500ms ease;
	}
	.chosen{
		font-weight: bold;
		font-size: 14px;
		color:#353535;
		width:28px;
		padding-left: 0;
		padding-right: 0;
		transition: padding-left 300ms,
		padding-right 300ms,
		font-size  300ms,
		font-weight  300ms,
		color 300ms;
		transition-delay: 100ms;
	}
</style>
