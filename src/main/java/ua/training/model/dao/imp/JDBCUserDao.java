package ua.training.model.dao.imp;

import ua.training.model.dao.UserDao;
import ua.training.constants.Queries;
import ua.training.constants.ColumnNames;
import ua.training.model.entity.User;
import ua.training.model.entity.builder.UserBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUserDao implements UserDao {

    private Connection connection;

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_CREATE)){
            ps.setString(1 , user.getLogin().toLowerCase());
            ps.setString(2 , user.getPassword());
            ps.setString(3 , firstUpperCase(user.getRole().toString()));
            ps.setString(4 , user.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String login, String password, User.Role role, String name) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_CREATE)){
            ps.setString(1 , login.toLowerCase());
            ps.setString(2 , password);
            ps.setString(3 , firstUpperCase(role.toString()));
            ps.setString(4 , name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(long id) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_FIND_BY_ID)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User extractFromResultSet(ResultSet rs) throws SQLException {
        long id         = rs.getLong(ColumnNames.USER_ID);
        String login    = rs.getString(ColumnNames.USER_LOGIN);
        String password = rs.getString(ColumnNames.USER_PASSWORD);
        String role     = rs.getString(ColumnNames.USER_ROLE).toUpperCase();
        String name     = rs.getString(ColumnNames.USER_NAME);
        return new UserBuilder().setId(id).setLogin(login).setPassword(password)
                .setRole(User.Role.valueOf(role)).setName(name).buildUser();
    }

    @Override
    public List<User> findAll() {
        List<User> resultList = new ArrayList<>();
        try (Statement ps = connection.createStatement()){
            ResultSet rs = ps.executeQuery(Queries.USER_FIND_ALL);
            while ( rs.next() ){
                resultList.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public void update(User user) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_UPDATE)){
            ps.setString(1 , user.getLogin().toLowerCase());
            ps.setString(2 , user.getPassword());
            ps.setString(3 , firstUpperCase(user.getRole().toString()));
            ps.setString(4 , user.getName());
            ps.setLong(6 , user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_DELETE)){
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User login(String login, String password) {
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_LOGIN)){
            ps.setString(1, login.toLowerCase());
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                user = extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public boolean userExists(String login) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_FIND_BY_LOGIN)){
            ps.setString(1, login.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public int getUserBall(long id) {
        try (PreparedStatement ps = connection.prepareStatement(Queries.USER_GET_BALL)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                return rs.getInt(ColumnNames.USER_TEST_BALL);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private String firstUpperCase(String word){
        if(word == null || word.isEmpty()){
            return "";
        }
        return word.substring(0, 1).toUpperCase().concat(word.substring(1).toLowerCase());
    }
}
