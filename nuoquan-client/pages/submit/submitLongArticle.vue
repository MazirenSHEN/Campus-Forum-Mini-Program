<!--
Description:  新建长文章页面
Author: yaoyao
Date: 17 Aug, 2020
-->
<template>
	<view id="submitLongArticle">
		<uni-nav-bar class="navigationBar" :style="{ height: this.getnavbarHeight() + 'px' }" :showLeftIcon="true" :isNavHome="isNavHome"
		 :left-text="lang.back" :title="lang.tabList[0]" :height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }"></view>

		<view class="submitMain">

			<!-- 标题 -->
			<view style="position: relative;margin-top: 20px;width: 100%;">
				<input class="title" v-model="articleTitle" :placeholder="lang.addTitle" :maxlength="maxTitleLength"
				 placeholder-class="title-placeholder" />
				<view class="titleTextLeft">{{ maxTitleLength - articleTitle.length }}</view>
			</view>
			<!-- 			副标题和封面图 -->
			<view class="additionalInfo">
				<view style="position: relative;margin-top: 20px;width: calc(100% - 124px - 12px);display: inline-block;">
					<textarea class="subTitle" v-model="subTitle" placeholder="添加副标题" :maxlength="maxSubTitleLength" placeholder-class="title-placeholder" ></textarea>
					<view class="titleTextLeft">{{ maxSubTitleLength - subTitle.length }}</view>
				</view>

				<view class="cover_image_box">
					<view v-if="!cover_image" @tap="chooseCoverImg()">选择封面图片</view>
					<image :src="pathFilter(cover_image)" v-if="cover_image" mode="aspectFill"></image>
				</view>
			</view>
			<!-- 			已保存文章区域-->
			<view class="contentFlow">
				<long-article-component v-for="(i,index) in savedArticleContent" :key="i.hash" :item="i" :order="index" :editable="componentsEditable"
				 @deleteBlock="deleteBlockReceiver"></long-article-component>
			</view>
			<!--输入框-->
			<view style="position: relative;" v-if="showInputBox">
				<textarea class="content" v-model="typingArticleContent" :maxlength="remainingContentLength" :auto-height="true"
				 :show-confirm-bar="false" @blur="saveTextFromInput"></textarea>
				<view style="position: absolute;bottom: 8px;right:8px;font-size: 11px;color:#888888;">{{ remainingContentLength - typingArticleContent.length }}</view>
				<image src="../../static/icon/emoji.png" style="position: absolute;left:12px;top:8px;width:20px;height:20px;"
				 @click="showToast()"></image>
			</view>

			<float-menu @selectionFinish='chooseType' @selectionStart="syncOpenFlag"></float-menu>

			<!--提交/编辑删除块/完成编辑  按钮-->
			<button v-if="!componentsEditable&&savedArticleContent.length != 0" class="submit-button" :class="{'choosingStatusButton':showTypeSelector}"
			 @tap="submitLongArticle">{{ lang.submit }}</button>
			<button v-if="!componentsEditable&&savedArticleContent.length != 0" class="submit-button" @tap="controlComponentsDisplayMode()">编辑</button>
			<button v-if="componentsEditable"  class="submit-button" @tap="controlComponentsDisplayMode()">退出编辑</button>
			<view style="width: 100%; height: 200px;"></view>
		</view>
		<modal></modal>
	</view>
</template>

<script>
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import longArticleComponent from '@/components/nq-card/long_article_component.vue'
	import floatMenu from '@/components/float_menu.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex';
	export default {
		components: {
			uniNavBar,
			longArticleComponent,
			floatMenu,
		},
		data() {
			return {
				userInfo: '',

				//客户待储存内容相关
				articleTitle: '',
				subTitle: '',
				cover_image: '',
				savedArticleContent: [], //已储存展示的输入内容
				typingArticleContent: "", //输入框中数据

				//辅助显示变量
				maxTitleLength: 20,
				maxSubTitleLength: 20,
				maxContentLength: 999,
				remainingContentLength: 999,

				//控制显示变量	--页面控制相关
				showTypeSelector: false, //控制  文本/图片/视频  选择菜单
				showInputBox: true, //控制	文本输入框
				componentsEditable: false, //display/edit,控制循环组件是否显示删除符号
				upLoadFlag: false,

				navbarHeight: '', //一次性储存 navbar 高度

			}
		},
		computed: {
			...mapState(['lang'])
		},
		onLoad() {
			// 一次性储存 navbar 高度
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			this.userInfo = this.getGlobalUserInfo();
			this.checkDraft();
		},
		onUnload() {
			var that = this;
			if (this.articleTitle != "" || this.subTitle != "" || this.cover_image != "" || this.savedArticleContent.length > 0) {
				console.log('start storge draft');
				uni.setStorage({
					key: that.userInfo.id + ':draftLongArticle',
					data: {
						status: true,
						articleTitle: that.articleTitle,
						subtitle: that.subTitle,
						cover_image: that.cover_image,
						savedArticleContent: that.savedArticleContent,
						remainingContentLength: that.remainingContentLength,
					},
					success: function() {
						console.log('Draft longArticle saving success');
					}
				})
			} else {
				_this.cleanDraft();
			}
		},
		methods: {
			submitLongArticle() {
				var that = this;
				if (!this.checkLongArticle()) {
					console.log('check fail, stop submit');
					return;
				};
				if (this.showInputBox) {
					this.saveTextFromInput();
				}
				if (this.upLoadFlag) {
					console.log('正在上传...');
					return;
				}
				this.uploadFlag = true;
				uni.showLoading({
					title: '正在上传...'
				});
				var preparedContent = this.covertContent();
				console.log(preparedContent);
				uni.request({
					url: this.$serverUrl + '/longarticle/uploadArticle',
					method: 'POST',
					data: {
						userId: that.userInfo.id,
						articleTitle: that.articleTitle,
						subTitle: that.subTitle,
						coverImage: that.cover_image,
						articleContent: preparedContent,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						if (res.data.status == 200) {
							that.uploadSuccess();
						} else {
							that.uploadFail();
						}
					},
				});
			},
			checkLongArticle() {
				if (this.articleTitle == "" || this.savedArticleContent == []) {
					uni.showToast({
						title: '好像忘记填什么东西了哦~~~',
						duration: 1500,
						icon: none,
					});
					return false;
				} else {
					return true;
				}

			},
			uploadSuccess() {
				this.cleanDraft();
				uni.hideLoading();
				uni.$emit('flash'); // 给 index 发送刷新信号
				uni.navigateBack({
					delta: 1
				});
				uni.showToast({
						title: '已提交审核',
						duration: 2000,
						icon: 'success'
					}),
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/tabPages/votePage'
						});
					}, 1800);
			},
			uploadFail() {
				// 上传失败 用户提醒

				uni.hideLoading();
				uni.showToast({
					title: '出现未知错误，上传失败',
					duration: 2000,
					icon: 'none'
				});
			},
			covertContent() { //已储存的文本转换为大文本，准备上传
				//todo 是否在每个拼接后加换行		\n  
				var i = 0;
				var convertedContent = "";
				for (var i = 0; i < this.savedArticleContent.length; i++) {
					if (this.savedArticleContent[i].mode == 'text') {
						convertedContent = convertedContent + this.savedArticleContent[i].content;
					} else if (this.savedArticleContent[i].mode == 'image') {
						convertedContent = convertedContent + '![jumbox-image](' + this.savedArticleContent[i].content +
							')[jumbox-image]';
					} else if (this.savedArticleContent[i].mode == 'video') {
						convertedContent = convertedContent + '![jumbox-video](' + this.savedArticleContent[i].content +
							')[jumbox-video]';
					}
					console.log(convertedContent);
				};
				return convertedContent;
			},

			chooseType(type) { //选择新加块的样式
				console.log(type);
				if (type == 'text') {
					this.showInputBox = true;
				} else if (type == 'image') {
					this.chooseContentImg();
				} else if (type == 'video') {
					this.chooseContentVideo();
				}
			},
			saveTextFromInput(e) {
				this.savedArticleContent.push({
					hash: this.createHash(),
					mode: 'text',
					content: this.typingArticleContent
				});
				this.remainingContentLength = this.remainingContentLength - this.typingArticleContent.length; //更新剩余可输入文本长度
				this.showInputBox = false;
				this.typingArticleContent = "";
			},
			chooseCoverImg: async function() { //插入图片
				var that = this;
				uni.chooseImage({
					sourceType: '相册',
					sizeType: '压缩',
					count: 1,
					success: res => {
						console.log(res.tempFilePaths[0]);
						uni.uploadFile({
							url: this.$serverUrl + '/resource/uploadFile',
							filePath: res.tempFilePaths[0],
							name: 'file',
							success: uploadFileRes => {
								//返回服务器地址
								console.log(uploadFileRes);
								var addressObj = JSON.parse(uploadFileRes.data);
								console.log(addressObj);
								that.cover_image = addressObj.data;
								console.log(that.cover_image);
								that.scrollToBottom();
							},
							fail: res => {
								console.log(res);
							},
						});
					}
				});
			},
			chooseContentImg: async function() { //插入图片
				var that = this;
				uni.chooseImage({
					sourceType: '相册',
					sizeType: '压缩',
					count: 1,
					success: res => {
						console.log(res);
						console.log(res.tempFilePaths[0]);
						uni.uploadFile({
							url: this.$serverUrl + '/resource/uploadFile',
							filePath: res.tempFilePaths[0],
							name: 'file',
							success: uploadFileRes => {
								//返回服务器地址
								console.log(uploadFileRes);
								var addressObj = JSON.parse(uploadFileRes.data);
								that.savedArticleContent.push({
									hash: that.createHash(),
									mode: "image",
									content: addressObj.data,
								})
								that.scrollToBottom();
							},
							fail: res => {
								console.log(res);
								// debugger;
							},
						});
					}
				});
			},
			chooseContentVideo: async function() {
				// debugger;
				var that = this;
				uni.chooseVideo({
					sourceType: ['album'],
					success: res => {

						uni.uploadFile({
							url: this.$serverUrl + '/resource/uploadFile',
							filePath: res.tempFilePath,
							name: 'file',
							success: uploadFileRes => {
								//返回服务器地址
								console.log(uploadFileRes);
								var addressObj = JSON.parse(uploadFileRes.data);
								that.savedArticleContent.push({
									hash: that.createHash,
									mode: "video",
									content: addressObj.data,
								})
							},
						});

					},
				})
			},
			controlComponentsDisplayMode() {
				this.componentsEditable = !this.componentsEditable;
			},
			deleteBlockReceiver(order) {
				var that = this;
				if (this.savedArticleContent[order].mode == ('image' || 'video')) {
					console.log('need delete file');
					uni.request({
						url: that.$serverUrl + '/resource/uploadFile',
						method: 'POST',
						data: {
							keys: that.savedArticleContent[order].content,
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							console.log('delete file success');
						}
					})
				}; //删除文件接口
				if (this.savedArticleContent[order].mode == 'text') {
					this.remainingContentLength += this.savedArticleContent[order].content.length;
				}; //删除文字后恢复剩余可输入长度
				var newArticleList = this.savedArticleContent;
				newArticleList.splice(order, 1);
				console.log(newArticleList);

				this.savedArticleContent = [];
				var oldArticleList = this.savedArticleContent;
				this.savedArticleContent = oldArticleList.concat(newArticleList);
				console.log(this.savedArticleContent); //更新数组，用于触发v-for刷新
			},
			scrollToBottom() {
				uni.createSelectorQuery().select("#submitLongArticle").boundingClientRect(data => { //目标节点
					uni.pageScrollTo({
						duration: 0, //过渡时间必须为0，uniapp bug，否则运行到手机会报错
						scrollTop: data.top, //滚动到实际距离是元素距离顶部的距离减去最外层盒子的滚动距离
					})
				}).exec();
			},
			createHash() {
				var chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
				];
				var res = "";
				for (var i = 0; i < 10; i++) {
					var id = Math.ceil(Math.random() * 35);
					res += chars[id];
				}
				return res;
			},
			checkDraft() {
				var that = this;
				uni.getStorage({
					key: that.userInfo.id + ':draftLongArticle',
					success: function(dft) {
						console.log("读取文章报告成功");
						//缓存读取成功，
						if (dft.data.status) {
							console.log("开始了");
							that.$store.commit('showModal', {
								title: that.lang.postDraftModal,
								confirmText: that.lang.yes,
								cancelText: that.lang.no,
								success: function(res) {
									that.loadDraft();
								},
								fail: function(res) {
									that.cleanDraft();
								},

							})

						}

					}
				})
			}, //checkDraft
			loadDraft() {
				var that = this;
				uni.getStorage({
					key: that.userInfo.id + ':draftLongArticle',
					success: function(dft) {
						console.log(dft);
						that.articleTitle = dft.data.articleTitle;
						that.subtitle = dft.data.subTitle;
						that.cover_image = dft.data.cover_image;
						that.savedArticleContent = dft.data.savedArticleContent;
						that.remainingContentLength = dft.data.remainingContentLength;
						console.log('draft loaded');
					},
				});
			},

			cleanDraft: function() {
				var that = this;
				that.articleTitle = "";
				that.subTitle = "";
				that.cover_image = "";
				that.savedArticleContent = [];
				uni.setStorage({
					key: that.userInfo.id + ':draftLongArticle',
					data: {
						statue: false,
					},
					success: function() {
						console.log('Draft cleaned');
					},
				});
			},
			syncOpenFlag(newFlag) {
				this.showTypeSelector = newFlag;
			},

		},
	}
</script>

<style>
	.submitMain {
		width: 698upx;
		margin: auto;
	}

	.submit-button {
		margin-top: 32px;
		width: 558upx;
		height: 42px;
		background: rgba(252, 192, 65, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		font-size: 18px;
		font-weight: 500;
		color: #ffffff;
		transition: all 0.5s ease-out 0s;
	}

	.title {
		width: calc(100% - 40px);
		height: 38px;
		border: 2px solid #fcc041;
		border-radius: 8px;
		padding-left: 12px;
		padding-right: 24px;
		font-size: 14px;
	}

	.title-placeholder {
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 24px;
		color: rgba(199, 199, 199, 1);

	}

	.titleTextLeft {
		position: absolute;
		right: 8px;
		top: 15px;
		width: 12px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		color: rgba(199, 199, 199, 1);
	}

	.subTitle {
		width: calc(100% - 36px);
		padding: 9px 0;
		font-size: 14px;
		line-height: 24px;
		height: 72px;
		border: 2px solid #fcc041;
		border-radius: 8px;
		padding-left: 12px;
		padding-right: 24px;
	}

	.cover_image_box {
		overflow: hidden;
		width: 120px;
		height: 90px;
		margin-left: 12px;
		margin-top: 20px;
		display: inline-flex;
		border: 2px solid #fcc041;
		border-radius: 8px;
		vertical-align: top;
		justify-content: center;
		align-items: center;
	}

	.cover_image_box image {
		width: 100%;
		height: 100%;
	}

	.add_block_type_selction_area {
		width: 200px;
		display: flex;
		justify-content: space-between;
	}

	.contentFlow {
		margin-top: 20px;
	}

	.content {
		min-height: 51px;
		max-height: 300px;
		margin-top: 13px;
		width: calc(100% - 12px);
		overflow: scroll;
		padding: 36px 4px 24px;
		border: 2px solid rgba(252, 192, 65, 1);
		border-radius: 8px;
		font-size: 14px;
	}

	.choosingStatusButton {
		margin-top: 92px;
		animation-fill-mode: forwards;
		transition: all 0.5s ease-out 0s;
	}
</style>
