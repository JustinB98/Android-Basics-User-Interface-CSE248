package behrman.justin.financialmanager.utils;

public final class StringConstants {

    /* Utility class */
    private StringConstants() {}

    /* URLS */
    public final static String SERVER_URL = "https://financial-manager-app.herokuapp.com/";
    public final static String PARSE_URL = SERVER_URL + "parse/";
    public final static String CONVERT_ACCESS_URL = SERVER_URL + "converttoaccess";
    public final static String GET_TRANSACTIONS_URL = SERVER_URL + "getTransactions";

    public final static String PARSE_TRANSACTIONS_CLOUD_FUNCTION = "getTransactions";

    /* DATABASE FIELDS */
    public final static String AUTO_CARD_NAME = "name";
    public final static String MANUAL_CARD_NAME = "name";
    public final static String AUTO_CARD_OWNER = "owner";
    public final static String MANUAL_CARD_OWNER = "owner";
    public final static String AUTO_CARD_PUBLIC_TOKEN = "publicToken";
    public final static String MANUAL_CARD_CLASS = "ManualCards";
    public final static String AUTO_CARD_CLASS = "AutoCards";
    public final static String TRANSACTION_CLASS = "Transactions";
    public final static String MANUAL_CARD_TRANSACTIONS = "transactions";
    public final static String MANUAL_CARD_TRANSACTIONS_PLACE = "place";
    public final static String MANUAL_CARD_TRANSACTIONS_AMOUNT = "amount";
    public final static String MANUAL_CARD_TRANSACTIONS_DATE = "date";
    public final static String MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE = "currencyCode";
    public final static String MANUAL_CARD_TRANSACTIONS_USER_ID = "userId";
    public final static String MANUAL_CARD_TRANSACTIONS_CARD_NAME = "cardName";
    public final static String MANUAL_CARD_TRANSACTIONS_START_DATE = "startDate";
    public final static String MANUAL_CARD_TRANSACTIONS_END_DATE = "endDate";

    public final static String MANUAL_CARD_TRANSACTIONS_YEAR = "year";
    public final static String MANUAL_CARD_TRANSACTIONS_MONTH = "month";

    public final static String PLAID_TRANSACTION_ERROR_CODE = "error_code";
    public final static String PLAID_TRANSACTION_ERROR_CODE_MESSAGE = "PRODUCT_NOT_READY";

    /* OTHER */
    // for passing card names across intents
    public final static String CARD_NAME = "cardName";
    public final static String CARD_TYPE = "cardType";

    public final static String TRANSACTIONS_LENGTH = "length";
    public final static String TRANSACTIONS_KEY = "transactions";

    // for the class to open after another class (ie Select Card to View Card History)
    public final static String NEXT_CLASS = "nextClass";

    public final static String PLAID_PLACE_NAME = "name";
    public final static String PLAID_DATE = "date";
    public final static String PLAID_AMOUNT = "amount";
    public final static String PLAID_CURRENCY_CODE = "iso_currency_code";

    public final static String GET_MANUAL_TRANSACTIONS_FUNCTION = "getManualTransactions";

    public final static String ORIGINAL_CARD_KEY = "originalCard";

    public final static String PARSE_CLOUD_FUNCTION_RENAME_MANUAL_CARD = "renameManualCard";
    public final static String PARSE_CLOUD_FUNCTION_RENAME_AUTO_CARD = "renameAutoCard";
    public final static String PARSE_CLOUD_NEW_CARD_NAME_PARAMETER = "newName";
    public final static String PARSE_CLOUD_ORIGINAL_CARD_NAME_PARAMETER = "originalName";

    public final static String CARD_KEY = "card";

    public static final String CARD_TYPE_KEY = "cardType";

}
