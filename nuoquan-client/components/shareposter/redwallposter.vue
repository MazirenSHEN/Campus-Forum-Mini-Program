<template>
	<view>
		<!-- <view >
			<view style="position: absolute;width:210px;height:240px;left:45px;top:45px;background:#34DF78;"></view>
		</view> -->
		<!-- <posters-layer :postersData="postersData" @success="onSuccessCreatePosters" @error="onPostersError"></posters-layer>
		<image class="shareimage" mode="scaleToFill" :src="posterImg.path"></image> -->
		<!-- <canvas
			class="shareimage"
			canvas-id="canvasdrawer"
			:style="{width: width + 'px', height: height + 'px'}"
		>
		</canvas> -->
		<view
			@click="unShow()"
			style="position: fixed;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
				background-color: #000000;
				opacity: 0.3;
				z-index: 35;"
		></view>

		<view>
			<image :src="shareImage" class="shareimage" mode="aspectFit"></image>
			<canvasdrawer :painting="painting" class="canvasdrawer" @getImage="eventGetImage" />
			<button class="backButton super_center" @click="unShow"><image src="../../static/icon/arrow-left-888888.png" mode="aspectFit"></image></button>
			<button class="shareButton column_center" @click="eventSave">
				<image src="../../static/icon/download-alt-ffffff.png" mode="aspectFit"></image>
				<text>{{ lang.sharetoMoments }}</text>
			</button>
		</view>
	</view>
</template>
<script>
import postersLayer from './posterslayer.vue';
import { pathToBase64, base64ToPath } from '../../common/image-tools/index.js';
import canvasdrawer from './uniapp-canvas-drawer.vue';
import { mapState, mapMutations } from 'vuex';

export default {
	props: {
		team: '', // 传进组的照片
	},

	components: {
		postersLayer,
		canvasdrawer
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			// postersData: {},
			// posterImg: {},

			// width: 320, // 画布宽度
			// height: 320, // 画布长度
			// context: '', // canvas 环境

			painting: {},
			shareImage: ''
		};
	},

	onReady() {
		// 开始画图
		this.eventDraw();
	},

	methods: {
		unShow() {
			uni.hideLoading();
			this.$emit('unShow');
		},

		eventDraw() {
			uni.showLoading({
				title: '绘制分享图片中',
				mask: false
			});

			if (this.team != null) {
				this.drawPoster();
			}else {
				throw new Error('Illegal object to draw');
			}
		},

		eventSave() {
			uni.saveImageToPhotosAlbum({
				filePath: this.shareImage,
				success(res) {
					uni.showToast({
						title: '保存图片成功',
						icon: 'success',
						duration: 2000
					});
				}
			});
		},

		eventGetImage(event) {
			uni.hideLoading();
			console.log(event);
			const { tempFilePath, errMsg } = event;
			// const result = event.detail.__args__
			// const tempFilePath = result[0].tempFilePath
			// const errMsg = result[0].errMsg
			if (errMsg === 'canvasdrawer:ok') {
				this.shareImage = tempFilePath;
				this.painting = {};
			}
		},

		/**
		 * 对应微信小程序生成二维码的场景B
		 * @param {Object} page 跳转页面，默认为空
		 * @param {Object} scene 携带参数
		 * @param {Object} width 二维码尺寸
		 * @param {Object} isHyaline 底色是否透明
		 */
		getQrcodeUnlimit(page, scene, width, isHyaline) {
			var that = this;
			return new Promise((resolve, reject) => {
				console.log('请求二维码');
				uni.request({
					url: this.$serverUrl + '/wechat/getQrcodeUnlimit',
					method: 'POST',
					data: {
						page: page,
						scene: scene,
						width: width,
						isHyaline: isHyaline
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						// console.log(res);
						var QrCode = 'data:image/png;base64,' + res.data.data;
						base64ToPath(QrCode)
							.then(path => {
								console.log(path);
								console.log('获得二维码成功');
								resolve(path);
							})
							.catch(error => {
								console.error(error);
								reject(new Error('getImageInfo fail'));
							});
					},
					fail: res => {
						console.log('获得二维码失败');
						reject(new Error('getImageInfo fail'));
					}
				});
			});
		},

		drawPoster() {
			// 先请求二维码
			// 部署的时候换成 'pagesSubEvent/eventVote/eventVote' 等待审核通过
			// 测试先用这个 'pagesSubA/detail/detail'
			this.getQrcodeUnlimit('pages/tabPages/index', 'none', 200, true).then(qrCodePath => {
				// 成功获取到二维码了，开始对画布内容赋值。
				// 把该列表传给组件，组件通过类型及位置信息画图。
				this.painting = {
					width: 760,
					height: 1070,
					clear: true,
					views: [
						{
							type: 'image',
							width: 760,
							height: 1070,
							top: 0,
							left: 0,
							url: "https://nqbucket-1258460770.cos.ap-shanghai.myqcloud.com/nqstatic/redWallEvent/poster_bk.jpg"
						},
						{
							//小组图片
							type: 'image',
							width: 637,
							height: 418,
							top: 278,
							left: 54,
							radius: 0,
							url: this.pathFilter(this.team.thisCard.img)
						},
						{
							//组名
							type: 'text',
							content: this.team.title,
							fontSize: 27,
							color: '#353535',
							textAlign: 'right',
							top: 216,
							left: 700,
							bolder: false
						},
						{
							//宣言
							type: 'text',
							content: "快来给我们组投票吧！",
							fontSize: 23,
							color: '#353535',
							textAlign: 'left',
							top: 780,
							left: 168,
							bolder: false
						},
						{	//二维码背景
							type: 'rect',
							width: 148,
							height: 148,
							top: 839,
							left: 307,
							background: '#ebebe9',
							radius: 0
						},
						{
							//二维码
							type: 'image',
							width: 148,
							height: 148,
							top: 839,
							left: 307,
							url: qrCodePath
						},
					]
				};
			});
		},

		/**
		 * 返回字符串的行数
		 * @param {Object} str 待测字符串
		 * @param {Object} fontSize 字体大小
		 * @param {Object} width 行宽
		 * @return {type} 行数
		 */
		getLines(str, fontSize, width) {
			const ctx = uni.createCanvasContext('myCanvas');
			ctx.setFontSize(fontSize);
			let fillText = '';
			let lineNum = 1;
			for (let i = 0; i < str.length; i++) {
				fillText += [str[i]];
				// 苹果机型会自动换行，
				// 安卓机型会自动转换为空格。
				// 处理方式：遇到换行符，跳到下一行。
				if (ctx.measureText(fillText).width > width || str[i] == '\n') {
					fillText = '';
					lineNum++;
				}
			}
			return lineNum;
		}
	}
};
</script>

<style>
/* .posters-layer {
	position: fixed;
	background-color: #007aff;
	top: 200rpx;
	left: 100rpx;
	z-index: 30;
} */

.shareimage {
	width: 760upx;
	height: 1070upx;
	position: fixed;
	z-index: 40;
	left: 0;
	right: 0;
	top: 18%;
	margin-left: auto;
	margin-right: auto;
}

.backButton {
	position: fixed;
	z-index: 40;
	left: calc((100% - 320px) / 2);
	bottom: 40px;
	width: 49px;
	height: 49px;
	background: rgba(255, 255, 255, 1);
	opacity: 1;
	border-radius: 8px;
}

.backButton image {
	width: 36px;
	height: 36px;
	opacity: 1;
}

.shareButton {
	position: fixed;
	right: calc((100% - 320px) / 2);
	bottom: 40px;
	z-index: 40;
	min-width: 164px;
	height: 49px;
	background: rgba(252, 192, 65, 1);
	box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.16);
	opacity: 1;
	border-radius: 8px;
}

.shareButton image {
	width: 20px;
	height: 20px;
	opacity: 1;
}

.shareButton text {
	margin-left: 8px;
	min-width: 104px;
	height: 17px;
	font-size: 17px;
	font-family: Source Han Sans CN;
	font-weight: 400;
	line-height: 17px;
	color: rgba(255, 255, 255, 1);
	opacity: 1;
}
</style>
