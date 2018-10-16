package behrman.justin.financialmanager.model;

import java.util.Date;

public class Transaction {

    private String mPlace;
    private double mAmount;
    private Date mDate;
    private String mCurrencyCode;

    public Transaction(String place, double amount, Date date, String currencyCode) {
        mPlace = place;
        mAmount = amount;
        mDate = date;
        mCurrencyCode = currencyCode;
    }

    public String getPlace() {
        return mPlace;
    }

    public double getAmount() {
        return mAmount;
    }

    public Date getDate() {
        return mDate;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    @Override
    public String toString() {
        return "place: " + mPlace + ", Amount: " + mAmount + ", Date: " + mDate + ", currencyCode: " + mCurrencyCode;
    }

}
