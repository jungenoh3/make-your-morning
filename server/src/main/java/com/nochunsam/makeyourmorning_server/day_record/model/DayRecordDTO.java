package com.nochunsam.makeyourmorning_server.day_record.model;

import java.time.OffsetDateTime;

public class DayRecordDTO {
    private OffsetDateTime date;
    private Long minute;

    public OffsetDateTime getDate() {
        return date;
    }
    public void setDate(OffsetDateTime date) {
        this.date = date;
    }
    public Long getMinute() {
        return minute;
    }
    public void setMinute(Long minute) {
        this.minute = minute;
    }
}
