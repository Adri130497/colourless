package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by User on 28/03/2017.
 */

public class PantallaTutorial extends Pantalla {
    private final ColourlessSoul menu;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));
    private Sound efectoHoja;

    private Animation<TextureRegion> spriteAnimado;         // Animación caminando, en reposo y parándose
    private float timerAnimacion;

    private EstadoTutorial estadoTutorial;

    //texturas
    private Texture texturaTutorial1;
    private Texture texturaTutorialSprites1;
    private Texture texturaTutorialSprites2;
    private Image imgTutorial1;

    //Escena
    private Stage escena;
    private SpriteBatch batch;
    private TextureRegion region;

    public PantallaTutorial(ColourlessSoul menu) {
        this.menu = menu;
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
        escena.addActor(imgTutorial1);

        TextureRegion texturaCompleta1 = new TextureRegion(texturaTutorialSprites1);
        TextureRegion[][] texturaAnimada1 = texturaCompleta1.split(1280,800);
        TextureRegion texturaCompleta2 = new TextureRegion(texturaTutorialSprites2);
        TextureRegion[][] texturaAnimada2 = texturaCompleta2.split(1280,800);
        spriteAnimado = new Animation(0.1f, texturaAnimada1[0][0], texturaAnimada1[0][1], texturaAnimada1[0][2], texturaAnimada2[0][0], texturaAnimada2[0][1]);

        AssetManager manager = new AssetManager();
        //Cargar audios
        manager.load("FondosTutorial/turnPage.mp3",Sound.class);
        manager.finishLoading();
        efectoHoja = manager.get("FondosTutorial/turnPage.mp3");

        estadoTutorial = EstadoTutorial.ESTATICO;
        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaTutorial1 = new Texture("FondosTutorial/howTo1.png");
        texturaTutorialSprites1 = new Texture("FondosTutorial/howToSprites1.png");
        texturaTutorialSprites2 = new Texture("FondosTutorial/howToSprites2.png");
        imgTutorial1 = new Image(texturaTutorial1);
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        batch.begin();
        if(estadoTutorial == EstadoTutorial.CAMBIANDO) {
            timerAnimacion += Gdx.graphics.getDeltaTime();
            region = spriteAnimado.getKeyFrame(timerAnimacion);
            batch.draw(region, 0, 0);
        }
        batch.end();
        escena.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));

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
        texturaTutorial1.dispose();
        texturaTutorialSprites1.dispose();
        texturaTutorialSprites2.dispose();
        clickSound.dispose();
        clickSound.stop();
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
            if(estadoTutorial == EstadoTutorial.ESTATICO) {
                imgTutorial1.remove();
                estadoTutorial = EstadoTutorial.CAMBIANDO;
                efectoHoja.play();
            }
            else{
                estadoTutorial = EstadoTutorial.ESTATICO;
                Gdx.app.log("clicked","Me hicieron click");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.EXTRAS));
                clickSound.stop();
            }
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

    private enum EstadoTutorial {
        ESTATICO,
        CAMBIANDO
    }
}