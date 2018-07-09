//
//  PublicMethods.m
//  MobileMoniter
//
//  Created by yongkun on 15/5/27.
//  Copyright (c) 2015年 nantian. All rights reserved.
//

#import "PublicMethods.h"
#import "AFNetworking.h"

@implementation PublicMethods

#pragma mark - 去除tableview多余行
/**
 *  去除tableview多余行
 *
 *  @param tableView tableView实例
 */
+(void)setExtraCellLineHidden:(UITableView *)tableView{
    
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    [tableView setTableFooterView:view];
}

#pragma mark - 计算指定字符串在指定宽度的前提下的高度
/**
 *  计算指定字符串在指定宽度的前提下的高度
 *
 *  @param string 字符串
 *  @param width  指定宽度
 *  @param font   字号
 *
 *  @return 高度
 */
+ (CGFloat)heightWithString:(NSString *)string andWidth:(CGFloat)width andTextFont:(UIFont*)font{
    
    CGRect rect = [string boundingRectWithSize:CGSizeMake(width,MAXFLOAT)//限制最大的宽度和高度
                                       options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading  |NSStringDrawingUsesLineFragmentOrigin//采用换行模式
                                    attributes:@{NSFontAttributeName:font}//传人的字体字典
                                       context:nil];
    
    return rect.size.height;
}

#pragma mark - 计算指定字符串宽度
/**
 *  计算指定字符串宽度
 *
 *  @param string 字符串
 *  @param font   字号
 *
 *  @return 宽度
 */
+ (CGFloat)widthWithString:(NSString *)string andTextFont:(UIFont*)font{
    
    CGRect rect = [string boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT)
                                       options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading  |NSStringDrawingUsesLineFragmentOrigin//采用换行模式
                                    attributes:@{NSFontAttributeName:font}//传人的字体字典
                                       context:nil];
    
    return rect.size.width;
}

#pragma mark - 通讯组件

/**
 *  通讯组件
 *
 *  @param urlStr       服务器url
 *  @param parameters   传入的参数字典
 *  @param successBlock 成功的回调
 *  @param faildBlock   失败的回调
 *  @param timeoutBlock 超时的回调
 */
+(void)sendAFNetworkingPostWithURL:(NSString*)urlStr andParameters:(id)parameters andSuccessBlock:(void (^)(id responsObject))successBlock andFaildBlock:(void (^)(NSString *errorDes))faildBlock andTimeoutBlock:(void (^)())timeoutBlock{

    AFHTTPRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    requestSerializer.timeoutInterval = 30;
    NSMutableURLRequest *request = [requestSerializer requestWithMethod:@"POST" URLString:urlStr parameters:parameters error:nil];
    
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    AFHTTPRequestOperation *op = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    op.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [op setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        if (operation.response.statusCode == 200) {
            NSLog(@"通讯成功");
            successBlock(responseObject);

        }else{
            faildBlock(@"通讯失败");
            NSLog(@"通讯不成功");

        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        if (error.code == -1001) {
            NSLog(@"timeout");
            timeoutBlock();
        }else{
            NSLog(@"通讯失败%@",operation.responseString);

            faildBlock(error.description);
        }
    }];
    
    [[NSOperationQueue mainQueue] addOperation:op];

}
/**
 *  通讯组件
 *
 *  @param urlStr       服务器url
 *  @param parameters   传入的参数字典
 *  @param successBlock 成功的回调
 *  @param faildBlock   失败的回调
 *  @param timeoutBlock 超时的回调
 */
+(void)sendPostWithURL:(NSString*)urlStr andParameters:(id)parameters andSuccessBlock:(void (^)(id responsObject))successBlock andFaildBlock:(void (^)(NSString *errorDes))faildBlock andTimeoutBlock:(void (^)())timeoutBlock{
    
    urlStr = [urlStr stringByAddingPercentEscapesUsingEncoding: NSUTF8StringEncoding];


    NSMutableURLRequest*  request= [NSMutableURLRequest new];
    request.timeoutInterval = 30.0f;
    
    [request setURL:[NSURL URLWithString:urlStr]];
    [request  setHTTPMethod:@"POST"];
    [request addValue:@"application/json"forHTTPHeaderField:@"Content-Type"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    [request setHTTPBody:[[PublicMethods convertDictionaryToString:parameters] dataUsingEncoding:NSUTF8StringEncoding]];
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        NSURLResponse *response;
        NSError *error = nil;
        NSData* data=  [NSURLConnection sendSynchronousRequest:request
                                             returningResponse:&response error:&error];
        dispatch_async(dispatch_get_main_queue(), ^{
            if (error) {
                if (error.code == -1001) {
                    timeoutBlock();
                }else{
                    if (data.length > 0) {
                        NSError *jsonError = nil;
                        id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&jsonError];
                        
                        if ([jsonObject isKindOfClass:[NSDictionary class]] && [jsonObject objectForKey:@"tips"] && jsonObject != nil) {
                            
                            NSDictionary *tipsDic = [jsonObject objectForKey:@"tips"];
                            
                            if ([tipsDic objectForKey:@"message"] && [tipsDic objectForKey:@"message"] != [NSNull null]) {
                                faildBlock([tipsDic objectForKey:@"message"]);
                            }
                            
                        }else{
                            
                            faildBlock(@"通讯错误");
                        }
                    }else{
                        
                        faildBlock(@"通讯错误");
                    }
                }
                
            }else{
                
                NSInteger responseCode = [(NSHTTPURLResponse *)response statusCode];
                
                switch (responseCode) {
                    case 200:
                    {
                        NSError *jsonError = nil;
                        id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&jsonError];
                        
                        if ([jsonObject isKindOfClass:[NSDictionary class]] && jsonObject != nil) {
                            
                            successBlock(jsonObject);
                            
                        }else{
                            
                            faildBlock(@"发生未知错误");
                        }

                        break;
                    }
                    default:{
                        //提示通讯错误
                        if (data.length > 0) {
                            NSError *jsonError = nil;
                            id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&jsonError];
                            
                            if ([jsonObject isKindOfClass:[NSDictionary class]] && [jsonObject objectForKey:@"tips"] && jsonObject != nil) {
                                
                                NSDictionary *tipsDic = [jsonObject objectForKey:@"tips"];
                                
                                if ([tipsDic objectForKey:@"message"] && [tipsDic objectForKey:@"message"] != [NSNull null]) {
                                    faildBlock([tipsDic objectForKey:@"message"]);
                                }

                            }else{
                                
                                faildBlock(@"通讯错误");
                            }
                        }else{
                            
                            faildBlock(@"通讯错误");
                        }
                        
                        break;
                    }
                }

            }
            
        });
        
    });

}

/**
 *  将Dictionary转换成字符串
 *
 *  @param dict 要转换的Dic
 *
 *  @return Dic的字符串
 */
+ (NSString*) convertDictionaryToString:(NSMutableDictionary*) dict{
    
    if (!dict) {
        return @"";
    }
    
    NSError* error;
    NSDictionary* tempDict = [dict copy]; // get Dictionary from mutable Dictionary
    //giving error as it takes dic, array,etc only. not custom object.
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:tempDict
                                                       options:NSJSONWritingPrettyPrinted error:&error];
    NSString* nsJson=  [[NSString alloc] initWithData:jsonData
                                             encoding:NSUTF8StringEncoding];
    return nsJson;
}


/**
 *  用颜色来生成图片
 *
 *  @param color        颜色
 *  @param size         图片大小
 *  @param cornerRadius 圆角
 *
 *  @return UIImage
 */
+ (UIImage *) ht_imageWithColor:(UIColor *)color size:(CGSize)size cornerRadius: (CGFloat) cornerRadius{
    
    //Draw the image according to the size and color
    CGRect rect = CGRectMake(0, 0, size.width, size.height);
    UIGraphicsBeginImageContextWithOptions(rect.size, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    //Round the image above according to cornerRadius
    UIImageView *imageView = [[UIImageView alloc] initWithImage:image];
    
    UIGraphicsBeginImageContextWithOptions(imageView.bounds.size, NO, 0);
    [[UIBezierPath bezierPathWithRoundedRect:imageView.bounds
                                cornerRadius:cornerRadius] addClip];
    [image drawInRect:imageView.bounds];
    imageView.image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return imageView.image;
}

/**
 *  获取Document目录
 *
 *  @return Document目录路径
 */
+ (NSString *)getDirectoryOfDocumentFolder {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES); // 获取所有Document文件夹路径
    NSString *documentsPath = paths[0]; // 搜索目标文件所在Document文件夹的路径，通常为第一个
    
    if (!documentsPath) {
        NSLog(@"Documents目录不存在");
    }
    
    return documentsPath;
}

/**
 *  去除字符串空格
 *
 *  @param str 要处理的字符串
 *
 *  @return 去除了空格的字符串
 */
+ (NSString *)trim:(NSString *)str{
    
    NSString *resultStr = [str stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    return resultStr;
}

/**
 *  晃动某一个View
 *
 *  @param view 要晃动的View
 */
+ (void)shakeAnimationForView:(UIView *) view{
    
    // 获取到当前的View
    CALayer *viewLayer = view.layer;
    
    // 获取当前View的位置
    CGPoint position = viewLayer.position;
    
    // 移动的两个终点位置
    CGPoint x = CGPointMake(position.x + 10, position.y);
    CGPoint y = CGPointMake(position.x - 10, position.y);
    
    // 设置动画
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"position"];
    
    // 设置运动形式
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault]];
    
    // 设置开始位置
    [animation setFromValue:[NSValue valueWithCGPoint:x]];
    
    // 设置结束位置
    [animation setToValue:[NSValue valueWithCGPoint:y]];
    
    // 设置自动反转
    [animation setAutoreverses:YES];
    
    // 设置时间
    [animation setDuration:.06];
    
    // 设置次数
    [animation setRepeatCount:3];
    
    // 添加上动画
    [viewLayer addAnimation:animation forKey:nil];
}

#pragma mark - NSUserDefaults
/**
 *  从NSUserDefaults中获取值
 *
 *  @param key 键
 *
 *  @return 对象
 */
+(id)getObjFromUserdefaultsWithKey:(NSString*)key{
    
    return [[NSUserDefaults standardUserDefaults] objectForKey:key];
}

/**
 *  保存值到NSUserDefaults中
 *
 *  @param key 键
 *  @param obj 值
 */
+(void)saveToUserdefaultsWithKey:(NSString*)key andObj:(id)obj{
    
    [[NSUserDefaults standardUserDefaults]setObject:obj forKey:key];
    [[NSUserDefaults standardUserDefaults]synchronize];
}

/**
 *  从NSUserDefaults中获取bool值
 *
 *  @param key 键
 *
 *  @return 对象
 */
+(BOOL)getBoolFromUserdefaultsWithKey:(NSString*)key{
    
    return [[NSUserDefaults standardUserDefaults] boolForKey:key];
}

/**
 *  保存bool值到NSUserDefaults中
 *
 *  @param key 键
 *  @param boolValue bool值
 */
+(void)saveToUserdefaultsWithKey:(NSString*)key andBool:(BOOL)boolValue{
    
    [[NSUserDefaults standardUserDefaults]setBool:boolValue forKey:key];
    [[NSUserDefaults standardUserDefaults]synchronize];
}

/**
 *  从NSUserDefaults中去除值
 *
 *  @param key 键
 */
+(void)removeValueFromUserdefaultsWithKey:(NSString*)key{
    
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:key];
    [[NSUserDefaults standardUserDefaults]synchronize];
}

- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
    return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust];
}

- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
    if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
        //if ([trustedHosts containsObject:challenge.protectionSpace.host])
        [challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust]
             forAuthenticationChallenge:challenge];
    
    [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
}
@end
