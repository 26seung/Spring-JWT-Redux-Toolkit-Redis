package com.euseung.backend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CMResponse<T> {

    private int code;           //  1(성공) / -1(실패)
    private String message;
    private T data;

    public static <T> CMResponse successResponse(String message, T data) {
        return CMResponse.builder()
                .code(1)
                .message(message)
                .data(data)
                .build();
    }
    public static <T> CMResponse errorResponse(String message, T data) {
        return CMResponse.builder()
                .code(-1)
                .message(message)
                .data(data)
                .build();
    }

}
