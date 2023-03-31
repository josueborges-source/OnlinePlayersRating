package br.com.jayybe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.jayybe.model.DadosTorneioERede;
import br.com.jayybe.model.EntradaPremioRecompensa;
import br.com.jayybe.util.Configuracoes;
import br.com.jayybe.util.Util;
import br.com.jayybe.view.TelaPrincipal;

public class ControleArquivoExcel {

	File arquivoExcelTorneios;

	public ControleArquivoExcel(File arquivoExcelTorneios) {
		this.arquivoExcelTorneios = arquivoExcelTorneios;
	}

	public ArrayList<DadosTorneioERede> transformarArquivoXLSEmObjetosDadosETorneio() {

		ArrayList<DadosTorneioERede> dadosTorneioERedeDeRetorno = new ArrayList<DadosTorneioERede>();

		if (arquivoExcelTorneios != null) {
			ArrayList<DadosTorneioERede> dadosTorneioERede = new Util()
					.instanciarTorneioERedeAPartirDeArquivoExcelLocal(arquivoExcelTorneios);

			int quantidadedadosTorneioERede = dadosTorneioERede.size();

			System.out.println("Quantidade Dados E Torneio e Rede no Arquivo: " + quantidadedadosTorneioERede);

			for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
				DadosTorneioERede dadoTorneioERedeResposta = InserirDadosDePremioERecompensaEmObjetoDadosTorneioERede(
						dadoTorneioERede);
				dadosTorneioERedeDeRetorno.add(dadoTorneioERedeResposta);
			}
		}
		return dadosTorneioERedeDeRetorno;
	}

	private String retornarURLDaPaginaAPartirDeObjetoDadosTorneioERede(DadosTorneioERede dadosTorneioERede) {
		String urlDaPagina = "https://pt.sharkscope.com/#Find-Tournament//networks/" + dadosTorneioERede.getRede()
				+ "/tournaments/" + dadosTorneioERede.getTorneio();

		return urlDaPagina;
	}

	private DadosTorneioERede InserirDadosDePremioERecompensaEmObjetoDadosTorneioERede(
			DadosTorneioERede dadoTorneioERede) {
		
		//Configura Chrome Driver
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");		
		
		//Retorna a URL a Partir da lista que possue valores dados do torneio
		String url = retornarURLDaPaginaAPartirDeObjetoDadosTorneioERede(dadoTorneioERede);
		
		//Insere Página Selecionada em TextArea
		Util.InserirValorEmJTextPaneComMarcacaoDeTempo("Página Selecionada a partir da lista: " + url);			

		Util.InserirValorEmJTextPaneComMarcacaoDeTempo(url);		
		
		Util.InserirValorEmJTextPaneComMarcacaoDeTempo("Abrindo Browser na URL: " + url);
				
		// Configura o caminho do driver do Chrome
		

		// Instancia o driver do Chrome
		WebDriver driver = new ChromeDriver();

		// Navega para o site do Google
		driver.get(url);

		try {								
			Util.InserirValorEmJTextPaneComMarcacaoDeTempo("Aguardando " + Configuracoes.tempoDeCarregamentoDaPagina + " segundos para carregamento da página");
			
			Thread.sleep(Configuracoes.tempoDeCarregamentoDaPagina);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		TelaPrincipal.frame.revalidate();
		TelaPrincipal.frame.repaint();

		List<WebElement> selectElements = (List<WebElement>) ((JavascriptExecutor) driver)
				.executeScript("return document.querySelectorAll(\"select[title='Records per Page']\");");

		// textAreaLogAndamentoProcesso.append("Ampliando tamanho de pesquisa da itens
		// da página para limite de " + Config.quantidadeDeItensDaPagina);

		WebElement selectElement = selectElements.get(2);

		// Altera o valor do último option para "20000"
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].value = '20000';" + "options[options.length - 1].text = '20000';",
				selectElement);

		// Clica no último option
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].selected = true;" + "arguments[0].dispatchEvent(new Event('change'));",
				selectElement);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		//// ID
		List<WebElement> linhasTabela = driver.findElements(By.cssSelector("[id^='jqg']"));
		System.out.println("linhasTabela: " + linhasTabela.size());

		for (WebElement webElement : linhasTabela) {
		    WebElement tdNome = webElement.findElement(By.xpath(".//td[4]//a[@class='playerlink']"));
		    System.out.println("Elemento encontrado: " + tdNome);
		    String nomeJogador = tdNome.getText();
		    System.out.println("Nome do jogador: " + nomeJogador);
		}
		
		///Refatorado		
		/*
		List<WebElement> linhasTabela = driver.findElements(By.cssSelector("[id^='jqg']"));

		for (WebElement linha : linhasTabela) {
		    // buscar o texto da quarta TD, que contém o nome do jogador
		    WebElement tdNome = linha.findElement(By.xpath(".//td[4]"));
		    String nomeJogador = tdNome.getText();

		    // buscar o texto do quinto TD, que contém o valor do prêmio e da recompensa
		    WebElement tdPremioRecompensa = linha.findElement(By.xpath(".//td[5]"));
		    String textoPremioRecompensa = tdPremioRecompensa.getText();

		    EntradaPremioRecompensa entradaPremioRecompensa = new EntradaPremioRecompensa();
		    entradaPremioRecompensa.setJogador(nomeJogador);

		    // extrair o valor do prêmio e da recompensa do texto usando as mesmas regras que você já estava usando
		    if (textoPremioRecompensa.contains("(Recompensas:")) {
		        String premio = textoPremioRecompensa.substring(1, textoPremioRecompensa.indexOf("(Recompensas:"))
		                .replace(",", "").trim();
		        String recompensa = textoPremioRecompensa.substring(textoPremioRecompensa.indexOf("(Recompensas:") + "(Recompensas:".length() + 2,
		                textoPremioRecompensa.length() - 1).replace(",", "").trim();

		        entradaPremioRecompensa.setPremio(Integer.parseInt(premio));
		        entradaPremioRecompensa.setRecompensa(Integer.parseInt(recompensa));
		    } else if (textoPremioRecompensa.length() > 0) {
		        String premio = textoPremioRecompensa.substring(1).replace(",", "").trim();
		        entradaPremioRecompensa.setPremio(Integer.parseInt(premio));
		    }

		    Util.imprimirEntradaPremioERecompensaCasoConfigurado(entradaPremioRecompensa);

		    dadoTorneioERede.adicionaEntradaPremioRecompensa(entradaPremioRecompensa);
		}
		*/
		/*
		List<WebElement> elementos = driver.findElements(By.xpath(
				"//*[@title[contains(., '$') and contains(translate(substring-after(., '$'), '0123456789', '0000000000'), '.')]]"));

		// Percorrer a lista de elementos e imprimir seu texto
		for (WebElement elemento : elementos) {

			String texto = elemento.getText();

			EntradaPremioRecompensa entradaPremioRecompensa = new EntradaPremioRecompensa();

			if (texto.contains("(Recompensas:")) {

				String premio = texto.substring(1, texto.indexOf("(Recompensas:"));

				premio = premio.replace(",", "");

				if (premio.contains(".")) {
					int posicaoPonto = premio.indexOf('.');
					premio = premio.substring(0, posicaoPonto);
					premio = premio.replaceAll("\\s", "");
					premio = premio.trim();
				}

				String recompensa = texto.substring(texto.indexOf("(Recompensas:") + "(Recompensas:".length() + 2,
						texto.length() - 1);

				recompensa = recompensa.replace(",", "");

				if (recompensa.contains(".")) {
					int posicaoPonto = recompensa.indexOf('.');
					recompensa = recompensa.substring(0, posicaoPonto);
					recompensa = recompensa.trim();
				}

				entradaPremioRecompensa.setPremio(Integer.parseInt(premio.trim()));
				entradaPremioRecompensa.setRecompensa(Integer.parseInt(recompensa.trim()));

				Util.imprimirEntradaPremioERecompensaCasoConfigurado(entradaPremioRecompensa);

			}

			else if (texto.length() > 0) {

				String premio = texto.substring(1);

				premio = premio.replace(",", "");

				if (premio.contains(".")) {
					int posicaoPonto = premio.indexOf('.');
					premio = premio.substring(0, posicaoPonto);
					premio = premio.trim();
					entradaPremioRecompensa.setPremio(Integer.parseInt(premio.trim()));
				}

				Util.imprimirEntradaPremioERecompensaCasoConfigurado(entradaPremioRecompensa);
			}

			dadoTorneioERede.adicionaEntradaPremioRecompensa(entradaPremioRecompensa);
			}		
			*/	

		driver.quit();
		return dadoTorneioERede;
	}

	public void exportarListaDadosTorneioERedeParaArquivoXLS(ArrayList<DadosTorneioERede> dadosTorneioERede) {
		try {
			FileInputStream file = new FileInputStream(arquivoExcelTorneios);
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rownum = 1; // começa da segunda linha, já que a primeira linha é o cabeçalho
			for (DadosTorneioERede dados : dadosTorneioERede) {
				int valorTotalPremioComRecompensa = 0;
				int valorTotalPremioSemRecompensa = 0;
				int valorTotalRecompensa = 0;

				for (EntradaPremioRecompensa entrada : dados.getListaDePremiosDaPagina()) {
					valorTotalPremioComRecompensa += entrada.getPremio();
					valorTotalPremioSemRecompensa += entrada.getPremioSemRecompensa();
					valorTotalRecompensa += entrada.getRecompensa();
				}

				XSSFRow row = sheet.createRow(rownum);

				row.createCell(0).setCellValue(dados.getTorneio());
				row.createCell(1).setCellValue(dados.getRede());
				row.createCell(2).setCellValue(valorTotalPremioComRecompensa);
				row.createCell(3).setCellValue(valorTotalRecompensa);
				row.createCell(4).setCellValue(valorTotalPremioSemRecompensa);

				rownum++;
			}

			FileOutputStream out = new FileOutputStream(arquivoExcelTorneios);
			workbook.write(out);
			workbook.close();
			out.close();
			System.out.println("Planilha exportada com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void
	 * exportarListaDadosTorneioERedeParaArquivoXLS(ArrayList<DadosTorneioERede>
	 * dadosTorneioERede) { try { FileInputStream file = new
	 * FileInputStream(arquivoExcelTorneios); XSSFWorkbook workbook = new
	 * XSSFWorkbook(file); XSSFSheet sheet = workbook.getSheetAt(0);
	 * 
	 * int rownum = 1; // começa da segunda linha, já que a primeira linha é o
	 * cabeçalho for (DadosTorneioERede dados : dadosTorneioERede) { int
	 * valorTotalPremioComRecompensa = 0; int valorTotalPremioSemRecompensa = 0; int
	 * valorTotalRecompensa = 0;
	 * 
	 * for (EntradaPremioRecompensa entrada : dados.getListaDePremiosDaPagina()) {
	 * 
	 * valorTotalPremioComRecompensa += entrada.getPremio();
	 * valorTotalPremioSemRecompensa += entrada.getPremioSemRecompensa();
	 * valorTotalRecompensa += entrada.getRecompensa();
	 * 
	 * rownum++; } XSSFRow row = sheet.getRow(rownum); if (row == null) { row =
	 * sheet.createRow(rownum); } System.out.println("Valor Premio Com Recompensa: "
	 * +valorTotalPremioComRecompensa);
	 * System.out.println("Valor Total Premio Sem Recompensa: "
	 * +valorTotalPremioSemRecompensa);
	 * System.out.println("Valor Total Apenas das Recompensa: "+valorTotalRecompensa
	 * );
	 * 
	 * 
	 * row.createCell(0).setCellValue(dados.getTorneio());
	 * row.createCell(1).setCellValue(dados.getRede());
	 * row.createCell(2).setCellValue(valorTotalPremioComRecompensa);
	 * row.createCell(3).setCellValue(valorTotalRecompensa);
	 * row.createCell(4).setCellValue(valorTotalPremioSemRecompensa); }
	 * 
	 * FileOutputStream out = new FileOutputStream(arquivoExcelTorneios);
	 * workbook.write(out); workbook.close(); out.close();
	 * System.out.println("Planilha exportada com sucesso!"); } catch (IOException
	 * e) { e.printStackTrace(); } }
	 */

}
