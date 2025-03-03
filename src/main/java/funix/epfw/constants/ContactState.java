package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum ContactState {
    NEW("Mới"),   // Mới
    DONE("Đã xử lý");       // Đã xử lý

    private final String Vietnamese;

    //Contructor
    ContactState(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }
}
