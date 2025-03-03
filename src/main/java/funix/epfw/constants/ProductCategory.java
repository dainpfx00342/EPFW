package funix.epfw.constants;

import lombok.Data;
import lombok.Getter;

@Getter
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

}
