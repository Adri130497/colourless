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

/**
 * Created by Aldo on 18/02/2017.
 */
public class PantallaAjustes extends Pantalla {
    private final mx.itesm.soul.ColourlessSoul menu;
    public static mx.itesm.soul.EstadoInvocado estado;

    private final AssetManager manager;
    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));

    //Preferencias
    private Preferences prefs = Gdx.app.getPreferences("Settings");
    private Preferences currentLevel = Gdx.app.getPreferences("CurrentLevel");
    private Preferences achievements = Gdx.app.getPreferences("Achievements");

    //Escena
    private Stage escena;
    private SpriteBatch batch;
    //texturas
    private Texture texturaFondo;
    private Texture texturaSettings;
    private Texture texturaMusicaOn;
    private Texture texturaMusicaOff;
    private Texture texturaSonidosOn;
    private Texture texturaSonidosOff;
    private Texture texturaControlsChange;
    private Texture texturaControlTouch;
    private Texture texturaControlButton;
    private Texture texturaMusicSounds;
    private Texture texturaBotonRegreso;
    private Texture texturaBotonL;
    private Texture texturaReset;


    public PantallaAjustes(mx.itesm.soul.ColourlessSoul menu) {
        this.menu = menu;
        manager=menu.getAssetManager();
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        if(prefs.getBoolean("Music",true))
            mx.itesm.soul.PantallaMenu.musicMenu.play();

        //Imagenes estáticas
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        Image imgSettings = new Image(texturaSettings);
        imgSettings.setPosition(ANCHO/2-imgSettings.getWidth()/2,ALTO/2-imgSettings.getHeight()/2);
        escena.addActor(imgSettings);

        final Image imgControlChange = new Image(texturaControlsChange);
        imgControlChange.setPosition(ANCHO/2-imgControlChange.getWidth()/2,imgSettings.getY()+2*imgControlChange.getHeight());
        escena.addActor(imgControlChange);

        final Image imgControlT = new Image(texturaControlTouch);
        imgControlT.setPosition(ANCHO/2-imgControlT.getWidth()/2,imgControlChange.getY()+1);
        //if(estadoJugabilidad == EstadoJugabilidad.TOUCH)


        final Image imgControlB = new Image(texturaControlButton);
        imgControlB.setPosition(ANCHO/2-imgControlB.getWidth()/2,imgControlChange.getY()+1);
        //if(estadoJugabilidad == EstadoJugabilidad.BOTONES)
        if(prefs.getBoolean("Touch",true))
            escena.addActor(imgControlT);
        else escena.addActor(imgControlB);

        Image imgControlMS = new Image(texturaMusicSounds);
        imgControlMS.setPosition(ANCHO/2-imgControlMS.getWidth()/2,imgSettings.getY()+4*imgControlChange.getHeight());
        escena.addActor(imgControlMS);

        //Botones

        TextureRegionDrawable trdBtnReset = new TextureRegionDrawable(new TextureRegion(texturaReset));
        final ImageButton btnReset = new ImageButton(trdBtnReset);
        btnReset.setPosition(ANCHO/2-btnReset.getWidth()/2,150);
        escena.addActor(btnReset);

        TextureRegionDrawable trdBtnMusicOn = new TextureRegionDrawable(new TextureRegion(texturaMusicaOn));
        final ImageButton btnMusicaOn = new ImageButton(trdBtnMusicOn);
        btnMusicaOn.setSize(120,120);
        btnMusicaOn.setPosition(imgControlMS.getX()-23,imgControlMS.getY()-11.5f);

        TextureRegionDrawable trdBtnMusicOff = new TextureRegionDrawable(new TextureRegion(texturaMusicaOff));
        final ImageButton btnMusicaOff = new ImageButton(trdBtnMusicOff);
        btnMusicaOff.setSize(120,120);
        btnMusicaOff.setPosition(imgControlMS.getX()-23,imgControlMS.getY()-11.5f);
        if(prefs.getBoolean("Music",true))
            escena.addActor(btnMusicaOn);
        else escena.addActor(btnMusicaOff);

        TextureRegionDrawable trdBtnSoundsOn = new TextureRegionDrawable(new TextureRegion(texturaSonidosOn));
        final ImageButton btnSoundsOn = new ImageButton(trdBtnSoundsOn);
        btnSoundsOn.setSize(120,120);
        btnSoundsOn.setPosition(imgControlMS.getX()+23+imgControlMS.getWidth()-btnSoundsOn.getWidth(),btnMusicaOn.getY());

        TextureRegionDrawable trdBtnSoundsOff = new TextureRegionDrawable(new TextureRegion(texturaSonidosOff));
        final ImageButton btnSoundsOff = new ImageButton(trdBtnSoundsOff);
        btnSoundsOff.setSize(120,120);
        btnSoundsOff.setPosition(imgControlMS.getX()+23+imgControlMS.getWidth()-btnSoundsOff.getWidth(),btnMusicaOn.getY());
        if(prefs.getBoolean("Sounds",true))
            escena.addActor(btnSoundsOn);
        else escena.addActor(btnSoundsOff);

        TextureRegionDrawable trdBtnL = new TextureRegionDrawable(new TextureRegion(texturaBotonL));
        final ImageButton btnL = new ImageButton(trdBtnL);
        btnL.setSize(120,120);
        btnL.setPosition(imgControlChange.getX()+imgControlChange.getWidth()-btnL.getWidth()+23,imgControlChange.getY()-11.5f);
        escena.addActor(btnL);

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRegreso));
        ImageButton btnRegreso = new ImageButton(trdBtnBack);
        btnRegreso.setSize(120,120);
        btnRegreso.setPosition(0,0);
        escena.addActor(btnRegreso);


        // Evento del boton
        btnReset.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                achievements.clear();
                achievements.flush();
                currentLevel.clear();
                currentLevel.flush();
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                clickSound.stop();
            }
        });

        btnRegreso.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.flush();
                mx.itesm.soul.PantallaMenu.musicMenu.stop();
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                if(estado == mx.itesm.soul.EstadoInvocado.PANTALLA_MENU)
                    menu.setScreen(new mx.itesm.soul.PantallaMenu(menu));
                else if(estado == mx.itesm.soul.EstadoInvocado.PANTALLA_PRINCIPAL) {
                    cargarPantalla();
                }
                clickSound.stop();
            }
        });

        btnMusicaOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mx.itesm.soul.PantallaMenu.musicMenu.stop();
                prefs.putBoolean("Music",false);
                escena.addActor(btnMusicaOff);
                btnMusicaOn.remove();
            }
        });

        btnMusicaOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mx.itesm.soul.PantallaMenu.musicMenu.play();
                prefs.putBoolean("Music",true);
                escena.addActor(btnMusicaOn);
                btnMusicaOff.remove();
            }
        });

        btnSoundsOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("Sounds",false);
                escena.addActor(btnSoundsOff);
                btnSoundsOn.remove();
            }
        });

        btnSoundsOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("Sounds",true);
                escena.addActor(btnSoundsOn);
                btnSoundsOff.remove();
            }
        });

        btnL.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(imgControlB.getStage() == null){
                    prefs.putBoolean("Touch",false);
                    escena.addActor(imgControlB);
                    imgControlT.remove();
                }
                else if(imgControlT.getStage() == null){
                    prefs.putBoolean("Touch",true);
                    escena.addActor(imgControlT);
                    imgControlB.remove();
                }
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarPantalla() {
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
    }


    private void cargarTexturas() {
        texturaFondo = manager.get("fondoPrincipal.jpg");
        texturaSettings = manager.get("fondoMadera.png");
        texturaMusicaOn = manager.get("musicOn.png");
        texturaMusicaOff = manager.get("musicOff.png");
        texturaSonidosOn = manager.get("soundsOn.png");
        texturaSonidosOff = manager.get("soundsOff.png");
        texturaMusicSounds = manager.get("musicSounds.png");
        texturaControlsChange = manager.get("controlsChange.png");
        texturaControlTouch = manager.get("controlsTouch.png");
        texturaControlButton = manager.get("controlsButton.png");
        texturaBotonL = manager.get("changeButtonL.png");
        texturaBotonRegreso = manager.get("backButton.png");
        texturaReset = manager.get("resetGame.png");
    }
    @Override
    public void render(float delta) {
        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            if(estado == mx.itesm.soul.EstadoInvocado.PANTALLA_MENU)
                menu.setScreen(new mx.itesm.soul.PantallaMenu(menu));
            else if(estado == mx.itesm.soul.EstadoInvocado.PANTALLA_PRINCIPAL) {
                cargarPantalla();
            }
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
        manager.unload("fondoPrincipal.jpg");
        manager.unload("fondoMadera.png");
        manager.unload("musicOn.png");
        manager.unload("musicOff.png");
        manager.unload("soundsOn.png");
        manager.unload("soundsOff.png");
        manager.unload("musicSounds.png");
        manager.unload("controlsChange.png");
        manager.unload("controlsTouch.png");
        manager.unload("controlsButton.png");
        manager.unload("changeButtonL.png");
        manager.unload("backButton.png");
        manager.unload("resetGame.png");
        clickSound.dispose();
    }

    public enum EstadoJugabilidad {
        BOTONES,
        TOUCH
    }
}
