package mx.sagh.soul;

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
 * Created by User on 17/02/2017.
 */

public class PantallaExtras extends Pantalla {
    private final ColourlessSoul menu;
    private final AssetManager manager;
    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonLogros;
    private Texture texturaBotonAcercaDe;
    private Texture texturaBotonHowToPlay;
    private Texture texturaBotonRetorno;
    private Texture texturaLeyenda;

    //Preferencias
    private Preferences settings = Gdx.app.getPreferences("Settings");
    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaExtras(ColourlessSoul menu) {
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

        Image imgLeyenda = new Image(texturaLeyenda);
        imgLeyenda.setScale(0.7f);
        imgLeyenda.setPosition(ANCHO/2-imgLeyenda.getWidth()*0.7f/2,40);
        escena.addActor(imgLeyenda);

        //Boton

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setSize(120,120);
        btnBack.setPosition(0,0);
        escena.addActor(btnBack);

        TextureRegionDrawable trdBtnAchievements = new TextureRegionDrawable(new TextureRegion(texturaBotonLogros));
        ImageButton btnAchievements = new ImageButton(trdBtnAchievements);
        btnAchievements.setPosition(ANCHO/3, 2*ALTO/3-btnAchievements.getHeight());
        escena.addActor(btnAchievements);

        TextureRegionDrawable trdBtnCredits = new TextureRegionDrawable(new TextureRegion(texturaBotonAcercaDe));
        ImageButton btnCredits = new ImageButton(trdBtnCredits);
        btnCredits.setPosition(ANCHO/3, 2*ALTO/3-2*btnCredits.getHeight());
        escena.addActor(btnCredits);

        TextureRegionDrawable trdBtnHowToPlay = new TextureRegionDrawable(new TextureRegion(texturaBotonHowToPlay));
        ImageButton btnHowToPlay = new ImageButton(trdBtnHowToPlay);
        btnHowToPlay.setPosition(ANCHO/3, 2*ALTO/3-3*btnHowToPlay.getHeight());
        escena.addActor(btnHowToPlay);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
                clickSound.stop();
            }
        });

        btnAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.LOGROS));
                clickSound.stop();
            }
        });

        btnCredits.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!clickSound.isPlaying())
                    if(settings.getBoolean("Sounds",true))
                        clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.CREDITOS));
                clickSound.stop();
            }
        });

        btnHowToPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!clickSound.isPlaying())
                    if(settings.getBoolean("Sounds",true))
                        clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.TUTORIAL));
                clickSound.stop();
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo =manager.get("fondoPrincipal.jpg");
        texturaBotonLogros = manager.get("achievementsButton.png");
        texturaBotonAcercaDe = manager.get("creditsButton.png");
        texturaBotonHowToPlay = manager.get("howToButton.png");
        texturaBotonRetorno = manager.get("backButton.png");
        texturaLeyenda = manager.get("nombreMateria.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            menu.setScreen(new PantallaMenu(menu));

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
        manager.unload("achievementsButton.png");
        manager.unload("creditsButton.png");
        manager.unload("howToButton.png");
        manager.unload("backButton.png");
        manager.unload("nombreMateria.png");
        clickSound.dispose();
        clickSound.stop();
    }
}