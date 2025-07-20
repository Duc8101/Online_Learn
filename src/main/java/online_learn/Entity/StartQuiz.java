package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.Enums.Answers;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "start_quiz")
@Getter
@Setter
public class StartQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "start_quiz_id", nullable = false)
    private int startQuizId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NonNull
    private User student;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @NonNull
    private Question question;

    @Column(name = "answer")
    @Nullable
    private Answers answer;
}
