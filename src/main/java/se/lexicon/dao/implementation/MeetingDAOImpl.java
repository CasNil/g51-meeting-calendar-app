package se.lexicon.dao.implementation;

import se.lexicon.dao.MeetingDAO;
import se.lexicon.dao.db.CalendarDBConnection;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.Meeting;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MeetingDAOImpl implements MeetingDAO {
    private Connection connection;

    public MeetingDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        String query = "INSERT INTO meetings(title, start_time, end_time, _description) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setString(1, meeting.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(meeting.getStartTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(meeting.getEndTime()));
            preparedStatement.setString(4, meeting.getDescription());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                    }
                }
            }
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while creating meeting: " + meeting, e);
        }
        return null;
    }

    @Override
    public Optional<Meeting> findById(int meetingId) {
        String query = "SELECT * FROM meetings WHERE id = ?";
        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, meetingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int foundMeetingId = resultSet.getInt("id");
                String foundTitle = resultSet.getString("title");
                Timestamp foundStartTime = resultSet.getTimestamp("start_time");
                Timestamp foundEndTime = resultSet.getTimestamp("end_time");
                String foundDescription = resultSet.getString("_description");
                Meeting meeting = new Meeting(foundMeetingId, foundTitle, foundStartTime.toLocalDateTime(), foundEndTime.toLocalDateTime(), foundDescription);
                return Optional.of(meeting);
            }
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding meeting by id: " + meetingId, e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Meeting> findAllMeetingsByCalendarId(int calendarId) {
        Collection<Meeting> meetings = new ArrayList<>();

        String query = "SELECT * FROM meetings WHERE calendar_id = ?";

        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, calendarId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int foundCalendarId = resultSet.getInt("calendar_id");
                String foundTitle = resultSet.getString("title");
                Timestamp foundStartTime = resultSet.getTimestamp("start_time");
                Timestamp foundEndTime = resultSet.getTimestamp("end_time");
                String foundDescription = resultSet.getString("_description");
                Meeting meeting = new Meeting(foundCalendarId, foundTitle, foundStartTime.toLocalDateTime(), foundEndTime.toLocalDateTime(), foundDescription);
                meetings.add(meeting);
            }

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding meeting by calendarID: " + calendarId, e);
        }

        return meetings;
    }

    @Override
    public boolean deleteMeeting(int meetingId) {
        String query = "DELETE FROM meetings WHERE id = ?";

        boolean isDeleted = false;

        try (
                Connection connection = CalendarDBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, meetingId);
            int affectedRows = preparedStatement.executeUpdate();
            isDeleted = (affectedRows > 0);

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while deleting meeting by id: " + meetingId, e);
        }
        return isDeleted;
    }
}
