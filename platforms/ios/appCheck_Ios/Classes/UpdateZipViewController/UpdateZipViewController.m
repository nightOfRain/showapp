//
//  UpdateZipViewController.m
//  icCard_Ios
//
//  Created by 古秀湖 on 15/10/12.
//
//

#import "UpdateZipViewController.h"
#import "MainViewController.h"
#import "AppDelegate.h"
#import "PublicMethods.h"
#import "FuFileManager.h"
#import "AFNetworking.h"
#import "ZipArchive.h"
#import "Masonry.h"
#import "NTMsgOperate.h"
#import "ASProgressPopUpView.h"
#import "UIAlertView+Blocks.h"
#import "MBProgressHUD.h"
#import "NSDictionary+Verified.h"

//#define SERVER_EMMMNG_ADDR @"http://168.39.5.57:8088"
#define SERVER_EMMMNG_ADDR @"https://apachedt.chinacloudapp.cn:6666"

@interface UpdateZipViewController ()<UIAlertViewDelegate,ASProgressPopUpViewDataSource>

/**
 *  保存zip信息
 */
@property (strong, nonatomic) NSDictionary *zipInfoDic;

@property (strong, nonatomic) ASProgressPopUpView *progressView;
@property (strong, nonatomic) UILabel *tipsLabel;

/**
 *  处理后的下载列表
 */
@property (strong, nonatomic) NSMutableArray *downloadList;
/**
 *  已下载的列表
 */
@property (strong, nonatomic) NSMutableArray *downloadedList;

/**
 *  是否有基线版本，是否需要删除原来的www再重新解压
 */
@property BOOL hasBase;

@end

@implementation UpdateZipViewController

- (id)init
{
    self = [super init];
    if (self) {
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark View lifecycle

- (void)viewWillAppear:(BOOL)animated
{
    // View defaults to full size.  If you want to customize the view's size, or its subviews (e.g. webView),
    // you can do so here.
    
    [super viewWillAppear:animated];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self.view setBackgroundColor:[UIColor whiteColor]];
    
    UIImageView *bgImageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:IS_IPHONE?PHONE_UPDATE_VIEW_BG_IMAGE_NAME:PAD_UPDATE_VIEW_BG_IMAGE_NAME]];
    bgImageView.contentMode = UIViewContentModeScaleAspectFill;
    [self.view addSubview:bgImageView];
    [bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.equalTo(self.view.mas_top).with.offset(0);
        make.left.equalTo(self.view.mas_left).with.offset(0);
        make.right.equalTo(self.view.mas_right).with.offset(0);
        make.bottom.equalTo(self.view.mas_bottom).with.offset(0);

    }];
    
    self.progressView = [[ASProgressPopUpView alloc]init];
    [self.view addSubview:self.progressView];
    [self.progressView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.bottom.equalTo(self.view.mas_bottom).with.offset(-40);
        make.left.equalTo(self.view.mas_left).with.offset(10);
        make.right.equalTo(self.view.mas_right).with.offset(-10);
        
        make.height.mas_equalTo(2);
    }];
    self.progressView.font = [UIFont fontWithName:@"Futura-CondensedExtraBold" size:16];
    self.progressView.popUpViewAnimatedColors = @[[UIColor redColor], [UIColor orangeColor], [UIColor greenColor]];
    self.progressView.dataSource = self;
    
    [self.progressView showPopUpViewAnimated:YES];
    [self.progressView setHidden:YES];
    
    /**
     *  下载提示
     */
    self.tipsLabel = [[UILabel alloc]init];
    [self.tipsLabel setFont:[UIFont systemFontOfSize:13]];
    [self.view addSubview:self.tipsLabel];
    [self.tipsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.progressView.mas_bottom).with.offset(3);
        make.left.equalTo(self.view.mas_left).with.offset(10);
        make.right.equalTo(self.view.mas_right).with.offset(-10);
        
        make.height.mas_equalTo(15);

    }];
    [self.tipsLabel setHidden:YES];
    
    [self checkUpdateInfo];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return [super shouldAutorotateToInterfaceOrientation:interfaceOrientation];
}

#pragma mark - Actions

/**
 *  跳到主页
 *
 *  @param sender 发送者
 */
-(void)jumpToMainViewAction:(id)sender{
    
    MainViewController *mainVC = [[MainViewController alloc]init];
    
    NSString *featureFolder = [PublicMethods getDirectoryOfDocumentFolder];
    NSString* wwwFolder = [[NSString stringWithFormat:@"file://%@",featureFolder ]stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];

    mainVC.wwwFolderName = wwwFolder;
    
    mainVC.startPage = [PublicMethods getObjFromUserdefaultsWithKey:@"startpage"];
    
    //跳转
    AppDelegate *delegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [UIView transitionWithView:delegate.window duration:0.1 options:UIViewAnimationOptionTransitionCrossDissolve animations:^(void) {
        
        BOOL oldState = [UIView areAnimationsEnabled];
        [UIView setAnimationsEnabled:NO];
        delegate.window.rootViewController = mainVC;
        [UIView setAnimationsEnabled:oldState];
        
    } completion:nil];
}

/**
 *  升级检查
 */
- (void)checkUpdateInfo{
    
    
    NSString *serverUrlStr = [NSString stringWithFormat:@"%@/emmmng/app?txcode=app101&appversion=%@&appid=%@",SERVER_EMMMNG_ADDR,[[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"],[[NSBundle mainBundle] bundleIdentifier]];
    [PublicMethods sendAFNetworkingPostWithURL:serverUrlStr andParameters:nil andSuccessBlock:^(id responsObject) {
        
        if ([responsObject isKindOfClass:[NSDictionary class]]) {
            
            NSString *hasUpdate = [responsObject verifiedObjectForKey:@"hasUpdate"];
            if ([hasUpdate isEqualToString:@"1"]) {
                NSLog(@"app 有新版本");
                [[MBProgressHUD getInstance]dismissIndicator];
                
                id app_version =  [PublicMethods getObjFromUserdefaultsWithKey:@"app_version"];
                if (app_version != [NSNull null] && app_version != nil && ![app_version isEqualToString:@""]) {
                    
                    [[[UIAlertView alloc] initWithTitle:@"提示"
                                                message:@"有新的版本，是否立即更新"
                                       cancelButtonItem:[RIButtonItem itemWithLabel:@"暂不更新" action:^{
                        // Handle "Cancel"
                        [self jumpToMainViewAction:nil];
                        
                    }]otherButtonItems:[RIButtonItem itemWithLabel:@"更新" action:^{
                        
                        //跳到app store
                        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:[NSString stringWithFormat:@"itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?type=Purple+Software&id=%@",[responsObject verifiedObjectForKey:@"appleStoreID"]]]];
                        
                        
                    }], nil] show];
                    
                }else{
                    [[[UIAlertView alloc] initWithTitle:@"提示"
                                                message:@"有新的版本，是否立即更新"
                                       cancelButtonItem:nil otherButtonItems:[RIButtonItem itemWithLabel:@"更新" action:^{
                        
                        //跳到app store
                        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:[NSString stringWithFormat:@"itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?type=Purple+Software&id=%@",[responsObject verifiedObjectForKey:@"appleStoreID"]]]];
                        
                        
                    }], nil] show];
                    
                    
                }
                
                
                
                
            }else{
                
                NSLog(@"app 没有新版本");
                /**
                 *  先检测有没有更新
                 */
                NSString *serverUrlStr;
                id app_version =  [PublicMethods getObjFromUserdefaultsWithKey:@"app_version"];
                if (app_version != [NSNull null] && app_version != nil && ![app_version isEqualToString:@""]) {
                    
                    serverUrlStr = [NSString stringWithFormat:@"%@/emmmng/zipfiles?txcode=vem002&app_id=%@&app_version=%@",SERVER_EMMMNG_ADDR,[[NSBundle mainBundle] bundleIdentifier],app_version];
                }else{
                    serverUrlStr = [NSString stringWithFormat:@"%@/emmmng/zipfiles?txcode=vem002&app_id=%@",SERVER_EMMMNG_ADDR,[[NSBundle mainBundle] bundleIdentifier]];
                    
                }
                
                
                [PublicMethods sendAFNetworkingPostWithURL:serverUrlStr andParameters:nil andSuccessBlock:^(id responsObject) {
                    
                    NSLog(@"拿回来了更新信息，开始判断");
                    
                    self.zipInfoDic = responsObject;
                    
                    [self downloadZipWithDic:responsObject];
                    
                    return;
                    
                } andFaildBlock:^(NSString *errorDes) {
                    
                    [self updateFaild];
                    
                } andTimeoutBlock:^{
                    
                    [self updateFaild];
                    
                }];
                
            }
        }else{
            
            [self updateFaild];
        }
        
    } andFaildBlock:^(NSString *errorDes) {
        
        [self updateFaild];
        
    } andTimeoutBlock:^{
        
        [self updateFaild];
        
    }];
    
}

/**
 *  开始下载zip包
 *
 *  @param ary zip包信息
 */
-(void)downloadZipWithDic:(NSDictionary*)dic{
    
    NSLog(@"开始下载zip包");
    
    /**
     *  数据处理
     */
    self.downloadList = [[NSMutableArray alloc]init];
    self.downloadedList = [[NSMutableArray alloc]init];
    
    for (NSDictionary *versionDic in [dic verifiedObjectForKey:@"version"]) {
        
        NSArray *packagesAry = [versionDic verifiedObjectForKey:@"packages"];
        for (NSDictionary *packDic in packagesAry) {
            
            NSMutableDictionary *dic = [[NSMutableDictionary alloc]initWithDictionary:packDic];
            [dic setObject:[versionDic verifiedObjectForKey:@"startpage"] forKey:@"startpage"];
            [self.downloadList addObject:dic];
        }
    }
    
    /**
     *  判断有没有下载包
     */
    if (self.downloadList.count > 0) {
        /**
         *  判断静默
         */
        NSString *isstatic = [dic verifiedObjectForKey:@"isstatic"];
        if ([isstatic isEqualToString:@"1"]) {
            NSLog(@"静默下载，开始下载了。。。");
            /**
             *  执行下载
             */
            [self handleDownload];
        }else{
            
            NSLog(@"不静默，开始判断是否强制");
            
            /**
             *  判断强制
             */
            NSString *forceupdate = [dic verifiedObjectForKey:@"forceupdate"];
            if ([forceupdate isEqualToString:@"1"]) {
                NSLog(@"强制更新");
                [[[UIAlertView alloc] initWithTitle:@"提示"
                                            message:[dic verifiedObjectForKey:@"version_msg"]
                                   cancelButtonItem:[RIButtonItem itemWithLabel:@"更新" action:^{
                    /**
                     *  执行下载
                     */
                    [self handleDownload];
                    
                }]
                                   otherButtonItems:nil, nil] show];
            } else {
                NSLog(@"不强制更新");
                
                [[[UIAlertView alloc] initWithTitle:@"提示"
                                            message:[dic verifiedObjectForKey:@"version_msg"]
                                   cancelButtonItem:[RIButtonItem itemWithLabel:@"暂不更新" action:^{
                    // Handle "Cancel"
                    
                    [self jumpToMainViewAction:nil];
                }]
                                   otherButtonItems:[RIButtonItem itemWithLabel:@"更新" action:^{
                    /**
                     *  执行下载
                     */
                    [self handleDownload];
                }], nil] show];
            }
            
        }
        
        /**
         *  基线版本
         */
        NSString *hasBaseVerson = [dic verifiedObjectForKey:@"hasBaseVerson"];
        if ([hasBaseVerson isEqualToString:@"1"]) {
            NSLog(@"有基线版本");
            
            self.hasBase = YES;
        }else{
            self.hasBase = NO;
        }
        

    }else{
        NSLog(@"没有更新");
        
        [self jumpToMainViewAction:nil];
    }
    
}


-(void)handleDownload{
    
    [self.progressView setHidden:NO];
    [self.tipsLabel setHidden:NO];
    
    [self.tipsLabel setText:[NSString stringWithFormat:@"正在下载更新包（%d/%d）",self.downloadedList.count+1,self.downloadedList.count+self.downloadList.count]];

    NSDictionary *dic = self.downloadList.firstObject;
    
    NSString *zip_id = [dic objectForKey:@"zip_id"];
    NSString *zip_type = [dic objectForKey:@"zip_type"];
    NSString *zip_version = [dic objectForKey:@"app_version"];
    
    NSString *serverUrlStr = [NSString stringWithFormat:@"%@/emmmng/zipfiles?txcode=vem003&app_id=%@&app_version=%@&zip_type=%@",SERVER_EMMMNG_ADDR,[[NSBundle mainBundle] bundleIdentifier],zip_version,zip_type];
    
    
    //下载附件
    NSURL *url = [[NSURL alloc] initWithString:serverUrlStr];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    operation.inputStream   = [NSInputStream inputStreamWithURL:url];
    
    NSString *filePath1 = [[PublicMethods getDirectoryOfDocumentFolder] stringByAppendingString:[NSString stringWithFormat:@"/%@_%@",zip_version,zip_type]];
    
    operation.outputStream  = [NSOutputStream outputStreamToFileAtPath:filePath1 append:NO];
    
    //下载进度控制
    [operation setDownloadProgressBlock:^(NSUInteger bytesRead, long long totalBytesRead, long long totalBytesExpectedToRead) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.progressView setProgress:(float)totalBytesRead/totalBytesExpectedToRead animated:YES];
            
        });
        
        NSLog(@"is download：%f", (float)totalBytesRead/totalBytesExpectedToRead);
    }];
    
    
    //已完成下载
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [self.downloadedList addObject:dic];
        
        NSLog(@"下载成功");
        if (self.downloadList.count > 0) {
            [self handleDownload];
        }else{
            
            NSLog(@"全部下载完了");
            [self.progressView setHidden:YES];
            [self.tipsLabel setHidden:YES];

            /**
             *  判断有没有基线版本，有的话删除原来的www
             */
            if (self.hasBase) {
                NSString *imageDir = [[PublicMethods getDirectoryOfDocumentFolder] stringByAppendingPathComponent:@"www"];
                NSFileManager *fileManager = [NSFileManager defaultManager];
                [fileManager removeItemAtPath:imageDir error:nil];
            }
            
            for (NSDictionary *downloadDic in self.downloadedList) {
                
                FuFileManager *manager = [FuFileManager Instance];
                [manager checkZipMd5WithPath:[[PublicMethods getDirectoryOfDocumentFolder] stringByAppendingString:[NSString stringWithFormat:@"/%@_%@",[downloadDic objectForKey:@"app_version"],[downloadDic objectForKey:@"zip_type"]]] tomd5:[downloadDic verifiedObjectForKey:@"zip_md5"] After:^(BOOL result) {
                    
                    if (result) {
                        
                        NSLog(@"%@ md5校验通过",[NSString stringWithFormat:@"/%@_%@",[downloadDic objectForKey:@"app_version"],[downloadDic objectForKey:@"zip_type"]]);
                        
                        [PublicMethods saveToUserdefaultsWithKey:@"startpage" andObj:[downloadDic verifiedObjectForKey:@"startpage"]];
                        [PublicMethods saveToUserdefaultsWithKey:@"app_version" andObj:[downloadDic objectForKey:@"app_version"]];

                        //解压完了的删除
                        NSFileManager *manager = [NSFileManager defaultManager];
                        [manager removeItemAtPath:[[PublicMethods getDirectoryOfDocumentFolder] stringByAppendingString:[NSString stringWithFormat:@"/%@_%@",[downloadDic objectForKey:@"app_version"],[downloadDic objectForKey:@"zip_type"]]] error:nil];
                        
                    }else{
                        
                        [self updateFaild];
                    }
                    
                }];
            }
            
            //解压cordoa
            NSString* filename = [NSString stringWithFormat:@"www/www.zip"];
            NSString* featureFolder = [[NSBundle mainBundle] pathForResource:filename ofType:@""];
            
            ZipArchive *za = [[ZipArchive alloc] init];
            if ([za UnzipOpenFile:featureFolder]) {
                BOOL ret = [za UnzipFileTo:[PublicMethods getDirectoryOfDocumentFolder] overWrite: YES];
                if (NO == ret){
                    
                    [self updateFaild];
                    
                }else{
                    //最终完成了，跳转
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self jumpToMainViewAction:nil];
                    });
                }
                [za UnzipCloseFile];
                
            }else{
                NSLog(@"cannot found file");
                [self updateFaild];
                
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        //下载失败
        NSLog(@"下载失败");
        [self updateFaild];
        
    }];
    
    [operation start];
    
    [self.downloadList removeObjectAtIndex:0];

}

/**
 *  更新失败了，显示重新更新
 */
-(void)updateFaild{
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        [self.progressView setHidden:YES];
        [self.tipsLabel setHidden:YES];

        [[[UIAlertView alloc] initWithTitle:@"提示"
                                    message:@"更新失败"
                           cancelButtonItem:[RIButtonItem itemWithLabel:@"更新" action:^{
            [self checkUpdateInfo];
            
        }] otherButtonItems:nil, nil] show];
        
        
    });

}

/**
 *  将Dictionary转换成字符串
 *
 *  @param dict 要转换的Dic
 *
 *  @return Dic的字符串
 */
- (NSString*) convertDictionaryToString:(NSMutableDictionary*) dict{
    
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


#pragma mark - ASProgressPopUpViewDataSource <NSObject>
- (NSString *)progressView:(ASProgressPopUpView *)progressView stringForProgress:(float)progress{
    
    NSString *s;
//    if (progress < 0.2) {
//        s = @"Just starting";
//    } else if (progress > 0.4 && progress < 0.6) {
//        s = @"About halfway";
//    } else if (progress > 0.75 && progress < 1.0) {
//        s = @"Nearly there";
//    } else if (progress >= 1.0) {
//        s = @"Complete";
//    }
    return s;

}


@end

