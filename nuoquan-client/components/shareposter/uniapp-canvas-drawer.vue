<template>
	<view>
		<canvas canvas-id="canvasdrawer" :style="{'width': width + 'px','height': height + 'px'}"
		class="board" v-if="showCanvas"></canvas>
	</view>
</template>

<script>
	export default {
		props: {
			painting: {
				type: Object,
			}
		},
		data() {
			return {
				showCanvas: false,
				width: 100,
				height: 100,
				tempFileList: [],
				isPainting: false,
				ctx: null,
				cache: Array(),
			}
		},
		watch: {
			painting(newVal, oldVal) {
				if (!this.isPainting) {
					if (JSON.stringify(newVal) !== JSON.stringify(oldVal)) {
						if (newVal && newVal.width && newVal.height) {
							this.showCanvas = true
							this.isPainting = true
							
							this.readyPigment()
						}
					} else {
						if (newVal && newVal.mode !== 'same') {
							// this.triggerEvent('getImage', {errMsg: 'canvasdrawer:samme params'})
							this.$emit('getImage', {errMsg: 'canvasdrawer:samme params'})
						}
					}
				}
			}
		},
		onReady() {
			uni.removeStorageSync('canvasdrawer_pic_cache')
			this.ctx = uni.createCanvasContext('canvasdrawer', this)
		},
		methods: {
			readyPigment () {
				// const { width, height, views } = this.painting
				const width = this.painting.width
				const height = this.painting.height
				const views = this.painting.views
				
				this.width = width
				this.height = height
				
				const inter = setInterval(() => {
					if (this.ctx) {
						clearInterval(inter)
						this.ctx.clearActions()
						this.ctx.save()
						this.getImagesInfo(views)
					}
				}, 100)
			},
			getImagesInfo(views) {
				const imageList = []
				for (let i = 0; i < views.length; i++) {
					if (views[i].type === 'image') {
						imageList.push(this.getImageInfo(views[i].url))
					}
				}

				const loadTask = []
				for (let i = 0; i < Math.ceil(imageList.length / 8); i++) {
					loadTask.push(new Promise((resolve, reject) => {
						Promise.all(imageList.splice(i * 8, 8)).then(res => {
							resolve(res)
						}).catch(res => {
							reject(res)
						})
					}))
				}
				Promise.all(loadTask).then(res => {
					let tempFileList = []
					for (let i = 0; i < res.length; i++) {
						tempFileList = tempFileList.concat(res[i])
					}
					this.tempFileList = tempFileList
					this.startPainting()
				})
			},
			/**
			 * 开始画图
			 */
		    startPainting () {
				
				const tempFileList = this.tempFileList
				const views = this.painting.views
				
				// const { tempFileList, painting: { views } } = this.$data
				// 判断类型按顺序画图
				for (let i = 0, imageIndex = 0; i < views.length; i++) {
					if (views[i].type === 'image') {
						this.drawImage({
							...views[i],
							url: tempFileList[imageIndex]
						})
						imageIndex++
					} else if (views[i].type === 'text') {
						if (!this.ctx.measureText) {
							uni.showModal({
								title: '提示',
								content: '当前微信版本过低，无法使用 measureText 功能，请升级到最新微信版本后重试。'
							})
							// this.triggerEvent('getImage', {errMsg: 'canvasdrawer:version too low'})
							this.$emit('getImage', {errMsg: 'canvasdrawer:version too low'})
							return
						} else {
							this.drawText(views[i])
						}
					} else if (views[i].type === 'rect') {
						this.drawRect(views[i])
					} else if (views[i].type === 'rectBorder') {
						this.drawRectBorder(views[i])
					} else if (views[i].type === 'line') {
						this.drawDividingLine(views[i])
					}
				}
				this.ctx.draw(false, () => {
					uni.setStorageSync('canvasdrawer_pic_cache', this.cache)
					const system = uni.getSystemInfoSync().system
					// Description:  ios和安卓机型都需要延迟保存图片，以解决异步问题--还是解决不了...
					// Author: Yifei
					// Date: 4 July, 2022
					//debugger
					if (/ios/i.test(system)) {
						setTimeout(()=>{
							this.saveImageToLocal()
						},800)
					} else {
						// 延迟保存图片，解决安卓生成图片错位bug。
						setTimeout(() => {
							this.saveImageToLocal()
						}, 1600)
					}
				})
		    },
			drawImage (params) {
				// this.ctx.save()
				// const { url, top = 0, left = 0, width = 0, height = 0, borderRadius = 0, deg = 0 } = params
				// if (deg !== 0) {
				// 	this.ctx.translate(left + width/2, top + height/2)
				// 	this.ctx.rotate(deg * Math.PI / 180)
				// 	this.ctx.drawImage(url, -width/2, -height/2, width, height)
				// } else {
				// 	this.ctx.drawImage(url, left, top, width, height)
				// }
				
				// this.ctx.restore()
				const {
				    type = '',
				    background,
				    top = 0,
				    left = 0,
				    width = 0,
				    height = 0,
				    radius = 0,
				    url = '',
				    sx = 0,
				    sy = 0,
				    ex = 0,
				    ey = 0
				} = params;
				let x = left;
				let y = top;
				let w = width;
				let h = height;
				let r = radius;
				this.ctx.save();
				if (radius) {
				    this.ctx.beginPath();
				    // if (radius === parseInt(width / 2)) {
				    //     console.log('圆');
				    //     this.ctx.beginPath();
				    //     this.ctx.arc(left + radius, top + radius, radius, 0, 2 * Math.PI);
				    //     this.ctx.setFillStyle(params.background || '#ffffff')
				    //     this.ctx.fill()
				    //     this.ctx.clip();
				    //     this.ctx.drawImage(url, x, y, w, h);
				    // }
				    // else {
				    // }
				    this._drawRadiusRect(params);
				    this.ctx.fill();
				    this.ctx.clip();
				}
				if ('tailor' in params) {
				    this.ctx.drawImage(url, sx, sy, ex, ey, left, top, width, height);
				}
				else {
					// console.log("draw:")
					// console.log(url)
				    this.ctx.drawImage(url, left, top, width, height);
				}
				// this.drawImage(params);
				
				this.ctx.restore();
			},
		    drawText (params) {
				this.ctx.save()
				const {
					MaxLineNumber = 2,
					breakWord = false,
					color = 'black',
					content = '',
					fontSize = 16,
					top = 0,
					left = 0,
					lineHeight = 20,
					textAlign = 'left',
					width,
					bolder = false,
					textDecoration = 'none'
				} = params // 赋默认值
				
				//设置绘画起始点，颜色字体等
				this.ctx.beginPath()
				this.ctx.setTextBaseline('top')
				this.ctx.setTextAlign(textAlign)
				this.ctx.setFillStyle(color)
				this.ctx.setFontSize(fontSize)
				//是否分行, 错行Bug关键点。每写一行他会重新计算下行的高度
				if (!breakWord) { 
					this.ctx.fillText(content, left, top)
					this.drawTextLine(left, top, textDecoration, color, fontSize, content)
				} else {
					let fillText = ''
					let fillTop = top
					let lineNum = 1
					for (let i = 0; i < content.length; i++) {
						fillText += [content[i]]
						// 苹果机型会自动换行，
						// 安卓机型会自动转换为空格。
						// 处理方式：遇到换行符，跳到下一行。
						if (this.ctx.measureText(fillText).width > width || content[i] == '\n') {
							if (lineNum === MaxLineNumber) {
								if (i !== content.length) {
									fillText = fillText.substring(0, fillText.length - 1) + '...'
									this.ctx.fillText(fillText, left, fillTop)
									this.drawTextLine(left, fillTop, textDecoration, color, fontSize, fillText)
									fillText = ''
									break
								}
							}
							this.ctx.fillText(fillText, left, fillTop)
							this.drawTextLine(left, fillTop, textDecoration, color, fontSize, fillText)
							fillText = ''
							fillTop += lineHeight
							lineNum ++
						}
					}
					this.ctx.fillText(fillText, left, fillTop)
					this.drawTextLine(left, fillTop, textDecoration, color, fontSize, fillText)
				}
		      
				this.ctx.restore()
		
				if (bolder) {
					this.drawText({
						...params,
						left: left + 0.3,
						top: top + 0.3,
						bolder: false,
						textDecoration: 'none' 
					})
				}
		    },
		    drawTextLine (left, top, textDecoration, color, fontSize, content) {
				if (textDecoration === 'underline') {
					this.drawRect({
						background: color,
						top: top + fontSize * 1.2,
						left: left - 1,
						width: this.ctx.measureText(content).width + 3,
						height: 1
					})
				} else if (textDecoration === 'line-through') {
					this.drawRect({
						background: color,
						top: top + fontSize * 0.6,
						left: left - 1,
						width: this.ctx.measureText(content).width + 3,
						height: 1
					})
				}
			},
			drawDividingLine (params){
				this.ctx.save()
				const {
					color = 'black',
					top = 0,
					left = 0,
					width,
					dashWidth = 5
				} = params
				
				this.ctx.setFillStyle(color);
				this.ctx.setLineDash([dashWidth]);
				this.ctx.beginPath();
				this.ctx.moveTo(left, top);
				this.ctx.lineTo(left + width, top);
				this.ctx.stroke()
				
				this.ctx.restore()
			},
			drawRect (params) {
				// this.ctx.save()
				// const { background, top = 0, left = 0, width = 0, height = 0 } = params
				// this.ctx.setFillStyle(background)
				// this.ctx.fillRect(left, top, width, height)
				// this.ctx.restore()
				// console.log(params)
				const { background, top = 0, left = 0, width = 0, height = 0 } = params
				this.ctx.save();
				this.ctx.setFillStyle(background);
				if (params.radius) {
				    this._drawRadiusRect(params);
				    this.ctx.fill();
				}
				else {
				    this.ctx.setFillStyle(background);
				    this.ctx.fillRect(left, top, width, height);
				}
				this.ctx.restore();
		    },
			_drawRadiusRect(params) {
			    const {
			        top = 0,
			        left = 0,
			        width = 0,
			        height = 0,
			        radius = 0,
			     } = params;
			    let x = left;
			    let y = top;
			    let w = width;
			    let h = height;
			    // let bgc = background;
			    let r = radius;
			    // let br = radius;
			    this.ctx.beginPath();
			    this.ctx.moveTo(x + r, y);    // 移动到左上角的点
			    this.ctx.lineTo(x + w - r, y);
			    this.ctx.arc(x + w - r, y + r, r, 2 * Math.PI * (3 / 4), 2 * Math.PI * (4 / 4));
			    this.ctx.lineTo(x + w, y + h - r);
			    this.ctx.arc(x + w - r, y + h - r, r, 0, 2 * Math.PI * (1 / 4));
			    this.ctx.lineTo(x + r, y + h);
			    this.ctx.arc(x + r, y + h - r, r, 2 * Math.PI * (1 / 4), 2 * Math.PI * (2 / 4));
			    this.ctx.lineTo((x), (y + r));
			    this.ctx.arc(x + r, y + r, r, 2 * Math.PI * (2 / 4), 2 * Math.PI * (3 / 4));
			
			    // this.ctx.moveTo(x + r, y);
			    // this.ctx.arcTo(x + w, y, x + w, y + h, r);
			    // this.ctx.arcTo(x + w, y + h, x, y + h, r);
			    // this.ctx.arcTo(x, y + h, x, y, r);
			    // this.ctx.arcTo(x, y, x + w, y, r);
			},
			drawRectBorder (params) {
				const {
				    top = 0,
				    left = 0,
				    width = 0,
				    height = 0,
				    radius = 0,
					color = "black"
				 } = params;
				let x = left;
				let y = top;
				let w = width;
				let h = height;
				let r = radius;
				this.ctx.save();
				this.ctx.beginPath();
				this.ctx.setStrokeStyle(color);
				this.ctx.moveTo(x + r, y);    // 移动到左上角的点
				this.ctx.lineTo(x + w - r, y);
				this.ctx.arc(x + w - r, y + r, r, 2 * Math.PI * (3 / 4), 2 * Math.PI * (4 / 4));
				this.ctx.lineTo(x + w, y + h - r);
				this.ctx.arc(x + w - r, y + h - r, r, 0, 2 * Math.PI * (1 / 4));
				this.ctx.lineTo(x + r, y + h);
				this.ctx.arc(x + r, y + h - r, r, 2 * Math.PI * (1 / 4), 2 * Math.PI * (2 / 4));
				this.ctx.lineTo((x), (y + r));
				this.ctx.arc(x + r, y + r, r, 2 * Math.PI * (2 / 4), 2 * Math.PI * (3 / 4));
				this.ctx.stroke();

				this.ctx.restore();
			},
			getImageInfo (url) {
				return new Promise((resolve, reject) => {
					if (this.cache[url]) {
						resolve(this.cache[url])
					} else {
						const objExp = new RegExp(/^http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/)
						if (objExp.test(url)) {
							uni.getImageInfo({
								src: url,
								complete: res => {
									if (res.errMsg === 'getImageInfo:ok') {
										this.cache[url] = res.path
										resolve(res.path)
									} else {
										// this.triggerEvent('getImage', {errMsg: 'canvasdrawer:download fail'})
										this.$emit('getImage', {errMsg: 'canvasdrawer:download fail'})
										reject(new Error('getImageInfo fail'))
									}
								}
							})
						} else {
							this.cache[url] = url
							resolve(url)
						}
					}
				})
		    },
			saveImageToLocal () {
				// const { width, height } = this.data
				const width = this.width
				const height = this.height
				uni.canvasToTempFilePath({
					x: 0,
					y: 0,
					width,
					height,
					canvasId: 'canvasdrawer',
					complete: res => {
						if (res.errMsg === 'canvasToTempFilePath:ok') {
							
							this.showCanvas = false
							this.isPainting = false
							this.tempFileList = []
							
							// this.triggerEvent('getImage', {tempFilePath: res.tempFilePath, errMsg: 'canvasdrawer:ok'})
							this.$emit('getImage', {tempFilePath: res.tempFilePath, errMsg: 'canvasdrawer:ok'})
						} else {
							// this.triggerEvent('getImage', {errMsg: 'canvasdrawer:fail'})
							this.$emit('getImage', {errMsg: 'canvasdrawer:fail'})
						}
					}
				}, this)
			}
		}
	}
</script>

<style>
	.board {
	  position: fixed;
	  top: 2000upx;
	}
</style>
