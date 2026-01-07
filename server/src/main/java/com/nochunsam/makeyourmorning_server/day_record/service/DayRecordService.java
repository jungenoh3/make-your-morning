package com.nochunsam.makeyourmorning_server.day_record.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.nochunsam.makeyourmorning_server.day_record.model.DayRecord;
import com.nochunsam.makeyourmorning_server.day_record.repository.DayRecordRepository;

@Service
public class DayRecordService {
    private final DayRecordRepository dayRecordRepository;

    public DayRecordService(DayRecordRepository dayRecordRepository) {
        this.dayRecordRepository = dayRecordRepository;
    }

    public List<DayRecord> getAllDayRecords() {
        return dayRecordRepository.findAll();
    }

    public List<DayRecord> getDayRecordsByUserId(String userId) {
        return dayRecordRepository.findByUserId(userId);
    }

    public DayRecord save(DayRecord dayRecord) {
        return dayRecordRepository.save(dayRecord);
    }
}