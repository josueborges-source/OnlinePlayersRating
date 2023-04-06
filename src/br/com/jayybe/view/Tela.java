package br.com.jayybe.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import br.com.jayybe.model.DadosTorneioERede;
import br.com.sharskscope.controle.ImportarArquivoExcel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Tela {

	private JFrame frame;
	private JTextField textField;
	
	private ControleArquivoExcel controleArquivoExcel;
	
	private ArrayList<DadosTorneioERede> listaDadosTorneiosERedesDoArquivoExcel;
	//private ControleArquivoExcel controleArquivoExcel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela window = new Tela();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
	public Tela() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 554, 242);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(50, 56, 273, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton importarExcelBtn = new JButton("Importar Excel...");

		importarExcelBtn.setBounds(358, 38, 126, 38);
		frame.getContentPane().add(importarExcelBtn);

		JLabel lblNewLabel = new JLabel("Nome do Arquivo:");
		lblNewLabel.setBounds(50, 24, 126, 14);
		frame.getContentPane().add(lblNewLabel);

		JButton atualizarArquivoBtn = new JButton("Atualizar Arquivo");
		atualizarArquivoBtn.setEnabled(false);
		
		atualizarArquivoBtn.setBounds(211, 127, 133, 43);

		// https://pt.sharkscope.com/#Find-Tournament//networks/iPoker/tournaments/654691241

		importarExcelBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				
				// Abre Prompt Para Seleção de Arquivo
				File arquivoExcel = new SelecaoDeArquivoLocal().retornaArquivoExcelComTabelaDados();
				
				//controleArquivoExcel = new ControleArquivoExcel(arquivoExcel);
				listaDadosTorneiosERedesDoArquivoExcel = new ImportarArquivoExcel(arquivoExcel).acaoImportarArquivoExcel();
								
				System.out.println(listaDadosTorneiosERedesDoArquivoExcel.size());
				atualizarArquivoBtn.setEnabled(true);
			}
		});
		
		
		atualizarArquivoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ControleArquivoExcel controleArquivoExcel = new ControleArquivoExcel(arquivoExcel);
				//dadosTorneioERede = controleArquivoExcel.acaoImportarArquivoExcel();
				System.out.println(listaDadosTorneiosERedesDoArquivoExcel.size());
				controleArquivoExcel.exportarArquivoParaExcel(listaDadosTorneiosERedesDoArquivoExcel);
				//new ControleArquivoExcel().exportarArquivoParaExcel(dadosTorneioERede);
			}
		});
		frame.getContentPane().add(atualizarArquivoBtn);
	}		
}


