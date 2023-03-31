package br.com.jayybe.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelecaoDeArquivoLocal {
	
	public File retornaArquivoExcelComTabelaDados() {
		
		/// Configuração da Janela de Seleção
		// Declara variável para arquivo excel a retornar
		File arquivoDoExcelASerRetornado = null;

		// Objeto janela de escolha de arquivo
		JFileChooser fileChooser = new JFileChooser();

		// Seta caminho para o Desktop
		File desktopDir = new File(System.getProperty("user.home") + "/Desktop");

		// Insere caminho do Desktop à janela de escolha de arquivo
		fileChooser.setCurrentDirectory(desktopDir);

		// Adiciona filtro para arquivos em Excel
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos do Excel", "xls", "xlsx");
		fileChooser.setFileFilter(filter);

		// Configura para exibir apenas arquivos, não diretórios
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Remove o filtro padrão que exibe "todos os arquivos"
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		for (FileFilter fileFilter : filters) {
			String description = fileFilter.getDescription();
			if (description.equals("Todos os Arquivos") || description.equals("All Files")) {
				fileChooser.removeChoosableFileFilter(fileFilter);
			}
		}

		/// Exibe janela de selecao de arquivos
		// Exibe o JFileChooser em uma janela e aguarda a seleção do usuário
		int result = fileChooser.showOpenDialog(null);

		// Se o usuário selecionou um arquivo,exibe o caminho do arquivo selecionado
		if (result == JFileChooser.APPROVE_OPTION) {
			arquivoDoExcelASerRetornado = fileChooser.getSelectedFile();
			System.out.println("Arquivo selecionado: " + arquivoDoExcelASerRetornado.getAbsolutePath());
		}
		return arquivoDoExcelASerRetornado;
	}

}
