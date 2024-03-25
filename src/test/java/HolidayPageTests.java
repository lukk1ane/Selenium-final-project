import data.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.asserts.SoftAssert;
import java.time.Duration;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
public class HolidayPageTests {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    SoftAssert softAssert = new SoftAssert();


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
    public void descendingOrderTest() throws InterruptedException {
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.dasvenebaString));
        section.click();
        List<String> prices = new ArrayList<>();
        int firstMax;

        for (int i = 1; i <= 17; i++) {
            String pageUrl = Constants.page1.replace("1", String.valueOf(i)); //https://www.swoop.ge/category/24/dasveneba/?page=1   1-2  -> 2-3
            driver.get(pageUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Constants.dealVoucherPrice)));

            List<WebElement> elements = driver.findElements(By.className(Constants.dealVoucherClass));
            for (WebElement element : elements) {
                String priceText = element.getText().trim();

                String textDecoration = element.getAttribute(Constants.textDecorationString);
                if (textDecoration != null && textDecoration.contains(Constants.differenceOfPrices)) {
                    continue;
                }

                String priceValue = priceText.replaceAll("[^0-9]", "");
                if (!priceValue.isEmpty()) {
                    int price = Integer.parseInt(priceValue);
                    prices.add(String.valueOf(price));
                }
            }
        }


        List<Integer> numericPrices = prices.stream()
                .map(Integer::parseInt)
                .toList();

        Optional<Integer> maxPrice = numericPrices.stream()
                .max(Comparator.naturalOrder());

        firstMax= maxPrice.orElse(0);
        maxPrice.ifPresent(price -> System.out.println("Max Price: " + price));

        //After pressing descending check
        driver.get(Constants.page1);
        WebElement sorting= driver.findElement(By.id(Constants.sortingId));
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        sorting.sendKeys(Keys.ARROW_DOWN);
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        sorting.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        WebElement priceElement = driver.findElement(By.cssSelector(Constants.firstPriceSelector));

        String priceText = priceElement.getText();
        String priceValue = priceText.replaceAll("[^0-9]", "");
        int price = Integer.parseInt(priceValue);

        System.out.println("Price: " + price);
        Assert.assertEquals(price, firstMax);

    }

    @Test
    public void ascendingOrderTest() {
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.dasvenebaString));
        section.click();
        List<String> prices = new ArrayList<>();
        int min;

        for (int i = 1; i <= 17; i++) {
            String pageUrl = Constants.page1.replace("1", String.valueOf(i)); //https://www.swoop.ge/category/24/dasveneba/?page=1   1-2  -> 2-3
            driver.get(pageUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Constants.dealVoucherPrice)));

            List<WebElement> elements = driver.findElements(By.className(Constants.dealVoucherClass));
            for (WebElement element : elements) {
                String priceText = element.getText().trim();

                String textDecoration = element.getAttribute(Constants.textDecorationString);
                if (textDecoration != null && textDecoration.contains(Constants.differenceOfPrices)) {
                    continue;
                }

                String priceValue = priceText.replaceAll("[^0-9]", "");
                if (!priceValue.isEmpty()) {
                    int price = Integer.parseInt(priceValue);
                    prices.add(String.valueOf(price));
                }
            }
        }

        List<Integer> numericPrices = prices.stream()
                .map(Integer::parseInt)
                .toList();

        Optional<Integer> minPrice = numericPrices.stream()
                .min(Comparator.naturalOrder());

        int firstMin = minPrice.orElse(0);
        minPrice.ifPresent(price -> System.out.println("Min Price: " + price));

        driver.get(Constants.page1);
        WebElement sorting= driver.findElement(By.id(Constants.sortingId));
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        sorting.sendKeys(Keys.ARROW_DOWN);
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        sorting.sendKeys(Keys.ARROW_DOWN);
        wait.until(ExpectedConditions.elementToBeClickable(sorting));

        sorting.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.elementToBeClickable(sorting));
        WebElement priceElement = driver.findElement(By.cssSelector(Constants.firstPriceSelector));

        String priceText = priceElement.getText();
        String priceValue = priceText.replaceAll("[^0-9]", "");
        int price = Integer.parseInt(priceValue);

        System.out.println("Price: " + price);
        Assert.assertEquals(price, firstMin);   //but bug on swoop.ge

    }

    @Test
    public void filterTest() throws InterruptedException {
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.dasvenebaString));
        section.click();


        WebElement cottage= driver.findElement(By.xpath(Constants.cottageSelectXpath));
        cottage.click();

        WebElement loader= driver.findElement(By.xpath(Constants.loaderXpath));
        wait.until(ExpectedConditions.visibilityOf(loader));
        wait.until(ExpectedConditions.invisibilityOf(loader));

        List<WebElement> description=driver.findElements(By.xpath(Constants.descriptionXpath));

        for (WebElement text:description) {
            wait.until(ExpectedConditions.visibilityOf(text));
            String checkText= String.valueOf(text.getText());
            softAssert.assertTrue(checkText.contains("კოტეჯი"));

        }

        WebElement sortBar = driver.findElement(By.id(Constants.sortingId));
        sortBar.click();


        Select options = new Select(sortBar);
        wait.until(ExpectedConditions.visibilityOf(sortBar));
        options.selectByValue("2");

        Thread.sleep(5000);

        List <WebElement> prices=driver.findElements(By.xpath(Constants.pricesXpathFilter));

        int lowestPrice=Integer.parseInt(prices.get(0).getText().replace("₾", ""));

        for (WebElement price: prices) {
            wait.until(ExpectedConditions.visibilityOf(price));
            int currentPrice = Integer.parseInt(price.getText().replace("₾", ""));
            System.out.println(currentPrice);
        softAssert.assertTrue(lowestPrice<=currentPrice);

        }

        softAssert.assertAll();
    }


    @Test
    public void priceRangeTest(){
        driver.get(Constants.site1);
        WebElement section = driver.findElement(By.linkText(Constants.dasvenebaString));
        section.click();

        int minPriceValue = 30;
        int maxPriceValue = 60;

        WebElement minPriceInput = driver.findElement(By.xpath(Constants.minPriceInputXpath));
        minPriceInput.sendKeys(String.valueOf(minPriceValue));

        WebElement maxPriceInput = driver.findElement(By.xpath(Constants.maxPriceInputXpath));
        maxPriceInput.sendKeys(String.valueOf(maxPriceValue));

        WebElement search= driver.findElement(By.xpath(Constants.searchXpath));
        search.click();
        WebElement loader= driver.findElement(By.xpath(Constants.loaderXpath));
        // three dots ... until there is load.

        wait.until(ExpectedConditions.visibilityOf(loader));
        wait.until(ExpectedConditions.elementToBeClickable(search));

        List<WebElement> priceList=driver.findElements(By.xpath(Constants.priceListXpath));
        for (WebElement price: priceList) {
            wait.until(ExpectedConditions.visibilityOf(price));
            int currentPrice=Integer.parseInt(price.getText().replace("₾" ,""));
            System.out.println(currentPrice);
            Assert.assertTrue(minPriceValue<=currentPrice);
            Assert.assertTrue(maxPriceValue>=currentPrice);

        }
    }


}

