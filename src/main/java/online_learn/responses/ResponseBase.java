package online_learn.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResponseBase {

    private int code;
    private Map<String, Object> data;

    public ResponseBase() {
    }

    public ResponseBase(int code, Map<String, Object> data) {
        this.code = code;
        this.data = data;
    }
}
