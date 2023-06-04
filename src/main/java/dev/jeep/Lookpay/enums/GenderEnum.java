package dev.jeep.Lookpay.enums;

public enum GenderEnum {

    MALE, FEMALE, OTHER;

    public static GenderEnum getGenderEnum(String str) {
        switch (str) {
            case "MALE":
                return MALE;

            case "FEMALE":
                return FEMALE;

            case "OTHER":
                return OTHER;

            default:
                return null;
        }
    }
}
