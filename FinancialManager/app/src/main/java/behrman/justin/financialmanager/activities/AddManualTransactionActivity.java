package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import behrman.justin.financialmanager.R;

public class AddManualTransactionActivity extends AppCompatActivity {

    private View transactionContainer;
    private Button addTransactionBtn;
    private EditText placeField, amountField, currencyField;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_transaction);
        extractViews();
        initButton();
    }

    private void initButton() {
        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    private void extractViews() {
        transactionContainer = findViewById(R.id.transaction_container);
        addTransactionBtn = findViewById(R.id.add_manual_transaction_btn);
        placeField = findViewById(R.id.place_input);
        amountField = findViewById(R.id.amount_input);
        currencyField = findViewById(R.id.currency_input);
        calendarView = findViewById(R.id.calendar_input);
    }

}
