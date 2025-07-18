<template>
	<view id="public-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar"
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		:left-text="lang.back"
		:title="lang.fanAndFollow" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>				
		
		<view :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}"></view>
		
		<view id="public-message-futherbox">
			<scroll-view class="top-menu-view" scroll-x="true" scroll-left="scrollLeft">
				<block v-for="(menuTab,index) in [{name: lang.follow}, {name: lang.fans}]" :key="index">
					<view class="menu-one-view" v-bind:id="'tabNum'+index" @click="swichMenu(index)">
						<view :class="[currentTab==index ? 'menu-one-act' : 'menu-one']">
							<view class="menu-one-txt" @tap="goTop">{{menuTab.name}}</view>
							<view class="menu-one-bottom">
								<view class="menu-one-bottom-color"></view>
							</view>
						</view>
					</view>
				</block>
			</scroll-view>
			<swiper :current="currentTab" class="swiper-box-list" duration="300" @change="swiperChange">
				<swiper-item class="swiper-box" v-for="(swiperData,index1) in swiperDataList" :key="index1">
					<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-test" @scrolltoupper="upper" @scrolltolower="lower"
					 @scroll="scroll" enable-back-to-top="true">
					 <!-- 相对、绝对定位不能改 by Guetta -->
						<view class="user-operation-line" v-for="(item,index2) in (index1==0?followList:fansList)" :key="index2">
							<view class="user-one-line column_center" hover-class="hoverColor"  @click.native.stop='goToPersonPublic(index1, index2)'>
								<!-- 这里方法直接传 item 获取不到，应该是官方的一个Bug -->
								<view class="touxiangBox">
									<image class="publicTouxiang" mode="scaleToFill" :src="pathFilter(item.faceImg)"></image>
								</view>
								<view class="userid" style="display: flex;">
									<view>
										{{item.nickname}}
									</view>
									<image v-if="item.authType == 2" src="../../static/icon/auth_red.png" style="height: 20px;width: 20px;margin-left: 5px;"></image>
									<image v-if="item.authType == 1" src="../../static/icon/auth_yellow.png" style="height: 20px;width: 20px;margin-left: 5px;"></image>
								</view>
								<!-- 暂时先拿掉，TODO: 获取列表同时查询我是否已关注该用户 
																				by Jerrio-->
								<view style="height: 30px;position: absolute;right: 0;width: 30%;z-index: 10;">
									<view v-if="item.id != myId" style="position: relative;width: 100%;height: 30px;">
										<view class="attentionButton" 
											v-if="item.follow==false" 
											@click.native.stop="addFollow(index1, index2)" 
											hover-stop-propagation="false">
											<view style="height: 100%;padding: 0px 18px;border-radius: 4px;" class="super_center" hover-class="hoverColorYellow">
												<text class="attentionButton-text">关注</text>
											</view>
										</view>
										<view class="attentionButton_followed super_center" 
											hover-class="hoverColor" 
											v-if="item.follow==true" 
											@click.native.stop="cancelFollow(index1, index2)"
											hover-stop-propagation="false">
											<text class="attentionButton-text">已关注</text>
										</view>
									</view>
								</view>

							</view>
							<view class="border-bottom-line">
							</view>
						</view>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
	</view>
</template>

<script>
	var me; // 表示本人用户，区别于查询的用户
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue"
	import { mapState, mapMutations } from 'vuex';
	
	export default {
		components:{
			uniNavBar
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				pageTitle: '关注和粉丝列表',
				scrollLeft: 0,
				isClickChange: false,
				currentTab: '', // 切换 list 0/1
				// menuTabs: [{
				// 	name: '他关注的'
				// }, {
				// 	name: '关注他的'
				// }],
				// 关注粉丝列表属性
				swiperDataList: [
					[], // followList 把数据写进里面首次进入页面加载不出，所以写到外面
					[] // fansList
				],
				followList: '',
				fansList: '',
				myId: '',
				screenWidth: 350,
				serverUrl: "",
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
			}
		},
		changeIndicatorDots(e) {
			this.indicatorDots = !this.indicatorDots
		},
		changeAutoplay(e) {
			this.autoplay = !this.autoplay
		},
		intervalChange(e) {
			this.interval = e.target.value
		},
		durationChange(e) {
			this.duration = e.target.value
		},
		onLoad(opt) {
			var data = JSON.parse(decodeURIComponent(opt.data));
			var thisUserInfo = data.thisUserInfo;
			var currentTab = data.currentTab;
			uni.setNavigationBarTitle({
				title: thisUserInfo.nickname + '的主页'
			});
			me = this.getGlobalUserInfo();
			this.myId = me.id;
			// 获取userId
			var userId = thisUserInfo.id;
			this.queryFansFollow(userId);
			// 设置列表 index
			this.currentTab = currentTab;
			var screenWidth = uni.getSystemInfoSync().screenWidth;
			this.screenWidth = screenWidth;
		},
		onPullDownRefresh() {
			console.log('refresh');
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 1000);
		},
		methods: {
			swichMenu: async function(current) { //点击其中一个 menu
				if (this.currentTab == current) {
					return false;
				} else {
					this.currentTab = current;
					this.setScrollLeft(current);
				}
			},
			swiperChange: async function(e) {
				let index = e.target.current;
				this.setScrollLeft(index);
				this.currentTab = index;
			},
			setScrollLeft: async function(tabIndex) {
				let leftWidthSum = 0;
				for (var i = 0; i <= tabIndex; i++) {
					let nowElement = await this.getWidth('tabNum' + i);
					leftWidthSum = leftWidthSum + nowElement.width;
				}
				let winWidth = uni.getSystemInfoSync().windowWidth;
				this.scrollLeft = leftWidthSum > winWidth ? (leftWidthSum - winWidth) : 0
			},
			getWidth: function(id) { //得到元素的宽高
				return new Promise((res, rej) => {
					uni.createSelectorQuery().select("#" + id).fields({
						size: true,
						scrollOffset: true
					}, (data) => {
						res(data);
					}).exec();
				})
			},
			loadMore: function(tabIndex) {
				console.log('正在加载更多数据。。。')
				this.getDateList(tabIndex);
			},
			upper: function(e) {
				console.log(e)
			},
			lower: function(e) {
				console.log(e)
			},
			scroll: function(e) {
				console.log(e)
				this.old.scrollTop = e.detail.scrollTop
			},
			goTop: function(e) {
				this.scrollTop = this.old.scrollTop
				this.$nextTick(function() {
					this.scrollTop = 0
				});
				// uni.showToast({
				// 	icon: "none",
				// 	title: "回到顶部喽~"
				// })
			},
			/**
			 * 添加关注
			 */
			addFollow: function(index1, index2) {
				console.log("加关注...");
				var list;
				if (index1 == 0) {
					list = this.followList;
				} else if (index1 == 1) {
					list = this.fansList;
				}
				var thisUser = list[index2];
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/follow',
					method: "POST",
					data: {
						userId: thisUser.id,
						fanId: me.id
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							// 刷新用户信息，这里本地改就好就不用重新刷新列表了
							thisUser.follow = true;
						}
					}
				});
			},
			/**
			 * 取消关注
			 */
			cancelFollow: function(index1, index2) {
				console.log("取关...");
				var list;
				if (index1 == 0) {
					list = this.followList;
				} else if (index1 == 1) {
					list = this.fansList;
				}
				var thisUser = list[index2];
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/dontFollow',
					method: "POST",
					data: {
						userId: thisUser.id,
						fanId: me.id
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						if (res.data.status == 200) {
							// 刷新用户信息
							thisUser.follow = false;
						}
					}
				});
			},
			/**
			 * 查询该用户的粉丝和关注用户信息列表
			 */
			queryFansFollow(userId) {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/queryFansAndFollow',
					method: "POST",
					data: {
						userId: userId,
						myId: me.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res)
						if (res.data.status == 200) {
							var data = res.data.data;
							// console.log(data);
							that.followList = data.followList;
							that.fansList = data.fansList;
						}
					}
				});
			},
			/**
			 * 直接传 item 有 bug, 所以用这个复杂一点的方式曲线救国
			 * @param {Object} index1 0=followList; 1=fansList
			 * @param {Object} index2 列表里的索引
			 */
			goToPersonPublic(index1, index2) {
				var list;
				if (index1 == 0) {
					list = this.followList;
				} else if (index1 == 1) {
					list = this.fansList;
				}
				var userId = list[index2].id
				uni.redirectTo({
					url: '/pagesSubA/personpublic/personpublic?userId=' + userId,
				});
			}
		}
	}
</script>

<style>
	page {
		width: 100%;
		height: 100%;
	}
	#public-container {
		position: fixed;
		height: 100%;
		width: 100%;
		background-color: #f3f3f3;
	}
	#public-infobox {
		position: fixed;
		height: 20%;
		width: 100%;
	}
	#public-message-futherbox
	/* 这里是帖子块最高级父组件*/
		{
		/* border: 1upx solid red; 如果想快速了解该组件样式,则取消这个注释*/
		position: fixed;
		width: 100%;
		height: 100%;
	}
	/* 以下是帖子展示块的样式 */
	.top-menu-view {
		display: flex;
		justify-content: space-around;
		width: 100%;
		background-color: rgba(255,201,90,1);
		height: 47px;
		/* 在这里设置导航条高度 */
	}
	.menu-one-view {
		display: inline-block;
		white-space: nowrap;
		height: 100%;
		width: 48%;
	}
	.top-menu-view .menu-one-view .menu-one {
		/* 在这里写 单个按钮样式 */
		margin-left: 8%;
		position: relative;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}
	.top-menu-view .menu-one-view .menu-one .menu-one-txt {
		height:21px;
		font-size:17px;
		font-weight:500;
		line-height:21px;
		color:rgba(255,255,255,1);
	}
	.top-menu-view .menu-one-view .menu-one .menu-one-bottom {
		position: absolute;
		bottom: 0;
		width: 100%;
	}
	.top-menu-view .menu-one-view .menu-one .menu-one-bottom .menu-one-bottom-color {
		width: 108px;
		height: 4px;
	}
	.top-menu-view .menu-one-view .menu-one-act {
		/* 在这里写 单个按钮样式 */
		margin-left: 8%;
		position: relative;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}
	.top-menu-view .menu-one-view .menu-one-act .menu-one-txt {
		height:21px;
		font-size:17px;
		font-weight:500;
		line-height:21px;
		color:rgba(255,255,255,1);
		margin-bottom: 8px;
	}
	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom {
		/* 在这里设置底部横条宽度 */
		position: absolute;
		bottom: 0;
		width: 80%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.top-menu-view .menu-one-view .menu-one-act .menu-one-bottom .menu-one-bottom-color {
		/* 在这里设置底部横条高度和颜色 */
		width: 108px;
		height: 4px;
		background-color: rgba(246,168,6,1);
		margin-bottom: 8px;
	}
	.swiper-box-list {
		flex: 1;
		min-height: 82%;
		width: 100%;
		background-color: #f3f3f3;
	}
	/* swiper */
	.swiper {
		height: 360upx;
	}
	.slideimage {
		width: 100%;
	}
	/* 一个粉丝 */
	.user-one-line {
		position: relative;
		width: 100%;
		height:64px;
	}
	.touxiangBox{
		position: absolute;
		left: 16px;
		width: 44px;
		height: 44px;
	}
	.publicTouxiang {
		width: 100%;
		height: 100%;
		border-radius: 999upx;
		display: inline-block;
		vertical-align: middle;
	}
	.border-bottom-line {
		height: 1px;
		background-color: #dcdcdc;
		margin-left: 130upx;
		width: 580upx;
	}
	.userid {
		position: absolute;
		left: 76px;
		height:21px;
		font-size:17px;
		font-weight:400;
		line-height:21px;
		color:rgba(53,53,53,1);
		opacity:1;
	}
	.attentionButton {
		position: absolute;
		z-index: 10;
		right: 16px;
		height:30px;
		background:rgba(252,192,65,1);
		opacity:1;
		border-radius:4px;
	}
	.attentionButton .attentionButton-text {
		height:23px;
		font-size:14px;
		font-weight:500;
		line-height:23px;
		color:rgba(255,255,255,1);
	}
	
	.attentionButton_followed {
		position: absolute;
		z-index: 10;
		right: 16px;
		padding: 0px 18px;
		height:30px;
		opacity:1;
		border:1px solid rgba(252,192,65,1);
		border-radius:4px;
	}
	
	.attentionButton_followed .attentionButton-text {
		height:23px;
		font-size:14px;
		font-weight:500;
		line-height:23px;
		color:rgba(252,192,65,1);
	}
	
	.scroll-test {
		height: 100%;
	}
	.swiper-box {
		width: 100%;
		height: 100%;
	}
</style>