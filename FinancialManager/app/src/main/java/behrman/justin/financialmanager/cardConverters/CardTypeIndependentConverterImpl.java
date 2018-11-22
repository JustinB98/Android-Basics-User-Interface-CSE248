package behrman.justin.financialmanager.cardConverters;

import java.io.Serializable;

import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.interfaces.CardTypeClassConverter;

public class CardTypeIndependentConverterImpl implements CardTypeClassConverter, Serializable {

    private Class<?> classToOpen;

    public CardTypeIndependentConverterImpl(Class<?> classToOpen) {
        this.classToOpen = classToOpen;
    }

    @Override
    public Class<?> convertCardTypeToClass(CardType cardType) {
        return classToOpen;
    }
}
