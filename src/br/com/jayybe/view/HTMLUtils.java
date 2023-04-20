package br.com.jayybe.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
		
		Elements elementos = doc.select("[id^='jqg']");
		String[] resultado = new String[elementos.size()];
		
		 int i = 0;
		    for (Element element : elementos) {
		    	resultado[i] = element.text();
		        i++;
		    }
		
		return resultado;
	}

}
