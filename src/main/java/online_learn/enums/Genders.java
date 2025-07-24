package online_learn.enums;

import lombok.Getter;

@Getter
public enum Genders {

    Male(0),
    Female(1),
    Other(2);

    private final int value;

    Genders(int value) {
        this.value = value;
    }
}
