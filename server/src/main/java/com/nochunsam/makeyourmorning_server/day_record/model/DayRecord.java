package com.nochunsam.makeyourmorning_server.day_record.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DayRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    private String userId;
    private Date date;
    private Long minute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    DayRecord() {
    }

    public DayRecord(String userId, Date date, Long minute) {
        this.uuid = UUID.randomUUID();
        this.userId = userId;
        this.date = date;
        this.minute = minute;
    }

    public DayRecord(UUID uuid, String userId, Date date, Long minute) {
        this.uuid = uuid;
        this.userId = userId;
        this.date = date;
        this.minute = minute;
    }
}