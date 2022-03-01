import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Library {
    public Library() {

    }

    public WebDriver BrowsJobs(String keywords, String location, WebDriver driver) throws InterruptedException, IOException {
        driver.get("https://www.linkedin.com/jobs/");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search by title, skill, or company']"))).sendKeys(keywords);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='City, state, or zip code']"))).sendKeys(location);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Search')]"))).click();
        Thread.sleep(2000);
        // Filter easy apply
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Easy Apply filter.']"))).click();
        Thread.sleep(2000);

        // Filter remote
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'On-site/Remote')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(.,'Remote')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Show')]"))).click();

        //scroll list of jobs one page
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementsByClassName('jobs-search-results display-flex flex-column')[0].scrollTo({top: 99999, behavior: 'smooth'});");
        Thread.sleep(5000);
        //get number of pages
        List<WebElement> pagination = driver.findElements(By.xpath("//button[contains(@aria-label, 'Page ')]"));
        int lastPage = Integer.parseInt(pagination.get(pagination.size() - 1).getAttribute("aria-label").replaceAll("Page ", ""));

        for (int p = 1; p <= lastPage; p++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Page " + p + "')]"))).click();
            Thread.sleep(5000);
            //scroll list of jobs one page
            js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementsByClassName('jobs-search-results display-flex flex-column')[0].scrollTo({top: 99999, behavior: 'smooth'});");
            Thread.sleep(5000);

            List<WebElement> list = driver.findElements(By.xpath("//div[@data-job-id]"));
            for (WebElement el : list) {
                FileWriter fw = new FileWriter("jobIds.txt", true); //the true will append the new data
                fw.write(el.getAttribute("data-job-id") + "\n");//appends the string to the file
                fw.close();
            }

        }

        return driver;
    }

    public WebDriver Apply(String jobId, WebDriver driver) throws InterruptedException, IOException {
        driver.get("https://www.linkedin.com/jobs/view/" + jobId + "/");
        WebDriverWait wait = new WebDriverWait(driver, 60);

        String tittle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='t-24 t-bold']"))).getText();
        String company = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='ember-view t-black t-normal']"))).getText();
        String location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(("(//span[@class='jobs-unified-top-card__bullet'])[1]")))).getText().trim();
        String dateApplied = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //  public Job(String id, String title, String company, String location, String status, String dateApplied)
        Job myJob = new Job(jobId, tittle, company, location, "", "", dateApplied);

        // check if accept application
        if (driver.findElements(By.xpath("//span[contains(.,'No longer accepting applications')]")).size() == 1) {
            FileWriter fw = new FileWriter("Applications.txt", true);
            fw.write(myJob.getId() + "@" + myJob.getTitle() + "@" + myJob.getCompany() + "@" + myJob.getLocation() + "@" + "NoLongerAccept" + "@" + myJob.getDateApplied() + "\n");
            fw.close();
        } else {
            //check if already applied
            if (driver.findElements(By.xpath("//span[contains(.,'Application submitted')]")).size() > 0) {
                FileWriter fw = new FileWriter("Applications.txt", true);
                fw.write(myJob.getId() + "@" + myJob.getTitle() + "@" + myJob.getCompany() + "@" + myJob.getLocation() + "@" + "AlreadyApplied" + "@" + myJob.getDateApplied() + "\n");
                fw.close();
            } else {
                //click apply button
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Easy Apply')]"))).click();
                // pop up


                //list of question sections jobs-easy-apply-form-section__grouping
                // dropdowns fb-dropdown

                // questions list //label[@class='fb-form-element-label']//span[contains(.,'fb-form-element-label')]

                // next button
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Next')]"))).click();
                // review button
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Review')]"))).click();
                // submit
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Submit application')]"))).click();
                // success
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(.,'Great!')]")));

                // write in file
                FileWriter fw = new FileWriter("Applications.txt", true); //the true will append the new data
                fw.write(myJob.getId() + "@" + myJob.getTitle() + "@" + myJob.getCompany() + "@" + myJob.getLocation() + "@" + "Success" + "@" + myJob.getDateApplied() + "\n");
                fw.close();

            }


        }
        return driver;
    }

    public WebDriver logIn(String username, String password) throws InterruptedException {
        WebDriverManager.chromedriver().browserVersion("98.0.4758.102").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--incognito");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
        //options.addArguments("--blink-settings=imagesEnabled=false");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.linkedin.com/checkpoint/rm/sign-in-another-account?fromSignIn=true");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Sign in' or @aria-label='S’identifier']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(.,'Issam Sahraoui')]")));
        return driver;
    }


    public void initFiles() {
        File questionsFile = new File("questions.txt");
        File applicationFile = new File("application.txt");
    }

    public void updateQuestion() {
        File questionsFile = new File("questions.txt");

    }

    public void updateApplication() throws FileNotFoundException {
        File questionsFile = new File("application.txt");
        Scanner questionReader = new Scanner(questionsFile);

    }

    public void addApplication(Job myJob) throws IOException {
        FileWriter fw = new FileWriter("application.txt", true); //the true will append the new data
        fw.write(myJob.getTitle() + "@" + myJob.getCompany() + "@" + myJob.getLocation() + "@" + "\n");//appends the string to the file
        fw.close();
    }

    public void addQuestion(Question myQuestion) throws IOException {
        FileWriter fw = new FileWriter("application3.txt", true); //the true will append the new data
        fw.write(myQuestion.getType() + "@" + myQuestion.getAnswer() + "@" + myQuestion.getText() + "\n");//appends the string to the file
        fw.close();
    }
    /*

      String filename= "MyFile.txt";
    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
    fw.write("add a line\n");//appends the string to the file
    fw.close();
     */


    // job format

}
