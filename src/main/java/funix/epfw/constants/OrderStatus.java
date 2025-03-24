package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Chờ xác nhật"),   // Mới
    CONFIRMED("Đã xác nhận"),      // Đang xử lý
    COMPLETED("Đã Hoàn thành"),
    CANCELED("Đã hủy đơn");       // Đã xử lý

    private final String Vietnamese;

    //Contructor
    OrderStatus(String Vietnamese) {
        this.Vietnamese = Vietnamese;
    }

}
