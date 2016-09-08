package com.itheima.goolepay.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.itheima.goolepay.R;
import com.itheima.goolepay.ui.fragment.BaseFragment;
import com.itheima.goolepay.ui.fragment.FragmentFactory;
import com.itheima.goolepay.ui.view.PagerTab;
import com.itheima.goolepay.utils.UIUtils;

/**
 * 当项目和appcompat关联时，就必须在清单文件中设置Theme.Appcompat,否则出现bug
 * @author xielianwu
 *
 */
public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
	private PagerTab mPagerTab;
	private ActionBarDrawerToggle toggle;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerTab = (PagerTab) findViewById(R.id.pager_tab);
      
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mPagerTab.setViewPager(mViewPager);  //将指示器与viewpager绑定在一起
        
        mPagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				BaseFragment fragment = FragmentFactory.createFragment(position);
				//开始加载数据
				fragment.loadData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			
				
			}
		});
        initActionBar();
    }
    
	
	
	private void initActionBar() {
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("谷歌电子市场");
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am, R.string.drawer_open, R.string.drawer_close);
		toggle.syncState();
	}



	// FragmentPagerAdpater 是ViewPager的子类，如果ViewPager的页面是fragment的话,就使用此类
	class MyAdapter extends FragmentPagerAdapter{

		private String[] tabNames;

		public MyAdapter(FragmentManager fm) {
			super(fm);
		
			tabNames = UIUtils.getStringArray(R.array.tab_names);
			
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			return tabNames[position];
			
		}

		@Override
		public Fragment getItem(int position) {
			
			return FragmentFactory.createFragment(position);
			
		}

		@Override
		public int getCount() {
			
			return tabNames.length;
		}
		
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
}
