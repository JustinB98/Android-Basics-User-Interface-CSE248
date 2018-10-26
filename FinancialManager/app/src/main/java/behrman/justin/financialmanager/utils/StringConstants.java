package behrman.justin.financialmanager.utils;

public final class StringConstants {

    /* Utility class */
    private StringConstants() {}

    /* URLS */
    public final static String SERVER_URL = "https://financial-manager-app.herokuapp.com/";
    public final static String PARSE_URL = SERVER_URL + "parse/";
    public final static String CONVERT_ACCESS_URL = SERVER_URL + "converttoaccess";

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


    /* OTHER */
    // for passing card names across intents
    public final static String CARD_NAME = "cardName";
    public final static String CARD_TYPE = "cardType";

}
