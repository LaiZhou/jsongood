/**
 * 
 */
package com.github.jessyZu.jsongood.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.jessyZu.jsongood.util.StringUtils;

public class DefaultRpcServiceHandler implements RpcServiceHandler {
    private final Logger logger = LoggerFactory.getLogger(DefaultRpcServiceHandler.class);

    private RpcInvoker   rpcInvoker;

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

        if (StringUtils.isNotBlank(context.getRpcRequest().getParameters())
                && (rpcInvoker instanceof LocalBeanServiceInvoker)) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            try {
                ArrayNode originalNode = objectMapper.readValue(context.getRpcRequest().getParameters(),
                        ArrayNode.class);
                for (JsonNode value : originalNode) {
                    context.getParameters().add(value.toString());
                }

            } catch (Exception e) {
                logger.error("{}", e);
                result.setWithRpcResultCodeEnum(RpcResultCodeEnum.PARAMETER_ERROR);
                return false;

            }

        }
        context.setAttachments(context.getRpcRequest().getAttachments());
        return true;

    }

}
