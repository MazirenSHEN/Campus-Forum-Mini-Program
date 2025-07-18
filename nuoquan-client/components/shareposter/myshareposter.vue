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
		articleCard: '', // 传进文章基本信息
		voteCard: '' // 传进文章基本信息
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

			if (this.articleCard != null) {
				this.drawArticle();
			} else if (this.voteCard != null) {
				this.drawVote();
			} else {
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
								// console.log(path);
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

		drawArticle() {
			// 先请求二维码
			this.getQrcodeUnlimit('pagesSubA/detail/detail', this.articleCard.id, 200, true).then(qrCodePath => {
				// 成功获取到二维码了，开始对画布内容赋值。
				// 把该列表传给组件，组件通过类型及位置信息画图。
				this.painting = {
					width: 500,
					height: 500,
					clear: true,
					views: [
						{
							type: 'image',
							width: 500,
							height: 500,
							top: 0,
							left: 0,
							url: '/static/BG/1-blue.png'
						},
						{
							type: 'rect',
							width: 324,
							height: 366,
							top: 67,
							left: 88,
							background: '#FFFFFF',
							radius: 20
						},
						{
							//头像
							type: 'image',
							width: 37,
							height: 37,
							top: 67 + 23,
							left: 88 + 31,
							radius: 18,
							url: this.pathFilter(this.articleCard.faceImg)
						},
						{
							//昵称
							type: 'text',
							content: this.articleCard.nickname,
							fontSize: 17,
							color: '#9A9A9A',
							textAlign: 'left',
							top: 67 + 31,
							left: 88 + 82,
							bolder: false
						},
						{
							//标题
							type: 'text',
							content: this.articleCard.articleTitle,
							fontSize: 22,
							lineHeight: 25,
							color: '#353535',
							textAlign: 'left',
							top: 67 + 80,
							left: 88 + 29,
							width: 266,
							MaxLineNumber: 1,
							breakWord: true,
							bolder: true
						},
						{
							//内容
							type: 'text',
							content: this.articleCard.articleContent,
							fontSize: 15,
							lineHeight: 22,
							color: '#888888',
							textAlign: 'left',
							top: 67 + 128,
							left: 88 + 29,
							width: 266,
							MaxLineNumber: 5,
							breakWord: true,
							bolder: false
						},
						{
							//分割线
							type: 'line',
							color: '#B1B1B1',
							top: 67 + 250,
							left: 88 + 27,
							width: 270,
							dashWidth: 5
						},
						{
							//二维码
							type: 'image',
							width: 70,
							height: 70,
							top: 67 + 269,
							left: 88 + 27,
							url: qrCodePath
						},
						{
							type: 'text',
							content: '我在Stoa的分享扫码查看完整内容',
							fontSize: 15,
							lineHeight: 25,
							color: '#888888',
							textAlign: 'right',
							top: 67 + 282,
							left: 500 - 88 - 28,
							width: 150,
							MaxLineNumber: 2,
							breakWord: true,
							bolder: false
						}
					]
				};
			});
		},

		drawVote() {
			console.log(this.voteCard);
			this.getQrcodeUnlimit('pages/tabPages/votePage', this.voteCard.id, 200, true).then(qrCodePath => {
				// 成功获取到二维码了，开始对画布内容赋值。
				// 把该列表传给组件，组件通过类型及位置信息画图。
				const width = 325; // poster width
				var height = 150; // poster basic height
				const leftMargin = 29
				const objectWeight = 266; //常用对象宽度
				var objectTop = 28; //计算当前对象长度
				var paintObjects = []; //要画的对象

				// 加入标题
				var titleLineHeight = 25;
				var titleLines = this.getLines(this.voteCard.voteTitle, 22, objectWeight);
				var title = {
					//标题
					type: 'text',
					content: this.voteCard.voteTitle,
					fontSize: 22,
					lineHeight: titleLineHeight,
					color: '#353535',
					textAlign: 'left',
					top: objectTop,
					left: leftMargin,
					width: objectWeight,
					MaxLineNumber: 1,
					breakWord: true,
					bolder: true
				};
				
				objectTop += titleLineHeight * titleLines + 16;
				// 加入内容
				var contentLineHeight = 15;
				var contentLines = this.getLines(this.voteCard.voteContent, contentLineHeight, objectWeight);
				var content = {
					//内容
					type: 'text',
					content: this.voteCard.voteContent,
					fontSize: contentLineHeight,
					lineHeight: 22,
					color: '#353535',
					textAlign: 'left',
					top: objectTop,
					left: leftMargin,
					width: objectWeight,
					MaxLineNumber: 5,
					breakWord: true,
					bolder: false
				};

				// 加入选项
				objectTop += contentLineHeight * contentLines + 28;
				var optionGap = 44; //两个选项顶部的距离
				var options = [];
				for (let option of this.voteCard.optionList) {
					var object = [
						{
							//选项框框
							type: 'rectBorder',
							width: objectWeight,
							height: 38,
							top: objectTop,
							left: leftMargin,
							color: '#EEEEEE',
							radius: 8
						},{
							//选项内容
							type: 'text',
							content: option.optionContent,
							fontSize: 15,
							lineHeight: 22,
							color: '#353535',
							textAlign: 'left',
							top: objectTop+12,
							left: leftMargin+20,
							width: 200,
							MaxLineNumber: 1,
							breakWord: true,
							bolder: false
						}
					];
					options = options.concat(object);
					objectTop += optionGap;
				}

				// 加上模板
				height += objectTop;
				var template = [
					{
						//背景
						type: 'rect',
						width: width,
						height: height,
						top: 0,
						left: 0,
						background: '#FFFFFF',
						radius: 20
					},
					{
						//分割线
						type: 'line',
						color: '#B1B1B1',
						top: height - 107,
						left: leftMargin,
						width: 270,
						dashWidth: 5
					},
					{
						//二维码
						type: 'image',
						width: 70,
						height: 70,
						top: height - 94,
						left: leftMargin,
						url: qrCodePath
					},
					{
						type: 'text',
						content: '扫码前往Stoa参与讨论',
						fontSize: 15,
						lineHeight: 25,
						color: '#888888',
						textAlign: 'right',
						top: height - 72,
						left: width - 37,
						width: 145,
						MaxLineNumber: 2,
						breakWord: true,
						bolder: false
					}
				];

				paintObjects = paintObjects.concat(template);
				paintObjects = paintObjects.concat(options);
				paintObjects.push(title);
				paintObjects.push(content);

				this.painting = {
					//画的数据
					width: width,
					height: height,
					clear: true,
					views: paintObjects
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
	width: 320px;
	height: 320px;
	position: fixed;
	z-index: 40;
	left: 0;
	right: 0;
	top: 22%;
	margin-left: auto;
	margin-right: auto;
}

.backButton {
	position: fixed;
	z-index: 40;
	left: calc((100% - 320px) / 2);
	top: calc(26% + 320px);
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
	top: calc(26% + 320px);
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
