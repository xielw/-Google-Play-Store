package com.itheima.goolepay.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.goolepay.domain.CategoryInfo;

/**
 * 分类模块请求网络
 * @author xielianwu
 *
 */
public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

	@Override
	public String getKey() {
		
		return "category";
	}

	@Override
	public String getParams() {
	
		return "";
	}

	@Override
	public ArrayList<CategoryInfo> parseData(String result) {
		
		try {
			ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
			JSONArray ja = new JSONArray(result);
			for(int i = 0; i < ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				if(jo.has("title")){
					CategoryInfo titleInfo = new CategoryInfo();
					titleInfo.title = jo.getString("title");
					titleInfo.isTitle = true;
					list.add(titleInfo);
				}
				
				if(jo.has("infos")){
					JSONArray ja1 = jo.getJSONArray("infos");
					for(int j = 0; j < ja1.length(); j++){
						JSONObject jo2 = ja1.getJSONObject(j);
						CategoryInfo info = new CategoryInfo();
						info.name1 = jo2.getString("name1");
						info.name2 = jo2.getString("name2");
						info.name3 = jo2.getString("name3");
						info.url1 = jo2.getString("url1");
						info.url2 = jo2.getString("url2");
						info.url3 = jo2.getString("url3");
						info.isTitle = false;
						list.add(info);
					}
				}
				
			}
			
			return list;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

}
