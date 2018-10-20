package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import behrman.justin.financialmanager.R;

public class MenuActivity extends AppCompatActivity {

    private Button addManualCardBtn, addAutoCardBtn, editCardBtn, checkHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        extractViews();
        initClickListeners();
    }

    private void initClickListeners() {
        initSingleClickListener(addManualCardBtn, AddManualCardActivity.class);
        initSingleClickListener(addAutoCardBtn, PlaidActivity.class);
    }

    private void initSingleClickListener(Button btn, final Class<?> classToOpen) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, classToOpen));
            }
        });
    }

    private void extractViews() {
        addManualCardBtn = (Button) findViewById(R.id.add_manual_card_btn);
        addAutoCardBtn = (Button) findViewById(R.id.add_auto_card_btn);
        editCardBtn = (Button) findViewById(R.id.edit_card_btn);
        checkHistoryBtn = (Button) findViewById(R.id.card_history_btn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Log.i("userloggedout", "user was logged out onStop");
        // ParseUser.logOutInBackground();
    }


    @Override
    public void onBackPressed() {
        // TODO: 10/19/2018 find out when to log out user, if logged out here, then they will be logged out for the rest of the application, also sign them out
        // ParseUser.logOutInBackground();
        super.onBackPressed();
    }
}
