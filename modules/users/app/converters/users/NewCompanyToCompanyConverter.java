package converters.users;

import models.users.Company;
import models.users.NewCompany;

import java.util.Date;
import java.util.function.Function;

public class NewCompanyToCompanyConverter implements Function<NewCompany, Company> {
    @Override
    public Company apply(NewCompany newCompany) {
        Company company = new Company();

        company.id = System.currentTimeMillis();
        company.createdAt = new Date();
        company.updatedAt = new Date();
        company.name = newCompany.name;
        company.email = newCompany.email;
        company.phone = newCompany.phone;
        company.taxNumber = newCompany.taxNumber;
        company.address = newCompany.address;
        company.city = newCompany.city;
        company.state = newCompany.state;
        company.country = newCompany.country;
        company.postalCode = newCompany.postalCode;

        return company;
    }
}
