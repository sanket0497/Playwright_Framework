package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    public void loginToApplication()
    {
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        String pageTitle=page.title();
        System.out.println("Title of the page is:"+pageTitle);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder("you@email.com").fill("raya11@gmail.com");
        page.getByLabel("Password").fill("Rayaba@1224");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
    }
}
