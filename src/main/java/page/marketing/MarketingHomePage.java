package page.marketing;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import page.marketing.valueobject.CourseItem;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MarketingHomePage {
    private final Page page;
    private final static String URL = "https://apps.openedu.ru/learning/course/course-v1:spbstu+MARKET+fall_2022/home";
    private final Locator courseItemsLocator;
    private final List<CourseItem> courseItemList = new ArrayList<>();

    public MarketingHomePage(Page page){
        this.page = page;
        navigate();
        assertThat(page.getByText("Общая информация")).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15000));
        courseItemsLocator = page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("Неделя"));
        int count = courseItemsLocator.count();
        for (int i = 0; i < count; ++i)
             courseItemList.add(new CourseItem(courseItemsLocator.nth(i)));
    }

    public void navigate(){
        page.navigate(URL);
    }

    public List<CourseItem> getCourseItemList() {
        return courseItemList;
    }
}
