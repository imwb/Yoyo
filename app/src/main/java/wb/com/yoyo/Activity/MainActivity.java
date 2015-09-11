package wb.com.yoyo.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;

import wb.com.yoyo.Activity.ActivitySupport;
import wb.com.yoyo.Fragment.ContacterFragment;
import wb.com.yoyo.Fragment.FindFragment;
import wb.com.yoyo.Fragment.RecentlyContacterFragment;
import wb.com.yoyo.Fragment.SetFragment;
import wb.com.yoyo.R;


public class MainActivity extends ActivitySupport {

    private ViewPager viewPager;
    private Button btn_rect;
    private Button btn_contact;
    private Button btn_find;
    private Button btn_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        initViewPger();
    }

    private void initTab() {
        btn_rect= (Button) findViewById(R.id.btn_recent);
        btn_contact= (Button) findViewById(R.id.btn_contacts);
        btn_find= (Button) findViewById(R.id.btn_find);
        btn_set= (Button) findViewById(R.id.btn_set);

        btn_rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                changeState(0);
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                changeState(1);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                changeState(2);
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                changeState(3);
            }
        });

    }

    private void initViewPger() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //加载页数
        viewPager.setOffscreenPageLimit(3);

        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new RecentlyContacterFragment());
        fragments.add(new ContacterFragment());
        fragments.add(new FindFragment());
        fragments.add(new SetFragment());

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void changeState(int pos){
        switch(pos){
            case 0:
                btn_rect.setBackgroundResource(R.drawable.icon_meassage_sel);
                btn_contact.setBackgroundResource(R.drawable.icon_selfinfo_nor);
                btn_find.setBackgroundResource(R.drawable.icon_square_nor);
                btn_set.setBackgroundResource(R.drawable.icon_more_nor);
                break;
            case 1:
                btn_rect.setBackgroundResource(R.drawable.icon_meassage_nor);
                btn_contact.setBackgroundResource(R.drawable.icon_selfinfo_sel);
                btn_find.setBackgroundResource(R.drawable.icon_square_nor);
                btn_set.setBackgroundResource(R.drawable.icon_more_nor);
                break;
            case 2:
                btn_rect.setBackgroundResource(R.drawable.icon_meassage_nor);
                btn_contact.setBackgroundResource(R.drawable.icon_selfinfo_nor);
                btn_find.setBackgroundResource(R.drawable.icon_square_sel);
                btn_set.setBackgroundResource(R.drawable.icon_more_nor);
                break;
            case 3:
                btn_rect.setBackgroundResource(R.drawable.icon_meassage_nor);
                btn_contact.setBackgroundResource(R.drawable.icon_selfinfo_nor);
                btn_find.setBackgroundResource(R.drawable.icon_square_nor);
                btn_set.setBackgroundResource(R.drawable.icon_more_sel);
                break;
        }
    }

    /**
     * 退出
     */
    @Override
    public void onBackPressed() {
        isExit();
    }
}
class MyAdapter extends FragmentPagerAdapter{

    List<Fragment> fragments;
    public MyAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        fragments=list;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
