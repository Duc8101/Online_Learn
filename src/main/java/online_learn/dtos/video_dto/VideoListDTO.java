package online_learn.dtos.video_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class VideoListDTO {

    public VideoListDTO(int videoId, @NonNull String videoName, @NonNull String fileVideo, int lessonId) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.fileVideo = fileVideo;
        this.lessonId = lessonId;
    }

    private int videoId;

    @NonNull
    private String videoName;

    @NonNull
    private String fileVideo;

    private int lessonId;
}
