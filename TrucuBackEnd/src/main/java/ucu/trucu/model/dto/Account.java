package ucu.trucu.model.dto;

import java.sql.Date;

/**
 *
 * @author NicoPuig
 */
public class Account {

    private String email;
    private String name;
    private String lastName;
    private Date birthDate;
    private String password;
    private String rolName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    public String getRolName() {
        return rolName;
    }
}
