package com.soleeklab.nilebot.features.home.pond.addPond;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo.AddPondFragmentTwo;
import com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne.AddPondFragmentOne;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPondFragment extends ParentFragment {


    FragmentPagerAdapter adapterViewPager;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.lay_top)
    RelativeLayout layTop;
    @BindView(R.id.view_paget)
    ViewPager viewPaget;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    Unbinder unbinder;


    @Inject
    public AddPondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pond, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        viewPaget.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPaget);

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }


    }

    public void setFragTwo() {
        viewPaget.setCurrentItem(1);
    }

    public void setFragOne() {
        viewPaget.setCurrentItem(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        if (viewPaget.getCurrentItem() == 0) {
            getActivity().onBackPressed();
        } else {
            viewPaget.setCurrentItem(0);
        }
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new AddPondFragmentOne();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new AddPondFragmentTwo();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

    }
}
