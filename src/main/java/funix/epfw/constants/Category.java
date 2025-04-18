package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum Category {
    AGRICULTURE("Nông sản"),   // Nông sản
    FORESTRY("Lâm sản"),      // Lâm sản
    FISHERY("Thủy sản"),       // Thủy sản
    LIVESTOCK("Chăn nuôi");

    private final String Vietnamese;

    //Contructor
    Category(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

}
