package page.marketing.valueobject;

import com.microsoft.playwright.Locator;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Problem {
    private final Locator root;
    private final Locator taskLocator;
    private final Locator answersLocator;

    public Problem(Locator root){
        this.root = root;
        this.taskLocator = root.locator("xpath=//div/p/span").first();
        this.answersLocator = root.locator("//label//span");
    }

    public void clickAnswer(String answer){

        Locator ans = this.answersLocator.getByText(answer, new Locator.GetByTextOptions().setExact(true));
        if (ans.isVisible()){
            ans.click();
            return;
        }
        ans = this.answersLocator.getByText(Pattern.compile("^"+answer));
        if (ans.isVisible()){
            ans.click();
            return;
        }
        ans = this.answersLocator.getByText(answer).first();
        if (ans.isVisible()){
            ans.click();
            return;
        }
        ans = this.answersLocator.getByText(Pattern.compile("^[0-9]+,[0-9]+$")).first();
        if (ans.isVisible()){
            ans.click();
        }

    }

    public String getTitle(){
        return taskLocator.textContent();
    }

    public List<String> getAnswers(){
        return answersLocator.allTextContents();
    }
}
