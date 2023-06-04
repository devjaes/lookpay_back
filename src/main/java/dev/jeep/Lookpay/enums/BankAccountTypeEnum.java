package dev.jeep.Lookpay.enums;

public enum BankAccountTypeEnum {

    SAVINGS_ACCOUNT,
    CURRENT_ACCOUNT;

    public static BankAccountTypeEnum fromString(String str) {
        switch (str) {
            case "SAVINGS_ACCOUNT":
                return BankAccountTypeEnum.SAVINGS_ACCOUNT;
            case "CURRENT_ACCOUNT":
                return BankAccountTypeEnum.CURRENT_ACCOUNT;
            default:
                return null;
        }
    }
}
