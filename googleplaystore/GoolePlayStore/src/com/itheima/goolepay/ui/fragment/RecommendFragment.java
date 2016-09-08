package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.itheima.goolepay.protocol.RecommendProtocol;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.ui.view.fly.ShakeListener;
import com.itheima.goolepay.ui.view.fly.ShakeListener.OnShakeListener;
import com.itheima.goolepay.ui.view.fly.StellarMap;
import com.itheima.goolepay.utils.UIUtils;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 推荐
 * @author xielianwu
 *
 */
public class RecommendFragment extends BaseFragment {

	private ArrayList<String> keyWords;

	@Override
	public View onCreateSuccessView() {
		
		final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
		stellarMap.setAdapter(new RecommendAdapter());
		//随机的方式,9行6列的格子，然后在格子中随机展示
		stellarMap.setRegularity(6, 9);
		int padding = UIUtils.dip2px(10);
		stellarMap.setPadding(padding, padding, padding, padding);
		//设置默认页面，第一组数据
		stellarMap.setGroup(0, true);
		ShakeListener shake = new ShakeListener(UIUtils.getContext());
		shake.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				
				stellarMap.zoomIn();//跳到下一页
			}
		});
		
		return stellarMap;
	}

	@Override
	public ResultSate onLoad() {
		RecommendProtocol rp = new RecommendProtocol();
		keyWords = rp.getData(0);
		return check(keyWords);
	}

	public class RecommendAdapter implements StellarMap.Adapter{

		//返回组的个数
		@Override
		public int getGroupCount() {
			
			
			
			return 2;
		}

		//返回某组item个数
		@Override
		public int getCount(int group) {
		 
			int count = keyWords.size() / getGroupCount();
			if(group == getGroupCount() -1){
				//最后一页，将除不尽，余下来的数量追加在最后一页，保证数据的完整不丢失
				count += keyWords.size() % getGroupCount();
			}
			return count;
		}

		//出始化布局
		@Override
		public View getView(int group, int position, View convertView) {
			
			//因为position每组都会从0开始计数,所以需要将前面几组数据的个数加起来，才能确定当前数据的角标位置
			position = position + getCount(group - 1) * group;
			TextView view = new TextView(UIUtils.getContext());
			Random random = new Random();
			//随机大小, 16 - 25
			int size = 16 + random.nextInt(10);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			
			//随机颜色值: 30 - 230,颜色值不能太小或者太大，避免颜色过亮过暗
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
			view.setTextColor(Color.rgb(r, g, b));
			final String keyWord = keyWords.get(position);
			view.setText(keyWord);
			//设置监听
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Toast.makeText(UIUtils.getContext(), keyWord, Toast.LENGTH_SHORT).show();
				}
			});
			return view;
		}

		//返回下组id
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			
			if(isZoomIn){
				//往下滑，加载上一页数据
				if(group > 0){
					group --;
				}else{
					group = getGroupCount() -1;
				}
			}else {
				//往上滑，加载下一页数据
				if(group < getGroupCount() - 1){
					group ++;
				}else {
					group = 0;
				}
			}
			return group;
		}
		
	}
}
