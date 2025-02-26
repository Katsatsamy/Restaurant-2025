package dao;

import db.migration.DataSource;
import entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DishCrudOperation implements CrudOperations<Dish> {
    private final DataSource dataSource = new DataSource();
    @Override
    public List<Dish> getAll(int page, int size) {
        if(page < 1){
            throw new IllegalArgumentException("page must be greater than 0 but is " + page);
        }else {
            List<Dish> dishes = new ArrayList<>();
            String sql = "SELECT id, name, unit_price FROM dish LIMIT ? OFFSET ?";
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, size);
                statement.setInt(2, size * (page - 1));
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){

                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Not implemented");
        }
    }

    @Override
    public Dish findById(String id) {
        String sql = "SELECT id, name, unit_price FROM dish WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Dish dish = new Dish(
                            resultSet.getInt("unit_price"),
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            getIngredientWithQuantity(id)
                    );
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Dish findById(String id, LocalDateTime dateTime) {
        String sql = "SELECT id, name, unit_price FROM dish WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Dish dish = new Dish(
                            resultSet.getInt("unit_price"),
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            getIngredientWithQuantity(id, dateTime)
                    );
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Dish> findByName(String name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Dish> saveAll(List<Dish> list) {
        throw new RuntimeException("Not implemented");
    }

    public double getIngredientsPriceTotal(String dishId) {
        Dish dish = findById(dishId);
        return dish.getIngredientsPriceTotal();
    }

    public double getIngredientsPriceTotal(String dishId, LocalDateTime dateTime) {
        Dish dish = findById(dishId, dateTime);
        return dish.getIngredientsPriceTotal();
    }

    public double getGrossMarginDao(String dishId) {
        Dish dish = findById(dishId);
        return dish.getGrossMargin();
    }

    public double getGrossMarginDao(String dishId, LocalDateTime dateTime) {
        Dish dish = findById(dishId, dateTime);
        return dish.getGrossMargin();
    }

    public List<IngredientWithQuantity> getIngredientWithQuantity(String id_dish) {
        List<IngredientWithQuantity> ingredientsWithQuantityList = new ArrayList<>();
        String sql = "SELECT id_ingredient, required_quantity, unity FROM dish_ingredient WHERE id_dish = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("id_ingredient"));
                    IngredientWithQuantity ingredientWithQuantity = new IngredientWithQuantity(
                            ingredient,
                            resultSet.getDouble("required_quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    ingredientsWithQuantityList.add(ingredientWithQuantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientsWithQuantityList;
    }
    public List<IngredientWithQuantity> getIngredientWithQuantity(String id_dish, LocalDateTime dateTime) {
        List<IngredientWithQuantity> ingredientsWithQuantityList = new ArrayList<>();
        String sql = "SELECT id_ingredient, required_quantity, unity FROM dish_ingredient WHERE id_dish = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("id_ingredient"), dateTime);
                    IngredientWithQuantity ingredientWithQuantity = new IngredientWithQuantity(
                            ingredient,
                            resultSet.getDouble("required_quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    ingredientsWithQuantityList.add(ingredientWithQuantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientsWithQuantityList;
    }

    public Ingredient findIngredientById(String id) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnit_price(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Ingredient findIngredientById(String id, LocalDateTime dateTime) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"), dateTime);
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnit_price(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ? AND date <= CURRENT_DATE ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
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
    public IngredientPrice getIngredientPrice(String ingredient_id, LocalDateTime dateTime){
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ? AND date <= ? ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(dateTime));
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
