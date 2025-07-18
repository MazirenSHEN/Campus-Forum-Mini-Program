<template>
	<view class="allCenter">
		<button open-type="getUserInfo" @click="login()" style="padding: 100px;">2132132131</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			login() {
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
								// debugger;
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
										 console.log(res)
										if (res.data.status == 200) {
											uni.showToast({
												title:"信息同步成功",
											});
											uni.navigateBackMiniProgram({
												extraData:{
													status:true,
												},
												success(res){
													console.log('成功授权，成功跳转回原小程序')
												},
											})
											// debugger;

										}
									}//sendToServer.success
								});//sendToServer
							}//getUserInfo.success
						});//gotUserInfo
					}//login.success
				});//uni.login({
			}, //f.login()
		}
	}
</script>

<style>
.allCenter{
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>
