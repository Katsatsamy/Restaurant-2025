package dao;

import db.migration.DataSource;
import entity.Ingredient;
import entity.IngredientPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientCrudOperation implements CrudOperations<Ingredient> {
    private final DataSource dataSource= new DataSource();

    @Override
    public List<Ingredient> getAll(int page, int size) {
        if(page < 1){
            throw new IllegalArgumentException("page must be greater than 0 but is " + page);
        }else{
            List<Ingredient> ingredients = new ArrayList<>();
            String sql = "SELECT "
            try()
        }
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Ingredient findById(String id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> list) {
        throw new RuntimeException("Not implemented");
    }

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT unit_price, date FROM price WHERE id_igredient = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT unit_price, date FROM price WHERE id_igredient ="+ ingredient_id + " ORDER BY date DESC LIMIT 1");){
            try(ResultSet resultSet = statement.executeQuery();){
                IngredientPrice ingredientPrice = new IngredientPrice(
                        resultSet.getInt("unit_price"),
                        resultSet.getTimestamp("date").toLocalDateTime()
                );
                return ingredientPrice;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
