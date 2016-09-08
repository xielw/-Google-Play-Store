package com.itheima.goolepay.ui.adapter;

import java.util.ArrayList;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.ui.holder.BaseHolder;
import com.itheima.goolepay.ui.holder.MoreHolder;
import com.itheima.goolepay.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public abstract class MyBaseAdapter<E> extends BaseAdapter {

	//类型一定要下标从零开始，因为系统getView寻找类型是从零开始
	private static final int TYPE_NORMAL = 1;//正常类型
	private static final int TYPE_MORE = 0;//加载更多类型    

	
	private ArrayList<E> data; 
	public MyBaseAdapter(ArrayList<E> data){
		this.data = data;
	}
	@Override
	public int getCount() {
		
		return data.size() + 1; //增加加载布局更多数量
	}

	@Override
	public E getItem(int position) {
	
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		BaseHolder holder;
		if (convertView == null) {

			// 1.加载布局文件
			// 2.初始化控件 findViewById
			// 3.打一个标记tag
			if (getItemViewType(position) == TYPE_MORE) {
				// 加载更多的类型
				holder = new MoreHolder(hasMore());
			} else {
				holder = getHolder(position); // 子类返回具体对象
			}

		} else {
			holder = (BaseHolder) convertView.getTag();
		}

		// 4.根据数据来刷新界面
		if (getItemViewType(position) != TYPE_MORE) {
			holder.setData(getItem(position));
		} else {
			// 加载更多布局
			// 一旦加载更多布局展示出来，就开始加载更多
			MoreHolder moreHolder = (MoreHolder) holder;
			if(moreHolder.getData() == MoreHolder.STATE_MORE_MORE){
				loadMore(moreHolder);
			}
			
		}

		return holder.getRootView();
	}

	//子类可以重写此方法是否有更多数据
	public boolean hasMore(){
		return true; //默认是true,
	}
	//返回当前位置应该展示哪种布局类型
	@Override
	public int getItemViewType(int position) {
		
		if(getCount() - 1 == position){  //最后一个
			
			return TYPE_MORE;
		}else {
			return getInnerType(position);
		}
	}

	//子类可以重写此方法更改返回的布局类型
	public int getInnerType(int position){
		return TYPE_NORMAL; //默认是普通类型
	}
	
	//返回布局类型个数
	@Override
	public int getViewTypeCount() {
		
		return 2;//返回两种类型，普通布局+加载更多布局
	}
	//返回当前页面的holder对象，必须子类实现
	public abstract BaseHolder getHolder(int position);
	
	private boolean isLoadMore = false; //标记是否正在加载更多
	//加载更多数据
	public void loadMore(final MoreHolder holder){
		if(!isLoadMore){
			isLoadMore = true;
			new Thread(){
				@Override
				public void run() {
					
					final ArrayList<E> moreData = onLoadMore();
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							
							if(moreData != null){
								
								if(moreData.size() < 20){
									//每页有20条，返回小于20条，就到了最后一页
									holder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
								}else {
									//还有更多数据
									holder.setData(MoreHolder.STATE_MORE_MORE);
								}
								
								//将更多数据追加到ListView控件里,并刷新界面
								data.addAll(moreData);
								MyBaseAdapter.this.notifyDataSetChanged();
								
							}else{
								
								//加载更多失败
								holder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							
							isLoadMore = false;
						}
					});
				}
			}.start();
			
		}
		
	}
	
	//加载更多数据，必须由子类实现
	public abstract ArrayList<E> onLoadMore();
	
	//获得列表中item个数
	public int getListSize(){
		return data.size();
	}
}
