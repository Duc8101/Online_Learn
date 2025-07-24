package online_learn.dtos.user_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.enums.Genders;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class ProfileFormDTO {

    @NonNull
    private String fullName;

    @Nullable
    private String phone;

    @NonNull
    private String image;

    @Nullable
    private String address;

    @NonNull
    private String email;

    @NonNull
    private Genders gender;

}
