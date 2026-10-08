package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationsTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;



    @BeforeMethod
    public void setUp()
    {
        playwright=Playwright.create();
        browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //by default playwright operates in headless mode, but we want to launch in headed mode so we gave
        // corresponding parameters
        //under launch method
        context=browser.newContext();
        //start tracing before page launch
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page=context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @Test
    public void childWindowHandle() throws InterruptedException {
      //Will click on blinking link and it will open new page, will capture that page at the same time when we are
        //clicking on it
        Locator blinkingText=page.locator(".blinkingText").first();
        //here page is opening under same context , so will use same context reference
       Page newPage= context.waitForPage(()-> blinkingText.click());
       //so now,'page' refers to parent/old page and 'newPage' refers to new/child page
        newPage.waitForLoadState();
        //above method will wait untill the page is fully loaded., navigate method by default waits untill the page is
        //fully loaded, so we don't define this method there.
        //featch email id from child window and fill it in input box of parent window
        Locator mailId=newPage.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("mentor@rahulshettyacademy.com"));
        String mail_id=mailId.innerText();
        System.out.println("Mentor mail_id:"+mail_id);
        //now come to parent window and enter this in username field inputbox
        page.getByLabel("Username:").fill(mail_id);
        //now we want to fetch value entered in the Username field, it's dynamically inserted,not present from the begining,
        //so in such cases we need to use inputValue() method to fetch such text/value
        String usernameInputValue=page.getByLabel("Username:").inputValue();
        System.out.println("Username Input Value:"+usernameInputValue);
        page.waitForTimeout(4000);
    }

    @Test
    public void uiControls()
    {
        Locator UserRdBtn=page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(" User"));
        UserRdBtn.click();
       //click okay for pop up acceptance
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        //Assert User radio button is selected
        Assert.assertTrue(UserRdBtn.isChecked());

        //select checkbox and Assert it is selected or not
       Locator consentChkBox= page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
       consentChkBox.check();
       Assert.assertTrue(consentChkBox.isChecked());
       page.waitForTimeout(4000);
    }

    @AfterMethod
    public void teardown()
    {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }


}
