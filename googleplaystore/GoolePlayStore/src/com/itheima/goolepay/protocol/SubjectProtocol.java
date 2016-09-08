package com.itheima.goolepay.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.goolepay.domain.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

	private ArrayList<SubjectInfo> subjectInfos;

	@Override
	public String getKey() {
	   
		return "subject";
	}

	@Override
	public String getParams() {
		
		return "";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String result) {
		JSONArray ja;
		try {
			ja = new JSONArray(result);
			subjectInfos = new ArrayList<SubjectInfo>();
			for(int i = 0;i < ja.length(); i++){
				
				JSONObject jo = ja.getJSONObject(i);
				SubjectInfo si = new SubjectInfo(); 
				si.des = jo.getString("des");
				si.url = jo.getString("url");
				subjectInfos.add(si);
				
			}
			
			return subjectInfos;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	
		return null;
	}

}
