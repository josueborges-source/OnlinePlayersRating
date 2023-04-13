package br.com.jayybe.controller;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SeletorDeArquivos {
	
	public static File mostrarSeletorDeArquivo() {
		
		/// Configuração da Janela de Seleção
		// Declara variável para arquivo excel a retornar
		File arquivoDoExcelASerRetornado = null;

		// Seta caminho para o Desktop
		File diretorioDesktop = new File(System.getProperty("user.home") + "/Desktop");
		FileNameExtensionFilter filtroDeExtensaoDeArquivo = new FileNameExtensionFilter("Arquivos do Excel", "xls", "xlsx");

		// Objeto janela de escolha de arquivo
		JFileChooser selecaoDeArquivo = new JFileChooser();

		// Insere caminho do Desktop à janela de escolha de arquivo
		selecaoDeArquivo.setCurrentDirectory(diretorioDesktop);

		// Adiciona filtro para arquivos em Excel
		selecaoDeArquivo.setFileFilter(filtroDeExtensaoDeArquivo);

		// Configura para exibir apenas arquivos, não diretórios
		selecaoDeArquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Remove o filtro padrão que exibe "todos os arquivos"
		FileFilter[] filters = selecaoDeArquivo.getChoosableFileFilters();
		for (FileFilter fileFilter : filters) {
			String descricaoTipoDeArquivo = fileFilter.getDescription();
			if (descricaoTipoDeArquivo.equals("Todos os Arquivos") || descricaoTipoDeArquivo.equals("All Files")) {
				selecaoDeArquivo.removeChoosableFileFilter(fileFilter);
			}
		}

		// Retorna 0 caso seja inválido, e 1 caso seja válido
		int resultado = selecaoDeArquivo.showOpenDialog(null);

		// Se o usuário selecionou um arquivo,exibe o caminho do arquivo selecionado
		if (resultado == JFileChooser.APPROVE_OPTION) {
			arquivoDoExcelASerRetornado = selecaoDeArquivo.getSelectedFile();
		}
		return arquivoDoExcelASerRetornado;
	}
}
