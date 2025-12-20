package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket {
    public enum Status{
        open,
        resolved,
        cancelled;
    }
    public enum JobRequest{
      network_issue(2),
      software_app_installation(3),
      new_computer_configuration(4),
      security_issues(1);

      private final int priority;

      JobRequest(int priority){

        this.priority = priority;
      }

      public int getPriority() {

        return priority;
      }

      public static JobRequest fromPriority(int p) {
        for (JobRequest req : values()) {
            if (req.priority == p) return req;
        }
        return null;
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

//    constructor
    public Ticket( String title, String description, User createdBy, JobRequest requestType) {

        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.status = Status.open;
        this.createdDate = LocalDateTime.now();
        this.requestType = requestType;
        this.resolution_note = " ";
    }

//  getters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
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
    public JobRequest getRequestType() {
        return requestType;
    }

    public int getPriority(){
        return requestType.getPriority();
    }

//  setter
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setResolution_note(String note){
        this.resolution_note = note;}

    public void setRequestType(JobRequest newRequestType) {
        this.requestType = newRequestType;
    }
    public void setCreatedDate(LocalDateTime createdDateTime){
        this.createdDate = createdDateTime;
    }

    public void setPriority(int newPriority) {
        JobRequest matching = JobRequest.fromPriority(newPriority);
        if (matching != null) {
            this.requestType = matching;
        }
    }

}