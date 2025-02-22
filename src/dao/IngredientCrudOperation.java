package dao;

import db.migration.DataSource;
import entity.Ingredient;
import entity.IngredientPrice;
import entity.Unity;

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
            String sql = "SELECT id, name, unity FROM ingredient LIMIT ? OFFSET ?";
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setInt(1, size);
                statement.setInt(2, size * (page - 1));
                try(ResultSet resultSet = statement.executeQuery();) {
                    while(resultSet.next()){
                        IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                        Ingredient ingredient = new Ingredient(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                ingredientPrice.getDate(),
                                ingredientPrice.getUnit_price(),
                                Unity.valueOf(resultSet.getString("unity"))
                        );
                        ingredients.add(ingredient);
                    }
                    return ingredients;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
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
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, ingredient_id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getInt("unit_price"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
