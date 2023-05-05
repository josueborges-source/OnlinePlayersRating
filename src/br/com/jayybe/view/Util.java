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

import br.com.jayybe.view.TelaPrincipal.Seletor;


public class Util {
	
	
	public static String retornarURLAPartirDeArquivoDadosDoTorneio(String[] textosTorneio) {
		
		String rede =  textosTorneio[0];
		String torneio = textosTorneio[1];
	
		String urlDaPagina = "https://pt.sharkscope.com/#Find-Tournament//networks/" + torneio
				+ "/tournaments/" + rede;
		
		TelaPrincipal.atualizarStatusLabel("Retornando URL Da Página: " + urlDaPagina, Seletor.ESTATICO);

		return urlDaPagina;
	}
	/*
	public static void aguardeSegundos(Integer segundos) {
		try {
			Thread.sleep(segundos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	*/

	public static Double valorNaoFormatadoParaDinheiro(String valor) {
		return Double.parseDouble(valor.replace(",", ""));
	}
	
	
}
