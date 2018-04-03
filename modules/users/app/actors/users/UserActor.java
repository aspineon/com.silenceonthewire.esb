package actors.users;

import akka.actor.AbstractActor;
import converters.users.NewUserToUserConverter;
import models.users.EmailAndPassword;
import models.users.NewUser;
import models.users.PhoneAndPassword;
import models.users.User;
import play.Logger;
import protocols.users.UserProtocol;
import repositories.users.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserActor extends AbstractActor {

    private final UserRepository userRepository;

    @Inject
    public UserActor(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(UserProtocol.class, userProtocol -> {

            switch (userProtocol.getUserAction()){

                case ADD_USER:
                    sender().tell(addUser(userProtocol.getNewUser()), self());
                    break;
                case EDIT_USER:
                    sender().tell(updateUser(userProtocol.getId(), userProtocol.getNewUser()), self());
                    break;
                case DELETE_USER:
                    sender().tell(deleteUser(userProtocol.getId()), self());
                    break;
                case GET_ALL:
                    sender().tell(getAll(), self());
                    break;
                case GET_BY_ID:
                    sender().tell(getById(userProtocol.getId()), self());
                    break;
                case GET_BY_EMAIL:
                    sender().tell(getUserByEmail(userProtocol.getUserdata()), self());
                    break;
                case GET_BY_PHONE:
                    sender().tell(getUserByPhone(userProtocol.getUserdata()), self());
                    break;
                case GET_BY_EMAIL_AND_PASSWORD:
                    sender().tell(getUserByEmailAndPassword(userProtocol.getEmailAndPassword()), self());
                    break;
                case GET_BY_PHONE_AND_PASSWORD:
                    sender().tell(getUserByPhoneAndPassword(userProtocol.getPhoneAndPassword()), self());
                    break;
                case GET_BY_COMPANY:
                    sender().tell(getUsersByCompany(userProtocol.getId()), self());
            }
        }).matchAny(any -> unhandled("unhandled" + any.getClass())).build();
    }

    private List<User> getAll(){

        try {

            return userRepository.getAll().toCompletableFuture().get();
        } catch ( Exception e ){

            Logger.error(e.getMessage(), e);
            return null;
        }
    }

    private Optional<User> addUser(NewUser newUser){

        try {

            NewUserToUserConverter newUserToUserConverter = new NewUserToUserConverter();
            User user = newUserToUserConverter.apply(newUser);
            return userRepository.addUser(user).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<User> updateUser(Long id, NewUser newUser){

        try {

            Optional<User> optionalUser = userRepository.getById(id).toCompletableFuture().get();
            if(optionalUser.isPresent()){

                User user = optionalUser.get();
                user.firstName = newUser.firstName;
                user.lastName = newUser.lastName;
                user.setEmail(newUser.email);
                user.phone = newUser.phone;
                user.setPassword(newUser.password);
                user.isAdmin = newUser.isAdmin;
                user.updatedAt = new Date();

                return userRepository.updateUser(user).toCompletableFuture().get();
            } else {

                return Optional.empty();
            }

        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<User> deleteUser(Long id){

        try {

            return userRepository.deleteUser(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<User> getUserByEmail(String userdata){

        try {

            return userRepository.getByEmail(userdata).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(),e);
            return Optional.empty();
        }
    }

    private Optional<User> getUserByPhone(String userdata){

        try {

            return userRepository.getByPhone(userdata).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<User> getUserByEmailAndPassword(EmailAndPassword emailAndPassword){

        try {

            return userRepository.getByEmailAndPassword(emailAndPassword).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<User> getUserByPhoneAndPassword(PhoneAndPassword phoneAndPassword){

        try {

            return userRepository.getByPhoneAndPassword(phoneAndPassword).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<User> getById(Long id){

        try {

            return userRepository.getById(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private List<User> getUsersByCompany(Long id){

        try {

            return userRepository.getUsersByCompany(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return null;
        }
    }
}
