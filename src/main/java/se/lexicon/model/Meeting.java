package se.lexicon.model;

import java.time.LocalDateTime;

public class Meeting {
    private int id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Calendar calendar;

    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, String description, Calendar calendar) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.calendar = calendar;
    }

    public Meeting(int id, String title, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Meeting(int id, String title, LocalDateTime startTime, LocalDateTime endTime, String description, Calendar calendar) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.calendar = calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    private void timeValidation() {

        LocalDateTime now = LocalDateTime.now();
        if (this.startTime.isBefore(now)) {
            throw new IllegalArgumentException("Start time must be in the future!");
        }
        if (this.endTime.isBefore(this.startTime)) {
            throw new IllegalArgumentException("End time must be after the start time!");
        }
    }

    public String meetingInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Calendar info: ").append("\n");
        stringBuilder.append("Id: ").append("\n");
        stringBuilder.append("Title: ").append("\n");
        stringBuilder.append("Start Time: ").append("\n");
        stringBuilder.append("End Time: ").append("\n");
        stringBuilder.append("Description: ").append("\n");
        stringBuilder.append("Calendar title: ").append("\n");

        return stringBuilder.toString();
    }
}
