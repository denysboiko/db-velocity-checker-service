package com.tryvault.velocityservice.limits;

import com.tryvault.velocityservice.repo.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Service
public class WeeklyVelocityLimit extends VelocityLimit {
    public WeeklyVelocityLimit(TransactionsRepository transactionsRepository) {
        super(transactionsRepository);
        this.period = "Weekly";
        this.limit = 20000.00;
        this.startingDate = TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY);
    }
}
