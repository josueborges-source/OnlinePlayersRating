package br.com.jayybe.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import br.com.jayybe.controller.ControleArquivoExcel;
import br.com.jayybe.model.DadosTorneioERede;
import br.com.jayybe.util.Util;
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
		frame.setBounds(100, 100, 561, 383);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(50, 56, 273, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		//JButton importarExcelBtn = new JButton("Importar Excel...");		
		importarExcelBtn = new ConfiguracaoBotaoImportacao().configuraBotaoImportacaoArquivoExcel(importarExcelBtn);
		
		frame.getContentPane().add(importarExcelBtn);

		JLabel lblNewLabel = new JLabel("Nome do Arquivo:");
		lblNewLabel.setBounds(50, 24, 126, 14);
		frame.getContentPane().add(lblNewLabel);

		atualizarArquivoBtn = new JButton("Atualizar Arquivo");
		atualizarArquivoBtn.setEnabled(false);
		
		atualizarArquivoBtn.setBounds(376, 162, 126, 38);

		// https://pt.sharkscope.com/#Find-Tournament//networks/iPoker/tournaments/654691241

		importarExcelBtn.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {
		    	
		        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
		        {
		            @Override
		            protected Void doInBackground() throws Exception 
		            {
		            	//importarExcelBtn.setEnabled(false);
		                acaoImportarArquivoBotao();
		                //importarExcelBtn.setEnabled(true);
		                return null;
		            }
		        };
		        
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
		textPane.setBounds(50, 103, 273, 176);
		frame.getContentPane().add(textPane);		
	}
	
	void acaoImportarArquivoBotao()
	{
		System.out.println("Botão pressionado");
		arquivoExcel = new JanelaDeSelecaoDeArquivoLocal().retornarArquivoExcelComTabelaDados();
		controleArquivoExcel = new ControleArquivoExcel(arquivoExcel);					
		dadosTorneioERede = controleArquivoExcel.transformarArquivoXLSEmObjetosDadosETorneio();
						
		System.out.println("Dados Torneio e Rede: " + dadosTorneioERede.size());
		atualizarArquivoBtn.setEnabled(true);
	}
	
	void acaoPressionarAtualizarArquivo()
	{		
		System.out.println("dadosTorneioERede.size(): " + dadosTorneioERede.size());
		Util.imprimirDadosTorneioERede(dadosTorneioERede);
		
		controleArquivoExcel.exportarListaDadosTorneioERedeParaArquivoXLS(dadosTorneioERede);
	}	
	
}


