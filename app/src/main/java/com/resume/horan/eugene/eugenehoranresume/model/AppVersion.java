package com.resume.horan.eugene.eugenehoranresume.model;


public class AppVersion {
    public int version;
    public String link;


    public AppVersion() {
    }

    public AppVersion(int version, String link) {
        this.version = version;
        this.link = link;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
