package online_learn.CompositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EnrollCourseId implements Serializable {

    @Column(name = "course_id", nullable = false)
    private int courseId;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    public EnrollCourseId() {
    }

    public EnrollCourseId(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollCourseId enrollCourseId)) return false;
        return Objects.equals(studentId, enrollCourseId.studentId) &&
                Objects.equals(courseId, enrollCourseId.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}
