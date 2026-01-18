package model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    public enum Status{
        open,
        resolved,
        cancelled
    }
    public enum JobRequest{
      network_issue(2),
      software_app_installation(3),
      new_computer_configuration(4),
      security_issues(1);

      private final int defaultPriority;

      JobRequest(int defaultPriority){
        this.defaultPriority = defaultPriority;
      }

      public int getDefaultPriority() {
        return defaultPriority;
      }
    }

    public int id;
    public String title;
    public String description;
    public User createdBy;
    public static int idCounter = 1;
    public Status status;
    public String resolution_note;
    public LocalDateTime createdDate;
 //changed activityType to requestType
    public JobRequest requestType;
    public int priority;
    private List<Path> attachments = new ArrayList<>();

//    constructor
    public Ticket( String title, String description, User createdBy, JobRequest requestType) {

        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.status = Status.open;
        this.createdDate = LocalDateTime.now();
        this.requestType = requestType;
        this.priority = requestType.getDefaultPriority();
        this.resolution_note = " ";
    }

//  getter methods
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public User getCreatedBy() {
        return createdBy;
    }
    public Status getStatus() {
        return status;
    }
    public String getResolution_note() {
        return resolution_note;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public JobRequest getRequestType() { return requestType;}
    public int getPriority(){
        return priority;
    }
    public String getdescription(){return description;}
    public List<Path> getAttachments() { return attachments; }
    // Called when user uploads a file
    public void addAttachment(Path attachment) { attachments.add(attachment);}

//  setter methods
    public void setStatus(Status status) { this.status = status; }
    public void setResolution_note(String note){ this.resolution_note = note;}
    public void setCreatedDate(LocalDateTime createdDateTime){
        this.createdDate = createdDateTime;
    }
    public void setPriority(int priority) { this.priority = priority; }
}
