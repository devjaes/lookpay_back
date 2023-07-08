package dev.jeep.Lookpay.enums;

public enum CardTypeEnum {

    CREDIT_CARD,
    DEBIT_CARD;

    public static CardTypeEnum fromString(String str) {
        switch (str) {
            case "CREDIT_CARD":
                return CardTypeEnum.CREDIT_CARD;
            case "DEBIT_CARD":
                return CardTypeEnum.DEBIT_CARD;
            default:
                return null;
        }
    }
}
