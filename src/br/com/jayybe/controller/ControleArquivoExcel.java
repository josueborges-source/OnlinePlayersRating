package br.com.jayybe.controller;


import java.io.*;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.jayybe.model.*;
import br.com.jayybe.util.*;
import br.com.jayybe.view.*;

public class ControleArquivoExcel {

	private ArrayList<DadosTorneioERede> dadosTorneioERede;	
	private File arquivoExcelTorneios;

	public ControleArquivoExcel() {
		
	}
	
	public ControleArquivoExcel(File arquivoExcelTorneios) {
		this.arquivoExcelTorneios = arquivoExcelTorneios;
	}
	
	public void retornarArquivoExcelParaModeloTorneio()
	{
		System.out.println("Botão pressionado");
		arquivoExcelTorneios = new JanelaDeSelecaoDeArquivoLocal().retornarArquivoExcelComTabelaDados();
		
		dadosTorneioERede = transformarArquivoXLSEmObjetosDadosETorneio();
		dadosTorneioERede = inserePremioERecompensaEmDadosTorneioERede(dadosTorneioERede);
		
		System.out.println("Dados Torneio e Rede: " + dadosTorneioERede.size());
	}
	

	private ArrayList<DadosTorneioERede> transformarArquivoXLSEmObjetosDadosETorneio() {

		ArrayList<DadosTorneioERede> dadosTorneioERede = new ArrayList<DadosTorneioERede>();

		if (arquivoExcelTorneios != null) {
			dadosTorneioERede = instanciarTorneioERedeAPartirDeArquivoExcelLocal(arquivoExcelTorneios);
		}

		return dadosTorneioERede;
	}
	
	
	private ArrayList<DadosTorneioERede> inserePremioERecompensaEmDadosTorneioERede(ArrayList<DadosTorneioERede> dadosTorneioERede){
		

		ArrayList<DadosTorneioERede> dadosTorneioERedeDeRetorno = new ArrayList<DadosTorneioERede>();
		
		for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
			DadosTorneioERede dadoTorneioERedeResposta = InserirDadosDePremioERecompensaEmObjetoDadosTorneioERede(
					dadoTorneioERede);
			dadosTorneioERedeDeRetorno.add(dadoTorneioERedeResposta);
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
		Log.acaoParaLog("Página Selecionada a partir da lista: " + url);
		Log.acaoParaLog(url);		
		Log.acaoParaLog("Abrindo Browser na URL: " + url);
						
		// Instancia o driver do Chrome
		WebDriver driver = new ChromeDriver();

		// Navega para o site do Google
		driver.get(url);

		String mensagemDeAguardo = 
					String.format("Aguardando %s segundos para carregamento da página", Configuracoes.tempoDeCarregamentoDaPagina);			
		Log.acaoParaLog(mensagemDeAguardo);
			
		aguardeSegundos(Configuracoes.tempoDeCarregamentoDaPagina);	

		TelaPrincipal.frame.revalidate();
		TelaPrincipal.frame.repaint();

		ControleSeleniumDriver controleSeleniumDriver = new ControleSeleniumDriver(driver);		
		
		
		List<WebElement> selectElements = controleSeleniumDriver.retornaElementosSelectDaPagina();
	
		WebElement selectElement = selectElements.get(2);
		
		controleSeleniumDriver.alteraElementoSelectPara20000(selectElement);		
		controleSeleniumDriver.clicaNaUltimaOpcaoDeElementoSelect(selectElement);

		aguardeSegundos(1000);	
		
		//// ID
		List<WebElement> linhasTabela = controleSeleniumDriver.retornaElementosIdJqg();
		
		controleSeleniumDriver.imprimaValoresDasLinhasDaTabela(linhasTabela);	
		
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
	
	public ArrayList<DadosTorneioERede> instanciarTorneioERedeAPartirDeArquivoExcelLocal(File arquivo) {

		ArrayList<DadosTorneioERede> dados = new ArrayList<>();

		try (FileInputStream file = new FileInputStream(arquivo)) {
			// Abre o arquivo Excel
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Seleciona a primeira planilha
			Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
			int linha = 0;

			// Itera sobre as linhas da planilha a partir da linha 2
			while (rowIterator.hasNext()) {
				
				Row row = rowIterator.next();

				// Pula a primeira linha (cabeçalho)
				if (row.getRowNum() < 1) {
					continue;
				}				

				// Verifica se as colunas A e B estão vazias
				Cell cellA = row.getCell(0);
				Cell cellB = row.getCell(1);

				System.out.println("Linha: " + ++linha);
				System.out.println("Import Célula A: " + cellA);
				System.out.println("Import Célula B: " + cellB);

				if (cellA == null || cellA.getCellType() == CellType.BLANK || cellB == null
						|| cellB.getCellType() == CellType.BLANK) {
					break;
				}

				// Captura os valores das colunas A e B
				Long torneio = (long) row.getCell(0).getNumericCellValue();
				String rede = row.getCell(1).getStringCellValue();

				// Cria um objeto DadosTorneioERede e atribui os valores capturados
				DadosTorneioERede dadosTorneioERede = new DadosTorneioERede();
				dadosTorneioERede.setTorneio(torneio);
				dadosTorneioERede.setRede(rede);

				// Adiciona o objeto à lista de dados
				dados.add(dadosTorneioERede);
			}

			// Fecha o arquivo Excel
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dados;
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
	
	void aguardeSegundos(Integer segundos) {
		try {
			Thread.sleep(segundos);
		} catch (InterruptedException e) {
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
