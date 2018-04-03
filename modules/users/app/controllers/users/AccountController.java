package controllers.users;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import converters.users.AccountToAccountConverter;
import converters.users.CurrentAccountToAccountConverter;
import converters.users.NewAccountToAccountConverter;
import models.users.Account;
import models.users.CurrentAccount;
import models.users.NewAccount;
import parsers.users.CurrentAccountParser;
import parsers.users.NewAccountParser;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import protocols.users.AccountAction;
import protocols.users.AccountProtocol;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AccountController extends Controller {

    private final ActorRef actorRef;
    private final AccountToAccountConverter accountToAccountConverter;

    @Inject
    public AccountController(
            @Named("company-actor") ActorRef actorRef,
            AccountToAccountConverter accountToAccountConverter
    ) {
        this.actorRef = actorRef;
        this.accountToAccountConverter = accountToAccountConverter;
    }

    public CompletionStage<Result> byId(Long id){
        try {
            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.GET_BY_ID, id), 1000)
                    .thenApply(accountOptional -> (Optional<Account>) accountOptional).thenApply(
                            accountOptional -> accountOptional.map(accountToAccountConverter)
                                    .map(account -> ok(Json.toJson(account))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byUser(Long id){

        try {


            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.GET_BY_USER, id), 1000)
                    .thenApply(accountOptional -> (Optional<Account>) accountOptional).thenApply(
                            accountOptional -> accountOptional.map(accountToAccountConverter)
                                    .map(account -> ok(Json.toJson(account))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> bySsn(String ssn){

        try {

            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.GET_BY_SSN, ssn), 1000)
                    .thenApply(accountOptional -> (Optional<Account>) accountOptional)
                    .thenApply(accountOptional -> accountOptional.map(accountToAccountConverter)
                            .map(account -> ok(Json.toJson(account))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byTaxNumber(String taxNumber){

        try {

            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.GET_BY_TAX_NUMBER, taxNumber), 1000)
                    .thenApply(optionalAccount -> (Optional<Account>) optionalAccount).thenApply(
                            optionalAccount -> optionalAccount.map(accountToAccountConverter)
                                    .map(account -> ok(Json.toJson(account))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    @BodyParser.Of(NewAccountParser.class)
    public CompletionStage<Result> create(){

        try {

            NewAccount jsonAccount = Json.fromJson(request().body().asJson(), NewAccount.class);
            Account account = new NewAccountToAccountConverter().apply(jsonAccount);

            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.ADD, account), 1000)
                    .thenApply(accountOptional -> (Optional<Account>) accountOptional)
                    .thenApply(accountOptional -> accountOptional.map(accountToAccountConverter)
                            .map(currentAccount -> ok(Json.toJson(currentAccount))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    @BodyParser.Of(CurrentAccountParser.class)
    public CompletionStage<Result> update(){

        try {
            CurrentAccount currentAccount = Json.fromJson(request().body().asJson(), CurrentAccount.class);
            Account account = new CurrentAccountToAccountConverter().apply(currentAccount);
            return PatternsCS.ask(actorRef, new AccountProtocol(AccountAction.UPDATE, account), 1000)
                    .thenApply(accountOptional -> (Optional<Account>) accountOptional)
                    .thenApply(accountOptional -> accountOptional.map(accountToAccountConverter)
                            .map(updatedAccount -> ok(Json.toJson(updatedAccount))).orElse(notAcceptable()));
        } catch (Exception e){

            Logger.error(e.getMessage(),e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }
}
