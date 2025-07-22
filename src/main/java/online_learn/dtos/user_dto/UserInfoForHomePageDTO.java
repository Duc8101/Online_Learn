package online_learn.dtos.user_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoForHomePageDTO {

    private int userId;
    private String fullName;
    private String image;

    public UserInfoForHomePageDTO() {
    }

    public UserInfoForHomePageDTO(int userId, String fullName, String image) {
        this.userId = userId;
        this.fullName = fullName;
        this.image = image;
    }
}
