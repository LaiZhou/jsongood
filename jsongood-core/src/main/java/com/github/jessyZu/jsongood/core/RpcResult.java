/**
 * 
 */
package com.github.jessyZu.jsongood.core;

import java.io.Serializable;

public class RpcResult implements Serializable {

	private static final long serialVersionUID = -2940045837162445419L;

	private int code;
	private String message;
	private Object data;


	public RpcResult() {
		super();

	}

	public RpcResult(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

    public boolean isSuccess() {
        return this.code == RpcResultCodeEnum.SUCCESS.getCode();
    }

    public boolean isValid() {
        return this.code != RpcResultCodeEnum.VALIDATION_ERROR.getCode();
    }
	@Override
	public String toString() {
		return "RpcResult [code=" + code + ", message=" + message + ", data="
				+ data + "]";
	}

	public void setWithRpcResultCodeEnum(RpcResultCodeEnum error) {
		this.setCode(error.getCode());
		this.setMessage(error.getMessage());
	}
	
}
