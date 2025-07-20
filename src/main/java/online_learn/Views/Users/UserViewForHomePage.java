package online_learn.Views.Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewForHomePage {

    private int userId;
    private String fullName;
    private String image;

    public UserViewForHomePage() {
    }

    public UserViewForHomePage(int userId, String fullName, String image) {
        this.userId = userId;
        this.fullName = fullName;
        this.image = image;
    }
}
