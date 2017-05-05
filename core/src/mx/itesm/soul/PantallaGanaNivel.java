package mx.itesm.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
 * Created by Adrian on 18/04/2017.
 */
public class PantallaGanaNivel extends Pantalla {
    private final ColourlessSoul menu;
    private final AssetManager manager;

    private Preferences prefs = Gdx.app.getPreferences("Settings");
    // Música
    private Music heroicMusic;


    //texturas
    private Texture texturaFondo;
    private Texture texturaCreditos;
    private Image imgCredits;
    private boolean vanish = false;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaGanaNivel(ColourlessSoul menu) {
        this.menu = menu;
        manager=menu.getAssetManager();
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();
        Gdx.input.setInputProcessor(new Procesador());
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);
        //Boton
        imgCredits = new Image(texturaCreditos);
        imgCredits.setPosition(0,-ALTO);
        escena.addActor(imgCredits);


        //Cargar audios
        heroicMusic = manager.get("musicSounds/heroicTheme.mp3");

        if(prefs.getBoolean("Music",true))
            heroicMusic.play();

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);

    }

    private void cargarTexturas() {
        texturaFondo = manager.get("FondosPantalla/fondoFinJuego.png");
        texturaCreditos = manager.get("creditos.png");
    }

    @Override
    public void render(float delta) {
        // 60 x seg
        actualizarObjetos(delta);
        if (vanish){
            imgCredits.setColor(1,1,1,imgCredits.getColor().a-0.01f);
            heroicMusic.setVolume(heroicMusic.getVolume()-0.009f);
        }
        if(imgCredits.getColor().a<=0){
            heroicMusic.stop();
            menu.setScreen(new mx.itesm.soul.PantallaMenu(menu));
        }
        borrarPantalla();
        escena.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            vanish = true;
    }

    private void actualizarObjetos(float delta) {
        if(imgCredits.getY()!=0) imgCredits.setY(imgCredits.getY()+1);
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
        manager.unload("FondosPantalla/fondoFinJuego.png");
        manager.unload("creditos.png");
        manager.unload("musicSounds/heroicTheme.mp3");
    }

    private class Procesador implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            vanish = true;
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
