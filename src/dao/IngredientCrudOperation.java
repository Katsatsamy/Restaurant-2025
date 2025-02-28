package dao;

import db.migration.DataSource;
import entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            String sql = "SELECT id, name, unity FROM ingredient ORDER BY id ASC LIMIT ? OFFSET ?";
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

    @Override
    public List<Ingredient> findByName(String name) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, unity FROM ingredient WHERE name ILIKE '%" + name + "%'";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();){
            try(ResultSet resultSet = statement.executeQuery(sql)){
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }


    @Override
    public List<Ingredient> saveAll(List<Ingredient> list) {
        List<Ingredient> ingredients = new ArrayList<>();
        for(Ingredient ingredient : list){
            String ingredientSql = "INSERT INTO ingredient (id, name, unity) VALUES (?, ?, ?::unity) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, unity = EXCLUDED.unity";
            String priceSql = "INSERT INTO price (id, unit_price, date, id_ingredient) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET unit_price = EXCLUDED.unit_price, date = EXCLUDED.date";
            try(Connection connection = dataSource.getConnection();
                PreparedStatement ingredientStatement = connection.prepareStatement(ingredientSql);
                PreparedStatement priceStatement = connection.prepareStatement(priceSql)){
                ingredientStatement.setString(1, ingredient.getId());
                ingredientStatement.setString(2, ingredient.getName());
                ingredientStatement.setString(3, ingredient.getUnity().toString());
                ingredientStatement.executeUpdate();

                priceStatement.setString(1, getPriceID(ingredient.getId()));
                priceStatement.setDouble(2, ingredient.getUnitPrice());
                priceStatement.setTimestamp(3, Timestamp.valueOf(ingredient.getUpdateDateTime()));
                priceStatement.setString(4, ingredient.getId());
                priceStatement.executeUpdate();
                Ingredient ingredientToDB = findById(ingredient.getId());
                ingredients.add(ingredientToDB);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ingredients;
    }

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ? ORDER BY ABS(EXTRACT(EPOCH FROM date) - EXTRACT(EPOCH FROM ?::timestamp)) LIMIT 1;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getDouble("unit_price"),
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

    public IngredientPrice getIngredientPrice(String ingredient_id, LocalDateTime date){
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ? AND date <= ? ORDER BY ABS(EXTRACT(EPOCH FROM date) - EXTRACT(EPOCH FROM ?::timestamp)) LIMIT 1;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            statement.setTimestamp(3, Timestamp.valueOf(date));
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getDouble("unit_price"),
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

    public List<Stock> getAvailableStocks(String ingredient_id){
        String sql = "SELECT id, movement, quantity, unity, date, id_ingredient FROM stock WHERE id_ingredient = ? AND date <= ?";
        List<Stock> stocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try(ResultSet resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    Ingredient ingredient = findById(resultSet.getString("id"));
                    Stock stock = new Stock(
                            resultSet.getString("id"),
                            Movement.valueOf(resultSet.getString("movement")),
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity")),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            ingredient
                    );
                    stocks.add(stock);
                }
            }
            return stocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stock> getAvailableStocks(String ingredient_id, LocalDateTime date){
        String sql = "SELECT id, movement, quantity, unity, date, id_ingredient FROM stock WHERE id_ingredient = ? AND date <= ?";
        List<Stock> stocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            try(ResultSet resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    Ingredient ingredient = findById(resultSet.getString("id"));
                    Stock stock = new Stock(
                            resultSet.getString("id"),
                            Movement.valueOf(resultSet.getString("movement")),
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity")),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            ingredient
                    );
                    stocks.add(stock);
                }
            }
            return stocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findByCriteria(List<Criteria> criteria){
        String sql = "SELECT id, name, unity, unit_price, date FROM ingredient_price WHERE 1=1";
        List<Criteria> criteriaFilter = new ArrayList<>();
        List<Criteria> criteriaOrder = new ArrayList<>();
        List<Criteria> criteriaFilterOrder = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        for (Criteria c: criteria){
            if(c.getOrder() != null){
                if(c.getValue() == null && c.getConnector() == null){
                    criteriaOrder.add(c);
                }else {
                    criteriaFilterOrder.add(c);
                }
            }else {
                criteriaFilter.add(c);
            }
        }
        for(Criteria c: criteriaFilter){
            String oper = getOp(c);

            sql += " " + c.getConnector() + " " + c.getFieldName() + " " + oper;
        }
        for(Criteria c: criteriaFilterOrder){
            String oper = getOp(c);

            sql += " " + c.getConnector() + " " + c.getFieldName() + " " + oper + " ORDER BY " + c.getFieldName() + " " + c.getOrder();
        }
        for (Criteria c: criteriaOrder){
            sql += " ORDER BY " + c.getFieldName() + " " + c.getOrder();
        }
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){;
                Ingredient ingredient = new Ingredient(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("date").toLocalDateTime(),
                        resultSet.getDouble("unit_price"),
                        Unity.valueOf(resultSet.getString("unity"))
                );
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPriceID(String ingredient_id){
        String sql = "SELECT id FROM price WHERE id_ingredient = ?";
        String id = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    id = resultSet.getString("id");
                }
                return id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOp(Criteria c){
        if(c.getOperator() == Operator.EQUAL){
            if(c.getFieldName() == "name"){
                return "ILIKE '%" + c.getValue() + "%'";
            }else{
                return "= '" + c.getValue() + "'";
            }
        }if(c.getOperator() == Operator.SUP){
            return "> '" + c.getValue() + "'";
        }if(c.getOperator() == Operator.INF){
             return "< '" + c.getValue() + "'";
        }if(c.getOperator() == Operator.SUP_AND_EQUAL){
            return ">= '" + c.getValue() + "'";
        }if(c.getOperator() == Operator.INF_AND_EQUAL){
            return "<= '" + c.getValue() + "'";
        }
        return "";
    }
}
