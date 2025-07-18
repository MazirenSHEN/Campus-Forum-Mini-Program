const request = {
	namespaced: false,
	state: {
		API:{
			fDeleteArticle:				'/article/fDeleteArticle',
			getQrcodeUnlimit:			'/wechat/getQrcodeUnlimit',
			userLikeArticle:			'/article/userLikeArticle',
			userUnLikeArticle:			'/article/userUnLikeArticle',
			queryAllArticles:			'/queryAllArticles',
			getArticleSubComments:		'/article/getSubComments',
			getVoteSubComments:			'/vote/getSubComments',
			saveComment:				'/article/saveComment',
			saveVoteComment:			'/vote/saveVoteComment',
			userLikeComment:			'/article/userLikeComment',
			userLikeVoteComment:		'/vote/userLikeVoteComment',
			userUnLikeComment:			'/article/userUnLikeComment',
			userUnLikeVoteComment:		'/vote/userUnLikeVoteComment',
			userUnLikeVoteComment:		'/article/getArticleById',
			addViewCount:				'/article/addViewCount',
			getMainComments:				'/article/getMainComments',
			//收藏文章
			userCollectArticle:			'/article/userCollectArticle',
			userUncollectArticle:		'/article/userUncollectArticle',
			// 关注相关
			follow:						'/user/follow',
			dontFollow:					'/user/dontFollow',
			queryFansAndFollow:			'/user/queryFansAndFollow',
			//hot
			getHotTop10:				'/article/getHotTop10',
			// myCollection
			queryCollectArticle:		'/article/queryCollectArticle',
			// myFavorite
			queryCollectArticle:		'/article/queryCollectArticle',
			//myPublish
			queryPublishHistory:		'/article/queryPublishHistory',
			queryPublishedVoteHistory:	'/vote/queryPublishedVoteHistory',
			// personPublic
			follow:						'/user/follow',
			queryUserWithFollow:		'/user/queryUserWithFollow',
			// profile
			updateUser:					'/user/updateUser',
			getCode:					'/user/getCode',
			
			confirmCode:				'/user/confirmCode',
			//search
			hot:						'/article/hot',
			searchArticleYANG:			'/article/searchArticleYANG',
			// signin
			getWxUserInfo:				'/user/getWxUserInfo',
			getTagsList:				'/article/getTagsList',
			// submit
			uploadArticle:				'/article/uploadArticle',
			// submitVote
			uploadVote:					'/vote/uploadVote',
			 // mine
			queryUser:					'/user/queryUser',
			 // index
			queryArticles:				'/article/queryArticles',
			queryUser:					'/user/queryUser',
			updateLatestLoginTime:		'/user/updateLatestLoginTime',
			getTagsList:				'/article/getTagsList',
			 // votePage 相关没有加入
			
			},
	},
	actions: {
		request(context, payload) {
			console.log(context);
			console.log(payload);
			//context.commit('showLoading');
			console.log('in API action'),
			console.log(payload),
			uni.request({
				url: 'https://www.checkchack.cn:8443/nottinghome/article/queryArticles',
				method: 'POST',
				data:payload.data,
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: res => {
					if(res.data.status == 200){
						
						//context.commit('hideModal');
						payload.success(res.data.data.rows);
						payload.returnaddress = res.data.data.rows;
						// debugger;
					}else{
						console.log('request error, code ' + res.data.status);
					}
				},
				fail: res => {
					console.log('requst failed, net error');
					console.log(res);
				}
			});
		},
	},
};

export default request;
