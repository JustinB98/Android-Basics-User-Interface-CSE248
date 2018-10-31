package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.cardConverters.CardTypeClassConverterViewHistoryImpl;
import behrman.justin.financialmanager.cardConverters.CardTypeIndependentConverterImpl;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;
import behrman.justin.financialmanager.utils.StringConstants;

public class MenuActivity extends AppCompatActivity implements Serializable {

    private final static String LOG_TAG = MenuActivity.class.getSimpleName() + "debug";

    private Button addManualCardBtn, addAutoCardBtn, editCardBtn, checkHistoryBtn, addManualTransactionBtn;

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
        setUpSelectCardsBtns();
    }

    private void setUpSelectCardsBtns() {
        initTypeDependentClickListener(checkHistoryBtn, SelectCardActivity.class, new CardTypeClassConverterViewHistoryImpl());
        initTypeDependentClickListener(editCardBtn, SelectCardActivity.class, new CardTypeIndependentConverterImpl(EditCardActivity.class));
        initAddManualTransactionBtn();
    }

    private void initAddManualTransactionBtn() {
        addManualTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SelectCardActivity.class);
                CardTypeIndependentConverterImpl cardConverter = new CardTypeIndependentConverterImpl(AddManualTransactionActivity.class);
                intent.putExtra(StringConstants.NEXT_CLASS, cardConverter);
                intent.putExtra(StringConstants.CARD_TYPE_KEY, CardType.MANUAL);
                startActivity(intent);
            }
        });

    }

    private void initTypeDependentClickListener(Button btn, final Class<?> initialClass, final CardTypeClassConverter converter) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, initialClass);
                intent.putExtra(StringConstants.NEXT_CLASS, converter);
                startActivity(intent);
            }
        });
    }

    private void initSingleClickListener(Button btn, final Class<?> classToOpen) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, classToOpen));
            }
        });
    }

    private void extractViews() {
        addManualCardBtn = findViewById(R.id.add_manual_card_btn);
        addAutoCardBtn = findViewById(R.id.add_auto_card_btn);
        editCardBtn = findViewById(R.id.edit_card_btn);
        checkHistoryBtn = findViewById(R.id.card_history_btn);
        addManualTransactionBtn = findViewById(R.id.add_manual_transaction_btn);
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
