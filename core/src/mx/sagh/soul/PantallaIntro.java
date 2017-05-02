package mx.sagh.soul;

/**
 * Created by Aldo on 01/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PantallaIntro extends Pantalla{
    private final ColourlessSoul menu;
    private final AssetManager manager;
    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));

    //texturas
    private Texture texturaC1, texturaC2, texturaC3, texturaC4;
    private Image imgC1, imgC2, imgC3, imgC4;
    private int touch = 0;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaIntro(ColourlessSoul menu) {
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
        imgC1.setPosition(50,400);
        imgC1.setColor(1,1,1,0);
        imgC1.setScale(0f);

        imgC2 = new Image(texturaC2);
        imgC2.setPosition(50,50);
        imgC2.setColor(1,1,1,0);
        imgC2.setScale(0f);

        imgC3 = new Image(texturaC3);
        imgC3.setPosition(350,50);
        imgC3.setColor(1,1,1,0);
        imgC3.setScale(0f);

        imgC4 = new Image(texturaC4);
        imgC4.setPosition(650,50);
        imgC4.setColor(1,1,1,0);
        imgC4.setScale(0f);

        //Botones
        /*TextureRegionDrawable trdBtnRestart = new TextureRegionDrawable(new TextureRegion(texturaBotonRestart));
        ImageButton btnRestart = new ImageButton(trdBtnRestart);
        btnRestart.setPosition(ANCHO / 3 - 150, ALTO / 3);
        escena.addActor(btnRestart);
        */


        // Evento del boton
        /*btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if (settings.getBoolean("Sounds", true))
                    clickSound.play();
                while (clickSound.isPlaying()) if (clickSound.getPosition() > 0.5f) break;

                switch (currentLevel.getInteger("Nivel", 1)) {
                    case 1:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_1));
                        break;
                    case 2:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_2));
                        break;
                    case 3:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_3));
                        break;
                }
                clickSound.stop();
            }
        });

        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                regresarAMenu();
            }
        });*/

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaC1 = manager.get("IntroHistoria/cuadro1.png");
        texturaC2 = manager.get("IntroHistoria/cuadro2.png");
        texturaC3 = manager.get("IntroHistoria/cuadro3.png");
        texturaC4 = manager.get("IntroHistoria/cuadro4.png");
    }

    /*private void stopMusic(){
        switch (currentLevel.getInteger("Nivel",1)){
            case 1:
                musicLevel1.stop();
                break;
            case 2:
                musicLevel2.stop();
                break;
            case 3:
                musicLevel3.stop();
                break;
        }
    }*/

    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla(0,0.05490f,0.12549f);
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
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
                break;
        }
        escena.draw();
    }


    private void cambiarAlpha(Image img) {
        if(img.getColor().a<1.0f)
            img.setColor(1,1,1,img.getColor().a+0.02f);
        else if(touch!=3) touch++;
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
            if(touch==3) touch++;
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
