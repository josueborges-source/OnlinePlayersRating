package br.com.jayybe.view;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Principal {

		public static void main(String[] args) {
	        
	        // Configura o caminho do driver do Chrome
	        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	        
	        // Instancia o driver do Chrome
	        WebDriver driver = new ChromeDriver();
	        
	        // Navega para o site do Google
	        driver.get("https://www.google.com");
	        
	        // Fecha o navegador
	        driver.quit();

	}

}
