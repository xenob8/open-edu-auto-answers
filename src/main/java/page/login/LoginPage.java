package page.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;
    private final Locator loginButton;
    private final Locator polyLocator;
    private final Locator loginInput;
    private final Locator passwordInput;
    private final Locator polySumbitButton;
    private final static String BASE_URL = "https://openedu.ru/";


    public LoginPage(Page page) {
        this.page = page;
//        this.loginButton = page.locator("//a[@class='header__button' and text()='Вход']");
        this.loginButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Вход"));
//        this.polyLocator = page.locator("//li[@class='social-form__item spbstu']//a");
        this.polyLocator = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Политех"));

//        this.loginInput = page.locator("//input[@id='user']");
        this.loginInput = page.getByLabel("Логин");
//        this.passwordInput = page.locator("//input[@id='password']");
        this.passwordInput = page.getByLabel("Пароль");
//        this.polySumbitButton = page.locator("//input[@id='doLogin']");
        this.polySumbitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Войти"));
    }

    public void navigate() {
        page.navigate(BASE_URL);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public void clickPoly() {
        polyLocator.click();
    }

    public void fillLogin(String login) {
        loginInput.fill(login);
    }

    public void fillPassword(String password) {
        passwordInput.fill(password);
    }

    public void polySubmit() {
        polySumbitButton.click();
    }
}
