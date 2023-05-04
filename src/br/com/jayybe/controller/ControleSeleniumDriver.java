package br.com.jayybe.controller;

import java.util.List;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ControleSeleniumDriver {
	
	
	private WebDriver driver = new ChromeDriver();
	
	ControleSeleniumDriver(WebDriver driver){
		this.driver = driver;
	}
	
	public List<WebElement> retornaElementosSelectDaPagina()
	{
		ArrayList<WebElement> elementosSelect = new ArrayList<WebElement>();
		
		elementosSelect = (ArrayList<WebElement>) ((JavascriptExecutor) driver)
		.executeScript("return document.querySelectorAll(\"select[title='Records per Page']\");");
		
		return elementosSelect;
	}
	
	public void alteraElementoSelectPara20000(WebElement selectElement)
	{		
		// Altera o valor do último option para "20000"
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].value = '20000';" + "options[options.length - 1].text = '20000';",
				selectElement);
	}
	

	public void clicaNaUltimaOpcaoDeElementoSelect(WebElement selectElement) {
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].selected = true;" + "arguments[0].dispatchEvent(new Event('change'));",
				selectElement);		
	}	
	
	public List<WebElement> retornaElementosIdJqg() {
		List<WebElement> linhasTabela = driver.findElements(By.cssSelector("[id^='jqg']"));
		
		System.out.println("Elementos jg(Jogador):: " + linhasTabela.size());
		
		return linhasTabela;
	}

	public void imprimaValoresDasLinhasDaTabela(List<WebElement> linhasTabela) {
		for (WebElement webElement : linhasTabela) {
		    WebElement tdNome = webElement.findElement(By.xpath(".//td[4]//a[@class='playerlink']"));
		    System.out.println("Elemento encontrado: " + tdNome);
		    String nomeJogador = tdNome.getText();
		    System.out.println("Nome do jogador: " + nomeJogador);
		}		
	}	
	
}
