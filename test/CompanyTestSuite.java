import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import repositories.users.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        AddCompanyTest.class,
        UpdateCompanyTest.class,
        DeleteCompanyTest.class,
        GetAllAllCompaniesTest.class,
        GetCompanyByIdTest.class,
        GetCompanyByNameTest.class,
        GetCompanyByEmailTest.class,
        GetCompanyByPhoneTest.class,
        GetCompanyByTaxNumberTest.class
})
public class CompanyTestSuite {

}
