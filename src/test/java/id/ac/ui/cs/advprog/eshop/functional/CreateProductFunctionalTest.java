package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("nameInput")).clear();
        driver.findElement(By.id("nameInput")).sendKeys("Sampo Cap Bambang");

        driver.findElement(By.id("quantityInput")).clear();
        driver.findElement(By.id("quantityInput")).sendKeys("100");

        driver.findElement(By.tagName("form")).submit();

        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        assertFalse(rows.isEmpty());

        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> columns = lastRow.findElements(By.tagName("td"));
        assertEquals("Sampo Cap Bambang", columns.get(0).getText());
        assertEquals("100", columns.get(1).getText());
    }

    @Test
    void createProduct_appearsInProductList(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");
        driver.findElement(By.id("nameInput")).sendKeys("Sampo Cap Usep");
        driver.findElement(By.id("quantityInput")).sendKeys("50");
        driver.findElement(By.tagName("form")).submit();

        driver.get(baseUrl + "/product/list");
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Usep"));
        assertTrue(pageSource.contains("50"));
    }
}
