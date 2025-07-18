<template>
	<view id="signin-container">
		<!-- 简介 -->
		<view id="introduction" class="super_center" :style="{ left: swiperLeft + '%;' }">
			<!-- 用户协议签订页 -->
			<view class="introduction-contentBox">
				<view class="email-intro">
					<view>{{lang.welcome1}}</view>
					<view>{{lang.welcome2}}</view>
					<view>{{lang.welcome3}}</view>
				</view>
				<!-- <view style="position: absolute;top: 45%;height: 45%;width: 100%;" class="super_center">
					<view class="icon-logoBox super_center">
						<image src="../../static/icon/logo_app.png" mode="aspectFill" class="icon-logo"></image>
					</view>
				</view> -->
				<view class="introduction-bottom-sign">
					<checkbox-group @change="changeAgreement">
						<label style="display: flex;" class="super_center">
							<checkbox class="wx-sign-checkbox" value="cb" :checked="agreement" style="transform: scale(0.5);" />
							<text style="font-size: 14px;font-weight: 550;color: #3d3d3d;">{{lang.isReadProtocol}}</text>
							<view style="font-size: 14px;font-weight: 550;color: #007AFF;" @click="toUserDeal">{{lang.userProtocol}}</view>
						</label>
					</checkbox-group>
				</view>
			</view>
			<!-- 邮箱认证页,并不会显示，保留可以防止错位-->
			<view class="email-Box">
				<view class="email-intro">
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">{{lang.welcome1}}</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">{{lang.lastStep}}</view>
					<view style="width: 100%;font-size: 42upx;font-weight: 550;color: #C0C0C0;">{{lang.certificateEmail}}</view>
				</view>

				<view class="email-content">
					<input
						class="email-input"
						type="text"
						:placeholder="lang.emailPrompt"
						placeholder-style="color: #C0C0C0;font-size:15px;"
						@input="onEmailInput"
						:disabled="auth"
						v-model="emailMes"
					/>
					<input
						class="email-input"
						type="number"
						:placeholder="lang.captcha"
						placeholder-style="color: #C0C0C0;font-size:15px;"
						@input="onCaptchaInput"
						:maxlength="captchaLength"
						:disabled="auth"
					/>
					<wh-captcha
						class="captcha"
						ref="captcha"
						:secord="60"
						:title="lang.getCaptcha"
						:waitTitle="lang.waitCaptcha"
						normalClass="captcha-normal"
						disabledClass="captcha-disabled"
						@click="getCaptcha"
					></wh-captcha>
				</view>
			</view>
		</view>
		<view id="confirm" class="super_center">
			<!-- 微信绑定 -->
			<view class="confirm-rel" v-if="swiperLeft == 0">
				<button v-if="agreement == false" class="confirm-button-before super_center" hover-class="hoverColorYellow">
					<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">{{lang.wechatLogin}}</view>
				</button>
				<button v-else class="confirm-button-checked super_center" hover-class="hoverColorYellow" open-type="getUserInfo" @getuserinfo="getUserInfo">
					<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">{{lang.wechatLogin}}</view>
				</button>
			</view>
			<!-- 邮箱认证 -->
<!-- 			<view class="confirm-rel" v-else-if="swiperLeft == -100">
				<view v-if="!auth">
					上一步 button
					<button class="backAngle" hover-class="hoverColorYellow" @click="lastStep(true)">
						<view class="back"><image style="width: 40px;height: 40px;position: absolute;left: -14px;" src="../../static/icon/angle-left.png"></image></view>
					</button>
					游客通道 button
					<button v-if="emailMes == ''" class="confirm-button-visitor super_center" hover-class="hoverColorYellow" @click="visitorLogin">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">{{lang.guestLogin}}</view>
					</button>
					邮箱认证 button
					<button v-else class="confirm-button-checked super_center" hover-class="hoverColorYellow" @click="confirmCode">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">{{lang.emailAuth}}</view>
					</button>
				</view>
				<view v-else>
					上一步 button
					<button class="backAngle" hover-class="hoverColorYellow" @click="cancleAuth">
						<view class="back"><image style="width: 40px;height: 40px;position: absolute;left: -14px;" src="../../static/icon/angle-left.png"></image></view>
					</button>
					确认登陆 button
					<button class="confirm-button-checked super_center" hover-class="hoverColorYellow" @click="login">
						<view style="color: white;font-weight: 550;letter-spacing: 3px;font-family: Microsoft YaHei;">{{lang.finishLogin}}</view>
					</button>
				</view>
				<view class="conform-bgBox"></view>
			</view> -->
		</view>

		<image src="../../static/BG/signin_bottom.png" mode="scaleToFill" class="bottom-picBox"></image>

		<!-- example 步骤条 -->
		<!-- <view class="example-body">
			<uni-steps :options="activeList" :active="active" />
		</view> -->
		<!-- 进度条 fixed -->
		<view style="position: fixed; width: 48%;left: 26%;top: 64%;height: 5px;background-color: #C0C0C0;z-index: 30;">
			<view style="position: relative;width: 100%;height: 100%;">
				<view style="position: absolute;left: 0;height: 5px;background-color: #FFCD2E;z-index: 40;" :style="{ width: swiperLineWidth + '%;' }"></view>
			</view>
		</view>

		<!-- 进度条文字 -->
		<view style="position: fixed;width: 70%;top: 66%;left: 15%;height: 30px;">
			<view style="display: flex;justify-content: space-around;width: 100%;height: 100%;">
				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">{{lang.wechatLogin}}</view>
<!-- 				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">{{lang.emailAuth}}</view> -->
				<view style="width: 33%;height: 100%;color:#C0C0C0;font-size:15px;" class="super_center">{{lang.finishLogin}}</view>
			</view>
		</view>
	</view>
</template>

<script>
import whCaptcha from '../../components/wh-captcha/wh-captcha.vue';
import uniSteps from '@/components/uni-steps/uni-steps.vue';
import { mapState, mapMutations } from 'vuex';

var isLoding = false;
var timer = null;
var timer_ = null;
//邮箱验证
var email; //输入的email值
var captcha; //输入的captcha值
export default {
	components: {
		whCaptcha,
		uniSteps
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			agreement: false, // 是否同意用户协议
			auth: false,
			emailMes: '', // 双向监听邮箱 input value 变化
			swiperLeft: 0, // 块滑动
			swiperLineWidth: 0, // 进度条滑动
			captchaLength: 6, // 验证码长度
			originSrc: '', // 跳转到注册的上级页面 url
			active: 0,
			activeList: [
				{
					title: '微信绑定'
				},
				{
					title: '邮箱认证'
				},
				{
					title: '登陆'
				}
			],

			userInfo: ''
		};
	},
	
	onLoad(src) {
		console.log(src.origin);
		this.originSrc = src.origin;
	},
	
	onBackPress(e) {
		// return true 表示禁止默认返回
		console.log('监听到返回');
		return false;
	},
	watch:{
		emailMes(val, oldvalue){
			// console.log("emailMes=  " + val);
		}
	},

	methods: {
		changeAgreement() {
			this.agreement = !this.agreement;
		},

		changeAuth() {
			this.auth = !this.auth;
		},

		cancleAuth() {
			this.auth = false;
			this.lastStep(false);
		},

		/**
		 * 微信小程序登陆
		 */
		getUserInfo(e) {
			// console.log("getting UserInfo...")
			// console.log(e);

			// 加锁
			if (isLoding) {
				return;
			}
			isLoding = true;

			uni.showLoading({
				// title: '载入中...'
				title:this.lang.loading,
			});
			setTimeout(() => {
				if (isLoding) {
					isLoding = false; // 解锁
					uni.hideLoading();
					uni.showToast({
						title: this.lang.networkError,
						icon: 'none',
						duration: 2000
					});
				}
			}, 5000); // 延时5s timeout

			var info = e.detail;
			var that = this;
			uni.login({
				success: res_login => {
					// console.log('-------res_login，获取code-------')
					// console.log(res_login);
					// 后端获取openid 并设置用户信息
					uni.request({
						url: that.$serverUrl + '/user/getWxUserInfo',
						method: 'POST',
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
						success: res => {
							// console.log(res)
							if (res.data.status == 200) {
								console.log('暂存用户信息');
								that.userInfo = res.data.data;
								console.log(this.userInfo);

								isLoding = false;
								uni.hideLoading();

								// 下一步
								// that.nextStep(true);
								that.login();
							}
						}
					});
				}, // end of login success
				fail: res => {
					console.log(res);
					isLoding = false;
					uni.hideLoading();
					uni.showToast({
						title: 'login fail',
						icon: 'none',
						duration: 2000
					});
				}
			});
		},

		onEmailInput(event) {
			email = event.target.value;
		},

		onCaptchaInput(event) {
			captcha = event.target.value;
		},

		getCaptcha() {
			if (email) {
				// 测试账号代码
				if (email == 'test@test.com') {
					uni.showToast({
						title: 'OK',
						icon: 'none',
						duration: 2000,
					});
					this.changeAuth();
					this.nextStep(false);
					return;
				}
				
				// Date: July 13,2022
				// Author: Yifei
				// Description: 外校体验用户，直接加在这里给判断就行
				if (email == '1193874@wku.edu.cn') {
					uni.showToast({
						title:'欢迎外校用户',
						icon: 'none',
						duration: 2000,
					});
					
					this.$refs.captcha.begin();
					
					uni.request({
						url: this.$serverUrl + '/user/getCode',
						method: 'POST',
						data: {
							userId: this.userInfo.id,
							email: email
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							console.log(res);
						}
					});
					return;
				}
				
				// 检测邮箱
				if (this.$util.regEmail(email) || this.$util.regUNNCEmail(email)) {
					uni.showToast({
						title: this.lang.notUNNCEmail,
						icon: 'none',
						duration: 2000,
					});
				} else {
					if (this.$refs.captcha.canSend()) {
						console.log('获取验证码 email=' + email);
						this.$refs.captcha.begin();

						uni.request({
							url: this.$serverUrl + '/user/getCode',
							method: 'POST',
							data: {
								userId: this.userInfo.id,
								email: email
							},
							header: {
								'content-type': 'application/x-www-form-urlencoded'
							},
							success: res => {
								if (res.data.status == 200){
									console.log(res);
								}
								else {
									uni.showToast({
										title: this.lang.repeatEmail,
										icon: 'none',
										duration: 2000,
									});
								}
							}
							
						});
					}
				}
			} else {
				uni.showToast({
					title: this.lang.notEmptyEmail,
					icon: 'none',
					duration: 2000,
				});
			}
		},
		
		visitorLogin(){
			var that =this;
			// 写入缓存
			this.setGlobalUserInfo(this.userInfo);
			
			if(!this.isNull(this.originSrc)){
				console.log("跳转");
				uni.navigateTo({
					url: that.originSrc
				});
			} else {
				uni.switchTab({
					url: '../tabPages/index'
				});
			}
		},
		
		confirmCode() {
			if (captcha) {
				uni.showLoading({
					title: this.lang.waiting,
				});
				uni.request({
					url: this.$serverUrl + '/user/confirmCode',
					method: 'POST',
					data: {
						userId: this.userInfo.id,
						code: captcha,
						email: email
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if (res.data.status == 200) {
							// 3.获取返回的用户信息
							var finalUser = res.data.data;
							// 4.分割邮箱地址, 重构 user
							this.userInfo = this.myUser(finalUser);
							console.log(this.userInfo);

							uni.showToast({
								title: 'OK',
								icon: 'none',
								duration: 2000,
							});
							this.changeAuth();
							this.nextStep(false);
						} else {
							console.log('验证失败 ' + res.data.msg);
							uni.showToast({
								title: this.lang.certificateFail,
								icon: 'none',
								duration: 2000,
							});
						}
					},
					complete: () => {
						uni.hideLoading();
					}
				});
			} else {
				uni.showToast({
					title: this.lang.emptyCaptcha,
					icon: 'none',
					duration: 2000,
				});
			}
		},

		login() {
			this.nextStep(false);
			uni.showLoading({
				title: this.lang.logining,
			});

			// 5.写入缓存
			this.setGlobalUserInfo(this.userInfo);

			setTimeout(() => {
				console.log('完成登录,正在进入');
				uni.hideLoading();
				uni.switchTab({
					url: '../tabPages/index'
				});
			}, 1000);
		},

		/**
		 * 转场动画,需计算转场时间以保证进度条和块内容运动一致，
		 * 当前运动时间为 500 ms
		 * TODO: 暂未动态设置块移动距离,和同步运动时间 -Jerrio
		 * @param {Object} sync 是否控制“块”同步运动
		 */
		nextStep(sync) {
			this.active++;
			var point = (100.0 / this.activeList.length) * this.active; //计算进度条节点 单位%
			// console.log(that.swiperLineWidth);
			// 进度条右滑
			clearInterval(timer_); //清空定时器，防止重复操作
			var that = this;
			timer_ = setInterval(function() {
				if (that.swiperLineWidth >= point) {
					clearInterval(timer_);
				} else {
					that.swiperLineWidth = that.swiperLineWidth + 2;
					// console.log(that.swiperLineWidth);
				}
			}, 20);

			if (sync) {
				// 块右滑
				clearInterval(timer); //清空定时器，防止重复操作
				var that = this;
				timer = setInterval(function() {
					if (that.swiperLeft <= -100) {
						clearInterval(timer);
					} else {
						that.swiperLeft = that.swiperLeft - 2;
						// console.log(that.swiperLeft);
					}
				}, 10);
			}
		},

		lastStep(sync) {
			this.active--;
			var point = (100.0 / this.activeList.length) * this.active; //计算进度条节点 单位%
			// 进度条左滑
			clearInterval(timer_); //清空定时器，防止重复操作
			var that = this;
			timer_ = setInterval(function() {
				if (that.swiperLineWidth <= point) {
					clearInterval(timer_);
				} else {
					that.swiperLineWidth = that.swiperLineWidth - 2;
					// console.log(that.swiperLineWidth);
				}
			}, 20);

			if (sync) {
				// 块左滑
				clearInterval(timer); //清空定时器，防止重复操作
				var that = this;
				timer = setInterval(function() {
					if (that.swiperLeft >= 0) {
						clearInterval(timer);
					} else {
						that.swiperLeft = that.swiperLeft + 2;
						// console.log(that.swiperLeft);
					}
				}, 10);
			}
		},

		toUserDeal() {
			uni.navigateTo({
				url: '../userDeal/userDeal'
			});
		}
	}
};
</script>

<style>
page {
	width: 100%;
	height: 100%;
}

button::after {
	border: none;
	outline: none;
}

.wx-sign-checkbox {
	margin-left: 3%;
	height: 100%;
}

/* 按百分比分配父组件区域 */
/* 如更改样式需重新计算百分比分配 */
#signin-container {
	width: 100%;
	height: 100%;
	position: relative;
}

#introduction {
	position: absolute;
	display: flex;
	justify-content: space-around;
	top: 0%;
	width: 200%;
	height: 54%;
}

.email-Box {
	position: relative;
	width: 35%;
	height: 90%;
}

.email-intro {
	position: absolute;
	top: 15%;
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 30%;
	font-size: 42upx;
	font-weight: 550;
	color: #C0C0C0;
}

.email-content {
	position: absolute;
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 50%;
	top: 50%;
}

.email-input {
	width: 100%;
	height: 40px;
	background-color: white;
	box-shadow: 0px 0px 5px 1px #c0c0c0;
	margin-top: 15px;
	font-size: 15px;
	color: #888888;
	user-select: auto;
}

.introduction-contentBox {
	position: relative;
	width: 35%;
	height: 90%;
}

.introduction-content {
	position: absolute;
	left: 5%;
	top: 4%;
	width: 90%;
	height: 86%;
}

.introduction-bottom-sign {
	position: absolute;
	bottom: 0%;
	height: 10%;
	right: -50%;
	width: 200%;
	/* background-color: #000000; */
	/* opacity: 0.8; */
	border-bottom-right-radius: 30upx;
	border-bottom-left-radius: 30upx;
}

#confirm {
	position: absolute;
	top: 52%;
	width: 100%;
	height: 10%;
}

.confirm-rel {
	position: relative;
	width: 100%;
	height: 100%;
}

.confirm-button-before {
	position: absolute;
	top: 24%;
	left: 30%;
	width: 40%;
	height: 40px;
	border-radius: 10upx;
	background-color: #cccccc;
	z-index: 20;
}

.confirm-button-visitor {
	position: absolute;
	top: 24%;
	left: 30%;
	width: 40%;
	height: 40px;
	border-radius: 10upx;
	background: linear-gradient(90deg, rgba(83, 224, 219, 1) 0%, rgba(72, 177, 233, 1) 100%);
	z-index: 20;
}


.confirm-button-checked {
	position: absolute;
	top: 24%;
	left: 30%;
	width: 40%;
	height: 40px;
	border-radius: 10upx;
	background-color: #ffcd2e;
	z-index: 20;
}

.conform-bgBox {
	position: absolute;
	top: 24%;
	left: 26%;
	width: 48%;
	height: 42px;
	background-color: #ffcd2e;
	border-radius: 10upx;
	z-index: 10;
}

.bottom-picBox {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 26%;
}

.backAngle {
	position: absolute;
	top: 24%;
	left: 15%;
	width: 40px;
	height: 40px;
	border-radius: 10upx;
	background-color: #ffcd2e;
	z-index: 20;
	background: #fdd041;
}

.back {
	width: 40px;
	height: 40px;
	background-color: #ffcd2e;
	z-index: 20;
	background: #fdd041;
	position: relative;
}

.captcha {
	margin-top: 15px;
	padding-top: 7px;
	padding-bottom: 7px;
	font-size: 15px;
	text-align: center;
	border-radius: 10upx;
	width: 130px;
	color:#FFFFFF;
	background-color:#FFCD2E;
}
</style>
