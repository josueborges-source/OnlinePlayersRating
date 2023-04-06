package br.com.jayybe.model;

import java.util.ArrayList;

public class DadosTorneioERede {
	
	private Long torneio;
	private String rede;
	
	private ArrayList<EntradaPremioRecompensa> listaDePremiosDaPagina = new ArrayList<EntradaPremioRecompensa>();	
	
	
	public ArrayList<EntradaPremioRecompensa> getListaDePremiosDaPagina() {
		return listaDePremiosDaPagina;
	}

	public void adicionaEntradaPremioRecompensa(EntradaPremioRecompensa entradaPremioRecompensa) {
		listaDePremiosDaPagina.add(entradaPremioRecompensa);
	}

	public Long getTorneio() {
		return torneio;
	}

	public void setTorneio(Long torneio) {
		this.torneio = torneio;
	}

	public String getRede() {
		return rede;
	}

	public void setRede(String rede) {
		this.rede = rede;
	}

	@Override
	public String toString() {
		return "DadosTorneioERede [torneio=" + torneio + ", rede=" + rede + ", listaDePremiosDaPagina="
				+ listaDePremiosDaPagina + "]";
	}	
}
