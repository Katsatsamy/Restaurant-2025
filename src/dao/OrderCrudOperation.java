package dao;

import db.migration.DataSource;
import entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderCrudOperation implements CrudOperations<Order> {
    private DataSource dataSource = new DataSource();

    @Override
    public List<Order> getAll(int page, int size) {
        if(page < 1){
            throw new IllegalArgumentException("page must be greater than 0 but is " + page);
        }else {
            List<Order> orders = new ArrayList<>();
            String sql = "SELECT id, status, date FROM orders LIMIT ? OFFSET ?";
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, size);
                statement.setInt(2, size * (page - 1));
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        Order order = new Order(
                                resultSet.getString("id"),
                                findDishOrderByOrderId("id")
                        );
                        orders.add(order);
                    }
                }
                return orders;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Order findById(String id) {
        Order order = null;
        String sql = "SELECT id, status, date FROM orders WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = new Order(
                            resultSet.getString("id"),
                            findDishOrderByOrderId(resultSet.getString("id"))
                    );
                }
            }
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Order addOrder(Order order) {
        Order createdOrder = null;
        String orderSql = "INSERT INTO orders (id, status, date) VALUES (?, DEFAULT, DEFAULT) RETURNING id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(orderSql)) {

            statement.setString(1, order.getId());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                createdOrder = findById(rs.getString("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la commande", e);
        }

        return createdOrder;
    }


    @Override
    public List<Order> findByName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Order> saveAll(List<Order> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DishOrder> findDishOrderByOrderId(String orderId) {
        List<DishOrder> dishOrderList = new ArrayList<>();
        String sql = "SELECT id, id_order, id_dish, quantity FROM dish_order WHERE id_order = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, orderId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    DishOrder dishOrder = new DishOrder(
                            resultSet.getString("id"),
                            findDishById(resultSet.getString("id_dish")),
                            resultSet.getDouble("quantity"),
                            findByDishOrderId(resultSet.getString("id"))
                    );
                    dishOrderList.add(dishOrder);
                }
            }
            return dishOrderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findDishById(String id) {
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

    public List<IngredientWithQuantity> getIngredientWithQuantity(String id_dish) {
        List<entity.IngredientWithQuantity> ingredientsWithQuantityList = new ArrayList<>();
        String sql = "SELECT id_ingredient, required_quantity, unity FROM dish_ingredient WHERE id_dish = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("id_ingredient"));
                    entity.IngredientWithQuantity ingredientWithQuantity = new entity.IngredientWithQuantity(
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

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT unit_price, date FROM price WHERE id_ingredient = ? AND date <= CURRENT_DATE ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
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

    public List<DishOrderStatusHistory> findByDishOrderId(String dishOrderId) {
        List<DishOrderStatusHistory> dishOrderStatusHistoryList = new ArrayList<>();
        String sql = "SELECT id, status, date FROM dish_order_status_history WHERE id_dish_order = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, dishOrderId);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {

                    DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory(
                            resultSet.getLong("id"),
                            OrderStatus.valueOf(resultSet.getString("status")),
                            resultSet.getTimestamp("date").toLocalDateTime());


                    dishOrderStatusHistoryList.add(dishOrderStatusHistory);
                }
            }
            return dishOrderStatusHistoryList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
