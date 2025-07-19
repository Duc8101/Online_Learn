package online_learn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", nullable = false)
    private int videoId;

    @Column(name = "video_name", nullable = false)
    @NonNull
    private String videoName;

    @Column(name = "file_video", nullable = false)
    @NonNull
    private String fileVideo;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @NonNull
    private Lesson lesson;

    @Column(name = "created_at", nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NonNull
    private LocalDateTime updatedAt;
}
