package behrman.justin.financialmanager.popups;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.interfaces.DateCallback;
import behrman.justin.financialmanager.utils.ProjectUtils;

public class SelectDatePopup extends PopupWindow {

    private Spinner monthSpinner;
    private EditText yearField;
    private Button incrementBtn, decrementBtn, selectDateBtn;
    private TextView closeBtn;

    private View layout;

    private DateCallback callback;

    private final int MIN_YEAR = 1970; // maybe I want to change it later

    public SelectDatePopup(Context context, DateCallback callback, int width, int height) {
        super(context);
        setFocusable(true); // makes it so that if the background activity is pressed, then the pop up dismisses
        setWidth(width);
        setHeight(height);
        initBackground(context);
        this.callback = callback;
        layout = LayoutInflater.from(context).inflate(R.layout.select_date_popup, null);
        extractViews();
        initBtnClicks();
        initDefaultFields();
        setContentView(layout);
    }

    private void initBackground(Context context) {
        ColorDrawable backgroundColor = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
        backgroundColor.setAlpha(220);
        setBackgroundDrawable(backgroundColor);
    }

    private void initDefaultFields() {
        int month = ProjectUtils.getCurrentMonth();
        int year = ProjectUtils.getCurrentYear();
        monthSpinner.setSelection(month - 1);
        yearField.setText(year + "");
    }

    private void initBtnClicks() {
        initIncrementBtnClick();
        initDecrementBtnClick();
        initSelectDateBtnClick();
        initCloseBtnClick();
    }

    private void initCloseBtnClick() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initSelectDateBtnClick() {
        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = monthSpinner.getSelectedItemPosition() + 1;
                int year = ProjectUtils.parseOrDefault(yearField, -1);
                year = constrainMin(year, MIN_YEAR);
                callback.callback(month, year);
                dismiss();
            }
        });
    }

    private void initIncrementBtnClick() {
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setYearFieldText(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer param) {
                        return param + 1;
                    }
                });
            }
        });
    }

    private void initDecrementBtnClick() {
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setYearFieldText(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer param) {
                        return param - 1;
                    }
                });
            }
        });
    }

    private void setYearFieldText(Function<Integer, Integer> function) {
        int year = ProjectUtils.parseOrDefault(yearField, -1);
        int result = function.apply(year);
        result = constrainMin(result, 0);
        yearField.setText(result + "");
    }

    private int constrainMin(int value, int min) {
        if (value < min) {
            return min;
        }
        return value;
    }

    private void extractViews() {
        monthSpinner = layout.findViewById(R.id.month_spinner);
        yearField = layout.findViewById(R.id.year_field);
        incrementBtn = layout.findViewById(R.id.increment_btn);
        decrementBtn = layout.findViewById(R.id.decrement_btn);
        selectDateBtn = layout.findViewById(R.id.select_date_btn);
        closeBtn = layout.findViewById(R.id.close_btn);
    }

    // ugh had to create my own because apparently it takes api 24 to call java.util.Function.apply for some reason
    private interface Function<T, K> {
        K apply(T param);
    }

}
