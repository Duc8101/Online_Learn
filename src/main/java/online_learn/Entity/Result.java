package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "result")
@Getter
@Setter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", nullable = false)
    private int resultId;

    @ManyToOne
    @JoinColumn(name = "quiz_id",  nullable = false)
    @NonNull
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NonNull
    private User student;

    @Column(name = "score", nullable = false)
    @NonNull
    private double score;
}
