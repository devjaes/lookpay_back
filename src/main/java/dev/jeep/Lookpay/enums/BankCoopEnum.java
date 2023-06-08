package dev.jeep.Lookpay.enums;

public enum BankCoopEnum {
    // lista de bancos y cooperativas de ecuador

    BANCO_PICHINCHA("Banco Pichincha"),
    BANCO_INTERNACIONAL("Banco Internacional"),
    BANCO_GUAYAQUIL("Banco Guayaquil"),
    BANCO_PACIFICO("Banco Pacifico"),
    BANCO_PRODUCCION("Banco Produccion"),
    BANCO_BOLIVARIANO("Banco Bolivariano"),
    BANCO_AUSTRO("Banco Austro"),
    BANCO_DEL_AHORRO("Banco del Ahorro"),
    BANCO_MACHALA("Banco Machala"),
    BANCO_SOLIDARIO("Banco Solidario"),
    BANCO_LOJA("Banco Loja"),
    BANCO_AMAZONAS("Banco Amazonas"),
    BANCO_DINERS("Banco Diners"),
    BANCO_FINCA("Banco Finca"),
    BANCO_GENERAL_RUMIÑAHUI("Banco General Rumiñahui"),
    BANCO_COOPNACIONAL("Banco Coopnacional"),
    COOP_JEP("Cooperativa JEP"),
    COOP_CHORDELEG("Cooperativa de Ahorro y Crédito Chordeleg"),
    COOP_JARDIN_AZUAYO("Cooperativa de Ahorro y Crédito Jardín Azuayo"),
    COOP_POLICIA_NACIONAL("Cooperativa de Ahorro y Crédito Policía Nacional"),
    COOP_COOPROGRESO("Cooperativa de Ahorro y Crédito Cooprogreso"),
    COOP_29_OCTUBRE("Cooperativa de Ahorro y Crédito 29 de Octubre"),
    COOP_CACPECO("Cooperativa de Ahorro y Crédito Cacpeco"),
    COOP_RIOBAMBA("Cooperativa de Ahorro y Crédito Riobamba"),
    COOP_OSCUS("Cooperativa de Ahorro y Crédito Oscus"),
    COOP_COOPMEGO("Cooperativa de Ahorro y Crédito Coopmego"),
    COOP_ALIANZA_VALLE("Cooperativa de Ahorro y Crédito Alianza del Valle");

    public String bankName;

    BankCoopEnum(String bankName) {
        this.bankName = bankName;
    }

    public static BankCoopEnum getBankCoopEnum(String bankName) {
        for (BankCoopEnum bankCoopEnum : BankCoopEnum.values()) {
            if (bankCoopEnum.bankName.equals(bankName)) {
                return bankCoopEnum;
            }
        }
        return null;
    }
}
