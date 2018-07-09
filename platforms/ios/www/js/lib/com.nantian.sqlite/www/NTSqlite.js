cordova.define("com.nantian.sqlite", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  执行sql语句
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *  @param sql             sql语句
                *  @param option          可选参数，0:查询，1:更改，如增／删／改
                *
                */
               execSql : function(successCallback,failCallback,sql,option) {
               
            	   exec(successCallback, failCallback, "NTSqlite", "execSql", [sql,option]);
               },
               
               selectAllProvince : function(successCallback,failCallback) {
               
               exec(successCallback, failCallback, "NTCitySqlite", "selectAllProvince", []);
               },
               
               selectAllCityFrom : function(successCallback,failCallback,province_id) {
               
               exec(successCallback, failCallback, "NTCitySqlite", "selectAllCityFrom", [province_id]);
               }
               
               }});
