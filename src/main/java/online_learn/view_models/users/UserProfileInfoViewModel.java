package online_learn.view_models.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.enums.Genders;

import java.io.Serializable;

@Getter
@Setter
public class UserProfileInfoViewModel implements Serializable {

    private int userId;

    @NonNull
    private String fullName;

    @NonNull
    private String phone;

    @NonNull
    private String image;

    @NonNull
    private String address;

    @NonNull
    private String email;

    @NonNull
    private Genders gender;

    @NonNull
    private String username;

    private int roleId;
}
