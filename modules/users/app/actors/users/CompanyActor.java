package actors.users;

import akka.actor.AbstractActor;
import models.users.Company;
import play.Logger;
import protocols.users.CompanyProtocol;
import repositories.users.CompanyRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class CompanyActor extends AbstractActor {

    private final CompanyRepository companyRepository;

    @Inject
    public CompanyActor(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CompanyProtocol.class, companyProtocol -> {

            switch (companyProtocol.getCompanyAction()){

                case GET_ALL:
                    sender().tell(getAll(), self());
                    break;
                case GET_BY_ID:
                    sender().tell(getById(companyProtocol.getId()), self());
                    break;
                case GET_BY_NAME:
                    sender().tell(getByName(companyProtocol.getCompanyData()), self());
                    break;
                case GET_BY_EMAIL:
                    sender().tell(getByEmail(companyProtocol.getCompanyData()),self());
                    break;
                case GET_BY_PHONE:
                    sender().tell(getByPhone(companyProtocol.getCompanyData()), self());
                    break;
                case GET_BY_TAX_NUMBER:
                    sender().tell(getByTaxNumber(companyProtocol.getCompanyData()), self());
                    break;
                case ADD:
                    sender().tell(add(companyProtocol.getCompany()), self());
                    break;
                case EDIT:
                    sender().tell(update(companyProtocol.getCompany()), self());
                    break;
                case DELETE:
                    sender().tell(delete(companyProtocol.getId()), self());
                    break;
            }
        }).matchAny(any -> unhandled("unhandled" + any.getClass())).build();
    }

    private List<Company> getAll(){

        try {

            return companyRepository.getAll().toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return null;
        }
    }

    private Optional<Company> getById(Long id){

        try {

            return companyRepository.getById(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> getByName(String companyData){

        try {

            return companyRepository.getByName(companyData).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> getByEmail(String companyData){

        try {

            return companyRepository.getByEmail(companyData).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> getByPhone(String companyData){

        try {

            return companyRepository.getByPhone(companyData).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> getByTaxNumber(String companyData){

        try {

            return companyRepository.getByTaxNumber(companyData).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> add(Company company){

        try {

            return companyRepository.addCompany(company).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> update(Company company){

        try {

            return companyRepository.updateCompany(company).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Company> delete(Long id){

        try {

            return companyRepository.deleteCompany(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
