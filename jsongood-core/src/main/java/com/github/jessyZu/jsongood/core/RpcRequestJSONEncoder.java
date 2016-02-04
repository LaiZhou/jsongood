/**
 * 
 */
package com.github.jessyZu.jsongood.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class RpcRequestJSONEncoder implements RpcRequestEncoder {
    private final Logger logger = LoggerFactory.getLogger(RpcRequestJSONEncoder.class);

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String encode(RpcResult result) {
        String rt = null;
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            String payload = objectMapper.writeValueAsString(result);
            rt = encodePayLoadString(payload);
        } catch (Exception e) {
            logger.error("encode RpcResult error:{}", e);
        }
        return rt;
    }

    public String encodePayLoadString(String payload) {
        //子类可以实现
        return payload;
    }
}
