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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Library {
    public Library() {

    }

    public void BrowsJobs(String keywords, String location, boolean remote, String visa, WebDriver driver) throws InterruptedException, IOException {
        // getting the list of already existing job IDS
        Vector<String> existingIds = new Vector<>();
        Scanner sc = new Scanner(new File("all.txt"));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            existingIds.add(line);
        }
        sc.close();
        //*****
        driver.get("https://www.linkedin.com/jobs/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Search by title, skill, or company']"))).sendKeys(keywords);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='City, state, or zip code']"))).sendKeys(location);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Search')]"))).click();
        Thread.sleep(2000);
        // Filter easy apply
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Easy Apply filter.']"))).click();
        Thread.sleep(3000);

        if (remote) {
            // Filter remote
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'On-site/Remote')]"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='workplaceType-2']"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-control-name='filter_show_results']"))).click();
            Thread.sleep(3000);
        }

        //scroll list of jobs one page
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementsByClassName('jobs-search-results display-flex flex-column')[0].scrollTo({top: 99999, behavior: 'smooth'});");
        Thread.sleep(10000);
        //get number of pages
        List<WebElement> pagination = driver.findElements(By.xpath("//button[contains(@aria-label, 'Page ')]"));
        int lastPage = Integer.parseInt(pagination.get(pagination.size() - 1).getAttribute("aria-label").replaceAll("Page ", ""));
        System.out.println(lastPage + "=> last page number");
        for (int p = 1; p <= lastPage; p++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Page " + p + "')]"))).click();
            Thread.sleep(2000);
            //scroll list of jobs one page
            js.executeScript("document.getElementsByClassName('jobs-search-results display-flex flex-column')[0].scrollTo({top: 99999, behavior: 'smooth'});");
            Thread.sleep(10000);

            List<WebElement> list = driver.findElements(By.xpath("//div[@data-job-id]"));
            for (WebElement el : list) {
                // check if job ID already exists in the file
                if (!existingIds.contains(el.getAttribute("data-job-id"))) {
                    FileWriter fw = new FileWriter(keywords + " " + location + " " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ".txt", true);
                    fw.write(el.getAttribute("data-job-id") + "\n"); //appends the string to the file
                    fw.close();
                } else {
                    System.out.println("exists");
                }

            }
            System.out.println(p + "=> current page number");
        }

    }

    public void Apply(String jobId, WebDriver driver) throws IOException, InterruptedException {
        driver.get("https://www.linkedin.com/jobs/view/" + jobId + "/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String tittle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='t-24 t-bold']"))).getText();
        String company = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='ember-view t-black t-normal']"))).getText();
        String location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(("(//span[@class='jobs-unified-top-card__bullet'])[1]")))).getText().trim();
        String dateApplied = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //  public Job(String id, String title, String company, String location, String status, String dateApplied)
        System.out.println("tittle: " + tittle + " company: " + company + " location :" + location + " " + dateApplied);
        Job myJob = new Job(jobId, tittle, company, location, "", "", dateApplied);

        Thread.sleep(3000);
        // check if still accept application
        if (driver.findElements(By.xpath("//span[contains(.,'No longer accepting applications')]")).size() == 1) {
            System.out.println("no longer accepts application");
            //check if already applied
        } else if (driver.findElements(By.xpath("//span[contains(.,'Application submitted')]")).size() > 0) {
            System.out.println("Already applied");
        } else {
            //click apply button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Easy Apply')]"))).click();
            System.out.println("Easy Applied Clicked applied");
            // pop up
            // check submit first thing
            boolean done = false;
            while (done == false) {
                System.out.println("inside loop");
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Submit application')]"))).click();
                    done = true;
                    System.out.println("submitted");
                    break;
                } catch (Exception ex) {
                    System.out.println("Submit not here");
                    try {
                        Thread.sleep(2000);
                        System.out.println("checking form");
                        List<WebElement> questions = driver.findElements(By.xpath("//div[@class='jobs-easy-apply-form-section__grouping']//span[@class='t-14 fb-form-element-label__title--is-required']"));

                        if (questions.size()>0) {
                            //get form
                            driver.findElement(By.xpath("//h3[contains(.,'Additional Questions')]"));
                              System.out.println(questions.size() + "=> number of questions");
                            for (int i = 0; i < questions.size(); i++) {
                                System.out.println(questions.get(i).getText());
                            }
                        }
                        // click next
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Next')]"))).click();
                        System.out.println("Next Clicked");
                        continue;
                    } catch (Exception ex2) {
                        System.out.println("Next not here");
                        // click review
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Review')]"))).click();
                        } catch (Exception ex3) {
                            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(.,'Great!')]")));
                            System.out.println("Done");
                            done = true;
                        }
                    }
                }


                //get all section
                //List<WebElement>

                //list of question sections //div[@class='jobs-easy-apply-form-section__grouping']//div//label//span
                // dropdowns fb-dropdown

                // questions list //label[@class='fb-form-element-label']//span[contains(.,'fb-form-element-label')]

                // next button second


                // write in file
             /*
                FileWriter fw = new FileWriter("Applications.txt", true); //the true will append the new data
                   fw.write(myJob.getId() + "@" + myJob.getTitle() + "@" + myJob.getCompany() + "@" + myJob.getLocation() + "@" + "Success" + "@" + myJob.getDateApplied() + "\n");
                fw.close();
              */

            }
        }


    }

    public WebDriver logIn(String username, String password) {
        WebDriverManager.chromedriver().browserVersion("98.0.4758.102").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--incognito");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");

       /* options.addArguments("--window-size=1920,1200");
        options.addArguments("--headless");*/

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.linkedin.com/checkpoint/rm/sign-in-another-account?fromSignIn=true");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Sign in' or @aria-label='S’identifier']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(.,'Issam Sahraoui')]")));
        return driver;
    }
}