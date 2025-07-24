package online_learn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online_learn.composite_key.EnrollCourseId;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "enroll_course")
@Getter
@Setter
public class EnrollCourse implements Serializable {

    public EnrollCourse() {
    }

    public EnrollCourse(Course course, User student) {
        this.course = course;
        this.student = student;
        this.enrollCourseId = new EnrollCourseId(course.getCourseId(), student.getUserId());
    }

    @EmbeddedId
    private EnrollCourseId enrollCourseId;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @MapsId("studentId")
    private User student;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
