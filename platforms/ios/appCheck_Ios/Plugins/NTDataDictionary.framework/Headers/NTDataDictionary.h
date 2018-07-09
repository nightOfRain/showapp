//
//  NTDataDictionary.h
//  iBank
//
//  Created by qiye on 14-10-10.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTDataDictionary : CDVPlugin

/**
 *  格式为取出key为@“key”做为键，@“value"做为值，保存到数据词典,如果已经存在，则更新，并且返回成功。
 *
 *  @param command 可以从中取出js中定义的参数
 */
- (void)addValue:(CDVInvokedUrlCommand *)command;

/**
 *  格式为取出key为@“key”做为键，从数据词典删除信息,如果信息不存在，返回删除失败。
 *
 *  @param command 可以从中取出js中定义的参数
 */
- (void)delValue:(CDVInvokedUrlCommand *)command;

/**
 *  格式为取出key为@“key”做为键，从数据词典读取信息,如果信息不存在，返回空。
 *
 *  @param command 可以从中取出js中定义的参数
 */
- (void)getValue:(CDVInvokedUrlCommand *)command;

//清空数据词典，请慎用！！！
- (void)deleteAll:(CDVInvokedUrlCommand *)command;

@end
