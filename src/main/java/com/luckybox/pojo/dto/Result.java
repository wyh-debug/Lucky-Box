package com.luckybox.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result {
    private Boolean success;
    private String msg;
    private Object data;
    private Long total;


    public static Result ok() {
        return new Result();
    }

    public static Result ok(Object data) {
        return new Result(true, null, data,null);
    }

    public static Result ok(List<?> data, Long total) {
        return new Result(true, null, data, total);
    }

    public static Result fail(String errorMsg) {
        return new Result(false, errorMsg, null, null);
    }
}
