package Persistance;

import Models.Pharmacist;
import Persistance.Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PharmacistRepository {
    private static final String INSERT_PHARMACIST = "INSERT INTO Pharmacist (id, name, dateOfBirth, salary) VALUES (pharmacistseq.nextval, ?, to_date(?, 'yyyy-mm-dd'), ?)";
    private static final String MODIFY_SALARY = "UPDATE Pharmacist SET salary = salary + ? WHERE id = ?";
    private static final String DELETE_PHARMACIST = "DELETE FROM Pharmacist WHERE id = ?";
    private static final String GET_PHARMACIST = "SELECT * FROM Pharmacist WHERE id = ?";
    private static final String GET_ALL_PHARMACISTS = "SELECT * FROM Pharmacist";

    private static PharmacistRepository instance;
    private static Connection connection;

    private PharmacistRepository() {
        connection = DatabaseConnection.getDatabaseConnection();
    }

    public static PharmacistRepository getInstance() {
        if (instance == null) {
            synchronized (PharmacistRepository.class) {
                if (instance == null) {
                    instance = new PharmacistRepository();
                }
            }
        }
        return instance;
    }

    private Pharmacist extractPharmacist(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
        double salary = resultSet.getDouble("salary");
        return new Pharmacist(id, name, dateOfBirth, salary);
    }

    public void addPharmacist(String name, String dateOfBirth, double salary) {
        try (var preparedStatement = connection.prepareStatement(INSERT_PHARMACIST)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dateOfBirth);
            preparedStatement.setDouble(3, salary);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error adding pharmacist: " + e.getMessage());
        }
    }

    public void updatePharmacistSalary(int id, double amount) {
        try (var preparedStatement = connection.prepareStatement(MODIFY_SALARY)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating pharmacist salary: " + e.getMessage());
        }
    }

    public void deletePharmacist(int id) {
        try (var preparedStatement = connection.prepareStatement(DELETE_PHARMACIST)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting pharmacist: " + e.getMessage());
        }
    }

    public Pharmacist getPharmacist(int id) {
        try (var preparedStatement = connection.prepareStatement(GET_PHARMACIST)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractPharmacist(resultSet);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving pharmacist: " + e.getMessage());
        }
        return null;
    }

    public Pharmacist getPharmacistById(int id) {
        try (var preparedStatement = connection.prepareStatement(GET_PHARMACIST)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractPharmacist(resultSet);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving pharmacist by id: " + e.getMessage());
        }
        return null;
    }

    public List<Pharmacist> getAllPharmacists() {
        List<Pharmacist> pharmacists = new ArrayList<>();
        try (var preparedStatement = connection.prepareStatement(GET_ALL_PHARMACISTS)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pharmacists.add(extractPharmacist(resultSet));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all pharmacists: " + e.getMessage());
        }
        return pharmacists;
    }
}
