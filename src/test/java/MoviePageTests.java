import data.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class MoviePageTests {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser) {
        if(browser.equalsIgnoreCase("chrome")){
            WebDriverManager.chromedriver().setup();
            driver=new ChromeDriver();
        }else if(browser.equalsIgnoreCase("edge")){
            WebDriverManager.edgedriver().setup();
            driver= new EdgeDriver();
        }else if(browser.equalsIgnoreCase("firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver=new FirefoxDriver();
        }

        driver.manage().window().maximize();

    }


    @AfterMethod
    public void  tearDown(){
        driver.close();
    }


    @Test
    public void movieTest() throws InterruptedException {
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.cinemaString));
        section.click();
        WebElement firstDiv= driver.findElement(By.xpath(Constants.firstMovieDiv));

        wait.until(ExpectedConditions.visibilityOf(firstDiv));
        Actions actions = new Actions(driver);
        actions.moveToElement(firstDiv).perform();
        WebElement purchaseElement = firstDiv.findElement(By.xpath(Constants.purchaseXpath));
        purchaseElement.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(Constants.scrollBy300);
        WebElement caveaEastPoint = driver.findElement(By.xpath(Constants.moviePlaceC));
        caveaEastPoint.click();

        WebElement lastDate= driver.findElement(By.xpath(Constants.lastDateXpath));

        lastDate.click();
        js.executeScript(Constants.scrollby600);

        WebElement name= driver.findElement(By.xpath(Constants.nameXpath));
        String[] date=lastDate.getText().split(" ");
        System.out.println(date[0]);

        WebElement lastSession =driver.findElement(By.xpath(Constants.lastSessionXpath));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(Constants.lastSessionXpath))));
        lastSession.click();

        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(Constants.movieNameXpath))));
        WebElement movieName=driver.findElement(By.xpath(Constants.movieNameXpath));

        WebElement moviePlace=driver.findElement(By.xpath(Constants.caveaPlaceXpath));

        WebElement movieDate=driver.findElement(By.xpath(Constants.movieDate));

        Assert.assertEquals(movieName.getText(), name.getText());
        Assert.assertEquals(moviePlace.getText(),Constants.moviePlace);
        Assert.assertTrue(movieDate.getText().contains(date[0]));

        WebElement freePlace=driver.findElement(By.xpath(Constants.freePlaceXpath));
        freePlace.click();

        WebElement registration = driver.findElement(By.xpath(Constants.registrationXpath));
        registration.click();

        WebElement mail=driver.findElement(By.id(Constants.mailId));
        mail.sendKeys("gutsberserk-eclipse.com");

        WebElement password=driver.findElement(By.id(Constants.pass1ID));
        password.sendKeys("Guts123!");

        WebElement password2=driver.findElement(By.id(Constants.pass2Id));
        password2.sendKeys("Guts123!");

        WebElement gender=driver.findElement(By.xpath(Constants.genderXpath));
        gender.click();

        WebElement nameOfUser=driver.findElement(By.id(Constants.userName));
        nameOfUser.sendKeys("Guts");

        WebElement surNameOfUser=driver.findElement(By.id(Constants.userSurname));
        surNameOfUser.sendKeys("Berserk");

        WebElement spanElement = driver.findElement(By.xpath(Constants.spanXpath));
        spanElement.click();
        WebElement liItem = driver.findElement(By.xpath(Constants.dateXpath));
        liItem.click();

        WebElement phoneNumber= driver.findElement(By.id(Constants.phoneNum));
        phoneNumber.sendKeys("599999999");

        WebElement phoneCode= driver.findElement(By.id(Constants.phoneCode));
        phoneCode.sendKeys("4444");

        js.executeScript(Constants.scrollBy500);

        List<WebElement> checkmarks = driver.findElements(By.className(Constants.checkmarkName));
        wait.until(ExpectedConditions.visibilityOfAllElements(checkmarks));
        for (WebElement checkmark : checkmarks) {
            checkmark.click();
        }

        js.executeScript(Constants.scrollBy500);

        WebElement submitButton=driver.findElement(By.id(Constants.submitID));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();

        WebElement errorOnMail=driver.findElement(By.id(Constants.mailErrorId));
        String mssg=errorOnMail.getText();

        Assert.assertTrue(mssg.contains(Constants.mailErrorMessage));
    }
}