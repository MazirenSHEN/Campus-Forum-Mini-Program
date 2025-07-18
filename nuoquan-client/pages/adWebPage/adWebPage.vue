/*
	#接入式网页展示
	#url 由外部传入
	#用于展示各种广告推文网站
	Author: Guetta
	Time: 24 May 2020
*/
<template>
	<web-view :src="webUrl" :style="display"></web-view>
</template>

<script>
	export default {
		data() {
			return {
				webUrl: '',
				display:'',
			}
		},
		onLoad(option){
			this.webUrl = decodeURIComponent(option.url);
			console.log(this.webUrl);
			this.display = {
				height: this.getnavbarHeight().bottom + 5 + 'px'
			}
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
