package page.marketing.valueobject;

import com.microsoft.playwright.Locator;
import java.util.List;

public class Problem {
    private final Locator taskLocator;
    private final Locator answersLocator;
    private final Locator input;

    public String questionType() {
        return input.getAttribute("type");
    }

    public Problem(Locator root) {
        this.taskLocator = root.locator("xpath=//div/p/span").first();
        this.answersLocator = root.locator("//label//span");
        this.input = root.locator("//input").first();
    }

    public void clickAnswer(String answer) {
        Locator ans = this.answersLocator.getByText(answer, new Locator.GetByTextOptions().setExact(true));
        ans.click();
    }

    public String getQuestion() {
        return taskLocator.textContent();
    }

    public List<String> getAnswers() {
        return answersLocator.allTextContents();
    }
}
