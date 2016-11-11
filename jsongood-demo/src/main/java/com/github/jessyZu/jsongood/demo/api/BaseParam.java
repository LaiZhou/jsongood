package com.github.jessyZu.jsongood.demo.api;

/**
 * Created by zhoulai on 16/10/20.
 */
public class BaseParam {
    private String kk;

    public String getKk() {
        return kk;
    }

    public void setKk(String kk) {
        this.kk = kk;
    }

    @Override
    public String toString() {
        return "BaseParam{" +
                "kk='" + kk + '\'' +
                '}';
    }
}
