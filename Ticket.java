import java.time.LocalDate;

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

    private JobRequest(int priority){
        this.priority = priority;
    }

        public int getPriority() {
            return priority;
        }
    }


    public int id;
    public String title;
    public String description;
    public User createdBy;
    public int idCounter = 1;
    public Status status;
    public String resolution_note;
    public LocalDate createdDate;
 //changed activityType to requestType
    public JobRequest requestType;

//    constructor
    public Ticket( String title, String description, User createdBy, JobRequest requestType) {

        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.status = Status.open;
        this.createdDate = LocalDate.now();
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
    public LocalDate getCreatedDate() {
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
}