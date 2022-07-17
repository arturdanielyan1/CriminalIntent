package com.bignerdranch.android.criminalintent.crimeDetailsScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.crimeStorage.Crime;
import com.bignerdranch.android.criminalintent.crimeStorage.CrimeLab;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleET;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mRequiresPoliceCheckBox;
    private Button mFirstCrimeButton;
    private Button mLastCrimeButton;
    private Button mReportButton;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    static final String REQUEST_DATE_KEY = "request_date";
    static final String REQUEST_TIME_KEY = "request_time";

    public static CrimeFragment newInstance(UUID crimeId, ViewPager2 viewPager) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
//        mViewPager = viewPager;

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("checking", "onCreate CrimeFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crime, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        //GETTING TITLE TEXT FIELD AND SETTING LISTENER ON IT
        mTitleET = view.findViewById(R.id.crime_title);
        mTitleET.setText(mCrime.getTitle());
        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        //GETTING DATE BUTTON
        mDateButton = view.findViewById(R.id.crime_date_button);
        mDateButton.setText(mCrime.getDateAsString());
        mDateButton.setOnClickListener(v -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            manager.setFragmentResultListener(REQUEST_DATE_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if(requestKey == REQUEST_DATE_KEY){
                        Date date = (Date) result.getSerializable(REQUEST_DATE_KEY);
                        mCrime.setDate(date);
                        mDateButton.setText(mCrime.getDateAsString());
                    }
                }
            });
            dialog.show(manager, DIALOG_DATE);
        });

        //GETTING TIME BUTTON
        mTimeButton = view.findViewById(R.id.crime_time);
        mTimeButton.setText(mCrime.getTimeAsString());
        mTimeButton.setOnClickListener(v -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getTime());
            manager.setFragmentResultListener(REQUEST_TIME_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if(requestKey == REQUEST_TIME_KEY){
                        Date time = (Date) result.getSerializable(REQUEST_TIME_KEY);
                        mCrime.setTime(time);
                        mTimeButton.setText(mCrime.getTimeAsString());
                    }
                }
            });
            dialog.show(manager, DIALOG_DATE);
        });

        //GETTING SOLVED CHECKBOX AND SETTING LISTENER ON IT
        mSolvedCheckBox = view.findViewById(R.id.crime_solved_img);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                mRequiresPoliceCheckBox.setChecked(false);
                if(!isChecked){
                    mRequiresPoliceCheckBox.setVisibility(View.VISIBLE);
                }else {
                    mRequiresPoliceCheckBox.setVisibility(View.INVISIBLE);
                }
            }
        });

        mRequiresPoliceCheckBox = view.findViewById(R.id.serious_crime);
        mRequiresPoliceCheckBox.setVisibility(View.GONE);
        if(!mCrime.isSolved()) {
            mRequiresPoliceCheckBox.setVisibility(View.VISIBLE);
        }
        mRequiresPoliceCheckBox.setChecked(mCrime.isRequiresPolice());

        mRequiresPoliceCheckBox.setChecked(mCrime.isRequiresPolice());
        mRequiresPoliceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setRequiresPolice(isChecked);
            }
        });


        //GETTING FIRST, LAST CRIME BUTTONS
        mFirstCrimeButton = view.findViewById(R.id.first_crime_button);
        mFirstCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimePagerFragment)getParentFragment()).scrollViewPagerUp();
            }
        });

        mLastCrimeButton = view.findViewById(R.id.last_crime_button);
        mLastCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimePagerFragment)getParentFragment()).scrollViewPagerDown();
            }
        });

        mFirstCrimeButton.setEnabled(true);
        mLastCrimeButton.setEnabled(true);

        if(CrimeLab.get(getContext()).getCrimePosition(mCrime) == 0){
            mFirstCrimeButton.setEnabled(false);
        }
        if (CrimeLab.get(getContext()).getCrimePosition(mCrime) == CrimeLab.get(getContext()).getCrimes().size() - 1){
            mLastCrimeButton.setEnabled(false);
        }

        mReportButton = view.findViewById(R.id.send_report_button);
        mReportButton.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            startActivity(i);
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("checking", "onPause: ");/** not called when dialog fragment is opened**/
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater); // not necessarily
        menu.clear(); /** without this it creates multiple buttons on every screen rotate.**/
        Log.d("checking", "on menu create crime fragment");/** called on edittext focus? **/
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime: {
                CrimeLab crimeLab = CrimeLab.get(getContext());
                crimeLab.deleteCrime(mCrime);
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private String getCrimeReport() {
        String solved = mCrime.isSolved() ? getString(R.string.crime_report_solved) : getString(R.string.crime_report_unsolved);
        String date = mCrime.getDateAsString();
        String time = mCrime.getTimeAsString();
        String suspect = mCrime.getSuspect();
        suspect = (suspect==null) ? getString(R.string.crime_report_no_suspect) : getString(R.string.crime_report_suspect, suspect);

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), date, time, solved, suspect);

        return report;
    }
}