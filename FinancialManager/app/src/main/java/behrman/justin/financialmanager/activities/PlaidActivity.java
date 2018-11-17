package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

/**
 * Most of the code DID NOT come from me. They are from Plaid. </br>
 * For more details:
 * <a href="https://github.com/plaid/link/blob/master/webviews/examples/android/app/src/main/java/plaid/samplewebviewapp/MainActivity.java">
 *     https://github.com/plaid/link/blob/master/webviews/examples/android/app/src/main/java/plaid/samplewebviewapp/MainActivity.java</a>
 */
public class PlaidActivity extends AppCompatActivity {

    private final String LOG_TAG = PlaidActivity.class.getSimpleName() + "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaid);
        getSupportActionBar().hide();
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        // Initialize Link
        HashMap<String, String> linkInitializeOptions = new HashMap<String,String>();
        linkInitializeOptions.put("key", "f5ee4cfa87e37d902d493ab70169aa");
        linkInitializeOptions.put("product", "auth");
        linkInitializeOptions.put("apiVersion", "v2"); // set this to "v1" if using the legacy Plaid API
        linkInitializeOptions.put("env", "sandbox");
        linkInitializeOptions.put("clientName", "Financial App");
        linkInitializeOptions.put("selectAccount", "true");
        linkInitializeOptions.put("webhook", "http://requestb.in");
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");
        // If initializing Link in PATCH / update mode, also provide the public_token
        //linkInitializeOptions.put("token", "PUBLIC_TOKEN");

        // Generate the Link initialization URL based off of the configuration options.
        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializeOptions);

        Log.v("url", linkInitializationUrl.toString());

        // Modify Webview settings - all of these settings may not be applicable
        // or necesscary for your integration.
        final WebView plaidLinkWebview = findViewById(R.id.plaid_webview);
        WebSettings webSettings = plaidLinkWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        WebView.setWebContentsDebuggingEnabled(true);

        // Initialize Link by loading the Link initiaization URL in the Webview
        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());

        // Override the Webview's handler for redirects
        // Link communicates success and failure (analogous to the web's onSuccess and onExit
        // callbacks) via redirects.
        Log.v("initwebclient", "starting");
        plaidLinkWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v("url loading", "should override url loading " + url);
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                if (parsedUri.getScheme().equals("plaidlink")) {
                    String action = parsedUri.getHost();
                    HashMap<String, String> linkData = parseLinkUriData(parsedUri);

                    if (action.equals("connected")) {
                       onConnected(linkData);
                    } else if (action.equals("exit")) {
                        onExit(linkData);
                        plaidLinkWebview.loadUrl(linkInitializationUrl.toString());
                    } else {
                        Log.d("Link action detected: ", action);
                    }
                    // Override URL loading
                    return true;
                } else if (parsedUri.getScheme().equals("https") ||
                        parsedUri.getScheme().equals("http")) {
                    // Open in browser - this is most  typically for 'account locked' or
                    // 'forgotten password' redirects
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // Override URL loading
                    return true;
                } else {
                    // Unknown case - do not override URL loading
                    return false;
                }


            }

        });
    }

    private void onExit(HashMap<String, String> linkData) {
        // User exited
        // linkData may contain information about the user's status in the Link flow,
        // the institution selected, information about any error encountered,
        // and relevant API request IDs.
        // Log.d("User status in flow: ", linkData.get("status"));
        // The requet ID keys may or may not exist depending on when the user exited
        // the Link flow.
        // Log.d("Link request ID: ", linkData.get("link_request_id"));
        // Log.d("API request ID: ", linkData.get("plaid_api_request_id"));

        // Reload Link in the Webview
        // You will likely want to transition the view at this point.

        // Intent intent = new Intent(PlaidActivity.this, MenuActivity.class);
        // intent.putExtra("public_token", "");
        // startActivity(intent);
        Log.i(LOG_TAG, "onExit() called, finishing activity");
        finish();
    }

    private void onConnected(HashMap<String, String> linkData) {
        // User successfully linked
        // Log.d("Public token: ", msgOrDefault(linkData.get("public_token")));
        // Log.d("Account ID: ", msgOrDefault(linkData.get("account_id")));
        // Log.d("Account name: ", msgOrDefault(linkData.get("account_name")));
        // Log.d("Institution type: ", msgOrDefault(linkData.get("institution_type")));
        // Log.d("Institution name: ", msgOrDefault(linkData.get("institution_name")));

        // Reload Link in the Webview
        // You will likely want to transition the view at this point.
        // plaidLinkWebview.loadUrl(linkInitializationUrl.toString());
        String publicToken = linkData.get("public_token");
        String name = linkData.get("account_name");
        String suggestedName = linkData.get("institution_name") + " " + name;
        HashMap<String, Object> params = new HashMap<>(2);
        params.put(StringConstants.CARD_NAME_KEY, suggestedName);
        params.put(StringConstants.AUTO_CARD_PUBLIC_TOKEN_COLUMN, publicToken);
        publicToken = null; // get rid of public token reference asap
        linkData.put("public_token", null);
        saveCardToDataBase(params, suggestedName);
    }

    private void saveCardToDataBase(HashMap<String, Object> params, final String suggestedName) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_ADD_AUTO_CARD, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null) {
                    if (object != null && ProjectUtils.deepEquals(object, StringConstants.SUCCESS)) {
                        finish();
                        switchToChangeName(suggestedName);
                    }
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e, PlaidActivity.this);
                }
            }
        });
    }

    private void switchToChangeName(String name) {
        Card card = new Card(name, CardType.AUTO);
        Intent intent = new Intent(this, EditCardActivity.class);
        intent.putExtra(StringConstants.CARD_KEY, card);
        startActivity(intent);
    }


    // Generate a Link initialization URL based on a set of configuration options
    public Uri generateLinkInitializationUrl(HashMap<String,String> linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }

    // Parse a Link redirect URL querystring into a HashMap for easy manipulation and access
    public HashMap<String,String> parseLinkUriData(Uri linkUri) {
        HashMap<String,String> linkData = new HashMap<String,String>();
        for(String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }

    private String msgOrDefault(String msg) {
        return msg == null ? "null" : msg;
    }
}
