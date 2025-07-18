<template>
	<view>
		<view class="zdTabBar">
			<view class="ul">
				<!-- 推出左侧空白 -->
				<view style="width: 20px;"></view>
				<view :class="['super_center', 'li', current == index ? 'cur' : '']" v-for="(item, index) in tabBarList" style="text-align: center;" :key="index" @tap="onClick(item)">
					<view >
						<msgcount style="position: absolute;z-index: 40;right: 10%;top: 9px;" :count="item.count"></msgcount>
						<image v-if="index==2" :src="item.icon" mode="aspectFit" style="width: 25px;height: 25px;margin-top: 5px;"></image>
						<view 
							class="img super_center"
							:style="{ height: current == index ? '44px' : '25px', width: current == index ? '44px' : '25px', background: current == index ? 'rgba(253,169,86,1)' : ''}"
							v-if="item.type == 0 && index!=2"
						>
							<image style="width: 20px;height: 20px;" :style="{ margin: current == index ? '' : '0 0 0 2.5px'}" :src="current == index ? item.selectIcon : item.icon" mode="aspectFit"></image>
						</view>
						<!-- <view class="ic super_center" v-if="item.type == 1">
							<image
								:class="[rotateStatus ? 'midIcon' : 'midIconBack']"
								:style="{ width: '24px', height: '24px', margin: '16px 16px', transition: 'all 500ms linear 200ms', transform: 'rotate(' + degree + 'deg)' }"
								src="../../static/icon/plus_tab.png"
								mode="aspectFit"
							></image>
						</view> -->
						<view class="p" v-if="current != index">{{ lang.tabbarName[index] }}</view>
					</view>
				</view>
				<!-- 推出右侧空白 -->
				<view style="width: 20px;"></view>
			</view>

			<tablist style="position: fixed;z-index: 40;" v-if="rotateStatus"></tablist>
		</view>
		<!-- 蒙层 -->
		<view
			v-if="rotateStatus"
			style="position: fixed; 
			top: 0;
			height: 100%;
			width: 100%;
			z-index: 30;"
			@tap="rotate"
		></view>
	</view>
</template>

<script>
import tablist from '../nq-tablist/nq-tablist.vue';
import msgcount from '../nq-msgcount/nq-msgcount.vue';
import { mapState, mapMutations } from 'vuex';

export default {
	props: {
		current: {
			type: Number,
			default: 0
		}
	},

	components: {
		tablist,
		msgcount,
	},

	data() {
		return {
			degree: 0, //旋转角度
			rotateStatus: false, //旋转状态,判断静止or顺时针or逆时针
			tabBarList: '',
		};
	},
	computed: {
		...mapState(['myMsgCount','lang'])
	},
	
	watch: {
		myMsgCount(newVal, oldVal) {
			// console.log(newVal)
			this.tabBarList[3].count = newVal;
		}
	},
	
	created() {
		this.tabBarList = [
				{
					type: 0,
					icon: '/static/icon/home_d4d4d4.png',
					selectIcon: '/static/icon/home_ffffff.png',
					name: '主页',
					url: '/pages/tabPages/index'
				},
				{
					type: 0,
					icon: '/static/icon/chartbar_d4d4d4.png',
					selectIcon: '/static/icon/chartbar_ffffff.png',
					name: '新生',
					url: '/pages/tabPages/iweek'
				},
				{
					type: 0,
					icon: '/static/icon/plus_tab.png',
					selectIcon: '/static/icon/plus_tab.png',
					name: '发布',
					url:'/pages/submit/submit'
				},
				{
					type: 0,
					icon: '/static/icon/comment_dots_d4d4d4.png',
					selectIcon: '/static/icon/comment_dots_ffffff.png',
					name: '动态',
					url: '/pages/tabPages/messagelist',
					count: this.myMsgCount,
				},
				{
					type: 0,
					icon: '/static/icon/user_d4d4d4.png',
					selectIcon: '/static/icon/user_ffffff.png',
					name: '我的',
					url: '/pages/tabPages/mine'
				}
			]
	},
	methods: {
		onClick(e) {
			// if (e.type == 1) {
			// 	this.$emit('clickMid', e);
			// 	this.rotate();
			// 	return;
			// }
			// debugger;
			if(e.name=="发布"){
				console.log(this.current);
				var userInfo = this.getGlobalUserInfo();
				if(this.isNull(userInfo.email)){
					uni.showModal({
											title: '请绑定邮箱后再发帖',
											content: '点击确定前往绑定邮箱',
											success: function (res) {
												if (res.confirm) {
													console.log('用户点击确定');
													uni.navigateTo({
													                            url:'/pages/profile/profile'
													                        });
					
												} else if (res.cancel) {
													console.log('用户点击取消');
												}
											}
										});
				}else{
					uni.navigateTo({
						url:e.url
					})
				}
				// uni.navigateTo({
				// 	url:e.url
				// })
			}
			else{
				console.log(e.name)
				console.log(this.current)
				if(this.rotateStatus){
					//切换窗口的时候把list关上，
					this.rotate();
				}
				this.$emit("clickTab", e)
				
				uni.switchTab({
					url: e.url
				});
			}
		},

		rotate() {
			if (!this.rotateStatus) {
				this.degree = 45;
			} else {
				this.degree = 0;
			}
			this.rotateStatus = !this.rotateStatus;
		}
	}
};
</script>
<style lang="less">
// 公用样式
@mainc: #00b7b8; //主色
@textc: #464646; //字体颜色
@textca: #909090; //字体颜色1
@borderc: #eeeeee; //边框颜色
@redc: #ec3851; //红色字体
image {
	display: block;
	width: 100%;
	height: 100%;
}

.df {
	display: flex;
}

page {
	font-size: 24upx;
	color: @textc;
	line-height: 35upx;
	padding-bottom: 60px;
	background: #f7f7f7;
	font-family: '微软雅黑', sans-serif, serif;
}

.zdTabBar {
	position: fixed;
	left: 0;
	bottom: 0;
	right: 0;
	z-index: 40;
	padding-bottom: env(safe-area-inset-bottom);
	height: 63px;
	background: rgba(255, 255, 255, 1);
	box-shadow: 0px 0px 3px rgba(0, 0, 0, 0.16);
	border-radius: 16px 16px 0px 0px;

	.ul {
		.df;
		.li {
			flex: 1;
			position: relative;
			height: 63px;
			// 配合super_center中的display=flex，使元素垂直且居中
			flex-direction: column;
			.img {
				border-radius: 50%;
				opacity: 1;
				z-index: 20;
			}
			.ic {
				position: absolute;
				bottom: 22px;
				border-radius: 50%;
				text-align: center;
				width: 56px;
				height: 56px;
				background: rgba(255, 255, 255, 1);
				box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.16);
				opacity: 1;
				z-index: 40;
			}

			.p {
				height: 14px;
				font-size: 12px;
				font-family: Source Han Sans CN;
				font-weight: bold;
				line-height: 23px;
				color: rgba(212, 212, 212, 1);
				opacity: 1;
				z-index: 20;
			}
			&.cur {
				.p {
					color: @redc;
				}
			}
		}
	}
}
.midIcon {
	animation: midIcon 300ms linear 1;
}
/* 
      midIcon : 定义的动画名称
      1s : 动画时间
      linear : 动画以何种运行轨迹完成一个周期
      infinite :规定动画应该无限次播放
     */
@keyframes midIcon {
	0% {
		-webkit-transform: rotate(0deg);
	}
	25% {
		-webkit-transform: rotate(11.25deg);
	}
	50% {
		-webkit-transform: rotate(22.5deg);
	}
	75% {
		-webkit-transform: rotate(33.75deg);
	}
	100% {
		-webkit-transform: rotate(45deg);
	}
}

.midIconBack {
	animation: midIconBack 300ms linear 1;
}
/* 
      midIcon : 定义的动画名称
      1s : 动画时间
      linear : 动画以何种运行轨迹完成一个周期
      infinite :规定动画应该无限次播放
     */
@keyframes midIconBack {
	0% {
		-webkit-transform: rotate(45deg);
	}
	25% {
		-webkit-transform: rotate(33.75deg);
	}
	50% {
		-webkit-transform: rotate(22.5deg);
	}
	75% {
		-webkit-transform: rotate(11.25deg);
	}
	100% {
		-webkit-transform: rotate(0deg);
	}
}
</style>
