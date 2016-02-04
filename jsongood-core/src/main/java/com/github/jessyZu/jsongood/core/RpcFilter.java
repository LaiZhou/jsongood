/**
 * 
 */
package com.github.jessyZu.jsongood.core;


public interface RpcFilter {

	/**
	 * 
	 * @param rpcContext
	 * @param rpcResult
	 * @return true:invoke流程结束，false：继续invoke流程
	 */
    boolean filter(RpcContext rpcContext, RpcResult rpcResult);
}
