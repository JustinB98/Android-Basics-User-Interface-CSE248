package behrman.justin.financialmanager.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import behrman.justin.financialmanager.model.CardType;

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

    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertToJSON(String name, Object obj) {
        return "\"" + name + "\":" + "\"" + obj + "\"";
    }

    public static CardType convertToCardType(String cardType) {
        if (cardType.equals(StringConstants.AUTO_CARD_CLASS)) {
            return CardType.AUTO;
        } else if (cardType.equals(StringConstants.MANUAL_CARD_CLASS)) {
            return CardType.MANUAL;
        } else {
            return null;
        }
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String turnToJSONObject(String... objects) {
        // looked at Arrays.toString();
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; ; ++i) {
            sb.append(objects[i]);
            if (i == sb.length() - 1) {
                return sb.append("}").toString();
            }
            sb.append(",");
        }
    }

}
