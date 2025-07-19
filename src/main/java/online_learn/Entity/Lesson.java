package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id", nullable = false)
    private int lessonId;

    @Column(name = "lesson_name", nullable = false)
    @NonNull
    private String lessonName;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NonNull
    private Course course;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @NonNull
    private List<Pdf> pdfs = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @NonNull
    private List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @NonNull
    private List<Quiz> quizzes = new ArrayList<>();

}
