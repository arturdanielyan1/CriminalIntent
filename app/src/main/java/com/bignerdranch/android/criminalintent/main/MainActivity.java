package com.bignerdranch.android.criminalintent.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.crimeDetailsScreen.CrimePagerFragment;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements FragmentLauncher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCrimeListFragment();
    }


    @Override
    public void openCrimeListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, CrimeListFragment.newInstance()).commit();
    }

    @Override
    public void openCrimePagerFragment(UUID crimeId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, CrimePagerFragment.newInstance(crimeId)).commit();
        showUpButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }


    public void showUpButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideUpButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}