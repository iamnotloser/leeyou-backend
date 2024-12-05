package com.leeyoubackend.pojo.enums;

/**
 * 队伍状态枚举
 */
public enum TeamStausEnum {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");
    private int value;

    private String text;

    public static TeamStausEnum getEnumByValue(Integer value){
        if(value == null){
            return null;
        }
        for (TeamStausEnum teamStausEnum : TeamStausEnum.values()) {
            if(teamStausEnum.value == value){
                return teamStausEnum;
            }
        }
        return null;
    }

     TeamStausEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
