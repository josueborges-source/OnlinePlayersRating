package br.com.jayybe.controller;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Log {
	
	private static JTextPane textPaneLogDoSharkscope;
	private static int contadorLinhas = 0;
	
	private static Color[] cores = new Color[] { Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };

	
	
	public static void acaoParaLog(String mensagemParaInserirEmLog) 
	{			
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
	                doc.insertString(doc.getLength(), mensagemParaInserirEmLog + "\n", estilo);
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
	
	
}
