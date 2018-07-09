//
//  HttpsConnection.m
//  icCard_Ios
//
//  Created by 古秀湖 on 15/10/24.
//
//

#import "HttpsConnection.h"

#define MBS_Comm_TimeOutInterval 20

#define MBSAmpersandCharacter @"&"
#define MBSQUESTION_MARK @"?"
#define MBSEQUAL_MARK @"="
#define MBSWhiteSpace @" "

@implementation HttpsConnection
@synthesize mp_urlString, mp_postDict, mp_httpMethod, mp_statusCode,mp_pageComm,delegate,m_postString ,m_requestType,m_timeoutInterval,mp_headDict,needGetData;

-(id) init{
    if(self = [super init]){
        m_receivedData = [[NSMutableData alloc] init];
        m_postString = nil;
        mp_httpMethod = NTMBSPOST;
        connection = nil;
        m_timeoutInterval = MBS_Comm_TimeOutInterval;
        m_requestType = NTRequestHeadCCB;
        m_timeoutFlag = NO;//NO为通信请求未完成　YES　为完成
        
    }
    return self;
}

- (void)setM_postString:(NSString *)postString
{
    if (m_postString != nil) {
        m_postString = nil;
    }
    
    m_postString = [postString copy];
}

-(void)setMp_postDict:(NSMutableDictionary*)dic
{
    if (m_postString != nil) {
        m_postString = nil;
    }
    m_postString = [self getPostStringFromDic];
    NSLog(@"http 通讯json：%@",m_postString);
}

//获取当前的mp_urlString中的键值对
- (void)getHrefDict:(NSMutableDictionary *)dict
{
    NSRange range1 = [mp_urlString rangeOfString:MBSQUESTION_MARK];
    
    if (range1.location != NSNotFound)
    {
        NSString *suffix = [mp_urlString substringFromIndex:(range1.location + range1.length)];
        NSArray *array = [suffix componentsSeparatedByString:MBSAmpersandCharacter];
        
        for (NSString *str in array)
        {
            NSArray *keyValueArray = [str componentsSeparatedByString:MBSEQUAL_MARK];
            
            if (2 == [keyValueArray count])
            {
                
                [dict setObject:[[keyValueArray objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] forKey:[[keyValueArray objectAtIndex:0] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
            }
        }
    }
}

- (void)commTimeOut{
    
    if (m_timeoutFlag == NO) {
        
        [self cancelComm];
        if (delegate != nil && [delegate respondsToSelector:@selector(timeOut:)] ) {
            [delegate timeOut:self];
            return;
        }
        NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
        [dic setValue:@"您的网络环境不给力，请重试！" forKey:@"NSLocalizedDescription"];
        NSError *error= [[NSError alloc] initWithDomain:NSURLErrorDomain code:0 userInfo:dic];
        //普通登陆里面,需要用到这一个error
        if (delegate != nil && [delegate respondsToSelector:@selector(communcationError:comm:)]) {
            [delegate communcationError:error.description comm:self];
        }
    }
}

//2012-11-14  修改通信方法
-(void)sendAHttpRequest
{
    NSString *staticUrlStr = mp_urlString;
    
    if(NTMBSPOST == mp_httpMethod)
    {
        [self sendPostAsynchronousRequest:staticUrlStr];
    }
    else if(NTMBSGET == mp_httpMethod)
    {
        [self sendAsynchronousRequest:staticUrlStr];
    }
    [self performSelector:@selector(commTimeOut) withObject:nil afterDelay:m_timeoutInterval];
}

-(void) sendHttpRequest
{
    [self performSelectorOnMainThread:@selector(sendAHttpRequest) withObject:nil waitUntilDone:NO];
}

-(BOOL)isEmptyObject:(NSObject *)object
{
    if ([object isEqual:[NSNull null]] || object == nil)
    {
        return YES;
    }
    else if ([object isKindOfClass:[NSString class]])
    {
        NSString *string = (NSString *)object;
        
        if (0 == [string length])
        {
            return YES;
        }
    }
    
    return NO;
}

/*
 发送get请求
 postfield标签也可以与 get 方法一起使用。注释：如果使用该方法，字段以及它们的值会添加到 URL 的末尾。
 url和参数用？拼接
 */
-(void) sendAsynchronousRequest:(NSString *)urlStr
{
    NSMutableString *finalUrlStr = [NSMutableString string];
    [finalUrlStr appendString:urlStr];
    
    if (![self isEmptyObject:m_postString]){
        [finalUrlStr appendString:@"?"];
        [finalUrlStr appendString:m_postString];
    }
    
    NSURL* url = [NSURL URLWithString:finalUrlStr];
    
    
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc] initWithURL:url
                                                                cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                            timeoutInterval:m_timeoutInterval] ;
    
    [request setHTTPMethod:@"GET"];
    
    
    [request setValue: @"application/json" forHTTPHeaderField:@"Content-Type"];
    [request addValue:@"IPHONE" forHTTPHeaderField:@"UA"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    //添加报文头信息
    NSArray *keys = [mp_headDict allKeys];
    
    if ([keys count] != 0)    {
        for (NSString* headKey in keys){
            [request addValue:[mp_headDict objectForKey:headKey] forHTTPHeaderField:headKey];
            
        }
    }
    
    connection = [[NSURLConnection alloc] initWithRequest:request delegate:self startImmediately:YES];
    //做加载的动画
    
    [connection start];
}

// 发送post请求
-(void) sendPostAsynchronousRequest:(NSString *)urlStr
{
    NSURL* url = [NSURL URLWithString:urlStr];
    
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc] initWithURL:url
                                                                cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                            timeoutInterval:m_timeoutInterval] ;
    [request setHTTPMethod:@"POST"];
    
    //header
    [request setValue: @"application/json" forHTTPHeaderField:@"Content-Type"];
    [request addValue:@"IPAD" forHTTPHeaderField:@"UA"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    [request addValue:[[NSUserDefaults standardUserDefaults] objectForKey:@"DN"] forHTTPHeaderField:@"DN"];
    
    
    //添加报文头信息
    NSArray *keys = [mp_headDict allKeys];
    
    if ([keys count] != 0)	{
        for (NSString* headKey in keys){
            [request addValue:[mp_headDict objectForKey:headKey] forHTTPHeaderField:headKey];
        }
    }
    
    NSData *postData = [m_postString dataUsingEncoding: NSUTF8StringEncoding allowLossyConversion: YES];
    
    NSString *postDataLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postData length]];
    [request setHTTPBody:postData];
    [request setValue:postDataLength forHTTPHeaderField:@"Content-Length"];
    
    connection	= [[NSURLConnection alloc] initWithRequest:request delegate:self startImmediately:YES];
    [connection start];
    
}

/*
 根据postdict获取postvalue
 1、判断postDict中是否有信息
 2、枚举postdict中的内容，拼接请求值
 3、返回拼接后的值
 */
-(NSString*) getPostStringFromDic
{
    NSMutableString* postStr = [NSMutableString string];
    NSArray *keys = [mp_postDict allKeys];
    
    if ([keys count] != 0){
        for (NSString* postKey in keys){
            NSObject *valueStr = [mp_postDict objectForKey:postKey];
            if([valueStr isKindOfClass:[NSArray class]]){
                for(NSString *item in (NSArray*)valueStr){
                    [postStr appendString:[NSString stringWithFormat:@"%@=%@&", postKey, item]];
                }
            }else{
                [postStr appendString:[NSString stringWithFormat:@"%@=%@&", postKey, valueStr]];
            }
        }
        
        NSRange andRange = [postStr rangeOfString:MBSAmpersandCharacter];
        if (andRange.location != NSNotFound){
            [postStr deleteCharactersInRange:NSMakeRange([postStr length]-1, 1)];
        }
        
    }
    
    return postStr;
}


//取消通讯
-(void)cancelComm
{
    if (connection != nil){
        [connection cancel];
        connection = nil;
        mp_statusCode = 0;
        m_timeoutFlag = YES;
        m_receivedData = [[NSMutableData alloc] init];
    }
}


//-----------delegate-------------

// 只有在证书错误的情况下,才需要这两个函数
//NSURLConnectionDelegate
// for https
- (BOOL)connection:(NSURLConnection *)conn canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace
{
    return YES;
}

// for https
// NSURLAuthenticationChallenge encapsulates a challenge from a server requiring authentication from the client.
- (void)connection:(NSURLConnection *)conn didReceiveAuthenticationChallenge: (NSURLAuthenticationChallenge *) challenge
{
    if ([challenge.protectionSpace.authenticationMethod isEqualToString: NSURLAuthenticationMethodServerTrust])
    {
        [challenge.sender useCredential: [NSURLCredential credentialForTrust: challenge.protectionSpace.serverTrust] forAuthenticationChallenge: challenge];
    }
    
    [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
}


// for both http & https
- (void)connection:(NSURLConnection *)conn didReceiveResponse:(NSURLResponse *)response
{
    NSHTTPURLResponse* httpUrlResponse = (NSHTTPURLResponse*)response;
    self.mp_statusCode = (int)[httpUrlResponse statusCode];
    m_headerDic = [httpUrlResponse allHeaderFields] ;
}

// for both http & https
- (void)connection:(NSURLConnection *)conn didReceiveData:(NSData *)data
{
    [m_receivedData appendData:data];
}

// for both http & https
- (void)connection:(NSURLConnection *)conn didFailWithError:(NSError *)error
{
    //通信失败，取消通信标识
    m_timeoutFlag=YES;
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    [self cancelComm];
    
    if (delegate != nil && [delegate respondsToSelector:@selector(communcationError:comm:)])	{
        [delegate communcationError:error.description comm:self];
    }
}

- (void)doExceptionWithErrorCode:(NSString *)errorCode
                        withType:(int)exceptionType
                    withDelegate:(id)dele
{
    NSString *msg = NSLocalizedString(errorCode,nil);
    
    if (nil == msg)
    {
        msg = NSLocalizedString(@"000",nil);
    }
    
    NSLog(@"JS通讯 exceptionType:%d;message:%@",exceptionType,msg);
    
}
// for both http & https
- (void)connectionDidFinishLoading:(NSURLConnection *)conn
{
    m_timeoutFlag = YES;
    //异步线程， 这里应该加一步取消time out 操作确保万无一失 ty 2012-2-2
    
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    
    //通信成功，取消通信标识
    //数据下载完毕就通知容器做页面流转.
    if(200 == self.mp_statusCode)	{
        [self performSelector:@selector(finishCommunication) withObject:nil afterDelay:.25f];
        
    }	else{
        if (701 == self.mp_statusCode || 702 == self.mp_statusCode) {
            
        }   else    {
            //不正常的情况
            NSString *code = [NSString stringWithFormat:@"%ld",(long)self.mp_statusCode];
            
            [self doExceptionWithErrorCode:code
                                  withType:0
                              withDelegate:nil];
            
            
            if (delegate != nil && [delegate respondsToSelector:@selector(communcationError:comm:)]){
                
                NSString* responseStr = [[NSString alloc] initWithData:m_receivedData encoding:NSUTF8StringEncoding] ;
                
                responseStr = [responseStr stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
                
                [delegate communcationError:responseStr comm:self];
            }
        }
        
        [self cancelComm];
    }
}


/**
 *   通信成功
 *    1、获取所获取的数据生成的页面内容string
 *    2、判断是否本地处理
 *    3、若有效，交给导航控制器处理页面
 */
- (void)finishCommunication
{
    m_timeoutFlag =  YES;
    
    
    if (needGetData) {
        if (delegate != nil && [delegate respondsToSelector:@selector(communcationFinishData:comm:header:)]) {
            [delegate communcationFinishData:m_receivedData comm:self header:m_headerDic];
        }
    }else{
        NSString* responseStr = [[NSString alloc] initWithData:m_receivedData encoding:NSUTF8StringEncoding] ;
        
        responseStr = [responseStr stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        
        if(delegate != nil && [delegate respondsToSelector:@selector(communcationFinish: comm: header:)]){
            [delegate communcationFinish:responseStr comm:self header:m_headerDic];
        }

    }
    
    [self cancelComm];
    
}
@end
