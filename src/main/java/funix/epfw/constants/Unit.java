package funix.epfw.constants;

public enum Unit {
    KG("Kilogram (Kg)"),
    LITRE("Lít"),
    PIECE("Cái"),
    BAG("Túi"),
    BOX("Hộp"),
    BOTTLE("Chai"),
    CAN("Lon"),
    CARTON("Thùng"),
    DOZEN("Tá (12 cái)"),
    GRAM("Gram (g)"),
    PACKET("Gói"),
    ROLL("Cuộn"),
    SET("Bộ"),
    TUBE("Tuýp"),
    UNIT("Đơn vị"),
    TON("Tấn");

    private final String Vietnamese;

    //Contructor
    Unit(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

    //Getter
    public String getVietnamese() {
        return Vietnamese;
    }
}
