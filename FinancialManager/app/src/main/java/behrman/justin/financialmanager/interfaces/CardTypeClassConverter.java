package behrman.justin.financialmanager.interfaces;

import java.io.Serializable;

import behrman.justin.financialmanager.model.CardType;

@FunctionalInterface
public interface CardTypeClassConverter extends Serializable {
    Class<?> convertCardTypeToClass(CardType cardType);
}
