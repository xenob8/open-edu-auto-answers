package page.marketing.valueobject;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import page.marketing.MarketingTaskPage;

public class CourseItem {

    private final Locator collapse_button;
    private final Locator topicLink;
    private final Locator attestationLink;

    private final Locator root;

    public CourseItem(Locator locator) {
        this.root = locator;
        this.collapse_button = locator.getByRole(AriaRole.BUTTON).getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Открыть"));
        this.topicLink = locator.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Тема"));
        this.attestationLink = locator.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Аттестация"));
    }

    public CourseItem expand() {
        if (collapse_button.isVisible()) {
            collapse_button.click();
        }
        return this;
    }

    public String getTopic() {
        return topicLink.textContent();
    }

    public MarketingTaskPage navigateToTopic(Page page) {
        this.topicLink.click();
        return new MarketingTaskPage(page);
    }

    public MarketingTaskPage navigateToAttestation(Page page) {
        this.attestationLink.click();
        return new MarketingTaskPage(page);
    }


    @Override
    public String toString() {
        return "CourseItem{" +
                "title='" + '\'' +
                '}';
    }
}
