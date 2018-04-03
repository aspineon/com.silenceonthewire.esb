package converters.users;

import models.users.Company;

import java.util.function.Function;

public class CompanyToCompanyConverter implements Function<Company, Company>{

    @Override
    public Company apply(Company company) {
        return company;
    }
}
