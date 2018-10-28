package behrman.justin.financialmanager.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    protected DatePicker datePicker;

    protected String cardName;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        cardName = getIntent().getStringExtra(StringConstants.CARD_NAME);
        setContentView(R.layout.activity_view_history);
        extractViews();
    }

    private void extractViews() {
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public void setToLoadView() {
        datePicker.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setToDateView() {
        datePicker.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
