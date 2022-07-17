package com.bignerdranch.android.criminalintent.main;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bignerdranch.android.criminalintent.crimeStorage.Crime;

import java.util.Date;
import java.util.UUID;

public interface FragmentLauncher {
    void openCrimeListFragment();

    void openCrimePagerFragment(UUID crimeId);

}
