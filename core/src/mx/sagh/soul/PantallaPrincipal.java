package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
public class PantallaPrincipal implements Screen {
    private static final float ANCHO = 1280 ;
    private static final float ALTO = 800 ;
    private final colourlessSoul menu;
    // Camara y vista
    private OrthographicCamera camara;
    private Viewport vista;
    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonPausa;
    //Escena
    private Stage escena;
    private SpriteBatch batch;


    public PantallaPrincipal(colourlessSoul menu) {
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
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Boton

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(0,0);
        escena.addActor(btnBack);
        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("azul.jpg");
        texturaBotonPausa = new Texture("back_button.png");
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }


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
        dispose();    }

    @Override
    public void dispose() {
        escena.dispose();
        texturaFondo.dispose();
        texturaBotonPausa.dispose();
    }
}
