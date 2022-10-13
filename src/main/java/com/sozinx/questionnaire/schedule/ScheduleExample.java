package com.sozinx.questionnaire.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@SuppressWarnings("unused")
public class ScheduleExample {

    static final Logger LOGGER = Logger.getLogger(ScheduleExample.class.getName());

    //@Scheduled(cron = "@daily")
    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    @Scheduled(cron = "${interval-in-cron-every-minute}")
    public void doSomeJob() {
        LOGGER.log(Level.INFO, "Scheduled msg: {0}", (new Date()).toString());
    }
}
