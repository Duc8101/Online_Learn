package online_learn.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN(1),
    TEACHER(2),
    STUDENT(3);

    private final int value;

    Roles(int value) {
        this.value = value;
    }
}
