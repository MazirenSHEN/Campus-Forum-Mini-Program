<template>
	<view style="position:absolute;right:0;top:0;">
<!-- 		<textarea class="content" v-model="articleContent" :maxlength="maxContentLength" :auto-height="true"
		 :show-confirm-bar="false" @input="dealmark()"></textarea> -->
		 
		 <!-- 此处预览为上线暂时注释掉，功能完成后重新更新 Guetta -->
		 <!-- <button class="preViewButton" @tap="showPreview">预览</button> -->

		<view v-if="showPre" class="preViewArea" v-html="nodes"></view>
	</view>
</template>

<script>
	export default {
		props:{
		articleContent:"",	
		},
		data() {
			return {
				nodes: "",
				showPre:false,
			}
		},
		watch: {
			articleContent(newValue, oldValue) {
				this.nodes = this.$markdownParse.parse(this.articleContent);
				
			}
		},
		methods: {
			showPreview(){
				this.showPre = !this.showPre;
			},

			// dealmark() {
			// 	console.log(this.articleContent);

			// 	var a = micromarkdown.parse(this.articleContent);
			// 	console.log(a);
			// 	this.nodes = a;
			// 	console.log(this.nodes);
			// },
			abstractFn(res) {
				if (!res) {
					return '';
				} else {
					var str = res.replace(/(\*\*|__)(.*?)(\*\*|__)/g, '') //全局匹配内粗体
						.replace(/\!\[[\s\S]*?\]\([\s\S]*?\)/g, '') //全局匹配图片
						.replace(/\[[\s\S]*?\]\([\s\S]*?\)/g, '') //全局匹配连接
						.replace(/<\/?.+?\/?>/g, '') //全局匹配内html标签
						.replace(/(\*)(.*?)(\*)/g, '') //全局匹配内联代码块
						.replace(/`{1,2}[^`](.*?)`{1,2}/g, '') //全局匹配内联代码块
						.replace(/```([\s\S]*?)```[\s]*/g, '') //全局匹配代码块
						.replace(/\~\~(.*?)\~\~/g, '') //全局匹配删除线
						.replace(/[\s]*[-\*\+]+(.*)/g, '') //全局匹配无序列表
						.replace(/[\s]*[0-9]+\.(.*)/g, '') //全局匹配有序列表
						.replace(/(#+)(.*)/g, '') //全局匹配标题
						.replace(/(>+)(.*)/g, '') //全局匹配摘要
						.replace(/\r\n/g, "") //全局匹配换行
						.replace(/\n/g, "") //全局匹配换行
						.replace(/\s/g, "") //全局匹配空字符;
					return str
				}
			},

		}

	}
</script>

<style>
	.preViewButton{
		font-size: 14px;
		line-height: 20px;
		height:20px;
		width: 60px;
		text-align: center;
		position: absolute;
		top:8px;
		right:12px;
		color:#8A6DE9;
	}
	.preViewArea{
		position: absolute;
		top:36px;
		right:0;
		width:600upx;
		padding:25upx;
		min-height: 400px;
		background: #F5F5F5;
		z-index: 10;
		border-radius: 8px;
		box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.16);
		overflow: scroll;
	}
</style>
<style> /* marke标签样式  后续marked样式可以单独做CSS */
	.marked_image{
		max-height: 50px;
		max-width: 100%;
	}
</style>
