package behrman.justin.financialmanager.model;

public class Card {

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
