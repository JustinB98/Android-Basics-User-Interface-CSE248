package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

// https://stackoverflow.com/questions/4905315/error-connection-refused
public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName() + "debug";

    private EditText usernameField, passwordField;
    private Button loginBtn;
    private TextView createAccountView;
    private ProgressBar progressBar;

    private boolean loggingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initParse(); // only for main activity;
        initViews();
        initClickables();
        initThreadLog();
    }

    private void initThreadLog() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                String errorLogTag = "errorlog";
                Log.i(errorLogTag, "Msg: " + e.toString());
                for (int i = 0; i < e.getStackTrace().length; ++i) {
                    Log.i(errorLogTag, e.getStackTrace()[i].toString()); // want it on different lines
                }
            }
        });
    }

    // https://guides.codepath.com/android/Building-Data-driven-Apps-with-Parse
    private void initParse() {
        OkHttpClient.Builder builder = initParseDebugging();
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dYphmVT5CsMdSAo6Kpfr") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server(StringConstants.PARSE_URL).build());
        // initSubRegisters();
    }

    private void initSubRegisters() {
        // ParseObject.registerSubclass(Transaction.class);
    }

    private OkHttpClient.Builder initParseDebugging() {
        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        return builder;
    }

    private void initClickables() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        createAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loggingIn) {
                    switchToCreateAccountIntent();
                }
            }
        });
    }

    private void switchToCreateAccountIntent() {
        Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void login() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        authenticate(username, password);
    }

    private void authenticate(String username, String password) {
        boolean oneEmpty = checkIfEmpty(username, R.string.username_empty, false);
        oneEmpty |= checkIfEmpty(password, R.string.password_empty, oneEmpty);
        if (!oneEmpty) {
            sendCredentials(username, password);
        }
    }

    // just don't want two toasts showing
    private boolean checkIfEmpty(String s, int msgIfEmptyId, boolean toastHappening) {
        // if the string is empty and there's no toast happening, making a toast then return true
        // to show that there was a toast
        if (s.trim().isEmpty() && !toastHappening) {
            Toast.makeText(MainActivity.this, msgIfEmptyId, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void sendCredentials(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        ProjectUtils.hideKeyboard(this);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(username);
        user.setPassword(password);
        loggingIn = true;
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                loggingIn = false;
                if (e == null) {
                    goToMainMenu();
                } else {
                    showErrorMsg(e);
                }
            }
        });

    }

    private void showErrorMsg(ParseException e) {
        // Toast.makeText(this, R.string.sign_in_fail, Toast.LENGTH_LONG).show();
        if (e.getCode() == ParseException.OBJECT_NOT_FOUND) { // invalid username/password
            Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.other_login_error, Toast.LENGTH_SHORT).show();
        }
        Log.i(LOG_TAG, "sign in failed! message: " + e.toString() + ", code: " + e.getCode());
    }

    private void initViews() {
        initWritableViews();
        loginBtn = findViewById(R.id.login_btn);
        createAccountView = findViewById(R.id.create_account);
        progressBar = findViewById(R.id.login_progress);
    }

    private void initWritableViews() {
        usernameField = findViewById(R.id.username_input);
        passwordField = findViewById(R.id.password_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProjectUtils.setContext(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        updateUIIfNeeded(currentUser);
        passwordField.setText("");
    }

    private void goToMainMenu() {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    private void updateUIIfNeeded(ParseUser currentUser) {
        if (currentUser != null) {
            goToMainMenu();
        }
    }

}
