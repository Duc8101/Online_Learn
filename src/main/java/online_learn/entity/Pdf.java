package online_learn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf")
@Getter
@Setter
public class Pdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pdf_id", nullable = false)
    private int pdfId;

    @Column(name = "pdf_name", nullable = false)
    private String pdfName;

    @Column(name = "file_pdf", nullable = false)
    private String filePdf;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
