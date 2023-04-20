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
		frame.setBounds(100, 100, 912, 531);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		botaoImportarXLS = new JButton("< ImportarXLS");

		/// Importar Dados Botão
		importarDadosWebButton = new JButton("Importar Dados Web >");

		importarDadosWebButton.setBounds(307, 150, 175, 40);
		frame.getContentPane().add(importarDadosWebButton);

		botaoImportarXLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// 1: Retorna Caminho do Arquivo via Janela
				File caminhoArquivo = SeletorDeArquivos.mostrarSeletorDeArquivo();

				// Se caminho do arquivo foi localizado
				if (caminhoArquivo != null) {

					String nomeCaminhoDoArquivo = caminhoArquivo.toString();
					// Teste 1 (Caminho do Arquivo via Janela)
					System.out.println("Nome do Caminho do Arquivo: " + nomeCaminhoDoArquivo);

					// 2: Atribui nome do arquivo para o textfield
					arquivoCaminhoField.setText(nomeCaminhoDoArquivo);

					// Teste 2 (Atribui nome do arquivo para o textfield)
					System.out.println("Valor do texto do textfield: " + arquivoCaminhoField.getText());

					// 3: Ler campos de torneio e rede do arquivo excel
					List<String[]> listaDeTorneiosERedes = LeitorExcel.lerTorneiosERedesExcel(caminhoArquivo);

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
					valoresDaLinha.add(linhaPremioERecompensa);
					String urlDoTorneioERede = Util.retornarURLAPartirDeArquivoDadosDoTorneio(linhaPremioERecompensa);

					WebDriverUtil webDriverUtil = new WebDriverUtil();
					String codigoHtml = webDriverUtil.getHtml(urlDoTorneioERede);

					aguardeSegundos(20);

					String[] valoresDaTabelaElements;
					valoresDaTabelaElements = HTMLUtils.encontrarElementosComIdJqg(codigoHtml);
					
					for (String valorDaTabelaElement : valoresDaTabelaElements) {
						System.out.println(valorDaTabelaElement);
					}

					// TODO: Refatorar
					System.out.println("Abrindo Página " + (linha + 1) + ": " + urlDoTorneioERede);
					System.out.println("Tempo Inicio: " + System.currentTimeMillis() / 1000);

					aguardeSegundos(20);

					// TODO: Refatorar
					System.out.println("Tempo Termino: " + System.currentTimeMillis() / 1000);

					System.out.println();
					// System.out.println(codigoHtmlPagina);
				}
				atualizaTableModelRedeTorneioPremioERecompensa(valoresDaLinha);
			}
		});

		botaoImportarXLS.setBounds(521, 63, 150, 40);
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
		panelTabelaDadosRetornoPagina.setBounds(521, 150, 320, 160);
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
