//
//  NTUIControl.h
//  iBank
//
//  Created by qiye on 14/10/31.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTUIControl : CDVPlugin<UIAlertViewDelegate>

- (void)beginLoaded:(CDVInvokedUrlCommand*)command;
- (void)onLoaded:(CDVInvokedUrlCommand*)command;
- (void)dismissLoadingWithTypeAndMessageAndCommand:(CDVInvokedUrlCommand*)command;

/**
 *  页面调用AlertView的响应方法
 *
 *  @param command 包含页面传过来的参数
 */
- (void)openNTMbsDialog:(CDVInvokedUrlCommand*)command;

/**
 *  大图展示
 *
 *  @param command 包含页面传过来的参数
 */
-(void)showBigImage:(CDVInvokedUrlCommand*)command;

@end
