package com.yuth.sql.helper.model;

import java.util.List;

public class AclNodeMModel {

    /** code */
    private String name;

    private String path;

    private AclMetaModel meta;

    private List<AclNodeMModel> children;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AclMetaModel getMeta() {
        return meta;
    }

    public void setMeta(AclMetaModel meta) {
        this.meta = meta;
    }

    public List<AclNodeMModel> getChildren() {
        return children;
    }

    public void setChildren(List<AclNodeMModel> children) {
        this.children = children;
    }

}
