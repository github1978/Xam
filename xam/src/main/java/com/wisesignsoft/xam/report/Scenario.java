package com.wisesignsoft.xam.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 说明:测试场景类，对应一个@TestMethod
 *
 */
public class Scenario {
	
	public static final String[] HEADERS = {
		"场景名称",
		"测试结果",
		"结果证迹"
	};

	private Map<String,String> Context = new HashMap<String,String>();
	
	private List<Map<String,String>> Evidences = new ArrayList<Map<String,String>>();

	public Map<String,String> getContext() {
		return Context;
	}

	public void setContext(Map<String,String> context) {
		Context = context;
	}

	public List<Map<String,String>> getEvidences() {
		return Evidences;
	}

	public void setEvidences(List<Map<String,String>> evidences) {
		Evidences = evidences;
	}
	
}
