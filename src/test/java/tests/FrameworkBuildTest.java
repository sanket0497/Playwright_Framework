package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class FrameworkBuildTest extends BaseTest {

    @Test
    public void popUpValidationTest()
    {
        //check element is visible
        System.out.println(page.getByPlaceholder("Hide/Show Example").isVisible());
        //click on Hide
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Hide")).click();
        //check Hidden or not
        System.out.println(page.getByPlaceholder("Hide/Show Example").isHidden());

        //handle dialogue box ..here will accept
        page.onDialog(dialog -> dialog.accept());
        //click on Alert button it will open dialogue box
        page.locator("#alertbtn").click();
        page.waitForTimeout(3000);

        //Mouse Hover and click on first link
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();
        page.waitForTimeout(3000);
        System.out.println(" ");
        //System.out.println("Done");

        //Frame Handling
        FrameLocator fm=page.frameLocator("#courses-iframe");
        fm.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        page.waitForTimeout(3000);

    }

    @Test
    public void takesScreenShot()
    {
        //page level ss
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("page.png")));
        //locator level ss
        Locator alertBox= page.locator("#alertbtn");
        alertBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("locator.png")));
    }
}
