package dev.jeep.Lookpay.enums;

public enum RolEnum {
    ADMIN, USER, COMPANY;

    public static RolEnum getRolEnum(String rol) {
        switch (rol) {
            case "ADMIN":
                return ADMIN;
            case "USER":
                return USER;
            case "COMPANY":
                return COMPANY;
            default:
                return null;
        }
    }
}
