<template>
	<view class="detail-page">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" :left-text="lang.back" :title="lang.detail" :showLeftIcon="true" :isNavHome="isNavHome"
		 :height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }" style="width: 100%;"></view>

		<!-- 第一个大块，文章本体 -->
		<detail_article class="article-area" :articleCard="articleCard" :userInfo="userInfo"></detail_article>

		<!-- 分享海报 -->
		<view v-if="share">
			<mySharePoster :articleCard="articleCard" @unShow="toggleShare"></mySharePoster>
		</view>


		<view style="border-bottom: 4px solid #ECECEC;
			height:0;
			width:750upx;
			font-size: 0;
			position: relative;
			left: -16px;">这是分割线</view>
		<!--第一个大块二，评论区域-->

		<commentarea class="comment-area" :commentList="commentList" :commentNum="articleCard.commentNum" @onChange="changeType"
		 @like="swLikeComment" @goToCommentDetail="goToCommentDetail" :articleId="articleId" :userInfo="userInfo"></commentarea>
		<view style="width:100%;height:88px;"></view><!--占位块，上下对称-->
		<view v-if="showInput" style="width: 100%;height: 160px;"></view><!-- 占位，评论框弹起出现 -->
		<!--触底提示和功能  start-->
<!-- 		<view class="comment-bottom" v-if="control_scroll_button_flag">
			<view class="comment-bottom-notice">划到底部啦</view>
			<view class="comment-bottom-buttons">
				<image class="back" @tap="backToLastPage" src="../../static/icon/arrow-left-fcc041.png" mode="aspectFit"></image>
				<image class="to-top" @tap="scrollToTop()" src="../../static/icon/arrow-left-fcc041.png"></image>
			</view>
		</view> -->
		<!--触底提示和功能  END-->

		<!-- <view class="bottoLayerOfInput" v-show="showInput" @tap="toggleMenu('input')" @touchmove="toggleMenu('input')">
			<view class="commentPart" :style="{ bottom: textAreaAdjust }">
			
				<view class="submit" @tap="saveComment()">{{lang.send}}</view>
				<view class="submit"  style="color:#C3C3C3;right:83px;">{{lang.cancle}}</view>
				<view class="commentSth">
					<textarea class="comment-text" :placeholder="placeholderText" :focus="writingComment" auto-height="true"
					 adjust-position="false" v-model="commentContent" :show-confirm-bar="false" @focus="popTextArea" @blur="unpopTextArea"
					 cursor-spacing="20" />
					<view class="word-count-left">{{ 140 - commentContent.length }}</view>
				</view>
			</view>
		</view> -->
		
		<!-- 评论框 -->
		<nq-commit-area :isShow="showInput" 
		:userInfo="articleCard.userId" 
		@submit="saveComment" 
		@killCommitArea="resetInput" 
		:openOrigin="'detail'"></nq-commit-area>
		
		<view class="menu-bar">
			<view class="like" :class="{'liked': articleCard.isLike}" @tap="swLikeArticle()" style="border-radius:8px 0 0 8px;">
				<image v-if="!articleCard.isLike" src="../../static/icon/heart_353535.png" mode="aspectFit"></image>
				<image v-if="articleCard.isLike" src="../../static/icon/heart_ffffff.png" mode="aspectFit"></image>
				{{ articleCard.likeNum }}
			</view>
			<view class="comment" :class="{'commented': showInput}" @tap="toggleMenu('input')">
				<image v-if="!showInput" src="../../static/icon/comment-alt-888888.png" mode="aspectFit"></image>
				<image v-if="showInput" src="../../static/icon/comment-alt-dots-ffffff.png" mode="aspectFit"></image>
				{{ articleCard.commentNum }}
			</view>
			<view class="menu-more" :class="{'clicked_more': menu_status.more}" @tap="toggleMenu('more')" style="border-radius:0 8px 8px 0;">
				<image v-if="!menu_status.more" src="../../static/icon/ellipsis-h-888888.png" mode="aspectFit"></image>
				<image v-if="menu_status.more" src="../../static/icon/check-pending.png" mode="aspectFit"></image>
				{{lang.menu_more}}
			</view>
			<view class="menu_more_items" v-if="menu_status.more">
				<view class="menu_more_item" @tap="reportArticle(),toggleMenu('reset')">
					<image src="../../static/icon/report-alt.png"></image>
					{{lang.report}}
				</view>
				<view class="menu_more_item" @tap="toggleCollect(),toggleMenu('reset')">
					<image v-if="!articleCard.isCollect" src="../../static/icon/star-888888.png"></image>
					<image v-if="articleCard.isCollect" src="../../static/icon/star-full-fcc041.png"></image>
					{{lang.collect}}
				</view>
				<view class="menu_more_item" @tap="scrollToTop(),toggleMenu('reset')">
					<image src="../../static/icon/top-arrow-to-top-888888.png"></image>
					{{lang.backToTop}}
				</view>
				<view class="menu_more_item" @tap="backToLastPage">
					<image src="../../static/icon/angle-left-888888.png"></image>
					{{lang.backToLastPage}}
				</view>
			</view>
		</view>

		<!--常驻input-->
		<!-- <view class="permanent_input_BG" v-if="!showInput" @click="controlInput(1)">
			<input class="permanent_input" :placeholder="lang.engageComment" v-model="commentContent" disabled="true" min-height="10px" />
		</view>
		<view style="height:80px;width:100%"></view> -->
		<!-- 占位块 -->
	</view>
</template>

<script>
	import detail_article from './detail_article.vue';
	import commentarea from '@/components/nq-comment/commentarea.vue';
	import mySharePoster from '@/components/shareposter/myshareposter.vue';
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import {
		mapState,
		mapMutations
	} from 'vuex';
	import nqCommitArea from '@/components/nq-commitArea/nq-commitArea.vue'

	var uploadFlag = false;
	export default {
		components: {
			detail_article,
			commentarea,
			uniNavBar,
			mySharePoster,
			nqCommitArea,
		},
		data() {
			return {
				userInfo: {},
				articleCard: '', //detail的主角，由index传过来的单个文章信息
				articleId:'',
				commentContent: '', //用户准备提交的评论内容
				commentList: [], //返回值，获取评论列表信息

				menu_status: {
					comment: false,
					more: false,
				}, //控制menu-bar的变量，存进一个变量里边吧
				share: false, // 是否显示分享海报
				showInput: false, //控制输入框，true时显示输入框
				writingComment: false, //控制输入框，true时自动获取焦点，拉起输入法
				//这俩 showInput 和 writingComment 是不是可以合并为一个？ 即，会不会出现显示输入框而允许其他操作的情况？----yao
				placeholderText: '',
				inputData: {}, //localData,用于拼接不同情况下的savecomment请求的数据

				imgIndex: '',
				serverUrl: this.$serverUrl,

				textAreaAdjust: 76,
				ifPop: false, //判断键盘是否弹起

				totalPage: 1, //评论分页属性
				currentPage: 1, //评论分页属性
				type: 0, //查询评论接口参数，0：按时间查询, 1：按热度查询
				control_scroll_button_flag: 0,

				isNavHome: getApp().globalData.isNavHome, //判断导航栏左侧是否显示home按钮
				navbarHeight: 0, //一次性储存 navbarheight
			};
		},
		computed: {
			...mapState(['lang'])
		},
		onReachBottom() {
			this.loadMore();
		},

		onUnload() {
			// 更新本文章信息给上级页面（主页）
			uni.$emit('updateArticle', this.articleCard);
			console.log(this.articleCard)
			console.log('返回');
		},

		onLoad(options) {
			// 一次性储存 navbar 高度
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			this.placeholderText = this.lang.engageComment;
			//获取全局用户信息
			var userInfo = this.getGlobalUserInfo();
			if (!this.isNull(userInfo)) {
				this.userInfo = this.getGlobalUserInfo();
				// console.log("userinfo++"+this.userInfo.id);
			} else {
				uni.redirectTo({
					url: '../../pages/signin/signin'
				});
				return;
			}

			var articleId = options.data || options.scene;
			this.articleId=articleId;
			// console.log("data="+options.data); //跳转进入
			// console.log("sence="+options.scene); //扫码进入
			this.getArticleById(articleId, this.userInfo.id).then(() => {
				this.getComments(this.currentPage);
				// 添加浏览量
				// this.addViewCount();
			})
			
			// uni.$on("flashSubComment", mainCommentId => {
			// 	console.log("修改对应主评下的次评论")
			// 	this.getSubComments(mainCommentId, 1).then(subComment =>{
			// 		var commentList = this.commentList;
			// 		for(var i=0; i<commentList.length;i++){
			// 			var mainComment = commentList[i].mainComment
			// 			if(mainComment.id == mainCommentId){
			// 				mainComment.commentNum++;//主评评论数+1
			// 				var comment = {
			// 					mainComment: mainComment,
			// 					subComment: subComment
			// 				}

			// 				commentList.splice(i, 1, comment);
			// 			}
			// 		}
			// 	})
			// })
		},

		onShareAppMessage(res) {
			if (res.from === 'menu') {
				// 来自右上角菜单的分享
				return {
					title: '速来围观' + this.userInfo.nickname + '的分享',
					path: '/pagesSubA/detail/detail?data=' + this.articleCard.id
				};
			}
		},
		
		onShareTimeline(res){
			if (res.from === 'menu') {
				// 来自右上角菜单的分享
				return {
					title: '速来围观' + this.userInfo.nickname + '的分享',
					path: '/pagesSubA/detail/detail?data=' + this.articleCard.id
				};
			}
		},

		methods: {
			// toggleShare() { //控制是否显示分享海报
			// 	this.share = !this.share;
			// },
			resetInput(e) { //传入组件内的 "isShow"
				console.log('resetInput' + e);
				this.commentContent = "";
				this.placeholderText = this.lang.engageComment;
				this.showInput = e;
				this.writingComment = false;
			},
			
			toggleMenu(mode) {
				// var userInfo = this.getGlobalUserInfo();
				// if(this.isNull(userInfo.email)){
				// 	uni.showToast({
				// 		icon:'error',
				// 		title:'未绑定邮箱'
				// 	})
				// }else{
				// 	if (mode == 'input') {
				// 		this.showInput = !this.showInput;
				// 		this.writingComment = this.showInput;
				// 		this.menu_status.more = false;
				// 	} else if (mode == 'more') {
				// 		this.menu_status.more = !this.menu_status.more;
				// 		this.showInput = false;
				// 	} else if (mode == 'reset') {
				// 		this.showInput = false;
				// 		this.writingComment = false;
				// 		this.menu_status.more = false;
				// 	}
				// }
				if (mode == 'input') {
					this.showInput = !this.showInput;
					this.writingComment = this.showInput;
					this.menu_status.more = false;
				} else if (mode == 'more') {
					this.menu_status.more = !this.menu_status.more;
					this.showInput = false;
				} else if (mode == 'reset') {
					this.showInput = false;
					this.writingComment = false;
					this.menu_status.more = false;
				}
			},
			toggleCollect() {
				if (this.articleCard.isCollect == true) {
					this.unCollectArticle();
				} else {
					this.collectArticle();
				}
			},

			getArticleById(articleId, userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: this.$serverUrl + '/article/getArticleById',
						method: 'POST',
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						data: {
							articleId: articleId,
							userId: userId
						},
						success: res => {
							if (res.data.status == 200) {
								if(res.data.data.status == 0){
									uni.navigateBack({});
									uni.showToast({
										title: '该文章已不存在',
										duration: 2000,
										icon: 'none'
									});
								} else if(res.data.data.status == 1){
									this.articleCard = res.data.data;
									console.log(this.articleCard);
									resolve('suc');
								}
							} else {
								uni.navigateBack({});
								uni.showToast({
									title:'该文章已不存在',
									duration:2000,
									icon:'none'
								})
								// reject('fail');
							}
						}
					});
				})
			},

			addViewCount() {
				uni.request({
					url: this.$serverUrl + '/article/addViewCount',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						userId: this.userInfo.id,
						articleId: this.articleCard.id
					},
					success: res => {}
				});
			},

			popTextArea(e) {
				// this.ifPop = true;
				console.log('展开');
				console.log(e);
				console.log(e.detail.height);
				// this.textAreaAdjust = e.detail.height / 3 + 'px';
				// this.textAreaAdjust = '0' ;
			},

			unpopTextArea(e) {
				// this.ifPop = false;
				console.log('收起');
				console.log(e);

				// this.textAreaAdjust = '';
			},
			changeLikeStatus(status) {
				this.articleCard.isLike = status;
				if (status) {
					this.articleCard.likeNum++;
				} else {
					this.articleCard.likeNum--;
				}
			},

			/**
			 * fromUserId 必填
			 * toUserId 必填
			 * articleId 必填 // 为了计算文章总评论数
			 * underCommentId // 显示在该主评论层ID下
			 * fatherCommentId // 父级评论ID
			 * comment 必填
			 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId, underCommentId;
			 *     子级评论有 fatherCommentId, underCommentId;
			 */
			saveComment: function(content) {
				console.log('conteng =' + content);
				console.log('tragger savecomment');
				// 赋值评论内容
				this.commentContent = content;
				if (uploadFlag) {
					console.log('正在上传...');
					return;
				}
				uploadFlag = true;
				uni.showLoading({
					title: '正在上传...'
				});

				var submitData = {
					fromUserId: this.userInfo.id,
					toUserId: this.articleCard.userId,
					targetType: "ARTICLE",
					targetId: this.articleCard.id,
					comment: content,
				}
				console.log(this.submitData);
				var that = this;
				if (this.commentContent == '') {
					uni.showToast({
						title: '好像忘写评论了哦~',
						duration: 1000,
						icon: 'none'
					});
				} else {
					uni.request({
						url: that.$serverUrl + '/social/saveComment',
						method: 'POST',
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						data: submitData,
						success: res => {
							if (res.data.status == 200) {
								uni.hideLoading();
								uploadFlag = false;

								that.resetInput(false);
								// 强制子组件重新刷新
								
								that.getComments(1);
								that.articleCard.commentNum++; // 文章评论数累加
							} else if (res.data.status == 500) {
								that.contentIllegal();
							}
						}
					});
				}
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

			changeType(e) {
				this.type = e.type;
				this.getComments(1);
			},

			getComments(page) {
				var that = this;
				uni.showLoading({
					title: '加载中...'
				});
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/getMainComments',
					data: {
						page: page,
						type: that.type,
						targetType: "ARTICLE",
						targetId: that.articleCard.id,
						userId: that.userInfo.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: async res => {
						if (res.data.status == 200) {
							if (page == 1) {
								that.commentList = [];
							}
							var commentList = [];
							//获取主评论
							var mainComments = res.data.data.rows;
							console.log(mainComments)
							//获取子评论
							// for(let mainComment of mainComments){
							for (var i = 0; i < mainComments.length; i++) {
								var comment;
								var mainComment = mainComments[i];
								await this.getSubComments(mainComment.id, 1).then(subComment => {
									comment = {
										mainComment: mainComment,
										subComment: subComment
									}
									commentList.push(comment);
								})
							}
							// 拼接
							var newCommentList = commentList;
							var oldCommentList = this.commentList;
							this.commentList = oldCommentList.concat(newCommentList);
							this.currentPage = page;
							this.totalPage = res.data.data.total;
							// console.log(this.commentList);

							this.control_scroll_butoon(); //获取评论数据后，生成卡片后，判断总页面高度，控制是否显示回到顶部按钮
						} else {
							console.log(res);
						}
						uni.hideLoading();
					}
				});
			},

			getSubComments(mainCommentId, page) {
				return new Promise((resolve, reject) => {
					uni.request({
						method: "POST",
						url: this.$serverUrl + '/social/getSubComments',
						data: {
							underCommentId: mainCommentId,
							userId: this.userInfo.id,
							page: page,
							type: 0,
						},
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						success: (res) => {
							// console.log(res);
							if (res.data.status == 200) {
								var subCommentNum = res.data.data.records;
								var subCommentList;
								if (subCommentNum <= 2) {
									subCommentList = res.data.data.rows;
								} else {
									subCommentList = res.data.data.rows.slice(0, 2);
								}
								var subComment = {
									subCommentList: subCommentList,
									subCommentNum: subCommentNum
								}
								resolve(subComment);
							} else {
								reject("Request fail");
							}
						},
						fail: res => {
							reject("Request fail");
						}
					});
				})
			},
			/**
			 * 点赞或取消点赞评论
			 * @param {Object} comment
			 */
			swLikeComment(comment) {
				var mainComment = comment.mainComment;
				if (mainComment.isLike) {
					this.unLikeComment(mainComment);
				} else {
					this.likeComment(mainComment);
				}
				mainComment.isLike = !mainComment.isLike;
			},

			likeComment(comment) {
				console.log('点赞评论');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userLike',
					data: {
						userId: that.userInfo.id,
						targetType: "COMMENT",
						targetId: comment.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						comment.likeNum++;
					}
				});
			},

			unLikeComment(comment) {
				console.log('取消点赞评论');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userUnLike',
					data: {
						userId: that.userInfo.id,
						targetType: "COMMENT",
						targetId: comment.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						comment.likeNum--;
					}
				});
			},

			loadMore: function() {
				var that = this;
				var currentPage = that.currentPage;
				console.log(currentPage);
				var totalPage = that.totalPage;
				console.log(totalPage);
				// 判断当前页数和总页数是否相等
				if (that.commentList.length < 10) {
					return;
				} else if (currentPage == totalPage) {
					// that.showArticles(1);
					uni.showToast({
						title: '没有更多评论了',
						icon: 'none',
						duration: 1000
					});
				} else {
					var page = currentPage + 1;
					that.getComments(page);
				}
			},

			goToPersonPublic() {
				uni.navigateTo({
					url: '../personpublic/personpublic?userId=' + this.articleCard.userId
				});
			},
			goToCommentDetail(mainComment) {
				var data = {
					mainComment: mainComment,
					type: "article",
					articleId:this.articleId,
				}
				uni.navigateTo({
					url: '/pages/comment-detail/comment-detail?data=' + encodeURIComponent(JSON.stringify(data))
				});
			},

			backToLastPage() {
				uni.navigateBack({});
			},
			scrollToTop() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
				});
			},
			control_scroll_butoon() {
				var content_height;
				var page_height;
				let info1 = uni.createSelectorQuery().select('.article-area');
				info1
					.boundingClientRect(function(data) {
						//data - 各种参数
						content_height = data.height;
						// console.log(content_height);
					})
					.exec();
				let info2 = uni.createSelectorQuery().select('.comment-area');
				info2
					.boundingClientRect(function(data) {
						//data - 各种参数
						if (data.height != 0) {
							content_height = content_height + data.height;
							// console.log(content_height);
						}
					})
					.exec();
				uni.getSystemInfo({
					success: function(res) {
						page_height = res.screenHeight;
					}
				});
				if (content_height > page_height * 2) {
					console.log(page_height);
					console.log(content_height);
					this.control_scroll_button_flag = 1;
				}
			}, //获取评论数据后，生成卡片后，判断总页面高度，控制是否显示回到顶部按钮

			swLikeArticle() { //点赞主文章功能三个函数
				// var userInfo = this.getGlobalUserInfo();
				// if(this.isNull(userInfo.email)){
				// 	uni.showToast({
				// 		icon:'error',
				// 		title:'未绑定邮箱'
				// 	})
				// }else{
				// 	if (this.articleCard.isLike) {
				// 		this.unLikeArticle();
				// 	} else {
				// 		this.likeArticle();
				// 	}
				// }
				if (this.articleCard.isLike) {
					this.unLikeArticle();
				} else {
					this.likeArticle();
				}
				// 	this.thisArticle.isLike = !this.thisArticle.isLike;
				//
			},

			likeArticle() {
				console.log('点赞文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userLike',
					data: {
						userId: that.userInfo.id,
						targetType: "ARTICLE",
						targetId: that.articleCard.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						that.articleCard.isLike = !that.articleCard.isLike;
						that.articleCard.likeNum++;
						// this.$emit('swLikeArticleSignal', true);
					}
				});
			},

			unLikeArticle() {
				console.log('取消点赞文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userUnLike',
					data: {
						userId: that.userInfo.id,
						targetType: "ARTICLE",
						targetId: that.articleCard.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						that.articleCard.isLike = !that.articleCard.isLike;
						that.articleCard.likeNum--;
						//this.$emit('swLikeArticleSignal', false);
					}
				});
			}, //点赞主文章函数结束
			reportArticle(){
				console.log('举报文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/Report/reportPublished',
					data: {
						userId: that.userInfo.id,
						targetId: that.articleCard.id,
						targetType: "ARTICLE",
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						//this.$emit('swLikeArticleSignal', false);
						uni.showToast({
							title:'举报成功',
							icon:'success',
							duration:1000,
						});
					}
				});
			},
			collectArticle(){
				console.log('收藏文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userCollect',
					data: {
						userId: that.userInfo.id,
						targetType: "ARTICLE",
						targetId: that.articleCard.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						that.articleCard.isCollect = !that.articleCard.isCollect;
						//this.$emit('swLikeArticleSignal', false);
						uni.showToast({
							title:'收藏成功',
							icon:'success',
							duration:1000,
						});
					}
				});
			},
			unCollectArticle(){
				console.log('取消收藏文章');
				var that = this;
				uni.request({
					method: 'POST',
					url: that.$serverUrl + '/social/userUncollectArticle',
					data: {
						userId: that.userInfo.id,
						targetType: "ARTICLE",
						targetId: that.articleCard.id,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						console.log(res);
						that.articleCard.isCollect = !that.articleCard.isCollect;
						uni.showToast({
							title:'取消收藏成功',
							icon:'success',
							duration:1000,
						});
					}
				});
			},
		} //method
	};
</script>
<style>
	page {
		height: 100%;
		width: 100%;
		background: #fcfcfc;
	}
</style>

<style scoped>
	.detail-page {
		width: calc(100% - 32px);
		margin: auto;
		background: #fcfcfc;
	}

	/* 滑到底了等提示
 */
	.comment-bottom {
		height: 160px;
		width: calc(88px + 40upx);
		margin: auto;
	}

	.comment-bottom-notice {
		width: 71px;
		height: 14px;
		line-height: 14px;
		font-size: 14px;
		color: #b2b2b2;
		margin: 37px auto 27px;
	}

	.comment-bottom-buttons {
		display: flex;
		justify-content: space-between;
	}

	.comment-bottom-buttons .back {
		width: 16px;
		height: 16px;
		padding: 14px;
		background: #fff1d5;
		border-radius: 22px;
	}

	.comment-bottom-buttons .to-top {
		width: 18px;
		height: 12px;
		background: #fff1d5;
		border-radius: 22px;
		padding: 16px 11px 16px 15px;
		position: relative;
		transform: rotate(90deg);
	}

	.comment-bottom-buttons .to-top::after {
		content: '';
		position: absolute;
		top: 14px;
		left: 12px;
		width: 2px;
		height: 16px;
		background: #fcc041;
		border-radius: 2px;
	}

	.active-input-button {
		color: #ffffff;
		width: 76px;
		height: 17px;
		font-size: 17px;
		font-family: Source Han Sans CN;
		font-weight: 400;
		line-height: 17px;
		color: rgba(255, 255, 255, 1);
		padding: 10px 22px;
		border-radius: 10px;
		box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.16);
		background: #fcc041;
		letter-spacing: 2px;
	}

	/* 以下五条为底部输入框样式 */
	.bottoLayerOfInput {
		position: fixed;
		width: 750upx;
		height: 100%;
		bottom: 0;
		left: 0;
		z-index: 40;
	}

	.commentPart {
		box-shadow: 0px 0px 4px rgba(121, 121, 121, 0.42);
		position: absolute;
		bottom: 76px;
		left: 16px;
		z-index: 999;
		width: 640upx;
		padding: 10px 24upx 4px;
		min-height: 50px;
		background: white;
		border-radius: 8px;
	}

	.emoji {
		background-repeat: no-repeat;
		background-position: center;
		border: none;
		width: 21px;
		height: 21px;
		background-size: 21px 21px;
		margin-bottom: 7px;
		display: inline-block;
	}

	.add-pic {
		background: url(../../static/icon/image-888888.png);
		background-repeat: no-repeat;
		background-position: center;
		border: none;
		width: 21px;
		height: 21px;
		background-size: 20px 16px;
		margin-bottom: 7px;
		margin-left: 11px;
		display: inline-block;
	}

	.submit {
		display: inline-block;
		width: 42px;
		position: absolute;
		bottom: 11px;
		height: 21px;
		right: 15px;

		background-size: 14px 14px;
		background-repeat: no-repeat;
		background-position: center;
		float: right;
		font-size: 14px;
		color: #fcc041;
		z-index: 50;
	}

	.commentSth {
		height: 110px;
		border: solid 2px #fcc041;
		border-radius: 8px;
		line-height: 20px;
		padding: 12px 12px 0px;
		position: relative;
		margin: 0 auto 37px;
	}

	.comment-text {
		width: calc(670upx - 60px);
		font-size: 14px;
		max-height: 95px;
		line-height: 20px;
		max-height: 100px;
		padding-bottom: 14px;
	}

	.comment-pic-area {
		position: relative;
		height: 45px;
	}

	.comment-pic-area image {
		width: 35px;
		height: 35px;
		margin-right: 18px;
	}

	.word-count-left {
		position: absolute;
		width: 15px;
		height: 11px;
		font-size: 11px;
		font-weight: 400;
		color: #9b9b9b;
		right: 11px;
		bottom: 8px;
		line-height: 11px;
	}

	/* 
.permanent_input_BG {
	position: fixed;
	bottom: 0px;
	left: 0;
	width: 100%;
	min-height: 58px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.16);
	background: #ffffff;
	line-height: 14px;
	z-index: 30;
}
.permanent_input {
	height: 30px;
	display: flex;
	vertical-align: top;
	color: #888888;
	overflow: hidden;
	width: calc(100% - 48px);
	padding: 3px 12px 4px;
	margin: 12px auto 24px;
	border-radius: 8px;
	border: 2px solid rgba(252, 192, 65, 1);
	font-size: 12px;
	line-height: 30px;
} */
	.menu-bar {
		position: fixed;
		width: calc(750upx - 32px);
		bottom: 24px;
		left: 16px;
		height: 40px;
		background: rgba(252, 252, 252, 1);
		box-shadow: 0px 0px 4px rgba(121, 121, 121, 0.42);
		border-radius: 8px;
		z-index: 50;
	}

	.menu-bar image {
		width: 16px;
		height: 16px;
		vertical-align: bottom;
	}

	.like,
	.comment,
	.menu-more {
		width: 50px;
		text-align: right;
		padding: 12px calc((100% - 150px)/6);
		heigh: 16px;
		line-height: 16px;
		color: rgba(136, 136, 136, 1);
		display: inline-flex;
		align-items: center;
		justify-content: space-around;
		font-size: 14px;
		vertical-align: bottom;
		position: relative;
	}

	.like::after,
	.comment::after {
		content: "";
		width: 1px;
		height: 15px;
		position: absolute;
		right: -1px;
		bottom: 11px;
		background-color: #ECECEC;
	}

	.liked {
		background: #FF7070;
		color: #FFFFFF;
	}

	.commented {
		background: #FCC041;
		color: #FFFFFF;
	}

	.clicked_more {
		background: rgba(59, 161, 239, 1);
		color: #FFFFFF;
	}

	.menu_more_items {
		position: absolute;
		right: 0;
		bottom: 52px;
		background: rgba(252, 252, 252, 1);
		box-shadow: 0px 0px 4px rgba(121, 121, 121, 0.42);
		border-radius: 8px;
	}

	.menu_more_item {
		width: 74px;
		height: 14px;
		font-size: 14px;
		line-height: 14px;
		color: rgba(136, 136, 136, 1);
		padding: 12px 0;
		padding-left: 40px;
		position: relative;
	}

	.menu_more_item image {
		width: 14px;
		height: 14px;
		position: absolute;
		left: 16px;
		bottom: 11px;
	}
</style>
