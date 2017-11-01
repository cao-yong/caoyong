package com.caoyong.core.bean.menu;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuTreeVO implements Serializable {
    private static final long serialVersionUID = -6479900153811480248L;

    public MenuTreeVO(Menu menu) {
        this.id = menu.getId();
        this.pId = menu.getParentId();
        this.name = menu.getName();
        this.open = true;
    }

    public MenuTreeVO() {

    }

    private String  id;
    private String  pId;
    private String  name;
    private boolean open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public String getpId() {
        return pId;
    }

    @JsonProperty
    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
