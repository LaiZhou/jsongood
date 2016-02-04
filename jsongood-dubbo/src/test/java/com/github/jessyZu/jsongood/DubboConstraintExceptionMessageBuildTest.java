/**
 * 
 */
package com.github.jessyZu.jsongood;

import java.util.Map;

import org.junit.Test;

import com.github.jessyZu.jsongood.dubbo.DubboGenericServiceInvoker;

/**
 *
 */
public class DubboConstraintExceptionMessageBuildTest {

    @Test
    public void test() {
        String messageString = "Failed to invoke the method $invoke in the service com.alibaba.dubbo.rpc.service.GenericService. Tried 1 times of the providers [30.10.233.1:20880] (1/1) from the registry 127.0.0.1:9090 on the consumer 30.10.233.1 using the dubbo version 2.5.3. Last error is: Failed to invoke remote method: $invoke, provider: dubbo://30.10.233.1:20880/com.github.jessyZu.jsongood.demo.DemoService?anyhost=true&application=rapidmobServer&check=false&dubbo=2.5.3&generic=true&interface=com.github.jessyZu.jsongood.demo.DemoService&loadbalance=roundrobin&methods=sayHello1,sayHello&owner=william&pid=63596&retries=0&revision=1.0.0&side=consumer&timestamp=1450840647280&validation=true&version=1.0.0, cause: com.alibaba.dubbo.rpc.RpcException: Failed to validate service: com.github.jessyZu.jsongood.demo.DemoService, method: sayHello1, cause: [ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument0, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}, ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument1, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}]\n"
                +
                "com.alibaba.dubbo.rpc.RpcException: Failed to validate service: com.github.jessyZu.jsongood.demo.DemoService, method: sayHello1, cause: [ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument0, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}, ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument1, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}]\n" + 
                "    at com.alibaba.dubbo.validation.filter.ValidationFilter.invoke(ValidationFilter.java:54)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.ExceptionFilter.invoke(ExceptionFilter.java:64)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.TimeoutFilter.invoke(TimeoutFilter.java:42)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.monitor.support.MonitorFilter.invoke(MonitorFilter.java:75)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.dubbo.filter.TraceFilter.invoke(TraceFilter.java:78)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.ContextFilter.invoke(ContextFilter.java:60)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.GenericFilter.invoke(GenericFilter.java:88)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.ClassLoaderFilter.invoke(ClassLoaderFilter.java:38)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.filter.EchoFilter.invoke(EchoFilter.java:38)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke(ProtocolFilterWrapper.java:91)\n" + 
                "    at com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol$1.reply(DubboProtocol.java:108)\n" + 
                "    at com.alibaba.dubbo.remoting.exchange.support.header.HeaderExchangeHandler.handleRequest(HeaderExchangeHandler.java:84)\n" + 
                "    at com.alibaba.dubbo.remoting.exchange.support.header.HeaderExchangeHandler.received(HeaderExchangeHandler.java:170)\n" + 
                "    at com.alibaba.dubbo.remoting.transport.DecodeHandler.received(DecodeHandler.java:52)\n" + 
                "    at com.alibaba.dubbo.remoting.transport.dispatcher.ChannelEventRunnable.run(ChannelEventRunnable.java:82)\n" + 
                "    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)\n" + 
                "    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\n" + 
                "    at java.lang.Thread.run(Thread.java:745)\n" + 
                "Caused by: javax.validation.ConstraintViolationException: Failed to validate service: com.github.jessyZu.jsongood.demo.DemoService, method: sayHello1, cause: [ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument0, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}, ConstraintViolationImpl{interpolatedMessage='>3', propertyPath=sayHello1Argument1, rootBeanClass=class com.github.jessyZu.jsongood.demo.DemoService$SayHello1Parameter, messageTemplate='>3'}]\n" + 
                "    at com.alibaba.dubbo.validation.support.jvalidation.JValidator.validate(JValidator.java:112)\n" + 
                "    at com.alibaba.dubbo.validation.filter.ValidationFilter.invoke(ValidationFilter.java:49)\n" + 
                "    ... 25 more\n" + 
                "";

        Map<String, String> vaidationResultMap = DubboGenericServiceInvoker.buildValidationResult(messageString);
        System.out.println(vaidationResultMap);
    }
}
