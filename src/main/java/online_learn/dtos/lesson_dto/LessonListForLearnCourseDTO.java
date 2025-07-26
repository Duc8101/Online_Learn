package online_learn.dtos.lesson_dto;

import lombok.Getter;
import lombok.Setter;
import online_learn.dtos.pdf_dto.PdfListDTO;
import online_learn.dtos.video_dto.VideoListDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LessonListForLearnCourseDTO extends LessonListForCourseDetailOrViewLessonDTO {

    public LessonListForLearnCourseDTO(int lessonId, String lessonName, int courseId, boolean hasQuiz) {
        super(lessonId, lessonName, courseId);
        this.hasQuiz = hasQuiz;
    }

    private boolean hasQuiz;
    private List<VideoListDTO> videoDTOs = new ArrayList<>();
    private List<PdfListDTO> pdfDTOs = new ArrayList<>();
}
