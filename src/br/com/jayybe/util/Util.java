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

	public static void imprimirDadosTorneioERede(ArrayList<DadosTorneioERede> dadosTorneioERede) {

		for (DadosTorneioERede dadoTorneioERede : dadosTorneioERede) {
			System.out.println("dadosTorneioERede: " + dadoTorneioERede);
		}

	}
	
	/*
	public static void EmitirValorTextoEmLogJTextPane(String texto) {
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
	*/
}
