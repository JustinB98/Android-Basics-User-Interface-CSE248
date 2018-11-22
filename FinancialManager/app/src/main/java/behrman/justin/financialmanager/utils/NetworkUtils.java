package behrman.justin.financialmanager.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

import behrman.justin.financialmanager.interfaces.InputStreamCallBack;

public class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() {}

    public static String getJSONSting(String strUrl, String key, String token) {

        URL url = createURL(strUrl);

        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url, 10000, 15000, key, token);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't make http request", e);
        }

        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createURL(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }
    }

    // --------------------- START GET REQUEST ---------------------

    public static void makeGetRequest(final String urlLink, final InputStreamCallBack callback) {
        new AsyncTask<String, Void, InputStream>() {
            @Override
            protected InputStream doInBackground(String... strings) {
                return makeGetRequestAsync(urlLink);
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                if (callback != null) {
                    callback.callback(inputStream);
                }
            }
        }.execute();
    }

    public static InputStream makeGetRequestAsync(String urlLink) {
        Log.i(LOG_TAG, "Making a get request to " + urlLink);
        HttpURLConnection urlConnection = makeGetRequest0(urlLink);
        if (urlConnection != null) {
            try {
                int responseCode = urlConnection.getResponseCode();
                Log.i(LOG_TAG, "response code: " + responseCode);
                if (responseCode / 100 == 2) {
                    Log.i(LOG_TAG, "Got input!");
                    return urlConnection.getInputStream();
                } else {
                    Log.e(LOG_TAG, "Invalid response code, aborting...");
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't connect to to " + urlLink, e);
            }
        }
        disconnect(urlConnection);
        Log.i(LOG_TAG, "urlConnection: " + urlConnection);
        return null;
    }

    private static HttpURLConnection makeGetRequest0(String urlLink) {
        URL url = createURL(urlLink);
        if (url != null) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                makeGetRequest1(urlConnection);
                return urlConnection;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't open url connection", e);
            }
        }
        return null;
    }

    private static void makeGetRequest1(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        Log.i(LOG_TAG, "Connecting to site via GET...");
    }
    // --------------------- END GET REQUEST ---------------------

    // --------------------- START POST REQUEST ---------------------

    public static void makePostRequest(final String urlLink, final String jsonBody, final InputStreamCallBack callback) {
        new AsyncTask<String, Void, InputStream>() {
            @Override
            protected InputStream doInBackground(String... strings) {
                return makePostRequestAsync(urlLink, jsonBody);
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                if (callback != null) {
                    callback.callback(inputStream);
                }
            }
        }.execute();
    }

    public static InputStream makePostRequestAsync(String urlLink, String jsonBody) {
        Log.i(LOG_TAG, "Making a post request to " + urlLink + " with json request: " + jsonBody);
        HttpURLConnection urlConnection = makePostRequest0(urlLink, jsonBody);
        if (urlConnection != null) {
            try {
                int responseCode = urlConnection.getResponseCode();
                Log.i(LOG_TAG, "response code: " + responseCode);
                if (responseCode / 100 == 2) {
                    Log.i(LOG_TAG, "Got input!");
                    return urlConnection.getInputStream();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making a post request", e);
            }
        }
        disconnect(urlConnection);
        Log.i(LOG_TAG, "urlConnection: " + urlConnection);
        return null;
    }

    private static HttpURLConnection makePostRequest0(String urlLink, String jsonBody) {
        URL url = createURL(urlLink);
        if (url != null) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                makePostRequest1(urlConnection, jsonBody);
                return urlConnection;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't open url connection", e);
            }
        }
        return null;
    }

    private static void makePostRequest1(HttpURLConnection urlConnection, String jsonBody) throws IOException {
       setURLConnectionToPost(urlConnection);
       urlConnection.connect();
       OutputStream outputStream = urlConnection.getOutputStream();
       outputStream.write(jsonBody.getBytes());
       Log.i(LOG_TAG, "Connecting to site via POST...");
    }

    private static void setURLConnectionToPost(HttpURLConnection urlConnection) throws ProtocolException {
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true);
    }


    // --------------------- END POST REQUEST ---------------------

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url, int millisReadTimeout, int millisConnectionTimeOut, String key, String token) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if (url == null) {
            return jsonResponse;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            // configureURLConnection(urlConnection, millisReadTimeout, millisConnectionTimeOut, key, token);
            urlConnection.connect();

            // get the response code. if it starts with 2 then it's successful
            if (urlConnection.getResponseCode() / 100 == 2) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.d("responsecode", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static void disconnect(HttpURLConnection urlConnection) {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

}
