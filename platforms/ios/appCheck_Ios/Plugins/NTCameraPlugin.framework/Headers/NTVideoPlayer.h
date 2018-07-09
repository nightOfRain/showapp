/*********************************************************************
 * 版权所有 (C)2013 南天电脑有限公司
 *
 * 文件名称：  NTVideoPlayer.h
 * 内容摘要：  给页面使用的播放视频插件
 * 其它说明：
 * 作   者：  古秀湖
 * 完成日期：  2015年02月06日
 **********************************************************************/

#import "NTCameraPlugin.h"

@interface NTVideoPlayer : NTCameraPlugin

/**
 *  页面调用扫描身份证插件的响应方法，弹出一个扫描框
 *
 *  @param command 包含页面传过来的参数
 */
-(void)playVideo:(CDVInvokedUrlCommand *)command;

@end
