package behrman.justin.financialmanager.utils;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;

import behrman.justin.financialmanager.R;

/**
 * For general errors such as connection loss. NOT for specific activity errors, unless they only appear once, such as username taken, but not
 * for such cases that can describe a specific error message to a general error, such as object not found. This is more for like scripting errors, connection failed, timeout, etc.
 */
public class ParseExceptionUtils {

    public static int returnParseExceptionMessage(ParseException e) {
        return returnParseExceptionMessage(e.getCode());
    }

    public static int returnParseExceptionMessage(int code) {
        switch (code) {
            case ParseException.CONNECTION_FAILED:
                return R.string.connection_failed;
            case ParseException.TIMEOUT:
                return R.string.timeout_msg;
            case ParseException.OTHER_CAUSE:
                return R.string.error_msg;
            case ParseException.EMAIL_TAKEN:
            case ParseException.USERNAME_TAKEN:
                return R.string.username_taken;
            default:
                return R.string.error_msg;

        }
    }

    public static void displayErrorMessage(ParseException e, Context context) {
        displayErrorMessage(e.getCode(), context);
    }

    public static void displayErrorMessage(int code, Context context) {
        int errorId = returnParseExceptionMessage(code);
        Toast.makeText(context, errorId, Toast.LENGTH_SHORT).show();
    }

}
