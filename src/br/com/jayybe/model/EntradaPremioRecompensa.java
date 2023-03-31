package br.com.jayybe.model;

public class EntradaPremioRecompensa {

	private int premio;
	private int recompensa;
	private String nomeDoJogador;
	
	public int getPremioSemRecompensa() {
		return premio - recompensa;
	}	
	public int getPremio() {
		return premio;
	}
	public void setPremio(int premio) {
		this.premio = premio;
	}
	public int getRecompensa() {
		return recompensa;
	}
	public void setRecompensa(int recompensa) {
		this.recompensa = recompensa;
	}
	@Override
	public String toString() {
		return "EntradaPremioRecompensa [premio=" + premio + ", recompensa=" + recompensa + "]";
	}
	public void setJogador(String nomeJogador) {
		this.nomeDoJogador = nomeDoJogador;		
	}	
	public String getJogador(String nomeDoJogador) {
		return this.nomeDoJogador;
	}
}
