
<!-- 
	Date: July 23, 2022
	Author: Zhuoli
	Description: 组织详情
 -->
 <template>
 	<view id="organizationDetail-container">
 		<uni-nav-bar v-if="detail.status == 1" class="navigationBar" 
 		:style="{ height: this.getnavbarHeight() + 'px'}"  
 		backgroundColor="#f9c406"
 		color="#fcfcfc"
 		:showLeftIcon="true" 
 		:isNavHome="isNavHome"
 		:left-text="lang.back" 
 		:title="lang.organizationDetail" 
 		:height="navbarHeight"></uni-nav-bar>
		
		<uni-nav-bar v-if="detail.status == 2" class="navigationBar"
		:style="{ height: this.getnavbarHeight() + 'px'}"  
		backgroundColor="#f9c406"
		color="#fcfcfc"
		:showLeftIcon="true" 
		:isNavHome="isNavHome"
		:left-text="lang.back" 
		:title="lang.clubDetail" 
		:height="navbarHeight"></uni-nav-bar>
		
 		<view :style="{ height: navbarHeight + 'px' }"></view>
 		<!-- safearea -->
 		<view style="height: 20px;"></view>
 		<view style="display: flex;">
 			<image :src="pathFilter(detail.logoPath)" mode="aspectFill" class="orgLogo"></image>
 			<view class="orgTitle">{{detail.name}}</view>
 		</view>
 		
 		<view class="orgContentBox">
 			<!-- <view v-for="(item,index) in showList" class="orgIntro">
 				<view style="font-size: 17px;font-weight: bold;">{{item}}</view>
 				<view class="orgDetail">
 					<view v-if="index==0">{{detail.intro}}</view>
 					<view v-else-if="index==1">{{detail.activityIntro}}</view>
 					<view v-else>{{detail.division}}</view>
 				</view>
 			</view> -->
 			<view  class="orgIntro">
 				<view style="font-size: 17px;font-weight: bold;">{{showList[0]}}</view>
 				<!-- <view class="orgDetail">{{detail.intro}}</view> -->
				
				<!-- 介绍 -->
 				<view class="orgDetail" v-html="$markdownParse.parse(detail.intro)"></view>
				<view v-if="detail.imgList">
					<view v-if="detail.imgList[3]" class="orgImgBox">
						<image :src="pathFilter(detail.imgList[3].imagePath)" mode="heightFix" class="orgImg" @tap="previewImg(3)" @longpress="aboutImg(3)"></image>
					</view>
				</view>
				
				<!-- 主要活动 -->
 				<view v-if="detail.activityIntro" style="margin-top: 20px;">
 					<view style="font-size: 17px;font-weight: bold;">{{showList[1]}}</view>
 					<view class="orgDetail" v-html="$markdownParse.parse(detail.activityIntro)"></view>
 				</view>
				<view v-if="detail.imgList">
					<view v-if="detail.imgList[4]" class="orgImgBox">
						<image :src="pathFilter(detail.imgList[4].imagePath)" mode="heightFix" class="orgImg" @tap="previewImg(4)" @longpress="aboutImg(4)"></image>
					</view>
				</view>
				<view v-if="detail.imgList">
					<view v-if="detail.imgList[5]" class="orgImgBox">
						<image :src="pathFilter(detail.imgList[5].imagePath)" mode="heightFix" class="orgImg" @tap="previewImg(5)" @longpress="aboutImg(5)"></image>
					</view>
				</view>
				
				<!-- 部门组成 -->
 				<view v-if="detail.division" style="margin-top: 20px;">
 					<view style="font-size: 17px;font-weight: bold;">{{showList[2]}}</view>
 					<view class="orgDetail" v-html="$markdownParse.parse(detail.division)"></view>
 				</view>
				
 			</view>
 			<!-- 招新推文以及介绍的二维码 -->
			<view  v-if="detail.imgList">
				<view class="orgImgBox">
					<view v-if="detail.imgList[0]">
						<image :src="pathFilter(detail.imgList[0].imagePath)" mode="aspectFit" class="orgQR" @tap="previewImg(0)" @longpress="aboutImg(0)"></image>
					</view>
					<view v-if="detail.imgList[1]">
						<image :src="pathFilter(detail.imgList[1].imagePath)" mode="aspectFit" class="orgQR" @tap="previewImg(1)" @longpress="aboutImg(1)"></image>
					</view>
				</view>
				<view class="orgImgBox">
					<view v-if="detail.imgList[2]">
						<image :src="pathFilter(detail.imgList[2].imagePath)" mode="aspectFit" class="orgQR" @tap="previewImg(2)" @longpress="aboutImg(2)"></image>
					</view>
				</view>
			</view>
 		</view>
 	</view>
 </template>
 
 <script>
 	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
 	import {
 		mapState,
 		mapMutations
 	} from 'vuex';
 	export default {
 		components: {
 			uniNavBar,
 		},
 		computed: {
 			...mapState(['lang'])
 		},
 		data() {
 			return {
 				navbarHeight: 0 ,//一次性储存 navbarheight
 				detail: '',
 				
 
 				showList:['简介','主要活动','部门组成']
 			}
 		},
 		onLoad:function (option) {
 			this.navbarHeight = this.getnavbarHeight().bottom + 5;
 			
 			const temp = JSON.parse(decodeURIComponent(option.detail));
 			this.detail = temp;
			console.log(this.detail);
 		},
		
		onShareAppMessage(res) {
			if (res.from === 'menu'){
				return{
					title: '来看看社团组织吧！',
					path: '/pages/Iweek/organizationDetail/organizationDetail?detail='+encodeURIComponent(JSON.stringify(this.detail))
				}
			}
		},
		
		onShareTimeline(res){
			if (res.from === 'menu'){
				return{
					title: '来看看社团组织吧！',
					path: '/pages/Iweek/organizationDetail/organizationDetail?detail='+encodeURIComponent(JSON.stringify(this.detail))
				}
			}
		},
 		methods: {
 			previewImg: function(index) {
				console.log(index);
				var imgList = this.detail.imgList;
				var arr = [];
				var path;
				var minus=0; //用来计算与实际长度差
				var key=[];	//与minus一起记录哪个位置缺损
				var k=0;	//index与实际位置之差
				for (var i = 0; i < imgList.length; i++) {
					if(imgList[i]){
						path = this.pathFilter(imgList[i].imagePath);
						arr = arr.concat(path);
					}else{
						minus=minus+1;
						key[minus]=i;
					}
				}
				//当index的值大于空的位置则k要加1，用来最后剪掉
				for(var j=0;j<imgList.length;j++){
					if(index>key[j]){
						k=k+1;
					}
				}
				// path = this.pathFilter(imgList[index].imagePath);
				// arr = arr.concat(path);
				console.log(arr);
				uni.previewImage({
					current:index-k,
					urls: arr
				});
		},
		aboutImg: function(index) {
			var that = this;
			// console.log(this.detail.imgList[index].imagePath);
			uni.showActionSheet({
				itemList: ['保存图片到本地'],
				success: function(res) {
					console.log(res.tapIndex);
					// 保存图片至本地
					if (res.tapIndex == 0) {
						uni.showLoading({
							title: '下载中...'
						});
						uni.downloadFile({
							url: that.pathFilter(that.detail.imgList[index].imagePath),
							success: function(res) {
								if (res.statusCode == 200) {
									uni.saveImageToPhotosAlbum({
										filePath: res.tempFilePath,
										success: function() {
											console.log('save success');
											uni.hideLoading();
										},
										fail: function() {
											console.log('save failed');
											uni.hideLoading();
											uni.showToast({
												title: '保存失败',
												icon: 'none',
												duration: 1000
											});
										}
									});
								}
							}
						});
					}
				}
			});
		},
 		}
 	}
 </script>
 
 <style>
 	.orgLogo{
 		width: 80px;
 		height: 80px;
 		border: 0.5px solid gray;
 		border-radius: 50%;
 		margin-left: 20px;
 	}
 	.orgTitle{
 		margin-top: 35px;
 		margin-left: 20px;
 		font-size: 18px;
 		font-weight:bold;
 		line-height: 20px;
 	}
 	
 	.orgContentBox{
 		width: 90%;
 		margin:15px 5% 5px 5%;
 		border-radius: 12px;
 		border: 1.5px solid gray;
 	}
 	.orgIntro{
 		margin: 10px 10px 20px 10px;
 	}
 	.orgDetail{
 		margin-top: 5px;
 		font-size: 14px;
 		line-height: 17px;
 		word-wrap: break-word;
 	}
 	
 	.orgImgBox{
 		display:flex;
 		align-items:center;
 		justify-content: space-evenly;
		margin: 10px 5% 10px 5%;
 	}
 	
 	.orgQR{
 		width: 130px;
 		height: 130px;
 		border: 1px solid beige ;
 	}
 	
 	.orgImg{
		height: 170px;
 		border: 1px solid beige ;
 	}
 </style> 
 