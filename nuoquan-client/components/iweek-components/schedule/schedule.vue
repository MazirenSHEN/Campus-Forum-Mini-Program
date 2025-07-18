<template>
	<view>
		<view class="calendar">
			<view class="hint" style="display: flex;margin-bottom: 15upx;padding-top: 5px;">
				<view class="text_topic">
					{{lang.dailySchedule}}
				</view>
				<view class="pickerbox" style="display: flex;padding-top: 20upx; right: 10px; position: absolute; vertical-align: middle;">
					<!--<picker class="picker faculty-picker" mode="selector" :range="facultyList" range-key="name" @change="facultyChange" style="height: 20px;display: table-cell;margin-right: 10px;">
						<view>{{beforeFaculty?beforeFaculty:'Faculty'}}</view>
					</picker>
					<picker class="picker degree-picker" mode="selector" :range="degreeList" range-key="name" @change="degreeChange" style="height: 20px;display: table-cell;">
						<view>{{beforeDegree?beforeDegree:'Degree'}}</view>
					</picker>-->
				</view>
			</view>
			<swiper-date @date="getEmitDate"></swiper-date>
			<schedule-card @event="showDetail" v-for="item in showList" :tag="item.tags" :key = "item.id" v-bind:scheduleCard="item"></schedule-card>
			<!--<view class="notify" v-if="facultyId == 0 || degreeId == 0">
				<text>请选择您的学院及学历</text><br>
				<text>Please choose your Faculty and Degree</text>
			</view>
			<view class="notify" v-if="isNull(showList) && facultyId!==0 && degreeId!==0">
				<text>今天没有日程哦</text><br>
				<text>There's no schedule today</text>
			</view>-->
			<view class="bottom-placeholder"></view>
		</view>
	</view>
</template>

<script>
	import swiperDate from '../swiper-date/swiper-date.vue';
	import scheduleCard from '../schedule-card/schedule-card.vue';
	import { mapState, mapMutations } from 'vuex';
	
	var loadScheduleFlag = false;	// 为加载日程加锁
	
	export default {
		components:{
		scheduleCard,
		swiperDate,
		},
		data() {
			return {
				//Tag:2,
				userInfo:'',
				date:13,
				event: {},
				showList: [],
				page: 1,
				beforeFaculty:'',
				facultyId:0,
				beforeDegree:'',
				degreeId:0,
				facultyList:[
					{
						id: 1,
						name: 'FoSE',
					},
					{
						id: 2,
						name: 'NUBS',
					},
					{
						id: 3,
						name: 'FHSS',
					}
				],
				degreeList:[
					{
						id: 1,
						name: 'Undergraduate',
					},
					{
						id:3,
						name: 'PGT',
					},
					{
						id:4,
						name: 'PGR'
					}
				]
			}
		},
		computed:{
			...mapState(['lang'])
		},
		created() {
			this.userInfo = this.getGlobalUserInfo();
			var that = this;
			uni.getStorage({
				key:that.userInfo.id + ':facultyId',
				success:function(res){
					// console.log(that.resultFaculty);
					// debugger
					that.facultyId = res.data;
					that.beforeFaculty =that.facultyList[that.facultyId-1].name;
					// console.log(that.facultyId);
					// that.facultyId = res.data;
				},
				fail() {
					console.log('无记录');
				}
			});
			uni.getStorage({
				key:that.userInfo.id + ':degreeId',
				success:function(res){
					that.degreeId = res.data;
					that.beforeDegree = that.degreeList[that.degreeId-1].name;
					// console.log(that.degreeId);
				},
				fail(){
					console.log('degree无记录');
				}
			})
		},
		onLoad() {
			var userInfo = this.getGlobalUserInfo();
			if (this.isNull(userInfo)) {
				uni.redirectTo({
					url: '../../../pages/signin/signin'
				});
				return;
			} else {
				this.userInfo = userInfo; // 刷去默认值(若有)
			}
			this.checkSchedule(this.page);
		},
		methods: {
			showDetail(e){
				this.$emit("event",e);
				console.log(e);
			},
			
			checkSchedule: function(page){
				if (loadScheduleFlag) {
					return;
				}
				loadScheduleFlag = true;
				
				uni.showLoading({
					title: '加载中...'
				});
				
				setTimeout(() => {
					if (loadScheduleFlag) {
						loadScheduleFlag = false; // 解锁
						uni.hideLoading();
						uni.showToast({
							title: '网络错误...',
							icon: 'none',
							duration: 1000
						});
					}
				}, 5000); // 延时5s timeout
				
				var userInfo = this.getGlobalUserInfo();
				if (this.isNull(userInfo)) {
					uni.redirectTo({
						url: '../../../pages/signin/signin'
					});
					return;
				} else {
					this.userInfo = userInfo; // 刷去默认值(若有)
				}
				// console.log(this.userInfo);
				var that = this;
				uni.request({
					url: that.$serverUrl + '/eventsCalendar/queryEventsCalendarByDate',
					method: 'POST',
					data: {
						userId: that.userInfo.id,
						// page 和 pagesize写成静态的 一次性加载出来
						page: page,
						pageSize: 20,
						
						targetDate:that.date,
						faculty:2,
						degree:1,
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: res=> {
						if (res.data.status == 200){
							uni.hideLoading();
							loadScheduleFlag = false;
						
							console.log(res);
						
							// 当前必然是第一页，设置showList为空
							that.showList = [];
						
							this.$nextTick(() =>{
								that.showList = res.data.data.rows;
							})
						}
						else {
							console.log(res);
						}
					},
					fail: res => {
						uni.hideLoading();
						loadScheduleFlag = false;
						
						console.log('index unirequest fail');
						console.log(res);
					}
				});
			},
			
			facultyChange(e){
				const index = e.target.value;
				this.beforeFaculty = this.facultyList[index].name;
				this.facultyId = this.facultyList[index].id;
				// console.log(this.userInfo.id);
				this.updateFaculty(this.userInfo.id,this.facultyId);
				this.checkSchedule(1);
			},
			degreeChange(e){
				const index2 = e.target.value;
				this.beforeDegree = this.degreeList[index2].name;
				this.degreeId = this.degreeList[index2].id;
				this.updateDegree(this.userInfo.id,this.degreeId)
				this.checkSchedule(1);
			},
			getEmitDate(data){
				this.date = data
				this.checkSchedule(1)
			},
			getEvent(data){
				this.event = data
				console.log(data)
				this.$emit("event",this.event)
			},
			updateFaculty(id,facultyId) {
				uni.setStorage({
					key: id + ':facultyId',
					data: facultyId,
					success:function(){
						console.log('更新faculty成功');
					},
					fail:function(){
						console.log('faculty更新失败');
					}
				})
			},
			updateDegree(id,degreeId) {
				uni.setStorage({
					key: id + ':degreeId',
					data: degreeId,
					success:function(){
						console.log('更新degree成功');
					},
					fail:function(){
						console.log('degree更新失败');
					}
				})
			}
		}
	}
</script>

<style>
	.picker{
		border-radius: 10rpx;
		background-color: #f2f2fc;
		/* height: 20px; */
		padding: 0 30rpx;
		font-size: 12px;
		
		/**文字隐藏后添加省略号*/
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		
	}
	/* .faculty-picker {
		position: absolute;
		margin-left: 130px;
		margin-top: -25px;
		width: 60px;
		text-align: center;
		right: 200px;
	}
	.degree-picker {
		position: absolute;
		margin-left: 240px;
		margin-top: -25px;
		width: 80px;
		text-align: center;
		right: 10px;
	} */
	.bottom-placeholder {
		height: 30px;
	}
	.notify{
		text-align: center;
		font-size: 12px;
		color: rgba(136, 136, 136, 1);
		letter-spacing: 1.5px;
		margin-top: 30px;
		/* bottom: 3px; */
	}
</style>
