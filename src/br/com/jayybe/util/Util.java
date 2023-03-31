package br.com.jayybe.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.jayybe.model.DadosTorneioERede;
import br.com.jayybe.model.EntradaPremioRecompensa;
import br.com.jayybe.view.TelaPrincipal;

public class Util {
	

	private static int contadorLinhas = 0;
	private static JTextPane textPaneLogDoSharkscope;
	
	static {
		textPaneLogDoSharkscope = TelaPrincipal.textPane;
	}
	
	private static Color[] cores = new Color[] { Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };

	public static void imprimirEntradaPremioERecompensaCasoConfigurado(
			EntradaPremioRecompensa entradaPremioRecompensa) {
		if (Configuracoes.imprimirPremioERecompensa == true) {
			System.out.println("Prêmio: " + entradaPremioRecompensa.getPremio());
			System.out.println("Recompensa: " + entradaPremioRecompensa.getRecompensa());
		}
	}


	private Color obterCorLinha(int linha) {
		return cores[linha % cores.length];
	}

	public static void InserirValorEmJTextPaneComMarcacaoDeTempo(String texto) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            // Obtém o modelo do documento do JTextPane
	            StyledDocument doc = textPaneLogDoSharkscope.getStyledDocument();

	            // Define o estilo do texto para a linha atual
	            SimpleAttributeSet estilo = new SimpleAttributeSet();
	            StyleConstants.setForeground(estilo, cores[contadorLinhas % cores.length]);

	            // Adiciona o texto com o estilo configurado
	            try {
	                doc.insertString(doc.getLength(), "•", estilo);
	                doc.insertString(doc.getLength(), texto + "\n", estilo);
	            } catch (BadLocationException e) {
	                e.printStackTrace();
	            }

	            contadorLinhas++;

	            // Executa o atraso em uma nova thread
	            new Thread(new Runnable() {
	                public void run() {
	                    try {
	                        Thread.sleep(50);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                    // Atualiza o JTextPane
	                    SwingUtilities.invokeLater(new Runnable() {
	                        public void run() {
	                            textPaneLogDoSharkscope.revalidate();
	                            textPaneLogDoSharkscope.repaint();
	                        }
	                    });
	                }
	            }).start();
	        }
	    });
	}


	public ArrayList<DadosTorneioERede> instanciarTorneioERedeAPartirDeArquivoExcelLocal(File arquivo) {

		ArrayList<DadosTorneioERede> dados = new ArrayList<>();

		try (FileInputStream file = new FileInputStream(arquivo)) {
			// Abre o arquivo Excel
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Seleciona a primeira planilha
			Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();

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

	public static void imprimirDadosTorneioERede(ArrayList<DadosTorneioERede> dadosTorneioERede) {

		for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
			System.out.println("dadosTorneioERede: " + dadoTorneioERede);
		}

	}
}
