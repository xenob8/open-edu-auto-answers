import com.microsoft.playwright.*;
import excel.AnswerParser;
import io.github.cdimascio.dotenv.Dotenv;
import page.login.LoginPage;
import page.marketing.MarketingHomePage;
import page.marketing.MarketingTaskPage;
import page.marketing.valueobject.CourseItem;
import page.marketing.valueobject.Problem;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    static private final String login = Dotenv.load().get("login");
    static private final String password = Dotenv.load().get("password");
    static private final boolean isLoginMod = Dotenv.load().get("isLoginMod").equals("true");
    private final AnswerParser parser = new AnswerParser();
    private final BrowserContext context;
    private final Page page;

    public Browser.NewContextOptions getContextOptions() {
        Browser.NewContextOptions options = new Browser.NewContextOptions().setLocale("ru");
        if (!isLoginMod) {
            options.setStorageStatePath(Paths.get("state.json"));
        }
        return options;
    }

    public App() throws IOException {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
        context = browser.newContext(getContextOptions());
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();

    }

    public static void main(String[] args) throws IOException {

        App app = new App();
        if (isLoginMod) {
            app.login();
            app.saveState();
        }
        app.run();
    }

    public void run() {
        try {
            MarketingHomePage marketingHomePage = MarketingHomePage.navigate(page);
            List<CourseItem> itemList = marketingHomePage.getCourseItemList();
            int size = itemList.size();
            for (int i = 3; i < size; i++) {
                CourseItem item = marketingHomePage.getCourseItemList().get(i);
                MarketingTaskPage taskPage = item.expand().navigateToTopic(page);
                taskPage.navigateToPractice();
                debug(this.handleProblems(taskPage.getProblems()));

                taskPage.navigateToSoloWork();
                debug(this.handleProblems(taskPage.getProblems()));

                MarketingHomePage.navigate(page);
                taskPage = item.expand().navigateToAttestation(page);
                debug(this.handleProblems(taskPage.getProblems()));

                MarketingHomePage.navigate(page);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
            page.pause();
            throw e;
        }
    }

    private int handleProblems(List<Problem> problems) {
        if (problems.isEmpty()) {
            return 0;
        }
        AtomicInteger unresolvedCount = new AtomicInteger();
        problems.forEach(problem -> {
            String question = problem.getQuestion();
            System.out.println("Вопрос? " + question);
            System.out.println("найденый ответ:");
            if (problem.questionType().equals("checkbox")) {
                List<String> answers = problem.getAnswers();
                for (String ans : answers) {
                    if (parser.isAnswer(question, ans)) {
                        problem.clickAnswer(ans);
                    }
                }
            } else {
                String answer = parser.getRightAnswerFromList(question, problem.getAnswers());
                System.out.println(answer);
                if (answer != null) {
                    problem.clickAnswer(answer);
                } else {
                    unresolvedCount.incrementAndGet();
                }
            }

            System.out.println("Возможные ответы: ");
            System.out.println(problem.getAnswers());
        });
        return unresolvedCount.get();
    }


    private void saveState() {
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));
    }

    private void login() {
        LoginPage loginPage = LoginPage.navigate(page);
        loginPage.clickLogin().clickPoly().fillLogin(login).fillPassword(password).polySubmit();
    }

    private void debug(int unresolvedCount) {
        if (unresolvedCount > 0) {
            System.out.println("Unresolved " + unresolvedCount + "questions");
            page.pause();
        }
    }

}
