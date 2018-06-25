package com.francis.ktv.dto;

import java.io.Serializable;

public class ReqDto implements Serializable {

    private Integer controlType;

    private String content;

    public Integer getControlType() {
        return controlType;
    }

    public void setControlType(Integer controlType) {
        this.controlType = controlType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
