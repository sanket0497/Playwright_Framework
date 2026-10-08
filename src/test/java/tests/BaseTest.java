package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    String base_url;
    @BeforeMethod
    public void setUp() throws IOException {
        Properties pr = new Properties();
        FileInputStream fis= new FileInputStream("src/test/resources/config.properties");
        pr.load(fis);
        String browserName=pr.getProperty("browser");
        playwright= Playwright.create();
        if(browserName.equalsIgnoreCase("firefox"))
        {
             browser=playwright.firefox().launch();
        }

        else if(browserName.equalsIgnoreCase("safari"))
        {
            browser=playwright.webkit().launch();
        }else {
            browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }


        //by default playwright operates in headless mode, but we want to launch in headed mode so we gave
        // corresponding parameters
        //under launch method
        page= browser.newPage();
        String base_url=pr.getProperty("url");
        page.navigate(base_url);
    }
}
