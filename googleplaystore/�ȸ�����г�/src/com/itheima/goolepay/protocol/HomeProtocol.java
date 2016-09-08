package com.itheima.goolepay.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.goolepay.domain.AppInfo;

/**
 * 网络数据解析
 * @author xielianwu
 *
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	private ArrayList<String> pics;

	@Override
	public String getKey() {
		
		return "home";
	}

	@Override
	public String getParams() {
		
		return "";//若没有参数，就传空串，不要传null
	}

	@Override
	public ArrayList<AppInfo> parseData(String result) {
		JSONObject jo;
		try {
			jo = new JSONObject(result);
			JSONArray ja = jo.getJSONArray("list");
			ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
			for(int i = 0; i < ja.length(); i++){
				
				JSONObject jo1 = ja.getJSONObject(i);
				AppInfo info = new AppInfo();
				info.des = jo1.getString("des");
				info.downloadUrl = jo1.getString("downloadUrl");
				info.iconUrl = jo1.getString("iconUrl");
				info.id = jo1.getString("id");
				info.name = jo1.getString("name");
				info.packageName = jo1.getString("packageName");
				info.size = jo1.getLong("size");
				info.stars = (float) jo1.getDouble("stars");
				appInfos.add(info);
			}
			
			//初始化轮番条的数据
			JSONArray ja1 = jo.getJSONArray("picture");
			pics = new ArrayList<String>();
			for(int i =0 ; i < ja1.length(); i++){
				String pic = ja1.getString(i);
				pics.add(pic);
			}
			
			return appInfos;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return null;
	}

	public ArrayList<String> getPictureList(){
		return pics;
	}
}
