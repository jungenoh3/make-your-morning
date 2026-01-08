package com.nochunsam.makeyourmorning_server.day_record.model;

import java.sql.Date;

public class DayRecordDTO {
    private Date date;
    private Long minute;

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Long getMinute() {
        return minute;
    }
    public void setMinute(Long minute) {
        this.minute = minute;
    }
}
