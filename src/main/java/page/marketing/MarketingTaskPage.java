package page.marketing;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import page.marketing.valueobject.Problem;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class MarketingTaskPage {
    private final Page page;
    private final List<Problem> problems = new ArrayList<>();
    private final Locator problemsLocator;
    private final Locator topicMenuLocator;
    private final FrameLocator taskFrame;


    public MarketingTaskPage(Page page){
        this.page = page;
        assertThat(page.locator("//h1")).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        topicMenuLocator = page.locator("//div[@class='sequence-navigation-tabs-container']");
        taskFrame = page.frameLocator("#unit-iframe");
//        problemsLocator = taskFrame.locator("//div[@class='problem']/div/p/span");
        problemsLocator = taskFrame.locator("//div[@class='problem']");
    }

    public void navigateToPractice(){
        topicMenuLocator.getByTitle("Практическое занятие").click();
        load();
    }

    public void navigateToSoloWork(){
        topicMenuLocator.getByTitle("Самостоятельная работа").click();
        load();
    }

    public void load(){
        page.locator("//iframe[@id='unit-iframe']").waitFor();
    }

    public List<Problem> getProblems() {
        int count = problemsLocator.count();
        for (int i=0; i< count; i++){
            problems.add(new Problem(problemsLocator.nth(i)));
        }
        return problems;
    }
}
