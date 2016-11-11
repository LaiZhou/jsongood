package com.github.jessyZu.jsongood.demo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Param  extends BaseParam{

	private int intParam;
	private double doubleParam;
	private String stringParam;
	private List<String> listParam = new ArrayList<String>();
	private Map<String, String> mapParam = new HashMap<String, String>();
	private Param param;

	public int getIntParam() {
		return intParam;
	}

	public void setIntParam(int intParam) {
		this.intParam = intParam;
	}

	public double getDoubleParam() {
		return doubleParam;
	}

	public void setDoubleParam(double doubleParam) {
		this.doubleParam = doubleParam;
	}

	public String getStringParam() {
		return stringParam;
	}

	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	public List<String> getListParam() {
		return listParam;
	}

	public void setListParam(List<String> listParam) {
		this.listParam = listParam;
	}

	public Map<String, String> getMapParam() {
		return mapParam;
	}

	public void setMapParam(Map<String, String> mapParam) {
		this.mapParam = mapParam;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "Param [intParam=" + intParam + ", doubleParam=" + doubleParam
				+ ", stringParam=" + stringParam + ", listParam=" + listParam
				+ ", mapParam=" + mapParam + ", param=" + param + "]";
	}

}
