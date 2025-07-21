package online_learn.view_models.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewModelForHomePage {

    private int userId;
    private String fullName;
    private String image;

    public UserViewModelForHomePage() {
    }

    public UserViewModelForHomePage(int userId, String fullName, String image) {
        this.userId = userId;
        this.fullName = fullName;
        this.image = image;
    }
}
