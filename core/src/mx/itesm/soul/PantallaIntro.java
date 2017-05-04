package mx.itesm.soul;

/**
 * Created by Aldo on 01/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PantallaIntro extends Pantalla {
    private final mx.itesm.soul.ColourlessSoul menu;
    private final AssetManager manager;

    private EstadoIntro estadoIntro;
    private Sound efectoHoja;

    //Preferencias
    private Preferences prefs = Gdx.app.getPreferences("Settings");


    private Animation<TextureRegion> spriteAnimado1, spriteAnimado2;         // Animación caminando, en reposo y parándose
    private float timerAnimacion1, timerAnimacion2;


    //texturas
    private Texture texturaC1, texturaC2, texturaC3, texturaC4, texturaC5, texturaC6, texturaC7;
    private Texture texturaIntroSprites1, texturaIntroSprites2, texturaIntroSprites3, texturaIntroSprites4;
    private Image imgC1, imgC2, imgC3, imgC4, imgC5, imgC6, imgC7;
    private int touch = 0;

    //Escena
    private Stage escena;
    private SpriteBatch batch;
    private TextureRegion region;

    public PantallaIntro(mx.itesm.soul.ColourlessSoul menu) {
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

    /*private void regresarAMenu(){
        stopMusic();
        if (settings.getBoolean("Sounds", true))
            clickSound.play();
        while (clickSound.isPlaying()) if (clickSound.getPosition() > 0.5f) break;
        menu.setScreen(new PantallaMenu(menu));
        clickSound.stop();
    }*/

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);

        imgC1 = new Image(texturaC1);
        imgC1.setPosition(60,400);
        imgC1.setColor(1,1,1,0);
        imgC1.setScale(0f);

        imgC2 = new Image(texturaC2);
        imgC2.setPosition(670,450);
        imgC2.setColor(1,1,1,0);
        imgC2.setScale(0f);

        imgC3 = new Image(texturaC3);
        imgC3.setPosition(60,50);
        imgC3.setColor(1,1,1,0);
        imgC3.setScale(0f);

        imgC4 = new Image(texturaC4);
        imgC4.setPosition(670,50);
        imgC4.setColor(1,1,1,0);
        imgC4.setScale(0f);

        imgC5 = new Image(texturaC5);
        imgC5.setPosition(60,400);
        imgC5.setColor(1,1,1,0);
        imgC5.setScale(0f);

        imgC6 = new Image(texturaC6);
        imgC6.setPosition(60,50);
        imgC6.setColor(1,1,1,0);
        imgC6.setScale(0f);

        imgC7 = new Image(texturaC7);
        imgC7.setPosition(670,50);
        imgC7.setColor(1,1,1,0);
        imgC7.setScale(0f);

        TextureRegion texturaCompleta1 = new TextureRegion(texturaIntroSprites1);
        TextureRegion[][] texturaAnimada1 = texturaCompleta1.split(1280,800);
        TextureRegion texturaCompleta2 = new TextureRegion(texturaIntroSprites2);
        TextureRegion[][] texturaAnimada2 = texturaCompleta2.split(1280,800);
        spriteAnimado1 = new Animation(0.1f, texturaAnimada1[0][0], texturaAnimada1[0][1], texturaAnimada2[0][0], texturaAnimada2[0][1]);

        TextureRegion texturaCompleta3 = new TextureRegion(texturaIntroSprites3);
        TextureRegion[][] texturaAnimada3 = texturaCompleta3.split(1280,800);
        TextureRegion texturaCompleta4 = new TextureRegion(texturaIntroSprites4);
        TextureRegion[][] texturaAnimada4 = texturaCompleta4.split(1280,800);
        spriteAnimado2 = new Animation(0.1f, texturaAnimada3[0][0], texturaAnimada3[0][1], texturaAnimada4[0][0], texturaAnimada4[0][1]);

        //Cargar audios
        manager.load("FondosTutorial/turnPage.mp3",Sound.class);
        manager.finishLoading();
        efectoHoja = manager.get("FondosTutorial/turnPage.mp3");
        estadoIntro = EstadoIntro.ESTATICO;
        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaC1 = manager.get("IntroHistoria/cuadro1.png");
        texturaC2 = manager.get("IntroHistoria/cuadro2.png");
        texturaC3 = manager.get("IntroHistoria/cuadro3.png");
        texturaC4 = manager.get("IntroHistoria/cuadro4.png");
        texturaC5 = manager.get("IntroHistoria/cuadro5.png");
        texturaC6 = manager.get("IntroHistoria/cuadro6.png");
        texturaC7 = manager.get("IntroHistoria/cuadro7.png");
        texturaIntroSprites1 = manager.get("IntroHistoria/introSprites1.png");
        texturaIntroSprites2 = manager.get("IntroHistoria/introSprites2.png");
        texturaIntroSprites3 = manager.get("IntroHistoria/introSprites3.png");
        texturaIntroSprites4 = manager.get("IntroHistoria/introSprites4.png");
    }

    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla(0,0.05490f,0.12549f);
        batch.begin();
        if(estadoIntro == EstadoIntro.CAMBIANDO1) {
            timerAnimacion1 += Gdx.graphics.getDeltaTime();
            region = spriteAnimado1.getKeyFrame(timerAnimacion1);
            batch.draw(region, 0, 0);
            if(spriteAnimado1.getKeyFrameIndex(timerAnimacion1)==3) {
                touch++;
                estadoIntro = EstadoIntro.ESTATICO;
            }
        }
        if(estadoIntro == EstadoIntro.CAMBIANDO2) {
            timerAnimacion2 += Gdx.graphics.getDeltaTime();
            region = spriteAnimado2.getKeyFrame(timerAnimacion2);
            batch.draw(region, 0, 0);
            if(spriteAnimado2.getKeyFrameIndex(timerAnimacion2)==3) {
                touch++;
                estadoIntro = EstadoIntro.ESTATICO;
                menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.MENU));
            }
        }
        batch.end();
        escena.draw();


        switch(touch){
            case 0:
                escena.addActor(imgC1);
                cambiarAlpha(imgC1);
                break;
            case 1:
                escena.addActor(imgC2);
                cambiarAlpha(imgC2);
                break;
            case 2:
                escena.addActor(imgC3);
                cambiarAlpha(imgC3);
                break;
            case 3:
                escena.addActor(imgC4);
                cambiarAlpha(imgC4);
                break;
            case 4:
                imgC1.remove();
                imgC2.remove();
                imgC3.remove();
                imgC4.remove();
                if(prefs.getBoolean("Sounds",true) && estadoIntro==EstadoIntro.ESTATICO)
                    efectoHoja.play();
                estadoIntro = EstadoIntro.CAMBIANDO1;
                break;
            case 5:
                escena.addActor(imgC5);
                cambiarAlpha(imgC5);
                break;
            case 6:
                escena.addActor(imgC6);
                cambiarAlpha(imgC6);
                break;
            case 7:
                escena.addActor(imgC7);
                cambiarAlpha(imgC7);
                break;
            case 8:
                imgC5.remove();
                imgC6.remove();
                imgC7.remove();
                if(prefs.getBoolean("Sounds",true) && estadoIntro==EstadoIntro.ESTATICO)
                    efectoHoja.play();
                estadoIntro = EstadoIntro.CAMBIANDO2;
        }
        escena.draw();
    }


    private void cambiarAlpha(Image img) {
        if(img.getColor().a<1.0f)
            img.setColor(1,1,1,img.getColor().a+0.02f);
        else if(touch!=3 && touch!=7) touch++;
        if(img.getScaleX()<1.0f)
            img.scaleBy(0.02f);
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
        manager.unload("IntroHistoria/cuadro1.png");
        manager.unload("IntroHistoria/cuadro2.png");
        manager.unload("IntroHistoria/cuadro3.png");
        manager.unload("IntroHistoria/cuadro4.png");
        manager.unload("IntroHistoria/cuadro5.png");
        manager.unload("IntroHistoria/cuadro6.png");
        manager.unload("IntroHistoria/cuadro7.png");
        manager.unload("IntroHistoria/introSprites1.png");
        manager.unload("IntroHistoria/introSprites2.png");
        manager.unload("IntroHistoria/introSprites3.png");
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
            if(touch==3 || touch==7) touch++;

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

    private enum EstadoIntro {
        ESTATICO,
        CAMBIANDO1,
        CAMBIANDO2
    }

}
