package dev.jeep.Lookpay.enums;

public enum CardTypeEnum {

    CREDIT_CARD("TARJETA DE CREDITO"),
    DEBIT_CARD("TARJETA DE DEBITO");

    private String value;

    CardTypeEnum(String value) {
        this.value = value;
    }

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

    @Override
    public String toString() {
        return this.value;
    }
}
