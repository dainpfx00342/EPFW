package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Chờ"),   // Mới
    IN_PROGRESS("Đang xử lý"),      // Đang xử lý
    DONE("Đã xử lý");       // Đã xử lý

    private final String Vietnamese;

    //Contructor
    OrderStatus(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

}
