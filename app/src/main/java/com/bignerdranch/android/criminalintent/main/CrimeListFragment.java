package com.bignerdranch.android.criminalintent.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.crimeStorage.Crime;
import com.bignerdranch.android.criminalintent.crimeStorage.CrimeLab;

import java.util.List;

public class CrimeListFragment extends Fragment {

    //ViewHolder
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private ImageView mSolvedImageView;
        private Button mCallPoliceButton;
        private int position;
        private Crime mCrime;

        public void bind(Crime crime, int position) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDateAsString());
            mTimeTextView.setText(crime.getTimeAsString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            this.position = position;

            if(!crime.isSolved()) {
                mCallPoliceButton.setVisibility(crime.isRequiresPolice() ? View.VISIBLE : View.GONE);
            }else{
                mCallPoliceButton.setVisibility(View.GONE);
            }
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent/*, int layout*/){
            super(inflater.inflate(R.layout.item_crime, parent, false));//?

            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date_button);
            mTimeTextView = itemView.findViewById(R.id.crime_time_tv);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved_img);
            mCallPoliceButton = itemView.findViewById(R.id.call_police);
            mCallPoliceButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"));
                startActivity(intent);
            });
        }

        @Override
        public void onClick(View v) {
            mLauncher.openCrimePagerFragment(mCrime.getId());
        }
    }


    //Adapter
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//called when RecyclerView needs to create a ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //int layout = (viewType == 0) ? R.layout.list_item_crime : R.layout.list_item_serious_crime;
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder/*?*/, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            //return mCrimes.get(position).isRequiresPolice() ? 1:0;
            return 0;
        }
    }

    //This Fragment

    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mAdapter;
    private FragmentLauncher mLauncher;
    private static boolean mSubtitleVisible;
    private LinearLayout mEmptyListLL;
    private Button mCreateCrimeButton;


    public static CrimeListFragment newInstance() {
        return new CrimeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crime_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCrimeRecycleView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager places elements in vertical order

        mEmptyListLL = view.findViewById(R.id.list_empty_ll);
        mCreateCrimeButton = view.findViewById(R.id.create_crime_button);



        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mCrimeRecycleView.setVisibility(View.INVISIBLE);

        if(crimes.size() == 0) {
            mEmptyListLL.setVisibility(View.VISIBLE);
            mCreateCrimeButton.setOnClickListener(v -> {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                mLauncher.openCrimePagerFragment(crime.getId());
            });

        } else {

            if(mAdapter == null) { //works only on first crime add
                mAdapter = new CrimeAdapter(crimes);
            }

            mAdapter.setCrimes(crimes);
            mCrimeRecycleView.setVisibility(View.VISIBLE);
            mEmptyListLL.setVisibility(View.GONE);
            mCrimeRecycleView.setAdapter(mAdapter);
        }



        updateSubtitle();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLauncher = (MainActivity) getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLauncher = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater); // not necessarily
        menu.clear(); /** without this it creates multiply buttons on every screen rotate. why? **/
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime: {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                mLauncher.openCrimePagerFragment(crime.getId());
                return true;
            }
            case R.id.show_subtitle: {
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_format, crimeCount, crimeCount);

        if(!mSubtitleVisible) {
            subtitle = null;
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setSubtitle(subtitle);
    }
}