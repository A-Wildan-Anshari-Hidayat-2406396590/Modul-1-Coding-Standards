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
class OrderFunctionalTest {

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
    void createOrder_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");

        // Fill out author name
        driver.findElement(By.id("authorInput")).clear();
        driver.findElement(By.id("authorInput")).sendKeys("John Doe");

        // Submit form
        driver.findElement(By.tagName("form")).submit();

        // Redirects or can be navigated to order history
        driver.get(baseUrl + "/order/history");

        // Search for the author's orders
        driver.findElement(By.id("authorInput")).clear();
        driver.findElement(By.id("authorInput")).sendKeys("John Doe");
        driver.findElement(By.tagName("form")).submit();

        // Check the table for the order
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        assertFalse(rows.isEmpty());

        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> columns = lastRow.findElements(By.tagName("td"));

        // Ensure status is waiting for payment
        assertEquals("WAITING_PAYMENT", columns.get(1).getText());
    }
}
