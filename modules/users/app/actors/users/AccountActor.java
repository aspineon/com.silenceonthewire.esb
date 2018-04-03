package actors.users;

import akka.actor.AbstractActor;
import models.users.Account;
import play.Logger;
import protocols.users.AccountProtocol;
import repositories.users.AccountRepository;

import javax.inject.Inject;
import java.util.Optional;

public class AccountActor extends AbstractActor {

    private final AccountRepository accountRepository;

    @Inject
    public AccountActor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AccountProtocol.class, accountProtocol -> {

            switch (accountProtocol.getAccountAction()){

                case GET_BY_ID:
                    sender().tell(getById(accountProtocol.getId()), self());
                    break;
                case GET_BY_USER:
                    sender().tell(getByUser(accountProtocol.getId()), self());
                    break;
                case GET_BY_SSN:
                    sender().tell(getBySsn(accountProtocol.getAccountData()), self());
                    break;
                case GET_BY_TAX_NUMBER:
                    sender().tell(getByTaxNumber(accountProtocol.getAccountData()), self());
                    break;
                case ADD:
                    sender().tell(create(accountProtocol.getAccount()), self());
                    break;
                case UPDATE:
                    sender().tell(update(accountProtocol.getAccount()), self());
                    break;
            }
        }).matchAny(any -> unhandled("unhandled" + any.getClass())).build();
    }

    private Optional<Account> getById(Long id){

        try {

            return accountRepository.getById(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Account> getByUser(Long id){

        try {

            return accountRepository.getByUser(id).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Account> getByTaxNumber(String taxNumber){

        try {

            return accountRepository.getByTaxNumber(taxNumber).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(),e);
            return Optional.empty();
        }
    }

    private Optional<Account> getBySsn(String ssn){

        try {

            return accountRepository.getBySsn(ssn).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Account> create(Account account){

        Logger.info("Create account actor.");
        try {

            return accountRepository.create(account).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<Account> update(Account account){

        try {

            return accountRepository.update(account).toCompletableFuture().get();
        } catch (Exception e){

            Logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
