package com.technologyconversations.usermanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "user_name")
    private String userName; // TODO Máximo de 20 caracteres. Debe ser identificador único. Sólo puede contener caracteres alfanuméricos.
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password")
    private String password; // TODO Mínimo de 8 caracteres. Debe contener alguna mayúscula y algún dígito. Encriptar  el password al persistirlo.
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "full_name")
    private String fullName; // TODO Obligatorio. Máximo de 200 caracteres. Sólo debe aceptar letras y espacios.
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "updated")
    private Date updated; // TODO Debe contener la fecha de la última actualización. Este campo no debe ser editable, sólo debe visualizarse en el formulario.
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!fullName.equals(user.fullName)) return false;
        if (!password.equals(user.password)) return false;
        if (!userName.equals(user.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }
}
