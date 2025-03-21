package dao;

import db.migration.DataSource;
import entity.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StockCrudOperation implements CrudOperations<Stock> {
    private DataSource dataSource = new DataSource();

    @Override
    public List<Stock> getAll(int page, int size) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Stock findById(String id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Stock> findByName(String name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Stock> saveAll(List<Stock> list) {
        String sql = "INSERT INTO stock(id,movement,quantity,unity,date,id_ingredient) VALUES(?,?::movement,?,?::unity,?,?) ON CONFLICT(id) DO NOTHING";
        List<Stock> successfullyInsertedStocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for(Stock stock : list) {
                statement.setString(1, stock.getId());
                statement.setString(2, stock.getMovement().toString());
                statement.setDouble(3, stock.getQuantity());
                statement.setString(4, stock.getUnity().toString());
                statement.setTimestamp(5, Timestamp.valueOf(stock.getDate()));
                statement.setString(6, stock.getIngredient().getId());
                statement.addBatch();
            }

            int[] updateCounts = statement.executeBatch();
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] > 0) {
                    successfullyInsertedStocks.add(list.get(i));  // Only add if the update count is > 0
                }
            }

            return successfullyInsertedStocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
