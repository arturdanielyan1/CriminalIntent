package com.bignerdranch.android.criminalintent.crimeStorage;


import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Crime {
    private static final HashMap<String, String> weekdays = new HashMap<>();

    static {
        weekdays.put("Mon", "Monday");
        weekdays.put("Tue", "Tuesday");
        weekdays.put("Wed", "Wednesday");
        weekdays.put("Thu", "Thursday");
        weekdays.put("Fri", "Friday");
        weekdays.put("Sat", "Saturday");
        weekdays.put("Sun", "Sunday");
    }

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice;
    private Date mTime;
    private String mSuspect;

    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
        mTime = new GregorianCalendar().getTime();
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
        mTime = new GregorianCalendar().getTime();
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getTimeAsString() {
        return mTime.toString().substring(11,16);
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDateAsString(){
        String date = mDate.toString();
        String weekday = weekdays.get(date.substring(0,3));
        String monthDay = date.substring(4,10);
        String year = date.substring(date.length()-4);
        return weekday + ", " + monthDay + ", " + year;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        return mId.equals(crime.mId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }
}
