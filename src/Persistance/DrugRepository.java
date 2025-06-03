package Persistance;

import Models.Drug;
import Persistance.Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DrugRepository {
    private static final String INSERT_DRUG = "INSERT INTO Drug (id, name, price, requiredAge, requiresPrescription, condition) VALUES (drugseq.nextval, ?, ?, ?, ?, ?)";
    private static final String UPDATE_DRUG_PRICE = "UPDATE Drug SET price = ? WHERE id = ?";
    private static final String DELETE_DRUG = "DELETE FROM Drug WHERE id = ?";
    private static final String GET_DRUG = "SELECT * FROM Drug WHERE id = ?";
    private static final String GET_ALL_DRUGS = "SELECT * FROM Drug";

    private static DrugRepository instance;
    private static Connection connection;

    private DrugRepository() {
        connection = DatabaseConnection.getDatabaseConnection();
    }

    public static DrugRepository getInstance() {
        if (instance == null) {
            synchronized (DrugRepository.class) {
                if (instance == null) {
                    instance = new DrugRepository();
                }
            }
        }
        return instance;
    }

    public void addDrug(String name, double price, int requiredAge, boolean requiresPrescription, String condition) {
        try (var preparedStatement = connection.prepareStatement(INSERT_DRUG)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, requiredAge);
            preparedStatement.setBoolean(4, requiresPrescription);
            preparedStatement.setString(5, condition);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error adding drug: " + e.getMessage());
        }
    }

    public void updateDrugPrice(int drugId, double newPrice) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_DRUG_PRICE)) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, drugId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error updating drug price: " + e.getMessage());
        }
    }

    public void deleteDrug(int drugId) {
        try (var preparedStatement = connection.prepareStatement(DELETE_DRUG)) {
            preparedStatement.setInt(1, drugId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleting drug: " + e.getMessage());
        }
    }

    public Drug getDrug(int drugId) {
        try (var preparedStatement = connection.prepareStatement(GET_DRUG)) {
            preparedStatement.setInt(1, drugId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractDrug(resultSet);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving drug: " + e.getMessage());
        }
        return null;
    }

    public Drug getDrugById(int drugId) {
        try (var preparedStatement = connection.prepareStatement(GET_DRUG)) {
            preparedStatement.setInt(1, drugId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractDrug(resultSet);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving drug by id: " + e.getMessage());
        }
        return null;
    }

    public List<Drug> getAllDrugs() {
        List<Drug> drugs = new ArrayList<>();
        try (var preparedStatement = connection.prepareStatement(GET_ALL_DRUGS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                drugs.add(extractDrug(resultSet));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all drugs: " + e.getMessage());
        }
        return drugs;
    }

    private Drug extractDrug(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        int requiredAge = resultSet.getInt("requiredAge");
        boolean requiresPrescription = resultSet.getBoolean("requiresPrescription");
        String condition = resultSet.getString("condition");
        return new Drug(id, name, price, requiredAge, requiresPrescription, condition);
    }
}
