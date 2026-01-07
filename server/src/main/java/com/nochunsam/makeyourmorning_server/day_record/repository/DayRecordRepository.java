package com.nochunsam.makeyourmorning_server.day_record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.nochunsam.makeyourmorning_server.day_record.model.DayRecord;

@Repository
public interface DayRecordRepository extends JpaRepository<DayRecord, Long> {
    public List<DayRecord> findByUserId(String userId);
}
