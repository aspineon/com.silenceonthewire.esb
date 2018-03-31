package controllers.users;

import actors.users.UserActor;
import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import converters.users.NewUserToUserConverter;
import converters.users.UserToJsonConverter;
import models.users.EmailAndPassword;
import models.users.NewUser;
import models.users.PhoneAndPassword;
import models.users.User;
import parsers.users.EmailAndPasswordParser;
import parsers.users.NewUserParser;
import parsers.users.PhoneAndPasswordParser;
import play.Logger;
import play.mvc.BodyParser;
import protocols.users.Action;
import protocols.users.UserProtocol;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Singleton
public class UserController extends Controller{

    private final ActorRef userActor;
    private final UserToJsonConverter userToJsonConverter;

    @Inject
    public UserController(
            @Named("user-actor") ActorRef userActor, UserToJsonConverter userToJsonConverter
    ) {

        this.userActor = userActor;
        this.userToJsonConverter = userToJsonConverter;
    }


    public CompletionStage<Result> getAll(){

        return PatternsCS.ask(userActor, new UserProtocol(Action.GET_ALL), 1000)
                .thenApply(response -> (List<User>) response).thenApply(
                        list -> ok(Json.toJson(list.stream().map(userToJsonConverter).collect(Collectors.toList()))
                        )
                );
    }

    public CompletionStage<Result> addUser(){

        try {
            NewUser newUser = Json.fromJson(request().body().asJson(), NewUser.class);
            System.out.println(newUser.email);
            return PatternsCS.ask(userActor, new UserProtocol(Action.ADD_USER, newUser), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> editUser(Long id){

        try {
            NewUser newUser = Json.fromJson(request().body().asJson(), NewUser.class);
            return PatternsCS.ask(userActor, new UserProtocol(Action.EDIT_USER, id, newUser), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> deleteUser(Long id){

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.DELETE_USER, id), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> getById(Long id){

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.GET_BY_ID, id), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> getByEmail(String email){

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.GET_BY_EMAIL, email), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notFound());
        }
    }

    public CompletionStage<Result> getByPhone(String phone){

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.GET_BY_PHONE, phone), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(
                            userOptional -> userOptional.map(userToJsonConverter)
                                    .map(currentUser -> ok(Json.toJson(currentUser))).orElse(notFound()));
        } catch (Exception e){
            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> getByEmailAndPassword(){

        EmailAndPassword emailAndPassword = Json.fromJson(request().body().asJson(), EmailAndPassword.class);

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.GET_BY_EMAIL_AND_PASSWORD, emailAndPassword), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(userOptional -> userOptional
                            .map(userToJsonConverter).map(currentUser -> ok(Json.toJson(currentUser))).orElse(notFound()));
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> getByPhoneAndPassword(){

        PhoneAndPassword phoneAndPassword = Json.fromJson(request().body().asJson(), PhoneAndPassword.class);

        try {
            return PatternsCS.ask(userActor, new UserProtocol(Action.GET_BY_PHONE_AND_PASSWORD, phoneAndPassword), 1000)
                    .thenApply(response -> (Optional<User>) response).thenApply(userOptional -> userOptional
                            .map(userToJsonConverter).map(currentUser -> ok(Json.toJson(currentUser))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }
}
