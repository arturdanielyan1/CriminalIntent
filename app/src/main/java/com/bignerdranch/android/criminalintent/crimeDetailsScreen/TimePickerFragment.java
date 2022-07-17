package com.bignerdranch.android.criminalintent.crimeDetailsScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;
    private Button mConfirmTimeButton;
    private Button mCancelButton;

    public static TimePickerFragment newInstance(Date time) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        //GETTING CONFIRM TIME BUTTON
        mConfirmTimeButton = view.findViewById(R.id.confirm_time_button);
        mConfirmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                Date time = new GregorianCalendar(0,0,0,hour, minute).getTime();
                sendResult(CrimeFragment.REQUEST_TIME_KEY, time);
            }
        });

        mCancelButton = view.findViewById(R.id.cancel_time_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(TimePickerFragment.this).commit();
            }
        });
    }

    private void sendResult(String resultKey, Date time){
        Bundle bundle = new Bundle();
        bundle.putSerializable(CrimeFragment.REQUEST_TIME_KEY, time);
        getActivity().getSupportFragmentManager().setFragmentResult(resultKey, bundle);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
