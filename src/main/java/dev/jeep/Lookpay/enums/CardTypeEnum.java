package dev.jeep.Lookpay.enums;

public enum CardTypeEnum {

    CREDIT_CARD,
    DEBIT_CARD;

    public static CardTypeEnum fromString(String str) {
        switch (str) {
            case "TARJETA DE CREDITO":
                return CardTypeEnum.CREDIT_CARD;
            case "TARJETA DE DEBITO":
                return CardTypeEnum.DEBIT_CARD;
            default:
                return null;
        }
    }
}
