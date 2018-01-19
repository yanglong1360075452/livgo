/**
 * Created by Administrator on 2017/4/18.
 */
/**实用工具*/

const util = function ( win ) {
    /**
     * 定义一系列变量
     * */
    var util = {};

    /**格式化时间*/
    util.dateFormat = function(fmt) {
        var time = new Date();
        var o = {
            "M+" : time.getMonth()+1,                 //月份
            "d+" : time.getDate(),                    //日
            "h+" : time.getHours(),                   //小时
            "m+" : time.getMinutes(),                 //分
            "s+" : time.getSeconds(),                 //秒
            "q+" : Math.floor((time.getMonth()+3)/3), //季度
            "S"  : time.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (time.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
        return fmt;
    };

    util.msToDate = function (val) {
      if(val > 0){
        var date = new Date(val);
        var Y = date.getFullYear(),
          m = date.getMonth() + 1,
          d = date.getDate(),
          H = date.getHours(),
          i = date.getMinutes(),
          s = date.getSeconds();
        if (m < 10) {
          m = '0' + m;
        }
        if (d < 10) {
          d = '0' + d;
        }
        if (H < 10) {
          H = '0' + H;
        }
        if (i < 10) {
          i = '0' + i;
        }
        if (s < 10) {
          s = '0' + s;
        }
        var t = Y + '-' + m + '-' + d + '  ' + H + ':'+i+':'+s;
        return t;
      }else {
        return;
      }
    };

  util.dateToMs = function (date) {
    if(date != null){
      return new Date(date).getTime();
    }else {
      return;
    }
  }

    /**临时数据存储到sessionStorage中*/
    util.dataToSessionStorageOperate = {
        /**存储*/
        save ( data_name, data_value ) {
          console.log("ddds");
            if( data_name && data_value )
                sessionStorage.setItem( data_name, JSON.stringify(data_value) );
        },
        /**取出*/
        achieve ( data_name ) {
            if( !data_name ) return;
            var data_value = sessionStorage.getItem( data_name );
            data_value && ( data_value = JSON.parse( data_value ) );
            return data_value;
        },
        /**删除*/
        remove ( data_name ) {
            if( data_name )
                sessionStorage.removeItem( data_name );
        },
        /**清空*/
        clear () {
            sessionStorage.clear();
        }
    };

    /**跳转页面*/
    util.jumpPage = function ( jumpUrl ) {
      console.log(jumpUrl);
      console.log(win.location.pathname);
      if(typeof jumpUrl == 'undefined') win.location.href = win.location.origin + '/#/abnormal';
      else if( jumpUrl.indexOf('http') == -1)
        win.location.href = win.location.origin  + '/#/' + jumpUrl;
      else
        win.location.href = jumpUrl;
    };
    return util;
} ( window );
export default util;
