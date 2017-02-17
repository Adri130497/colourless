package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by User on 17/02/2017.
 */

public class PantallaExtras extends Pantalla {
    private final colourlessSoul menu;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonLogros;
    private Texture texturaBotonAcercaDe;
    private Texture texturaBotonHowToPlay;
    private Texture texturaBotonRetorno;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaExtras(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Boton

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(0,0);
        escena.addActor(btnBack);

        TextureRegionDrawable trdBtnAchievements = new TextureRegionDrawable(new TextureRegion(texturaBotonLogros));
        ImageButton btnAchievements = new ImageButton(trdBtnAchievements);
        btnAchievements.setPosition(ANCHO/3, 2*ALTO/3-btnAchievements.getHeight());
        escena.addActor(btnAchievements);

        TextureRegionDrawable trdBtnCredits = new TextureRegionDrawable(new TextureRegion(texturaBotonAcercaDe));
        ImageButton btnCredits = new ImageButton(trdBtnCredits);
        btnCredits.setPosition(ANCHO/3, 2*ALTO/3-2*btnCredits.getHeight());
        escena.addActor(btnCredits);

        TextureRegionDrawable trdBtnHowToPlay = new TextureRegionDrawable(new TextureRegion(texturaBotonHowToPlay));
        ImageButton btnHowToPlay = new ImageButton(trdBtnHowToPlay);
        btnHowToPlay.setPosition(ANCHO/3, 2*ALTO/3-3*btnHowToPlay.getHeight());
        escena.addActor(btnHowToPlay);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        btnAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "Hiciste click en Achievements");
                menu.setScreen(new PantallaLogros(menu));
            }
        });

        btnCredits.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Credits");
                menu.setScreen(new PantallaCreditos(menu));
            }
        });

        btnHowToPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en How To Play");
                //menu.setScreen(new PantallaLogros(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaBotonLogros = new Texture("achievementsButton.png");
        texturaBotonAcercaDe = new Texture("creditsButton.png");
        texturaBotonHowToPlay = new Texture("howToButton.png");
        texturaBotonRetorno = new Texture("backButton.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            menu.setScreen(new PantallaMenu(menu));
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        escena.dispose();
        texturaFondo.dispose();
        texturaBotonLogros.dispose();
        texturaBotonAcercaDe.dispose();
        texturaBotonHowToPlay.dispose();
        texturaBotonRetorno.dispose();
    }
}