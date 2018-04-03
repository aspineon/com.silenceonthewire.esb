package controllers.users;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import converters.users.CompanyToCompanyConverter;
import converters.users.NewCompanyToCompanyConverter;
import models.users.Company;
import models.users.CurrentCompany;
import models.users.CurrentCompanyToCompanyCoverter;
import models.users.NewCompany;
import parsers.users.CurrentCompanyParser;
import parsers.users.NewCompanyParser;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import protocols.users.CompanyAction;
import protocols.users.CompanyProtocol;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Singleton
public class CompanyController extends Controller {

    private final ActorRef actorRef;
    private final CompanyToCompanyConverter companyToCompanyConverter;

    @Inject
    public CompanyController(
            @Named("company-actor") ActorRef actorRef,
            CompanyToCompanyConverter companyToCompanyConverter
    ) {
        this.actorRef = actorRef;
        this.companyToCompanyConverter = companyToCompanyConverter;
    }

    public CompletionStage<Result> all(){

        try {
            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_ALL), 1000)
                    .thenApply(response -> (List<Company>) response)
                    .thenApply(
                            list -> ok(
                                    Json.toJson(
                                            list.stream().map(companyToCompanyConverter).collect(Collectors.toList())
                                    )
                            )
                    );
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(badRequest());
        }
    }

    public CompletionStage<Result> byId(Long id){

        try {

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_BY_ID, id), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byName(String name){

        try {

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_BY_NAME, name), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byEmail(String email){

        try {

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_BY_EMAIL, email), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byPhone(String phone){

        try {

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_BY_PHONE, phone), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> byTaxNumber(String taxNumber){

        try {

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.GET_BY_TAX_NUMBER, taxNumber), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany ->ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    @BodyParser.Of(NewCompanyParser.class)
    public CompletionStage<Result> add(){

        try {

            NewCompany newCompany = Json.fromJson(request().body().asJson(), NewCompany.class);
            Company company = new NewCompanyToCompanyConverter().apply(newCompany);

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.ADD, company), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalResponse -> optionalResponse.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(badRequest()));
        } catch (Exception e) {

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    @BodyParser.Of(CurrentCompanyParser.class)
    public CompletionStage<Result> update(){

        try {

            CurrentCompany jsonCompany = Json.fromJson(request().body().asJson(), CurrentCompany.class);
            Company company = new CurrentCompanyToCompanyCoverter().apply(jsonCompany);

            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.ADD, company), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalResponse -> optionalResponse.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(badRequest()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }

    public CompletionStage<Result> delete(Long id){

        try {
            return PatternsCS.ask(actorRef, new CompanyProtocol(CompanyAction.DELETE, id), 1000)
                    .thenApply(response -> (Optional<Company>) response).thenApply(
                            optionalCompany -> optionalCompany.map(companyToCompanyConverter).map(
                                    currentCompany -> ok(Json.toJson(currentCompany))).orElse(notFound()));
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(notAcceptable());
        }
    }
}
