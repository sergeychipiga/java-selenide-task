package org.selenium.task;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;
import org.selenium.task.support.Utils;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by schipiga on 07.02.17.
 */
public class ResultPage extends BasePage {

    @Step
    public void sortBy(String val) {
        $(By.linkText(val)).click();
    }

    @Step
    public void changeDealTypeTo(String val) {
        $(By.xpath("//span[text()='Тип сделки:']//select")).selectOptionContainingText(val);
    }

    @Step
    public SearchPage goToExpandedSearch() {
        $(By.linkText("Расширенный поиск")).click();
        return page(SearchPage.class);
    }

    @Step
    public List<String> selectRandomAdverts(int count) {
        ElementsCollection advertLinks = $$("tr div.d1 > a");
        ElementsCollection advertCheckboxes = $$("tr > td > input[type='checkbox']");

        List<String> selectedAdverts = new ArrayList<>();
        int[] random = Utils.randomArray(0, advertLinks.size(), count);

        for (int i = 0; i < count; i++) {
            advertCheckboxes.get(random[i]).click();
            selectedAdverts.add(
                advertLinks.get(random[i]).getText().substring(0, TITLE_LIMIT));
        }

        return selectedAdverts;
    }
}
