package com.github.jessyZu.jsongood.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jessyZu.jsongood.util.StringUtils;

public class RpcRequestJSONDecoder implements RpcRequestDecoder {

    private final Logger logger = LoggerFactory.getLogger(RpcRequestJSONDecoder.class);

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RpcRequest decode(String payload) {
        RpcRequest rpcRequest = null;
        try {
            if (StringUtils.isNotBlank(payload)) {
                String realPayload = decodePayLoadString(payload);
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }

                ObjectNode jNode = (ObjectNode) objectMapper.readValue(realPayload, JsonNode.class);

                rpcRequest = new RpcRequest();
                rpcRequest.setMethodEndPoint(jNode.get("methodEndPoint").asText());
                ArrayNode parametersArrayNode = ((ArrayNode) jNode.get("parameters"));
                if (parametersArrayNode != null) {
                    rpcRequest.setParameters(parametersArrayNode.toString());

                }
                JsonNode attachmentsNode = jNode.get("attachments");
                if (attachmentsNode != null) {
                    Map attachmentsMap = objectMapper.readValue(jNode.get("attachments").toString(), Map.class);
                    if (attachmentsMap != null) {
                        rpcRequest.setAttachments(attachmentsMap);

                    }
                }
                rpcRequest.setRequestId(jNode.get("requestId").asText());

                logger.debug("decode RpcRequest:{}", rpcRequest);

            }
        } catch (Exception e) {
            logger.error("decode RpcRequest error:{}", e);
            rpcRequest = null;
        }

        return rpcRequest;

    }

    public String decodePayLoadString(String payLoad) {
        return payLoad;
    }
}
