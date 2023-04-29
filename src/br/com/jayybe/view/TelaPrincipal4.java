package br.com.jayybe.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.com.jayybe.controller.LeitorExcel;
import br.com.jayybe.controller.SeletorDeArquivos;
import br.com.jayybe.controller.WebDriverUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TelaPrincipal4 {

	private JFrame frame;
	private JTextField arquivoCaminhoField;
	private JTable tabelaRedeETorneio;
	private DefaultTableModel tableModel;
	private DefaultTableModel tableModelDadosDaWeb;
	private JTable tabelaRedeTorneioPremioRecompensa;
	private JButton importarDadosWebButton;
	private JButton botaoImportarXLS;
	private JPanel panelTabelaDadosXLS;
	private JLabel lblNewLabel;
	private JLabel labelDadosXLS;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal4 window = new TelaPrincipal4();
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
	public TelaPrincipal4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1056, 532);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		botaoImportarXLS = new JButton("< ImportarXLS");

		/// Importar Dados Botão
		importarDadosWebButton = new JButton("Importar Dados Web");

		importarDadosWebButton.setBounds(50, 348, 215, 30);
		frame.getContentPane().add(importarDadosWebButton);

		botaoImportarXLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// 1: Retorna Caminho do Arquivo via Janela
				File arquivoExcel = SeletorDeArquivos.mostrarSeletorDeArquivo();

				// Se caminho do arquivo foi localizado
				if (arquivoExcel != null) {

					String nomeCaminhoDoArquivo = arquivoExcel.toString();
					// Teste 1 (Caminho do Arquivo via Janela)
					System.out.println("Nome do Caminho do Arquivo: " + nomeCaminhoDoArquivo);

					// 2: Atribui nome do arquivo para o textfield
					arquivoCaminhoField.setText(nomeCaminhoDoArquivo);

					// Teste 2 (Atribui nome do arquivo para o textfield)
					System.out.println("Valor do texto do textfield: " + arquivoCaminhoField.getText());
					
					
					// 3: Ler campos de torneio e rede do arquivo excel
					LeitorExcel.setArquivo(arquivoExcel);
					System.out.println(arquivoExcel.getName());
					
					System.out.println("LeitorArquivoExcel: " + LeitorExcel.getArquivo().getName());
					List<String[]> listaDeTorneiosERedes = LeitorExcel.lerTorneiosERedesExcel();

					if (listaDeTorneiosERedes != null) {

						// Teste 3 (Ler campos de torneio e rede do arquivo excel)
						for (String[] listaTorneioERede : listaDeTorneiosERedes) {
							System.out.println(listaTorneioERede[0]);
							System.out.println(listaTorneioERede[1]);
						}
						// 4: Seta valores campo de torneio e rede na tabela
						atualizaTableModelRedeETorneio(listaDeTorneiosERedes);

					}
				}
			}
		});

		importarDadosWebButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<String[]> valoresDaLinha = new ArrayList<String[]>();

				int numLinhasTabelaImportacaoXLS = tabelaRedeETorneio.getRowCount();
				int numColunasTabelaImportacaoXLS = tabelaRedeETorneio.getColumnCount();

				for (int linha = 0; linha < numLinhasTabelaImportacaoXLS; linha++) {
					String[] linhaPremioERecompensa = new String[4];

					for (int coluna = 0; coluna < numColunasTabelaImportacaoXLS; coluna++) {

						if (coluna == 0) {
							linhaPremioERecompensa[0] = (String) tabelaRedeETorneio.getValueAt(linha, 0);
						} else if (coluna == 1) {
							linhaPremioERecompensa[1] = (String) tabelaRedeETorneio.getValueAt(linha, 1);
						}
					}					
					
					String urlDoTorneioERede = Util.retornarURLAPartirDeArquivoDadosDoTorneio(linhaPremioERecompensa);

					WebDriverUtil webDriverUtil = new WebDriverUtil();

					String codigoHtml = webDriverUtil.getHtml(urlDoTorneioERede);

					aguardeSegundos(20);

					String[] valoresDaTabelaElements;
					valoresDaTabelaElements = HTMLUtils.encontrarElementosComIdJqg(codigoHtml);							
					
					String[][] listaValorPremioRecompensa = HTMLUtils.retornaValorPremioRecompensa(valoresDaTabelaElements);
													
					Double premioTotal = Double.valueOf(0);
					Double recompensaTotal = Double.valueOf(0);
					
					for (int i = 0; i < listaValorPremioRecompensa.length; i++) {
						System.out.println("Nome: " + listaValorPremioRecompensa[i][0]);	
						
						if(listaValorPremioRecompensa[i][1]!="") {
							Double premio = Util.valorNaoFormatadoParaDinheiro(listaValorPremioRecompensa[i][1]);							
							premioTotal = Double.sum(premioTotal, premio);
							//System.out.println("Prêmio Atual: " + premio);
							//System.out.println("Prêmio Total: " + premioTotal + "-" + (premioTotal - premio) +  "=" + premio);
							System.out.println();
							//System.out.println("Prêmio: " + premio);
						}
						if(listaValorPremioRecompensa[i][2]!="") {
							Double recompensa = Util.valorNaoFormatadoParaDinheiro(listaValorPremioRecompensa[i][2]);
							recompensaTotal = Double.sum(recompensaTotal, recompensa);							
							//System.out.println("Recompensa Atual: " + recompensa );
							//System.out.println("Recompensa Total: " + recompensaTotal + "-" + (recompensaTotal - recompensa) + "=" + recompensa);
							//System.out.println();
							//System.out.println("Recompensa: " + recompensa);
						}
						System.out.println();
					}
					
					aguardeSegundos(20);
					
					DecimalFormat df = new DecimalFormat("0.00");
					
					linhaPremioERecompensa[2] = df.format(premioTotal).toString();
					linhaPremioERecompensa[3] = df.format(recompensaTotal).toString();
					
					
					valoresDaLinha.add(linhaPremioERecompensa);

					System.out.println();
				}
				System.out.println(LeitorExcel.getArquivo());
				atualizaTableModelRedeTorneioPremioERecompensa(valoresDaLinha);
			}
		});
		
		JButton exportarDadosButton = new JButton("Exportar Dados XLS");
		exportarDadosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				int numLinhasTabelaImportacaoXLS = tabelaRedeTorneioPremioRecompensa.getRowCount();
				int numColunasTabelaImportacaoXLS = tabelaRedeTorneioPremioRecompensa.getColumnCount();
				
				for (int linha = 0; linha < numLinhasTabelaImportacaoXLS; linha++) {
					String[] linhaPremioERecompensa = new String[4];
					
					System.out.println("numColunasTabelaImportacaoXLS: " + numColunasTabelaImportacaoXLS);
					for (int coluna = 0; coluna < numColunasTabelaImportacaoXLS; coluna++) 
					{
						if (coluna == 0) {
							linhaPremioERecompensa[0] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 0);
							System.out.println("linhaPremioERecompensa[0]: " + linhaPremioERecompensa[0]);
						} else if (coluna == 1) {
							linhaPremioERecompensa[1] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 1);
							System.out.println("linhaPremioERecompensa[1]: " + linhaPremioERecompensa[1]);
						} else if(coluna == 2) {
							linhaPremioERecompensa[2] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 2);
							LeitorExcel.insertValueIntoColumn1(linhaPremioERecompensa[2]);
							System.out.println("linhaPremioERecompensa[2]: " + linhaPremioERecompensa[2]);
						} else if(coluna == 3) {
							linhaPremioERecompensa[3] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 3);
							LeitorExcel.insertValueIntoColumn2(linhaPremioERecompensa[3]);
							System.out.println("linhaPremioERecompensa[3]: " + linhaPremioERecompensa[3]);
						}
					}
				}
				File caminhoArquivo = new File(arquivoCaminhoField.getText());
				System.out.println(LeitorExcel.getArquivo().getName());			
			}
		});
		
		exportarDadosButton.setBounds(300, 348, 320, 30);
		frame.getContentPane().add(exportarDadosButton);

		botaoImportarXLS.setBounds(479, 63, 150, 40);
		frame.getContentPane().add(botaoImportarXLS);

		arquivoCaminhoField = new JTextField();
		arquivoCaminhoField.setBounds(50, 71, 391, 24);
		frame.getContentPane().add(arquivoCaminhoField);
		arquivoCaminhoField.setColumns(10);

		lblNewLabel = new JLabel("Arquivo:");
		lblNewLabel.setBounds(50, 45, 50, 15);
		frame.getContentPane().add(lblNewLabel);

		/// Dados XLS Label
		labelDadosXLS = new JLabel("Dados do XLS:");
		labelDadosXLS.setBounds(50, 120, 85, 15);
		frame.getContentPane().add(labelDadosXLS);

		/// Panel Tabela Dados XLS
		panelTabelaDadosXLS = new JPanel();
		panelTabelaDadosXLS.setBounds(50, 150, 215, 160);
		panelTabelaDadosXLS.setLayout(new BorderLayout());
		frame.getContentPane().add(panelTabelaDadosXLS);

		tableModel = new DefaultTableModel(new Object[] { "Rede", "Torneio" }, 0);
		tabelaRedeETorneio = new JTable(tableModel);
		scrollPane = new JScrollPane(tabelaRedeETorneio);
		panelTabelaDadosXLS.add(scrollPane, BorderLayout.CENTER);

		// Panel Tabela Dados Retorno da Pagina
		JPanel panelTabelaDadosRetornoPagina = new JPanel();
		panelTabelaDadosRetornoPagina.setBounds(300, 150, 320, 160);
		panelTabelaDadosRetornoPagina.setLayout(new BorderLayout());
		frame.getContentPane().add(panelTabelaDadosRetornoPagina);

		tableModelDadosDaWeb = new DefaultTableModel(new Object[] { "Rede", "Torneio", "Prêmio", "Recompensa" }, 0);
		tabelaRedeTorneioPremioRecompensa = new JTable(tableModelDadosDaWeb);
		scrollPane2 = new JScrollPane(tabelaRedeTorneioPremioRecompensa);
		panelTabelaDadosRetornoPagina.add(scrollPane2, BorderLayout.CENTER);
	}

	private void atualizaTableModelRedeETorneio(List<String[]> listaDeTorneiosERedes) {
		tableModel.setRowCount(0); // limpa a tabela
		for (String[] listaTorneioERede : listaDeTorneiosERedes) {
			tableModel.addRow(new Object[] { listaTorneioERede[0], listaTorneioERede[1] });
		}
	}

	private void atualizaTableModelRedeTorneioPremioERecompensa(List<String[]> listaDeTorneiosERedes) {
		tableModelDadosDaWeb.setRowCount(0); // limpa a tabela
		for (String[] listaTorneioERede : listaDeTorneiosERedes) {
			tableModelDadosDaWeb.addRow(new Object[] { listaTorneioERede[0], listaTorneioERede[1], listaTorneioERede[2],
					listaTorneioERede[3] });
		}
	}

	public void aguardeSegundos(Integer segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
