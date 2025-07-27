package online_learn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online_learn.converters.AnswerConverter;
import online_learn.enums.Answers;

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
    private User student;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "answer")
    @Convert(converter = AnswerConverter.class)
    private Answers answer;
}
