package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import online_learn.CompositeKey.EnrollCourseId;

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
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    @NonNull
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @MapsId("studentId")
    @NonNull
    private User student;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;
}
