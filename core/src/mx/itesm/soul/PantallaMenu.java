package mx.itesm.soul;

import com.badlogic.gdx.Gdx;
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

/**
 * Created by Adrian on 11/02/2017.
 */
public class PantallaMenu extends Pantalla {
    private final ColourlessSoul menu;
    private final AssetManager manager;

    //sonidos
    public static Music musicMenu;


    private Preferences prefs = Gdx.app.getPreferences("Settings");

    //texturas
    private Texture texturaFondoMenu;
    private Texture texturaBotonInicio;
    private Texture texturaBotonCargar;
    private Texture texturaBotonAjustes;
    private Texture texturaBotonExtras;
    private Texture texturaBannerTutorial;
    private Texture texturaBotonYes;
    private Texture texturaBotonNo;
    private Texture texturaTitulo;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    Preferences currentLevel = Gdx.app.getPreferences("CurrentLevel");

    public PantallaMenu(ColourlessSoul menu) {
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
        Image imgFondo = new Image(texturaFondoMenu);
        imgFondo.setSize(1280,800);
        escena.addActor(imgFondo);

        //Cargar audios
        manager.load("musicSounds/menuTheme.mp3",Music.class);
        manager.finishLoading();
        musicMenu = manager.get("musicSounds/menuTheme.mp3");
        musicMenu.setLooping(true);
        if(prefs.getBoolean("Music",true))
            musicMenu.play();

        Image imgTitulo = new Image(texturaTitulo);
        imgTitulo.setPosition(50, 350);
        escena.addActor(imgTitulo);

        //Botones del menú principal
        TextureRegionDrawable trdBtnStart = new TextureRegionDrawable(new TextureRegion(texturaBotonInicio));
        ImageButton btnStart = new ImageButton(trdBtnStart);
        btnStart.setPosition(2*ANCHO/3, 2*ALTO/3-btnStart.getHeight());
        escena.addActor(btnStart);

        TextureRegionDrawable trdBtnLoad = new TextureRegionDrawable(new TextureRegion(texturaBotonCargar));
        ImageButton btnLoad = new ImageButton(trdBtnLoad);
        btnLoad.setPosition(2*ANCHO/3, 2*ALTO/3-2*btnLoad.getHeight());
        escena.addActor(btnLoad);

        TextureRegionDrawable trdBtnSettings = new TextureRegionDrawable(new TextureRegion(texturaBotonAjustes));
        ImageButton btnSettings = new ImageButton(trdBtnSettings);
        btnSettings.setPosition(2*ANCHO/3, 2*ALTO/3-3*btnLoad.getHeight());
        escena.addActor(btnSettings);

        TextureRegionDrawable trdBtnExtras = new TextureRegionDrawable(new TextureRegion(texturaBotonExtras));
        ImageButton btnExtras = new ImageButton(trdBtnExtras);
        btnExtras.setPosition(2*ANCHO/3, 2*ALTO/3-4*btnLoad.getHeight());
        escena.addActor(btnExtras);

        TextureRegionDrawable trdBannerTuto = new TextureRegionDrawable(new TextureRegion(texturaBannerTutorial));
        final Image banner = new Image(trdBannerTuto);
        banner.setPosition(ANCHO/2-banner.getWidth()/2,ALTO/2-banner.getHeight()/2);

        TextureRegionDrawable trdBtnYes = new TextureRegionDrawable(new TextureRegion(texturaBotonYes));
        final ImageButton btnYes = new ImageButton(trdBtnYes);
        btnYes.setPosition(ANCHO/2-btnYes.getWidth()/2-100,ALTO/2-btnYes.getHeight()/2-100);

        TextureRegionDrawable trdBtnNo = new TextureRegionDrawable(new TextureRegion(texturaBotonNo));
        final ImageButton btnNo = new ImageButton(trdBtnNo);
        btnNo.setPosition(ANCHO/2-btnNo.getWidth()/2+100,ALTO/2-btnNo.getHeight()/2-100);

        // Evento del boton
        btnStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentLevel.putInteger("Nivel",1);
                currentLevel.flush();
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                escena.addActor(banner);
                escena.addActor(btnYes);
                escena.addActor(btnNo);
            }
        });

        btnYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("Tutorial1",true);
                prefs.putBoolean("Tutorial2",true);
                prefs.flush();
                musicMenu.stop();
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_1));
                clickSound.stop();
            }
        });

        btnNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("Tutorial1",false);
                prefs.putBoolean("Tutorial2",false);
                prefs.flush();
                musicMenu.stop();
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_1));
                clickSound.stop();
            }
        });

        btnLoad.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("Tutorial1",false);
                prefs.putBoolean("Tutorial2",false);
                prefs.flush();
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                musicMenu.stop();
                switch (currentLevel.getInteger("Nivel",1)) {
                    case 1:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_1));
                        break;
                    case 2:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_2));
                        break;
                    case 3:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_3));
                        break;
                    case 4:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_4));
                        break;
                    case 5:
                        menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_FINAL));
                        break;
                }
                clickSound.stop();
            }
        });

        btnSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PantallaAjustes.estado = EstadoInvocado.PANTALLA_MENU;
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new PantallaCargando(menu, Pantallas.AJUSTES));
                clickSound.stop();
            }
        });

        btnExtras.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(prefs.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new PantallaCargando(menu, Pantallas.EXTRAS));
                //clickSound.stop();
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondoMenu = manager.get("fondoMenu.jpg");
        texturaBotonInicio = manager.get("startButton.png");
        texturaBotonCargar = manager.get("loadButton.png");
        texturaBotonAjustes = manager.get("settingsButton1.png");
        texturaBotonExtras = manager.get("extrasButton.png");
        texturaBannerTutorial = manager.get("tutorialBanner.png");
        texturaBotonYes = manager.get("yes.png");
        texturaBotonNo = manager.get("no.png");
        texturaTitulo = manager.get("titulo.png");
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
        manager.unload("musicSounds/menuTheme.mp3");
        manager.unload("fondoMenu.jpg");
        manager.unload("startButton.png");
        manager.unload("loadButton.png");
        manager.unload("settingsButton1.png");
        manager.unload("extrasButton.png");
        manager.unload("tutorialBanner.png");
        manager.unload("yes.png");
        manager.unload("no.png");
        manager.unload("titulo.png");
    }
}
