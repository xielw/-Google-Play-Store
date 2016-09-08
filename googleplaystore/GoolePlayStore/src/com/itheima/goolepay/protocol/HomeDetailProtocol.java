package com.itheima.goolepay.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.domain.AppInfo.SafeInfos;

/**
 * 首页祥情页网访问
 * @author xielianwu
 *
 */
public class HomeDetailProtocol extends BaseProtocol<AppInfo>{

	private String packageName;
	public HomeDetailProtocol(String packageName){
		this.packageName = packageName;
	}
	@Override
	public String getKey() {
		
		return "detail";
	}

	@Override
	public String getParams() {
	
		return "&packageName=" + packageName;
	}

	//补充字段，供应用祥情页面使用
		public String author;
		public String date;
		public String downloadNum;
		public String version;
		ArrayList<SafeInfos> safe;
		ArrayList<String> screen;
		
	@Override
	public AppInfo parseData(String result) {
		try {
			
			JSONObject jo = new JSONObject(result);
			AppInfo info = new AppInfo();
			info.des = jo.getString("des");
			info.downloadUrl = jo.getString("downloadUrl");
			info.iconUrl = jo.getString("iconUrl");
			info.id = jo.getString("id");
			info.name = jo.getString("name");
			info.packageName = jo.getString("packageName");
			info.size = jo.getLong("size");
			info.stars = (float) jo.getDouble("stars");
			info.author = jo.getString("author");
			info.date = jo.getString("date");
			info.downloadNum = jo.getString("downloadNum");
			info.version = jo.getString("version");
			
			JSONArray ja = jo.getJSONArray("safe");
			ArrayList<SafeInfos> safe = new ArrayList<AppInfo.SafeInfos>();
			for(int i = 0; i < ja.length(); i++){
				JSONObject jo1 = ja.getJSONObject(i);
				SafeInfos safts = new SafeInfos();
				safts.safeDes=jo1.getString("safeDes");
				safts.safeDesUrl=jo1.getString("safeDesUrl");
				safts.safeUrl=jo1.getString("safeUrl");
				safe.add(safts);
				
			}
			info.safe = safe;
			JSONArray ja1 = jo.getJSONArray("screen");
			ArrayList<String> screen = new ArrayList<String>();
			for(int j = 0; j < ja1.length(); j++){
				screen.add(ja1.getString(j));
			}
			info.screen = screen;
			
			return info;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

}
