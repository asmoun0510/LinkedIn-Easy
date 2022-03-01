import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) throws IOException, InterruptedException {

        Library myLib = new Library();
        System.out.println("*************** Menu *****************");
        System.out.println("1. Browse jobs by Keywords and Location to get jobs IDs");
        System.out.println("2. Apply for jobs - from job_ids.txt and result of application will be stored in 'application.txt'");
        System.out.println("3. Answer questions needed for applying");
        Scanner keyboard = new Scanner(System.in);
        String choice = keyboard.nextLine();
        switch (choice) {
            case "1" -> {
                System.out.println("Please enter you Keywords");
                String keyWord = keyboard.nextLine();
                System.out.println("Please enter your location");
                String location = keyboard.nextLine();
                System.out.println("add Remote ? yes/no");
                boolean remote = keyboard.nextLine().equals("yes");
                WebDriver driverBrowseJobs = myLib.logIn("hello@issamsahraoui.com", "Rahaf@0510");
                Thread.sleep(5000);
                myLib.BrowsJobs(keyWord, location, remote, driverBrowseJobs);
                System.out.println("done browsing jobs");
            }
            case "2" -> {
                WebDriver driverApplyJobs = myLib.logIn("hello@issamsahraoui.com", "Rahaf@0510");
                Scanner sc = new Scanner(new File("job_ids.txt"));
                while (sc.hasNext()) {
                    String id = sc.nextLine();
                    myLib.Apply(id, driverApplyJobs);
                }
                sc.close();

            }
            case "3" -> {
                FileWriter fw = new FileWriter("Q&Updated.txt", true);
                String line, type, question, answer;
                Scanner sc = new Scanner(new File("Q&A.txt"));
                while (sc.hasNext()) {
                    line = sc.nextLine();
                    String[] var = line.split("@");
                    type = var[0];
                    question = var[1];
                    answer = var[2];
                    if (answer.equals("none")) {
                        System.out.println(type + "=>" + question);
                        answer = keyboard.nextLine();
                    }
                    fw.write(type + "@" + question + "@" + answer + "\n");
                }
                fw.close();
                sc.close();

            }
            default -> System.out.println("wrong input please rerun ");
        }


    }
}
