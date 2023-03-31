package br.com.jayybe.view;

import java.awt.Component;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ConfiguracaoBotaoImportacao {

	public JButton configuraBotaoImportacaoArquivoExcel(JButton importarExcelBtn) {
		try {
		importarExcelBtn.setLayout(new BoxLayout(importarExcelBtn, BoxLayout.Y_AXIS)); // Define o layout personalizado

		// Cria o ícone e redimensiona para 30x30 pixels
		ImageIcon icon;
		icon = new ImageIcon(getClass().getResource("/excel.jpg"));
		Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel iconLabel = new JLabel(new ImageIcon(img));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente a imagem

		// Cria um rótulo para o texto e define o alinhamento
		JLabel textLabel = new JLabel("Importar Excel");
		textLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		textLabel.setAlignmentY(Component.TOP_ALIGNMENT); // Centraliza verticalmente o texto
		textLabel.setBorder(BorderFactory.createEmptyBorder(1,0,0,0)); 

		// Adiciona o ícone e o rótulo de texto ao botão		
		importarExcelBtn.add(iconLabel);
		importarExcelBtn.add(Box.createVerticalStrut(0)); // Adiciona uma folga vertical entre a imagem e o texto
		importarExcelBtn.add(textLabel);		
		
		importarExcelBtn.setBounds(376, 37, 117, 85);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return importarExcelBtn;
	}		
	
}
