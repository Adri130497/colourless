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
public class PantallaMenu implements Screen {
    private static final float ANCHO = 1280 ;
    private static final float ALTO = 800 ;
    private final colourlessSoul menu;
    // Camara y vista
    private OrthographicCamera camara;
    private Viewport vista;
    //texturas
    private Texture texturaFondoMenu;
    private Texture texturaBotonIniciar;
    //Escena
    private Stage escena;
    private SpriteBatch batch;





    public PantallaMenu(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        crearCamara();
        cargarTexturas();
        crearObjetos();


    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondoMenu);
        escena.addActor(imgFondo);

        //Boton

        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaBotonIniciar));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2, 3*ALTO/4-btnPlay.getHeight()/2);
        escena.addActor(btnPlay);
        // Evento del boton
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaPrincipal(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondoMenu = new Texture("negro.jpg");
        texturaBotonIniciar = new Texture("play-boton-300x300.jpg");
    }

    private void crearCamara() {
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2, ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);

    }

    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();

        escena.draw();


    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(1,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        escena.dispose();
        texturaFondoMenu.dispose();
        texturaBotonIniciar.dispose();
    }
}
