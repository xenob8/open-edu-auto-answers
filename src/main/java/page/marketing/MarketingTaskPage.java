package page.marketing;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import page.BasePage;
import page.marketing.valueobject.Problem;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class MarketingTaskPage extends BasePage {
    private final Locator problemsLocator;
    private final Locator topicMenuLocator;
    private final FrameLocator taskFrame;


    public MarketingTaskPage(Page page) {
        super(page);
        topicMenuLocator = page.locator("//div[@class='sequence-navigation-tabs-container']");
        taskFrame = page.frameLocator("#unit-iframe");
        problemsLocator = taskFrame.locator("//div[@class='problem']");
    }

    public MarketingTaskPage navigateToPractice() {
        topicMenuLocator.getByTitle("Практическое занятие").click();
        return new MarketingTaskPage(page);
    }

    public MarketingTaskPage navigateToSoloWork() {
        topicMenuLocator.getByTitle("Самостоятельная работа").click();
        return new MarketingTaskPage(page);
    }

    @Override
    public void isLoaded() {
        page.locator("//iframe[@id='unit-iframe']").waitFor();
    }

    public List<Problem> getProblems() {
        List<Problem> problems = new ArrayList<>();
        int count = problemsLocator.count();
        for (int i = 0; i < count; i++) {
            problems.add(new Problem(problemsLocator.nth(i)));
        }
        return problems;
    }
}
