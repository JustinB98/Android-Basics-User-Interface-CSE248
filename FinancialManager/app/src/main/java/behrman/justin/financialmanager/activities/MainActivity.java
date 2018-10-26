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

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.InputStreamCallBack;
import behrman.justin.financialmanager.utils.NetworkUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

// https://stackoverflow.com/questions/4905315/error-connection-refused
public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName() + "logs";

    private EditText usernameField, passwordField;
    private Button loginBtn;
    private TextView createAccountView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initParse(); // only for main activity;
        initViews();
        initClickables();
        initThreadLog();
        // Log.i("currentuser", ParseUser.getCurrentUser().toString());
        // initTest();
    }

    private void initThreadLog() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.i("errorlog", e.toString());
                e.printStackTrace();
            }
        });
    }

    private void initTest() {
        final String tag = "parseobjecttesting";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ManualCards");
        query.whereEqualTo("name", "justin");
        query.whereEqualTo("owner", "");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i(tag, "trying to get the user");
                    Log.i(tag, "objects len: " + objects.size());
                    for (ParseObject obj : objects) {
                        Log.i(tag, "object: " + obj.getObjectId());
                        // Log.i(tag, "owner: " + obj.get("owner").toString());
                        // Log.i(tag, "owner 1: " + obj.getParseObject("owner"));
                        // Log.i(tag, "owner 2: " + obj.getJSONObject("owner").toString());
                        // ParseUser user = obj.getParseUser("owner");
                        // Log.i(tag, "got the user " + (user == null ? "null" : user.toString()));
                        // String email = user.getEmail();
                        // String username = user.getUsername();
                        // Log.i(tag, "user info: " + user.getEmail() + " " + user.getUsername());
                    }
                } else {
                    Log.i(tag, "Error Code: " + e.getCode());
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
                switchToCreateAccountIntent();
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
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                if (e == null) {
                    goToMainMenu();
                } else {
                    showErrorMsg(e.getCode());
                }
            }
        });

    }

    // TODO: 10/17/2018 work on what could cause the login to fail besides poor connection and invalid credentials
    private void showErrorMsg(int errorCode) {
        String msg = null;
        Toast.makeText(this, "sign in failed!!!!", Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG, "sign in failed!");
    }

    private void initViews() {
        initWritableViews();
        loginBtn = (Button) findViewById(R.id.login_btn);
        createAccountView = (TextView) findViewById(R.id.create_account);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    private void initWritableViews() {
        usernameField = (EditText) findViewById(R.id.username_input);
        passwordField = (EditText) findViewById(R.id.password_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        updateUIIfNeeded(currentUser);
        passwordField.setText("");
    }

    private void goToMainMenu() {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    private void updateUIIfNeeded(ParseUser currentUser) {
        if (currentUser != null) {
            Log.i("jsonresponse", "starting");
            NetworkUtils.makePostRequest(StringConstants.SERVER_URL + "usertest", "{\"email\": " + "\"" +  currentUser.getUsername() +"\",\"sessionToken\":\"" + currentUser.getSessionToken() + "\"}", new InputStreamCallBack() {
                @Override
                public void callback(InputStream is) {
                    Log.i("jsonresponse", "got in callback");
                    try {
                        String s = NetworkUtils.readFromStream(is);
                        Log.i("jsonresponse", "the response was " + s);
                    } catch (IOException e) {
                        Log.i("jsonresponse", "error", e);
                    }
                }
            });

            goToMainMenu();
        }
    }

}
