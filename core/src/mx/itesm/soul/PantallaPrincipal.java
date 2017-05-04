package mx.itesm.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static mx.itesm.soul.ColourlessSoul.clickSound;

public class PantallaPrincipal extends Pantalla {
    private final ColourlessSoul menu;

    public static final float ANCHO_MAPA = 5120;
    private OrthogonalTiledMapRenderer renderer; // Dibuja el mapa
    private TiledMap mapa;

    private EstadoDisparo estadoDisparo;
    private EstadoTutorial estadoTutorial;

    private final float DELTA_X = 10;    // Desplazamiento del personaje
    private final float DELTA_Y = 10;
    private final float UMBRAL = 50; // Para asegurar que hay movimiento
    private float tiempoAsustado = 0.8f;

    // Preferencias
    Preferences prefs = Gdx.app.getPreferences("Achievements");
    Preferences currentLevel = Gdx.app.getPreferences("CurrentLevel");
    private Preferences settings = Gdx.app.getPreferences("Settings");

    // Punteros (dedo para pan horizontal, vertical)
    private final int INACTIVO = -1;
    private int punteroHorizontal = INACTIVO;
    private int punteroVertical = INACTIVO;

    // Coordenadas
    private float xHorizontal = 0;
    private float yVertical = 0;

    //Sistema de partículas
    private ParticleEffect sistemaParticulasCroqueta, sistemaParticulasPocion;

    private SpriteBatch batch;

    // Personaje
    private Kai kai;
    private float dx = 0;
    private float dy = 0;
    private Texture texturaKaiCaminando, texturaKaiReposo, texturaKaiBrincando, texturaKaiCayendo, texturaKaiAsustado;



    // Música / efectos
    public static Music musicLevel1, musicLevel2, musicLevel3;

    // HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;
    private Touchpad pad;
    private ImageButton btnPausa;
    private Image imgPause;
    private Image imgEndLevel;
    private Image imgGamePaused;
    private Image vida1, vida2, vida3, vida4, vida5, vida6, vida7, vidaFull;
    private Image spritesVida[];
    private ImageButton btnResume;
    private ImageButton btnRestart;
    private ImageButton btnSettings;
    private ImageButton btnMainMenu;
    private ImageButton btnUp;
    private ImageButton btnReplay;
    private ImageButton btnMenu;
    private ImageButton btnNextLevel;
    private ImageButton btnDisp;

    //Imagenes tutorial
    private Texture texturaBannerL1, texturaBannerL2, texturaBannerR1, texturaBannerR2, texturaBannerR3, texturaline;
    private Image bannerL1, bannerL2, bannerR1, bannerR2, bannerR3, line;
    private float tiempoTouch = 1.0f;
    private int saltos = 0;
    private int shoots = 0;

    //texturas
    private Texture texturaBotonPausa;
    private Texture texturaMenuPausa;
    private Texture texturaFinNivel;
    private Texture texturaGamePaused;
    private Texture texturaResumeButton;
    private Texture texturaRestartButton;
    private Texture texturaSettingsButton;
    private Texture texturaMainMenuButton;
    private Texture texturaBotonUp;
    private Texture texturaBotonMenu;
    private Texture texturaBotonReplay;
    private Texture texturaBotonNextLevel;
    private Texture texturaBotonDisparar;
    private Texture barra1,barra2,barra3,barra4,barra5,barra6,barra7,barraFull;

    // Puntos del jugador
    private int score = 0;
    private int slimeTocados=0;
    private mx.itesm.soul.Texto texto;
    private int maxScore = 0;

    // Enemigo
    private Texture texturaSlime;
    private Array<Slime> slimes;

    //Disparos
    private Texture texturaDisparo;
    private Array<Disparo> disparos;

    private final AssetManager manager;

    private boolean bitedCookie, tookPotion;
    private EstadoNivel estadoNivel;


    public PantallaPrincipal(ColourlessSoul menu) {
        this.menu = menu;

        manager=menu.getAssetManager();
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();
        crearMapaAleatorio();
        sistemaParticulasCroqueta = new ParticleEffect();
        sistemaParticulasPocion = new ParticleEffect();
        bitedCookie = false;
        tookPotion = false;
        sistemaParticulasCroqueta.load(Gdx.files.internal("pezVanish.pe"),Gdx.files.internal(""));
        sistemaParticulasPocion.load(Gdx.files.internal("pocionVanish.pe"),Gdx.files.internal(""));
        //if(PantallaAjustes.estadoJugabilidad == PantallaAjustes.EstadoJugabilidad.TOUCH)
        if(settings.getBoolean("Touch",true))
            Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearMapaAleatorio() {
        switch (currentLevel.getInteger("Nivel",1)){
            case 1:
                mapa.getLayers().get(0).setVisible(true);
                mapa.getLayers().get(1).setVisible(false);
                mapa.getLayers().get(2).setVisible(false);
                mapa.getLayers().get(3).setVisible(false);
                break;
            case 2:
                mapa.getLayers().get(1).setVisible(true);
                mapa.getLayers().get(2).setVisible(false);
                mapa.getLayers().get(3).setVisible(false);
                break;
            case 3:
                mapa.getLayers().get(2).setVisible(true);
                mapa.getLayers().get(3).setVisible(false);
                break;
            case 4:
                mapa.getLayers().get(3).setVisible(true);
                break;
        }
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(4); //puedes recuperar una capa del mapa
        //Colocar pociones, sin importar que ya las haya tomado en otro nivel
        capa.setCell(93,4,capa.getCell(159,23));
        capa.setCell(129,4,capa.getCell(159,23));
        //Arreglo de posibles posiciones donde se puedan colocar los peces de forma aleatoria
        int arrY[] = {4,6,8,10};
        int arrX[] = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 152};
        for (int b: arrY)   for(int a: arrX)    capa.setCell(a,b,null);
        int x, y;
        //Colocar los peces de forma aleatoria
        for(int i=0; i<40; i++){
            y = arrY[(int)(Math.random() * 4)];
            x = arrX[(int)(Math.random() * 72)];
            if(capa.getCell(x,y)==null) maxScore++;
            capa.setCell(x,y,capa.getCell(159,24));
        }


        //Colocar las gemas al final, dependiendo el nivel
        switch (currentLevel.getInteger("Nivel",1)){
            case 1:
                capa.setCell(156,4,capa.getCell(159,0));
                break;
            case 2:
                capa.setCell(156,4,capa.getCell(159,2));
                break;
            case 3:
                capa.setCell(156,4,capa.getCell(159,4));
                break;
            case 4:
                capa.setCell(156,4,capa.getCell(159,6));
                break;
        }
    }

    private void crearObjetos() {
        texto = new mx.itesm.soul.Texto();
        estadoNivel = EstadoNivel.ACTIVE;
        estadoDisparo = EstadoDisparo.LIBRE;
        estadoTutorial = EstadoTutorial.FINGERS;

        manager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapaColourless.tmx", TiledMap.class);

        manager.finishLoading();    // Carga los recursos
        mapa = manager.get("mapaColourless.tmx");

        //Cargar audios
        musicLevel1 = manager.get("musicSounds/level1Theme.mp3");
        musicLevel1.setLooping(true);
        musicLevel2 = manager.get("musicSounds/level2Theme.mp3");
        musicLevel2.setLooping(true);
        musicLevel3 = manager.get("musicSounds/level3Theme.mp3");
        musicLevel3.setLooping(true);
        if(settings.getBoolean("Music",true))
            playMusic();

        batch = new SpriteBatch();
        renderer = new OrthogonalTiledMapRenderer(mapa, batch);
        renderer.setView(camara);

        disparos = new Array();
        slimes = new Array();

        crearHUD();
        Gdx.input.setInputProcessor(escenaHUD);
        kai = new Kai(texturaKaiCaminando, texturaKaiReposo, texturaKaiBrincando, texturaKaiCayendo, texturaKaiAsustado, 228,128);
        //slime = new Slime[8];
        for(int i=0; i<8+5*(currentLevel.getInteger("Nivel",1)-1); i++)
            slimes.add(new Slime(texturaSlime, MathUtils.random(2080,4736),MathUtils.random(128,160)));
            //slime[i] = new Slime(texturaSlime, MathUtils.random(2080,4736),MathUtils.random(128,160));

        //Botones
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setSize(64,64);
        btnPausa.setPosition(2*ANCHO/3+220,2*ALTO/3-btnPausa.getHeight()+220);
        escenaHUD.addActor(btnPausa);

        TextureRegionDrawable trdBtnUp = new TextureRegionDrawable(new TextureRegion(texturaBotonUp));
        btnUp = new ImageButton(trdBtnUp);
        btnUp.setPosition(ANCHO-btnUp.getWidth(),0);

        TextureRegionDrawable trdBtnDisp = new TextureRegionDrawable(new TextureRegion(texturaBotonDisparar));
        btnDisp = new ImageButton(trdBtnDisp);
        btnDisp.setPosition(btnUp.getX() - btnDisp.getWidth(),0);

        if(!settings.getBoolean("Tutorial1",true) && !settings.getBoolean("Touch",true)) {
            escenaHUD.addActor(btnUp);
            if(currentLevel.getInteger("Nivel",1)>=2) escenaHUD.addActor(btnDisp);
            escenaHUD.addActor(pad);
        }

        //Tutorial de inicio
        bannerL1 = new Image(texturaBannerL1);
        bannerL1.setPosition(ANCHO/4-bannerL1.getWidth()/2,ALTO/2);
        bannerL2 = new Image(texturaBannerL2);
        bannerL2.setPosition(ANCHO/4-bannerL2.getWidth()/2,ALTO/2);
        bannerR1 = new Image(texturaBannerR1);
        bannerR1.setPosition(3*ANCHO/4-bannerR1.getWidth()/2,ALTO/2);
        bannerR2 = new Image(texturaBannerR2);
        bannerR2.setPosition(3*ANCHO/4-bannerR2.getWidth()/2,ALTO/2);
        bannerR3 = new Image(texturaBannerR3);
        bannerR3.setPosition(3*ANCHO/4-bannerR3.getWidth()/2,ALTO/2);
        line = new Image(texturaline);
        line.setPosition(ANCHO/2-10,ALTO/2-line.getHeight()/2);

        if(settings.getBoolean("Tutorial1",true)){
            Gdx.app.log("TUto","True");
            escenaHUD.addActor(bannerL1);
            escenaHUD.addActor(bannerR1);
        }
        else if(settings.getBoolean("Tutorial2",true)){
            Gdx.app.log("TUto2","True");
            escenaHUD.addActor(bannerR3);
            escenaHUD.addActor(line);
            estadoTutorial = EstadoTutorial.SHOOT;
        }
        //Pantalla pausa
        imgPause = new Image(texturaMenuPausa);
        imgPause.setPosition(ANCHO/2-imgPause.getWidth()/2,ALTO/2-imgPause.getHeight()/2);

        imgGamePaused = new Image(texturaGamePaused);
        imgGamePaused.setPosition(ANCHO/2-imgGamePaused.getWidth()/2,imgPause.getY()+imgPause.getHeight()-2*imgGamePaused.getHeight()-10);

        //Pantalla fin de nivel
        imgEndLevel = new Image(texturaFinNivel);
        imgEndLevel.setPosition(ANCHO/2-imgEndLevel.getWidth()/2,ALTO/2-imgEndLevel.getHeight()/2);

        TextureRegionDrawable trdBtnReplay = new TextureRegionDrawable(new TextureRegion(texturaBotonReplay));
        btnReplay = new ImageButton(trdBtnReplay);
        btnReplay.setSize(120,120);
        btnReplay.setPosition(imgEndLevel.getX()+50,imgEndLevel.getY()+50);

        TextureRegionDrawable trdBtnNextLevel = new TextureRegionDrawable(new TextureRegion(texturaBotonNextLevel));
        btnNextLevel = new ImageButton(trdBtnNextLevel);
        btnNextLevel.setPosition(ANCHO/2-btnNextLevel.getWidth()/2,btnReplay.getY()+btnReplay.getMinHeight()/2-btnNextLevel.getHeight());

        TextureRegionDrawable trdBtnMenu = new TextureRegionDrawable(new TextureRegion(texturaBotonMenu));
        btnMenu = new ImageButton(trdBtnMenu);
        btnMenu.setSize(120,120);
        btnMenu.setPosition(imgEndLevel.getX()+imgEndLevel.getWidth()-btnMenu.getWidth()-50,imgEndLevel.getY()+50);

        vida1 = new Image(barra1);
        vida2 = new Image(barra2);
        vida3 = new Image(barra3);
        vida4 = new Image(barra4);
        vida5 = new Image(barra5);
        vida6 = new Image(barra6);
        vida7 = new Image(barra7);
        vidaFull = new Image(barraFull);

        spritesVida = new Image[]{vidaFull, vida1, vida2, vida3, vida4, vida5, vida6, vida7};
        for (Image sprite : spritesVida) {
            sprite.setPosition(25,btnPausa.getY()+btnPausa.getHeight()-sprite.getHeight());
        }
        escenaHUD.addActor(spritesVida[0]);

        TextureRegionDrawable trdBtnResume = new TextureRegionDrawable(new TextureRegion(texturaResumeButton));
        btnResume = new ImageButton(trdBtnResume);
        btnResume.setPosition(ANCHO/2-btnResume.getWidth()/2,imgGamePaused.getY()-2*btnResume.getHeight());

        TextureRegionDrawable trdBtnRestart = new TextureRegionDrawable(new TextureRegion(texturaRestartButton));
        btnRestart = new ImageButton(trdBtnRestart);
        btnRestart.setPosition(ANCHO/2-btnRestart.getWidth()/2,btnResume.getY()-35-btnRestart.getHeight());

        TextureRegionDrawable trdBtnSettings = new TextureRegionDrawable(new TextureRegion(texturaSettingsButton));
        btnSettings = new ImageButton(trdBtnSettings);
        btnSettings.setPosition(ANCHO/2-btnSettings.getWidth()/2,btnRestart.getY()-35-btnSettings.getHeight());

        TextureRegionDrawable trdBtnMainMenu = new TextureRegionDrawable(new TextureRegion(texturaMainMenuButton));
        btnMainMenu = new ImageButton(trdBtnMainMenu);
        btnMainMenu.setPosition(ANCHO/2-btnMainMenu.getWidth()/2,btnSettings.getY()-35-btnMainMenu.getHeight());

        // Evento del botón
        btnPausa.addListener(new ClickListener(){

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                inPauseEvent();
                return true;
            }
            /*public void clicked(InputEvent event, float x, float y) {
                inPauseEvent();
            }*/
        });

        btnUp.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if (kai.getEstadoSalto() != Kai.EstadoSalto.SUBIENDO)
                    kai.saltar();
                return true;
            }
        });

        btnDisp.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(currentLevel.getInteger("Nivel",1)>=2)
                    if(estadoDisparo != EstadoDisparo.DISPARANDO)
                        disparar();
                return true;
            }
        });

        Gdx.input.setCatchBackKey(true);
    }

    private void resumeGame(){
        estadoNivel = EstadoNivel.ACTIVE;
        if(!settings.getBoolean("Tutorial1",true) && !settings.getBoolean("Touch",true)) {
            escenaHUD.addActor(btnUp);
            if(currentLevel.getInteger("Nivel",1)>=2) escenaHUD.addActor(btnDisp);
            escenaHUD.addActor(pad);
        }
        if(settings.getBoolean("Sounds",true))
            clickSound.play();
        imgPause.remove();
        imgGamePaused.remove();
        btnResume.remove();
        btnRestart.remove();
        btnSettings.remove();
        btnMainMenu.remove();
        clickSound.stop();
        //if(PantallaAjustes.estadoJugabilidad == PantallaAjustes.EstadoJugabilidad.TOUCH)
        if(settings.getBoolean("Touch",true))
            Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void inPauseEvent() {
        Gdx.input.setInputProcessor(escenaHUD);
        estadoNivel = EstadoNivel.PAUSED;
        btnDisp.remove();
        pad.remove();
        btnUp.remove();
        btnDisp.remove();
        escenaHUD.addActor(imgPause);
        escenaHUD.addActor(imgGamePaused);
        escenaHUD.addActor(btnResume);
        escenaHUD.addActor(btnRestart);
        escenaHUD.addActor(btnSettings);
        escenaHUD.addActor(btnMainMenu);

        btnResume.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeGame();
            }
        });

        btnSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                PantallaAjustes.estado = mx.itesm.soul.EstadoInvocado.PANTALLA_PRINCIPAL;
                menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.AJUSTES));
                clickSound.stop();
            }
        });

        btnRestart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                switch (currentLevel.getInteger("Nivel",1)){
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
                    case 5:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_FINAL));
                        break;
                }
                clickSound.stop();
            }
        });

        btnMainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.MENU));
                clickSound.stop();
            }
        });


        }



    private void atFinishEvent() {
        Gdx.input.setInputProcessor(escenaHUD);
        kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
        estadoNivel = EstadoNivel.FINISHED;
        pad.remove();
        btnUp.remove();
        btnDisp.remove();
        btnPausa.remove();
        escenaHUD.addActor(imgEndLevel);
        escenaHUD.addActor(btnReplay);
        escenaHUD.addActor(btnNextLevel);
        escenaHUD.addActor(btnMenu);

        int nivel = currentLevel.getInteger("Nivel",1);
        if(score >= Integer.parseInt(prefs.getString(nivel+"-Score","0"+"/"+Integer.toString(maxScore)).substring(0,prefs.getString(nivel+"-Score","0"+"/"+Integer.toString(maxScore)).indexOf("/"))))
            prefs.putString(nivel+"-Score",Integer.toString(score)+"/"+Integer.toString(maxScore));
        if(slimeTocados <= Integer.parseInt(prefs.getString(nivel+"-Slimes","50 Hits").substring(0,prefs.getString(nivel+"-Slimes","50 Hits").indexOf(" "))))
            prefs.putString(nivel+"-Slimes",Integer.toString(slimeTocados)+" Hits");
        prefs.flush();

        btnReplay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                if(settings.getBoolean("Music",true))
                    playMusic();
                switch (currentLevel.getInteger("Nivel",1)){
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
                    case 5:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_FINAL));
                        break;
                }
                clickSound.stop();
            }
        });

        btnNextLevel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnNextLevel.remove();
                stopMusic();
                if(currentLevel.getInteger("Nivel",1)==1) {

                    currentLevel.putInteger("Nivel", 2);
                }
                else if(currentLevel.getInteger("Nivel",1)==2)
                    currentLevel.putInteger("Nivel",3);
                else currentLevel.putInteger("Nivel",4);


                currentLevel.flush();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                switch (currentLevel.getInteger("Nivel",1)){
                    case 2:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_2));
                        break;
                    case 3:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_3));
                        break;
                    case 4:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_4));
                        break;
                    case 5:
                        menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.NIVEL_FINAL));
                        break;

                }
                clickSound.stop();
            }
        });

        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                if(currentLevel.getInteger("Nivel",1)==1) {

                    currentLevel.putInteger("Nivel", 2);
                }
                else if(currentLevel.getInteger("Nivel",1)==2)
                    currentLevel.putInteger("Nivel",3);
                else currentLevel.putInteger("Nivel",4);
                currentLevel.flush();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.MENU));
                clickSound.stop();
            }
        });

    }

    private void crearHUD() {
        // Cámara HUD
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();

        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        // HUD
        Skin skin = new Skin();
        skin.add("padBack", manager.get("padBack.png"));
        skin.add("padKnob", manager.get("padKnob.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padBack");
        estilo.knob = skin.getDrawable("padKnob");

        pad = new Touchpad(20, estilo);
        pad.setBounds(0, 0, 120, 120);
        pad.setColor(1,1,1,0.5f);

        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                if (pad.getKnobPercentX()>0.20) {
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_DERECHA);
                } else if (pad.getKnobPercentX()<-0.20){
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_IZQUIERDA);
                } else {
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
                }
            }
        });
        escenaHUD = new Stage(vistaHUD);
    }

    private void cargarTexturas() {
        texturaBotonPausa = manager.get("pauseButton.png");
        texturaMenuPausa=manager.get("fondoMadera.png");
        texturaFinNivel = manager.get("fondoFinNivel.png");
        texturaGamePaused = manager.get("gamePaused.png");
        texturaResumeButton = manager.get("resumeButton.png");
        texturaRestartButton = manager.get("restartButton.png");
        texturaSettingsButton = manager.get("settingsButton.png");
        texturaMainMenuButton = manager.get("mainMenuButton.png");
        texturaKaiCaminando = manager.get("KaiSprites/kaiWalkingSprite.png");
        texturaKaiReposo = manager.get("KaiSprites/kaiRestingSprite.png");
        texturaKaiBrincando = manager.get("KaiSprites/kaiJumpingSprite.png");
        texturaKaiCayendo = manager.get("KaiSprites/kaiFallingSprite.png");
        texturaKaiAsustado = manager.get("KaiSprites/kaiGotHitSprite.png");
        texturaBotonUp = manager.get("upButton.png");
        texturaBotonReplay = manager.get("replayButton.png");
        texturaBotonMenu = manager.get("menuButton.png");
        texturaBotonNextLevel = manager.get("nextLevel.png");
        texturaBotonDisparar = manager.get("shootButton.png");
        texturaDisparo = manager.get("SpritesDisparo/Balas3.png");
        barra1 = manager.get("SpritesBarraVida/vida1.png");
        barra2 = manager.get("SpritesBarraVida/vida2.png");
        barra3 = manager.get("SpritesBarraVida/vida3.png");
        barra4 = manager.get("SpritesBarraVida/vida4.png");
        barra5 = manager.get("SpritesBarraVida/vida5.png");
        barra6 = manager.get("SpritesBarraVida/vida6.png");
        barra7 = manager.get("SpritesBarraVida/vida7.png");
        barraFull = manager.get("SpritesBarraVida/vidaFull.png");
        texturaSlime = manager.get("SpritesSlime/slimePiso.png");
        texturaBannerL1 = manager.get("FondosTutorial/bannerLeft1.png");
        texturaBannerL2 = manager.get("FondosTutorial/bannerLeft2.png");
        texturaBannerR1 = manager.get("FondosTutorial/bannerRight1.png");
        texturaBannerR2 = manager.get("FondosTutorial/bannerRight2.png");
        texturaBannerR3 = manager.get("FondosTutorial/bannerRight3.png");
        texturaline = manager.get("FondosTutorial/linea.png");
    }

    @Override
    public void render(float delta) {

        actualizarDisparos(delta);
        if(estadoNivel != EstadoNivel.PAUSED && estadoNivel != EstadoNivel.FINISHED) {
            kai.actualizar(delta, mapa, camara);
            for(Slime baba: slimes) {
                baba.actualizar(mapa);
                if ((kai.sprite.getX() + 400) >= baba.sprite.getX()) {
                    baba.setEstadoMovimiento(Slime.EstadoMovimiento.MOV_IZQUIERDA);
                }
                if (kai.tocoSlime(baba)) {
                    slimeTocados++;
                    tiempoAsustado = 0.8f;
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.ASUSTADO);
                    disminuirVida();
                }
            }
            actualizarCamara();
        }

        if(kai.getEstadoMovimiento()==Kai.EstadoMovimiento.ASUSTADO){
            tiempoAsustado -= delta;
            if (tiempoAsustado<=0) {
                kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
            }
        }
        // 60 x seg
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        renderer.setView(camara);
        renderer.render();

        batch.begin();

        if(settings.getBoolean("Tutorial1",true) || settings.getBoolean("Tutorial2",true)){
            tutorial(delta);
        }

        if (bitedCookie) {
            sistemaParticulasCroqueta.update(delta);
            sistemaParticulasCroqueta.draw(batch);
            if (sistemaParticulasCroqueta.getEmitters().first().getActiveCount() >= 1)
                sistemaParticulasCroqueta.allowCompletion();
        }
        if (tookPotion) {
            sistemaParticulasPocion.update(delta);
            sistemaParticulasPocion.draw(batch);
            if (sistemaParticulasPocion.getEmitters().first().getActiveCount() >= 1)
                sistemaParticulasPocion.allowCompletion();
        }
        if (kai.recolectarItems(mapa)) {
            int x = (int) (kai.sprite.getX());
            int y = (int) (kai.sprite.getY());
            sistemaParticulasCroqueta = new ParticleEffect();
            sistemaParticulasCroqueta.load(Gdx.files.internal("pezVanish.pe"), Gdx.files.internal(""));
            sistemaParticulasCroqueta.getEmitters().first().setPosition(x + kai.sprite.getWidth(), y + kai.sprite.getHeight());
            score++;
            bitedCookie = true;

        }
        if (kai.tomoPocion(mapa)) {
            int x = (int) (kai.sprite.getX());
            int y = (int) (kai.sprite.getY());
            sistemaParticulasPocion = new ParticleEffect();
            sistemaParticulasPocion.load(Gdx.files.internal("pocionVanish.pe"), Gdx.files.internal(""));
            sistemaParticulasPocion.getEmitters().first().setPosition(x + kai.sprite.getWidth(), y + kai.sprite.getHeight());
            tookPotion = true;
            aumentarVida();
        }

        if(kai.recogeGema(mapa))
            atFinishEvent();

        if (kai.esAlcanzado(camara))
            menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.GAMEOVER));

        kai.dibujar(batch);
        for(Slime baba: slimes)
            baba.dibujar(batch);

        for (Disparo disparo:disparos){
            disparo.dibujar(batch);
        }

        batch.end();

        //OTRA CAMARA
        batch.setProjectionMatrix(camaraHUD.combined);

        escenaHUD.draw();
        //if(PantallaAjustes.estadoJugabilidad == PantallaAjustes.EstadoJugabilidad.TOUCH) {
        if(settings.getBoolean("Touch",true)){
            pad.remove();
            btnUp.remove();
            btnDisp.remove();
        }

        batch.begin();

        texto.mostrarMensaje(batch, "Score: " + Integer.toString(score), vida1.getX(), ALTO / 35 + 680);
        //texto.mostrarMensaje(batch, "Slime: "+Integer.toString(slimeTocados), ANCHO / 2 - 550, ALTO / 35 + 640);

        //Score final del nivel
        if(estadoNivel ==EstadoNivel.FINISHED) {
            texto.mostrarMensaje(batch, Integer.toString(score) + "/" +Integer.toString(maxScore), ANCHO / 3 + 50, ALTO / 2 + 125);
            texto.mostrarMensaje(batch, Integer.toString(slimeTocados)+" Hits", ANCHO / 3 + 50, ALTO / 2 - 10);
            //if(musicLevel1.getVolume()>0.00225f)|
              //  musicLevel1.setVolume(musicLevel1.getVolume()-0.00225f);

        }

        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(estadoNivel==EstadoNivel.ACTIVE)
                inPauseEvent();
            else if(estadoNivel==EstadoNivel.PAUSED)
                resumeGame();
            else if (estadoNivel==EstadoNivel.FINISHED){
                stopMusic();
                if(currentLevel.getInteger("Nivel",1)==1)
                    currentLevel.putInteger("Nivel", 2);
                else if(currentLevel.getInteger("Nivel",1)==2)
                    currentLevel.putInteger("Nivel",3);
                else currentLevel.putInteger("Nivel",4);
                currentLevel.flush();
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new mx.itesm.soul.PantallaMenu(menu));
                clickSound.stop();
            }
        }

    }

    private void tutorial(float delta){
        if(punteroHorizontal!=INACTIVO || punteroVertical!=INACTIVO)
            tiempoTouch-=delta;
        switch (estadoTutorial){
            case FINGERS:
                if(tiempoTouch<=0) {
                    bannerL1.remove();
                    bannerR1.remove();
                    escenaHUD.addActor(line);
                    estadoTutorial = EstadoTutorial.SLIDE;
                    escenaHUD.addActor(bannerL2);
                    tiempoTouch = 2.0f;
                }
                break;
            case SLIDE:
                if(tiempoTouch<=0) {
                    bannerL2.remove();
                    estadoTutorial = EstadoTutorial.JUMP;
                    escenaHUD.addActor(bannerR2);
                }
                break;
            case JUMP:
                if(saltos>=3) {
                    bannerR2.remove();
                    line.remove();
                    settings.putBoolean("Tutorial1",false);
                    settings.flush();
                }
                break;
            case SHOOT:
                if(shoots>=3) {
                    Gdx.app.log("Turo2","true");
                    bannerR3.remove();
                    line.remove();
                    settings.putBoolean("Tutorial2",false);
                    settings.flush();
                }
                break;
        }
    }

    private void actualizarDisparos(float delta) {
        for(int i=disparos.size-1; i>=0; i--) {
            Disparo disparo = disparos.get(i);
            disparo.mover(delta);
            if (disparo.sprite.getX()>camara.position.x+Pantalla.ANCHO/2) {
                // Se salió de la pantalla
                estadoDisparo = EstadoDisparo.LIBRE;
                disparos.removeIndex(i);
                break;
            }
            // Prueba choque contra todos los enemigos
            for (int j=slimes.size-1; j>=0; j--) {
                Slime enemigo = slimes.get(j);
                if (disparo.chocaCon(enemigo)) {
                    // Borrar hongo, bala, aumentar puntos, etc.
                    estadoDisparo = EstadoDisparo.LIBRE;
                    slimes.removeIndex(j);
                    disparos.removeIndex(i);
                    break;  // Siguiente bala, ésta ya no existe
                }
            }
        }
    }

    private void disminuirVida() {
        for(int i=0; i<8; i++){
            if(spritesVida[i].getStage()!=null){
                if(i!=7) {
                    spritesVida[i].remove();
                    escenaHUD.addActor(spritesVida[i+1]);
                    break;
                }
                else
                    menu.setScreen(new PantallaCargando(menu, mx.itesm.soul.Pantallas.GAMEOVER));
            }
        }
    }

    private void aumentarVida() {
        for(int i=0; i<8; i++)
            if(spritesVida[i].getStage()!=null){
                if(i!=0) {
                    spritesVida[i].remove();
                    escenaHUD.addActor(spritesVida[i-1]);
                    break;
                }
            }
    }

    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando está en la primera y última parte del mundo
    private void actualizarCamara() {
        //float posX = kai.sprite.getX(); //siempre es el sprite quien me da la x o la y del personaje
        if(camara.position.x<4440)
            if(!settings.getBoolean("Tutorial1",true) || estadoTutorial==EstadoTutorial.SLIDE  || estadoTutorial==EstadoTutorial.JUMP)
                camara.position.set((camara.position.x+(100+15*currentLevel.getInteger("Nivel",1))*Gdx.graphics.getDeltaTime()), camara.position.y, 0);
        /*if (posX>ANCHO_MAPA-ANCHO/2) {    // Si está en la última mitad
            camara.position.set(ANCHO_MAPA - ANCHO / 2, camara.position.y, 0);
            Gdx.app.log("Pos",Float.toString(camara.position.x));
        }*/
        camara.update();
    }

    private void playMusic(){
        switch (currentLevel.getInteger("Nivel",1)){
            case 1:
                musicLevel1.play();
                break;
            case 2:
                musicLevel2.play();
                break;
            case 3:
                musicLevel3.play();
                break;
            case 4:
                musicLevel3.play();
                break;
        }
    }

    private void stopMusic(){
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
            case 4:
                musicLevel3.stop();
                break;
        }
    }

    private void disparar(){
        estadoDisparo = EstadoDisparo.DISPARANDO;
        Disparo disparo=new Disparo(texturaDisparo,kai.sprite.getX()+kai.sprite.getWidth()/2,
                kai.sprite.getY()+kai.sprite.getHeight()/2-texturaDisparo.getHeight()/2);
        disparos.add(disparo);
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        manager.unload("musicSounds/level1Theme.mp3");
        manager.unload("musicSounds/level2Theme.mp3");
        manager.unload("musicSounds/level3Theme.mp3");
        manager.unload("shootButton.png");
        manager.unload("pauseButton.png");
        manager.unload("fondoMadera.png");
        manager.unload("fondoFinNivel.png");
        manager.unload("gamePaused.png");
        manager.unload("resumeButton.png");
        manager.unload("restartButton.png");
        manager.unload("settingsButton.png");
        manager.unload("mainMenuButton.png");
        manager.unload("KaiSprites/kaiWalkingSprite.png");
        manager.unload("KaiSprites/kaiRestingSprite.png");
        manager.unload("KaiSprites/kaiJumpingSprite.png");
        manager.unload("KaiSprites/kaiFallingSprite.png");
        manager.unload("KaiSprites/kaiGotHitSprite.png");
        manager.unload("upButton.png");
        manager.unload("replayButton.png");
        manager.unload("menuButton.png");
        manager.unload("nextLevel.png");
        manager.unload("SpritesBarraVida/vida1.png");
        manager.unload("SpritesBarraVida/vida2.png");
        manager.unload("SpritesBarraVida/vida3.png");
        manager.unload("SpritesBarraVida/vida4.png");
        manager.unload("SpritesBarraVida/vida5.png");
        manager.unload("SpritesBarraVida/vida6.png");
        manager.unload("SpritesBarraVida/vida7.png");
        manager.unload("SpritesBarraVida/vidaFull.png");
        manager.unload("SpritesSlime/slimePiso.png");
        manager.unload("FondosTutorial/bannerLeft1.png");
        manager.unload("FondosTutorial/bannerLeft2.png");
        manager.unload("FondosTutorial/bannerRight1.png");
        manager.unload("FondosTutorial/bannerRight2.png");
        manager.unload("FondosTutorial/bannerRight3.png");
        manager.unload("FondosTutorial/linea.png");
    }

    private class ProcesadorEntrada implements InputProcessor {
        private Vector3 v = new Vector3();
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
            v.set(screenX, screenY, 0);
            camaraHUD.unproject(v);

            if(v.x>=btnPausa.getX() && v.x<=(btnPausa.getX()+btnPausa.getWidth()))
                if(v.y>=btnPausa.getY() && v.y<=(btnPausa.getY()+btnPausa.getHeight())){
                    inPauseEvent();
                }
            if (v.x < Pantalla.ANCHO/2 && punteroHorizontal == -1) {
                // Horizontal
                punteroHorizontal = pointer;
                xHorizontal = v.x;
            } else if (v.x >= Pantalla.ANCHO/2 && punteroVertical == INACTIVO ) {
                // Vertical
                punteroVertical = pointer;
                yVertical = v.y;
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (pointer == punteroHorizontal) {
                punteroHorizontal = INACTIVO;
                kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
                //dx = 0; // Deja de moverse en x
            } else if (pointer == punteroVertical) {
                punteroVertical = INACTIVO;
                dy = 0; // Deja de moverse en y
            }
            tiempoTouch = 2.0f;
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            v.set(screenX, screenY, 0);
            camaraHUD.unproject(v);
            if(estadoTutorial==EstadoTutorial.SLIDE || estadoTutorial==EstadoTutorial.JUMP || !settings.getBoolean("Tutorial1",true)) {
                if (pointer == punteroHorizontal && Math.abs(v.x - xHorizontal) > UMBRAL) {
                    if (v.x > xHorizontal) {
                        kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_DERECHA);
                        dx = DELTA_X;   // Derecha
                    } else {
                        kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_IZQUIERDA);
                        dx = -DELTA_X;  // Izquierda
                    }
                    xHorizontal = v.x;
                }
            }
            if(estadoTutorial==EstadoTutorial.JUMP || !settings.getBoolean("Tutorial1",true)) {
                if (pointer == punteroVertical && Math.abs(v.y - yVertical) > UMBRAL) {
                    if (v.y > yVertical && kai.getEstadoSalto() != Kai.EstadoSalto.SUBIENDO) {
                        kai.saltar();
                        saltos++;
                        dy = DELTA_Y;   // Arriba
                    }
                }
            }
            if(currentLevel.getInteger("Nivel",1)>=2) {
                if (pointer == punteroVertical && Math.abs(v.y - yVertical) > UMBRAL) {
                    if (v.y < yVertical && estadoDisparo != EstadoDisparo.DISPARANDO) {
                        disparar();
                        shoots++;
                        dy = -DELTA_Y;  // Abajo
                    }
                    yVertical = v.y;
                }
            }
            return true;
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


    public enum EstadoDisparo {
        DISPARANDO,
        LIBRE
    }

    private enum EstadoTutorial{
        FINGERS,
        SLIDE,
        JUMP,
        SHOOT
    }
}