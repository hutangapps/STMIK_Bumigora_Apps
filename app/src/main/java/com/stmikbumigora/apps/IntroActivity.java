package com.stmikbumigora.apps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Intromanager intromanager;
    private PrefManager prefManager;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    Button next, skip;
    ImageView img_btnNext;
    ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;


    public class PrefManager {
        // Shared preferences file name
        private static final String PREF_NAME = "IntroActivity";
        private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        Context _context;
        // shared pref mode
        int PRIVATE_MODE = 0;

        public PrefManager(Context context) {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        public boolean isFirstTimeLaunch() {
            return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        }

        public void setFirstTimeLaunch(boolean isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
            editor.commit();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        prefManager = new PrefManager(this);
//        if (!prefManager.isFirstTimeLaunch()) {
////do something
//            prefManager.setFirstTimeLaunch(false);
//            Intent i = new Intent(IntroActivity.this, SplashActivity.class);
//            startActivity(i);
//            finish();
//        }
        intromanager = new Intromanager(this);

        if (!intromanager.Check()) {
            intromanager.setFirst(false);
            Intent i = new Intent(IntroActivity.this, SplashActivity.class);
            startActivity(i);
            finish();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        skip = (Button) findViewById(R.id.btn_skip);
        next = (Button) findViewById(R.id.btn_next);
        img_btnNext = (ImageView) findViewById(R.id.img_btnNext);
        layouts = new int[]{R.layout.activity_screen1, R.layout.activity_screen2, R.layout.activity_screen3, R.layout.activity_screen4, R.layout.activity_screen5}
        ;

        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intromanager.setFirst(false);
                startActivity(new Intent(IntroActivity.this, SplashActivity.class));
                finish();
            }
        });
        img_btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    startActivity(new Intent(IntroActivity.this, SplashActivity.class));
                    finish();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current > layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    intromanager.setFirst(false);
                    startActivity(new Intent(IntroActivity.this, SplashActivity.class));
                    finish();
                }
            }
        });


    }

    private void addBottomDots(int position) {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorinActive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorinActive[position]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(colorActive[position]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + 1;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                next.setVisibility(View.VISIBLE);
                next.setText("START");
                skip.setVisibility(View.GONE);
                img_btnNext.setVisibility(View.GONE);
            } else {
                next.setVisibility(View.GONE);
                skip.setVisibility(View.VISIBLE);
                img_btnNext.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }
}
