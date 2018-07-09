//
//  PublicMethods.h
//  MobileMoniter
//
//  Created by yongkun on 15/5/27.
//  Copyright (c) 2015年 nantian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AFNetworking.h"

@class MBProgressHUD;

@interface PublicMethods : NSObject

#pragma mark - 去除tableview多余行
/**
 *  去除tableview多余行
 *
 *  @param tableView tableView实例
 */
+(void)setExtraCellLineHidden:(UITableView *)tableView;

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
+ (CGFloat)heightWithString:(NSString *)string andWidth:(CGFloat)width andTextFont:(UIFont*)font;

#pragma mark - 计算指定字符串宽度
/**
 *  计算指定字符串宽度
 *
 *  @param string 字符串
 *  @param font   字号
 *
 *  @return 宽度
 */
+ (CGFloat)widthWithString:(NSString *)string andTextFont:(UIFont*)font;

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
+(void)sendPostWithURL:(NSString*)urlStr andParameters:(id)parameters andSuccessBlock:(void (^)(id responsObject))successBlock andFaildBlock:(void (^)(NSString *errorDes))faildBlock andTimeoutBlock:(void (^)())timeoutBlock;
/**
 *  通讯组件
 *
 *  @param urlStr       服务器url
 *  @param parameters   传入的参数字典
 *  @param successBlock 成功的回调
 *  @param faildBlock   失败的回调
 *  @param timeoutBlock 超时的回调
 */
+(void)sendAFNetworkingPostWithURL:(NSString*)urlStr andParameters:(id)parameters andSuccessBlock:(void (^)(id responsObject))successBlock andFaildBlock:(void (^)(NSString *errorDes))faildBlock andTimeoutBlock:(void (^)())timeoutBlock;

#pragma mark - 用颜色来生成图片
/**
 *  用颜色来生成图片
 *
 *  @param color        颜色
 *  @param size         图片大小
 *  @param cornerRadius 圆角
 *
 *  @return UIImage
 */
+ (UIImage *) ht_imageWithColor:(UIColor *)color size:(CGSize)size cornerRadius: (CGFloat) cornerRadius;

#pragma mark - 获取Document目录
/**
 *  获取Document目录
 *
 *  @return Document目录路径
 */
+ (NSString *)getDirectoryOfDocumentFolder;

/**
 *  将Dictionary转换成字符串
 *
 *  @param dict 要转换的Dic
 *
 *  @return Dic的字符串
 */
+ (NSString*) convertDictionaryToString:(NSMutableDictionary*) dict;

/**
 *  去除字符串空格
 *
 *  @param str 要处理的字符串
 *
 *  @return 去除了空格的字符串
 */
+ (NSString *)trim:(NSString *)str;

/**
 *  晃动某一个View
 *
 *  @param view 要晃动的View
 */
+ (void)shakeAnimationForView:(UIView *) view;

#pragma mark - NSUserDefaults
/**
 *  从NSUserDefaults中根据
 *
 *  @param key 键
 *
 *  @return 对象
 */
+(id)getObjFromUserdefaultsWithKey:(NSString*)key;

/**
 *  保存值到NSUserDefaults中
 *
 *  @param key 键
 *  @param obj 值
 */
+(void)saveToUserdefaultsWithKey:(NSString*)key andObj:(id)obj;

/**
 *  从NSUserDefaults中获取bool值
 *
 *  @param key 键
 *
 *  @return 对象
 */
+(BOOL)getBoolFromUserdefaultsWithKey:(NSString*)key;

/**
 *  保存bool值到NSUserDefaults中
 *
 *  @param key 键
 *  @param boolValue bool值
 */
+(void)saveToUserdefaultsWithKey:(NSString*)key andBool:(BOOL)boolValue;

/**
 *  从NSUserDefaults中去除值
 *
 *  @param key 键
 */
+(void)removeValueFromUserdefaultsWithKey:(NSString*)key;

@end
