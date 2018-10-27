package behrman.justin.financialmanager.model;

import java.io.Serializable;

@FunctionalInterface
public interface CardTypeClassConverter extends Serializable {
    Class<?> convertCardTypeToClass(CardType cardType);
}
