import com.microsoft.playwright.*;
import excel.AnswerParser;
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

    static private final String login = "solodovnikov.sf@edu.spbstu.ru";
    static private final String password = "Sus4xok";
    private final AnswerParser parser = new AnswerParser();
    private final BrowserContext context;

    public App() throws IOException {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
        context = browser.newContext(new Browser.NewContextOptions().setLocale("ru").setStorageStatePath(Paths.get("state.json")));
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));

    }


    public static void main(String[] args) throws IOException {
        App app = new App();
        app.run();
    }

    public void run() throws IOException {
        Page page = context.newPage();
        try {
            MarketingHomePage marketingHomePage = MarketingHomePage.navigate(page);
            List<CourseItem> itemList = marketingHomePage.getCourseItemList();
            int size = itemList.size();
            for (int i = 3; i < size; i++) {
                CourseItem item = marketingHomePage.getCourseItemList().get(i);
                MarketingTaskPage taskPage = item.expand().navigateToTopic(page);
                taskPage.navigateToPractice();
                debug(this.handleProblems(taskPage.getProblems()), page);

                taskPage.navigateToSoloWork();
                debug(this.handleProblems(taskPage.getProblems()), page);

                MarketingHomePage.navigate(page);
                taskPage = item.expand().navigateToAttestation(page);
                debug(this.handleProblems(taskPage.getProblems()), page);

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


    private void saveState(BrowserContext context) {
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));
    }

    private void login(Page page) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        loginPage.clickLogin().clickPoly().fillLogin(login).fillPassword(password).polySubmit();
    }

    private void debug(int unresolvedCount, Page page) {
        if (unresolvedCount > 0) {
            System.out.println("Unresolved " + unresolvedCount + "questions");
            page.pause();
        }
    }

}
