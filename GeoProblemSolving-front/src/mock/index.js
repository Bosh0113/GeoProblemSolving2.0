import Mock from 'mockjs' //引入mockjs，npm已安装
import { Random } from 'mockjs' // 引入random对象,随机生成数据的对象，（与占位符@一样）
Mock.setup({
	timeout:1000  //设置请求延时时间
})

// const getTopic = function(option){
//   let topiclist = []
//   for(let i = 0 ;i < 10; i++){
//     const o = {
//       title:Random.cname(),
//     }
//   }
// }


const getdata = function(option){ //定义请求数据方法
	let datalist = []
	for (let i = 0; i < 30; i += 1) {
	  const o = {  //mockjs模拟随机生成数据，生成20条
	   recipeId: Random.guid(),
	   billId: Random.string(10),
	   orgId: Random.string('number', 8, 10),
	   Date:Random.date('yyyy-MM-dd'),
	   time:Random.time('A HH:mm:ss'),
	   adress:Random.county(),
	   viewName: Random.cword(4, 16), // 随机生成任意名称
	   personName: Random.first(),
	   reason: Random.csentence(10, 32),
	  }
	  datalist.push(o)
	 }
	return{
		data:datalist
	}
}
const getdata2 = function(option){ //定义请求数据方法
	let datalist2 = []
	for (let i = 0; i < 30; i += 1) {
	  const o = {  //mockjs模拟随机生成数据，生成20条
	   recipeId: Random.guid(),
	   billId: Random.string(10),
	   orgId: Random.string('number', 8, 10),
	   Date:Random.date('yyyy-MM-dd'),
	   time:Random.time('A HH:mm:ss'),
	   adress:Random.county(),
	   viewName: Random.cword(4, 16), // 随机生成任意名称
	   personName: Random.first(),
	   reason: Random.ctitle(10, 32),
	  }
	  datalist2.push(o)
	 }
	return{
		data:datalist2
	}
}
Mock.mock('/users', /post|get/i,getdata2) //调用模拟数据方法
Mock.mock('/user', /post|get/i,getdata) //调用模拟数据方法

