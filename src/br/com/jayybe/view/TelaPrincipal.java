package br.com.jayybe.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import br.com.jayybe.controller.ControleArquivoExcel;
import br.com.jayybe.model.DadosTorneioERede;
import br.com.jayybe.util.Util;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;

public class TelaPrincipal {

	public static JFrame frame;
	public static JTextField textField;
	
	JButton importarExcelBtn = new JButton();
	
	ControleArquivoExcel controleArquivoExcel;
	
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
		frame = new JFrame();
		frame.setBounds(100, 100, 710, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(50, 56, 440, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		//JButton importarExcelBtn = new JButton("Importar Excel...");
		
		importarExcelBtn = configuraBotaoImportacaoArquivoExcel(importarExcelBtn);
		
		frame.getContentPane().add(importarExcelBtn);

		JLabel lblNewLabel = new JLabel("Nome do Arquivo:");
		lblNewLabel.setBounds(50, 24, 126, 14);
		frame.getContentPane().add(lblNewLabel);

		atualizarArquivoBtn = new JButton("Atualizar Arquivo");
		atualizarArquivoBtn.setEnabled(false);
		
		atualizarArquivoBtn.setBounds(535, 250, 130, 145);

		// https://pt.sharkscope.com/#Find-Tournament//networks/iPoker/tournaments/654691241

		importarExcelBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Desabilita o botão para evitar que o usuário clique novamente
		        importarExcelBtn.setEnabled(false);

		        // Cria um SwingWorker para executar as operações em uma thread separada
		        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		            @Override
		            protected Void doInBackground() throws Exception {
		                // Executa as operações demoradas aqui, como por exemplo:
		                acaoImportarArquivoBotao();
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
		});	
		
		atualizarArquivoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				acaoPressionarAtualizarArquivo();
			}
		});
		
		frame.getContentPane().add(atualizarArquivoBtn);
		
		textPane = new JTextPane();
		textPane.setBounds(50, 100, 275, 190);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(50, 100, 440, 294);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(270, 190));
		frame.getContentPane().add(scrollPane);
		Component verticalStrut = Box.createVerticalStrut(0);
		verticalStrut.setBounds(553, 180, 99, 12);
		frame.getContentPane().add(verticalStrut);
	}
	
	void acaoImportarArquivoBotao()
	{
		System.out.println("Botão pressionado");
		arquivoExcel = new JanelaDeSelecaoDeArquivoLocal().retornarArquivoExcelComTabelaDados();
		controleArquivoExcel = new ControleArquivoExcel(arquivoExcel);
		
		dadosTorneioERede = controleArquivoExcel.transformarArquivoXLSEmObjetosDadosETorneio();
		dadosTorneioERede = controleArquivoExcel.inserePremioERecompensaEmDadosTorneioERede(dadosTorneioERede);
		
		System.out.println("Dados Torneio e Rede: " + dadosTorneioERede.size());
		atualizarArquivoBtn.setEnabled(true);
	}
	
	void acaoPressionarAtualizarArquivo()
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


