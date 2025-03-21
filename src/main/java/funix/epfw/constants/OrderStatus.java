package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Chờ"),   // Mới
    CONFIRMED("Đã xác nhận"),      // Đang xử lý
    COMPELETED("Đã Hoàn thành"),
    CANCELED("Đã hủy");       // Đã xử lý

    private final String Vietnamese;

    //Contructor
    OrderStatus(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

}
