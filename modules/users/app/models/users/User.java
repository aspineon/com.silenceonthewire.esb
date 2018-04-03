package models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
public class User extends BaseModel {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String firstName;

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String lastName;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    @Constraints.Email
    public String email;

    @Column(unique = true)
    @Constraints.Required
    @Constraints.MaxLength(255)
    public String phone;

    @JsonIgnore
    @Constraints.Required
    @Constraints.MaxLength(255)
    public byte[] password;

    @Constraints.Required
    public boolean isAdmin;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    public Company company;

    public void setPassword(String password) {
        this.password = getSha512(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
