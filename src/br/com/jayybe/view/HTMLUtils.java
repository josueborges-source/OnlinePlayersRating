package br.com.jayybe.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.jayybe.controller.WebDriverUtil;

public class HTMLUtils {

	/*
	 * public static String[] encontrarElementosComIdJqg(String codigoHtml) {
	 * WebDriver driver = new ChromeDriver(); driver.get(codigoHtml);
	 * List<WebElement> elementos =
	 * driver.findElements(By.cssSelector("*[id^='jqg']")); String[] elementosJQG =
	 * new String[elementos.size()]; for (int i = 0; i < elementosJQG.length; i++) {
	 * elementosJQG[i] = elementos.get(i).getText(); } return elementosJQG; }
	 */

	public static String[] encontrarElementosComIdJqg(String codigoHtml) {
		Document doc = Jsoup.parse(codigoHtml);
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
			/*
			System.out.println("Linha do Texto da Tabela: " + valoresDaTabelaElements[i]);
			System.out.println("Nome do Usuário: " + valorStringArray[0]);
			System.out.println("Prêmio: " + valorStringArray[1]);
			System.out.println("Recompensa: " + valorStringArray[2]);
			*/
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

}
