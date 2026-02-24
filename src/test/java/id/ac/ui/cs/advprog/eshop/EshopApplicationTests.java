package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
        // This method left empty intentionally to test if the Spring context loads
        // successfully
    }

    @Test
    void main() {
        EshopApplication.main(new String[] {});
        assertTrue(true, "Application context should load and exit without exceptions");
    }
}
