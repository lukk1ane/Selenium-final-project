import data.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

public class LandingPageTests {
    WebDriver driver;

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
    }

    @AfterMethod
    public void  tearDown(){
        driver.close();
    }

    @Test
    public void activeCategoryTest(){
        driver.get(Constants.site1);
        WebElement categories= driver.findElement(By.xpath(Constants.categoriesXpath));
        categories.click();
        WebElement sport=driver.findElement(By.xpath(Constants.sportsXpath));
        Actions actions = new Actions(driver);
        actions.moveToElement(sport).perform();
        WebElement carting= driver.findElement(By.xpath(Constants.cartingXpath));
        carting.click();

        String currentUrl= driver.getCurrentUrl();
        Assert.assertEquals(currentUrl,Constants.cartingUrl);

        WebElement cartingColor= driver.findElement(By.xpath(Constants.cartingColorXpath));
        String hexColor = Main.convertRGBToHex(cartingColor.getCssValue("color"));
        System.out.println(hexColor);
        Assert.assertTrue(hexColor.equalsIgnoreCase(Constants.colorValue));

    }


    @Test
    public void logoTest(){
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.dasvenebaString));
        section.click();

        WebElement logoPic= driver.findElement(By.xpath(Constants.logoXpath));
        logoPic.click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, Constants.site1);
    }
}