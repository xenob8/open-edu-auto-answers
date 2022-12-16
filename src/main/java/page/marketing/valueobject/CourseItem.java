package page.marketing.valueobject;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;

public class CourseItem {
//    private final Locator titleLocator;
    private final static String TITLE_SELECTOR = "//span";
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

//    public String getTitle() {
//        return titleLocator.textContent();
//    }

    public void collapse() {
        if (collapse_button.isVisible()) {
            collapse_button.click();
        }
    }

    public String getTopic(){
        return topicLink.textContent();
    }

    public void navigateTopic(){
        this.topicLink.click();
    }

    public void navigateAttestation(){
        this.attestationLink.click();
    }


    @Override
    public String toString() {
        return "CourseItem{" +
                "title='" + '\'' +
                '}';
    }
}
