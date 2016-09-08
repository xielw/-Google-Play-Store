package com.itheima.goolepay.domain;

import java.util.ArrayList;

public class AppInfo {	
	public String des;
	public String downloadUrl;
	public String iconUrl;
	public String id;
	public String name;
	public String packageName;
	public long size;
	public float stars;
	
	//补充字段，供应用祥情页面使用
	public String author;
	public String date;
	public String downloadNum;
	public String version;
	public ArrayList<SafeInfos> safe;
	public ArrayList<String> screen;
	
	public static class SafeInfos{
		public String safeDes;
		public String safeDesUrl;
		public String safeUrl;
		@Override
		public String toString() {
			return "SafeInfos [safeDes=" + safeDes + ", safeDesUrl="
					+ safeDesUrl + ", safeUrl=" + safeUrl + "]";
		}
		
		
	}
	
	
}
