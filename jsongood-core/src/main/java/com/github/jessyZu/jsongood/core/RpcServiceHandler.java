/**
 * 
 */
package com.github.jessyZu.jsongood.core;


public interface RpcServiceHandler {

    void invoke(RpcContext rpcContext, RpcResult rpcResult);
}
