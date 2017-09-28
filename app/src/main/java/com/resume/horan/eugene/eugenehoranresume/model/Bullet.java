
package com.resume.horan.eugene.eugenehoranresume.model;


public class Bullet {

    private String order;
    private String bullet;

    /**
     * No args constructor for use in serialization
     */
    public Bullet() {
    }

    /**
     * @param order
     * @param bullet
     */
    public Bullet(String order, String bullet) {
        super();
        this.order = order;
        this.bullet = bullet;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getBullet() {
        return bullet;
    }

    public void setBullet(String bullet) {
        this.bullet = bullet;
    }

}
