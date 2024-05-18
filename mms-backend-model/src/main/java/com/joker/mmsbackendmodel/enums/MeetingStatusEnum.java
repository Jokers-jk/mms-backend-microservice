package com.joker.mmsbackendmodel.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 会议室状态
 */
public enum MeetingStatusEnum {

    READY("可预定",false),
    MAINTAIN("维护中",true);

    private final String text;

    private final Boolean value;

    MeetingStatusEnum(String text, Boolean value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Boolean> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static MeetingStatusEnum getEnumByValue(Long value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MeetingStatusEnum anEnum : MeetingStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Boolean getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
