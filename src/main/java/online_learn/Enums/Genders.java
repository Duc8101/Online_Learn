package online_learn.Enums;

import lombok.Getter;

@Getter
public enum Genders {

    MALE(0),
    FEMALE(1),
    OTHER(2);

    private final int value;

    Genders(int value) {
        this.value = value;
    }
}
