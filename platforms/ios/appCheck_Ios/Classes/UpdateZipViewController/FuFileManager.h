//
//  FuFileManager.h
//  Future
//
//  Created by huihui on 14-6-17.
//  Copyright (c) 2014年 China M-world Co.,Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^DownLoadBlock)();
typedef void (^UnzipBlock)(BOOL result);
typedef void (^CallBack)(int Code);
typedef void (^onError)(int errorCode);
typedef void (^CheckCallBack)(int update);

@interface FuFileManager : NSObject

#pragma mark - 单例
+(FuFileManager*)Instance;

- (NSString *)getDirectoryOfDocumentFolder;
- (NSString *)getDocumentHtmlFileWithName:(NSString *)fileName;

- (BOOL)isFileExitsAtPath:(NSString *)filePath;
- (void)removeFileAtPath:(NSString *)filePath;
- (void)UnZipFileFromPath:(NSString *)fromPath toPath:(NSString *)toPath ;




- (void)checkZipMd5WithPath:(NSString *)path tomd5:(NSString *)md5 After:(UnzipBlock)block;

@end
