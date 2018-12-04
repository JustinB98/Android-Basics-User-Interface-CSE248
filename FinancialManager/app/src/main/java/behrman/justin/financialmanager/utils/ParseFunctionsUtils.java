package behrman.justin.financialmanager.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.RetryHandler;

public final class ParseFunctionsUtils {

    private ParseFunctionsUtils() {}

    public static <T> void callFunctionInBackgroundShowErrorScreen(String functionName, HashMap<String, Object> params, DataCallback<T> callback, Retriable retriable, AppCompatActivity activity, String tag) {
        callFunctionInBackground(functionName, params, callback, true, retriable, activity, tag);
    }

    public static <T> void callFunctionInBackgroundDisplayError(String functionName, HashMap<String, Object> params, DataCallback<T> callback, AppCompatActivity activity, String tag) {
        callFunctionInBackground(functionName, params, callback, false, null, activity, tag);
    }

    public static <T> void callFunctionInBackground(String functionName, HashMap<String, Object> params, final DataCallback<T> callback, final boolean showErrorScreen,
                                             final Retriable retriable, final AppCompatActivity activity, final String tag) {
        ParseCloud.callFunctionInBackground(functionName, params, new FunctionCallback<T>() {
            @Override
            public void done(T object, ParseException e) {
                callback.done(object);
                if (e != null) {
                    Log.i(tag, "e: " + e.toString() + ", code: " + e.getCode());
                    handleError(showErrorScreen, retriable, activity, e);
                }
            }
        });
    }

    public static <T> void callFunctionInBackgroundDisplayError(String functionName, HashMap<String, Object> params, final DataCallback<T> callback, final Context context, final String tag) {
        ParseCloud.callFunctionInBackground(functionName, params, new FunctionCallback<T>() {
            @Override
            public void done(T object, ParseException e) {
                callback.done(object);
                if (e != null) {
                    Log.i(tag, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e, context);
                }
            }
        });
    }

    private static void handleError(boolean showErrorScreen, Retriable retriable, AppCompatActivity activity, ParseException e) {
        if (showErrorScreen) {
            RetryHandler.setToRetryScreen(retriable, activity, e.getCode());
        } else {
            ParseExceptionUtils.displayErrorMessage(e, activity);
        }
    }

    public interface DataCallback<T> {
        void done(T object);
    }

}
