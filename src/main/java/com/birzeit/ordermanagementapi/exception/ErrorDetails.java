package com.birzeit.ordermanagementapi.exception;

import com.birzeit.ordermanagementapi.LocalDateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class ErrorDetails {
    private String timeStamp;
    private String message;
    private String details;
    private int status;

    public ErrorDetails(LocalDateTime timeStamp, String message, String details, int status) {
        this.timeStamp = LocalDateTimeFormatter.formatDateAndTime(timeStamp);
        this.message = message;
        this.details = details;
        this.status = status;
    }
}