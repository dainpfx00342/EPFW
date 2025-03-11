package funix.epfw.constants;

import lombok.Getter;

@Getter
public enum TourType {
    SIGHTSEEING ("Tham quan"),
    FISHING("Câu cá"),
    ECO_TOURISM("Du lịch sinh thái"),  // Du lịch sinh thái
    FARMING_EXPERIENCE("Trải nghiệm làm nông");

    private final String vietnamese;
    //contractor
   TourType(String vietnamese){
       this.vietnamese = vietnamese;
   }
}
