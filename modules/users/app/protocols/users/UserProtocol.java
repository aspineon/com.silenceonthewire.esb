package protocols.users;

import models.users.EmailAndPassword;
import models.users.NewUser;
import models.users.PhoneAndPassword;
import models.users.User;

public class UserProtocol {

    private Action action;
    private User user;
    private Long id;
    private String userdata;
    private NewUser newUser;
    private EmailAndPassword emailAndPassword;
    private PhoneAndPassword phoneAndPassword;

    public UserProtocol(Action action){

        this.action = action;
    }

    public UserProtocol(Action action, Long id){

        this.action = action;
        this.id = id;
    }

    public UserProtocol(Action action, String userdata){

        this.action = action;
        this.userdata = userdata;
    }

    public UserProtocol(Action action, User user){

        this.action = action;
        this.user = user;
    }

    public UserProtocol(Action action, NewUser newUser){

        this.action = action;
        this.newUser = newUser;
    }

    public UserProtocol(Action action, EmailAndPassword emailAndPassword){

        this.action = action;
        this.emailAndPassword = emailAndPassword;
    }

    public UserProtocol(Action action, PhoneAndPassword phoneAndPassword){

        this.action = action;
        this.phoneAndPassword = phoneAndPassword;
    }
    public UserProtocol(Action action, Long id, NewUser newUser){

        this.action = action;
        this.id = id;
        this.newUser = newUser;
    }

    public Action getAction() {
        return action;
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
