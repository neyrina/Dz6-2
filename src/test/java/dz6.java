import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;


public class dz6 {
    WebDriver driver;
    private final Logger logger = (Logger) LogManager.getLogger(dz6.class);

    @Before
    public void StartUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        logger.info("Драйвер поднят");
    }

    @Test
    public void mainTest() throws InterruptedException {
        //1. Открыть otus.ru
        driver.get("http://otus.ru");
        logger.info("Отус открылся");
        //2. Авторизоваться на сайте
        auth();
        //3. Войти в личный кабинет
        enterLk();
        //4. В разделе "О себе" заполнить все поля "Личные данные" и добавить не менее двух контактов
        inputFio();//Вводим ФИО и дату рождения
        inputCountry();//Страна
        inputCity();//Город
        inputLanguage();//уровень англ.
        chooseMoveOptions();//готовность к переезду
        chooseWorkingFormat();//Формат работы
        chooseContacts();//Способ связи
        chooseGender();//пол
        //5. Нажать сохранить
        save();
        //6. Открыть https://otus.ru в “чистом браузере”
        quit();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        logger.info("Драйвер поднят");
        driver.get("http://otus.ru");
        //7. Авторизоваться на сайте
        auth();
        //8. Войти в личный кабинет
        enterLk();
        //9. Проверить, что в разделе о себе отображаются указанные ранее данные
        Assert.assertEquals("Елена", driver.findElement(By.id("id_fname")).getAttribute("value"));
        Assert.assertEquals("Кукунина", driver.findElement(By.id("id_lname")).getAttribute("value"));
        Assert.assertEquals("24.09.1989", driver.findElement(By.cssSelector(".input-icon > input:nth-child(1)")).getAttribute("value"));
        Assert.assertEquals("Россия", driver.findElement(By.cssSelector(".js-lk-cv-dependent-master > label:nth-child(1) > div:nth-child(2)")).getText());
        Assert.assertEquals("Москва", driver.findElement(By.cssSelector(".js-lk-cv-dependent-slave-city > label:nth-child(1) > div:nth-child(2)")).getText());
        Assert.assertEquals("Выше среднего (Upper Intermediate)", driver.findElement(By.cssSelector("div[class='select lk-cv-block__input lk-cv-block__input_full js-lk-cv-custom-select'][data-selected-option-class='lk-cv-block__select-option_selected']")).getText());
        Assert.assertTrue("Есть готовность к переезду", driver.findElement(By.xpath("//input[@id='id_ready_to_relocate_0']")).isSelected());
        Assert.assertTrue("Не Удаленно", driver.findElement(By.cssSelector("input[title='Удаленно']")).isSelected());
        Assert.assertEquals("Elena Kukunina", driver.findElement(By.id("id_contact-0-value")).getAttribute("value"));
        Assert.assertEquals("Elena Kukunina", driver.findElement(By.id("id_contact-1-value")).getAttribute("value"));
        Assert.assertEquals("f", driver.findElement(By.id("id_gender")).getAttribute("value"));
    }

    @After
    public void quit(){
        driver.quit();
    }

    private void save() throws InterruptedException {
        driver.findElement(By.xpath("//*[contains(text(), 'Сохранить и продолжить')]")).click();
        Thread.sleep(2000);
        logger.info("Сохранились");
    }

    private void auth() throws RuntimeException  {
        String login = "kukunina_ey@interrao.ru";
        String password = "Aa!23456789";
        String locator = "button.header2__auth.js-open-modal";
        driver.findElement(By.cssSelector(locator)).click();
        driver.findElement(By.cssSelector(".js-email-input")).sendKeys(login);
        driver.findElement(By.cssSelector(".js-psw-input")).sendKeys(password);
        driver.findElement(By.cssSelector("div.new-input-line.new-input-line_last.new-input-line_relative")).click();
        logger.info("Залогинились");
    }

    private void enterLk() throws InterruptedException {
        Thread.sleep(1000);
        driver.get("https://otus.ru/lk/biography/personal/");
        logger.info("Переход в ЛК выполнен");
    }

    private void inputFio() {
        driver.findElement(By.id("id_fname")).clear();
        driver.findElement(By.id("id_lname")).clear();
        driver.findElement(By.id("id_fname_latin")).clear();
        driver.findElement(By.id("id_lname_latin")).clear();
        driver.findElement(By.name("date_of_birth")).clear();

        driver.findElement(By.id("id_fname")).sendKeys("Елена");
        driver.findElement(By.id("id_lname")).sendKeys("Кукунина");
        driver.findElement(By.id("id_fname_latin")).sendKeys("Elena");
        driver.findElement(By.id("id_lname_latin")).sendKeys("Kukunina");
        driver.findElement(By.name("date_of_birth")).sendKeys("24.09.1989");
        logger.info("Введены ФИО и дата рождения");
    }

    private void inputCountry() throws InterruptedException {
        if (!driver.findElement(By.cssSelector(".js-lk-cv-dependent-master.js-lk-cv-custom-select")).getText().contains("Россия")) {
            driver.findElement(By.cssSelector(".js-lk-cv-dependent-master.js-lk-cv-custom-select")).click();
            driver.findElement(By.xpath("//*[contains(text(), 'Россия')]")).click();
        }
        logger.info("Страна выбрана");
        Thread.sleep(1000);
    }

    private void inputCity() throws InterruptedException {
        if (!driver.findElement(By.cssSelector(".js-lk-cv-dependent-slave-city.js-lk-cv-custom-select")).getText().contains("Москва")) {
            driver.findElement(By.cssSelector(".js-lk-cv-dependent-slave-city.js-lk-cv-custom-select")).click();
            driver.findElement(By.xpath("//*[contains(text(), 'Москва')]")).click();
        }
        logger.info("Город выбран");
        Thread.sleep(1000);
    }

    private void inputLanguage() throws InterruptedException {
        if (!driver.findElement(By.cssSelector("div[class='select lk-cv-block__input lk-cv-block__input_full js-lk-cv-custom-select'][data-selected-option-class='lk-cv-block__select-option_selected']")).getText().contains("\n" +
                "        Выше среднего (Upper Intermediate)\n" +
                "    ")) {
            driver.findElement(By.cssSelector("div[class='select lk-cv-block__input lk-cv-block__input_full js-lk-cv-custom-select'][data-selected-option-class='lk-cv-block__select-option_selected']")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[contains(text(), '\n" +
                    "        Выше среднего (Upper Intermediate)\n" +
                    "    ')]")).click();
        }
        logger.info("Выбран уровень английского");
        Thread.sleep(1000);
    }
    private void chooseWorkingFormat() throws InterruptedException {
        if (!driver.findElement(By.cssSelector("input[title='Удаленно']")).isSelected()) {
            driver.findElement(By.cssSelector("input[title='Удаленно']")).click();
        }
        if (driver.findElement(By.cssSelector("input[title='Полный день']")).isSelected()) {
            driver.findElement(By.cssSelector("input[title='Полный день']")).click();
        }
        if (driver.findElement(By.cssSelector("input[title='Гибкий график']")).isSelected()) {
            driver.findElement(By.cssSelector("input[title='Гибкий график']")).click();
        }
        logger.info("Выбрана удаленка");
        Thread.sleep(1000);
    }

    private void chooseGender() throws InterruptedException {
        driver.findElement(By.id("id_gender")).click();
        driver.findElement(By.cssSelector("option[value='f']")).click();
        logger.info("Добавлен пол");
        Thread.sleep(1000);
    }

    private void chooseContacts() throws InterruptedException {
        driver.findElement(By.xpath("//span[@class='placeholder']")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button[title='Тelegram']")).click();
        driver.findElement(By.id("id_contact-0-value")).sendKeys("Elena Kukunina");
        logger.info("Добавлен 1 контакт");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("button[class='lk-cv-block__action lk-cv-block__action_md-no-spacing js-formset-add js-lk-cv-custom-select-add']")).click();
        driver.findElement(By.xpath("//span[@class='placeholder']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[@class='lk-cv-block__select-options lk-cv-block__select-options_left js-custom-select-options-container']//button[last()]")).click();
        driver.findElement(By.id("id_contact-1-value")).sendKeys("Elena Kukunina");
        logger.info("Добавлено 2 контакта");
    }

    private void chooseMoveOptions() throws InterruptedException {
        driver.findElement(By.xpath("//input[@id='id_ready_to_relocate_0']"));
        //driver.findElement(By.cssSelector("label[class='radio radio_light4 radio_size-sm radio_vertical-center lk-cv-block__radio']")).click();
        logger.info("Нет готовности к переезду");
    }

}
