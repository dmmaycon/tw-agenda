package br.com.treinaweb.agenda.repositorios;

import java.util.List;

public interface AgendaRepositorio<T> {

	public List<T> selecionar();
	public void inserir(T entidade);
	public void atualizar(T entidade);
	public void excluir(T entidade);
	
}
