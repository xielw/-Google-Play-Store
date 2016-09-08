package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;

import com.itheima.goolepay.domain.CategoryInfo;
import com.itheima.goolepay.protocol.CategoryProtocol;
import com.itheima.goolepay.ui.adapter.MyBaseAdapter;
import com.itheima.goolepay.ui.holder.BaseHolder;
import com.itheima.goolepay.ui.holder.CategoryHolder;
import com.itheima.goolepay.ui.holder.TitleHolder;
import com.itheima.goolepay.ui.view.MyListView;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.utils.UIUtils;

import android.view.View;


/**
 * 分类
 * @author xielianwu
 *
 */
public class CategoryFragment extends BaseFragment {

	private ArrayList<CategoryInfo> data;

	@Override
	public View onCreateSuccessView() {
		
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new CategoryAdapter(data));
		return listView;
	}

	@Override
	public ResultSate onLoad() {
		
		CategoryProtocol cp = new CategoryProtocol();
		data = cp.getData(0);
		return check(data);
	}

	class CategoryAdapter extends MyBaseAdapter<CategoryInfo>{

		public CategoryAdapter(ArrayList<CategoryInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder getHolder(int position) {
			//判断是标题类型还是普通类型，来返回不同的holder
			CategoryInfo info = data.get(position);
			if(info.isTitle){
				//返回标题类型holder
				System.out.println("---返回标题类型holder");
				return new TitleHolder();
			}else{
				System.out.println("---返回普通类型holder");
				//返回普通类型holder
				return new CategoryHolder();
			}
			
			
		}

		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			
			return null;
		}
		
		@Override
		public boolean hasMore() {
			
			return false;
		}
		
		@Override
		public int getInnerType(int position) {
			
			CategoryInfo info = data.get(position);
			if(info.isTitle){
				//返回标题类型
				return super.getInnerType(position) + 1;//原来类型基础上加1;注意:
			
			}else {
				//返回普通类型
				return super.getInnerType(position);
			}
			
		}
		
		@Override
		public int getViewTypeCount() {
		
			return super.getViewTypeCount() + 1;
		}
	}
}
