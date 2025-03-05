package funix.epfw.model.farm.tour;

import funix.epfw.model.farm.Farm;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Farm farm;
}
