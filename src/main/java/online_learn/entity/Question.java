package online_learn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online_learn.converters.AnswerConverter;
import online_learn.enums.Answers;

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
    private String questionName;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "answer1", nullable = false)
    private String answer1;

    @Column(name = "answer2", nullable = false)
    private String answer2;

    @Column(name = "answer3")
    private String answer3;

    @Column(name = "answer4")
    private String answer4;

    @Column(name = "answer_correct", nullable = false)
    @Convert(converter = AnswerConverter.class)
    private Answers answerCorrect;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<StartQuiz> startQuizzes = new ArrayList<>();
}
