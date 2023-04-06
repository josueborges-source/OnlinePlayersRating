package br.com.sharskscope.controle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.jayybe.model.*;
import br.com.jayybe.view.Util;

public class ImportarArquivoExcel {

	private File arquivoExcelTorneios;
	
	public ImportarArquivoExcel(File arquivoExcelTorneios) {
		this.arquivoExcelTorneios = arquivoExcelTorneios;
	}

	public ArrayList<DadosTorneioERede> acaoImportarArquivoExcel() {

		ArrayList<DadosTorneioERede> dadosTorneioERedeDeRetorno = new ArrayList<DadosTorneioERede>();

		if (arquivoExcelTorneios != null) {
			ArrayList<DadosTorneioERede> dadosTorneioERede = new Util()
					.instanciaTorneioERedeAPartirDeArquivoExcelLocal(arquivoExcelTorneios);

			System.out.println("Dados Do Torneio: "+ dadosTorneioERede.size());
			
			
			// Para Cada Dado Torneio e Rede Popular Premio e Recompensa
			for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
				DadosTorneioERede dadoTorneioERedeResposta = popularPremioERecompensaEmDadosTorneioERede(
						dadoTorneioERede);
				System.out.println(dadoTorneioERedeResposta.toString());
				dadosTorneioERedeDeRetorno.add(dadoTorneioERedeResposta);
			}
		}
		return dadosTorneioERedeDeRetorno;
	}

	private DadosTorneioERede popularPremioERecompensaEmDadosTorneioERede(DadosTorneioERede dadoTorneioERede) {

		String urlDaListaDeDadosDoTorneio = retornarURLAPartirDeArquivoDadosDoTorneio(dadoTorneioERede);

		WebDriver driverChromePagina = configuraPaginaDeElementosPremioERecompensaDaPagina(urlDaListaDeDadosDoTorneio);

		List<WebElement> elementosPremioERecompensa = retornaElementosPremioERecompensaDaPagina(driverChromePagina);
	
		// Imprime Lista de Premios e Recompensas
		for (WebElement premioERecompensaElemento : elementosPremioERecompensa) {

			String premioERecompensa = premioERecompensaElemento.getText();
			
			System.out.println("PremioERecompensa: "+premioERecompensa);

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
			} else if (textoMaiorQueZero(premioERecompensa)) {

				String premio = retornaPremioEmStringSemRecompensa(premioERecompensa);

				Integer premioInteiro = formataPremioParaFormatoInteiro(premio);

				entradaPremioRecompensa.setPremio(premioInteiro);

				System.out.println("Prêmio Inteiro: " + premioInteiro);
			}
			dadoTorneioERede.adicionaEntradaPremioRecompensa(entradaPremioRecompensa);
		}

		driverChromePagina.quit();
		return dadoTorneioERede;
	}
	
	private String retornaPremioEmStringSemRecompensa(String premioERecompensa) {
		System.out.println("premioERecompensa: "+premioERecompensa);
		return premioERecompensa.substring(1);
	}

	private boolean textoMaiorQueZero(String premioERecompensa) {
		return premioERecompensa.length() > 0;
	}
	
	private Integer formataPremioParaFormatoInteiro(String premio) {
		premio = removeVirgulasDoValor(premio);				
		premio = removeCentavosDoValor(premio);
		Integer premioInteiro = valorSemEspacos(premio);
		return premioInteiro;
	}
	
	private Integer valorSemEspacos(String valor) {
		return Integer.parseInt(valor.trim());
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

	private String retornaRecompensaDaStringPremioERecompensa(String premioERecompensa) {
		String recompensa = premioERecompensa.substring(
				premioERecompensa.indexOf("(Recompensas:") + "(Recompensas:".length() + 2,
				premioERecompensa.length() - 1);
		return recompensa;
	}
	
	public String retornarURLAPartirDeArquivoDadosDoTorneio(DadosTorneioERede dadosTorneioERede) {
		String urlDaPagina = "https://pt.sharkscope.com/#Find-Tournament//networks/" + dadosTorneioERede.getRede()
				+ "/tournaments/" + dadosTorneioERede.getTorneio();

		return urlDaPagina;
	}

	private Integer formataRecompensaParaFormatoInteiro(String recompensa) {
		
		recompensa = removeVirgulasDoValor(recompensa);		
		recompensa = removeCentavosDoValor(recompensa);
		Integer recompensaInteiro = valorSemEspacos(recompensa);
		
		return recompensaInteiro;
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
		
		WebElement ultimoSelectEntradasPorPagina = ultimoElementosSeletorEntradaPorPagina(
				elementosSeletorEntradaPorPagina);

		alteraUltimoValorDoSeletorDeEntradaPorPaginaPara20000(driverChromePagina, ultimoSelectEntradasPorPagina);

		clicaNaUltimaOpcaoDoSeletorDeEntradaPorPagina(driverChromePagina, ultimoSelectEntradasPorPagina);

		aguardarEmSegundos(10);
		return driverChromePagina;
	}
	
	private String retornaPremioDaStringPremioERecompensa(String premioERecompensa) {
		System.out.println("premioERecompensa: "+premioERecompensa);
		return premioERecompensa.substring(1, premioERecompensa.indexOf("(Recompensas:"));
	}
	
	private List<WebElement> retornaElementosPremioERecompensaDaPagina(WebDriver driver) {
		
		List<WebElement> elementosPremioRecomensa = driver.findElements(By.xpath("//*[@title]"));
		
		for (WebElement elemento : elementosPremioRecomensa) {
		    String title = elemento.getAttribute("title");
		    if (title.startsWith("$") && title.substring(1).matches("\\d+")) {
		    	elementosPremioRecomensa.add(elemento);
		    }
		}		
		return elementosPremioRecomensa;
	}

	
	private boolean premioeRecompensaContemParteRecompensas(String premioERecompensa) {
		return premioERecompensa.contains("(Recompensas:");
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

	private WebElement ultimoElementosSeletorEntradaPorPagina(List<WebElement> elementosSeletorEntradaPorPagina) {
		return elementosSeletorEntradaPorPagina.get(2);
	}
}
