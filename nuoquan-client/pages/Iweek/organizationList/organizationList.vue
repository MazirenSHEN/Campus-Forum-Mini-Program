<!-- 
	Date: July 23, 2022
	Author: Zhuoli
	Description: 校内组织
 -->
<template>
	<view id="organizationList-container">
		<uni-nav-bar v-if="type == 1" class="navigationBar" 
		:style="{ height: this.getnavbarHeight() + 'px'}"  
		backgroundColor="#f9c406"
		color="#fcfcfc"
		:showLeftIcon="true" 
		:isNavHome="isNavHome"
		:left-text="lang.back" 
		:title="lang.organizationList" 
		:height="navbarHeight"></uni-nav-bar>
		
		<uni-nav-bar v-else-if="type == 2" class="navigationBar"
		:style="{ height: this.getnavbarHeight() + 'px'}"  
		backgroundColor="#f9c406"
		color="#fcfcfc"
		:showLeftIcon="true" 
		:isNavHome="isNavHome"
		:left-text="lang.back" 
		:title="lang.clubList" 
		:height="navbarHeight"></uni-nav-bar>
		<view :style="{ height: navbarHeight + 'px' }"></view>
		<!-- safearea -->
		<view style="height: 30px;"></view>
		<organizationCard v-for="(item,index) in showList" :detail="showList[index]"></organizationCard>
	</view>
</template>

<script>
	import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
	import organizationCard from '@/components/iweek-components/organizationCard/organizationCard/organizationCard.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex';
	export default {
		components: {
			uniNavBar,
			organizationCard
		},
		computed: {
			...mapState(['lang'])
		},
		data() {
			return {
				navbarHeight: 0 ,//一次性储存 navbarheight
				
				page:1,
				pagesize:0,
				currentPage: 1,
				thisUserInfo: '',
				organizationList:[],
				showList: [],
				
				orgList:[],
				clubList:[],
				
				type:'',
			}
		},
		onLoad(e) {
			this.type = e.type;
			this.navbarHeight = this.getnavbarHeight().bottom + 5;
			this.thisUserInfo = this.getGlobalUserInfo();
			this.getOrg(this.page,e);
		},
		methods: {
			getOrg: function(page,e){
				console.log(e.type);
				uni.showLoading({
						title: '加载中...'
				});
				var that = this;
				uni.request({
					url:that.$serverUrl +'/organization/queryOrganizations',
					method: 'POST',
					data: {
						page: page,
						userId:that.thisUserInfo.id,
						userId:that.userId,
						pageSize:that.pagesize,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res => {
						uni.hideLoading();
						console.log(res);
						// 判断当前页是不是第一页，如果是第一页，那么设置showList为空
						if (page == 1) {
							that.organizationList=[];
						}
						this.$nextTick(() => {
							var newOrgList = res.data.data.rows;
							var oldOrgList = that.organizationList;
							that.organizationList = oldOrgList.concat(newOrgList);
							
							console.log(that.organizationList);
							that.sortList(that.organizationList);
							if (e.type == 1){
								that.showList = that.orgList;
							}else if(e.type == 2){
								that.showList = that.clubList;
								// console.log(that.showList);
							}
							that.currentPage = page;
						})
					},
					fail: res => {
						uni.hideLoading();
							
						console.log('index unirequest fail');
						console.log(res);
					}
				});
			},
			sortList:function(organizationList){
				var item;
				for(item in this.organizationList){
					// this.organizationList[item]
					// 1是组织，2是社团
					if (this.organizationList[item].status == 1){
						this.orgList.push(this.organizationList[item]);
					}
					else if(this.organizationList[item].status == 2){
						this.clubList.push(this.organizationList[item]);
					}
				}
				// console.log(this.orgList);
				// console.log(this.clubList);
			}
		}
	}
</script>

<style>

</style>
