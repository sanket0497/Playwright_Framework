package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp()
    {
         playwright= Playwright.create();
         browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
         page= browser.newPage();
         page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @Test(description="Create Event and Book that event and verify it's booked")
    public void demoTest()
    {


       assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))) .isVisible();
       //Step 1 : Create Event
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("QA Summit Rahul Shetty");
        page.locator("#admin-event-form textarea").fill("Rahul Shetty QA Meetups");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Test City");
        page.getByLabel("Venue").fill("Test Venue");
        page.getByLabel("Event Date & Time").fill("2026-12-10T20:12");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("50");
        page.locator("#add-event-btn").click();
        //Event Created
        assertThat(page.getByText("Event created")).isVisible();


        //Step 2 : Find newly craeted event under events page
        page.locator("#nav-events").click();
        //locate all events
       Locator eventCards= page.getByTestId("event-card");
       System.out.println("Total Events:"+eventCards.count());
       //filter and check our event
        Locator targetCard=eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit Rahul Shetty"));
        assertThat(targetCard).isVisible();
        String seatsTextBeforeBooking=targetCard.getByText("seats").innerText();
        int seatCountBeforeBooking=Integer.parseInt(seatsTextBeforeBooking.split(" ")[0]);
        System.out.println("Seat count Before Booking:"+seatCountBeforeBooking);
        System.out.println(seatsTextBeforeBooking);
        //click on Book Now
        targetCard.getByTestId("book-now-btn").click();

        //enter user details
        page.getByLabel("Full Name").fill("Rahul");
        page.getByPlaceholder("you@email.com").fill("Rahul@email.com");
        page.getByLabel("Phone Number").fill("0000001111");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        //assert ticket reserved text is present on the screen
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        //grab booking refernce id, so will verify our booking is present in Bookings, by compa
        // iring this ref id
        String bookingRef= page.locator(".booking-ref").innerText();
        System.out.println("Booking Reference:"+bookingRef);
        //go to my bookings and see our booking is present
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //verify in the booking history
        Locator bookingCards=page.getByTestId("booking-card");
        page.waitForTimeout(2000);
        System.out.println("Total booking cards:"+bookingCards.count());
       Locator targetBookingCard= bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
       assertThat(targetBookingCard).isVisible();

       //Now go to events page and see booking seat count has been reduced or not, seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(2000);
        //locate all events
        Locator eventCardsAfterBooking= page.getByTestId("event-card");
        //filter and check our event
        Locator targetCardAfterBooking=eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("QA Summit Rahul Shetty"));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking=targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        int seatCountAfterBooking=Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);
        System.out.println("Seat count After Booking:"+seatCountAfterBooking);

        //check seatsTextBeforeBooking > seatsTextAfterBooking (Logic)
        //here both before and afterbooking are strings, so get number we need to parse it and get number out of it
        //for comparison

        Assert.assertTrue(seatCountBeforeBooking>seatCountAfterBooking);











    }
}
