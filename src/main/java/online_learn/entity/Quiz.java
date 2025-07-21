package online_learn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz")
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id", nullable = false)
    private int quizId;

    @Column(name = "quiz_name", nullable = false)
    @NonNull
    private String quizName;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @NonNull
    private Lesson lesson;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;
}
