package online_learn.dtos.user_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.enums.Genders;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class RegisterDTO {

    @NonNull
    private String fullName;

    @NonNull
    private String phone;

    @NonNull
    private String email;

    @Nullable
    private String address;

    @NonNull
    private Genders gender;

    @NonNull
    private String username;
}
