package com.technologyconversations.usermanagement;

public class Status {

    public Status() { }

    public Status(StatusEnum status) {
        this.setStatus(status);
    }

    private StatusEnum status;
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
