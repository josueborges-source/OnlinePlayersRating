package br.com.jayybe.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverUtil {

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

	public static String[] extrairDadosDaPaginaComIndiceDeAmostra(String inputString, int indiceDeAmostra) 
	{		
		System.out.println(inputString);
		String[] dadosDaPagina = extrairInformacao(inputString);

		int tamanhoDoArrayDeSaida = dadosDaPagina.length / indiceDeAmostra;
		String[] dadosDaPaginaAPartirDeIndiceDeAmostra = new String[tamanhoDoArrayDeSaida];

		int i = 0;
		for (int contagemDadosDaPagina = 0; contagemDadosDaPagina < dadosDaPagina.length; contagemDadosDaPagina++) {
			
			int numeroDaEntrada = Integer.parseInt(inputString.substring(0, inputString.indexOf(" ")).replace(",", ""));

			System.out.println("numeroDaEntrada: " + numeroDaEntrada);
			System.out.println("indiceDeAmostra: " + indiceDeAmostra);
			
			if (numeroDaEntrada % indiceDeAmostra == 0) {
				dadosDaPaginaAPartirDeIndiceDeAmostra[i] = dadosDaPagina[contagemDadosDaPagina];
				System.out.println(dadosDaPagina[contagemDadosDaPagina]);
				i++;
			}
		}

		return dadosDaPaginaAPartirDeIndiceDeAmostra;
	}

	public static String[] extrairInformacao(String inputString) {

		/*
		 * int numeroDaEntrada = Integer.parseInt(inputString.substring(0,
		 * inputString.indexOf(" ")).replace(",", ""));
		 * 
		 * if (numeroDaEntrada % 60 == 0) { System.out.println(inputString); }
		 */

		// System.out.println(numeroDaEntrada);

		String[] informacaoExtraida = new String[3];

		Pattern namePattern = Pattern.compile("^\\d+\\s+([a-zA-Z0-9_]+).*");
		Matcher nameMatcher = namePattern.matcher(inputString);

		if (nameMatcher.matches()) {
			informacaoExtraida[0] = nameMatcher.group(1);
		} else {
			informacaoExtraida[0] = "";
		}

		Pattern valuePattern = Pattern.compile(".*PokerStars\\s*\\$(\\d+(?:\\.\\d{1,2})?)");
		Matcher valueMatcher = valuePattern.matcher(inputString);

		if (valueMatcher.find()) {
			informacaoExtraida[1] = valueMatcher.group(1);
		} else {
			informacaoExtraida[1] = "";
		}

		Pattern rewardPattern = Pattern.compile("Recompensas:\\s*\\$([\\d,\\.]+)");
		Matcher rewardMatcher = rewardPattern.matcher(inputString);

		if (rewardMatcher.find()) {
			informacaoExtraida[2] = rewardMatcher.group(1);
		} else {
			informacaoExtraida[2] = "";
		}		

		return informacaoExtraida;
	}

	private void alteraValorDoUltimoSelectDaPaginaPara20000(WebDriver driver, WebElement selectElement) {
		// Altera o valor do último option para "20000"
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].value = '20000';" + "options[options.length - 1].text = '20000';",
				selectElement);
	}

	private List<WebElement> retornaElementosDeSelecaoDoHTML(WebDriver driver) {
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
