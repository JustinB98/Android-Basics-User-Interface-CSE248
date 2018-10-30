package behrman.justin.financialmanager.cardConverters;

import java.io.Serializable;

import behrman.justin.financialmanager.activities.ViewAutoHistoryActivity;
import behrman.justin.financialmanager.activities.ViewManualHistoryActivity;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;

public class CardTypeClassConverterViewHistoryImpl implements CardTypeClassConverter, Serializable {
    @Override
    public Class<?> convertCardTypeToClass(CardType cardType) {
        if (cardType == CardType.MANUAL) {
            return ViewManualHistoryActivity.class;
        } else if (cardType == CardType.AUTO) {
            return ViewAutoHistoryActivity.class;
        } else {
            return null;
        }
    }
}
