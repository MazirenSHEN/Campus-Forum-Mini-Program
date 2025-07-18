const showModal = {
	state:{
		mode:'default',  //mode = "toast/modal"
		show:false,
		duration:'1500',
		
		image:null, 
		title:"",
		content:'',
		cancelText:"取消",
		confirmText:"确定",
		
	//	cancelColor:"#000000",
	//	confirmColor:"#576b95",
		
		success:null,
		fail:null,
	},

	mutations: {
		hideLoading(state) {
			console.log('hideLoading');
			state.show = false;
			const defaultState = {
				mode:'modal',  //mode = "toast/modal/loading"
					show:false,
					title:"默认标题",
					content:'',
					duration:'1500',
					showCancel:true,
					cancelText:"取消",
				//	cancelColor:"#000000",
					confirmText:"确定",
				//	confirmColor:"#576b95",
					image:null, 
					success:null,
					fail:null,
			}
			state = Object.assign(state, defaultState);
			console.log(state);
		},
		showModal(state, data) {
			state.mode = 'modal',
			console.log('showModal function triggered');
			state = Object.assign(state, data);
			state.show = true;
			console.log(state);
		},
		showToast(state,data){
			state.mode = 'toast',
			console.log('showToast function triggered');
			state = Object.assign(state, data);
			state.show = true;
			console.log(state);
		},
		showLoading(state,data){
			state.mode = 'loading';
			state.image = true;
			state.show = true;
		},
		
		success(state, res) {
			let cb = state.success
			let resObj = {
				cancel: false,
				confirm: false
			}
			res == "confirm" ? resObj.confirm = true : resObj.cancel = true
			cb && cb(resObj)
		},
		fail(state,res){
			
			let cb = state.fail
			let resObj = {
				cancel: false,
				confirm: false
			}
			res == "confirm" ? resObj.confirm = true : resObj.cancel = true,
			cb && cb(resObj)
		},
	},
};

export default showModal;
