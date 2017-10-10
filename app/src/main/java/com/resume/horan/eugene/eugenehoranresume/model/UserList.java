package com.resume.horan.eugene.eugenehoranresume.model;

import java.util.List;

public class UserList {
    private List<User> objectList;

    public UserList(List<User> objectList) {
        this.objectList = objectList;
    }

    public List<User> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<User> objectList) {
        this.objectList = objectList;
    }
}
