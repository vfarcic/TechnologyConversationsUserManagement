package com.technologyconversations.usermanagement;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import static com.technologyconversations.usermanagement.StatusEnum.*;

@Entity
@Table(name = "User")
@XmlRootElement
public class User {

    public User() {}

    public User(String userName) {
        this.setUserName(userName);
    }

    @Id
    @Column(name = "user_name")
    private String userName;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password")
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "full_name")
    private String fullName;
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "updated")
    private Date updated;
    public Date getUpdated() {
        if (updated == null) {
            updated = new Date();
        }
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }


    private StatusEnum status = OK;
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    private String statusMessage;
    public String getStatusMessage() {
        return statusMessage;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!fullName.equals(user.fullName)) return false;
        if (!userName.equals(user.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }
}
