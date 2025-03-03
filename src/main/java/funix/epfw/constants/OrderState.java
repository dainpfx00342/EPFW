package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum OrderState {
    NEW("Mới"),   // Mới
    IN_PROGRESS("Đang xử lý"),      // Đang xử lý
    DONE("Đã xử lý");       // Đã xử lý

    private final String Vietnamese;

    //Contructor
    OrderState(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

}
