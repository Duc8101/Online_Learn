package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online_learn.CompositeKey.EnrollCourseId;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "enroll_course")
@Getter
@Setter
public class EnrollCourse implements Serializable {

    public EnrollCourse() {
    }

    public EnrollCourse(@NonNull Course course, @NonNull User student) {
        this.course = course;
        this.student = student;
        this.enrollCourseId = new EnrollCourseId(course.getCourseId(), student.getUserId());
    }

    @EmbeddedId
    private EnrollCourseId enrollCourseId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @NonNull
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @NonNull
    private User student;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;
}
