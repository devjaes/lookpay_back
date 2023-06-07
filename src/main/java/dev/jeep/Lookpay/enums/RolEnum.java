package dev.jeep.Lookpay.enums;

public enum RolEnum {
    ADMIN, CLIENT, COMPANY;

    public static RolEnum getRolEnum(String rol) {
        switch (rol) {
            case "ADMIN":
                return ADMIN;
            case "CLIENT":
                return CLIENT;
            case "COMPANY":
                return COMPANY;
            default:
                return null;
        }
    }
}
