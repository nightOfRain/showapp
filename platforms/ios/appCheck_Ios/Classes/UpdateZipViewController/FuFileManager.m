//
//  FuFileManager.m
//  Future
//
//  Created by huihui on 14-6-17.
//  Copyright (c) 2014年 China M-world Co.,Ltd. All rights reserved.
//

#import "FuFileManager.h"
#import "ZipArchive.h"
#import "FuMD5Util.h"
#import "PublicMethods.h"

@implementation FuFileManager
static FuFileManager* _instanceFu;

+(FuFileManager*)Instance{
    if (_instanceFu == nil) {
        _instanceFu = [[FuFileManager alloc]init];
    }
    return _instanceFu;
}
- (id)init
{
    self = [super init];
    if (self) {
    }
    return self;
}

- (NSString *)getDirectoryOfDocumentFolder {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES); // 获取所有Document文件夹路径
    NSString *documentsPath = paths[0]; // 搜索目标文件所在Document文件夹的路径，通常为第一个
    
    if (!documentsPath) {
        NSLog(@"Documents目录不存在");
    }
    
    return documentsPath;
}

- (NSString *)getDocumentHtmlFileWithName:(NSString *)fileName {
    NSString *documentsPath = [self getDirectoryOfDocumentFolder];
    if (documentsPath) {
        return [documentsPath stringByAppendingPathComponent:fileName]; // 获取用于存取的目标文件的完整路径
    }
    
    return nil;
}

- (BOOL)isFileExitsAtPath:(NSString *)filePath {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if ([fileManager fileExistsAtPath:filePath isDirectory:NULL]) {
        return YES;
    }
    return NO;
}

- (void)removeFileAtPath:(NSString *)filePath
{
    NSLog(@"删除文件夹下文件:%@",filePath);
    NSError *error = nil;
    NSFileManager *fileMgr = [NSFileManager defaultManager];
    NSArray *directoryContents = [fileMgr contentsOfDirectoryAtPath:filePath error:&error];
    if (error == nil) {
        for (NSString *path in directoryContents) {
            NSString *fullPath = [filePath stringByAppendingPathComponent:path];
            BOOL removeSuccess = [fileMgr removeItemAtPath:fullPath error:&error];
            if (!removeSuccess) {
                // Error handling
                NSLog(@"移除文件失败，错误信息：%@", error);
                //                DebugLog(@"清除Documents下文件失败");
            }
        }
    } else {
        // Error handling
        //        DebugLog(@"清除Documents下文件失败");
    }
    
}

#pragma mark - 将初始zip包解压到document目录
-(void)UnzipFileFromMainbundleToDocument
{
    NSBundle* mainBundle = [NSBundle mainBundle];
    NSString* filename = [NSString stringWithFormat:@"www/zip/www.zip"];
    NSString* featureFolder = [mainBundle pathForResource:filename ofType:@""];
    
    BOOL result = [self isFileExitsAtPath:featureFolder];
    if (result) {
        NSLog(@"解压原始zip包");
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            [self UnZipFileFromPath:featureFolder toPath:[NSString stringWithFormat:@"%@/www",[self getDirectoryOfDocumentFolder]]];

        });
    }
}

- (void )downLoadFileFromPath:(NSString *)fromPath toPath:(NSString *)toPath After:(DownLoadBlock)block
{
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        NSURL *url = [NSURL URLWithString:fromPath];
        NSError *error = nil;
        NSData *data = [NSData dataWithContentsOfURL:url options:0 error:&error];
        
        
        if(!error)
        {
            //            NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
            //            NSString *path = [paths objectAtIndex:0];
            //            NSString *zipPath = [path stringByAppendingPathComponent:@"fidget.zip"];
            
            [data writeToFile:toPath options:0 error:&error];
            
            
            if(!error)
            {
                block();
            }
            else
            {
                NSLog(@"Error saving file %@",error);
            }
        }
        else
        {
            NSLog(@"Error downloading zip file: %@", error);
        }
        
    });
    
}

- (void)UnZipFileFromPath:(NSString *)fromPath toPath:(NSString *)toPath
{
    ZipArchive *za = [[ZipArchive alloc] init];
    if ([za UnzipOpenFile:fromPath Password:@"123456"]) {
        BOOL ret = [za UnzipFileTo:toPath overWrite: YES];
        if (NO == ret){}
        [za UnzipCloseFile];
    }else{
        NSLog(@"cannot found file %@",fromPath);
    }
}

- (void)checkZipMd5WithPath:(NSString *)path tomd5:(NSString *)md5 After:(UnzipBlock)block{
    
    if ([self isFileExitsAtPath:path]) {
        if ([FuMD5Util compareResourcesMD5:[FuMD5Util getFileMD5WithPath:path] withSourcesMD5:md5]) {
            NSLog(@"md5校验通过，解压");
            [self UnZipFileFromPath:path toPath:[PublicMethods getDirectoryOfDocumentFolder]];
            block(YES);
            
        }else{
            NSLog(@"md5校验不通过");
            block(NO);
        }
    }else{
        NSLog(@"下载的更新包不存在");
        block(NO);
    }
}

@end
