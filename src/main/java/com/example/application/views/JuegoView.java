package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route(value = "juego", layout = MainLayout.class)
@PageTitle("Juego")
public class JuegoView extends VerticalLayout {

    private Personaje personaje;
    private Enemigo[] enemigos;
    private TextField vidaPersonaje;
    private TextField vidaEnemigo;
    private TextField armaPersonaje;
    private Button atacar;
    private Button huir;
    private Button salir;
    private int currentEnemyIndex;

    public JuegoView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        vidaPersonaje = new TextField("Vida del Personaje");
        vidaPersonaje.setReadOnly(true);
        vidaEnemigo = new TextField("Vida del Enemigo");
        vidaEnemigo.setReadOnly(true);
        armaPersonaje = new TextField("Arma del Personaje");
        armaPersonaje.setReadOnly(true);

        atacar = new Button("Atacar", event -> atacar());
        huir = new Button("Huir", event -> huir());
        salir = new Button("Salir", event -> getUI().ifPresent(ui -> ui.navigate("")));

        add(new H1("Juego"), vidaPersonaje, armaPersonaje, vidaEnemigo, atacar, huir, salir);
        iniciarJuego();
    }

    private void iniciarJuego() {
        personaje = new Personaje(100, new Arma("Espada", 10), new Arma("Hacha", 15));
        enemigos = new Enemigo[]{
                new Enemigo("Ogro", 20, new Arma("Mazo", 5)),
                new Enemigo("Minotauro", 20, new Arma("Hacha", 7)),
                new Enemigo("Elfo", 20, new Arma("Lanza", 6)),
                new Enemigo("Gigante", 40, new Arma("Arco", 10))
        };
        currentEnemyIndex = 0;
        actualizarVida();
        actualizarArma();
    }

    private void atacar() {
        Enemigo enemigoActual = enemigos[currentEnemyIndex];
        personaje.atacar(enemigoActual);
        actualizarArma();
        if (enemigoActual.getVida() > 0) {
            enemigoActual.atacar(personaje);
        }
        actualizarVida();
        verificarEstadoDelJuego();
    }

    private void huir() {
        gameOver("¡Has huido del juego! GAME OVER");
    }

    private void gameOver(String mensaje) {
        Div gameText = new Div();
        gameText.setText(mensaje);
        gameText.addClassNames("text-l", "m-m");
        add(gameText);
        atacar.setEnabled(false);
        huir.setEnabled(false);
    }

    private void ganarJuego() {
        Div gameText = new Div();
        gameText.setText("¡Has ganado el juego y la medalla de campeón!");
        gameText.addClassNames("text-l", "m-m");
        add(gameText);
        atacar.setEnabled(false);
        huir.setEnabled(false);
    }

    private void actualizarVida() {
        vidaPersonaje.setValue("Vida: " + personaje.getVida());
        vidaEnemigo.setLabel(enemigos[currentEnemyIndex].getNombre() + " - Vida");
        vidaEnemigo.setValue("Vida: " + enemigos[currentEnemyIndex].getVida());
    }

    private void actualizarArma() {
        armaPersonaje.setValue("Arma: " + personaje.getArmaActual().getNombre());
    }

    private void verificarEstadoDelJuego() {
        if (personaje.getVida() <= 0) {
            gameOver("¡Has perdido el juego! GAME OVER");
        } else if (enemigos[currentEnemyIndex].getVida() <= 0) {
            if (currentEnemyIndex < enemigos.length - 1) {
                currentEnemyIndex++;
                actualizarVida();
                actualizarVida();
                actualizarArma();
            } else {
                ganarJuego();
            }
        }
    }
}

class Personaje {
    private int vida;
    private Arma[] armas;
    private int armaActualIndex = 0;

    public Personaje(int vida, Arma... armas) {
        this.vida = vida;
        this.armas = armas;
    }

    public void atacar(Enemigo enemigo) {
        enemigo.recibirDano(armas[armaActualIndex].getDanio());
        armaActualIndex = (armaActualIndex + 1) % armas.length;
    }

    public void recibirDano(int danio) {
        vida -= danio;
        vida = Math.max(vida, 0);
    }

    public int getVida() {
        return vida;
    }

    public Arma getArmaActual() {
        return armas[armaActualIndex];
    }
}

class Enemigo {
    private String nombre;
    private int vida;
    private Arma arma;

    public Enemigo(String nombre, int vida, Arma arma) {
        this.nombre = nombre;
        this.vida = vida;
        this.arma = arma;
    }

    public void atacar(Personaje personaje) {
        personaje.recibirDano(arma.getDanio());
    }

    public void recibirDano(int danio) {
        vida -= danio;
        vida = Math.max(vida, 0);
    }

    public int getVida() {
        return vida;
    }

    public String getNombre() {
        return nombre;
    }
}

class Arma {
    private String nombre;
    private int danio;

    public Arma(String nombre, int danio) {
        this.nombre = nombre;
        this.danio = danio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanio() {
        return danio;
    }
}










