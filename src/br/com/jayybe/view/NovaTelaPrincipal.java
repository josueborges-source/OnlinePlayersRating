package br.com.jayybe.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

import br.com.jayybe.controller.LeitorExcel;
import br.com.jayybe.controller.SeletorDeArquivos;
import br.com.jayybe.model.DadosTorneioERede;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class NovaTelaPrincipal {

	private JFrame frame;
	private JTextField caminhoDoArquivo;
	private JTable table;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NovaTelaPrincipal window = new NovaTelaPrincipal();
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
	public NovaTelaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	 private void initialize() {
	        frame = new JFrame();
	        frame.setBounds(100, 100, 1000, 2000);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().setLayout(new BorderLayout());

	        JButton EscolherArquivo = new JButton("Escolher Arquivo");

	        EscolherArquivo.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                File caminhoArquivo = SeletorDeArquivos.mostrarSeletorDeArquivo();

	                if (caminhoArquivo != null) {

	                    String nomeCaminhoDoArquivo = caminhoArquivo.toString();

	                    // Teste 1
	                    System.out.println("Nome do Caminho do Arquivo: " + nomeCaminhoDoArquivo);
	                    caminhoDoArquivo.setText(nomeCaminhoDoArquivo);

	                    List<String[]> listaDeTorneiosERedes = LeitorExcel.lerTorneiosERedesExcel(caminhoArquivo);

	                    // Teste 2
	                    for (String[] listaTorneioERede : listaDeTorneiosERedes) {
	                        System.out.println(listaTorneioERede[0]);
	                        System.out.println(listaTorneioERede[1]);
	                    }

	                    atualizaTableModel(listaDeTorneiosERedes);

	                }
	            }
	        });

	        EscolherArquivo.setBounds(563, 61, 113, 94);
	        frame.getContentPane().add(EscolherArquivo);

	        caminhoDoArquivo = new JTextField();
	        caminhoDoArquivo.setBounds(137, 61, 383, 20);
	        frame.getContentPane().add(caminhoDoArquivo);
	        caminhoDoArquivo.setColumns(10);

	        panel = new JPanel(new BorderLayout());
	        panel.setBounds(137, 102, 383, 146);
	        frame.getContentPane().add(panel);

	        
	        table = new JTable();
	        
	        JScrollPane scrollPane = new JScrollPane(table);
	        
	        panel.add(scrollPane);

	        JLabel nomeDoArquivo = new JLabel("Nome do Arquivo:");
	        nomeDoArquivo.setBounds(40, 61, 86, 65);
	        frame.getContentPane().add(nomeDoArquivo);
	        frame.getContentPane().add(panel);	        
	        
	        
	        frame.pack();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	    }
	
	 private void atualizaTableModel(List<String[]> valores) {
		    // create table model
		    String[] columnNames = {"Rede", "Torneio"};

		    Object[][] data = new Object[valores.size()][];
		    for (int i = 0; i < valores.size(); i++) {
		        String[] linha = valores.get(i);
		        Object[] linhaObjetos = new Object[linha.length];
		        for (int j = 0; j < linha.length; j++) {
		            linhaObjetos[j] = linha[j];
		        }
		        data[i] = linhaObjetos;
		    }

		    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

		    // remove previous panel with table
		    frame.getContentPane().remove(panel);

		    // create new panel with table
		    panel = new JPanel();
		    panel.setBounds(137, 102, 383, 146);
		    JTable table = new JTable(tableModel);
		    JScrollPane scrollPane = new JScrollPane(table);
		    panel.add(scrollPane);

		    frame.getContentPane().add(panel);

		    frame.pack();
		    frame.repaint();
		}
}
