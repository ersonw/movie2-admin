(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0a9051e0"],{"333d":function(t,e,n){"use strict";var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pagination-container",class:{hidden:t.hidden}},[n("el-pagination",t._b({attrs:{background:t.background,"current-page":t.currentPage,"page-size":t.pageSize,layout:t.layout,"page-sizes":t.pageSizes,total:t.total},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"update:pageSize":function(e){t.pageSize=e},"update:page-size":function(e){t.pageSize=e},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}},"el-pagination",t.$attrs,!1))],1)},i=[];n("a9e3");Math.easeInOutQuad=function(t,e,n,a){return t/=a/2,t<1?n/2*t*t+e:(t--,-n/2*(t*(t-2)-1)+e)};var r=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(t){window.setTimeout(t,1e3/60)}}();function l(t){document.documentElement.scrollTop=t,document.body.parentNode.scrollTop=t,document.body.scrollTop=t}function o(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function s(t,e,n){var a=o(),i=t-a,s=20,u=0;e="undefined"===typeof e?500:e;var c=function t(){u+=s;var o=Math.easeInOutQuad(u,a,i,e);l(o),u<e?r(t):n&&"function"===typeof n&&n()};c()}var u={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[20,300,900,3e3]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(t){this.$emit("update:page",t)}},pageSize:{get:function(){return this.limit},set:function(t){this.$emit("update:limit",t)}}},methods:{handleSizeChange:function(t){this.$emit("pagination",{page:this.currentPage,limit:t}),this.autoScroll&&s(0,800)},handleCurrentChange:function(t){this.$emit("pagination",{page:t,limit:this.pageSize}),this.autoScroll&&s(0,800)}}},c=u,d=(n("5b13"),n("2877")),m=Object(d["a"])(c,a,i,!1,null,"02467173",null);e["a"]=m.exports},"5b13":function(t,e,n){"use strict";n("9037")},6724:function(t,e,n){"use strict";n("8d41");var a="@@wavesContext";function i(t,e){function n(n){var a=Object.assign({},e.value),i=Object.assign({ele:t,type:"hit",color:"rgba(0, 0, 0, 0.15)"},a),r=i.ele;if(r){r.style.position="relative",r.style.overflow="hidden";var l=r.getBoundingClientRect(),o=r.querySelector(".waves-ripple");switch(o?o.className="waves-ripple":(o=document.createElement("span"),o.className="waves-ripple",o.style.height=o.style.width=Math.max(l.width,l.height)+"px",r.appendChild(o)),i.type){case"center":o.style.top=l.height/2-o.offsetHeight/2+"px",o.style.left=l.width/2-o.offsetWidth/2+"px";break;default:o.style.top=(n.pageY-l.top-o.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",o.style.left=(n.pageX-l.left-o.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return o.style.backgroundColor=i.color,o.className="waves-ripple z-active",!1}}return t[a]?t[a].removeHandle=n:t[a]={removeHandle:n},n}var r={bind:function(t,e){t.addEventListener("click",i(t,e),!1)},update:function(t,e){t.removeEventListener("click",t[a].removeHandle,!1),t.addEventListener("click",i(t,e),!1)},unbind:function(t){t.removeEventListener("click",t[a].removeHandle,!1),t[a]=null,delete t[a]}},l=function(t){t.directive("waves",r)};window.Vue&&(window.waves=r,Vue.use(l)),r.install=l;e["a"]=r},8307:function(t,e,n){"use strict";n.r(e);var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-row",{staticStyle:{"flex-wrap":"warp","text-align-all":"center"},attrs:{type:"flex",gutter:20}},[n("el-col",[n("el-card",[n("el-form",{staticStyle:{"margin-left":"50px"},attrs:{model:t.user,"label-position":t.left}},[n("el-form-item",{attrs:{label:"用户ID"}},[n("el-tag",[t._v(t._s(t.user.id))])],1),n("el-form-item",{attrs:{label:"用户昵称"}},[n("el-tag",[t._v(t._s(t.user.nickname))])],1),n("el-form-item",{attrs:{label:"会员状态"}},[n("el-tag",[t._v(t._s(t.user.member))])],1),n("el-form-item",{attrs:{label:"会员等级"}},[n("el-tag",[t._v(t._s(t.user.level))])],1),n("el-form-item",{attrs:{label:"会员到期"}},[n("el-tag",[t._v(t._s(t._f("parseTime")(t.user.expired,"{y}-{m}-{d} {h}:{i}")))])],1)],1)],1)],1),n("el-col",[n("el-card",[n("el-form",{staticStyle:{"margin-left":"50px"},attrs:{model:t.user,"label-position":t.left}},[n("el-form-item",{attrs:{label:"手机号"}},[n("el-tag",[t._v(t._s(t.user.phone))])],1),n("el-form-item",{attrs:{label:"注册IP"}},[n("el-tag",[t._v(t._s(t.user.registerIp))])],1),n("el-form-item",{attrs:{label:"个人签名"}},[n("el-tag",[t._v(t._s(t.user.text))])],1),n("el-form-item",{attrs:{label:"注册时间"}},[n("el-tag",[t._v(t._s(t._f("parseTime")(t.user.addTime,"{y}-{m}-{d} {h}:{i}")))])],1),n("el-form-item",{attrs:{label:"更新时间"}},[n("el-tag",[t._v(t._s(t._f("parseTime")(t.user.updateTime,"{y}-{m}-{d} {h}:{i}")))])],1)],1)],1)],1),n("el-col",[n("el-card",[n("el-form",{staticStyle:{"margin-left":"50px"},attrs:{model:t.user,"label-position":t.left}},[n("el-form-item",{attrs:{label:"资料完整度"}},[n("el-tag",[t._v(t._s(t.user.profile)+"%")])],1),n("el-form-item",{attrs:{label:"发布视频数"}},[n("el-tag",[t._v(t._s(t.user.videos))])],1),n("el-form-item",{attrs:{label:"总获赞"}},[n("el-tag",[t._v(t._s(t.user.likes))])],1),n("el-form-item",{attrs:{label:"总播放"}},[n("el-tag",[t._v(t._s(t.user.plays))])],1),n("el-form-item",{attrs:{label:"总评论与回复"}},[n("el-tag",[t._v(t._s(t.user.comments))])],1)],1)],1)],1)],1)],1),n("div",{staticClass:"filter-container"},[n("el-input",{staticClass:"filter-item",staticStyle:{width:"300px"},attrs:{placeholder:"视频标题",clearable:""},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.handleFilter(e)}},model:{value:t.listQuery.title,callback:function(e){t.$set(t.listQuery,"title",e)},expression:"listQuery.title"}}),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"primary",icon:"el-icon-search"},on:{click:t.handleFilter}},[t._v(" 搜索 ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"danger",icon:"el-icon-delete-solid"},on:{click:t.handleDeleteAll}},[t._v(" 删除选中("+t._s(t.ids.length)+") ")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],key:t.tableKey,staticStyle:{width:"100%"},attrs:{data:t.list,border:"",fit:"","highlight-current-row":""},on:{"selection-change":t.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"ID",prop:"id",sortable:"custom",align:"center",width:"80"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.id))])]}}])}),n("el-table-column",{attrs:{label:"标题","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",{staticClass:"link-type",on:{click:function(e){return t.handleUpdate(a)}}},[t._v(t._s(a.title))])]}}])}),n("el-table-column",{attrs:{label:"上传IP","class-name":"status-col",width:"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(" "+t._s(a.ip)+" ")])]}}])}),n("el-table-column",{attrs:{label:"点赞","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(a.like)+" ")])]}}])}),n("el-table-column",{attrs:{label:"播放","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(a.play)+" ")])]}}])}),n("el-table-column",{attrs:{label:"评论数","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(a.comments)+" ")])]}}])}),n("el-table-column",{attrs:{label:"创建时间",width:"150px",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(t._f("parseTime")(a.addTime,"{y}-{m}-{d} {h}:{i}")))])]}}])}),n("el-table-column",{attrs:{label:"更新时间",width:"150px",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(t._f("parseTime")(a.updateTime,"{y}-{m}-{d} {h}:{i}")))])]}}])}),n("el-table-column",{attrs:{label:"用户主页置顶","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-switch",{attrs:{"active-value":1,"inactive-value":0},model:{value:a.pin,callback:function(e){t.$set(a,"pin",e)},expression:"row.pin"}})]}}])}),n("el-table-column",{attrs:{label:"转发开关","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-switch",{attrs:{"active-value":1,"inactive-value":0},model:{value:a.forward,callback:function(e){t.$set(a,"forward",e)},expression:"row.forward"}})]}}])}),n("el-table-column",{attrs:{label:"状态","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(t._f("statusFilter")(a.status))+" ")])]}}])}),n("el-table-column",{attrs:{label:"操作",align:"center",width:"230","class-name":"small-padding fixed-width"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row,i=e.$index;return[n("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(e){return t.handleUpdate(a)}}},[t._v(" 编辑 ")]),n("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(e){return t.handleDelete(a,i)}}},[t._v(" 删除 ")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{total:t.total,page:t.listQuery.page,limit:t.listQuery.limit},on:{"update:page":function(e){return t.$set(t.listQuery,"page",e)},"update:limit":function(e){return t.$set(t.listQuery,"limit",e)},pagination:t.getList}}),n("el-drawer",{attrs:{size:"60%",visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"90%","margin-left":"50px"},attrs:{model:t.temp,"label-position":"left"}},[n("el-form-item",{attrs:{label:"标题"}},[n("el-input",{attrs:{type:"textarea"},model:{value:t.temp.title,callback:function(e){t.$set(t.temp,"title",e)},expression:"temp.title"}})],1),n("el-form-item",{attrs:{label:"用户置顶"}},[n("el-switch",{attrs:{"active-value":1,"inactive-value":0},model:{value:t.temp.pin,callback:function(e){t.$set(t.temp,"pin",e)},expression:"temp.pin"}})],1),n("el-form-item",{attrs:{label:"开启转发"}},[n("el-switch",{attrs:{"active-value":1,"inactive-value":0},model:{value:t.temp.forward,callback:function(e){t.$set(t.temp,"forward",e)},expression:"temp.forward"}})],1),n("el-form-item",{attrs:{label:"状态"}},[n("el-select",{staticClass:"filter-item",attrs:{placeholder:"Please select"},model:{value:t.temp.status,callback:function(e){t.$set(t.temp,"status",e)},expression:"temp.status"}},t._l(t.statusOptions,(function(t,e){return n("el-option",{key:e,attrs:{label:t,value:e}})})),1)],1),n("el-button",{on:{click:function(e){t.dialogFormVisible=!1}}},[t._v(" 取消 ")]),n("el-button",{attrs:{type:"primary"},on:{click:function(e){return t.updateData()}}},[t._v(" 确认 ")])],1)],1)],1)},i=[],r=(n("d81d"),n("c740"),n("a434"),n("9f33")),l=n("6724"),o=n("ed08"),s=n("333d"),u={name:"ComplexTable",components:{Pagination:s["a"]},directives:{waves:l["a"]},filters:{statusFilter:function(t){switch(t){case 0:return"未审核";case 1:return"审核通过";default:return"审核失败"}}},data:function(){return{tableKey:0,list:null,total:0,listLoading:!0,infoLoading:!0,listQuery:{page:1,limit:20,title:void 0},ids:[],temp:{},statusOptions:["下架","通过"],dialogFormVisible:!1,id:0,user:{}}},created:function(){var t=this.$route.params.id;this.id=parseInt(t),this.getList(),this.getUser()},methods:{getUser:function(){var t=this;this.infoLoading=!0,Object(r["o"])(this.id).then((function(e){t.user=e,setTimeout((function(){t.infoLoading=!1}),1500)}))},getList:function(){var t=this;this.listLoading=!0,Object(r["n"])(this.listQuery,this.id).then((function(e){t.list=e.list,t.total=e.total,setTimeout((function(){t.listLoading=!1}),1500)}))},handleFilter:function(){this.listQuery.page=1,this.getList()},handleSelectionChange:function(t){this.ids=t.map((function(t){return t.id}))},handleUser:function(t){console.log(t)},handleUpdate:function(t){var e=this;this.temp=Object.assign({},t),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},updateData:function(){var t=this;this.$refs["dataForm"].validate((function(e){if(e){var n=Object.assign({},t.temp);Object(r["u"])(n).then((function(e){var n=t.list.findIndex((function(e){return e.id===t.temp.id}));t.list.splice(n,1,e),t.dialogFormVisible=!1,t.$notify({title:"Success",message:"更新成功",type:"success",duration:2e3})}))}}))},handleDeleteAll:function(){var t=this;Object(r["d"])({ids:this.ids,id:this.id}).then((function(e){t.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3}),t.getList()}))},handleDelete:function(t,e){var n=this,a=[];a.push(t.id),Object(r["d"])({ids:a,id:this.id}).then((function(t){n.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3}),n.list.splice(e,1)}))},formatJson:function(t){return this.list.map((function(e){return t.map((function(t){return"timestamp"===t?Object(o["d"])(e[t]):e[t]}))}))}}},c=u,d=n("2877"),m=Object(d["a"])(c,a,i,!1,null,null,null);e["default"]=m.exports},"8d41":function(t,e,n){},9037:function(t,e,n){},"9f33":function(t,e,n){"use strict";n.d(e,"q",(function(){return i})),n.d(e,"d",(function(){return r})),n.d(e,"u",(function(){return l})),n.d(e,"n",(function(){return o})),n.d(e,"o",(function(){return s})),n.d(e,"l",(function(){return u})),n.d(e,"k",(function(){return c})),n.d(e,"t",(function(){return d})),n.d(e,"g",(function(){return m})),n.d(e,"i",(function(){return f})),n.d(e,"b",(function(){return p})),n.d(e,"f",(function(){return h})),n.d(e,"s",(function(){return g})),n.d(e,"j",(function(){return v})),n.d(e,"h",(function(){return b})),n.d(e,"a",(function(){return y})),n.d(e,"e",(function(){return w})),n.d(e,"r",(function(){return _})),n.d(e,"m",(function(){return k})),n.d(e,"c",(function(){return S})),n.d(e,"p",(function(){return V})),n.d(e,"v",(function(){return x}));var a=n("b775");function i(t){return Object(a["a"])({url:"/shortVideo/getVideoList",method:"get",params:t})}function r(t){return Object(a["a"])({url:"/shortVideo/deleteVideo",method:"post",data:t})}function l(t){return Object(a["a"])({url:"/shortVideo/updateVideo",method:"post",data:t})}function o(t,e){return Object(a["a"])({url:"/shortVideo/getShortVideoUser/"+e,method:"get",params:t})}function s(t){return Object(a["a"])({url:"/shortVideo/getUser/"+t,method:"get"})}function u(t){return Object(a["a"])({url:"/shortVideo/getAuditVideoList",method:"get",params:t})}function c(t){return Object(a["a"])({url:"/shortVideo/getAuditVideo",method:"get",params:t})}function d(t){return Object(a["a"])({url:"/shortVideo/passAuditVideo",method:"post",data:t})}function m(t){return Object(a["a"])({url:"/shortVideo/denyAuditVideo",method:"post",data:t})}function f(t){return Object(a["a"])({url:"/shortVideo/getAuditCommentList",method:"get",params:t})}function p(t){return Object(a["a"])({url:"/shortVideo/deleteAuditComments",method:"post",data:t})}function h(t){return Object(a["a"])({url:"/shortVideo/denyAuditComments",method:"post",data:t})}function g(t){return Object(a["a"])({url:"/shortVideo/passAuditComments",method:"post",data:t})}function v(t,e){return Object(a["a"])({url:"/shortVideo/getAuditCommentListChild/"+e,method:"get",params:t})}function b(t){return Object(a["a"])({url:"/shortVideo/getAuditComment",method:"get",params:t})}function y(t){return Object(a["a"])({url:"/shortVideo/deleteAuditComment",method:"post",data:t})}function w(t){return Object(a["a"])({url:"/shortVideo/denyAuditComment",method:"post",data:t})}function _(t){return Object(a["a"])({url:"/shortVideo/passAuditComment",method:"post",data:t})}function k(t){return Object(a["a"])({url:"/shortVideo/getShareVideo",method:"get",params:t})}function S(t){return Object(a["a"])({url:"/shortVideo/deleteShareVideo",method:"post",data:t})}function V(t){return Object(a["a"])({url:"/shortVideo/getVideoConfig",method:"get",params:t})}function x(t){return Object(a["a"])({url:"/shortVideo/updateVideoConfig",method:"post",data:t})}}}]);