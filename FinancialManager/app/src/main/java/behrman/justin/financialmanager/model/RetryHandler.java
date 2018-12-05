package behrman.justin.financialmanager.model;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.interfaces.Retriable;

public class RetryHandler {

    private RetryHandler() {}

    public static void setToRetryScreen(Retriable retriable, AppCompatActivity activity, int code) {
        activity.setContentView(R.layout.retry);
        setViews(activity, code, retriable);
    }

    public static void setToRetryScreen(Retriable retriable, AppCompatActivity activity) {
        activity.setContentView(R.layout.retry);
        TextView codeView = activity.findViewById(R.id.error_code_view);
        codeView.setVisibility(View.GONE);
        setViews(activity, 0, retriable); // code can be anything
    }

    private static void setViews(AppCompatActivity activity, int code, Retriable retriable) {
        Button btn = activity.findViewById(R.id.retry_btn);
        btn.setOnClickListener(getOnClickListener(retriable));
        TextView errorView = activity.findViewById(R.id.error_code_view);
        setErrorView(activity, errorView, code);
    }

    private static void setErrorView(Context context, TextView errorView, int code) {
        if (code > 0 || code == -1) { // if positive or -1
            errorView.setText(context.getString(R.string.error_code_display, code));
        } else {
            errorView.setVisibility(View.GONE);
        }
    }

    private static View.OnClickListener getOnClickListener(final Retriable retriable) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retriable.retry();
            }
        };
    }

}
