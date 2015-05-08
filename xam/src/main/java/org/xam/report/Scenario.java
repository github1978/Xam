package org.xam.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 测试场景实体模型
 * @author 朱晓峰
 * @testerhome umbrella1978
 * @email umbrella1978@live.cn
 * @github github1978
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
