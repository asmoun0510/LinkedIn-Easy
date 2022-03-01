import java.util.Vector;

public class Job {
    // status : applied //  expired  //  blocked -need more answers  // waiting
    String id, title, company, location, status, datePosted, dateApplied;

    public Job() {

    }

    public Job(String id, String title, String company, String location, String status, String datePosted, String dateApplied) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.status = status;
        this.datePosted = datePosted;
        this.dateApplied = dateApplied;

    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getDateApplied() {
        return dateApplied;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setDateApplied(String dateApplied) {
        this.dateApplied = dateApplied;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
