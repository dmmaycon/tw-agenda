package br.com.treinaweb.agenda.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import br.com.treinaweb.agenda.entidades.Contato;
import br.com.treinaweb.agenda.repositorios.AgendaRepositorio;
import br.com.treinaweb.agenda.repositorios.impl.ContatoRepositorio;

public class MainController implements Initializable{
	
	@FXML
	private TableView<Contato> tabelaContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAlterar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private TextField txtfNome;
	@FXML
	private TextField txtfIdade;
	@FXML
	private TextField txtfTelefone;
	@FXML
	private Button botaoCancelar;
	@FXML
	private Button botaoSalvar;
	
	
	private void carregarTabelaContatos() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		List<Contato> contatos = repositorioContato.selecionar();
		if (contatos.isEmpty()) {
			Contato c = new Contato();
			c.setNome("Teste");
			c.setIdade(20);
			c.setTelefone("33669988");
			contatos.add(c);
		}
		ObservableList<Contato> contatoObservableList = FXCollections.observableArrayList(contatos);
		this.tabelaContatos.getItems().setAll(contatoObservableList);
	}
	
	
	private void habilitarEdicaoAgenda(Boolean edicaoEstaHabilitada) {
		this.txtfNome.setDisable(!edicaoEstaHabilitada);
		this.txtfIdade.setDisable(!edicaoEstaHabilitada);
		this.txtfTelefone.setDisable(!edicaoEstaHabilitada);
		this.botaoCancelar.setDisable(!edicaoEstaHabilitada);
		this.botaoSalvar.setDisable(!edicaoEstaHabilitada);
		
		this.botaoInserir.setDisable(edicaoEstaHabilitada);
		this.botaoAlterar.setDisable(edicaoEstaHabilitada);
		this.botaoExcluir.setDisable(edicaoEstaHabilitada);
		this.tabelaContatos.setDisable(edicaoEstaHabilitada);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		habilitarEdicaoAgenda(false);
		
		// maneira antiga
//		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contato>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Contato> observable, Contato oldValue, Contato newValue) {
//				if(newValue != null) {				
//					txtfNome.setText(newValue.getNome());
//					txtfIdade.setText(String.valueOf(newValue.getIdade()));
//					txtfTelefone.setText(newValue.getTelefone());
//				}
//			}
//		});
		
		// interface funcional JAVA 9 >=
		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener((observable, contatoAntigo, contatoNovo) -> {
			if(contatoNovo != null) {				
				txtfNome.setText(contatoNovo.getNome());
				txtfIdade.setText(String.valueOf(contatoNovo.getIdade()));
				txtfTelefone.setText(contatoNovo.getTelefone());
			}
		});
		carregarTabelaContatos();
	}

	
}

