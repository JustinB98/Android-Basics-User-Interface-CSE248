package behrman.justin.financialmanager.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectUtils {

    public final static String LOG_TAG = ProjectUtils.class.getSimpleName();

    private ProjectUtils() {}

    /**
     *
     * @param date YYYY-MM-DD
     * @return
     */
    public static Date convertToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Trouble parsing date string: " + date);
            return null;
        }

    }

}
