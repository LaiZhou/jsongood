/**
 * 
 */
package com.github.jessyZu.jsongood.core;



public interface RpcRequestDecoder {

    RpcRequest decode(String payload);

}
