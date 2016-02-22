package com.github.jessyZu.jsongood.demo.api;

import java.util.List;

import com.github.jessyZu.jsongood.core.RpcContext;

public class DemoServiceImpl implements DemoService {

    public Param sayHello1(Param param1, Param param2, List<Param> params) {
        System.out.println("param1:" + param1.toString() + "param2:" + param2.toString() + "params:"
                + params.toString()
                + " rpcContext" + RpcContext.getContext());
        return param1;
    }

    public void sayHello2(String param1, String param2) {
        System.out.println("param:" + param1 + "," + param2 + "rpcContext:" + RpcContext.getContext());
    }

}
