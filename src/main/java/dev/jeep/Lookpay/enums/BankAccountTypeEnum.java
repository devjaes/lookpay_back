package dev.jeep.Lookpay.enums;

public enum BankAccountTypeEnum {

    SAVINGS_ACCOUNT("CUENTA DE AHORROS"),
    CURRENT_ACCOUNT("CUENTA CORRIENTE");

    private String value;

    BankAccountTypeEnum(String value) {
        this.value = value;
    }

    public static BankAccountTypeEnum fromString(String str) {
        switch (str) {
            case "CUENTA DE AHORROS":
                return BankAccountTypeEnum.SAVINGS_ACCOUNT;
            case "CUENTA CORRIENTE":
                return BankAccountTypeEnum.CURRENT_ACCOUNT;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return this.value;
    }
}
