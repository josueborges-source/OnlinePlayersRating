package br.com.jayybe.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.com.jayybe.controller.LeitorExcel;
import br.com.jayybe.controller.SeletorDeArquivos;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.awt.event.ActionEvent;

public class TelaPrincipal4 {

    private JFrame frame;
    private JTextField arquivoCaminhoField;
    private JTable tabelaRedeETorneio;
    private DefaultTableModel tableModel;

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
        frame.setBounds(100, 100, 743, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton botaoImportar = new JButton("Importar...");
        botaoImportar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                File caminhoArquivo = SeletorDeArquivos.mostrarSeletorDeArquivo();

                if (caminhoArquivo != null) {

                    String nomeCaminhoDoArquivo = caminhoArquivo.toString();

                    // Teste 1
                    System.out.println("Nome do Caminho do Arquivo: " + nomeCaminhoDoArquivo);
                    arquivoCaminhoField.setText(nomeCaminhoDoArquivo);

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
        botaoImportar.setBounds(316, 40, 104, 23);
        frame.getContentPane().add(botaoImportar);

        arquivoCaminhoField = new JTextField();
        arquivoCaminhoField.setBounds(50, 74, 370, 20);
        frame.getContentPane().add(arquivoCaminhoField);
        arquivoCaminhoField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Arquivo:");
        lblNewLabel.setBounds(50, 43, 46, 14);
        frame.getContentPane().add(lblNewLabel);

        JPanel panel = new JPanel();
        panel.setBounds(50, 146, 213, 161);
        frame.getContentPane().add(panel);
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Rede", "Torneio"}, 0);
        tabelaRedeETorneio = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaRedeETorneio);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel lblNewLabel_1 = new JLabel("Dados do XLS:");
        lblNewLabel_1.setBounds(50, 121, 83, 14);
        frame.getContentPane().add(lblNewLabel_1);
    }

    private void atualizaTableModel(List<String[]> listaDeTorneiosERedes) {
        tableModel.setRowCount(0); //limpa a tabela
        for (String[] listaTorneioERede : listaDeTorneiosERedes) {
            tableModel.addRow(new Object[]{listaTorneioERede[0], listaTorneioERede[1]});
        }
    }
}
