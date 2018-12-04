package behrman.justin.financialmanager.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(mCardName, card.mCardName) &&
                mCardType == card.mCardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mCardName, mCardType);
    }

    @Override
    public String toString() {
        return "Card{" +
                "mCardName='" + mCardName + '\'' +
                ", mCardType=" + mCardType +
                '}';
    }

}
