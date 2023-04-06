package br.com.jayybe.view;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import br.com.jayybe.controller.ControleArquivoExcel;
import br.com.jayybe.model.DadosTorneioERede;
import br.com.jayybe.util.Util;


public class TelaPrincipal {

	public static JFrame frame;
	public static JTextField textField;
	
	JButton importarExcelBtn = new JButton();
	
	private ControleArquivoExcel controleArquivoExcel = new ControleArquivoExcel();
	
	JButton atualizarArquivoBtn;
	
	File arquivoExcel;
	
	private ArrayList<DadosTorneioERede> dadosTorneioERede;
	public static JTextPane textPane;
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
	
	public TelaPrincipal() {
		initialize();
	}
	
	private void initialize() {
		
		JLabel lblNewLabel = new JLabel("Nome do Arquivo:");
		JScrollPane scrollPane = new JScrollPane(textPane);	
		
		Component verticalStrut = Box.createVerticalStrut(0);
		verticalStrut.setBounds(553, 180, 99, 12);	
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 710, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(50, 56, 440, 20);
		textField.setColumns(10);

		//JButton importarExcelBtn = new JButton("Importar Excel...");
		
		importarExcelBtn = configuraBotaoImportacaoArquivoExcel(importarExcelBtn);				
		
		lblNewLabel.setBounds(50, 24, 126, 14);

		atualizarArquivoBtn = new JButton("Atualizar Arquivo");
		atualizarArquivoBtn.setEnabled(false);
		
		atualizarArquivoBtn.setBounds(535, 250, 130, 145);

		// https://pt.sharkscope.com/#Find-Tournament//networks/iPoker/tournaments/654691241	
		
		textPane = new JTextPane();
		textPane.setBounds(50, 100, 275, 190);
		
		scrollPane.setBounds(50, 100, 440, 294);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(270, 190));
			
		
		
		frame.getContentPane().add(textField);
		frame.getContentPane().add(importarExcelBtn);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(atualizarArquivoBtn);
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().add(verticalStrut);
		
		importarExcelBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	acaoImportarArquivo();
		    }

		});	
		
		atualizarArquivoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				acaoPressionarAtualizarArquivo();
			}
		});
	}
	
	
	private void acaoImportarArquivo() {
		
		 // Desabilita o botão para evitar que o usuário clique novamente
        importarExcelBtn.setEnabled(false);

        // Cria um SwingWorker para executar as operações em uma thread separada
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception 
            {		            	
                // Executa as operações demoradas aqui, como por exemplo:
            	controleArquivoExcel.retornarArquivoExcelParaModeloTorneio();
                return null;
            }

            @Override
            protected void done() {
                // Ativa o botão novamente quando as operações terminarem
                importarExcelBtn.setEnabled(true);
            }
        };
        // Executa o SwingWorker
        worker.execute();		
	}
	
	
	private void acaoPressionarAtualizarArquivo()
	{		
		System.out.println("dadosTorneioERede.size(): " + dadosTorneioERede.size());
		Util.imprimirDadosTorneioERede(dadosTorneioERede);
		
		controleArquivoExcel.exportarListaDadosTorneioERedeParaArquivoXLS(dadosTorneioERede);
	}
	
	private JButton configuraBotaoImportacaoArquivoExcel(JButton importarExcelBtn) {
		importarExcelBtn.setLayout(new BoxLayout(importarExcelBtn, BoxLayout.Y_AXIS)); // Define o layout personalizado

		// Cria o ícone e redimensiona para 30x3 pixels
		ImageIcon icon = new ImageIcon(getClass().getResource("/excel.jpg"));
		Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel iconLabel = new JLabel(new ImageIcon(img));

		// Cria um rótulo para o texto e define o alinhamento
		JLabel textLabel = new JLabel("Importar Excel");
		textLabel.setAlignmentY(2.0f);
		textLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		textLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		textLabel.setBorder(BorderFactory.createEmptyBorder(1,0,0,0)); 

		// Adiciona o ícone e o rótulo de texto ao botão		
		importarExcelBtn.add(iconLabel);
		importarExcelBtn.add(textLabel);		
		
		
		importarExcelBtn.setBounds(534, 94, 131, 145);
		return importarExcelBtn;
	}	
}


