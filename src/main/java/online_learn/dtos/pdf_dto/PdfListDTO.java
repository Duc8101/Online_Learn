package online_learn.dtos.pdf_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PdfListDTO {

    public PdfListDTO(int pdfId, @NonNull String pdfName, @NonNull String filePdf, int lessonId) {
        this.pdfId = pdfId;
        this.pdfName = pdfName;
        this.filePdf = filePdf;
        this.lessonId = lessonId;
    }

    private int pdfId;

    @NonNull
    private String pdfName;

    @NonNull
    private String filePdf;

    private int lessonId;
}
