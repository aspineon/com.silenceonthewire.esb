package defaultData;

import io.ebean.Finder;
import models.users.Company;

import java.util.Date;

public class DefaultCompanies {

    private Long firstCompanyId = 1L;
    private Long secondCompanyId = 2L;

    private String firstName = "Example1";
    private String secondName = "Example2";

    private String firstPhone = "0";
    private String secondPhone = "1";

    private String firstEmail = "company1@example.com";
    private String secondEmail = "company2@example.com";

    private String firstTaxNumber = "1";
    private String secondTaxNumber = "2";

    private String address = "Street 1";
    private String city = "City";
    private String state = "State";
    private String country = "country";
    private String postalCode = "1";

    public void createCompanies(){

        createCompany(this.firstCompanyId,this.firstName, this.firstPhone, this.firstEmail, this.firstTaxNumber);
        createCompany(this.secondCompanyId, this.secondName, this.secondPhone, this.secondEmail, this.secondTaxNumber);
    }

    public void deleteCompanies(){

        Finder<Long, Company> finder = new Finder<Long, Company>(Company.class);

        finder.all().forEach(company -> company.delete());
    }

    private void createCompany(Long id, String name, String phone, String email, String taxNumber){

        Company company = new Company();

        company.id = id;
        company.createdAt = new Date();
        company.updatedAt = new Date();
        company.name = name;
        company.email = email;
        company.phone = phone;
        company.taxNumber = taxNumber;
        company.address = this.address;
        company.city = this.city;
        company.state = this.state;
        company.country = this.country;
        company.postalCode = this.postalCode;

        company.save();
    }
}
