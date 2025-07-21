package online_learn.enums;

import lombok.Getter;

@Getter
public enum Answers {
    ANSWER1(1),
    ANSWER2(2),
    ANSWER3(3),
    ANSWER4(4);

    private final int value;

    Answers(int value) {
        this.value = value;
    }
}
