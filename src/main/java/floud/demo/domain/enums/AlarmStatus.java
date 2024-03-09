package floud.demo.domain.enums;

import lombok.Getter;

@Getter
public enum AlarmStatus {
    MEMOIR("MEMOIR"),
    FRIEND("FRIEND");

    private final String alarmStatus;

    AlarmStatus(String alarmStatus){
        this.alarmStatus = alarmStatus;
    }

}
