
package com.resume.horan.eugene.eugenehoranresume.model;


public class Goals {

    private String goalCurrent;
    private String goalFuture;

    /**
     * No args constructor for use in serialization
     */
    public Goals() {
    }

    /**
     * @param goalCurrent
     * @param goalFuture
     */
    public Goals(String goalCurrent, String goalFuture) {
        super();
        this.goalCurrent = goalCurrent;
        this.goalFuture = goalFuture;
    }

    public String getGoalCurrent() {
        return goalCurrent;
    }

    public void setGoalCurrent(String goalCurrent) {
        this.goalCurrent = goalCurrent;
    }

    public String getGoalFuture() {
        return goalFuture;
    }

    public void setGoalFuture(String goalFuture) {
        this.goalFuture = goalFuture;
    }

}
