package br.com.jayybe.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class TelaPrincipal4 {

	public static JLabel cardsLabel = new JLabel("");
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
	private JLabel tituloStatusLabel = new JLabel("Status:");
	private static JLabel statusLabel = new JLabel("Aguardando Comandos");
	private JButton exportarDadosButton = new JButton("Exportar Dados XLS");
	private Border border;

	static public Timer timer;
	static StringBuilder textoFinal;
	static private JLabel carregandoLabel = new JLabel();

	/**
	 * Inicia a Aplicação
	 */
	public static void main(String[] args) {
		
		try {
		    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            javax.swing.UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
		    java.util.logging.Logger.getLogger(TelaPrincipal4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		
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
	 * Cria a Aplicação
	 */
	public TelaPrincipal4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();

		JPanel painel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image imagem = new ImageIcon(getClass().getResource("/resource/blackgrunge.jpg")).getImage();
				g.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
			}
		};
		painel.setForeground(new Color(0, 0, 0));
		frame.setContentPane(painel);

		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(0, 0, 836, 524);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		botaoImportarXLS = new JButton("Importar XLS");
		botaoImportarXLS.setBackground(Color.BLACK);
		botaoImportarXLS.setForeground(Color.WHITE);

		/// Importar Dados Botão
		importarDadosWebButton = new JButton("Importar Dados Web");
		importarDadosWebButton.setBackground(Color.BLACK);
		importarDadosWebButton.setForeground(Color.WHITE);
		
		importarDadosWebButton.setEnabled(false);

		importarDadosWebButton.setBounds(50, 348, 215, 24);
		
		importarDadosWebButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {

						TelaPrincipal4.atualizarStatusLabel("Importando Dados do Arquivo",
								Seletor.TRANSFORMACAO_EM_MAISCULO);

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

							String urlDoTorneioERede = Util
									.retornarURLAPartirDeArquivoDadosDoTorneio(linhaPremioERecompensa);

							WebDriverUtil webDriverUtil = new WebDriverUtil();

							String codigoHtml = webDriverUtil.getHtml(urlDoTorneioERede);

							aguardeSegundos(20);

							String[] valoresDaTabelaElements;
							valoresDaTabelaElements = HTMLUtils.encontrarElementosComIdJqg(codigoHtml);

							String[][] listaValorPremioRecompensa = HTMLUtils
									.retornaValorPremioRecompensa(valoresDaTabelaElements);

							Double premioTotal = Double.valueOf(0);
							Double recompensaTotal = Double.valueOf(0);

							for (int i = 0; i < listaValorPremioRecompensa.length; i++) {
								System.out.println("Nome: " + listaValorPremioRecompensa[i][0]);

								if (listaValorPremioRecompensa[i][1] != "") {
									Double premio = Util
											.valorNaoFormatadoParaDinheiro(listaValorPremioRecompensa[i][1]);
									premioTotal = Double.sum(premioTotal, premio);
								}
								if (listaValorPremioRecompensa[i][2] != "") {
									Double recompensa = Util
											.valorNaoFormatadoParaDinheiro(listaValorPremioRecompensa[i][2]);
									recompensaTotal = Double.sum(recompensaTotal, recompensa);
								}
							}

							aguardeSegundos(20);

							DecimalFormat df = new DecimalFormat("0.00");

							linhaPremioERecompensa[2] = df.format(premioTotal).toString();
							linhaPremioERecompensa[3] = df.format(recompensaTotal).toString();

							valoresDaLinha.add(linhaPremioERecompensa);
						}
						importarDadosWebButton.setEnabled(true);
						botaoImportarXLS.setEnabled(true);
						exportarDadosButton.setEnabled(true);

						TelaPrincipal4.atualizarStatusLabel("Aguardando Comandos", Seletor.TRES_PONTOS);
						System.out.println(LeitorExcel.getArquivo());
						atualizaTableModelRedeTorneioPremioERecompensa(valoresDaLinha);
						return null;
					}

					@Override
					protected void done() {
						// Atualizar a interface do usuário aqui
					}
				};
				TelaPrincipal4.atualizarStatusLabel("Aguardando Comandos", Seletor.TRES_PONTOS);
				worker.execute();
			}
		});
		
		importarDadosWebButton.setBackground(Color.BLACK);		
				
		botaoImportarXLS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				acaoDoInicioDoBotaoImportar();
				
				TelaPrincipal4.atualizarStatusLabel("Aguardando Comandos", Seletor.TRES_PONTOS);
				
				
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
					
					// System.out.println("LeitorArquivoExcel: " + LeitorExcel.getArquivo().getName());
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
				
				acaoAposImportarBotoes();				
				
				TelaPrincipal4.atualizarStatusLabel("Aguardando Comandos", Seletor.TRES_PONTOS);
				
			}			
		});
		
		botaoImportarXLS.setBounds(480, 72, 140, 24);
		
		frame.getContentPane().add(importarDadosWebButton);
		TelaPrincipal4.atualizarStatusLabel("Aguardando Comandos", Seletor.TRES_PONTOS);
		

		exportarDadosButton.setEnabled(false);
		exportarDadosButton.setBackground(Color.BLACK);
		exportarDadosButton.setForeground(Color.WHITE);
		
		exportarDadosButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int numLinhasTabelaImportacaoXLS = tabelaRedeTorneioPremioRecompensa.getRowCount();
				int numColunasTabelaImportacaoXLS = tabelaRedeTorneioPremioRecompensa.getColumnCount();

				for (int linha = 0; linha < numLinhasTabelaImportacaoXLS; linha++) {
					String[] linhaPremioERecompensa = new String[4];

					System.out.println("numColunasTabelaImportacaoXLS: " + numColunasTabelaImportacaoXLS);
					for (int coluna = 0; coluna < numColunasTabelaImportacaoXLS; coluna++) {
						if (coluna == 0) {
							linhaPremioERecompensa[0] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 0);
							System.out.println("linhaPremioERecompensa[0]: " + linhaPremioERecompensa[0]);
						} else if (coluna == 1) {
							linhaPremioERecompensa[1] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 1);
							System.out.println("linhaPremioERecompensa[1]: " + linhaPremioERecompensa[1]);
						} else if (coluna == 2) {
							linhaPremioERecompensa[2] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 2);
							LeitorExcel.insereValor(linha + 2, coluna + 1, linhaPremioERecompensa[2]);
							System.out.println("linhaPremioERecompensa[2]: " + linhaPremioERecompensa[2]);
						} else if (coluna == 3) {
							linhaPremioERecompensa[3] = (String) tabelaRedeTorneioPremioRecompensa.getValueAt(linha, 3);
							LeitorExcel.insereValor(linha + 2, coluna + 1, linhaPremioERecompensa[3]);
							System.out.println("linhaPremioERecompensa[3]: " + linhaPremioERecompensa[3]);
						}
					}
				}
				System.out.println(LeitorExcel.getArquivo().getName());
			}
		});

		exportarDadosButton.setBackground(Color.BLACK);
		
		exportarDadosButton.setBounds(300, 348, 320, 24);
		
		frame.getContentPane().add(exportarDadosButton);		
		frame.getContentPane().add(botaoImportarXLS);

		arquivoCaminhoField = new JTextField();
		arquivoCaminhoField.setForeground(Color.WHITE);		
		arquivoCaminhoField.setBackground(Color.BLACK);
		arquivoCaminhoField.setBounds(50, 71, 391, 24);
		frame.getContentPane().add(arquivoCaminhoField);
		arquivoCaminhoField.setColumns(10);

		lblNewLabel = new JLabel("Arquivo:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(50, 45, 50, 15);
		frame.getContentPane().add(lblNewLabel);

		/// Dados XLS Label
		labelDadosXLS = new JLabel("Dados do XLS:");
		labelDadosXLS.setForeground(Color.WHITE);
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
		scrollPane.setEnabled(false);
		panelTabelaDadosXLS.add(scrollPane, BorderLayout.CENTER);

		// Panel Tabela Dados Retorno da Pagina
		JPanel panelTabelaDadosRetornoPagina = new JPanel();
		panelTabelaDadosRetornoPagina.setBounds(300, 150, 320, 160);
		panelTabelaDadosRetornoPagina.setLayout(new BorderLayout());
		frame.getContentPane().add(panelTabelaDadosRetornoPagina);

		tableModelDadosDaWeb = new DefaultTableModel(new Object[] { "Rede", "Torneio", "Prêmio", "Recompensa" }, 0);
		tabelaRedeTorneioPremioRecompensa = new JTable(tableModelDadosDaWeb);
		scrollPane2 = new JScrollPane(tabelaRedeTorneioPremioRecompensa);
		scrollPane2.setEnabled(false);
		panelTabelaDadosRetornoPagina.add(scrollPane2, BorderLayout.CENTER);
		tituloStatusLabel.setForeground(new Color(255, 255, 255));

		tituloStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		tituloStatusLabel.setBounds(300, 120, 46, 14);
		frame.getContentPane().add(tituloStatusLabel);
		statusLabel.setForeground(new Color(255, 255, 255));
		statusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));

		statusLabel.setBounds(345, 120, 275, 14);
		frame.getContentPane().add(statusLabel);
		ImageIcon imageIcon = new ImageIcon(TelaPrincipal4.class.getResource("/resource/loading.gif"));
		Image image = imageIcon.getImage();
		Image novaImagem = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon novoIcone = new ImageIcon(novaImagem);

		imageIcon.setImage(novaImagem);

		border = BorderFactory.createLineBorder(Color.BLACK, 2);

		cardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cardsLabel.setIcon(new ImageIcon(TelaPrincipal4.class.getResource("/resource/giphy.gif")));
		cardsLabel.setBounds(660, 190, 120, 120);

		cardsLabel.setBorder(border);

		frame.getContentPane().add(cardsLabel);

		carregandoLabel = new JLabel("");

		carregandoLabel.setOpaque(false);
		carregandoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		carregandoLabel.setIcon(new ImageIcon(TelaPrincipal4.class.getResource("/resource/loading.gif")));
		carregandoLabel.setBounds(660, 190, 120, 120);

		carregandoLabel.setBorder(border);

		frame.getContentPane().add(carregandoLabel);
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

	private void aguardeSegundos(Integer segundos) {

		try {
			Thread.sleep(segundos * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void acaoDoInicioDoBotaoImportar() {
		importarDadosWebButton.setEnabled(false);
		botaoImportarXLS.setEnabled(false);
		exportarDadosButton.setEnabled(false);				
	}

	public static void atualizarStatusLabel(String texto, Seletor seletor) {

		if (texto.contains("Aguardando")) {
			cardsLabel.setVisible(false);
			carregandoLabel.setVisible(true);
		} else {
			cardsLabel.setVisible(true);
			carregandoLabel.setVisible(false);
		}

		if (timer != null) {
			timer.stop();
		}

		timer = new Timer(250, new ActionListener() {

			StringBuilder textoASerImplementado = new StringBuilder(texto);
			int contagemDoElementoMaiusculo = 0;
			boolean progressao = true;
			boolean tresPontosNaString = false;

			StringBuilder textoFinal = new StringBuilder(texto);

			public void actionPerformed(ActionEvent e) {

				if (seletor.equals(Seletor.TRANSFORMACAO_EM_MAISCULO)) {
					if (progressao) {
						textoASerImplementado = new StringBuilder(
								texto.substring(0, contagemDoElementoMaiusculo).toUpperCase())
								.append(texto.substring(contagemDoElementoMaiusculo));
						statusLabel.setText(textoASerImplementado.toString());
						contagemDoElementoMaiusculo++;
					} else {
						textoASerImplementado = new StringBuilder(
								texto.substring(0, contagemDoElementoMaiusculo).toUpperCase())
								.append(texto.substring(contagemDoElementoMaiusculo));
						statusLabel.setText(textoASerImplementado.toString());
						contagemDoElementoMaiusculo--;
					}
					if (contagemDoElementoMaiusculo == textoASerImplementado.length()) {
						progressao = false;
					} else if (contagemDoElementoMaiusculo == 0) {
						progressao = true;
					}
				} else if (seletor.equals(Seletor.TRES_PONTOS)) {
					timer.setDelay(500);
					if (tresPontosNaString == false) {
						textoFinal.append("   ");
						tresPontosNaString = true;
					}
					if (textoFinal.toString().endsWith("   ")) {
						textoFinal = new StringBuilder(textoFinal.toString().replace("   ", ".  "));
					} else if (textoFinal.toString().endsWith(".  ")) {
						textoFinal = new StringBuilder(textoFinal.toString().replace(".  ", ".. "));
					} else if (textoFinal.toString().endsWith(".. ")) {
						textoFinal = new StringBuilder(textoFinal.toString().replace(".. ", "..."));
					} else if (textoFinal.toString().endsWith("...")) {
						textoFinal = new StringBuilder(textoFinal.toString().replace("...", "   "));
					}
					statusLabel.setText(textoFinal.toString());
				} else if (seletor.equals(Seletor.DINAMICO)) {
					statusLabel.setText(texto);
				} else if (seletor.equals(Seletor.ESTATICO)) {
					statusLabel.setText(texto);
				}

			}
		});
		timer.start();
	}
	
	private void acaoAposImportarBotoes() {
		importarDadosWebButton.setEnabled(true);
		botaoImportarXLS.setEnabled(true);
		exportarDadosButton.setEnabled(false);				
	}

	public enum Seletor {
		TRANSFORMACAO_EM_MAISCULO, TRES_PONTOS, ESTATICO, DINAMICO
	}
}
