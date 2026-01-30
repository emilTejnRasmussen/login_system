package persistence.repo;

import domain.User;
import domain.UserAuthRow;
import persistence.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepo
{
    private final Database db;

    public UserRepo(Database db)
    {
        this.db = db;
    }

    public void init()
    {
        String sql = """
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        username TEXT NOT NULL UNIQUE,
                        email TEXT NOT NULL UNIQUE,
                        password TEXT NOT NULL
                    );
                """;

        try (Connection c = db.getConnection(); Statement s = c.createStatement())
        {
            s.execute(sql);
        } catch (SQLException e)
        {
            throw new RuntimeException("DB init failed", e);
        }
    }

    public List<User> findAll()
    {
        String sql = "SELECT id, username, email FROM users ORDER BY id;";
        List<User> out = new ArrayList<>();

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                out.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email")));
            }
            return out;
        } catch (SQLException e)
        {
            throw new RuntimeException("findAll failed", e);
        }
    }

    public User insert(String username, String email, String password) {
        String sql = "INSERT INTO users(username, email, password) VALUES (?,?,?) RETURNING id;";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            try(ResultSet rs = ps.executeQuery())
            {
                if (!rs.next()) throw new SQLException("No id returned");
                int id = rs.getInt(1);
                return new User(id, username, email);
            }
        } catch (SQLException e)
        {
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Username or email already exists");
            }
            throw new RuntimeException("insert failed", e);
        }
    }

    public void update(int id, String username, String email, String password) {
        String sql = "UPDATE users SET username=?, email=?, password=? WHERE id=?";

        try(Connection c = db.getConnection();
        PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update failed", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";

        try(Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        )
        {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("delete failed", e);
        }
    }

    public Optional<UserAuthRow> findAuthByUsername(String username) {
        String sql = "SELECT id, username, email, password FROM users WHERE username = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                return Optional.of(new UserAuthRow(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("findAuthByUsername failed", e);
        }
    }
}
