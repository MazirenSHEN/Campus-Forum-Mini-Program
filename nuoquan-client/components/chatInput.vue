<template>
	<view class="bottomBar" id="chatarea">
		<textarea :style="{ width:  textareaWidth + 'px'}" fixed="true" cursor-spacing="20" auto-height="true" v-model="inputMessage"
		 :show-confirm-bar="false" adjust-position="false" auto-focus @focus="getKeyBoardHeight" @blur="recoverTextArea" />
		<view id="icons" class="icons">
					<button class="viewPic" @click="showToast()"><image src="../static/icon/viewLocalPic.png"></image></button>
					<!-- 				<button><image src="../../static/icon/emoji.png"></image></button>
	-->
					<button class="viewEmoji" @click="showToast()"><image src="../static/icon/emoji.png"></image></button>
					<button @click="send(textMsg)" class="sendText" @touchend.prevent="onTap" >{{lang.send}}</image></button>
	
				</view>
	</view>
</template>

<script>
	export default {
		props:{
			inputMessageFromPage:"",
			windowWidth:"",
			lang:"",
			
		},
		data() {
			return {
				inputMessage:this.inputMessageFromPage,
				textareaWidth:"",
				height:'',
			};
		},
		mounted() {
			var that = this;
			//根据屏幕宽度，自适应输入框宽度
			var query2 = uni.createSelectorQuery().in(this);
			query2.select('#icons').boundingClientRect(data => {
			    console.log(data);
				that.textareaWidth = that.windowWidth - data.width - 56;
			}).exec();
		},
		methods: {
			showToast() {
				uni.showToast({
					// title: '⠀⠀⠀⠀⠀under⠀⠀⠀⠀⠀development',//不是空格，是特殊符号，莫删
					title: '开发小哥正在玩命实现中...',
					duration: 2000,
					icon: 'none'
				});
			},
			send(){
				this.$emit('send',this.inputMessage);
				this.inputMessage = '' // 清空输入框
			},
			getKeyBoardHeight(e){
				// debugger
				var textAreaTop_ = e.detail.height;
				var phoneHeight;
				console.log("键盘高度" + e.detail.height);
				uni.getSystemInfo({
					success(res) {
						phoneHeight = res.windowHeight;
						console.log(phoneHeight);
					}
				});
				this.height= textAreaTop_;
				this.$emit("change",this.height);
			},
			recoverTextArea(){
				this.$emit("recover",this.height);
			}
		}
	}
</script>

<style>
	.bottomBar {
		
		display: flex;
		align-items: center;
		bottom: 0;
		min-height: 48px;
		width: 100%;
		margin: 0;
		padding: 0;
		box-shadow: 0px -2px 2px 0px rgba(130, 130, 130, 0.2);
		background: #ffffff;
	}
	
	.bottomBar textarea {
		display: inline-block;
		line-height: 16px;
		/* width: 62.67%; */
		max-height: 75px;
		padding: 5px 8px;
		margin: 11px 0 11px 14px;
		font-size: 13px;
		height:34px;
		border:1px solid rgba(252,192,65,1);
		opacity:1;
		border-radius:8px;
	}
	
	.icons {
		position: absolute;
		bottom: 13px;
		right:10px;
		height: 24px;
		display: inline-flex;
	}
	
	.icons image {
		display: block;
		width: 24px;
		height: 24px;
		vertical-align: bottom;
	}
	
	/* button {
		display: inline-block;
		margin: 0;
		padding: 0;
		width: 24px;
		height: 24px;
		vertical-align: bottom;
		background: #ffffff;
	} */
	button::after {
		border: none;
	}
	.viewPic, .viewEmoji{
		display: inline-block;
		margin: 0 4px;
		padding: 0;
		width: 24px;
		height: 24px;
		vertical-align: bottom;
	}
	
	.sendText{
		padding: 0 0;
		line-height: 23px;
		min-width:34px;
		height:23px;
		font-size:17px;
		font-weight:500;
		color:rgba(252,192,65,1);
		margin: 0 0 0 6px;
	}
</style>
