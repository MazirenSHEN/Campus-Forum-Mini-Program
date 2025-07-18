//获取当前时间，格式YYYY-MM-DD HH:MM:SS
const GetNowTime = time => {
	var date = time,
		year = date.getFullYear(),
		month = date.getMonth() + 1,
		day = date.getDate(),
		hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours(),
		minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes(),
		second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
	month >= 1 && month <= 9 ? (month = "0" + month) : "";
	day >= 0 && day <= 9 ? (day = "0" + day) : "";
	// var timer = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
	var timer = year + '-' + month + '-' + day;
	return timer;
}

//返回日期和周几数组
function weekDate() {
	var myDate = new Date();
	myDate.toLocaleDateString();
	var month = myDate.getMonth() + 1;
	var time = myDate.getFullYear() + '年' + month + '月' + myDate.getDate() + '日';
	var total = 1; // 个数
	var dayList = [];
	dayList.push({
		'day': myDate.getDate(),
		'month': myDate.getMonth() + total,
		'week': toWeekDay(myDate.getDay()),
		'year': myDate.getFullYear()
	});
	for (var i = 0; i < 10; i++) {
		myDate.setDate(myDate.getDate() + total); // number 是最近几天  则会自动计算
		// 需求  月份-日   星期几      
		dayList.push({
			'day': myDate.getDate(),
			'month': myDate.getMonth() + total,
			'week': toWeekDay(myDate.getDay()),
			'year': myDate.getFullYear()
		})
	}
	// return dayList;
	let length = dayList.length
	let arrOne = dayList[0]
	let arrLast = dayList[length - 1]
	let StartDate = arrOne.year.toString() + '-' + arrOne.month + '-' + arrOne.day
	let EndDate = arrLast.year.toString() + '-' + arrLast.month + '-' + arrLast.day
	return {
		dayList,
		StartDate,
		EndDate
	}
}

function toWeekDay(weekDay) { // 传入数据  讲一周的某一天返回成中文状态下的字符
	switch (weekDay) {
		case 1:
			return '一';
			break;
		case 2:
			return '二';
			break;
		case 3:
			return '三';
			break;
		case 4:
			return '四';
			break;
		case 5:
			return '五';
			break;
		case 6:
			return '六';
			break;
		case 0:
			return '日';
			break;
		default:
			break;
	}
	return '传入未知参数';
}

module.exports = {
	GetNowTime,
	GetPhone,
	weekDate
}