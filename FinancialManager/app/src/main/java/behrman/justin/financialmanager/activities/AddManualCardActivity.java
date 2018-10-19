package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import behrman.justin.financialmanager.R;

public class AddManualCardActivity extends AppCompatActivity {

    private EditText cardNameField;
    private Button addCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_card);
        extractViews();
        initClickListener();
    }
    
    private void initClickListener() {
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 10/7/2018 connect with server to ensure it's a unique id then add it to the server list

                finish();
            }
        });
    }

    private void extractViews() {
        cardNameField = (EditText) findViewById(R.id.card_name);
        addCardBtn = (Button) findViewById(R.id.add_card_btn);
    }

}
