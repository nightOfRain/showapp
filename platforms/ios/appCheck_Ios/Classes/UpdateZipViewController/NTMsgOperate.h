//
//  NTMsgOperate.h
//  MobileMoniter
//
//  Created by guxiuhu on 15/8/13.
//  Copyright (c) 2015年 nantian. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NTMsgOperate : NSObject

/**
 *  初始化方法
 *
 *  @return 该类的实例
 */
-(id)init;

/**
 *  插入用户通知
 *
 *  @param sysmsgAry 数组
 */
-(void)insertUsermsgWithAry:(NSArray*)sysmsgAry;

/**
 *  插入系统通知
 *
 *  @param sysmsgAry 数组
 */
-(void)insertSysmsgWithAry:(NSArray*)sysmsgAry;

@end
