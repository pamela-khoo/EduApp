package com.example.eduapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;



public class IntroduceFragment extends Fragment {
    private Button start;
    private ImageButton back,next;
    private static int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_introduce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back = view.findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        next = view.findViewById(R.id.next);
        start = view.findViewById(R.id.startBtn);
        start.setVisibility(View.INVISIBLE);
        start.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_introduceLesson_to_welcomeActivity));

        int colorFilter = getResources().getColor(R.color.white);
        int selectedColorFilter = getResources().getColor(R.color.orange);

        // find views by id
        ViewPager viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new IntroduceLesson());
        adapter.addFrag(new IntroduceQuiz());
        adapter.addFrag(new IntroduceRewards());
        adapter.addFrag(new IntroduceCompetition());

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_circle_24);
        }
        // default choosing first tab
        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColorFilter, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(selectedColorFilter, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;

                // default settings
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);

                // control the display of buttons
                if(position == 0){
                    back.setVisibility(View.INVISIBLE);
                }
                else if(position == adapter.getCount()-1){
                    start.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        back.setOnClickListener(v -> tabLayout.getTabAt(page-1).select());

        next.setOnClickListener(v -> tabLayout.getTabAt(page+1).select());
    }

}






