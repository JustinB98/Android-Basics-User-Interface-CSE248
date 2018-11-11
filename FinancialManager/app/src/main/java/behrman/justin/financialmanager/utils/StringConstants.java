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
    // ---------------------- END PARSE CLOUD FUNCTIONS----------------------

    // ---------------------- START PARSE CLOUD PARAMETERS----------------------
    public final static String PARSE_CLOUD_PARAMETER_NEW_CARD_NAME = "newName";
    public final static String PARSE_CLOUD_PARAMETER_ORIGINAL_CARD_NAME = "originalName";
    public final static String PARSE_CLOUD_PARAMETER_CARD_NAME_LIST = "cardNames";
    public final static String PARSE_CLOUD_PARAMETER_CARD_TYPE_LIST = "cardTypes";
    public final static String PARSE_CLOUD_PARAMETER_MONTH = "month";
    public final static String PARSE_CLOUD_PARAMETER_YEAR = "year";
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
    // ---------------------- END KEYS ----------------------

    // ---------------------- START LITERALS ----------------------
    public final static String SUCCESS = "success";
    // ---------------------- END LITERALS ----------------------

}
