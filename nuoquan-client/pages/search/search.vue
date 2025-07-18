<template>
	<view class="weui-search-bar">
		<view class="input-bar" :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}">
			<span class="back" @tap="exitSearch">{{lang.back}}</span>
			<input type="text" v-model="searchKeyWords" focus confirm-type="search" @confirm="search(1)" />
		</view>

		<!-- 历史搜索标题行及区域 -->
		<view class="SearchHistoryItem" v-show="searching">
			<view class="SearchHistoryItemTitle">{{lang.searchHistory}}</view>
			<icon type="clear" @tap="searchDeleteAll" size="11"></icon>
			<!-- <view v-for="key in searchHisKeyList" :key="key">{{key}}</view> -->
			<view class="searchList">
				<view class="item" v-for="(item,index) in searchHisKeyList" :key="index" @click="putHisIntoInput(index)">{{item}}</view>
			</view>
		</view>

		<!-- 		大家都在搜-->
		<view class="wxSearchKey" v-show="searching">
			<view class="exSearchTitle">{{lang.topSearches}}</view>
			<view class="searchList">
				<view class="item" v-for="(i,index) in hotList" :key="index" @click="putHotIntoInput(index)">
					{{i}}
				</view>
			</view>
		</view>



		<!-- 搜索结果标题行 -->
		<!--
		<view class="searchResultWrods column_center">
			<view style="color: #888888;font-size: 13px;font-weight: 300;">搜索结果:</view>
			<icon type="clear" style="position: absolute;right:28px;" v-show="!searching" @click="searchCancle(searching)" size="11"></icon>
		</view> 
		-->

		<!-- 搜索结果显示区域 -->
		<scroll-view class="searchResult" v-show="!searching" scroll-y="true" @scrolltolower="loadMore()">
			<searchResultArticle v-for="i in searchedArticleList" :key="i.id" v-bind:articleCard="i"></searchResultArticle>
			<view style="height:140px;width: 100%;"></view>
		</scroll-view>
	</view>
</template>

<script>
	import searchResultArticle from '../../components/nq-card/searchResultArticle.vue';
	import { mapState, mapMutations } from 'vuex';
	
	var isSearching = false; //搜索锁
	export default {
		data() {
			return {
				hotList: [],
				searchKeyWords: '',
				searchedArticleList: [],
				searching: true,
				searchHisKeyList: uni.getStorageSync('search_history'),

				userInfo: this.getGlobalUserInfo(),

				totalPage: 1,
				currentPage: 1,
			}
		},
		computed: {
			...mapState(['lang'])
		},
		components: {
			searchResultArticle,
		},
		created() {
			// 查询热搜词
			this.getHotWords();
		},
		destroyed() { // 组件退出隐藏加载
			uni.hideLoading();
		},
		methods: {
			...mapMutations(['changeLang']), //引用vuex中的方法
			
			getHotWords: function() {
				var that = this;
				uni.request({
					url: that.$serverUrl + '/article/hot',
					method: "POST",
					success: (res) => {
						console.log(res);
						that.hotList = res.data.data;
						console.log(that.hotList);
					}

				})
			},
			search: function(page) {
				if (isSearching) {
					return;
				}else if (this.searchKeyWords == '' || this.searchKeyWords == null) {
					uni.showToast({
						title: '搜索内容不能为空',
						duration: 1000,
						icon: 'none',
					})
					return;
				}
				
				isSearching = true;

				var that = this;
				var isSaveRecord = 1;
				// console.log(page);
				

				uni.getStorage({
					key: 'search_history',
					success(res) {
						let list = res.data;
						// console.log(list);
						if (list.length > 10) {
							for (let item of list) {
								if (item == that.searchKeyWords) {
									return false;
								}
							}
							list.pop();
							list.unshift(that.searchKeyWords);
						} else {
							for (let item of list) {
								if (item == that.searchKeyWords) {
									return false;
								}
							}
							list.unshift(that.searchKeyWords);
						}
						that.searchHisKeyList = list;
						uni.setStorage({
							key: 'search_history',
							data: that.searchHisKeyList
						})
					},
					fail() {
						that.searchHisKeyList = [];
						that.searchHisKeyList.push(that.searchKeyWords);
						uni.setStorage({
							key: 'search_history',
							data: that.searchHisKeyList
						});
					}
				});

				uni.showLoading({
					title: "搜索中..."
				})
				setTimeout(() => {
					if (isSearching) {
						isSearching = false // 解锁
						uni.hideLoading();
						uni.showToast({
							title: "网络未知错误",
							icon: "none",
							duration: 1000
						})
					}
				}, 5000); // 延时5s timeout

				uni.request({
					url: that.$serverUrl + '/article/searchArticleYANG',
					method: "POST",
					data: {
						searchText: that.searchKeyWords,
						userId: that.userInfo.id,
						isSaveRecord: isSaveRecord,
						page: page,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: function(result) {
						isSearching = false // 解锁
						uni.hideLoading();

						that.searching = false;
						if (result.data.status == 200) {
							// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
							if (that.currentPage == 1) {
								that.searchedArticleList = [];
							}

							var newArticleList = result.data.data.rows;
							console.log(newArticleList);

							newArticleList = that.highLightKeyWord(newArticleList);
							// 需要一个方程处理高亮标签内容

							var oldArticleList = that.searchedArticleList;
							that.searchedArticleList = oldArticleList.concat(newArticleList);
							// console.log(result.data.data.page);
							that.currentPage = page;
							that.totalPage = result.data.data.total;
						}

					},
					fail: (res) => {
						console.log("index unirequest fail");
						console.log(res);
					}
				})
			},

			loadMore: function() {
				console.log('currentpage is ' + this.currentPage);
				console.log('totalpage is ' + this.totalPage);
				// 判断当前页数和总页数是否相等
				if (this.currentPage == this.totalPage) {
					uni.showToast({
						title: "没有更多文章了",
						icon: "none",
						duration: 1000
					})
				} else {
					this.currentPage += 1;
					this.search(this.currentPage);
				}
			},

			searchDeleteAll: function() {
				var that = this;
				uni.showModal({
					title: "提示",
					content: '确定删除所有历史记录吗?',
					success: function(res) {
						if (res.confirm) {
							that.searchHisKeyList = [];
							uni.setStorage({
								key: 'search_history',
								data: that.searchHisKeyList,
							})
						} else if (res.cancle) {

						}
					}
				})
			},
			searchCancle: function(searching) {
				this.searching = !searching;
				// console.log(this.searching);
			},
			exitSearch() {
				if (this.searching == false) {
					this.searchKeyWords = "",
						this.searchedArticleList = "",
						this.searching = !this.searching;
				} else {
					this.hotList = "";
					this.searchKeyWords = "";
					this.searchedArticleList = "";
					this.$emit("exitSearchSignal", 0);
				}
			},
			putHotIntoInput: function(index) {
				// console.log(index);
				var keywords = this.hotList[index];
				// console.log(keywords);
				this.searchKeyWords = keywords;
			},
			putHisIntoInput: function(index) {
				// console.log(index);
				var keywords = this.searchHisKeyList[index];
				// console.log(keywords);
				this.searchKeyWords = keywords;
				this.search(1);
			},
			highLightKeyWord(newArticleList) {
				// 	console.log("highLighting!!!!!");
				var highLighPart = '<span style="color:#FF5D5D">' + this.searchKeyWords + '</span>';
				console.log(highLighPart);
				var i;
				for (i = 0; i < newArticleList.length; i++) {
					newArticleList[i].articleTitle = newArticleList[i].articleTitle.replace(new RegExp(this.searchKeyWords, 'g'),
						highLighPart);
					newArticleList[i].articleContent = newArticleList[i].articleContent.replace(new RegExp(this.searchKeyWords, 'g'),
						highLighPart);
					// console.log(newArticleList[i].articleTitle);
					// console.log(newArticleList[i].articleContent);
				}
				return newArticleList;
			},
		}
	}
</script>

<style scoped>
	.weui-search-bar {
		display: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: #FFFFFE;
		z-index: 10;
	}

	.input-bar {
		width: 100%;
		position: relative;
		background: #FFC85A;
	}

	.back {
		width: 32px;
		height: 23px;
		font-size: 16px;
		position: absolute;
		left: 24px;
		bottom: 8px;
		color: #FFFFFF;

	}

	.back::after {
		content: "";
		background: url(../../static/icon/angle-left.png);
		background-size: 35px 35px;
		background-repeat: no-repeat;
		position: absolute;
		left: -28px;
		top: -8px;
		display: inline-block;
		width: 35px;
		height: 35px;
	}



	.input-bar input {
		position: absolute;
		bottom: 5px;
		left: 63px;
		font-size: 15px;
		display: inline-block;
		width: calc(56% - 36px);
		height: 30px;
		letter-spacing: 1px;
		color: #353535;
		background: rgba(255, 247, 231, 1);
		border-radius: 75px;
		padding-left: 36px;
		
		opacity: 1;
		display: inline-block;
		vertical-align: middle;
	}

	.input-bar input::after {
		content: "";
		background: url(../../static/icon/search-B79144.png);
		background-size: 20px 20px;
		background-repeat: no-repeat;
		width: 20px;
		height: 20px;
		position: absolute;
		left: 13px;
		top: 5px;

	}

	.wxSearchKey,
	.SearchHistoryItem {
		margin-top: 4px;
		width: calc(750upx-56px);
		padding: 0 28px;
		position: relative;
	}

	.exSearchTitle,
	.SearchHistoryItemTitle {
		height:16px;
		font-size:14px;
		font-family:Source Han Sans CN;
		font-weight:400;
		line-height:16px;
		color:rgba(136,136,136,1);
		opacity:1;
		margin-top: 12px;
		margin-bottom: 12px;
	}

	.searchResult {
		width: 100%;
		height: 100%;
	}

	/* 	
	.searchResultWrods {
		margin-top: 26px;
		width: calc(750upx-56px);
		padding: 0 28px;
		position: relative;
	}
 */
	.item {
		display: inline-block;
		padding: 6px 10px;
		background: #FFDD98;
		border-radius: 20px;
		height: 11px;
		line-height: 11px;
		color: #353535;
		font-size: 11px;
		font-weight: 300;
		margin-right: 8px;
		margin-bottom: 8px;
		/* 限制字数 */
		max-width: 48%;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.SearchHistoryItem icon {
		position: absolute;
		right: 28px;
		top: 0;
	}
</style>
