package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
 * Created by Adrian on 11/02/2017.
 */
public class PantallaMenu extends Pantalla {
    private final colourlessSoul menu;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));

    //texturas
    private Texture texturaFondoMenu;
    private Texture texturaBotonInicio;
    private Texture texturaBotonCargar;
    private Texture texturaBotonAjustes;
    private Texture texturaBotonExtras;

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
        imgFondo.setSize(1280,800);
        escena.addActor(imgFondo);

        //Botones del menú principal
        TextureRegionDrawable trdBtnStart = new TextureRegionDrawable(new TextureRegion(texturaBotonInicio));
        ImageButton btnStart = new ImageButton(trdBtnStart);
        btnStart.setPosition(2*ANCHO/3, 2*ALTO/3+btnStart.getHeight());
        escena.addActor(btnStart);

        TextureRegionDrawable trdBtnLoad = new TextureRegionDrawable(new TextureRegion(texturaBotonCargar));
        ImageButton btnLoad = new ImageButton(trdBtnLoad);
        btnLoad.setPosition(2*ANCHO/3, 2*ALTO/3-btnLoad.getHeight());
        escena.addActor(btnLoad);

        TextureRegionDrawable trdBtnSettings = new TextureRegionDrawable(new TextureRegion(texturaBotonAjustes));
        ImageButton btnSettings = new ImageButton(trdBtnSettings);
        btnSettings.setPosition(2*ANCHO/3, 2*ALTO/3-3*btnLoad.getHeight());
        escena.addActor(btnSettings);

        TextureRegionDrawable trdBtnExtras = new TextureRegionDrawable(new TextureRegion(texturaBotonExtras));
        ImageButton btnExtras = new ImageButton(trdBtnExtras);
        btnExtras.setPosition(2*ANCHO/3, 2*ALTO/3-5*btnLoad.getHeight());
        escena.addActor(btnExtras);

        // Evento del boton
        btnStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Start");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaPrincipal(menu));
                clickSound.stop();
            }
        });

        btnLoad.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Load");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaPrincipal(menu));
                clickSound.stop();
            }
        });

        btnSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Settings");
                PantallaAjustes.estado = EstadoInvocado.PANTALLA_MENU;
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaAjustes(menu));
                clickSound.stop();
            }
        });

        btnExtras.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Extras");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaExtras(menu));
                clickSound.stop();
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondoMenu = new Texture("fondoMenu.png");
        texturaBotonInicio = new Texture("startButton.png");
        texturaBotonCargar = new Texture("loadButton.png");
        texturaBotonAjustes = new Texture("settingsButton1.png");
        texturaBotonExtras = new Texture("extrasButton.png");
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
        texturaBotonCargar.dispose();
        texturaBotonAjustes.dispose();
        texturaBotonExtras.dispose();
        clickSound.dispose();
    }
}
