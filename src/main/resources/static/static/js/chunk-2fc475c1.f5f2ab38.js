(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2fc475c1"],{"333d":function(t,e,n){"use strict";var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pagination-container",class:{hidden:t.hidden}},[n("el-pagination",t._b({attrs:{background:t.background,"current-page":t.currentPage,"page-size":t.pageSize,layout:t.layout,"page-sizes":t.pageSizes,total:t.total},on:{"update:currentPage":function(e){t.currentPage=e},"update:current-page":function(e){t.currentPage=e},"update:pageSize":function(e){t.pageSize=e},"update:page-size":function(e){t.pageSize=e},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}},"el-pagination",t.$attrs,!1))],1)},a=[];n("a9e3");Math.easeInOutQuad=function(t,e,n,i){return t/=i/2,t<1?n/2*t*t+e:(t--,-n/2*(t*(t-2)-1)+e)};var o=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(t){window.setTimeout(t,1e3/60)}}();function s(t){document.documentElement.scrollTop=t,document.body.parentNode.scrollTop=t,document.body.scrollTop=t}function r(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function l(t,e,n){var i=r(),a=t-i,l=20,u=0;e="undefined"===typeof e?500:e;var c=function t(){u+=l;var r=Math.easeInOutQuad(u,i,a,e);s(r),u<e?o(t):n&&"function"===typeof n&&n()};c()}var u={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[20,300,900,3e3]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(t){this.$emit("update:page",t)}},pageSize:{get:function(){return this.limit},set:function(t){this.$emit("update:limit",t)}}},methods:{handleSizeChange:function(t){this.$emit("pagination",{page:this.currentPage,limit:t}),this.autoScroll&&l(0,800)},handleCurrentChange:function(t){this.$emit("pagination",{page:t,limit:this.pageSize}),this.autoScroll&&l(0,800)}}},c=u,d=(n("5b13"),n("2877")),f=Object(d["a"])(c,i,a,!1,null,"02467173",null);e["a"]=f.exports},"5b13":function(t,e,n){"use strict";n("9037")},6724:function(t,e,n){"use strict";n("8d41");var i="@@wavesContext";function a(t,e){function n(n){var i=Object.assign({},e.value),a=Object.assign({ele:t,type:"hit",color:"rgba(0, 0, 0, 0.15)"},i),o=a.ele;if(o){o.style.position="relative",o.style.overflow="hidden";var s=o.getBoundingClientRect(),r=o.querySelector(".waves-ripple");switch(r?r.className="waves-ripple":(r=document.createElement("span"),r.className="waves-ripple",r.style.height=r.style.width=Math.max(s.width,s.height)+"px",o.appendChild(r)),a.type){case"center":r.style.top=s.height/2-r.offsetHeight/2+"px",r.style.left=s.width/2-r.offsetWidth/2+"px";break;default:r.style.top=(n.pageY-s.top-r.offsetHeight/2-document.documentElement.scrollTop||document.body.scrollTop)+"px",r.style.left=(n.pageX-s.left-r.offsetWidth/2-document.documentElement.scrollLeft||document.body.scrollLeft)+"px"}return r.style.backgroundColor=a.color,r.className="waves-ripple z-active",!1}}return t[i]?t[i].removeHandle=n:t[i]={removeHandle:n},n}var o={bind:function(t,e){t.addEventListener("click",a(t,e),!1)},update:function(t,e){t.removeEventListener("click",t[i].removeHandle,!1),t.addEventListener("click",a(t,e),!1)},unbind:function(t){t.removeEventListener("click",t[i].removeHandle,!1),t[i]=null,delete t[i]}},s=function(t){t.directive("waves",o)};window.Vue&&(window.waves=o,Vue.use(s)),o.install=s;e["a"]=o},"8d41":function(t,e,n){},9037:function(t,e,n){},"9f33":function(t,e,n){"use strict";n.d(e,"q",(function(){return a})),n.d(e,"d",(function(){return o})),n.d(e,"u",(function(){return s})),n.d(e,"n",(function(){return r})),n.d(e,"o",(function(){return l})),n.d(e,"l",(function(){return u})),n.d(e,"k",(function(){return c})),n.d(e,"t",(function(){return d})),n.d(e,"g",(function(){return f})),n.d(e,"i",(function(){return p})),n.d(e,"b",(function(){return h})),n.d(e,"f",(function(){return m})),n.d(e,"s",(function(){return g})),n.d(e,"j",(function(){return v})),n.d(e,"h",(function(){return b})),n.d(e,"a",(function(){return y})),n.d(e,"e",(function(){return w})),n.d(e,"r",(function(){return S})),n.d(e,"m",(function(){return _})),n.d(e,"c",(function(){return k})),n.d(e,"p",(function(){return C})),n.d(e,"v",(function(){return V}));var i=n("b775");function a(t){return Object(i["a"])({url:"/shortVideo/getVideoList",method:"get",params:t})}function o(t){return Object(i["a"])({url:"/shortVideo/deleteVideo",method:"post",data:t})}function s(t){return Object(i["a"])({url:"/shortVideo/updateVideo",method:"post",data:t})}function r(t,e){return Object(i["a"])({url:"/shortVideo/getShortVideoUser/"+e,method:"get",params:t})}function l(t){return Object(i["a"])({url:"/shortVideo/getUser/"+t,method:"get"})}function u(t){return Object(i["a"])({url:"/shortVideo/getAuditVideoList",method:"get",params:t})}function c(t){return Object(i["a"])({url:"/shortVideo/getAuditVideo",method:"get",params:t})}function d(t){return Object(i["a"])({url:"/shortVideo/passAuditVideo",method:"post",data:t})}function f(t){return Object(i["a"])({url:"/shortVideo/denyAuditVideo",method:"post",data:t})}function p(t){return Object(i["a"])({url:"/shortVideo/getAuditCommentList",method:"get",params:t})}function h(t){return Object(i["a"])({url:"/shortVideo/deleteAuditComments",method:"post",data:t})}function m(t){return Object(i["a"])({url:"/shortVideo/denyAuditComments",method:"post",data:t})}function g(t){return Object(i["a"])({url:"/shortVideo/passAuditComments",method:"post",data:t})}function v(t,e){return Object(i["a"])({url:"/shortVideo/getAuditCommentListChild/"+e,method:"get",params:t})}function b(t){return Object(i["a"])({url:"/shortVideo/getAuditComment",method:"get",params:t})}function y(t){return Object(i["a"])({url:"/shortVideo/deleteAuditComment",method:"post",data:t})}function w(t){return Object(i["a"])({url:"/shortVideo/denyAuditComment",method:"post",data:t})}function S(t){return Object(i["a"])({url:"/shortVideo/passAuditComment",method:"post",data:t})}function _(t){return Object(i["a"])({url:"/shortVideo/getShareVideo",method:"get",params:t})}function k(t){return Object(i["a"])({url:"/shortVideo/deleteShareVideo",method:"post",data:t})}function C(t){return Object(i["a"])({url:"/shortVideo/getVideoConfig",method:"get",params:t})}function V(t){return Object(i["a"])({url:"/shortVideo/updateVideoConfig",method:"post",data:t})}},af9e:function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-input",{staticClass:"filter-item",staticStyle:{width:"300px"},attrs:{placeholder:"评论关键字",clearable:""},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.handleFilter(e)}},model:{value:t.listQuery.title,callback:function(e){t.$set(t.listQuery,"title",e)},expression:"listQuery.title"}}),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"primary",icon:"el-icon-search"},on:{click:t.handleFilter}},[t._v(" 搜索 ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"danger",icon:"el-icon-delete-solid"},on:{click:t.handleDeleteAll}},[t._v(" 批量删除("+t._s(t.ids.length)+") ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"success",icon:"el-icon-plus"},on:{click:t.handlePassAll}},[t._v(" 批量通过("+t._s(t.ids.length)+") ")]),n("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",staticStyle:{"margin-left":"1vw"},attrs:{type:"info",icon:"el-icon-plus"},on:{click:t.handleDenyAll}},[t._v(" 批量拒绝("+t._s(t.ids.length)+") ")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],key:t.tableKey,staticStyle:{width:"100%"},attrs:{data:t.list,border:"",fit:"","highlight-current-row":""},on:{"selection-change":t.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"ID",prop:"id",sortable:"custom",align:"center",width:"80"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("span",[t._v(t._s(i.id))])]}}])}),n("el-table-column",{attrs:{label:"视频","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("span",{staticClass:"link-type",on:{click:function(e){return t.handleVideo(i)}}},[t._v(t._s(i.title))])]}}])}),n("el-table-column",{attrs:{label:"评论","min-width":"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("router-link",{attrs:{to:"/shortVideo/auditAuditCommentListChild/"+i.id}},[n("span",{staticClass:"link-type"},[t._v(t._s(i.text))])])]}}])}),n("el-table-column",{attrs:{label:"上传者","class-name":"status-col",width:"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("span",{staticClass:"link-type"},[t._v(t._s(i.nickname))])]}}])}),n("el-table-column",{attrs:{label:"上传IP","class-name":"status-col",width:"150px"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("span",[t._v(" "+t._s(i.ip)+" ")])]}}])}),n("el-table-column",{attrs:{label:"点赞","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(i.likes)+" ")])]}}])}),n("el-table-column",{attrs:{label:"待审核回复","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(i.comments)+" ")])]}}])}),n("el-table-column",{attrs:{label:"创建时间",width:"150px",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("span",[t._v(t._s(t._f("parseTime")(i.addTime,"{y}-{m}-{d} {h}:{i}")))])]}}])}),n("el-table-column",{attrs:{label:"用户主页置顶","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("el-switch",{attrs:{"active-value":1,"inactive-value":0},model:{value:i.pin,callback:function(e){t.$set(i,"pin",e)},expression:"row.pin"}})]}}])}),n("el-table-column",{attrs:{label:"状态","class-name":"status-col",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row;return[n("el-tag",{attrs:{type:"info"}},[t._v(" "+t._s(t._f("statusFilter")(i.status))+" ")])]}}])}),n("el-table-column",{attrs:{label:"操作",align:"center",width:"230","class-name":"small-padding fixed-width"},scopedSlots:t._u([{key:"default",fn:function(e){var i=e.row,a=e.$index;return[1!==i.status?n("el-button",{attrs:{type:"info",size:"mini"},on:{click:function(e){return t.handleDeny(i,a)}}},[t._v(" 拒绝 ")]):t._e(),1!==i.status?n("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(e){return t.handlePass(i,a)}}},[t._v(" 通过 ")]):t._e(),n("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(e){return t.handleDelete(i,a)}}},[t._v(" 删除 ")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{total:t.total,page:t.listQuery.page,limit:t.listQuery.limit},on:{"update:page":function(e){return t.$set(t.listQuery,"page",e)},"update:limit":function(e){return t.$set(t.listQuery,"limit",e)},pagination:t.getList}}),n("el-drawer",{attrs:{"before-close":t.drawerClose,size:"36%",visible:t.dialogFormVisible},on:{"update:beforeClose":function(e){t.drawerClose=e},"update:before-close":function(e){t.drawerClose=e},"update:visible":function(e){t.dialogFormVisible=e}}},[n("el-row",{staticStyle:{"text-align-all":"center"}},[n("el-col",{attrs:{span:24}},[n("video",{ref:"video",staticClass:"full-height full-width",attrs:{width:"90%",controls:""}})])],1)],1)],1)},a=[],o=(n("d81d"),n("c740"),n("a434"),n("9f33")),s=n("6724"),r=n("ed08"),l=n("333d"),u=n("ba56"),c=n.n(u),d={name:"ComplexTable",components:{Pagination:l["a"]},directives:{waves:s["a"]},filters:{statusFilter:function(t){switch(t){case 0:return"未审核";case 1:return"审核通过";default:return"审核失败"}}},data:function(){return{tableKey:0,list:null,total:0,listLoading:!0,listQuery:{page:1,limit:20,title:void 0},ids:[],temp:{},statusOptions:["退审","通过"],dialogFormVisible:!1}},created:function(){this.getList()},methods:{drawerClose:function(t){this.$refs.video.pause(),t()},getStream:function(t){var e=this;c.a.isSupported()&&(this.hls=new c.a,this.hls.loadSource(t),this.hls.attachMedia(this.$refs.video),this.hls.on(c.a.Events.MANIFEST_PARSED,(function(){console.log("加载成功"),e.$refs.video.play()})),this.hls.on(c.a.Events.ERROR,(function(t,e){console.log("加载失败")})))},getList:function(){var t=this;this.listLoading=!0,Object(o["i"])(this.listQuery).then((function(e){t.list=e.list,t.total=e.total,setTimeout((function(){t.listLoading=!1}),1500)}))},handleVideo:function(t){var e=this;this.temp=Object.assign({},t),this.dialogFormVisible=!0,this.$nextTick((function(){e.getStream(e.temp.playUrl)}))},handleFilter:function(){this.listQuery.page=1,this.getList()},handleSelectionChange:function(t){this.ids=t.map((function(t){return t.id}))},handleDeleteAll:function(){var t=this;Object(o["b"])({ids:this.ids}).then((function(e){for(var n=function(e){var n=t.list.findIndex((function(n){return n.id===t.ids[e]}));t.list.splice(n,1)},i=0;i<t.ids.length;i++)n(i);t.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3})}))},handleDenyAll:function(){var t=this;Object(o["f"])({ids:this.ids}).then((function(e){for(var n=e.list,i=function(e){var i=t.list.findIndex((function(t){return t.id===n[e].id}));t.list.splice(i,1,n[e])},a=0;a<n.length;a++)i(a);t.$notify({title:"Success",message:"拒绝成功",type:"success",duration:2e3})}))},handlePassAll:function(){var t=this;Object(o["s"])({ids:this.ids}).then((function(e){for(var n=e.list,i=function(e){var i=t.list.findIndex((function(t){return t.id===n[e].id}));t.list.splice(i,1,n[e])},a=0;a<n.length;a++)i(a);t.$notify({title:"Success",message:"通过成功",type:"success",duration:2e3})}))},handleDeny:function(t,e){var n=this,i=[];i.push(t.id),Object(o["f"])({ids:i}).then((function(t){var i=t.list;n.list.splice(e,1,i[0]),n.$notify({title:"Success",message:"拒绝成功",type:"success",duration:2e3})}))},handlePass:function(t,e){var n=this,i=[];i.push(t.id),Object(o["s"])({ids:i}).then((function(t){var i=t.list;n.list.splice(e,1,i[0]),n.$notify({title:"Success",message:"通过成功",type:"success",duration:2e3})}))},handleDelete:function(t,e){var n=this,i=[];i.push(t.id),Object(o["b"])({ids:i}).then((function(t){n.$notify({title:"Success",message:"删除成功",type:"success",duration:2e3}),n.list.splice(e,1)}))},formatJson:function(t){return this.list.map((function(e){return t.map((function(t){return"timestamp"===t?Object(r["d"])(e[t]):e[t]}))}))}}},f=d,p=n("2877"),h=Object(p["a"])(f,i,a,!1,null,null,null);e["default"]=h.exports}}]);