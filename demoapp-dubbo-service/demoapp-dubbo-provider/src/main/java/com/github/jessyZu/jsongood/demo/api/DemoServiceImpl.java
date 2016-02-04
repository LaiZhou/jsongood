package com.github.jessyZu.jsongood.demo.api;

import com.alibaba.dubbo.rpc.RpcContext;

public class DemoServiceImpl implements DemoService {

    public Param sayHello1(Param param1, Param param2) {
		System.out.println("param1:" + param1.toString() + "param2:"
				+ param2.toString() + " rpcContext"
				+ RpcContext.getContext());
		return param1;
	}

    public String sayHello2(String param, String param2) {
        System.out.println("param:" + param + " rpcContext" + RpcContext.getContext());
        return param;
    }
}