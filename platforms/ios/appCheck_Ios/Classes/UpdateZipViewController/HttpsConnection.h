//
//  HttpsConnection.h
//  icCard_Ios
//
//  Created by 古秀湖 on 15/10/24.
//
//

#import <Foundation/Foundation.h>
#import <Foundation/Foundation.h>

@class HttpsConnection;

@protocol HttpsConnectionDelegate

-(void)communcationFinish:(NSString *)pageString comm:(HttpsConnection *)comm header:(NSDictionary *)headerDic;//通讯成功，返回字符串的回调方法
-(void)communcationFinishData:(NSData *)pageData comm:(HttpsConnection *)comm header:(NSDictionary *)headerDic;//通讯成功，返回数据的回调方法

-(void)communcationError:(NSString *)errorDes  comm:(HttpsConnection *)comm ;//通讯报错
-(void)timeOut:(HttpsConnection *)comm ;//通讯超时
@end

typedef enum{
    NTRequestHeadCCB,
    NTRequestHeadPay,
    NTRequestHeadLogin,//liuwei 2.1.8 反欺诈
    NTRequestHeadOther,
}NTRequestHeadType;


typedef enum{
    NTMBSGET = 0,
    NTMBSPOST = 1,
    NTMBSPUT	= 2,
}NTMbsHttpMethod;

@interface HttpsConnection : NSObject<UIAlertViewDelegate> {
    
    //请求的url(不包含域名)
    NSString* mp_urlString;	           //请求的url(不包含域名)
    NTMbsHttpMethod mp_httpMethod;	       //方法类型
    NSMutableDictionary* mp_postDict;  //发送post请求的时候需要的信息.
    int mp_statusCode;	               //状态码
    
    NSMutableDictionary* mp_headDict;  //通讯报文头需要的信息.
    
    NSDictionary *m_headerDic; //通讯返回时报文头信息.
    
    float m_timeoutInterval;
    
    BOOL m_timeoutFlag;
    
    
    BOOL mp_pageComm;
    NSMutableData* m_receivedData;	//接收到的数据
    NSObject<HttpsConnectionDelegate> *delegate;
@private
    NSURLConnection* connection;	//http连接
    NSString *m_postString;
    NSString *m_callbackId;
    
}

/**
 *  加个标志
 */
@property NSInteger tag;

@property (nonatomic) NTRequestHeadType m_requestType;
@property (nonatomic, retain) 	NSObject<HttpsConnectionDelegate> *delegate;
@property (nonatomic, retain)	NSString *m_postString;
/**
 *  标志是拿nsdata还是string
 */
@property (nonatomic)	BOOL needGetData;
@property (nonatomic, copy) NSString* mp_urlString;
@property (nonatomic, retain) NSMutableDictionary* mp_postDict;
@property (nonatomic, retain) NSMutableDictionary* mp_headDict;
@property (nonatomic) NTMbsHttpMethod mp_httpMethod;
@property (nonatomic) int mp_statusCode;
@property (nonatomic) BOOL mp_pageComm;
@property (nonatomic)  float m_timeoutInterval;


//发起请求,根据mp_httpMethod,执行post还是get请求方法
-(void) sendHttpRequest;
-(void) sendAsynchronousRequest:(NSString *)urlStr;
-(void) sendPostAsynchronousRequest:(NSString *)urlStr;
-(NSString*) getPostStringFromDic;
- (void)finishCommunication;

-(void)cancelComm;

@end
