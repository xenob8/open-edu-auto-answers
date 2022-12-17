package page.marketing.valueobject;

import com.microsoft.playwright.Locator;

import java.util.List;
import java.util.regex.Pattern;


public class Problem {
    private final Locator taskLocator;
    private final Locator answersLocator;

    public Problem(Locator root) {
        this.taskLocator = root.locator("xpath=//div/p/span").first();
        this.answersLocator = root.locator("//label//span");
    }

    private Locator findAnswer(String answer) {
        Locator ans = this.answersLocator.getByText(answer, new Locator.GetByTextOptions().setExact(true));
        if (ans.isVisible()) {
            return ans;
        }
        ans = this.answersLocator.getByText(Pattern.compile("^" + answer));
        if (ans.isVisible()) {
            return ans;
        }
        ans = this.answersLocator.getByText(answer).first();
        if (ans.isVisible()) {
            return ans;
        }
        ans = this.answersLocator.getByText(Pattern.compile("^[0-9]+,[0-9]+$")).first();
        if (ans.isVisible()) {
            return ans;
        }
        System.out.println("no matched answer");
        return null;
    }

    public boolean clickAnswer(String answer) {
        Locator ans = this.findAnswer(answer);
        if (ans != null) {
            ans.click();
            return true;
        }
        return false;
    }

    public String getTitle() {
        return taskLocator.textContent();
    }

    public List<String> getAnswers() {
        return answersLocator.allTextContents();
    }
}
