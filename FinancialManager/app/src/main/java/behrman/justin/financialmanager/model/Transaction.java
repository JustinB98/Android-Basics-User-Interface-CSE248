package behrman.justin.financialmanager.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

// @ParseClassName(StringConstants.TRANSACTION_CLASS)
public class Transaction /* extends ParseObject */ implements Serializable, Comparable<Transaction> {

    /**
     * optional
     */
    private String mObjectId;

    private String mPlace;
    private double mAmount;
    private Date mDate;
    private String mCurrencyCode;

    public Transaction(String place, double amount, Date date, String currencyCode, @Nullable String objectId) {
        mPlace = place;
        mAmount = amount;
        mDate = date;
        mCurrencyCode = currencyCode;
        mObjectId = objectId;
    }

    public Transaction(String place, double amount, Date date, String currencyCode) {
        this(place, amount, date, currencyCode, null);
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

    public String getObjectId() {
        return mObjectId;
    }

    public void deepCopy(Transaction t) {
        mPlace = t.mPlace;
        mAmount = t.mAmount;
        mDate = t.mDate;
        mCurrencyCode = mCurrencyCode;
        mObjectId = mObjectId;
    }

    @Override
    public int compareTo(@NonNull Transaction o) {
        return mDate.compareTo(o.mDate);
    }

    @Override
    public String toString() {
        return "place: " + mPlace + ", Amount: " + mAmount + ", Date: " + mDate + ", currencyCode: " + mCurrencyCode;
    }

}
