package com.zwl.cit2;


import com.zwl.common.bean.IBean;
import com.zwl.common.bean.ICallback;

public class Bean2 implements IBean {
    private String name = "面向接口编程";

    private ICallback callback;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String paramString) {
        this.name = paramString;
    }

    @Override
    public void register(ICallback callback) {
        this.callback = callback;

        clickButton();
    }

    public void clickButton() {
        callback.sendResult("Hello: " + this.name);
    }
}
