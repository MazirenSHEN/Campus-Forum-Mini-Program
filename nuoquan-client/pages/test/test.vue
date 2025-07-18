<!-- TODO: 取消添加图片, 标签输入不能含有特殊字符，颜色变化 -->
<template>
	<view>
		<view>{{dataList.length}}</view>
		<view style="width:50px;height:50px;background: #007AFF;" @click="load(2)">+1</view>
		<view>{{testdata}}</view>
		<view v-for="(i, index) in dataList" :key="index">{{i.title}}</view>
		<modal></modal>

				<view v-html="tr" style="border:2px solid #000000;"></view>

	</view>
</template>

<script>
	import tagSelectBox from '@/components/nq-tag/tagSelectBox.vue';
	import tagSelected from '@/components/nq-tag/tagSelected.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import plusSquare from '@/components/plusSquare.vue';
	// import nq_parse from '@/pagesSubA/longArticleDetail/parse.js'

	// #ifdef APP-PLUS
	import permision from '@/common/permission.js';
	// #endif
	var sourceType = [
		['camera'],
		['album'],
		['camera', 'album']
	];
	var sizeType = [
		['compressed'],
		['original'],
		['compressed', 'original']
	];

	var uploadFlag = false; // 标志文章正在上传，为 true 时 block 该方法
	var requestTask;
	var uploadTasks = [];
	export default {
		components: {
			tagSelectBox,
			tagSelected,
			uniNavBar,
			plusSquare,
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				testdata: "默认",
				//dataList: [],
				tr: "",
				content: "w1![image](p1)[image]w1![image](p1)[image]w2![image](p2)[image]w3![image](p4)[image]w5![image](p)[image]w6",
			}
		},
		onLoad() {
			//this.load(1);
			//this.tr = this.htmlDecodeByRegExp(this.tw);
			//console.log(this.tr);
			var str = this.content;
			//var a = /!\[([^\]<>]+)\]\(([^\)<>]+)("[^\(\)\"]+")?\)/g;
			//var stra = a.exec(str);
			
			var tmp = "**234423**  soidufah***sdkfh***  skjhfa *89* lsah ";
			// this.tr = nq_parse.parseText(tmp);

			// var result = nq_parse.parseMedia(str);
			console.log('最终结果看看');
			console.log(result);
			//console.log(stra);
		},
		computed: {

		},
		methods: {

			load(page) {
				var that = this;
				this.$store.dispatch('request', {
					mode: 'queryArticles',
					data: {
						page: page,
						userId: "oDwsO5DwqW67LTc-DIP7J-EiSHjg",
						queryType: 0,
						orderType: 0,
						selectedTag: "",
					},
					success: function(res) {
						console.log('in test success, 成功了111111111');
						console.log(res);
					},
				});
			},
			checkDraft() {
				var that = this;
				uni.getStorage({
					key: that.userInfo.id + ':draftArticle',
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
			},
			loadDraft() {
				var that = this;
				uni.getStorage({
					key: that.userInfo.id + ':draftArticle',
					success: function(dft) {
						console.log(dft);
						that.articleTitle = dft.data.articleTitle;
						that.articleContent = dft.data.articleContent;
						that.selectedTags = dft.data.selectedTags;
						that.imageList = dft.data.imageList;
						console.log('draft loaded');
					},
				});
			},
			cleanDraft() {
				var that = this;
				that.articleTitle = "";
				that.articleContent = "";
				that.selectedTags = "";
				that.imageList = "";
				uni.setStorage({
					key: that.userInfo.id + ':draftArticle',
					data: {
						statue: false,
					},
					success: function() {
						console.log('Draft cleaned');
					},
				});
			},
			getTagsList() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/getTagsList',
					method: 'POST',
					data: {},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						if (res.data.status == 200) {
							this.tagList = res.data.data;
						}
					}
				});
			},

			combineTagToString: function() {
				var finalTag = '';
				for (var i = 0; i < this.selectedTags.length; i++) {
					finalTag = finalTag + '#' + this.selectedTags[i].tag;
				}
				return finalTag;
			},

			chooseImg: async function() {
				uni.chooseImage({
					sourceType: sourceType[this.sourceTypeIndex],
					sizeType: sizeType[this.sizeTypeIndex],
					count: 9 - this.imageList.length,
					//this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
					success: res => {
						console.log(res);
					}
				});
			},
			previewImage: function(e) {
				var current = e.target.dataset.src;
				// console.log(e)
				console.log(this.imageList);
				uni.previewImage({
					current: current,
					urls: this.imageList
				});
			},

			// TODO：图片上传需加上大小限制，后台限制10M
			upload: function(e) {
				var me = this;
				if (this.isBlank(me.articleTitle) || this.isNull(me.articleTitle)) {
					uni.showToast({
						icon: 'none',
						title: '文章标题不能为空～',
						duration: 1000
					});
					return;
				}

				if (this.isBlank(me.articleContent) || this.isNull(me.articleContent)) {
					uni.showToast({
						icon: 'none',
						title: '文章内容不能为空～',
						duration: 1000
					});
					return;
				}

				if (uploadFlag) {
					console.log('正在上传...');
					return;
				}
				uploadFlag = true;
				uni.showLoading({
					title: '正在上传...'
				});

				setTimeout(() => {
					var finalTag = this.combineTagToString();

					var serverUrl = me.$serverUrl;
					requestTask = uni.request({
						url: serverUrl + '/article/uploadArticle',
						method: 'POST',
						data: {
							userId: me.userInfo.id,
							articleTag: finalTag,
							articleTitle: me.articleTitle,
							articleContent: me.articleContent
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: res => {
							// console.log(res);
							if (res.data.status == 200) {
								if (me.imageList.length > 0) {
									const articleId = res.data.data;
									var resCount = 0;
									for (var i = 0; i < me.imageList.length; i++) {
										uploadTasks[i] = uni.uploadFile({
											url: this.$serverUrl + '/article/uploadArticleImg',
											filePath: me.imageList[i],
											name: 'file',
											formData: {
												userId: me.userInfo.id,
												articleId: articleId,
												order: i
											},
											success: uploadFileRes => {
												resCount++;
												if (resCount == me.imageList.length) {
													me.uploadSuccess();
												}
											}
										});
									}
								} else {
									me.uploadSuccess();
								}
							} else if (res.data.status == 501) {
								me.contentIllegal();
							} else {
								me.uploadFail();
							}
						},
						fail: res => {
							me.uploadFail();
						}
					});
				}, 100); //延时执行等待上锁
			},

			uploadSuccess() {
				uploadFlag = false;
				uni.hideLoading();
				this.cleanDraft();
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
							url: '/pages/tabPages/index'
						});
					}, 1800);
			},

			uploadFail() {
				// 上传失败 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '出现未知错误，上传失败',
					duration: 2000,
					icon: 'none'
				});
			},

			contentIllegal() {
				// 内容非法 用户提醒
				uploadFlag = false;
				uni.hideLoading();
				uni.showToast({
					title: '内容涉嫌违规，请联系管理员。',
					duration: 2000,
					icon: 'none'
				});
			},

			//获取组件返回的已选标签值
			getselectedTag(tag) {
				var a = this.selectedTags.indexOf(tag);
				if (a == -1) {
					if (this.selectedTags.length < 3) {
						this.selectedTags.push(tag);
					} else {
						uni.showToast({
							duration: 300,
							title: '最多添加三个标签~',
							icon: 'none'
						});
					}
				} else {
					uni.showToast({
						icon: 'none',
						title: '已经添加～',
						duration: 200
					});
				}
			},

			deleteTag: function(index) {
				console.log(index);
				this.selectedTags.splice(index, 1);
				console.log(this.selectedTags.length);
			},
			deleteImg: function(index) {
				// console.log(index);
				// console.log(this.imageList[index]);
				this.imageList.splice(index, 1);
			},

			editTag(a) {
				this.editingTag = a;
			},

			showToast() {
				uni.showToast({
					// title: '⠀⠀⠀⠀⠀under⠀⠀⠀⠀⠀development',//不是空格，是特殊符号，莫删
					title: this.lang.developing,
					duration: 2000,
					icon: 'none'
				});
			}
		}
	};
</script>
<style>
	page {
		height: 100%;
	}

	.submitMain {
		width: 698upx;
		margin: auto;
	}

	.title {
		width: calc(698upx - 36px);
		height: 38px;
		border: 2px solid #fcc041;
		border-radius: 8px;
		padding-left: 12px;
		padding-right: 24px;
	}

	.title-placeholder {
		height: 14px;
		font-size: 14px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 16px;
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

	/* 已选标签部分,开始
 */
	.selectedTagsArea {
		margin-top: 13px;
		position: relative;
		margin-bottom: 8px;
	}

	.finish-button {
		margin-top: 8px;
		width: 68px;
		height: 26px;
		background: linear-gradient(318deg, rgba(251, 118, 118, 1) 0%, rgba(254, 192, 77, 1) 100%);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		font-size: 14px;
		line-height: 26px;
		color: rgba(255, 255, 255, 1);
		text-align: center;
	}

	.editTagsButton {
		vertical-align: bottom;
		margin-top: 8px;
		width: 99px;
		height: 26px;
		background: linear-gradient(318deg, rgba(251, 118, 118, 1) 0%, rgba(254, 192, 77, 1) 100%);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		border-radius: 8px;
		text-align: center;
		font-size: 14px;
		line-height: 23px;
		color: rgba(255, 255, 255, 1);
		display: inline-block;
	}

	/* .selectedTag {
	display: inline-block;
	vertical-align: bottom;
	color: #000000;
	font-size: 14px;
	line-height: 26px;
	height: 26px;

	position: relative;
	margin-right: 9px;
	margin-top: 8px;
	padding-right: 27px;
	padding-left: 12px;
	pointer-events: none;
	white-space: nowrap;
	border-radius: 20px;
}

.selectedTag::after {
	position: absolute;
	content: '✕';
	right: 10px;
	margin-left: 4px;
	color: #000000;
	font-size: 13px;
	pointer-events: auto;
	font-weight: 900;
	white-space: nowrap;
} */

	/* 已选标签,结束...待选标签部分开始,灰色部分
 */

	.tagsArea text {
		display: block;
		height: 12px;
		font-size: 12px;
		color: rgba(155, 155, 155, 1);
		padding-top: 16px;
		padding-left: 12px;
		padding-bottom: 8px;
	}

	.tag {
		display: inline-block;
		vertical-align: bottom;
		color: #ffffff;
		font-size: 14px;
		line-height: 26px;
		height: 26px;

		position: relative;
		margin-left: 8px;
		margin-top: 6px;
		padding-right: 12px;
		padding-left: 12px;
		/* 用于解决空格换行问题 */
		white-space: nowrap;

		border-radius: 20px;
	}

	/* 待选部分结束*/
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

	.picturearea {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		flex: 0 0 auto;
		margin-top: 10px;
	}

	.picturearea image {
		width: 190upx;
		height: 190upx;
		margin: 6px 0;
	}

	.addPic {
		width: 178upx;
		height: 178upx;
		line-height: 160upx;
		margin: 6px 0;
		text-align: center;
		vertical-align: middle;
		color: #888888;
		font-size: 50px;
		font-weight: 200;
		background: #ececec;
	}

	.submit-button {
		margin-top: 100px;
		width: 558upx;
		height: 42px;
		background: rgba(252, 192, 65, 1);
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		border-radius: 8px;

		font-size: 18px;
		font-weight: 500;
		color: #ffffff;
	}
</style>
