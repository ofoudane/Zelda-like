package controler.mainGame;

import controler.Controleur;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.function.Supplier;

/*
 * Cette classe represente d'une part le controleur de Ground.fxml et d'autre part
 * les elements affichables dans la fenetre du jeu et qui sont lu a partir des fichiers fxml
 * represente la couche initiale sur laquelle les autres vues se superposeront
 * Responsabilites : -> Changer totalement les elements de la fenetre
 */

public class GroundControler implements Initializable {


	/*
	 * Variables statiques : 
	 * VIEWPATH : le chemin vers tous les fichiers du package de la vue.
	 * FXMLPATH : le chemin vers le fichier dont GroundControler est un controleur.
	 */

	public final static int ERRORSTATE = 1;
	private static final String VIEWPATH = "vue/";
	public final static String FXMLPATH = "/vue/template/Ground.fxml";
	public final static int DEFAULTWIDTH = 960;
	public final static int DEFAULTHEIGHT = 640;

	//Le stackPane qui contiendra l'ensemble des elements ajoute a  la fenetre.
	@FXML private StackPane mainStackPane; 

	private Stack<Node[]> anchorStack; 
	private Supplier<Void> gameLoopStart;
	private Scene scene;

	public GroundControler() {
		this.anchorStack = new Stack<Node[]>();
	}


	public void initialize(URL arg0, ResourceBundle arg1) {
		this.changeView(Controleur.FXMLMAINMENUPATH);
	}

	/* 
	 * Methode qui retire les elements du stackpane et les stock dans un anchorStack 
	 * permet de charger sur le StackPane une nouvelle Vue
	 */
	public void changeView(String  viewName ){
		ObservableList<Node> children = this.mainStackPane.getChildren();
		Node[] nodeList = new Node[children.size()];
		nodeList = children.toArray(nodeList);
		this.anchorStack.push(nodeList);

		children.clear();
		addElement(viewName);
	}

	/*
	 * Methode qui permet d'ajouter un element au Stackpane 
	 */
	public void addElement(String viewName) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getClassLoader().getResource(VIEWPATH + viewName));
			AnchorPane newView = loader.load();
			newView.setPrefHeight(DEFAULTHEIGHT);
			newView.setPrefWidth(DEFAULTWIDTH);

			SceneLoader sceneLoader = loader.getController();
			sceneLoader.loadScene(this);
			this.mainStackPane.getChildren().add(newView);


		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}	
	
	private boolean loadNodeList(Node[] currentList) {
		boolean loaded = currentList != null;
		if(currentList != null) {
			this.mainStackPane.getChildren().setAll(currentList);
		}
		return loaded;
	}

	public boolean loadParent() {
		Node[] currentList = this.anchorStack.pop();
		return this.loadNodeList(currentList);
	}

	public void removeLast() {
		int lastIndex = this.mainStackPane.getChildren().size() - 1;
		if(lastIndex > 0)
			this.mainStackPane.getChildren().remove(lastIndex);
	}

	public void setScene(Scene scene) {
		if(scene != null)
			this.scene = scene;
		else
			throw new IllegalArgumentException("ERROR IN GROUND ");
	}

	public Scene getScene() {
		return this.scene;
	}

	public void setGameLoopStart(Supplier<Void> gameLoopStart) {
		if(gameLoopStart != null) {
			this.gameLoopStart = gameLoopStart;
		}
	}

	public void startGameLoop() {
		if(this.gameLoopStart != null)
			this.gameLoopStart.get();
	}
}
