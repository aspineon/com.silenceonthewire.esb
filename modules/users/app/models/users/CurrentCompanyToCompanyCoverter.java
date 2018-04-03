package models.users;

import java.util.Date;
import java.util.function.Function;

public class CurrentCompanyToCompanyCoverter implements Function<CurrentCompany, Company> {
    @Override
    public Company apply(CurrentCompany currentCompany) {

        Company company = new Company();

        company.id = currentCompany.id;
        company.createdAt = currentCompany.createdAt;
        company.updatedAt = new Date();
        company.name = currentCompany.name;
        company.email = currentCompany.email;
        company.phone = currentCompany.phone;
        company.taxNumber = currentCompany.taxNumber;
        company.address = currentCompany.address;
        company.city = currentCompany.city;
        company.state = currentCompany.state;
        company.country = currentCompany.country;
        company.postalCode = currentCompany.postalCode;

        return company;
    }
}
