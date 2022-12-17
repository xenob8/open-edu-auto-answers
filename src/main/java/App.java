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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class App {

    static private final String login = "solodovnikov.sf@edu.spbstu.ru";
    static private final String password = "Sus4xok";
    private final AnswerParser parser = new AnswerParser();

    public App() throws IOException {
    }

    private void saveState(BrowserContext context) {
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));
    }

    private void login(Page page) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        loginPage.clickLogin().clickPoly().fillLogin(login).fillPassword(password).polySubmit();
    }

    private int handleProblems(List<Problem> problems) {
        if (problems.isEmpty()) {
            return 0;
        }
        AtomicInteger unresolvedCount = new AtomicInteger();
        problems.forEach(problem -> {
            String question = problem.getTitle();
            System.out.println("Вопрос? " + question);
            System.out.println("найденый ответ:");
            Optional<Map<String, String>> answer = parser.getAnswer(question);
            System.out.println(answer);
            answer.ifPresent(e -> {
                String ans = e.get("ОТВЕТ");
                if (ans != null) {
                    if (!problem.clickAnswer(ans)){
                        unresolvedCount.getAndIncrement();
                    }
                }

            });
            System.out.println("Возможные ответы: ");
            System.out.println(problem.getAnswers());
        });
        return unresolvedCount.get();
    }

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.run();
    }

    public void run() throws IOException {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setLocale("ru").setStorageStatePath(Paths.get("state.json")));

            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));

            Page page = context.newPage();
            try {
                MarketingHomePage marketingHomePage = new MarketingHomePage(page);
                List<CourseItem> itemList = marketingHomePage.getCourseItemList();
                int size = itemList.size();
                for (int i = 0; i < size; i++) {
                    CourseItem item = marketingHomePage.getCourseItemList().get(i);
                    item.collapse();
                    item.getTopic();
                    item.navigateTopic();
                    MarketingTaskPage taskPage = new MarketingTaskPage(page);
                    taskPage.navigateToPractice();
                    List<Problem> problems = taskPage.getProblems();
                    int unresolvedCount = this.handleProblems(problems);
                    if (unresolvedCount > 0){
                        page.pause();
                    }

                    taskPage.navigateToSoloWork();
                    problems = taskPage.getProblems();
                    unresolvedCount = this.handleProblems(problems);
                    if (unresolvedCount > 0){
                        page.pause();
                    }
                    marketingHomePage.navigate();
                    item.collapse();
                    item.navigateAttestation();
                    taskPage.load();
                    problems = taskPage.getProblems();
                    unresolvedCount = this.handleProblems(problems);
                    if (unresolvedCount > 0){
                        page.pause();
                    }
                    marketingHomePage.navigate();
                }

            } catch (Exception e) {
                System.out.println(e);
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("trace.zip")));
                page.pause();
                throw e;
            }

        }
    }

}