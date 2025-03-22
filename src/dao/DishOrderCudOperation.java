package dao;

import db.migration.DataSource;
import entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static entity.OrderStatus.*;

public class DishOrderCudOperation implements  CrudOperations<DishOrder> {
    private DataSource dataSource = new DataSource();

    @Override
    public List<DishOrder> getAll(int page, int size) {
        if(page > 0){
            throw new IllegalArgumentException("page must be greater than 0 but is "+page);
        }else{
            List<DishOrder> dishOrders = new ArrayList<>();
            String sql = "SELECT id, id_dish, quantity FROM dish_order LIMIT ? OFFSET ?";
            try(Connection con = dataSource.getConnection();
                PreparedStatement statement = con.prepareStatement(sql)){
                statement.setInt(1, size);
                statement.setInt(2, size * (page - 1));
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        DishOrder dishOrder = new DishOrder(
                                resultSet.getString("id"),
                                findDishById(resultSet.getString("id_dish")),
                                resultSet.getDouble("quantity"),
                                findByDishOrderId(resultSet.getString("id"))
                        );
                        dishOrders.add(dishOrder);
                    }
                    return dishOrders;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public DishOrder findById(String id) {
        DishOrder dishOrder = null;
        String sql = "SELECT id, id_dish, quantity FROM dish_order WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    dishOrder = new DishOrder(
                            resultSet.getString("id"),
                            findDishById(resultSet.getString("id_dish")),
                            resultSet.getDouble("quantity"),
                            findByDishOrderId(resultSet.getString("id"))
                    );
                }
            }
            return dishOrder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DishOrderStatusHistory changeStatus(DishOrder dishOrder){
        DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory();
        OrderStatus orderStatus = dishOrder.getActualStatus().getStatus();

        switch(orderStatus){
            case CREE -> {
                if (dishOrder.getQuantity() > dishOrder.getDish().getAvailableQuantity()){
                    throw new IllegalArgumentException("La quantité disponible doit être supérieure à la quantité commandée");
                } else {
                    orderStatus = OrderStatus.CONFIRME;
                }
            }
            case CONFIRME -> orderStatus = OrderStatus.EN_PREPARATION;
            case EN_PREPARATION -> orderStatus = OrderStatus.TERMINE;
            case TERMINE -> orderStatus = OrderStatus.SERVI;
            case SERVI -> throw new RuntimeException("Il n'est pas possible de changer le statut de la commande de plat car elle est déjà SERVI");
        }

        String sql = "INSERT INTO dish_order_status_history (id_dish, status) VALUES (?,?::order_status)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, dishOrder.getId());
            statement.setString(2, orderStatus.toString());
            statement.executeUpdate();

            dishOrderStatusHistory.setId(dishOrder.getId());
            dishOrderStatusHistory.setStatus(orderStatus);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'historique du statut", e);
        }

        return dishOrderStatusHistory;
    }


    @Override
    public List<DishOrder> findByName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> list) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "INSERT INTO dish_order (id, id_order, id_dish, quantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (DishOrder dishOrder : list) {
                statement.setString(1, dishOrder.getId());
                statement.setString(2, dishOrder.getOrder().getId());
                statement.setString(3, dishOrder.getDish().getId());
                statement.setDouble(4, dishOrder.getQuantity());
                statement.addBatch();
            }

            statement.executeBatch();

            for (DishOrder dishOrder : list) {
                dishOrders.add(findById(dishOrder.getId()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishOrders;
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dishOrderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory(
                            resultSet.getLong("id"),
                            OrderStatus.valueOf(resultSet.getString("status")),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    dishOrderStatusHistoryList.add(dishOrderStatusHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dishOrderStatusHistoryList;
    }

}
