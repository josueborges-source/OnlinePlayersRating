package br.com.jayybe.controller;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverUtil {
	
	 //

	    public String getHtml(String url) {
	        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	        
	        WebDriver driver;
	        driver = new ChromeDriver();
	        driver.get(url);
	        
	        aguardarSegundos(20);
	        
	        List<WebElement> elementosHTML = retornaElementosDeSelecaoDoHTML(driver);
	        alteraValorDoUltimoSelectDaPaginaPara20000(driver, elementosHTML.get(2));
	        clicaNaUltimaOpcaoDeElementoSelect(driver, elementosHTML.get(2));
	        
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
	    
	    private void alteraValorDoUltimoSelectDaPaginaPara20000(WebDriver driver, WebElement selectElement)
		{		
			// Altera o valor do último option para "20000"
			((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
					+ "options[options.length - 1].value = '20000';" + "options[options.length - 1].text = '20000';",
					selectElement);
		}
	    
		private List<WebElement> retornaElementosDeSelecaoDoHTML(WebDriver driver)
		{
			ArrayList<WebElement> elementosSelect = new ArrayList<WebElement>();
			
			elementosSelect = (ArrayList<WebElement>) ((JavascriptExecutor) driver)
			.executeScript("return document.querySelectorAll(\"select[title='Records per Page']\");");
			
			return elementosSelect;
		}
		
		private void clicaNaUltimaOpcaoDeElementoSelect(WebDriver driver, WebElement selectElement) {
			((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
					+ "options[options.length - 1].selected = true;" + "arguments[0].dispatchEvent(new Event('change'));",
					selectElement);		
		}

		
}
