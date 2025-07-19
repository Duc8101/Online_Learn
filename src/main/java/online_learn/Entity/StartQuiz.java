package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "start_quiz")
@Getter
@Setter
public class StartQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "start_quiz_id")
    private int startQuizId;
}
