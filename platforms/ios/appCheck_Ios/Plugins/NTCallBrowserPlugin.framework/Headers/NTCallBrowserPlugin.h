//
//  NTCallBrowserPlugin.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/12/2.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTCallBrowserPlugin : CDVPlugin

/**
 *  打开浏览器
 *
 *  @param command 页面传来的参数
 */
- (void)callBrowser:(CDVInvokedUrlCommand *)command;

@end
