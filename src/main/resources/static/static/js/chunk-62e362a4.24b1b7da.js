(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-62e362a4"],{2612:function(e,t,n){"use strict";n.d(t,"f",(function(){return a})),n.d(t,"a",(function(){return s})),n.d(t,"c",(function(){return u})),n.d(t,"m",(function(){return i})),n.d(t,"d",(function(){return o})),n.d(t,"n",(function(){return l})),n.d(t,"g",(function(){return c})),n.d(t,"h",(function(){return m})),n.d(t,"l",(function(){return d})),n.d(t,"k",(function(){return p})),n.d(t,"i",(function(){return f})),n.d(t,"j",(function(){return v})),n.d(t,"b",(function(){return b})),n.d(t,"e",(function(){return g})),n.d(t,"o",(function(){return h}));var r=n("b775");function a(e){return Object(r["a"])({url:"/users/getUserList",method:"get",params:e})}function s(e){return Object(r["a"])({url:"/users/deleteUser",method:"post",data:e})}function u(e){return Object(r["a"])({url:"/users/freezeUser",method:"post",data:e})}function i(e){return Object(r["a"])({url:"/users/unfreezeUser",method:"post",data:e})}function o(e){return Object(r["a"])({url:"/users/getConfig",method:"get",params:e})}function l(e){return Object(r["a"])({url:"/users/updateConfig",method:"post",data:e})}function c(e){return Object(r["a"])({url:"/users/getUsersConsumeList",method:"get",params:e})}function m(e){return Object(r["a"])({url:"/users/getUsersConsumeListUser",method:"get",params:e})}function d(e){return Object(r["a"])({url:"/users/makeupConsumeListUser",method:"post",data:e})}function p(e){return Object(r["a"])({url:"/users/makeDownConsumeListUser",method:"post",data:e})}function f(e){return Object(r["a"])({url:"/users/getUsersSpreadRecordList",method:"get",params:e})}function v(e){return Object(r["a"])({url:"/users/getUsersSpreadRecordUserList",method:"get",params:e})}function b(e){return Object(r["a"])({url:"/users/deleteUsersSpreadRecordUserList",method:"post",data:e})}function g(e){return Object(r["a"])({url:"/users/getSpreadConfig",method:"get",params:e})}function h(e){return Object(r["a"])({url:"/users/updateSpreadConfig",method:"post",data:e})}},"71d4":function(e,t,n){"use strict";n.r(t);var r=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"dataForm",staticStyle:{width:"80%","margin-left":"50px"},attrs:{model:e.temp,"label-position":"left"}},[n("el-form-item",{attrs:{label:"开车进群链接"}},[n("el-input",{attrs:{type:"text"},model:{value:e.temp.carUrl,callback:function(t){e.$set(e.temp,"carUrl",t)},expression:"temp.carUrl"}})],1),n("el-form-item",{attrs:{label:"客服链接"}},[n("el-input",{attrs:{type:"text"},model:{value:e.temp.serviceUrl,callback:function(t){e.$set(e.temp,"serviceUrl",t)},expression:"temp.serviceUrl"}})],1),n("el-form-item",{attrs:{label:"开启购买钻石"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.buyDiamond,callback:function(t){e.$set(e.temp,"buyDiamond",t)},expression:"temp.buyDiamond"}})],1),n("el-form-item",{attrs:{label:"开启购买金币"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.buyCoin,callback:function(t){e.$set(e.temp,"buyCoin",t)},expression:"temp.buyCoin"}})],1),n("el-form-item",{attrs:{label:"开启我的下载"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.download,callback:function(t){e.$set(e.temp,"download",t)},expression:"temp.download"}})],1),n("el-form-item",{attrs:{label:"开启开车进群"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.openCar,callback:function(t){e.$set(e.temp,"openCar",t)},expression:"temp.openCar"}})],1),n("el-form-item",{attrs:{label:"开启我的视频"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.myVideo,callback:function(t){e.$set(e.temp,"myVideo",t)},expression:"temp.myVideo"}})],1),n("el-form-item",{attrs:{label:"开启在线客服"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.service,callback:function(t){e.$set(e.temp,"service",t)},expression:"temp.service"}})],1),n("el-form-item",{attrs:{label:"开启我的钱包"}},[n("el-switch",{attrs:{"active-value":"1","inactive-value":"0"},model:{value:e.temp.money,callback:function(t){e.$set(e.temp,"money",t)},expression:"temp.money"}})],1),n("el-button",{on:{click:e.getConfig}},[e._v(" 重置 ")]),n("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.updateData()}}},[e._v(" 保存 ")])],1)],1)},a=[],s=n("2612"),u={data:function(){return{loading:!0,temp:{}}},created:function(){this.getConfig()},methods:{getConfig:function(){var e=this;this.loading=!0,Object(s["d"])().then((function(t){e.temp=t,e.loading=!1}))},updateData:function(){var e=this;this.loading=!0,Object(s["n"])(this.temp).then((function(t){e.$notify({title:"Success",message:"更新成功",type:"success",duration:2e3}),e.getConfig()}))}}},i=u,o=n("2877"),l=Object(o["a"])(i,r,a,!1,null,"42f681d9",null);t["default"]=l.exports}}]);