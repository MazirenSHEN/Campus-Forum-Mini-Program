<template>
	<view v-if="isShow">
		<view class="nq-commitArea" :style="{top: textAreaTop + 'px'}">
				<textarea class="inputArea" 
					placeholder-style="font-size:14px;font-weight:400;color:rgba(177,177,177,1);" 
					:placeholder="placeholder" 
					:show-confirm-bar="false"
					:adjust-position="false"
					auto-focus
					@focus="getKeyBoardHeight"
					v-model="inputContent"
					@blur="killCommitArea"
					:maxlength="maxReplyLength"
					/>
				<view style="position: absolute;bottom: 35px;right:15px;font-size: 11px;color: #888888;">{{maxReplyLength - inputContent.length}}</view>
				<!-- 添加图片，等功能开发完成打开注释 -->
				<!-- <image src="../../static/icon/viewLocalPic.png" mode="aspectFit" class="addPic"></image> -->
				<view class="bottomRight">
					<!-- <view style="width: 20px;height: 20px;background-color: #000000;" @tap="con()"></view> -->
					<view class="cancelText" @click.native.stop="killCommitArea" hover-class="hoverColor">
						{{lang.cancel}}
					</view>
					<view class="replyText" @click.native="submit" hover-class="hoverColor">
						{{replyText}}
					</view>
				</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
export default {
	name: 'commitArea',
	data() {
		return {
			maxReplyLength: 150,
			replyText: '',
			textAreaTop: 1000,
			getIsShow: '',
			inputContent:'',
			placeholder: '',
		} 
	},
	props:{
		openOrigin:{
			type: String,
			default: ''
		},
		isShow:{
			type:Boolean,
			default: true
		},
		userInfo: '',
	},
	computed: {
		...mapState(['lang'])
	},
	created() {
		console.log(this.openOrigin);
		// console.log("+++++++" + this.userInfo);
		if(this.openOrigin == "cmt-likedetail"){
			this.replyText = this.lang.reply;
			console.log(this.replyText);
		}
		
		if(this.openOrigin == "detail"){
			this.replyText = this.lang.submit;
			console.log(this.replyText);
		}
	},
	
	watch: {
		isShow(newVal, oldVal){
			console.log("newVal =" + newVal);
			if (newVal == false){
				this.killCommitArea()
			}
		}
	},
	
	methods: {
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
			
			this.textAreaTop = phoneHeight - textAreaTop_ - 12 - 160;
			console.log(this.textAreaTop);
		},
		// con(){
		// console.log(this.isShow);
		// 	debugger;
		// },
		killCommitArea(){
			console.log('kill');
			this.getIsShow = this.isShow;
			if(this.getIsShow){
				this.getIsShow = !this.getIsShow;
				this.$emit("killCommitArea", this.getIsShow);
				this.inputContent = '';
			}else{
				// this.$emit("killCommitArea", this.getIsShow);
				this.inputContent = '';
			}
		},
		submit(){
			console.log('submit from input');
			this.$emit('submit',this.inputContent);
		}
	}
};
</script>

<style>
	.nq-commitArea{
		position: fixed;
		z-index: 999;
		width:calc(100% - 26px);
		height:160px;
		background:rgba(252,252,252,1);
		box-shadow:0px 0px 4px rgba(121,121,121,0.42);
		border-radius: 8px;
		opacity:1;
	}
	
	.inputArea{
		position: absolute;
		top: 13px;
		width:calc(100% - 48px);
		height:86px;
		padding: 12px 12px;
		border:2px solid rgba(252,192,65,1);
		opacity:1;
		border-radius:8px;
	}
	
	.nq-commitArea, .inputArea{
		left: 0;
		right: 0;
		margin-left: auto;
		margin-right: auto;
	}
	
	.addPic{
		position: absolute;
		left: 23px;
		bottom: 5px;
		height: 23px;
		width: 23px;
		border-radius: 4px;
	}
	
	.bottomRight{
		position: absolute;
		right: 12px;
		bottom: 5px;
		height: 23px;
		display: flex;
	}
	
	.replyText{
		margin-left: 10px;
		color:rgba(252,192,65,1);
	}
	
	.cancelText{
		color:rgba(195,195,195,1);
	}
	
	.replyText, .cancelText{
		padding: 0 10px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:23px;
		text-align: center;
		border-radius: 4px;
	}
</style>
