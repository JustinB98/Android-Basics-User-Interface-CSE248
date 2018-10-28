package behrman.justin.financialmanager.model;

import java.io.Serializable;
import java.util.Date;

// @ParseClassName(StringConstants.TRANSACTION_CLASS)
public class Transaction /* extends ParseObject */ implements Serializable {

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
