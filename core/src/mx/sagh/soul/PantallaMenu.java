package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Adrian on 11/02/2017.
 */
public class PantallaMenu extends Pantalla {
    private final colourlessSoul menu;

    //texturas
    private Texture texturaFondoMenu;
    private Texture texturaBotonInicio;
    private Texture texturaBotonLogros;
    private Texture texturaBotonCargar;
    private Texture texturaBotonAjustes;

    //Escena
    private Stage escena;
    private SpriteBatch batch;


    public PantallaMenu(colourlessSoul menu) {
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
        Image imgFondo = new Image(texturaFondoMenu);
        escena.addActor(imgFondo);

        //Botones del menú principal
        TextureRegionDrawable trdBtnSettings = new TextureRegionDrawable(new TextureRegion(texturaBotonAjustes));
        ImageButton btnSettings = new ImageButton(trdBtnSettings);
        btnSettings.scaleBy(0.25f);
        btnSettings.setPosition(ANCHO-btnSettings.getWidth(), ALTO-btnSettings.getHeight());
        escena.addActor(btnSettings);

        TextureRegionDrawable trdBtnStart = new TextureRegionDrawable(new TextureRegion(texturaBotonInicio));
        ImageButton btnStart = new ImageButton(trdBtnStart);
        btnStart.setPosition(2*ANCHO/3, 2*ALTO/3-btnStart.getHeight());
        escena.addActor(btnStart);

        TextureRegionDrawable trdBtnAchievs = new TextureRegionDrawable(new TextureRegion(texturaBotonLogros));
        ImageButton btnAchievs = new ImageButton(trdBtnAchievs);
        btnAchievs.setPosition(2*ANCHO/3, 2*ALTO/3-2*btnAchievs.getHeight());
        escena.addActor(btnAchievs);

        TextureRegionDrawable trdBtnLoad = new TextureRegionDrawable(new TextureRegion(texturaBotonCargar));
        ImageButton btnLoad = new ImageButton(trdBtnLoad);
        btnLoad.setPosition(2*ANCHO/3, 2*ALTO/3-3*btnLoad.getHeight());
        escena.addActor(btnLoad);

        // Evento del boton
        btnStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Start");
                menu.setScreen(new PantallaPrincipal(menu));
            }
        });

        btnLoad.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Load");
                //menu.setScreen(new PantallaPrincipal(menu));
            }
        });

        btnAchievs.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Achievements");
                menu.setScreen(new PantallaLogros(menu));
            }
        });

        btnSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Settings");
                //menu.setScreen(new PantallaPrincipal(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondoMenu = new Texture("fondoMenu.jpg");
        texturaBotonInicio = new Texture("startButton.png");
        texturaBotonLogros = new Texture("achievsButton.png");
        texturaBotonCargar = new Texture("loadButton.png");
        texturaBotonAjustes = new Texture("settingsButton.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
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
        texturaFondoMenu.dispose();
        texturaBotonInicio.dispose();
        texturaBotonLogros.dispose();
        texturaBotonCargar.dispose();
    }
}
