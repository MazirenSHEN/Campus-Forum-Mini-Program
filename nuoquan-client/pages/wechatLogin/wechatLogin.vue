<!-- 开发测试用界面 -->
<template>
	<view>
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar"
		:style="{height: this.getnavbarHeight().bottom + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="true" 
		:left-text="'返回'"
		:title="pageTitle" 
		:height="navbarHeight"></uni-nav-bar>				
		
		<view :style="{height: navbarHeight + 'px'}"></view>
		
		<button type="primary" open-type="getUserInfo" @getuserinfo="getUserInfo" withCredentials="true">微信登陆授权</button>

		<!-- 用于开发阶段清除用户信息的缓存，发布时需要注释掉 -->
		<button type="primary" @tap="removeUserInfo">[dev]清除用户信息缓存</button>
		<!-- 用于开发阶段清除所有缓存，发布时需要注释掉 -->
		<button type="primary" @tap="clearStorage">[dev]清除缓存</button>

		userId：<input v-model="userId" />
		<button @tap="testLogIn">测试用登陆</button>
	</view>
</template>

<script>
	/* User Example
	 userInfo: {
	 	id: 'test-id123',
	 	nickname: 'test-name',
	 	faceImg: '../static/touxiang.jpg',
	 	faceImgThumb: '../static/touxiang.jpg',
	 	email: 'zy22089@nottingham.edu.cn',
	 	emailPrefix: 'zy22089',
	 	emailSuffix: '@nottingham.edu.cn'
	}*/
	import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue";
	export default {
		components: {
			uniNavBar,
		},
		data() {
			return {
				pageTitle: '开发调试页',
				userId: '',
				navbarHeight: 0,
			}
		},

		onLoad() {
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
		},
		methods: {
			/**
			 * 微信小程序登陆
			 */
			getUserInfo() {
				var that = this;
				uni.login({
					success: res_login => {
						// console.log('-------res_login，获取code-------')
						 console.log(res_login);
						uni.getUserInfo({
							success: info => {
								// console.log('-------获取sessionKey、openid(unionid)-------')
								 console.log(info);
								// 后端获取openid 并设置用户信息
								uni.request({
									url: that.$serverUrl + '/user/getWxUserInfo',
									method: "POST",
									data: {
										encryptedData: info.encryptedData,
										iv: info.iv,
										code: res_login.code,

										nickname: info.userInfo.nickName,
										faceImg: info.userInfo.avatarUrl
									},
									header: {
										'content-type': 'application/x-www-form-urlencoded'
									},
									success: (res) => {
										// console.log(res)
										if (res.data.status == 200) {
											// 3.获取返回的用户信息
											var finalUser = res.data.data;
											// 4.分割邮箱地址, 重构 user
											finalUser = this.myUser(finalUser);
											// 5.写入缓存
											this.setGlobalUserInfo(finalUser);
											console.log(finalUser);

											// 6.返回
											uni.navigateBack({
												delta: 1
											});
										}
									}
								});
							}
						});
					}
				});
			},

			setUser(wUserInfo) {
				/* Deprecated ! 现用 /user/getWxUserInfo 直接处理代替 */
				// 1.微信用户信息 用于上传
				var weUser = {
					// id: wUserInfo.openid
					id: 'test-id123',
					nickname: wUserInfo.nickName,
					faceImg: wUserInfo.avatarUrl
				}
				console.log(wUserInfo)
				var finalUser;
				// 2.把微信信息上传给服务器
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/updateUser',
					method: "POST",
					data: JSON.stringify(weUser),
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						console.log(res)
						if (res.data.status == 200) {
							// 3.获取返回的用户信息
							finalUser = res.data.data;
							// 4.分割邮箱地址, 重构 user
							finalUser = this.myUser(finalUser);
							// 5.写入缓存
							this.setGlobalUserInfo(finalUser);
							console.log(finalUser);

							// 6.返回
							uni.navigateBack({
								delta: 1
							});
						}
					}
				});
			},

			removeUserInfo() {
				this.removeGlobalUserInfo();
				console.log("用户信息缓存已清除")
			},

			clearStorage() {
				uni.clearStorage();
				console.log("所有缓存已清除")
			},

			testLogIn() {
				console.log(this.userId)
				var that = this;
				uni.request({
					url: that.$serverUrl + '/user/queryUser',
					method: "POST",
					data: {
						userId: that.userId
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						console.log(res)
						if (res.data.status == 200) {
							var finalUser = res.data.data;
							this.setGlobalUserInfo(finalUser);
							// 6.返回
							uni.navigateBack({
								delta: 1
							});
						}
					}
				});
			}
		}
	}
</script>

<style>

</style>
