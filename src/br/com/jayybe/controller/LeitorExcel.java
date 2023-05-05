package br.com.jayybe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.jayybe.view.TelaPrincipal;
import br.com.jayybe.view.TelaPrincipal.Seletor;

public class LeitorExcel {

	private static XSSFWorkbook workbook;
	private static File arquivo = null;
	private static int linhaAtual = 1;

	public static XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public static void setWorkbook(XSSFWorkbook workbook) {
		LeitorExcel.workbook = workbook;
	}

	public static File getArquivo() {
		return arquivo;
	}

	public static void setArquivo(File arquivo) {
		LeitorExcel.arquivo = arquivo;
	        try {
	            LeitorExcel.workbook = new XSSFWorkbook(arquivo);
	        } catch (InvalidFormatException | IOException e) {
	            JOptionPane.showMessageDialog(null, e);
	            TelaPrincipal.atualizarStatusLabel("Erro no Arquivo: " + e.getMessage(), Seletor.ESTATICO);
	            e.printStackTrace();
	        }
	}

	public LeitorExcel(File arquivo) throws IOException {
		LeitorExcel.arquivo = arquivo;
		try (FileInputStream file = new FileInputStream(arquivo)) {
			LeitorExcel.workbook = new XSSFWorkbook(file);
		} catch (IOException e) {
			throw new IOException("Falha ao abrir o arquivo: " + e.getMessage());
		}
	}

	public static List<String[]> lerTorneiosERedesExcel() {
		List<String[]> dados = new ArrayList<>();

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
			if (cellA == null || cellA.getCellType() == CellType.BLANK || cellB == null
					|| cellB.getCellType() == CellType.BLANK) {
				break;
			}

			// Captura os valores das colunas A e B
			String torneio = String.valueOf((long) row.getCell(0).getNumericCellValue());
			String rede = row.getCell(1).getStringCellValue();

			// Cria um array de strings com os valores capturados
			String[] dadosTorneioERede = { torneio, rede };

			// Adiciona o array de strings à lista de dados
			dados.add(dadosTorneioERede);
		}
		return dados;
	}

	// Método para fechar o arquivo e salvar as alterações
	public void fecharArquivo() {
		try {
			if (workbook != null) {
				workbook.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Grava as alterações no arquivo
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(arquivo);
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	public static void inserirValorNaColuna3(String valor, int linha) {
		try {
			FileInputStream file;
			file = new FileInputStream(arquivo);

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Verifica se o arquivo Excel foi definido
			if (arquivo == null) {
				throw new RuntimeException("Arquivo Excel não definido");
			}

			// Verifica se o workbook foi carregado
			if (workbook == null) {
				throw new RuntimeException("Workbook não carregado");
			}

			// Seleciona a primeira planilha
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Cria uma nova linha
			XSSFRow row = sheet.createRow(linha);

			// Insere o valor na coluna 3 da nova linha (considerando que a primeira coluna
			// é a coluna 0)
			XSSFCell cell = row.createCell(2);
			cell.setCellValue(valor);

			// Salva as alterações no arquivo Excel
			FileOutputStream fileOut = new FileOutputStream(arquivo);
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void inserirValorNaColuna4(String valor, int linha) {
		try {
			FileInputStream file;
			file = new FileInputStream(arquivo);

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Verifica se o arquivo Excel foi definido
			if (arquivo == null) {
				throw new RuntimeException("Arquivo Excel não definido");
			}

			// Verifica se o workbook foi carregado
			if (workbook == null) {
				throw new RuntimeException("Workbook não carregado");
			}

			// Seleciona a primeira planilha
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Atualiza o contador de linhas
			linhaAtual = linha + 1;

			// Cria uma nova linha
			XSSFRow row = sheet.createRow(linhaAtual);

			// Insere o valor na coluna 4 da nova linha (considerando que a primeira coluna
			// é a coluna 0)
			XSSFCell cell = row.createCell(3);
			cell.setCellValue(valor);

			// Salva as alterações no arquivo Excel
			FileOutputStream fileOut = new FileOutputStream(arquivo);
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void insereValor(int linha, int coluna, String valor) {
		try {
	    FileInputStream arquivoEntrada;
			arquivoEntrada = new FileInputStream(arquivo);
		
	    XSSFWorkbook workbook = new XSSFWorkbook(arquivoEntrada);
	    XSSFSheet sheet = workbook.getSheetAt(0); // planilha de índice 0
	    
	    // acessando a célula de acordo com a linha e coluna especificadas
	    XSSFRow row = sheet.getRow(linha-1); // subtraindo 1 porque as linhas começam em 0
	    if (row == null) {
	        row = sheet.createRow(linha-1);
	    }
	    XSSFCell cell = row.getCell(coluna-1); // subtraindo 1 porque as colunas começam em 0
	    if (cell == null) {
	        cell = row.createCell(coluna-1);
	    }
	    
	    // definindo o valor da célula
	    cell.setCellValue(valor);
	    
	    // salvando o arquivo
	    FileOutputStream arquivoSaida = new FileOutputStream(arquivo);
	    workbook.write(arquivoSaida);
	    arquivoSaida.close();
	    workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

/*
 * public static void writeWorkbookToFile() {
 * 
 * try { FileOutputStream out = new FileOutputStream(arquivo);
 * workbook.write(out); workbook.close(); out.close();
 * System.out.println("Planilha exportada com sucesso!"); } catch (IOException
 * e) { e.printStackTrace(); } }
 */
