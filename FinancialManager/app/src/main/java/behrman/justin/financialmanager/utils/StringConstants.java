package behrman.justin.financialmanager.utils;

/**
 * Strings that don't need to translated but may be changed
 */
public final class StringConstants {

    /* Utility class */
    private StringConstants() {}

    // ---------------------- START URLS ----------------------
    public final static String SERVER_URL = "https://financial-manager-app.herokuapp.com/";
    public final static String PARSE_URL = SERVER_URL + "parse/";
    // ---------------------- END URLS ----------------------

    // ---------------------- START DATABASE ----------------------
    public final static String DATABASE_CARD_NAME_COLUMN = "name";
    public final static String DATABASE_CARD_OWNER_COLUMN = "owner";
    public final static String AUTO_CARD_PUBLIC_TOKEN_COLUMN = "publicToken";
    public final static String MANUAL_CARD_CLASS_NAME = "ManualCards";
    public final static String AUTO_CARD_CLASS_NAME = "AutoCards";
    public final static String MANUAL_CARD_TRANSACTIONS_PLACE_COLUMN = "place";
    public final static String MANUAL_CARD_TRANSACTIONS_AMOUNT_COLUMN = "amount";
    public final static String MANUAL_CARD_TRANSACTIONS_DATE_COLUMN = "date";
    public final static String MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE_COLUMN = "currencyCode";
    public final static String MANUAL_CARD_TRANSACTIONS_USER_ID_COLUMN = "userId";
    public final static String MANUAL_CARD_TRANSACTIONS_CARD_NAME_COLUMN = "cardName";
    public final static String MANUAL_CARD_TRANSACTIONS_YEAR_COLUMN = "year";
    public final static String MANUAL_CARD_TRANSACTIONS_MONTH_COLUMN = "month";
    public final static String USER_EMAIL_COLUMN = "email";
    // ---------------------- END PARSE CLOUD PARAMETERS----------------------

    // ---------------------- START PLAID KEYS ----------------------
    public final static String PLAID_PLACE_NAME = "name";
    public final static String PLAID_DATE = "date";
    public final static String PLAID_AMOUNT = "amount";
    public final static String PLAID_CURRENCY_CODE = "iso_currency_code";
    // ---------------------- END PLAID KEYS----------------------

    // ---------------------- START PARSE CLOUD FUNCTIONS ----------------------
    public final static String PARSE_CLOUD_FUNCTION_GET_MANUAL_TRANSACTIONS = "getManualTransactions";
    public final static String PARSE_CLOUD_FUNCTION_RENAME_MANUAL_CARD = "renameManualCard";
    public final static String PARSE_CLOUD_FUNCTION_RENAME_AUTO_CARD = "renameAutoCard";
    public final static String PARSE_CLOUD_FUNCTION_ADD_MANUAL_TRANSACTION = "addManualTransaction";
    public final static String PARSE_CLOUD_FUNCTION_ADD_AUTO_CARD = "addAutoCard";
    public final static String PARSE_CLOUD_FUNCTION_EDIT_MANUAL_CARD = "editManualTransaction";
    public final static String PARSE_CLOUD_FUNCTION_GET_AUTO_TRANSACTIONS = "getAutoTransactions";
    public final static String PARSE_CLOUD_FUNCTION_DELETE_MANUAL_TRANSACTION = "deleteManualTransaction";
    public final static String PARSE_CLOUD_FUNCTION_DELETE_AUTO_CARD = "deleteAutoCard";
    public final static String PARSE_CLOUD_FUNCTION_DELETE_MANUAL_CARD = "deleteManualCard";
    public final static String PARSE_CLOUD_FUNCTION_CALCULATE_MONTHLY = "calculateMonthly";
    public final static String PARSE_CLOUD_FUNCTION_GET_ALL_CARDS = "getAllCards";
    public final static String PARSE_CLOUD_FUNCTION_GET_ALL_MANUAL_CARDS = "getAllManualCards";
    public final static String PARSE_CLOUD_FUNCTION_GET_ALL_AUTO_CARDS = "getAllAutoCards";
    public final static String PARSE_CLOUD_FUNCTION_RESET_PASSWORD = "resetPassword";
    public final static String PARSE_CLOUD_FUNCTION_ADD_MANUAL_CARD = "addManualCard";
    public final static String PARSE_CLOUD_FUNCTION_EDIT_CARD_NAME = "editCardName";
    public final static String PARSE_CLOUD_FUNCTION_DELETE_USER = "deleteUser";
    public final static String PARSE_CLOUD_FUNCTION_QUERY_TRANSACTIONS = "queryTransactions";
    public final static String PARSE_CLOUD_FUNCTION_RESEND_VERIFICATION_EMAIL = "resendVerificationEmail";
    // ---------------------- END PARSE CLOUD FUNCTIONS----------------------

    // ---------------------- START PARSE CLOUD PARAMETERS----------------------
    public final static String PARSE_CLOUD_PARAMETER_NEW_CARD_NAME = "newName";
    public final static String PARSE_CLOUD_PARAMETER_ORIGINAL_CARD_NAME = "originalName";
    public final static String PARSE_CLOUD_PARAMETER_CARD_NAME_LIST = "cardNames";
    public final static String PARSE_CLOUD_PARAMETER_CARD_TYPE_LIST = "cardTypes";
    public final static String PARSE_CLOUD_PARAMETER_MONTH = "month";
    public final static String PARSE_CLOUD_PARAMETER_YEAR = "year";
    public final static String PARSE_CLOUD_PARAMETER_USERNAME = "username";
    public final static String PARSE_CLOUD_PARAMETER_ORIGINAL_EMAIL = "originalEmail";
    public final static String PARSE_CLOUD_PARAMETER_NEW_EMAIL = "newEmail";
    // ---------------------- END PARSE CLOUD PARAMETERS----------------------


    // ---------------------- START KEYS ----------------------
    // for the class to open after another class (ie Select Card to View Card History)
    public final static String NEXT_CLASS_KEY = "nextClass";
    public final static String CARD_NAME_KEY = "cardName";
    public final static String TRANSACTIONS_LENGTH_KEY = "length";
    public final static String TRANSACTIONS_INTENT_KEY = "transactions";
    public final static String CARD_KEY = "card";
    public final static String CARD_TYPE_KEY = "cardType";
    public final static String TRANSACTION_KEY = "transaction";
    public final static String DATE_KEY = "date";
    public final static String OBJECT_ID_KEY = "objectId";
    public final static String IS_MANUAL_CARD_KEY = "isManual";
    public final static String MONTH_SPINNER_POSITION_KEY = "monthSpinnerPosition";
    public final static String YEAR_KEY = "year";
    public final static String SELECTED_CARDS_KEY = "selectedCards";
    public final static String USER_KEY = "user";
    public final static String EMAIL_KEY = "email";
    public final static String PASSWORD_KEY = "password";
    public final static String RES_ID_KEY = "resId";
    public final static String TITLE_KEY = "title";
    public final static String SELECTED_DATE_KEY = "selectedDate";
    // ---------------------- END KEYS ----------------------

    // ---------------------- START LITERALS ----------------------
    public final static String SUCCESS = "success";
    public final static String INVALID = "invalid";
    public final static String EXISTS = "exists";
    public final static String ERROR = "error";
    public final static String RENEW = "renew";
    public final static String UNDEFINED = "undefined";
    // ---------------------- END LITERALS ----------------------

    // ---------------------- START MONTHLY CALCULATIONS KEYS ----------------------
    public final static String TOTAL_KEY = "total";
    public final static String TRANSACTION_DATA_KEY = "transactions";
    // ---------------------- END MONTHLY CALCULATIONS KEYS ----------------------

    // ---------------------- START QUERY TRANSACTIONS KEYS ----------------------
    public final static String PLACE_KEY = "place";
    public final static String MIN_AMOUNT_KEY = "minAmount";
    public final static String MAX_AMOUNT_KEY = "maxAmount";
    public final static String MIN_MONTH_KEY = "minMonth";
    public final static String MAX_MONTH_KEY = "maxMonth";
    public final static String MIN_YEAR_KEY = "minYear";
    public final static String MAX_YEAR_KEY = "maxYear";
    public final static String CARDS_KEY = "cards";
    // ---------------------- END QUERY TRANSACTIONS KEYS ----------------------

    // ---------------------- START OTHER ----------------------
    public final static String VERIFICATION_PARSE_MESSAGE = "User email is not verified.";
    // ---------------------- END OTHER ----------------------
}
