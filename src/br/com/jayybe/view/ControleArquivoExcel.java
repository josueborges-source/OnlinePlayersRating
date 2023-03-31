package br.com.jayybe.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ControleArquivoExcel {

	File arquivoExcelTorneios;

	ControleArquivoExcel(File arquivoExcelTorneios) {
		this.arquivoExcelTorneios = arquivoExcelTorneios;
	}

	public ArrayList<DadosTorneioERede> acaoImportarArquivoExcel() {

		ArrayList<DadosTorneioERede> dadosTorneioERedeDeRetorno = new ArrayList<DadosTorneioERede>();

		if (arquivoExcelTorneios != null) {
			ArrayList<DadosTorneioERede> dadosTorneioERede = new Util()
					.instanciaTorneioERedeAPartirDeArquivoExcelLocal(arquivoExcelTorneios);

			// Para Cada Dado Torneio e Rede Popular Premio e Recompensa
			for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
				DadosTorneioERede dadoTorneioERedeResposta = popularPremioERecompensaEmDadosTorneioERede(
						dadoTorneioERede);

				dadosTorneioERedeDeRetorno.add(dadoTorneioERedeResposta);
			}
		}
		return dadosTorneioERedeDeRetorno;
	}

	private String retornarURLAPartirDeArquivoDadosDoTorneio(DadosTorneioERede dadosTorneioERede) {
		String urlDaPagina = "https://pt.sharkscope.com/#Find-Tournament//networks/" + dadosTorneioERede.getRede()
				+ "/tournaments/" + dadosTorneioERede.getTorneio();

		return urlDaPagina;
	}

	private DadosTorneioERede popularPremioERecompensaEmDadosTorneioERede(DadosTorneioERede dadoTorneioERede) {
				
		String urlDaListaDeDadosDoTorneio = retornarURLAPartirDeArquivoDadosDoTorneio(dadoTorneioERede);		
						
		WebDriver driverChromePagina = configuraPaginaDeElementosPremioERecompensaDaPagina(urlDaListaDeDadosDoTorneio);
		
		List<WebElement> elementosPremioERecompensa = retornaElementosPremioERecompensaDaPagina(driverChromePagina);
		
		// Imprime Lista de Premios e Recompensas
		for (WebElement premioERecompensaElemento : elementosPremioERecompensa) {

			String premioERecompensa = premioERecompensaElemento.getText();

			EntradaPremioRecompensa entradaPremioRecompensa = new EntradaPremioRecompensa();

			if (premioeRecompensaContemParteRecompensas(premioERecompensa)) {

				String premio = retornaPremioDaStringPremioERecompensa(premioERecompensa);
				
				Integer premioInteiro = formataPremioParaFormatoInteiro(premio);
								
				String recompensa = retornaRecompensaDaStringPremioERecompensa(premioERecompensa);
				
				Integer recompensaInteiro = formataRecompensaParaFormatoInteiro(recompensa);				
							
				entradaPremioRecompensa.setPremio(premioInteiro);
				entradaPremioRecompensa.setRecompensa(recompensaInteiro);

				System.out.println("Prêmio Inteiro: " + premioInteiro);
				System.out.println("Recompensa Inteiro: " + recompensaInteiro);
			}
			else if (textoMaiorQueZero(premioERecompensa)) {

				String premio = retornaPremioDaStringPremioERecompensa(premioERecompensa);
				
				Integer premioInteiro = formataPremioParaFormatoInteiro(premio);				
				
				entradaPremioRecompensa.setPremio(premioInteiro);
				
				System.out.println("Prêmio Inteiro: " + premioInteiro);
			}
			dadoTorneioERede.adicionaEntradaPremioRecompensa(entradaPremioRecompensa);
		}

		driverChromePagina.quit();
		return dadoTorneioERede;
	}

	private WebDriver configuraPaginaDeElementosPremioERecompensaDaPagina(String urlDaListaDeDadosDoTorneio) {
		
		// Configura o caminho do driver do Chrome
				System.setProperty("webdriver.chrome.driver", "chromedriver.exe");		
				
		// Instancia o driver do Chrome
				WebDriver driverChromePagina = new ChromeDriver();	

				// Navega para o site da url do torneio
				driverChromePagina.get(urlDaListaDeDadosDoTorneio);

				aguardarEmSegundos(20);

				List<WebElement> elementosSeletorEntradaPorPagina = retornaElementosRecordPerPage(driverChromePagina);
				
				WebElement ultimoSelectEntradasPorPagina = ultimoElementosSeletorEntradaPorPagina(elementosSeletorEntradaPorPagina);
				
				alteraUltimoValorDoSeletorDeEntradaPorPaginaPara20000(driverChromePagina, ultimoSelectEntradasPorPagina);

				clicaNaUltimaOpcaoDoSeletorDeEntradaPorPagina(driverChromePagina, ultimoSelectEntradasPorPagina);	

				aguardarEmSegundos(10);
				return driverChromePagina;
	}

	private Integer formataRecompensaParaFormatoInteiro(String recompensa) {
		
		recompensa = removeVirgulasDoValor(recompensa);		
		recompensa = removeCentavosDoValor(recompensa);
		Integer recompensaInteiro = valorSemEspacos(recompensa);
		
		return recompensaInteiro;
	}

	private Integer formataPremioParaFormatoInteiro(String premio) {
		premio = removeVirgulasDoValor(premio);				
		premio = removeCentavosDoValor(premio);
		Integer premioInteiro = valorSemEspacos(premio);
		return premioInteiro;
	}

	private boolean textoMaiorQueZero(String premioERecompensa) {
		return premioERecompensa.length() > 0;
	}

	private WebElement ultimoElementosSeletorEntradaPorPagina(List<WebElement> elementosSeletorEntradaPorPagina) {
		return elementosSeletorEntradaPorPagina.get(2);
	}

	private Integer valorSemEspacos(String valor) {
		return Integer.parseInt(valor.trim());
	}

	private String retornaRecompensaDaStringPremioERecompensa(String premioERecompensa) {
		String recompensa = premioERecompensa.substring(
				premioERecompensa.indexOf("(Recompensas:") + "(Recompensas:".length() + 2,
				premioERecompensa.length() - 1);
		return recompensa;
	}

	private String removeCentavosDoValor(String premio) {
		
		if (premio.contains(".")) {
			int posicaoPonto = premio.indexOf('.');
			premio = premio.substring(0, posicaoPonto);
			premio = premio.replaceAll("\\s", "");
			premio = premio.trim();
		}		
		return premio;
	}

	private String removeVirgulasDoValor(String premio) 
	{
		premio = premio.replace(",", "");
		return premio;
	}

	private String retornaPremioDaStringPremioERecompensa(String premioERecompensa) {
		return premioERecompensa.substring(1, premioERecompensa.indexOf("(Recompensas:"));
	}

	private boolean premioeRecompensaContemParteRecompensas(String premioERecompensa) {
		return premioERecompensa.contains("(Recompensas:");
	}

	private List<WebElement> retornaElementosPremioERecompensaDaPagina(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@title[starts-with(., '$')]]"));
	}

	private void clicaNaUltimaOpcaoDoSeletorDeEntradaPorPagina(WebDriver driver, WebElement selectElement) {
		// Clica no último option
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].selected = true;" + "arguments[0].dispatchEvent(new Event('change'));",
				selectElement);		
	}

	private List<WebElement> retornaElementosRecordPerPage(WebDriver driver) {
		return (List<WebElement>) ((JavascriptExecutor) driver)
		.executeScript("return document.querySelectorAll(\"select[title='Records per Page']\");");
	}

	private void alteraUltimoValorDoSeletorDeEntradaPorPaginaPara20000(WebDriver driver, WebElement selectElement) {
		// Altera o valor do último option para "20000"
		((JavascriptExecutor) driver).executeScript("var options = arguments[0].getElementsByTagName('option');"
				+ "options[options.length - 1].value = '20000';" + "options[options.length - 1].text = '20000';",
				selectElement);
	}

	private void aguardarEmSegundos(int i) {
		try {
			Thread.sleep(1000 * i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void exportarArquivoParaExcel(ArrayList<DadosTorneioERede> dadosTorneioERede) {
		try {
			FileInputStream file = new FileInputStream(arquivoExcelTorneios);
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rownum = 1; // começa da segunda linha, já que a primeira linha é o cabeçalho
			for (DadosTorneioERede dados : dadosTorneioERede) {
				int valorTotalPremioSemRecompensa = 0;
				for (EntradaPremioRecompensa entrada : dados.getListaDePremiosDaPagina()) {
					valorTotalPremioSemRecompensa += entrada.getPremioSemRecompensa();
					rownum++;
				}
				XSSFRow row = sheet.getRow(rownum);
				if (row == null) {
					row = sheet.createRow(rownum);
				}
				System.out.println("ValorTotalPremioSemRecompensa: " + valorTotalPremioSemRecompensa);
				row.createCell(0).setCellValue(dados.getTorneio());
				row.createCell(1).setCellValue(dados.getRede());
				row.createCell(2).setCellValue(valorTotalPremioSemRecompensa);
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

}
