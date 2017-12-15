package com.hrmp.bean;

import java.io.Serializable;

/**
 * Created by Ly on 2017/5/14.
 */

public class HandleHistoryBean implements Serializable{
    private String handleDate;
    private String handleName;
    private String operate;
    private String content;

    public String getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(String handleDate) {
        this.handleDate = handleDate;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
