import java.util.Vector;

public class Job {
    // status : applied // waiting // blocked
    String id, title, company, location, status, datePosted, dateApplied;
    Vector<Question> listOfQuestions;

    public Job() {

    }

    public Job(String id, String title, String company, String location, String status, String datePosted, String dateApplied, Vector<Question> listOfQuestions) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.status = status;
        this.datePosted = datePosted;
        this.dateApplied = dateApplied;
        this.listOfQuestions = listOfQuestions;
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

    public Vector<Question> getListOfQuestions() {
        return listOfQuestions;
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

    public void setListOfQuestions(Vector<Question> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
}
