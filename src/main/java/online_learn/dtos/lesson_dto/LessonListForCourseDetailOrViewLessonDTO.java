package online_learn.dtos.lesson_dto;

import lombok.Getter;
import lombok.Setter;
import online_learn.dtos.pdf_dto.PdfListDTO;
import online_learn.dtos.video_dto.VideoListDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LessonListForCourseDetailOrViewLessonDTO {

    private int lessonId;
    private String lessonName;
    private int courseId;
    private List<VideoListDTO> videoDTOs = new ArrayList<>();
    private List<PdfListDTO> pdfDTOs = new ArrayList<>();

    public LessonListForCourseDetailOrViewLessonDTO() {
    }

    public LessonListForCourseDetailOrViewLessonDTO(int lessonId, String lessonName, int courseId) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.courseId = courseId;
    }
}
