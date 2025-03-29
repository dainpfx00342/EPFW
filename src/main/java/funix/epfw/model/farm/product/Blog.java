package funix.epfw.model.farm.product;

import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.vote.Vote;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToMany(mappedBy = "blogs",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product> products= new ArrayList<>();

    @ManyToMany(mappedBy = "blogs",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Tour> tours= new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    }
