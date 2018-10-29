package behrman.justin.financialmanager.model;

import java.io.Serializable;

public class Card implements Serializable {

    private String mCardName;
    private CardType mCardType;

    public Card(String cardName, CardType cardType) {
        mCardName = cardName;
        mCardType = cardType;
    }

    public String getCardName() {
        return mCardName;
    }

    public CardType getCardType() {
        return mCardType;
    }

    public void setCardName(String mCardName) {
        this.mCardName = mCardName;
    }

    public void setCardType(CardType mCardType) {
        this.mCardType = mCardType;
    }
}
