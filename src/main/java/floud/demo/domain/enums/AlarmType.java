package floud.demo.domain.enums;

import lombok.Getter;

@Getter
public enum AlarmType {
    MEMOIR("MEMOIR"),
    FRIEND("FRIEND");

    private final String alarmType;

    AlarmType(String alarmType){
        this.alarmType = alarmType;
    }

}
