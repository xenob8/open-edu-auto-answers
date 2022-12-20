package page.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitUntilState;
import page.BasePage;

public class LoginPage extends BasePage {
    private final Locator loginButton;
    private final Locator polyLocator;
    private final Locator loginInput;
    private final Locator passwordInput;
    private final Locator polySumbitButton;
    private final static String BASE_URL = "https://openedu.ru/";


    public LoginPage(Page page) {
        super(page);
        this.loginButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Вход"));
        this.polyLocator = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Политех"));

        this.loginInput = page.getByLabel("Логин");
        this.passwordInput = page.getByLabel("Пароль");
        this.polySumbitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Войти"));
    }

    @Override
    public void isLoaded() {

    }

    public static LoginPage navigate(Page page) {
        page.navigate(BASE_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.COMMIT));
        return new LoginPage(page);
    }

    public LoginPage clickLogin() {
        loginButton.click();
        return this;
    }

    public LoginPage clickPoly() {
        polyLocator.click();
        return this;
    }

    public LoginPage fillLogin(String login) {
        loginInput.fill(login);
        return this;
    }

    public LoginPage fillPassword(String password) {
        passwordInput.fill(password);
        return this;
    }

    public LoginPage polySubmit() {
        polySumbitButton.click();
        return this;
    }
}
