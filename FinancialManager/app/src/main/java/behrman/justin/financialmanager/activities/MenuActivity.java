package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.cardConverters.CardTypeClassConverterViewHistoryImpl;
import behrman.justin.financialmanager.cardConverters.CardTypeIndependentConverterImpl;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;
import behrman.justin.financialmanager.utils.StringConstants;

public class MenuActivity extends AppCompatActivity implements Serializable {

    private final static String LOG_TAG = MenuActivity.class.getSimpleName() + "debug";

    private TextView addManualCardView, addAutoCardView, editCardView, checkHistoryView, addManualTransactionView, deleteCardView, monthlyCalculationView;

    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_menu, null, false);
        setContentView(rootView);
        extractViews();
        initClickListeners();
        initSizes();
    }

    // https://stackoverflow.com/questions/24927575/how-to-make-perfect-square-shaped-image-button
    // https://stackoverflow.com/questions/2963152/how-to-resize-a-custom-view-programmatically
    private void initSizes() {
        if(rootView.getViewTreeObserver().isAlive()){
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout(){
                    int width = rootView.getMeasuredWidth();
                    int height = rootView.getMeasuredHeight();
                    int singleHeight = getNewHeight(height, rootView.getChildCount());
                    for (int i = 0; i < rootView.getChildCount(); ++i) {
                        // getting linear layout
                        ViewGroup btnHolder = (ViewGroup) rootView.getChildAt(i);
                        setChildView(btnHolder, singleHeight, width);
                    }
                    // Destroy the onGlobalLayout afterwards, otherwise it keeps changing
                    // the sizes non-stop, even though it's already done
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void setChildView(ViewGroup btnHolder, int singleHeight, int fullWidth) {
        int singleWidth = fullWidth / btnHolder.getChildCount();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(singleWidth, singleHeight);
        for (int i = 0; i < btnHolder.getChildCount(); ++i) {
            View view = btnHolder.getChildAt(i);
            view.setLayoutParams(params);
        }
    }

    private int getNewHeight(int height, int count) {
        int i;
        Log.i(LOG_TAG, "height: " + height + ", count: " + count);
        for (i = height; i >= 0; --i) {
            if (i % count == 0) {
                return i / count;
            }
        }
        return i;
    }

    private void initClickListeners() {
        initSingleClickListener(addManualCardView, AddManualCardActivity.class);
        initSingleClickListener(addAutoCardView, PlaidActivity.class);
        setUpSelectCardsBtns();
    }

    private void setUpSelectCardsBtns() {
        initTypeDependentClickListener(checkHistoryView, new CardTypeClassConverterViewHistoryImpl());
        initTypeDependentClickListener(editCardView, new CardTypeIndependentConverterImpl(EditCardActivity.class));
        initTypeDependentClickListener(deleteCardView, new CardTypeIndependentConverterImpl(DeleteCardActivity.class));
        initAddManualTransactionBtn();
        initSingleClickListener(monthlyCalculationView, MonthlyCalculationsActivity.class);
    }

    private void initAddManualTransactionBtn() {
        addManualTransactionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SelectCardActivity.class);
                CardTypeIndependentConverterImpl cardConverter = new CardTypeIndependentConverterImpl(AddManualTransactionActivity.class);
                intent.putExtra(StringConstants.NEXT_CLASS_KEY, cardConverter);
                intent.putExtra(StringConstants.CARD_TYPE_KEY, CardType.MANUAL);
                startActivity(intent);
            }
        });

    }

    private void initTypeDependentClickListener(TextView view, final CardTypeClassConverter converter) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SelectCardActivity.class);
                intent.putExtra(StringConstants.NEXT_CLASS_KEY, converter);
                startActivity(intent);
            }
        });
    }

    private void initSingleClickListener(TextView view, final Class<?> classToOpen) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, classToOpen));
            }
        });
    }

    private void extractViews() {
        addManualCardView = findViewById(R.id.add_manual_card_btn);
        addAutoCardView = findViewById(R.id.add_auto_card_btn);
        editCardView = findViewById(R.id.edit_card_btn);
        checkHistoryView = findViewById(R.id.card_history_btn);
        addManualTransactionView = findViewById(R.id.add_manual_transaction_btn);
        deleteCardView = findViewById(R.id.delete_card_btn);
        monthlyCalculationView = findViewById(R.id.monthly_calculations_view);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Log.i("userloggedout", "user was logged out onStop");
        // ParseUser.logOutInBackground();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        // TODO: 10/19/2018 find out when to log out user, if logged out here, then they will be logged out for the rest of the application, also sign them out
        // ParseUser.logOutInBackground();
        super.onBackPressed();
    }
}
