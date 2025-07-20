package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.Enums.Answers;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private int questionId;

    @Column(name = "question_name", nullable = false)
    @NonNull
    private String questionName;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @NonNull
    private Quiz quiz;

    @Column(name = "answer1", nullable = false)
    @NonNull
    private String answer1;

    @Column(name = "answer2", nullable = false)
    @NonNull
    private String answer2;

    @Column(name = "answer3")
    @Nullable
    private String answer3;

    @Column(name = "answer4")
    @Nullable
    private String answer4;

    @Column(name = "answer_correct", nullable = false)
    @NonNull
    private Answers answerCorrect;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<StartQuiz> startQuizzes = new ArrayList<>();
}
