<template>
	<view class="searchResultUserCard" hover-class="hoverColor">
		<image class="touxiang" src="../../static/BG/1-blue.png"></image>
		<view class="right-body">
			<view class="userName">

			</view>
			<view class="personalMessage">

			</view>
		</view>

	</view>
</template>

<script>
	export default {
		name: 'aticlebrief',
		props: {
			resultUser: "",
		},
		data() {
			return {
				serverUrl: this.$serverUrl,
			};
		},

		methods: {
			goToPersonPublic(userId) {
				uni.navigateTo({
					url: '/pagesSubA/personpublic/personpublic?userId=' + userId
				});
			},
		},
	};
</script>

<style>
	.searchResultUserCard {
		margin: 0 23px;
	}

	.touxiang {
		width: 44px;
		height: 44px;
		display: inline-block;
		vertical-align: top;
		border-radius: 22px;
	}

	.right-body {
	width: calc(100% - 60px);
	}

	.userName {
		width: 86px;
		height: 17px;
		font-size: 17px;
		font-weight: 600;
		line-height: 17px;
	}

	.personalMessage {
		margin-top: 12px;
		height: 14px;
		font-size: 14px;
		line-height: 14px;
		color: rgba(155, 155, 155, 1);
	}
</style>
