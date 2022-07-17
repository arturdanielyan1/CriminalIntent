package com.bignerdranch.android.criminalintent.crimeStorage;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.*;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String date = getString(getColumnIndex(CrimeTable.Cols.DATE));
        String time = getString(getColumnIndex(CrimeTable.Cols.TIME));
        int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        int requiresPolice = getInt(getColumnIndex(CrimeTable.Cols.REQUIRES_POLICE));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        try {
            crime.setDate(DateFormat.getDateInstance().parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GregorianCalendar timeObj = new GregorianCalendar();

        timeObj.setTimeInMillis(Long.parseLong(time));
        crime.setTime(timeObj.getTime());
        crime.setSolved(solved == 1);
        crime.setRequiresPolice(requiresPolice == 1);
        crime.setSuspect(suspect);

        return crime;
    }
}
