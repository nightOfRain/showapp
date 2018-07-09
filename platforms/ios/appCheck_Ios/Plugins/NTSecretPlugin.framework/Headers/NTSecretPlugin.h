//
//  NTSecretPlugin.h
//  iBank
//
//  Created by 古秀湖 on 15/9/22.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTSecretPlugin : CDVPlugin

/**
 *  加密
 *
 *  @param successCallback 成功的回调，返回加密后的密文
 *  @param failCallback    失败的回调，返回失败原因
 *	@param type 加密的类型（rsa/3des）
 *	@param text 需要加密的明文
 */
- (void)encrypt:(CDVInvokedUrlCommand *)command;

/**
 *  解密
 *
 *  @param successCallback 成功的回调，返回解密后的明文
 *  @param failCallback    失败的回调，返回失败原因
 *	@param type 解密的类型（rsa/3des）
 *	@param text 需要解密的密文
 */
- (void)decrypt:(CDVInvokedUrlCommand *)command;


@end
