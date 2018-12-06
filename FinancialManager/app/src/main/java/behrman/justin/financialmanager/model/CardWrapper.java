package behrman.justin.financialmanager.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import behrman.justin.financialmanager.interfaces.CardReceiever;
import behrman.justin.financialmanager.utils.GetCardsUtil;

public class CardWrapper extends Observable {

    private ArrayList<Card> allCards;
    private ArrayList<Card> manualCards;
    private ArrayList<Card> autoCards;

    private boolean error;

    private boolean loading;

    private static CardWrapper cardWrapper;

    {
        allCards = new ArrayList<>();
        manualCards = new ArrayList<>();
        autoCards = new ArrayList<>();
    }

    private CardWrapper() {}

    public static CardWrapper getInstance() {
        if (cardWrapper == null) {
            cardWrapper = new CardWrapper(); // lazy instantiation
        }
        return cardWrapper;
    }

    public void refresh(Context context) {
        if (!loading) { // don't want two network calls when one is already going on
            loading = true;
            error = false;
            GetCardsUtil.findAllCards(new CardReceiever() {
                @Override
                public void receiveCards(List<Card> cards) {
                    setCards(cards);
                }
            }, context);
        }
    }

    private void setCards(List<Card> cards) {
        loading = false;
        if (cards != null) {
            setCards0(cards);
        } else {
            error = true;
        }
        notify0(); // let observers know either there was an error or not
    }

    private void notify0() {
        setChanged();
        notifyObservers();
    }

    private void setCards0(List<Card> cards) {
        clearCards();
        for (Card c : cards) {
            addCard(c);
        }
    }

    private void clearCards() {
        autoCards.clear();
        manualCards.clear();
        allCards.clear();
    }

    public boolean hasError() {
        return error;
    }

    public boolean isLoading() {
        return loading;
    }

    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public ArrayList<Card> getManualCards() {
        return manualCards;
    }

    public ArrayList<Card> getAutoCards() {
        return autoCards;
    }

    public void updateCard(Card originalCard, Card newCard) {
        replaceCard(allCards, originalCard, newCard);
        changeBasedOffType(originalCard, newCard);
    }

    private void changeBasedOffType(Card originalCard, Card newCard) {
        ArrayList<Card> listAffected;
        if (originalCard.getCardType() == CardType.MANUAL) {
            listAffected = manualCards;
        } else if (originalCard.getCardType() == CardType.AUTO) {
            listAffected = autoCards;
        } else {
            listAffected = new ArrayList<>(); // card type either null or something else
        }
        replaceCard(listAffected, originalCard, newCard);
    }

    private void replaceCard(ArrayList<Card> cards, Card originalCard, Card newCard) {
        int index = getIndex(cards, originalCard);
        if (index >= 0) {
            allCards.set(index, newCard);
        }
    }

    public void addCard(Card card) {
        allCards.add(card);
        if (card.getCardType() == CardType.MANUAL) {
            manualCards.add(card);
        } else if (card.getCardType() == CardType.AUTO) {
            autoCards.add(card);
        }
    }

    private int getIndex(ArrayList<Card> cards, Card originalCard) {
        int index = 0;
        for (Card card : cards) {
            if (card.equals(originalCard)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

}
