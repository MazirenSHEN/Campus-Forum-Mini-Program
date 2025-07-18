<template>
	<view v-show="show" style="width: 0;height: 0;">
		<view class="container"  @touchmove.stop.prevent="">
			<view class="modalBox" @click="closeModal" @touchmove.stop.prevent="">
				<image class="pic" v-if="image" mode="aspectFit" src="../../static/loadingGiff.jpg"></image>
				<view class="modal-title" v-show="title">{{title}}</view>
				<view class="modal-content">{{content}}</view>
				<view class="modal-menu" v-if=" mode=='modal' ">
					<view class="cancle" @click.stop="clickBtn('cancel')">{{cancelText}}</view>
					<view class="confirm" @click.stop="clickBtn('confirm')">{{confirmText}}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'

	export default {
		computed: {
			mode() {
				return this.$store.state.showModal.mode;
			},
			show() {
				console.log('state.show triggered');
				console.log(this.$store.state.showModal.show);
				return this.$store.state.showModal.show;
			},
			duration() {
				return this.$store.state.showModal.duration;
			},
			
			image(){
				return this.$store.state.showModal.image;
			},
			title() {
				return this.$store.state.showModal.title;
			},
			content() {
				return this.$store.state.showModal.content;
			},
			cancelText() {
				return this.$store.state.showModal.cancelText;
			},
			confirmText() {
				return this.$store.state.showModal.confirmText;
			},

			
		},

		methods: {
			closeModal() {
				this.$store.commit('hideLoading')
			},
			clickBtn(res) {
				if (res == 'confirm') {
					this.$store.commit('success', res)
				} else if (res == 'cancel') {
					this.$store.commit('fail', res)
				}
				this.$store.commit('hideLoading')
			}
		},
		beforeDestroy() {
			this.$store.commit('hideLoading')
		},
		watch: {
			show(newValue, oldValue) {
				if (newValue) {
					var that = this;
					console.log(this.$store.state.showModal);
					if (this.$store.state.showModal.mode == 'toast') {
						console.log("mode == toast, timing");
						console.log(that.duration);
						setTimeout(function() {
							that.$store.commit('hideLoading')
						}, that.duration);
					}
				}

			}
		},
		mounted() {
			var that = this;
			console.log(this.$store.state.showModal);
			if (this.$store.state.showModal.mode == 'toast') {
				console.log("mode == toast, timing");
				console.log(that.duration)
				setTimeout(function() {
					that.$store.commit('hideLoading')
				}, that.duration);
			}
		}
	};
</script>


<style scoped>
	.container {
		background-color: #ECECECEC;
		position: fixed;
		top: 0;
		left: 0;
		width: 750upx;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 100;
	}

	.modalBox {
		border-radius: 12px;
		background: #FFFFFF;
		width: 670upx;

	}

	.pic {
		display: block;
		margin: 36px auto 36px;
		width: 280upx;
		height: 280upx;
	}

	.modal-title {
		font-size: 17px;
		color: #353535;
		font-weight: bold;
		height: 20px;
		ling-height: 20px;
		text-align: center;
		margin: 28px auto 20px;
	}

	.modal-menu {
		display: flex;
		width: 606upx;
		margin: 16px auto;
		justify-content: space-between;
	}

	.cancle {
		color: #888888;
		width: 292upx;
		height: 41px;
		background: rgba(236, 236, 236, 1);
		border-radius: 8px;
		line-height: 41px;
		text-align: center;
	}

	.confirm {
		color: #FFFFFF;
		width: 292upx;
		height: 41px;
		background: rgba(252, 192, 65, 1);
		border-radius: 8px;
		line-height: 41px;
		text-align: center;
	}
</style>
