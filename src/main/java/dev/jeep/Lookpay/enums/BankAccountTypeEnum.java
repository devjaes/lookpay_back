package dev.jeep.Lookpay.enums;

public enum BankAccountTypeEnum {

    SAVINGS_ACCOUNT,
    CURRENT_ACCOUNT;

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
}
