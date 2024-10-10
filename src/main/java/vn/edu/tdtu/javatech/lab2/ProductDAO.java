package vn.edu.tdtu.javatech.lab2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO<P extends Product, L extends Long> implements Repository<Product, Long>{
    private Connection connection;

    private final String SQL_SELECT = "select * from product";
    private final String SQL_SELECT_BY_ID = "select * from product where id=?";
    private final String SQL_INSERT = "insert into product (id, name, price) values (?,?,?)";
    private final String SQL_UPDATE = "update product set name=? , price=? where id=?";
    private final String SQL_DELETE = "delete from product where id = ?";
    private final String SQL_DROP_TABLE = "drop table if exists product";
    private final String SQL_CREATE_TABLE = """
            CREATE TABLE product (
                id bigint primary key,
                name varchar(50) NOT NULL,
                price double precision NOT NULL
            );
            """;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public ProductDAO() {}

    @Override
    public Long add(Product product) {
        List<Product> result = new ArrayList<Product>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setLong(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.executeQuery();
            return product.getId();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> readAll() {
        List<Product> result = new ArrayList<Product>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Double price = resultSet.getDouble("price");
                Product obj = new Product(id, name, price);
                result.add(obj);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public Product read(Long id) {
        List<Product> result = new ArrayList<Product>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            Double price = resultSet.getDouble("price");
            Product product = new Product(id, name, price);
            return product;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Product product) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean createProductTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP_TABLE);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(SQL_CREATE_TABLE);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
