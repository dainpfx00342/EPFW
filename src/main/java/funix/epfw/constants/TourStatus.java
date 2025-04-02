package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum TourStatus {
    OPENING ("Đang mở"),
    CLOSED("Đã đóng");



    private final String vietnamese;
    //Contructor
    TourStatus(String vienamese){
        this.vietnamese = vienamese;
    }


}
