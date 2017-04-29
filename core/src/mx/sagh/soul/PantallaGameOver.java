package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
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
 * Created by Adrian on 28/03/2017.
 */

public class PantallaGameOver extends Pantalla{
    private final ColourlessSoul menu;
    private final AssetManager manager;
    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));
    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRestart;
    private Texture texturaBotonMenu;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaGameOver(ColourlessSoul menu) {
        this.menu = menu;
        manager=menu.getAssetManager();
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

        //Botones
        TextureRegionDrawable trdBtnRestart = new TextureRegionDrawable(new TextureRegion(texturaBotonRestart));
        ImageButton btnRestart = new ImageButton(trdBtnRestart);
        btnRestart.setPosition(ANCHO/3-150,ALTO/4);
        escena.addActor(btnRestart);

        TextureRegionDrawable trdBtnMain = new TextureRegionDrawable(new TextureRegion(texturaBotonMenu));
        ImageButton btnMenu = new ImageButton(trdBtnMain);
        btnMenu.setPosition(ANCHO/2+150,ALTO/4);
        escena.addActor(btnMenu);

        // Evento del boton
        btnRestart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaPrincipal.musicLevel.stop();
                if(PantallaAjustes.prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_1));
                clickSound.stop();
            }
        });

        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaPrincipal.musicLevel.stop();
                if(PantallaAjustes.prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
                clickSound.stop();
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);



    }

    private void cargarTexturas() {
        texturaFondo = manager.get("GameOverGris.jpg");
        texturaBotonRestart = manager.get("restartButton.png");
        texturaBotonMenu = manager.get("mainMenuButton.png");



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
        manager.unload("GameOverGris.jpg");
        manager.unload("mainMenuButton.png");
        manager.unload("restartButton.png");


        clickSound.dispose();
        clickSound.stop();

    }

}
