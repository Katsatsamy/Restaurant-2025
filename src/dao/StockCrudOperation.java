package dao;

import entity.Stock;

import java.util.List;

public class StockCrudOperation implements CrudOperations<Stock> {

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
        String sql = "INSERT INTO stock(id,movement,quantity,unity,date,id_ingredient) VALUES(?,?,?,?,?,?) ON CONFLICT(id)";
        throw new RuntimeException("Not implemented");
    }
}
