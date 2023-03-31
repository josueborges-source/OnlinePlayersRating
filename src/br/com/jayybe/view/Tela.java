package br.com.jayybe.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Tela {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela window = new Tela();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tela() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 554, 242);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(50, 56, 273, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton importarExcelBtn = new JButton("Importar Excel...");

		importarExcelBtn.setBounds(358, 38, 126, 38);
		frame.getContentPane().add(importarExcelBtn);

		JLabel lblNewLabel = new JLabel("Nome do Arquivo:");
		lblNewLabel.setBounds(50, 24, 126, 14);
		frame.getContentPane().add(lblNewLabel);

		JButton atualizarArquivoBtn = new JButton("Atualizar Arquivo");
		atualizarArquivoBtn.setEnabled(false);
		atualizarArquivoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		atualizarArquivoBtn.setBounds(211, 127, 133, 43);

		// https://pt.sharkscope.com/#Find-Tournament//networks/iPoker/tournaments/654691241

		importarExcelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				acaoImportarExcelBotao();
			}
		});
		frame.getContentPane().add(atualizarArquivoBtn);
	}

	public void acaoImportarExcelBotao() {
		System.out.println("Importar Excel Pressionado");
		File arquivoExcelTorneios = retornaArquivoExcelComTabelaDados();
		if (arquivoExcelTorneios != null) {

			// atualizarArquivoBtn.setEnabled(true);
			ArrayList<DadosTorneioERede> dadosTorneioERede = new Util()
					.listaTorneioERedeAPartirArquivo(arquivoExcelTorneios);

			// Teste dados capturados
			for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
				String url = retornarURL(dadoTorneioERede);
				System.out.println("URL: " + url);
				processarDadosDaPagina(url);
			}
		}
	}

	public void processarDadosDaPagina(String url) {

		// Configura o caminho do driver do Chrome
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

		// Instancia o driver do Chrome
		WebDriver driver = new ChromeDriver();

		// Navega para o site do Google
		driver.get(url);

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		

		List<WebElement> elementosSelect = (List<WebElement>) ((JavascriptExecutor) driver).executeScript(
			    "return document.querySelectorAll(\"select[title='Records per Page']\");"
			);

		WebElement selectElement = elementosSelect.get(2);
		
		// Altera o valor do último option para "20000"
		((JavascriptExecutor) driver).executeScript(
		        "var options = arguments[0].getElementsByTagName('option');" +
		        "options[options.length - 1].value = '20000';" +
		        "options[options.length - 1].text = '20000';",
		        selectElement
		);

		// Clica no último option
		((JavascriptExecutor) driver).executeScript(
		        "var options = arguments[0].getElementsByTagName('option');" +
		        "options[options.length - 1].selected = true;" +
		        "arguments[0].dispatchEvent(new Event('change'));",
		        selectElement
		);
		
		// Encontra todos os elementos da página com um atributo "title" que começa com "$"
		List<WebElement> elements = driver.findElements(By.cssSelector("[title^='$']"));

		System.out.println(elements.size());
		// Cria uma lista de Strings para armazenar os títulos
		List<String> titles = new ArrayList<String>();

		// Itera sobre os elementos encontrados e adiciona seus títulos à lista de títulos
		for (WebElement element : elements) {			
			System.out.println(element.getText());
			String elementoPremio = element.getText();
			
			//String termoDeSeparacaoTrechoRecompensa = "(Recompensas:";
			
			//Obtém primeira parte do texto
			String premio = obterParteDoTextoAPatirDeDelimitador(elementoPremio, Integer.valueOf(1));
			premio = premio.replaceAll("\\,", "");
			premio = eliminarCentavos(premio);
			
			//Obtém segunda parte do texto
			String recompensa = obterParteDoTextoAPatirDeDelimitador(elementoPremio, Integer.valueOf(2));
			recompensa = recompensa.replaceAll("\\,","" );
			recompensa = eliminarCentavos(recompensa);		
			
			
			/*
			String primeiraParte = elementString.substring(0, elementString.indexOf("(Recompensas:"));
			String segundaParte = elementString.substring(elementString.indexOf("(Recompensas:")+"(Recompensas:".length()+1);
			*/
			
			System.out.println(premio);
			System.out.println(recompensa);
			
			/*
			System.out.println("elementString: "+elementString);
			elementString = elementString.replaceAll("\\$", "");
			elementString = elementString.replaceAll(",", "");		
			elementString = elementString.replaceAll("\\.", "");
			System.out.println("elementString: "+elementString);
			*/
		    titles.add(elementoPremio);
		}

		// Imprime a lista de títulos
		System.out.println(titles);
	}	
	
	private String eliminarCifroes(String texto) {
		if(texto.contains("$")) {
		texto = texto.substring(texto.indexOf("$"));
		}
		return texto;
	}
	
	private String eliminarCentavos(String texto) {
		if(texto.contains(".")) {
		texto = texto.substring(texto.indexOf("."));
		}
		return texto;
	}

	public static String obterParteDoTextoAPatirDeDelimitador(String textoOriginal, Integer numeroDaParte) {
		String delimitador = "(Recompensas:";
	    String[] partes = textoOriginal.split(Pattern.quote(delimitador));
	    String parte = partes[numeroDaParte-1];	    
	    return parte;	  
	}

	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	private String retornarURL(DadosTorneioERede dadosTorneioERede) {
		String urlDaPagina = "";

		urlDaPagina = "https://pt.sharkscope.com/#Find-Tournament//networks/" + dadosTorneioERede.getRede()
				+ "/tournaments/" + dadosTorneioERede.getTorneio();

		return urlDaPagina;
	}

	private File retornaArquivoExcelComTabelaDados() {
		JFileChooser fileChooser = new JFileChooser();
		File arquivoSelecionadoExcel = null;

		// Define o diretório inicial para a Área de Trabalho
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		File desktopDir = new File(desktopPath);
		fileChooser.setCurrentDirectory(desktopDir);

		// Adiciona um filtro para exibir apenas arquivos do tipo Excel
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos do Excel", "xls", "xlsx");
		fileChooser.setFileFilter(filter);

		// Configura para exibir apenas arquivos, não diretórios
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Remove o filtro padrão que exibe "Todos os Arquivos"
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		for (FileFilter fileFilter : filters) {
			String description = fileFilter.getDescription();
			if (description.equals("Todos os Arquivos") || description.equals("All Files")) {
				fileChooser.removeChoosableFileFilter(fileFilter);
			}
		}

		// Exibe o JFileChooser em uma janela e aguarda a seleção do usuário
		int result = fileChooser.showOpenDialog(null);

		// Se o usuário selecionou um arquivo, exibe o caminho do arquivo selecionado
		if (result == JFileChooser.APPROVE_OPTION) {
			arquivoSelecionadoExcel = fileChooser.getSelectedFile();
			System.out.println("Arquivo selecionado: " + arquivoSelecionadoExcel.getAbsolutePath());
		}
		return arquivoSelecionadoExcel;
	}
}
