package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import behrman.justin.financialmanager.R;

public class MonthlyCalculationsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button actionBtn;
    private EditText yearField;
    private Spinner monthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_calculations);
        extractViews();

    }

    private void extractViews() {
        progressBar = findViewById(R.id.progress_bar);
        actionBtn = findViewById(R.id.action_btn);
        yearField = findViewById(R.id.year_field);
        monthSpinner = findViewById(R.id.month_spinner);
    }

}
