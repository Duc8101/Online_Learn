package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course implements Serializable {

    public Course() {
    }

    public Course(int courseId, @NonNull String courseName, @NonNull String image, @NonNull Category category, @NonNull User creator, @Nullable String description, @NonNull LocalDateTime createdAt, @NonNull LocalDateTime updatedAt, boolean isDeleted) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.image = image;
        this.category = category;
        this.creator = creator;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private int courseId;

    @Column(name = "course_name", nullable = false)
    @NonNull
    private String courseName;

    @Column(name = "image", nullable = false)
    @NonNull
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NonNull
    private Category category;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @NonNull
    private User creator;

    @Column(name = "description")
    @Nullable
    private String description;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @NonNull
    private List<EnrollCourse> enrollCourses = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @NonNull
    private List<Lesson> lessons = new ArrayList<>();
}
