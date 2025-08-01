package online_learn.dtos.user_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.enums.Genders;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
public class UserProfileInfoDTO implements Serializable {

    private int userId;

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

    @NonNull
    private String username;

    private int roleId;
}
