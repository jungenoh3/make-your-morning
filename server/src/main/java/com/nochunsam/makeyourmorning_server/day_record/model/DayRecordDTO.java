package com.nochunsam.makeyourmorning_server.day_record.model;

import java.sql.Date;

public class DayRecordDTO {
    private String userId;
    private Date date;
    private Long minute;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
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
