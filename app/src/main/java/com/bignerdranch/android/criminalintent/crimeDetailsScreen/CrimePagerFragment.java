package com.bignerdranch.android.criminalintent.crimeDetailsScreen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.main.MainActivity;
import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.crimeStorage.Crime;
import com.bignerdranch.android.criminalintent.crimeStorage.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerFragment extends Fragment {

    private static final String EXTRA_CRIME_ID = "crime_id";

        private ViewPager2 mViewPager;
    private List<Crime> mCrimes;

    public static CrimePagerFragment newInstance(UUID crimeId) {
        CrimePagerFragment fragment = new CrimePagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crime_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        UUID crimeId = (UUID) (getArguments().getSerializable(EXTRA_CRIME_ID));

        mViewPager = view.findViewById(R.id.crime_view_pager);

        mCrimes = CrimeLab.get(getContext()).getCrimes();

        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId(), mViewPager);
            }



            @Override
            public int getItemCount() {
                return mCrimes.size();
            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                Log.d("checking", "onPageScrolled position = " + position + ", positionOffset = " +
//                        positionOffset + ", positionOffsetPixels = " + positionOffsetPixels );
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("checking", "onPageSelected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.d("checking", "onPageScrollStateChanged  " + state);
            }
        });


        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i, false); /** setCurrentItem(i, true) doesn't work without post(Runnable) for first 4 items **/
                break;
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideUpButton();
    }

    public void scrollViewPagerUp(){
        mViewPager.setCurrentItem(0);
    }

    public void scrollViewPagerDown(){
        mViewPager.setCurrentItem(CrimeLab.get(getActivity()).getCrimes().size()-1);
    }
}