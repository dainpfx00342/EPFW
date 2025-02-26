package funix.epfw.constants;

public enum ProductCategory {
    AGRICULTURE("Nông sản"),   // Nông sản
    FORESTRY("Lâm sản"),      // Lâm sản
    FISHERY("Thủy sản"),       // Thủy sản
    LIVESTOCK("Chăn nuôi");

    private final String Vietnamese;

    //Contructor
    ProductCategory(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

    //Getter
    public String getVietnamese() {
        return Vietnamese;
    }
}
