package mx.itesm.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import static mx.itesm.soul.ColourlessSoul.clickSound;
import static mx.itesm.soul.PantallaPrincipal.musicLevel1;
import static mx.itesm.soul.PantallaPrincipal.musicLevel2;
import static mx.itesm.soul.PantallaPrincipal.musicLevel3;

/**
 * Created by Adrian on 28/03/2017.
 */

public class PantallaGameOver extends Pantalla {
    private final mx.itesm.soul.ColourlessSoul menu;
    private final AssetManager manager;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRestart;
    private Texture texturaBotonMenu;

    private Preferences currentLevel = Gdx.app.getPreferences("CurrentLevel");
    private Preferences settings = Gdx.app.getPreferences("Settings");

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaGameOver(mx.itesm.soul.ColourlessSoul menu) {
        this.menu = menu;
        manager=menu.getAssetManager();
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();

    }

    private void regresarAMenu(){
        stopMusic();
        if (settings.getBoolean("Sounds", true))
            clickSound.play();
        menu.setScreen(new mx.itesm.soul.PantallaMenu(menu));
        clickSound.stop();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Botones
        TextureRegionDrawable trdBtnRestart = new TextureRegionDrawable(new TextureRegion(texturaBotonRestart));
        ImageButton btnRestart = new ImageButton(trdBtnRestart);
        btnRestart.setPosition(ANCHO / 3 - 150, ALTO / 3);
        escena.addActor(btnRestart);

        TextureRegionDrawable trdBtnMain = new TextureRegionDrawable(new TextureRegion(texturaBotonMenu));
        ImageButton btnMenu = new ImageButton(trdBtnMain);
        btnMenu.setPosition(ANCHO / 3 - 150, ALTO / 5);
        escena.addActor(btnMenu);

        // Evento del boton
        btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if (settings.getBoolean("Sounds", true))
                    clickSound.play();

                switch (currentLevel.getInteger("Nivel", 1)) {
                    case 1:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_1));
                        break;
                    case 2:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_2));
                        break;
                    case 3:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_3));
                        break;
                    case 4:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_4));
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
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = manager.get("GameOverGris.jpg");
        texturaBotonRestart = manager.get("restartButton.png");
        texturaBotonMenu = manager.get("mainMenuButton.png");
    }

    private void stopMusic(){
        switch (currentLevel.getInteger("Nivel",1)){
            case 1:
                musicLevel1.stop();
                break;
            case 2:
                musicLevel2.stop();
                break;
            case 3: case 4:
                musicLevel3.stop();
                break;
        }
    }

    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            regresarAMenu();
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
