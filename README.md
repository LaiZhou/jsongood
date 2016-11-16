#轻量级的微服务网关jsongood
简单地，上图说明这个轮子的作用:

![img1](./img1.png)


###jsongood是一个简单可扩展的API网关框架，支持客户端透明地调用服务端接口(基于JSON序列化)，可以在此基础上方便地实现一套微服务网关架构。
##网关目前已有功能:
* 可反射调用本地spring bean;
* 可泛化调用远程dubbo service bean;
* RPC模块支持基于声明式validation验证
* 支持自定义Filter，完成业务上的安全验证、协议转换、访问控制、会话管理等需求；
* 目前API暴露方式支持Servlet，见ServletRpcServer，当然也可方便扩展至其他通讯架构比如websocket

##未来计划增加的功能
* IDL CODE生成：提供maven插件，支持从java代码来生成不同客户端的代码
* 提供其他通讯模块集成，比如websocket，socketio等，保持客户端长连接能大幅优化调用开销；
* 集成zipkin框架，追踪全链路调用数据

##服务端使用方式

```

  <!-- jsongood -->
      
        <dependency>
            <groupId>com.github.jessyZu</groupId>
            <artifactId>jsongood-servlet</artifactId>
            <version>1.0.2</version>
        </dependency>

  <!-- validation 可选 -->

  		<dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>4.2.0.Final</version>
        </dependency>

```



```
  @Autowired
    private ServletRpcServer rpcServer;

    @RequestMapping("/gateway")
    void apiGateway(HttpServletRequest request, HttpServletResponse response) throws IOException {
        rpcServer.handle(request, response);
    }


```


##客户端调用方式

###JSON以及JSONP请求
调用代码参考

[https://github.com/jessyZu/jsongood/blob/master/jsongood-demo/src/main/resources/static/test.htm](https://github.com/jessyZu/jsongood/blob/master/jsongood-demo/src/main/resources/static/test.htm)

###android客户端请求

引入依赖

```
<dependency>
    <groupId>com.github.jessyZu</groupId>
    <artifactId>jsongood-android</artifactId>
    <version>1.0.1</version>
</dependency>

```
* 注意:jsongood-android会间接依赖okhttp做网络请求

调用代码参考

[https://github.com/jessyZu/jsongood-android-client/blob/master/jsongood-android/src/androidTest/java/com/github/jessyzu/jsongood/RpcManagerTests.java](https://github.com/jessyZu/jsongood-android-client/blob/master/jsongood-android/src/androidTest/java/com/github/jessyzu/jsongood/RpcManagerTests.java)

```
  manager.invoke("com.github.jessyZu.jsongood.demo.api.DemoService:sayHello1:1.0.0", new Object[]{p1, p2, new Param[]{p1, null}}, new RpcCallback() {
            @Override
            public void success(RpcResult result) {
                if (result.isSuccess()) {
                    Param data = result.dataToObject(Param.class);
                    boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
                    Log.i(RPCConstants.JSONGOOD_LOG_FLAG, data.toString());

                }
                signal.countDown();
            }

            @Override
            public void failure(Request request, Response response, IOException e) {
                signal.countDown();
                fail();
            }


```

###IOS客户端请求

引入依赖

```
    pod 'JsonGood', :git => 'https://github.com/jessyZu/JsonGoodIOS.git', :branch=>'master'

```
* 注意:JsonGood会间接依赖AFNetworking做网络请求

调用代码参考

[https://github.com/jessyZu/jsongood-ios-client/blob/master/JsonGoodDemo/JsonGoodDemoTests/JsonGoodDemoTests.m](https://github.com/jessyZu/jsongood-ios-client/blob/master/JsonGoodDemo/JsonGoodDemoTests/JsonGoodDemoTests.m)


##更新说明
2016.11.16

服务端发布1.0.2版本,变更:

* 优化: 对于dubbo 远程泛化调用场景，在网关初始化时，主动与dubbo注册中心建立长连接,而不是在第一次泛化调用时;
* 优化+fixBug: 对网关的本地spring bean调用进行类型转换增强，借鉴dubbo泛化调用PojoUtils.java从弱类型转化为强类型；

