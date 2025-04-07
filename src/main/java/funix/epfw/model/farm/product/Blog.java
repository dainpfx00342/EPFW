package funix.epfw.model.farm.product;

import funix.epfw.model.vote.Comment;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.vote.Vote;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate createdTime;

    @ManyToMany(mappedBy = "blogs", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    @ManyToMany(mappedBy = "blogs", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Tour> tours = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        createdTime = LocalDate.now();
    }
}
