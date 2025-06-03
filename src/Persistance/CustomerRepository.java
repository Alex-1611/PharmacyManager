package Persistance;

import Models.Customer;
import Models.InsuranceCompany;
import Persistance.Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private static final String INSERT_CUSTOMER = "INSERT INTO Customer (id, name, dateOfBirth) VALUES (customerseq.nextval, ?, to_date(?, 'yyyy-mm-dd'))";
    private static final String SET_INSURANCE = "UPDATE Customer SET insurance_id = ? WHERE id = ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM Customer WHERE id = ?";
    // Use explicit aliases for joined columns
    private static final String GET_ALL_CUSTOMERS =
        "SELECT Customer.id AS customer_id, Customer.name AS customer_name, Customer.dateOfBirth AS customer_dob, " +
        "Customer.insurance_id AS customer_insurance_id, InsuranceCompany.id AS insurance_id, InsuranceCompany.name AS insurance_name " +
        "FROM Customer LEFT JOIN InsuranceCompany ON Customer.insurance_id = InsuranceCompany.id";
    private static final String GET_CUSTOMER_BY_ID =
        "SELECT Customer.id AS customer_id, Customer.name AS customer_name, Customer.dateOfBirth AS customer_dob, " +
        "Customer.insurance_id AS customer_insurance_id, InsuranceCompany.id AS insurance_id, InsuranceCompany.name AS insurance_name " +
        "FROM Customer LEFT JOIN InsuranceCompany ON Customer.insurance_id = InsuranceCompany.id WHERE Customer.id = ?";

    private static CustomerRepository instance;
    private static Connection connection;

    private CustomerRepository() {
        connection = DatabaseConnection.getDatabaseConnection();
    }

    public static CustomerRepository getInstance() {
        if (instance == null) {
            synchronized (CustomerRepository.class) {
                if (instance == null) {
                    instance = new CustomerRepository();
                }
            }
        }
        return instance;
    }

    public void addCustomer(String name, String dateOfBirth) {
        try (var preparedStatement = connection.prepareStatement(INSERT_CUSTOMER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dateOfBirth);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    public void setCustomerInsurance(int customerId, int insuranceId) {
        try (var preparedStatement = connection.prepareStatement(SET_INSURANCE)) {
            preparedStatement.setInt(1, insuranceId);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error setting customer insurance: " + e.getMessage());
        }
    }

    public void deleteCustomer(int customerId) {
        try (var preparedStatement = connection.prepareStatement(DELETE_CUSTOMER)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (var preparedStatement = connection.prepareStatement(GET_ALL_CUSTOMERS)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("customer_id");
                String name = resultSet.getString("customer_name");
                LocalDate dob = resultSet.getDate("customer_dob").toLocalDate();
                InsuranceCompany insurance = null;
                int insuranceId = resultSet.getInt("insurance_id");
                String insuranceName = resultSet.getString("insurance_name");
                if (insuranceId != 0 && insuranceName != null) {
                    insurance = new InsuranceCompany(insuranceId, insuranceName);
                }
                Customer customer = new Customer(id, name, dob);
                customer.setInsurance(insurance);
                customers.add(customer);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all customers: " + e.getMessage());
        }
        return customers;
    }

    public Customer getCustomerById(int id) {
        try (var preparedStatement = connection.prepareStatement(GET_CUSTOMER_BY_ID)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("customer_name");
                LocalDate dob = resultSet.getDate("customer_dob").toLocalDate();
                InsuranceCompany insurance = null;
                int insuranceId = resultSet.getInt("insurance_id");
                String insuranceName = resultSet.getString("insurance_name");
                if (insuranceId != 0 && insuranceName != null) {
                    insurance = new InsuranceCompany(insuranceId, insuranceName);
                }
                Customer customer = new Customer(id, name, dob);
                customer.setInsurance(insurance);
                return customer;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving customer by id: " + e.getMessage());
        }
        return null;
    }
}

