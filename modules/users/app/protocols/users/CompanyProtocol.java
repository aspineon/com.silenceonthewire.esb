package protocols.users;

import models.users.Company;

public class CompanyProtocol {

    private CompanyAction companyAction;
    private Company company;
    private Long id;
    private String companyData;

    public CompanyProtocol(){}

    public CompanyProtocol(CompanyAction companyAction){

        this.companyAction = companyAction;
    }

    public CompanyProtocol(CompanyAction companyAction, Company company){

        this.companyAction = companyAction;
        this.company = company;
    }

    public CompanyProtocol(CompanyAction companyAction, Long id){

        this.companyAction = companyAction;
        this.id = id;
    }

    public CompanyProtocol(CompanyAction companyAction, String companyData){

        this.companyAction = companyAction;
        this.companyData = companyData;
    }

    public Company getCompany() {
        return company;
    }

    public CompanyAction getCompanyAction() {
        return companyAction;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyData(){

        return companyData;
    }
}
