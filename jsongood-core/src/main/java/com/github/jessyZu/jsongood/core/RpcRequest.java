package com.github.jessyZu.jsongood.core;

import java.io.Serializable;
import java.util.Map;

public class RpcRequest implements Serializable {

    /**
	 * 
	 */
    private static final long   serialVersionUID = 4595542829334215640L;

    private String              methodEndPoint;                         // 类名:方法名:服务版本号
    private String              parameters;
    private Map<String, String> attachments;
    private String              requestId;

    public String getMethodEndPoint() {
        return methodEndPoint;
    }

    public void setMethodEndPoint(String methodEndPoint) {
        this.methodEndPoint = methodEndPoint;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RpcRequest [" + (methodEndPoint != null ? "methodEndPoint=" + methodEndPoint + ", " : "")
                + (parameters != null ? "parameters=" + parameters + ", " : "")
                + (attachments != null ? "attachments=" + attachments + ", " : "")
                + (requestId != null ? "requestId=" + requestId : "") + "]";
    }


}
