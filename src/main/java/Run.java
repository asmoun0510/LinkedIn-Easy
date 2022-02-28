import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException, InterruptedException {
        Library myLib = new Library();
        // myLib.initFiles();
        WebDriver driverBrowseJobs = myLib.logIn("hello@issamsahraoui.com", "Rahaf@0510");
        driverBrowseJobs = myLib.BrowsJobs("SDET", "European Union", driverBrowseJobs);

        FileWriter myWriter = new FileWriter("jobs-list.txt", true);
        myWriter.write("test");
        myWriter.close();


    }
}
