//
//  NTMsgOperate.m
//  MobileMoniter
//
//  Created by guxiuhu on 15/8/13.
//  Copyright (c) 2015年 nantian. All rights reserved.
//

#import "NTMsgOperate.h"
#import "FMDatabase.h"
#import "PublicMethods.h"
#import "NSDictionary+Verified.h"

static NSString *MSG_TABLE_NAME = @"t_msg";//数据表名

static NSString *MSG_ID         = @"message_id";
static NSString *MSG_ACCOUNT_ID         = @"account_id";
static NSString *MSG_ADD_TIME       = @"addtime";
static NSString *MSG_AUTHOR       = @"author";
static NSString *MSG_CREATETIME    = @"createtime";
static NSString *MSG_ENDTIME     = @"endtime";
static NSString *MSG_IS_ALERT  = @"isalert";
static NSString *MSG_CONTEXT   = @"message_context";

static NSString *MSG_TITLE       = @"message_title";
static NSString *MSG_TYPE       = @"message_type";
static NSString *MSG_STATUS    = @"status";
static NSString *MSG_SYS_CODE     = @"syscode";
static NSString *MSG_IS_READ  = @"isread";


@interface NTMsgOperate (){
    FMDatabase *db;
}

@end

@implementation NTMsgOperate

/**
 *  初始化方法
 *
 *  @return 该类的实例
 */
-(id)init{
    
    self = [super init];
    if (self) {
        
        NSString * database_path = [[PublicMethods getDirectoryOfDocumentFolder] stringByAppendingPathComponent:@"nt.sqlite"];
        
        db = [FMDatabase databaseWithPath:database_path];
        
        if ([db open]) {
            
            NSString *sqlCreateTable = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' (%@ interger PRIMARY KEY UNIQUE, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT, %@ TEXT)",MSG_TABLE_NAME,MSG_ID,MSG_ACCOUNT_ID,MSG_ADD_TIME,MSG_AUTHOR,MSG_CREATETIME,MSG_ENDTIME,MSG_IS_ALERT,MSG_CONTEXT,MSG_TITLE,MSG_TYPE,MSG_STATUS,MSG_SYS_CODE,MSG_IS_READ];
            [db executeUpdate:sqlCreateTable];
            
            [db close];
            
        }else {
            NSLog(@"数据库打开失败");
        }
    }
    
    return self;
}

/**
 *  插入系统通知
 *
 *  @param sysmsgAry 数组
 */
-(void)insertSysmsgWithAry:(NSArray*)sysmsgAry{
    
    if ([db open]) {
        for (NSDictionary *dic in sysmsgAry) {
            
            NSString *insertSql = [NSString stringWithFormat:
                                   @"INSERT INTO '%@' ('%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@') VALUES ('%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@')",
                                   MSG_TABLE_NAME,MSG_ID,MSG_ACCOUNT_ID,MSG_ADD_TIME,MSG_AUTHOR,MSG_CREATETIME,MSG_ENDTIME,MSG_IS_ALERT,MSG_CONTEXT,MSG_TITLE,MSG_TYPE,MSG_STATUS,MSG_SYS_CODE,MSG_IS_READ,[dic verifiedObjectForKey:@"message_id"],[dic verifiedObjectForKey:@"account_id"],[dic verifiedObjectForKey:@"addtime"],[dic verifiedObjectForKey:@"author"],[dic verifiedObjectForKey:@"createtime"],[dic verifiedObjectForKey:@"endtime"],@"1",[dic verifiedObjectForKey:@"message_context"],[dic verifiedObjectForKey:@"message_title"],[dic verifiedObjectForKey:@"message_type"],[dic verifiedObjectForKey:@"status"],[dic verifiedObjectForKey:@"syscode"],@"0"];
            BOOL result = [db executeUpdate:insertSql];
            if (!result) {
                
                NSLog(@"数据库插入消息失败");
            }
        }
        
        [db close];
    }

}

/**
 *  插入用户通知
 *
 *  @param sysmsgAry 数组
 */
-(void)insertUsermsgWithAry:(NSArray*)sysmsgAry{
    
    if ([db open]) {
        for (NSDictionary *dic in sysmsgAry) {
            
            NSString *insertSql = [NSString stringWithFormat:
                                   @"INSERT INTO '%@' ('%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@') VALUES ('%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@','%@')",
                                   MSG_TABLE_NAME,MSG_ID,MSG_ACCOUNT_ID,MSG_ADD_TIME,MSG_AUTHOR,MSG_CREATETIME,MSG_ENDTIME,MSG_IS_ALERT,MSG_CONTEXT,MSG_TITLE,MSG_TYPE,MSG_STATUS,MSG_SYS_CODE,MSG_IS_READ,[dic verifiedObjectForKey:@"message_id"],[dic verifiedObjectForKey:@"account_id"],[dic verifiedObjectForKey:@"addtime"],[dic verifiedObjectForKey:@"author"],[dic verifiedObjectForKey:@"createtime"],[dic verifiedObjectForKey:@"endtime"],[dic verifiedObjectForKey:@"isalert"],[dic verifiedObjectForKey:@"message_context"],[dic verifiedObjectForKey:@"message_title"],[dic verifiedObjectForKey:@"message_type"],[dic verifiedObjectForKey:@"status"],[dic verifiedObjectForKey:@"syscode"],@"0"];
            BOOL result = [db executeUpdate:insertSql];
            if (!result) {
                
                NSLog(@"数据库插入消息失败");
            }
        }
        
        [db close];
    }
    
}

@end
