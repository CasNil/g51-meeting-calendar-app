package se.lexicon.dao.implementation;

import se.lexicon.dao.CalendarDAO;
import se.lexicon.dao.db.CalendarDBConnection;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.Calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CalendarDAOImpl implements CalendarDAO {

    private Connection connection;

    public CalendarDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Calendar createCalendar(String title, String username) {
        String query = "INSERT INTO calendars(title,username) VALUES (?,?)";
        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            Calendar calendar = new Calendar(title, username);
            preparedStatement.setString(1, calendar.getTitle());
            preparedStatement.setString(2, calendar.getUsername());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new MySQLException("Creating calendar failed, no rows affected");
            }
            return calendar;
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while creating calendar:" + title, e);
        }
    }

    @Override
    public Optional<Calendar> findById(int id) {
        String query = "SELECT * FROM calendars WHERE id = ?";
        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int foundId = resultSet.getInt("id");
                String foundTitle = resultSet.getString("title");
                String foundUsername = resultSet.getString("username");
                Calendar calendar = new Calendar(foundId, foundTitle, foundUsername);
                return Optional.of(calendar);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding calendar by id: " + id, e);
        }
    }

    @Override
    public Collection<Calendar> findCalendarByUsername(String username) {

        Collection<Calendar> calendars = new ArrayList<>();

        String query = "SELECT * FROM calendars WHERE username = ?";

        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int foundId = resultSet.getInt("id");
                String foundTitle = resultSet.getString("title");
                String foundUsername = resultSet.getString("username");
                Calendar calendar = new Calendar(foundId, foundTitle, foundUsername);
                calendars.add(calendar);
            }

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding calendar by username: " + username, e);
        }

        return calendars;
    }

    @Override
    public Optional<Calendar> findByTitleAndUsername(String title, String username) {
        String query = "SELECT * FROM calendars WHERE title = ? AND username = ?";
        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int foundId = resultSet.getInt("id");
                String foundTitle = resultSet.getString("title");
                String foundUsername = resultSet.getString("username");
                Calendar calendar = new Calendar(foundId, foundTitle, foundUsername);
                return Optional.of(calendar);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding calendar by title and username: " + title + username, e);
        }
    }

    @Override
    public boolean deleteCalendar(int id) {
        String query = "DELETE FROM calendars WHERE id = ?";

        boolean isDeleted = false;

        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            isDeleted = (affectedRows > 0);

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while deleting calendar" + id, e);
        }
        return isDeleted;
    }
}
