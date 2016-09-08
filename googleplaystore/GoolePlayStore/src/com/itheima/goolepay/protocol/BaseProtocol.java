package com.itheima.goolepay.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import android.R.integer;

import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.http.HttpHelper.HttpResult;
import com.itheima.goolepay.utils.IOUtils;
import com.itheima.goolepay.utils.StringUtils;
import com.itheima.goolepay.utils.UIUtils;

/**
 * 访问网络的基类
 * 
 * @author xielianwu
 * 
 */
public abstract class BaseProtocol<E> {

	public E getData(int index) {
		// 先判断是否有缓存，有的话就加载缓存
		String result = getCache(index);
		if (StringUtils.isEmpty(result)) { // 若没有缓存，或者缓存失效
			// 请求服务器
			result = getDataFromServer(index);
		}

		// 开始解析数据
		if (result != null) {
			E data = parseData(result);
			return data;
		}
		return null;
	}

	// 从网络获取数据
	// index表示是从哪个位置开始返回20条数据，用于分页
	private String getDataFromServer(int index) {

		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
				+ "?index=" + index + getParams());
		if (httpResult != null) {
			String result = httpResult.getString();
			System.out.println("访问结果:" + result);

			// 写缓存
			if (result != null) {
				setCache(index, result);
			}
			return result;
		}
		return null;
	}

	// 获取网络链接关键词，子类必须实现
	public abstract String getKey();

	// 获取网络链接参数，子类必须实现
	public abstract String getParams();

	// 解析数据
	public abstract E parseData(String result);

	// 写缓存
	// Url为key,以json为value
	public void setCache(int index, String json) {
		// 以url为文件名，以json为文件内容，保存在本地
		File cacheDir = UIUtils.getContext().getCacheDir();
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index
				+ getParams());
		FileWriter writer = null;
		try {
			writer = new FileWriter(cacheFile);
			long deadtime = System.currentTimeMillis() + 30 * 60 * 1000; // 小时后失效
			writer.write(deadtime + "\n"); // 第一行写入缓存时间，换行
			writer.write(json); // 写入json
			writer.flush();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
	}

	// 读缓存
	public String getCache(int index) {

		// 以url为文件名，以json为文件内容，保存在本地
		File cacheDir = UIUtils.getContext().getCacheDir();
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index
				+ getParams());

		BufferedReader reader = null;
		if (cacheFile.exists()) {
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				String deadline = reader.readLine();
				long deadtime = Long.parseLong(deadline);
				if (System.currentTimeMillis() < deadtime) { // 当前时间小于截至时间，说明缓存有效

					// 缓存有效
					StringBuffer buffer = new StringBuffer();
					String line = null;
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
					return buffer.toString();
				}
			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				IOUtils.close(reader);
			}

		}
		return null;
	}
}
