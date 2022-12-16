import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import excel.AnswerParser;
import org.opentest4j.AssertionFailedError;
import page.login.LoginPage;
import page.marketing.MarketingHomePage;
import page.marketing.MarketingTaskPage;
import page.marketing.valueobject.CourseItem;
import page.marketing.valueobject.Problem;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class App {

    static private final String login = "solodovnikov.sf@edu.spbstu.ru";
    static private final String password = "Sus4xok";

        public static void main(String[] args) throws IOException {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setLocale("ru"));

            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));

            Page page = context.newPage();

            LoginPage loginPage = new LoginPage(page);
            loginPage.navigate();
            loginPage.clickLogin();
            loginPage.clickPoly();
            loginPage.fillLogin(login);
            loginPage.fillPassword(password);
            loginPage.polySubmit();

            MarketingHomePage marketingHomePage = new MarketingHomePage(page);
            CourseItem item = marketingHomePage.getCourseItemList().get(6);
            item.collapse();
            item.getTopic();
            item.navigateTopic();
            MarketingTaskPage taskPage = new MarketingTaskPage(page);
            taskPage.navigateToPractice();
            List<Problem> problems = taskPage.getProblems();
            AnswerParser parser = new AnswerParser();
            problems.forEach(problem -> {
                String question = problem.getTitle();
                System.out.println("Вопрос? " + question);
                System.out.println("найденый ответ:");
                Optional<Map<String, String>> answer = parser.getAnswer(question);
                System.out.println(answer);
                answer.ifPresent(e -> {
                    String ans = e.get("ОТВЕТ");
                    if (ans != null){
                        problem.clickAnswer(ans);
                    }

                });
                System.out.println("Возможные ответы: ");
                System.out.println(problem.getAnswers());
            });
            taskPage.navigateToSoloWork();
            problems = taskPage.getProblems();
            problems.forEach(problem -> {
                String question = problem.getTitle();
                System.out.println("Вопрос? " + question);
                System.out.println("найденый ответ:");
                Optional<Map<String, String>> answer = parser.getAnswer(question);
                System.out.println(answer);
                answer.ifPresent(e -> {
                    String ans = e.get("ОТВЕТ");
                    if (ans != null){
                        problem.clickAnswer(ans);
                    }

                });
                System.out.println("Возможные ответы: ");
                System.out.println(problem.getAnswers());
            });



            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
            page.pause();
        }
    }

}