/**
 * 
 */
package com.github.jessyZu.jsongood.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jessyZu.jsongood.core.RpcContext;
import com.github.jessyZu.jsongood.core.RpcRequest;
import com.github.jessyZu.jsongood.core.RpcRequestDecoder;
import com.github.jessyZu.jsongood.core.RpcRequestEncoder;
import com.github.jessyZu.jsongood.core.RpcRequestJSONDecoder;
import com.github.jessyZu.jsongood.core.RpcRequestJSONEncoder;
import com.github.jessyZu.jsongood.core.RpcResult;
import com.github.jessyZu.jsongood.core.RpcResultCodeEnum;
import com.github.jessyZu.jsongood.core.RpcServiceHandler;


public class ServletRpcServer {

    private final Logger        logger             = LoggerFactory.getLogger(ServletRpcServer.class);
    private static final String JSONP_CALLBACK_KEY = "callback";

    private RpcServiceHandler   rpcServiceHandler;

    public void setRpcServiceHandler(RpcServiceHandler rpcServiceHandler) {
        this.rpcServiceHandler = rpcServiceHandler;
    }

    private RpcRequestDecoder rpcRequestDecoder;

    public void setRpcRequestDecoder(RpcRequestDecoder rpcRequestDecoder) {
        this.rpcRequestDecoder = rpcRequestDecoder;
    }

    private RpcRequestEncoder rpcRequestEncoder;

    public void setRpcRequestEncoder(RpcRequestEncoder rpcRequestEncoder) {
        this.rpcRequestEncoder = rpcRequestEncoder;
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest req) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = req.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("request httpHeaders:{}", getHeadersInfo(req));
        RpcContext context = RpcContext.getContext();
        RpcRequest rpcRequest = null;
        RpcResult rpcResult = new RpcResult();
        try {
            String payload = null;
            //get payload
            boolean isJSONPrequest = req.getMethod().equals("GET") && req.getParameter(JSONP_CALLBACK_KEY) != null
                    && req.getParameter("content") != null;
            if (isJSONPrequest) {
                context.setJsonpCallback(req.getParameter(JSONP_CALLBACK_KEY));
                payload = URLDecoder.decode(req.getParameter("content"), "UTF-8");
            } else {
                payload = IOUtils.toString(req.getInputStream());
            }

            if (rpcRequestDecoder == null) {
                rpcRequestDecoder = new RpcRequestJSONDecoder();
            }
            rpcRequest = rpcRequestDecoder.decode(payload);
            if (rpcRequest == null) {
                logger.error("decode rpcRequest null");
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.DECODE_ERROR);//TODO 可以在decode里细化DECODE_ERROR,抛出异常
            } else {
                context.setRpcRequest(rpcRequest);
                rpcServiceHandler.invoke(context, rpcResult);
            }
        } catch (Throwable e) {
            rpcResult.setCode(RpcResultCodeEnum.SYSTEM_ERROR.getCode());
            rpcResult.setMessage(e.getMessage());
            logger.error("{}", e);
        } finally {
            RpcContext.removeContext();
            writeAndFlushResponse(resp, context, rpcResult);
        }

    }

    private void writeAndFlushResponse(HttpServletResponse resp, RpcContext context, RpcResult rpcResult)
            throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        if (rpcRequestEncoder == null) {
            rpcRequestEncoder = new RpcRequestJSONEncoder();
        }
        String result = rpcRequestEncoder.encode(rpcResult);

        if (result == null) {
            rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.ENCODE_ERROR);
            rpcResult.setData(null);
            result = rpcRequestEncoder.encode(rpcResult);
        }
        if (context.isJSONPRequest()) {
            result = new StringBuilder(context.getJsonpCallback()).append("(").append(result).append(");").toString();
        }

        resp.getOutputStream().write(result.getBytes("UTF-8"));
        resp.getOutputStream().flush();

    }

}
