package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum TourStatus {
    PENDING ("Chờ duyệt"),
    APPROVED("Được chập nhận"),
    REJECTED("Bị từ chối");


    private final String vietnamese;
    //Contructor
    TourStatus(String vienamese){
        this.vietnamese = vienamese;
    }


}
