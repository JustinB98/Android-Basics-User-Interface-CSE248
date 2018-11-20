package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.cardConverters.CardTypeClassConverterViewHistoryImpl;
import behrman.justin.financialmanager.cardConverters.CardTypeIndependentConverterImpl;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class MenuActivity extends AppCompatActivity implements Serializable {

    private final static String LOG_TAG = MenuActivity.class.getSimpleName() + "debug";

    private TextView addManualCardView, addAutoCardView, editCardView, checkHistoryView, addManualTransactionView, deleteCardView, monthlyCalculationView;

    private ViewGroup rootView;

    private boolean signingOut;

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
                    initAnimations();
                    // Destroy the onGlobalLayout afterwards, otherwise it keeps changing
                    // the sizes non-stop, even though it's already done
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void initAnimations() {
        ArrayList<View> children = new ArrayList<>(rootView.getChildCount() * 2);
        for (int i = 0; i < rootView.getChildCount(); ++i) {
            ViewGroup row = (ViewGroup) rootView.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); ++j) {
                View view = row.getChildAt(j);
                Log.i("errorlog", "setting scale to 0");
                view.setScaleX(0);
                view.setScaleY(0);
                children.add(view);
            }
        }
        startAnimations(children, 0);
    }

    private Animation getAnimation(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bouncer);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setScaleX(1);
                view.setScaleY(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("errorlog", "setting view back to scale");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }

    private void startAnimations(final ArrayList<View> children, final int index) {
        if (index >= children.size()) {
            return;
        }
        View view = children.get(index);
        Animation bounceAnim = getAnimation(view);
        view.startAnimation(bounceAnim);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimations(children, index + 1);
            }
        }, 150);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void menuAction(MenuItem item) {
        if (item.getItemId() == R.id.log_out_menu_item && !signingOut) {
            signingOut = true;
            logout();
        } else if (item.getItemId() == R.id.settings_item) {
            switchToSettings();
        }
    }

    private void switchToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                signingOut = false;
                if (e == null) {
                    finish();
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e, MenuActivity.this);
                }
            }
        });
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
                if (!signingOut) {
                    Intent intent = new Intent(MenuActivity.this, SelectCardActivity.class);
                    CardTypeIndependentConverterImpl cardConverter = new CardTypeIndependentConverterImpl(AddManualTransactionActivity.class);
                    intent.putExtra(StringConstants.NEXT_CLASS_KEY, cardConverter);
                    intent.putExtra(StringConstants.CARD_TYPE_KEY, CardType.MANUAL);
                    startActivity(intent);
                } else {
                    Toast.makeText(MenuActivity.this, R.string.cant_do_that_while_logging_out, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initTypeDependentClickListener(TextView view, final CardTypeClassConverter converter) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signingOut) {
                    Intent intent = new Intent(MenuActivity.this, SelectCardActivity.class);
                    intent.putExtra(StringConstants.NEXT_CLASS_KEY, converter);
                    startActivity(intent);
                } else {
                    Toast.makeText(MenuActivity.this, R.string.cant_do_that_while_logging_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSingleClickListener(TextView view, final Class<?> classToOpen) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!signingOut) {
                    startActivity(new Intent(MenuActivity.this, classToOpen));
                } else {
                    Toast.makeText(MenuActivity.this, R.string.cant_do_that_while_logging_out, Toast.LENGTH_SHORT).show();
                }
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
        ParseUser.logOutInBackground();
        super.onBackPressed();
    }
}
