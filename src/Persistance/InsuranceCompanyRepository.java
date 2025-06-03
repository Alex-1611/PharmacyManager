package Persistance;

import Models.InsuranceCompany;
import Persistance.Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InsuranceCompanyRepository {
    private static final String INSERT_INSURANCE = "INSERT INTO InsuranceCompany (id, name) VALUES (insurancecompanyseq.nextval, ?)";
    private static final String DELETE_INSURANCE = "DELETE FROM InsuranceCompany WHERE id = ?";
    private static final String ADD_DRUG_TO_INSURANCE = "INSERT INTO InsuranceDrug (insurance_id, drug_id) VALUES (?, ?)";
    private static final String GET_ALL_INSURANCE = "SELECT * FROM InsuranceCompany";
    private static final String GET_INSURANCE_BY_ID = "SELECT * FROM InsuranceCompany WHERE id = ?";

    private static InsuranceCompanyRepository instance;
    private static Connection connection;

    private InsuranceCompanyRepository() {
        connection = DatabaseConnection.getDatabaseConnection();
    }

    public static InsuranceCompanyRepository getInstance() {
        if (instance == null) {
            synchronized (InsuranceCompanyRepository.class) {
                if (instance == null) {
                    instance = new InsuranceCompanyRepository();
                }
            }
        }
        return instance;
    }

    public void addInsuranceCompany(String name) {
        try (var preparedStatement = connection.prepareStatement(INSERT_INSURANCE)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error adding insurance company: " + e.getMessage());
        }
    }

    public void deleteInsuranceCompany(int insuranceId) {
        try (var preparedStatement = connection.prepareStatement(DELETE_INSURANCE)) {
            preparedStatement.setInt(1, insuranceId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting insurance company: " + e.getMessage());
        }
    }

    public void addDrugToInsurance(int insuranceId, int drugId) {
        try (var preparedStatement = connection.prepareStatement(ADD_DRUG_TO_INSURANCE)) {
            preparedStatement.setInt(1, insuranceId);
            preparedStatement.setInt(2, drugId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error adding drug to insurance: " + e.getMessage());
        }
    }

    public List<InsuranceCompany> getAllInsuranceCompanies() {
        List<InsuranceCompany> companies = new ArrayList<>();
        try (var preparedStatement = connection.prepareStatement(GET_ALL_INSURANCE)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                companies.add(new InsuranceCompany(id, name));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all insurance companies: " + e.getMessage());
        }
        return companies;
    }

    public InsuranceCompany getInsuranceCompanyById(int id) {
        try (var preparedStatement = connection.prepareStatement(GET_INSURANCE_BY_ID)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return new InsuranceCompany(id, name);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving insurance company by id: " + e.getMessage());
        }
        return null;
    }
}
