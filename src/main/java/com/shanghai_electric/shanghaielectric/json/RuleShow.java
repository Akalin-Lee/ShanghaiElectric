package com.shanghai_electric.shanghaielectric.json;

import java.util.List;

/**
 * Created by Akalin on 2017/3/12.
 */

public class RuleShow {
    private String device;
    private String component;
    private String trouble_name;
    private String fuzzy_word;
    private List<ExplainItem> trouble_explain;

    public String getFuzzy_word() {
        return fuzzy_word;
    }

    public void setFuzzy_word(String fuzzy_word) {
        this.fuzzy_word = fuzzy_word;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTrouble_name() {
        return trouble_name;
    }

    public void setTrouble_name(String trouble_name) {
        this.trouble_name = trouble_name;
    }

    public List<ExplainItem> getTrouble_explain() {
        return trouble_explain;
    }

    public void setTrouble_explain(List<ExplainItem> trouble_explain) {
        this.trouble_explain = trouble_explain;
    }





}

