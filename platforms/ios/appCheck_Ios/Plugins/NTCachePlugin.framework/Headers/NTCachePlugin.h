//
//  NTCachePlugin.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/12/2.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTCachePlugin : CDVPlugin

/**
 * 保存信息
 *
 *  @param command 页面传来的参数
 */
- (void)save:(CDVInvokedUrlCommand *)command;

/**
 * 通过key获取之前保存的信息
 *
 *  @param command 页面传来的参数
 */
- (void)get:(CDVInvokedUrlCommand *)command;

@end
