package protocols.users;

import models.users.EmailAndPassword;
import models.users.NewUser;
import models.users.PhoneAndPassword;
import models.users.User;

public class UserProtocol {

    private UserAction userAction;
    private User user;
    private Long id;
    private String userdata;
    private NewUser newUser;
    private EmailAndPassword emailAndPassword;
    private PhoneAndPassword phoneAndPassword;

    public UserProtocol(UserAction userAction){

        this.userAction = userAction;
    }

    public UserProtocol(UserAction userAction, Long id){

        this.userAction = userAction;
        this.id = id;
    }

    public UserProtocol(UserAction userAction, String userdata){

        this.userAction = userAction;
        this.userdata = userdata;
    }

    public UserProtocol(UserAction userAction, User user){

        this.userAction = userAction;
        this.user = user;
    }

    public UserProtocol(UserAction userAction, NewUser newUser){

        this.userAction = userAction;
        this.newUser = newUser;
    }

    public UserProtocol(UserAction userAction, EmailAndPassword emailAndPassword){

        this.userAction = userAction;
        this.emailAndPassword = emailAndPassword;
    }

    public UserProtocol(UserAction userAction, PhoneAndPassword phoneAndPassword){

        this.userAction = userAction;
        this.phoneAndPassword = phoneAndPassword;
    }
    public UserProtocol(UserAction userAction, Long id, NewUser newUser){

        this.userAction = userAction;
        this.id = id;
        this.newUser = newUser;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public User getUser() {
        return user;
    }

    public NewUser getNewUser() {
        return newUser;
    }

    public Long getId(){

        return id;
    }

    public String getUserdata() {
        return userdata;
    }

    public EmailAndPassword getEmailAndPassword() {
        return emailAndPassword;
    }

    public PhoneAndPassword getPhoneAndPassword() {
        return phoneAndPassword;
    }
}
