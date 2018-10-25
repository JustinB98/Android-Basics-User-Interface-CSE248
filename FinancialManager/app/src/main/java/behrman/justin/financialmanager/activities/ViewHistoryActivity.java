package behrman.justin.financialmanager.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewHistoryActivity extends AppCompatActivity {

    private String cardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        cardName = getIntent().getStringExtra(StringConstants.CARD_NAME);

    }
}
