package page.marketing;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import page.BasePage;
import page.marketing.valueobject.CourseItem;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MarketingHomePage extends BasePage {
    private final static String URL = "https://apps.openedu.ru/learning/course/course-v1:spbstu+MARKET+fall_2022/home";
    private final Locator courseItemsLocator;

    public MarketingHomePage(Page page) {
        super(page);
        courseItemsLocator = page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("Неделя"));
    }

    @Override
    public void isLoaded() {
        assertThat(page.getByText("Общая информация")).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(25000));
    }

    public static MarketingHomePage navigate (Page page) {
        page.navigate(URL);
        return new MarketingHomePage(page);
    }

    public List<CourseItem> getCourseItemList() {
        List<CourseItem> courseItemList = new ArrayList<>();
        int count = courseItemsLocator.count();
        for (int i = 0; i < count; ++i) {
            courseItemList.add(new CourseItem(courseItemsLocator.nth(i)));
        }
        return courseItemList;
    }

}
