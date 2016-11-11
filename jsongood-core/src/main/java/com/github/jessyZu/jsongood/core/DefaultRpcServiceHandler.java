/**
 *
 */
package com.github.jessyZu.jsongood.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jessyZu.jsongood.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultRpcServiceHandler implements RpcServiceHandler {
    private final Logger logger = LoggerFactory.getLogger(DefaultRpcServiceHandler.class);

    private RpcInvoker rpcInvoker;

    public void setRpcInvoker(RpcInvoker rpcInvoker) {
        this.rpcInvoker = rpcInvoker;
    }

    private List<RpcFilter> rpcFilters;

    public void setRpcFilters(List<RpcFilter> rpcFilters) {
        this.rpcFilters = rpcFilters;
    }

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void invoke(RpcContext context, RpcResult result) {
        boolean isRpcContextOK = buildRpcContext(context, result);
        if (isRpcContextOK) {
            //invoke
            boolean isInvokeFinished = false;
            if (rpcFilters != null) {
                for (RpcFilter rpcFilter : rpcFilters) {
                    if (rpcFilter.filter(context, result)) {// invoke流程处理完成
                        isInvokeFinished = true;
                        break;
                    }
                }
            }

            if (!isInvokeFinished) {
                rpcInvoker.invoke(context, result);
            }
        }
    }

    public boolean buildRpcContext(RpcContext context, RpcResult result) {
        String methodEndPoint = context.getRpcRequest().getMethodEndPoint();
        if (StringUtils.isBlank(methodEndPoint)) {
            result.setWithRpcResultCodeEnum(RpcResultCodeEnum.METHOD_ENDPOINT_ERROR);
            return false;

        }
        String[] splitMethodEndPoint = methodEndPoint.split(":");
        if (splitMethodEndPoint.length < 2 || splitMethodEndPoint.length > 3) {
            result.setWithRpcResultCodeEnum(RpcResultCodeEnum.METHOD_ENDPOINT_ERROR);
            return false;
        }
        context.setClassName(splitMethodEndPoint[0]);
        context.setMethodName(splitMethodEndPoint[1]);
        if (splitMethodEndPoint.length == 3) {
            context.setServiceVersion(splitMethodEndPoint[2]);
        }
        return true;

    }

}
