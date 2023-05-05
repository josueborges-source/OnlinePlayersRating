package br.com.jayybe.controller;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import br.com.jayybe.view.TelaPrincipal;
import br.com.jayybe.view.TelaPrincipal.Seletor;

public class HTMLWebDriverUtil {

	public static String[] encontrarElementosComIdJqg(String codigoHtml) {
		Document doc = Jsoup.parse(codigoHtml);
		
		TelaPrincipal.atualizarStatusLabel("Procurando Elementos de Jogo", TelaPrincipal.Seletor.TRANSFORMACAO_EM_MAISCULO);
		
		Pattern pattern = Pattern.compile("jqg\\d+$"); // Expressão regular para IDs que começam com 'jqg' e terminam
														// com um ou mais dígitos
		List<String> valores = new ArrayList<>();
		for (Element element : doc.select("[id]")) { // Seleciona todos os elementos que possuem um atributo ID
			String id = element.id();
			if (pattern.matcher(id).matches()) { // Verifica se o ID do elemento corresponde à expressão regular
				valores.add(element.text());
			}
		}
		return valores.toArray(new String[0]);
	}	

	public static String[][] retornaValorPremioRecompensa(String[] valoresDaTabelaElements) {

		String[][] valorPremioRecompensa = new String[valoresDaTabelaElements.length][];

		for (int i = 0; i < valoresDaTabelaElements.length; i++) {
			String[] valorStringArray = extrairInformacao(valoresDaTabelaElements[i]);
			valorPremioRecompensa[i] = valorStringArray;		
		}
		return valorPremioRecompensa;
	}
	
	public static String[] extrairInformacao(String inputString) {

		String[] informacaoExtraida = new String[3];

		Pattern namePattern = Pattern.compile("^\\d+\\s+(.+?)\\s+USD.*");
		Matcher nameMatcher = namePattern.matcher(inputString);

		if (nameMatcher.matches()) {
			informacaoExtraida[0] = nameMatcher.group(1);
		} else {
			informacaoExtraida[0] = "";
		}

		Pattern valuePattern = Pattern.compile(".*PokerStars\\s*\\$([\\d,]+(?:\\.\\d{1,2})?)");
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
	
		
	
	
	public String getHtml(String url) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

		TelaPrincipal.atualizarStatusLabel("Abrindo página", Seletor.TRES_PONTOS);		
		
		
		WebDriver driver;
		driver = new ChromeDriver();				
		
		
		// Obtém a largura e altura do monitor
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int larguraDaTela = screenSize.width;
		int alturaDaTela = screenSize.height;

		// Obtém a largura e altura da janela
		Dimension windowSize = driver.manage().window().getSize();
		int windowWidth = windowSize.width;
		// Define a largura e altura da janela
		int novaLargura = larguraDaTela - windowWidth;
		int novaAltura = alturaDaTela;
		Dimension janelaTamanho = new Dimension(novaLargura, novaAltura);

		// Define a posição da janela
		Point posicao = new Point(larguraDaTela - novaLargura, 0);

		// Redimensiona e reposiciona a janela
		driver.manage().window().setSize(janelaTamanho);
		driver.manage().window().setPosition(posicao);
		
		driver.get(url);
		
		TelaPrincipal.getFrame().setAlwaysOnTop(true);
         
		TelaPrincipal.atualizarStatusLabel("Abrindo página - Esperando 20 segundos para carregamento completo", Seletor.DINAMICO);		
		removerElementosDesnecessarios(driver);
		
		aguardarSegundos(20);

		List<WebElement> elementosHTML = retornaElementosDeSelecaoDoHTML(driver);
		alteraValorDoUltimoSelectDaPaginaPara20000(driver, elementosHTML.get(2));
		clicaNaUltimaOpcaoDeElementoSelect(driver, elementosHTML.get(2));

		TelaPrincipal.atualizarStatusLabel("Abrindo página - Expandindo Prêmios e Recompensas ao limite", Seletor.DINAMICO);		
		aguardarSegundos(20);
		
		
		String html = driver.getPageSource();

		driver.quit();
		
		return html;
	}

	private void removerElementosDesnecessarios(WebDriver driver) {
		
		
		WebElement elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[1]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);		
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[4]/div[3]/div/div[2]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[4]/div[1]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);			
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[1]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[4]/div[2]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[2]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[4]/div[1]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove = driver.findElement(By.xpath("//*[@id=\"Find-Tournament-SearchBox\"]"));		
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);	
		elementToRemove  = driver.findElement(By.id("Find-Tournament-SearchBox"));
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);
		elementToRemove  = driver.findElement(By.className("graphic"));
		((JavascriptExecutor) driver).executeScript("arguments[0].remove();", elementToRemove);

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
