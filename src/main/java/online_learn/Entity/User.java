package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.Enums.Genders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "full_name", nullable = false)
    @NonNull
    private String fullName;

    @Column(name = "phone", nullable = false)
    @NonNull
    private String phone;

    @Column(name = "image", nullable = false)
    @NonNull
    private String image;

    @Column(name = "address", nullable = false)
    @NonNull
    private String address;

    @Column(name = "email", nullable = false)
    @NonNull
    private String email;

    @Column(name = "gender", nullable = false)
    @NonNull
    private Genders gender;

    @Column(name = "username", nullable = false)
    @NonNull
    private String username;

    @Column(name = "password", nullable = false)
    @NonNull
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "lockout_end_time", nullable = false)
    @NonNull
    private LocalDateTime lockoutEndTime;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<EnrollCourse> enrollCourses = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Result> results = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StartQuiz> startQuizzes = new ArrayList<>();
}
