/*
	#投票活动落地页,投票卡片
	#Author: Guetta
	#Update Log: Guetta 2020.9.13 v2.0.2
*/
<template>
	<view class="picVoteCard" :style="{'min-height': cardHeight + 'px', width: cardWidth + 'px'}"
	@click="switchCardStatus">
		<!-- 小组照片 -->
		<view :style="{ width: cardWidth + 'px', height: cardWidth * 3/4 + 'px'}"  class="photoBox">
			<image :src="pathFilter(thisCard.img)" mode="aspectFill"></image>
			<view> {{thisCard.count}} {{lang.voteCount}} </view> 
		</view>
		<!-- 详情 -->
		<view class="teamDetail">
			<text class="teamTitle">{{title}}</text>
			<text class="teamIntro">{{intro}}</text>
		</view>
		<!-- 投票按钮 -->
		<view class="voteButton">
			<view hover-class="hoverColor">{{lang.vote}}</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex';
	
	const voteParse = {
		parseObject: {
			title: /\[(.*)\]/,
			intro: /\{(.*)\}/,
		},
		parseTitle(str) {
			var stra = "";
			stra = voteParse.parseObject.title.exec(str);
			reture;
		}
	};
	export default {
		components:{

		},
		props:{
			cardInfo: {},
			cardWidth: '',
			cardHeight: '',
		},
		data() {
			return {
				// 界面显示控制

				// 数据控制
				thisCard: this.cardInfo, // 转为局部变量
				title: '', 
				intro: '',
			}
		},
		computed: {
			...mapState(['lang'])
		},
		watch:{
			cardWidth(newVal1,oldVal1){
				this.recieveWidth = newVal1;
			},
			cardHeight(newVal2,oldVal2){
				this.recieveHeight = newVal2;
			}
		},
		created() {
			// TODO: the content still cannot be parsed. Need to debug.
			// debugger
			// parse thisCard
			var that = this;
			// debugger
			that.title = voteParse.parseObject.title.exec(that.thisCard.content)[1];
			that.intro = voteParse.parseObject.intro.exec(that.thisCard.content)[1];
		},
		
		methods: {
			// Show team detail card
			switchCardStatus(){
				// console.log("click");
				// send info data to teamDetailCard
				var that = this;
				this.$emit('eventData', {
					showDetailCardstatus:true,
					voteStatus: '',
					refreshList: '',
					share: false,
					teamDetail:{
						title: that.title,
						intro: that.intro,
						thisCard: that.thisCard,
					},
				});
			},
			
			// vote for this team
			// voteThisTeam(){
			// 	var that = this;
			// 	uni.request({
			// 		url: that.$serverUrl + '/vote/selectOption',
			// 		method: 'POST',
			// 		data: {
						
			// 		}
			// 	})
			// }
			
			
		}
	}
</script>

<style>
	.picVoteCard{
		margin: 10px 4px;
		display: flex;
		flex-direction: column;
		border-radius: 5px;
		background-color: #FAFAFA;
	}
	
	.photoBox{
		position: relative;
		border-radius: 5px;
	}
	
	.photoBox image{
		width: 100%;
		height: 100%;
		position: absolute;
		border-top-left-radius: 5px;
		border-top-right-radius: 5px;
	}
	
	.photoBox view{
		position: absolute;
		min-width: 36px;
		left: 0;
		bottom: 0;
		height: 22px;
		background: linear-gradient(0deg, rgba(0, 0, 0, 0.5) 0%, rgba(0, 0, 0, 0.32) 32%, rgba(255,255,255,0) 100%);
		vertical-align: bottom;
		
		height: 20px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 20px;
		color: #FAFAFA;
		overflow: hidden;
		text-overflow: ellipsis;
		opacity: 1;
	}
	
	.teamDetail{
		width: calc(100% - 12px);
		padding: 2px 6px;
		display: flex;
		flex-direction: column;
	}
	
	.teamDetail .teamTitle{
		margin-top: 12px;
		width: 100%;
		height: 15px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 15px;
		color: #4A4A4A;
		overflow: hidden;
		text-overflow: ellipsis;
		opacity: 1;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
	}
	
	.teamDetail .teamIntro{
		width: 100%;
		margin-top: 12px;
		height: 15px;
		font-size: 12px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 15px;
		color: #4A4A4A;
		overflow: hidden;
		text-overflow: ellipsis;
		opacity: 1;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
	}
	
	.voteButton{
		position: relative;
		margin-top: 12px;
		width: 100%;
		height: 20px;
	}
	
	.voteButton view{
		position: absolute;
		right: 8px;
		bottom: 0;
		height: 22px;
		padding: 3px 8px;
		background-color: rgba(213,91,80,1);
		border-radius: 5px;
		
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 500;
		line-height: 20px;
		color: #FAFAFA;
		opacity: 1;
	}
	
	
	
</style>
