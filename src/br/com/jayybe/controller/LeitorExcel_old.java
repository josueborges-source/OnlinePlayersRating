package br.com.jayybe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeitorExcel_old {

	private static XSSFWorkbook workbook;
	private static File arquivo;

	LeitorExcel_old(File arquivo) {
		try (FileInputStream file = new FileInputStream(arquivo)) {
			LeitorExcel_old.workbook = new XSSFWorkbook(file);
			LeitorExcel_old.arquivo = arquivo;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public static File getArquivo() {
		return arquivo;
	}
	
	public static void setArquivo(File arquivo) {
		try {			
			LeitorExcel_old.workbook = new XSSFWorkbook(arquivo);
			LeitorExcel_old.arquivo = arquivo;
		} catch (InvalidFormatException |IOException e) {
			e.printStackTrace();
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

	private static int linhaAtual = 1; // linha inicial

	public static void insertValueIntoColumn1(String value) {
		XSSFSheet sheet = workbook.getSheetAt(0);

		XSSFRow row = sheet.getRow(linhaAtual);
		if (row == null) {
			row = sheet.createRow(linhaAtual);
		}
		XSSFCell cell = row.getCell(0);
		if (cell == null) {
			cell = row.createCell(0);
		}
		cell.setCellValue(value);		
	}

	public static void insertValueIntoColumn2(String value)  {
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFRow row = sheet.getRow(linhaAtual);
		if (row == null) {
			row = sheet.createRow(linhaAtual);
		}
		XSSFCell cell = row.getCell(1);
		if (cell == null) {
			cell = row.createCell(1);
		}
		cell.setCellValue(value);
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(arquivo);
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void fecharArquivo() {
		try {
			if (workbook != null) {
				workbook.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(arquivo);
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
