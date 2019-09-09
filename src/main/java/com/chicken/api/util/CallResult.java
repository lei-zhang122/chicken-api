package com.chicken.api.util;

import java.io.Serializable;

public class CallResult<T> implements Serializable{
	
	private int code;
	
	private String message;
	
	private T result;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CallResult(int code, String message, T result){
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public static <T>CallResult<T> success() {
		return new CallResult<T>(CodeEnum.DEFAULT_SUCCESS.getCode(), CodeEnum.DEFAULT_SUCCESS.getMsg(), null);
	}

	public static <T>CallResult<T> success(T result) {
		return new CallResult<T>(CodeEnum.DEFAULT_SUCCESS.getCode(), CodeEnum.DEFAULT_SUCCESS.getMsg(), result);
	}

	public static <T>CallResult<T> fail() {
		return new CallResult<T>(CodeEnum.DEFAULT_SYS_ERROR.getCode(), CodeEnum.DEFAULT_SYS_ERROR.getMsg(), null);
	}
	
	public static <T>CallResult<T> fail(int code, String message) {
		return new CallResult<T>(code, message, null);
	}

	public static <T>CallResult<T> fail(String message) {
		return new CallResult<T>(1, message, null);
	}
	
	public  boolean isSuccess(){
		return this.code == CodeEnum.DEFAULT_SUCCESS.getCode();
	}
}
