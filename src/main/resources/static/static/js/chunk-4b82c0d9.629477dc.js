(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-4b82c0d9"],{"333d":function(t,e,n){"use strict";var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pagination-container",class:{hidden:t.hidden}},[n("el-pagination",t._b({attrs:{background:t.background,"current-page":t.currentPage,"page-size":t.pageSize,layout:t.layout,"page-sizes":t.pageSizes,total:t.total},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"update:pageSize":function(e){t.pageSize=e},"update:page-size":function(e){t.pageSize=e},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}},"el-pagination",t.$attrs,!1))],1)},r=[];n("a9e3");Math.easeInOutQuad=function(t,e,n,a){return t/=a/2,t<1?n/2*t*t+e:(t--,-n/2*(t*(t-2)-1)+e)};var i=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(t){window.setTimeout(t,1e3/60)}}();function o(t){document.documentElement.scrollTop=t,document.body.parentNode.scrollTop=t,document.body.scrollTop=t}function u(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function l(t,e,n){var a=u(),r=t-a,l=20,s=0;e="undefined"===typeof e?500:e;var c=function t(){s+=l;var u=Math.easeInOutQuad(s,a,r,e);o(u),s<e?i(t):n&&"function"===typeof n&&n()};c()}var s={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[20,300,900,3e3]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(t){this.$emit("update:page",t)}},pageSize:{get:function(){return this.limit},set:function(t){this.$emit("update:limit",t)}}},methods:{handleSizeChange:function(t){this.$emit("pagination",{page:this.currentPage,limit:t}),this.autoScroll&&l(0,800)},handleCurrentChange:function(t){this.$emit("pagination",{page:t,limit:this.pageSize}),this.autoScroll&&l(0,800)}}},c=s,d=(n("5b13"),n("2877")),m=Object(d["a"])(c,a,r,!1,null,"02467173",null);e["a"]=m.exports},"5b13":function(t,e,n){"use strict";n("9037")},6724:function(t,e,n){"use strict";n("8d41");var a="@@wavesContext";function r(t,e){function n(n){var a=Object.assign({},e.value),r=Object.assign({ele:t,type:"hit",color:"rgba(0, 0, 0, 0.15)"},a),i=r.ele;if(i){i.style.position="relative",i.style.overflow="hidden";var o=i.getBoundingClientRect(),u=i.querySelector(".waves-ripple");switch(u?u.className="waves-ripple":(u=document.createElement("span"),u.className="waves-ripple",u.style.height=u.style.width=Math.max(o.width,o.height)+"px",i.appendChild(u)),r.type){case"center":u.style.top=o.height/2-u.offsetHeight/2+"px",u.style.left=o.width/2-u.offsetWidth/2+"px";break;default:u.style.top=(n.pageY-o.top-u.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",u.style.left=(n.pageX-o.left-u.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return u.style.backgroundColor=r.color,u.className="waves-ripple z-active",!1}}return t[a]?t[a].removeHandle=n:t[a]={removeHandle:n},n}var i={bind:function(t,e){t.addEventListener("click",r(t,e),!1)},update:function(t,e){t.removeEventListener("click",t[a].removeHandle,!1),t.addEventListener("click",r(t,e),!1)},unbind:function(t){t.removeEventListener("click",t[a].removeHandle,!1),t[a]=null,delete t[a]}},o=function(t){t.directive("waves",i)};window.Vue&&(window.waves=i,Vue.use(o)),i.install=o;e["a"]=i},"89cb":function(t,e,n){"use strict";n.d(e,"p",(function(){return r})),n.d(e,"g",(function(){return i})),n.d(e,"C",(function(){return o})),n.d(e,"b",(function(){return u})),n.d(e,"n",(function(){return l})),n.d(e,"f",(function(){return s})),n.d(e,"B",(function(){return c})),n.d(e,"a",(function(){return d})),n.d(e,"m",(function(){return m})),n.d(e,"l",(function(){return p})),n.d(e,"o",(function(){return f})),n.d(e,"D",(function(){return g})),n.d(e,"q",(function(){return h})),n.d(e,"h",(function(){return b})),n.d(e,"z",(function(){return v})),n.d(e,"w",(function(){return y})),n.d(e,"I",(function(){return w})),n.d(e,"x",(function(){return O})),n.d(e,"y",(function(){return k})),n.d(e,"A",(function(){return _})),n.d(e,"v",(function(){return j})),n.d(e,"k",(function(){return S})),n.d(e,"H",(function(){return x})),n.d(e,"u",(function(){return C})),n.d(e,"t",(function(){return G})),n.d(e,"G",(function(){return $})),n.d(e,"r",(function(){return z})),n.d(e,"E",(function(){return L})),n.d(e,"i",(function(){return F})),n.d(e,"c",(function(){return T})),n.d(e,"s",(function(){return Q})),n.d(e,"F",(function(){return P})),n.d(e,"j",(function(){return E})),n.d(e,"d",(function(){return W})),n.d(e,"e",(function(){return N}));var a=n("b775");function r(t){return Object(a["a"])({url:"/game/getGameList",method:"get",params:t})}function i(t){return Object(a["a"])({url:"/game/deleteGame",method:"post",data:t})}function o(t){return Object(a["a"])({url:"/game/updateGame",method:"post",data:t})}function u(t){return Object(a["a"])({url:"/game/addGame",method:"post",data:t})}function l(t){return Object(a["a"])({url:"/game/getButtonList",method:"get",params:t})}function s(t){return Object(a["a"])({url:"/game/deleteButton",method:"post",data:t})}function c(t){return Object(a["a"])({url:"/game/updateButton",method:"post",data:t})}function d(t){return Object(a["a"])({url:"/game/addButton",method:"post",data:t})}function m(t){return Object(a["a"])({url:"/game/getButtonConfigList",method:"get",params:t})}function p(t){return Object(a["a"])({url:"/game/getButtonConfig/"+t,method:"get"})}function f(t){return Object(a["a"])({url:"/game/getGameConfig",method:"get",params:t})}function g(t){return Object(a["a"])({url:"/game/updateGameConfig",method:"post",data:t})}function h(t){return Object(a["a"])({url:"/game/getGameOrderList",method:"get",params:t})}function b(t){return Object(a["a"])({url:"/game/deleteGameOrder",method:"post",data:t})}function v(t){return Object(a["a"])({url:"/game/makeupGameOrder",method:"post",data:t})}function y(t){return Object(a["a"])({url:"/game/getGameWithdrawConfig",method:"get",params:t})}function w(t){return Object(a["a"])({url:"/game/updateGameWithdrawConfig",method:"post",data:t})}function O(t){return Object(a["a"])({url:"/game/getGameWithdrawOrder",method:"get",params:t})}function k(t){return Object(a["a"])({url:"/game/makeDownGameWithdrawOrder",method:"post",data:t})}function _(t){return Object(a["a"])({url:"/game/makeupGameWithdrawOrder",method:"post",data:t})}function j(t){return Object(a["a"])({url:"/game/getGameWithdrawCard",method:"get",params:t})}function S(t){return Object(a["a"])({url:"/game/deleteGameWithdrawCard",method:"post",data:t})}function x(t){return Object(a["a"])({url:"/game/updateGameWithdrawCard",method:"post",data:t})}function C(t){return Object(a["a"])({url:"/game/getGameWaterList",method:"get",params:t})}function G(t){return Object(a["a"])({url:"/game/getGameWater",method:"get",params:t})}function $(t){return Object(a["a"])({url:"/game/updateGameWater",method:"get",params:t})}function z(t){return Object(a["a"])({url:"/game/getGamePublicity",method:"get",params:t})}function L(t){return Object(a["a"])({url:"/game/updateGamePublicity",method:"post",data:t})}function F(t){return Object(a["a"])({url:"/game/deleteGamePublicity",method:"post",data:t})}function T(t){return Object(a["a"])({url:"/game/addGamePublicity",method:"post",data:t})}function Q(t){return Object(a["a"])({url:"/game/getGameScroll",method:"get",params:t})}function P(t){return Object(a["a"])({url:"/game/updateGameScroll",method:"post",data:t})}function E(t){return Object(a["a"])({url:"/game/deleteGameScroll",method:"post",data:t})}function W(t){return Object(a["a"])({url:"/game/addGameScroll",method:"post",data:t})}function N(t){return Object(a["a"])({url:"/game/automaticGameScroll",method:"post",data:t})}},"8c23":function(t,e,n){"use strict";n.r(e);var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-input",{staticClass:"filter-item",staticStyle:{width:"300px"},attrs:{placeholder:t.listQuery.enabled?"用户ID":"用户手机号",clearable:""},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.handleFilter(e)}},model:{value:t.listQuery.title,callback:function(e){t.$set(t.listQuery,"title",e)},expression:"listQuery.title"}}),n("el-checkbox",{staticClass:"filter-item",staticStyle:{"margin-left":"15px"},model:{value:t.listQuery.enabled,callback:function(e){t.$set(t.listQuery,"enabled",e)},expression:"listQuery.enabled"}},[t._v(" 查找用户ID ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"primary",icon:"el-icon-search"},on:{click:t.handleFilter}},[t._v(" 搜索 ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"danger",icon:"el-icon-delete-solid"},on:{click:t.handleDeleteAll}},[t._v(" 批量删除("+t._s(t.ids.length)+") ")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],key:t.tableKey,staticStyle:{width:"100%"},attrs:{data:t.list,border:"",fit:"","highlight-current-row":""},on:{"selection-change":t.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"ID",prop:"id",sortable:"custom",align:"center",width:"80"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.id))])]}}])}),n("el-table-column",{attrs:{label:"姓名","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"success"}},[t._v(" "+t._s(a.name)+" ")])]}}])}),n("el-table-column",{attrs:{label:"卡号","class-name":"status-col","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.card))])]}}])}),n("el-table-column",{attrs:{label:"银行","class-name":"status-col",width:"150"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("el-tag",{attrs:{type:"success"}},[t._v(" "+t._s(a.bank)+" ")])]}}])}),n("el-table-column",{attrs:{label:"开户行","class-name":"status-col","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.address))])]}}])}),n("el-table-column",{attrs:{label:"用户","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.user))])]}}])}),n("el-table-column",{attrs:{label:"手机号","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.phone))])]}}])}),n("el-table-column",{attrs:{label:"添加IP","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.addIp))])]}}])}),n("el-table-column",{attrs:{label:"更新IP","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(a.updateIp))])]}}])}),n("el-table-column",{attrs:{label:"添加时间",width:"150px",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(t._f("parseTime")(a.addTime,"{y}-{m}-{d} {h}:{i}")))])]}}])}),n("el-table-column",{attrs:{label:"更新时间",width:"150px",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[n("span",[t._v(t._s(t._f("parseTime")(a.updateTime,"{y}-{m}-{d} {h}:{i}")))])]}}])}),n("el-table-column",{attrs:{label:"操作",align:"center",width:"230","class-name":"small-padding fixed-width"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row,r=e.$index;return[n("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(e){return t.handleUpdate(a)}}},[t._v(" 编辑 ")]),n("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(e){return t.handleDelete(a,r)}}},[t._v(" 删除 ")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{total:t.total,page:t.listQuery.page,limit:t.listQuery.limit},on:{"update:page":function(e){return t.$set(t.listQuery,"page",e)},"update:limit":function(e){return t.$set(t.listQuery,"limit",e)},pagination:t.getList}}),n("el-drawer",{attrs:{size:"40%",visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"90%","margin-left":"50px"},attrs:{model:t.temp,"label-position":"left"}},[n("el-form-item",{attrs:{label:"姓名"}},[n("el-input",{attrs:{type:"text"},model:{value:t.temp.name,callback:function(e){t.$set(t.temp,"name",e)},expression:"temp.name"}})],1),n("el-form-item",{attrs:{label:"银行"}},[n("el-input",{attrs:{type:"text"},model:{value:t.temp.bank,callback:function(e){t.$set(t.temp,"bank",e)},expression:"temp.bank"}})],1),n("el-form-item",{attrs:{label:"卡号"}},[n("el-input",{attrs:{type:"number"},model:{value:t.temp.card,callback:function(e){t.$set(t.temp,"card",e)},expression:"temp.card"}})],1),n("el-form-item",{attrs:{label:"开户行"}},[n("el-input",{attrs:{type:"text"},model:{value:t.temp.address,callback:function(e){t.$set(t.temp,"address",e)},expression:"temp.address"}})],1),n("el-button",{on:{click:function(e){t.dialogFormVisible=!1}}},[t._v(" 取消 ")]),n("el-button",{attrs:{type:"primary"},on:{click:function(e){return t.updateData()}}},[t._v(" 确认 ")])],1)],1)],1)},r=[],i=(n("d81d"),n("c740"),n("a434"),n("89cb")),o=n("6724"),u=n("ed08"),l=n("333d"),s={name:"ComplexTable",components:{Pagination:l["a"]},directives:{waves:o["a"]},filters:{statusFilter:function(t){switch(t){case 0:return"未上线";case 1:return"已上线";default:return"已删除"}}},data:function(){return{tableKey:0,list:null,total:0,listLoading:!0,listQuery:{page:1,limit:20,enabled:!1,title:void 0},ids:[],temp:{},statusOptions:["下线游戏","上线游戏"],dialogFormVisible:!1,dialogStatus:"create"}},created:function(){this.getList()},methods:{getList:function(){var t=this;this.listLoading=!0,Object(i["v"])(this.listQuery).then((function(e){t.list=e.list,t.total=e.total,setTimeout((function(){t.listLoading=!1}),1500)}))},handleFilter:function(){this.listQuery.page=1,this.getList()},handleSelectionChange:function(t){this.ids=t.map((function(t){return t.id}))},handleUser:function(t){console.log(t)},resetTemp:function(){this.temp={id:void 0,status:1}},handleUpdate:function(t){var e=this;this.temp=Object.assign({},t),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},updateData:function(){var t=this;this.$refs["dataForm"].validate((function(e){if(e){var n=Object.assign({},t.temp);Object(i["H"])(n).then((function(e){var n=t.list.findIndex((function(e){return e.id===t.temp.id}));t.list.splice(n,1,e),t.dialogFormVisible=!1,t.$notify({title:"Success",message:"更新成功",type:"success",duration:2e3})}))}}))},handleDeleteAll:function(){var t=this;Object(i["k"])({ids:this.ids}).then((function(e){t.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3}),t.getList()}))},handleDelete:function(t,e){var n=this,a=[];a.push(t.id),Object(i["k"])({ids:a}).then((function(t){n.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3}),n.list.splice(e,1)}))},formatJson:function(t){return this.list.map((function(e){return t.map((function(t){return"timestamp"===t?Object(u["d"])(e[t]):e[t]}))}))}}},c=s,d=n("2877"),m=Object(d["a"])(c,a,r,!1,null,null,null);e["default"]=m.exports},"8d41":function(t,e,n){},9037:function(t,e,n){}}]);