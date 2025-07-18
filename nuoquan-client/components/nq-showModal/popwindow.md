全局弹窗说明与uniapp API对照

HTML 部分，直接任意地方使用
<modal></modal> 

##API 转换：
#uni.showToast(OBJECT)   ==>   this.$store.commit('showToast', OBJECT);

OBJECT
| 参数       | 类型    | 必填 | 说明         |
| ---------- | ------ | ---- | -------------- |
| title      | String | 是   | 字数数限制未做 |
| content    | String | 否   | 字数数限制未做 |
| duration   | Sring  | 否   | 默认1500     |
| icon/image | String | 否   | 未完成      |

#uni.showLoading();      ==>   this.$store.commit('showLoading');

#uni.hideToast();  /  uni.hideLoading();  ==>    this.$store.commit('hideModal');

#uni.showModal(OBJECT)   ==>    this.$store.commit('showToast',OBJECT)

OBJECT
| 参数       | 类型     | 必填 | 说明         |
| ---------- | ------  | ---- | -------------- |
| title      | String  | 是   | 字数数限制未做   |
| content    | String  | 否   | 字数数限制未做   |
| duration   | Sring   | 否   | 默认1500        |
| icon/image | String  | 否   | 未完成          |
| showCancle | Boolean | 否   | 默认为 true     |
| confirmText| String  | 否   | 确认按钮文字，在右，默认为 “确定”   |
| cancleText | String  | 否   | 取消按钮文字，在左，默认为 “取消”   |
| success    | Function| 否   | confirm 回调函数 |
| fail       | Function| 否   | cancle  回调函数 |