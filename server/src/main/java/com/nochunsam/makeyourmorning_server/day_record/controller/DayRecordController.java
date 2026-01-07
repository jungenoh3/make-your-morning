package com.nochunsam.makeyourmorning_server.day_record.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nochunsam.makeyourmorning_server.day_record.model.DayRecord;
import com.nochunsam.makeyourmorning_server.day_record.model.DayRecordDTO;
import com.nochunsam.makeyourmorning_server.day_record.service.DayRecordService;

@RestController
@RequestMapping("/day-record")
public class DayRecordController {
    private final DayRecordService dayRecordService;

    public DayRecordController(DayRecordService dayRecordService) {
        this.dayRecordService = dayRecordService;
    }
    
    @GetMapping("/all")
    public List<DayRecord> getDayRecordPage() {
        return dayRecordService.getAllDayRecords();
    }

    @GetMapping("/user")
    public List<DayRecord> getDayRecordsByUserId(String userId) {
        return dayRecordService.getDayRecordsByUserId(userId);
    }

    @PostMapping("/create")
    public DayRecord createDayRecord(@RequestBody DayRecordDTO dayRecordDTO) {
        DayRecord dayRecord = new DayRecord(dayRecordDTO.getUserId(), dayRecordDTO.getDate(), dayRecordDTO.getMinute());
        return dayRecordService.save(dayRecord);
    }
}
