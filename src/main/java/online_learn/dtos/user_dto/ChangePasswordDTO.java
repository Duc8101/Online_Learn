package online_learn.dtos.user_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @NonNull
    private String currentPassword;

    @NonNull
    private String newPassword;

    @NonNull
    private String confirmPassword;
}
