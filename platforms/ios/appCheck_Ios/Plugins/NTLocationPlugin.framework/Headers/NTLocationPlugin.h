//
//  NTLocationPlugin.h
//  iBank
//
//  Created by 林色琦 on 16/2/26.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTLocationPlugin : CDVPlugin

/**
 *  定位
 *
 */
- (void)getLocation:(CDVInvokedUrlCommand *)command;

@end
