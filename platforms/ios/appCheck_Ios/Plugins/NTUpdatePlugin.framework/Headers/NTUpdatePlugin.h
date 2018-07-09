
//
//  NTUpdatePlugin.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/12/10.
//
//

#import <Cordova/CDVPlugin.h>

@interface NTUpdatePlugin: CDVPlugin

- (void)update:(CDVInvokedUrlCommand *)command;

- (void)getVersion:(CDVInvokedUrlCommand *)command;

@end
