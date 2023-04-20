package br.com.jayybe.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverUtil {
	
	 private WebDriver driver;

	    public String getHtml(String url) {
	        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	        driver = new ChromeDriver();
	        driver.get(url);
	        aguardarSegundos(20);
	        String html = driver.getPageSource();
	        driver.quit();
	        return html;
	    }

	    private void aguardarSegundos(Integer segundos) {
	        try {
	            Thread.sleep(segundos * 1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

		
}
