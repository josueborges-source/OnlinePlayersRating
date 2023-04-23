package br.com.jayybe.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class HTMLUtils {


	/*
	public static String[] encontrarElementosComIdJqg(String codigoHtml) {
		WebDriver driver = new ChromeDriver();
	    driver.get(codigoHtml);
	    List<WebElement> elementos = driver.findElements(By.cssSelector("*[id^='jqg']"));
	    String[] elementosJQG = new String[elementos.size()];
	    for (int i = 0; i < elementosJQG.length; i++) {
	        elementosJQG[i] = elementos.get(i).getText();
	    }
	    return elementosJQG;
	}
	*/
	
	
	public static String[] encontrarElementosComIdJqg(String codigoHtml) {
		 Document doc = Jsoup.parse(codigoHtml);
		    Pattern pattern = Pattern.compile("jqg\\d+$"); // Expressão regular para IDs que começam com 'jqg' e terminam com um ou mais dígitos
		    List<String> valores = new ArrayList<>();
		    for (Element element : doc.select("[id]")) { // Seleciona todos os elementos que possuem um atributo ID
		        String id = element.id();
		        if (pattern.matcher(id).matches()) { // Verifica se o ID do elemento corresponde à expressão regular
		            valores.add(element.text());
		        }
		    }
		    return valores.toArray(new String[0]);
	}

}
