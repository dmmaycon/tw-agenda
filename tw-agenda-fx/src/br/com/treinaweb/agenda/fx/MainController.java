package br.com.treinaweb.agenda.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.Optional;
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
	
	private Boolean ehInserir;
	
	private Contato contatoSelecionado;
	
	public void botaoInserir_Action() {
		this.ehInserir = true;
		this.txtfNome.setText("");
		this.txtfIdade.setText("");
		this.txtfTelefone.setText("");
		habilitarEdicaoAgenda(true);
	}
	
	public void botaoAlterar_Action() {
		habilitarEdicaoAgenda(true);
		this.ehInserir = false;
		this.txtfNome.setText(this.contatoSelecionado.getNome());
		this.txtfIdade.setText(Integer.toString(this.contatoSelecionado.getIdade()));
		this.txtfTelefone.setText(this.contatoSelecionado.getTelefone());
	}
	
	public void botaoCancelar_Action() {
		habilitarEdicaoAgenda(false);
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
	public void botaoExcluir_Action() {
		
		Alert confirmacao = new Alert(AlertType.CONFIRMATION);
		confirmacao.setTitle("Confirma��o");
		confirmacao.setHeaderText("Excluir Contato!");
		confirmacao.setContentText("Deseja realmente excluir o contato?");
		
		Optional<ButtonType> resultadoConfirmacao = confirmacao.showAndWait();
		
		if (resultadoConfirmacao.isPresent() && resultadoConfirmacao.get() == ButtonType.OK) {
			AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
			repositorioContato.excluir(this.contatoSelecionado);
			carregarTabelaContatos();
			this.tabelaContatos.getSelectionModel().selectFirst();
		}
				

	}
	
	public void botaoSalvar_Action() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		Contato contato = new Contato();
		contato.setNome(txtfNome.getText());
		contato.setIdade(Integer.parseInt(txtfIdade.getText()));
		contato.setTelefone(txtfTelefone.getText());
		if (this.ehInserir) {
			repositorioContato.inserir(contato);
		} else {
			repositorioContato.atualizar(contato);
		}
		habilitarEdicaoAgenda(false);
		carregarTabelaContatos();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
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
				this.contatoSelecionado = contatoNovo;
			}
		});
		carregarTabelaContatos();
	}

	
}

