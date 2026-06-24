package in.belenits.response;

import lombok.Data;

@Data
public class CustomApiResponse<T> {

    private Integer status;
    private String message;
    private T data;
}