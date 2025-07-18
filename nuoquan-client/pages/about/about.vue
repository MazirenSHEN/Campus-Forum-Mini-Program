<template>
	<view id="about-container">
		<!-- 导航栏 -->
		<uni-nav-bar class="navigationBar" 
		:style="{height: this.getnavbarHeight() + 'px'}" 
		:showLeftIcon="true" 
		:isNavHome="isNavHome" 
		:left-text="lang.back"
		:title="lang.about" 
		:height="this.getnavbarHeight().bottom + 5"></uni-nav-bar>
		<view :style="{height: this.getnavbarHeight().bottom + 5 + 'px'}"></view>
		<!--顶部名称部分-->
		<view class="top-picture">
			<!--logo-->
			<view><image class="logo-app" src="../../static/icon/logo_app.jpeg" ></image></view>
			<!--Nottinghome-->
			<view class="company-name">STOA</view>
			<!--版本-->
			<view class="version">{{versionNum}}</view>
		</view>
		<!--表单部分-->
		<view class="two-forms">
			<dropdown :lang="lang" :objList="data" v-for="(data,index) in dataList" :key="index"> </dropdown>
		</view>
		
		<!--底部图片部分-->
		<view class="bottom-picture"><image src="../../static/BG/signin_bottom.png" mode="aspectFit"></image></view>
	</view>
</template>

<script>
import uniNavBar from "@/components/uni-nav-bar/uni-nav-bar.vue";	
import { mapState, mapMutations } from 'vuex';
import dropdown from "@/components/dropdown.vue";
	
export default {
	components:{
		uniNavBar,
		dropdown
	},
	computed: {
		...mapState(['lang'])
	},
	data() {
		return {
			versionNum: "3.1.5",
			isNavHome: getApp().globalData.isNavHome,//判断导航栏左侧是否显示home按钮
			dataList:[
				{
					name:'社区守则 Co-creation Guidelines', 
					// subtitle:'商务合作',
					body:'大家好，非常感谢各位同学一直以来对聚像科技的关注和支持。\n\
						聚像科技旗下的“STOA”社区欢迎你参与到全体同学的精彩生活之中、与聚像科技共同创造价值、分享价值！\n \
						这里是属于每一个同学的社区。大家既是参与者，也是建设者。我们尊重每一个“STOA”社区用户的分享、表达和共创权利。无论是您发布的原创内容，分享的优质内容，还是您提供给我们的各种建议、以及各种形式的支持和鼓励，我们都非常欢迎！\n \
						随着社区用户的不断进入、社区的日益壮大，为维护良好的共创社区氛围，我们的社区行为准则也做了相应的调整和升级，请大家共同遵守。我们欢迎大家的建议，并据此做出及时调整！ \n \
						“STOA”社区禁止发布的内容:\n\
						1、禁止发布违反国家法律规定，国家宪法基本原则，损害国家荣誉和利益的内容。滥用、错用国家有关规定已明确的标识、呼号、称谓、用语等；\n\
						2、禁止发布危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一，煽动民族仇恨，民族歧视，破坏民族团结，对领士的描述不符合国家定论等内容；\n\
						3、禁止发布散布谣言，扰乱社会秩序，破坏社会稳定的内容;\n\
						4、禁止发布侮辱和损坏/损毁国旗、国徽人民币的内容。例如在不适宜的场合使用国旗、国徽，以焚烧、毁损、涂划、践踏、恶搞等方式侮辱国旗、国徽等；\n\
						5、禁止发布恶搞、贬损国家和民族的形象、精神内容;\n\
						6、禁止发布恶搞、丑化、亵渎、否定革命领袖、英雄烈士事迹和精神的内容。将政治内容、经典文化、严肃历史文化进行过度娱乐化展示解读;\n\
						7、禁止发布损害人民军队、国安、警察行政、司法等国家公职人员形象的内容；\n\
						8、禁止发布未经国家授权或批准，装扮/模仿党和国家领导人的形象，参加主持、演讲、摆拍等活动，谋取利益或哗众取宠产生不良影响;\n\
						9、禁止发布影响公共秩序与安全的群体性事件、恐怖主义、非官方新闻单位发布的灾难事故信息或重大安全事故等内容；\n\
						10、禁止发布未经国家相关部门证实且具有误导性的内容;\n\
						11、禁止发布淫秽、色情、赌博、毒品、暴力、凶杀、恐慌、引导教唆他人犯罪等内容。涉及破坏社会稳定、影响公共秩序与公共安全的群体性事件；\n\
						12、禁止发布宣扬不良、消极颓废的人生观、世界观和价值观的内容；\n\
						13、禁止发布针对民族、种族、宗教、性取向、性别、年龄、地域等的人身攻击内容；\n\
						14、禁止发布涉及宣扬邪教、封建迷信、戏说/调侃宗教、恶意伤害民族宗教感情言论、过度宣扬宗教教义/教规/仪式、违背/破坏国家宗教政策的内容;\n\
						15、禁止发布煽动民族仇恨、民族歧视、损害民族和地域团结的内容；\n\
						16、禁止发布诽谤、捏造、诋毁他人形象的内容;\n\
						17、违法开展募集、募捐活动；\n\
						18、利用社区实施诈骗、传销、走私等违法犯罪行为;\n\
						19、禁止发布和违法销售任何国家法律法规禁止或限制销售的商品/服务，包括但不限于枪支弹药、爆炸物、管制刀具、野生动植物、医疗服务、医美服务、医疗器械、药品和保健品、三无产品、烟草等商品；\n\
						20、禁止发布涉及和暴露他人个人隐私的内容;\n\
						21、禁止发布投资融资、股权交易、理财产品、保险品或其他金融产品相关的内容。为避免被诈骗也请共创社区用户们不要轻信及点击不明内容和来源的外部链接；\n\
						22、禁止发布带有第三方平台的二维码或链接，商业广告或类似的商业招揽信息、过度营销信息、导流内容及垃圾信息；\n\
						23、禁止使用违规的头像和昵称进行注册。\n\
						\n\
						我们在这里，期待遇到每一个不同又非凡的你。\n\
						\n\
						更多资讯，请关注官方微信公众号：\n\
						Jumboxtech'
				},
				{
					name:'更新公告	Update Announcement',
					subtitle:this.versionNum,
					body:'\ STOA v3.1.5 版本更新！\n \
						1. 改变了发帖要求，现在不绑定邮箱也能发帖啦！\n \
						2. 修复了查看个人信息的bug\n \
						\n \
						'
				}
			],
		};
	},
};
</script>

<style>
page {
	width: 100%;
	height: 100%;
	overflow: scroll;
}
#about-container{
	width:100%;
	height:100%;
}
.top-picture{
	height:30%;
}
.two-forms{
	min-height:30%;
}
.bottom-picture {
	height:40%;
}
.logo-app {
	display: block;
	margin: auto;
	margin-top: 80upx;
	height: 144upx;
	width: 144upx;
}
.company-name {
	margin-top: 16upx;
	text-align: center;
	font-family: 'Source Han Sans CN';
	font-size: 34upx;
	color: #888888;
	opacity: 1;
}
.version {
	text-align: center;
	font-family: 'Source Han Sans CN';
	font-size: 34upx;
	color: #888888;
	opacity: 1;
	margin-top: 10upx;
}
.bottom-picture image {
	width: 100%;
}
</style>
