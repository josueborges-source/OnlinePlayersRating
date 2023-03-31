package br.com.jayybe.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Util {


	public ArrayList<DadosTorneioERede> listaTorneioERedeAPartirArquivo(File arquivo){
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
	                if (cellA == null || cellA.getCellType() == CellType.BLANK
	                        || cellB == null || cellB.getCellType() == CellType.BLANK) {
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
}