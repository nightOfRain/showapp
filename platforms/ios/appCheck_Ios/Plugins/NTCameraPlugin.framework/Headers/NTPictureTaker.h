/*********************************************************************
 * 版权所有 (C)2013 南天电脑有限公司
 *
 * 文件名称：  NTPictureTaker.h
 * 内容摘要：  给页面使用的拍照插件
 * 其它说明：
 * 作   者：  古秀湖
 * 完成日期：  2014年11月25日
 **********************************************************************/

#import "NTCameraPlugin.h"

@interface NTPictureTaker : NTCameraPlugin

/**
 *  页面调用拍照插件的响应方法，弹出一个拍照框
 *
 *  @param command 包含页面传过来的参数
 */
- (void)showPictureTaker:(CDVInvokedUrlCommand *)command;

- (void)showImageSwitcher:(CDVInvokedUrlCommand *)command;

@end
