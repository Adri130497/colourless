package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaPrincipal extends Pantalla {
    private final colourlessSoul menu;

    public static final float ANCHO_MAPA = 5120;
    private OrthogonalTiledMapRenderer renderer, renderer2; // Dibuja el mapa
    private TiledMap mapa;

    //Sistema de partículas
    private ParticleEffect sistemaParticulas;

    private SpriteBatch batch;

    // Personaje
    private Kai kai;
    private Texture texturaKaiCaminando, texturaKaiReposo;

    // Música / efectos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));
    private Sound efectoCroqueta;

    // HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;
    private Touchpad pad;

    //texturas
    private Texture texturaPrimerPlano;
    private Texture texturaBotonPausa;
    private Texture texturaPez;
    private Texture texturaPocion;
    private Texture texturaBaba;
    private Texture texturaScore;
    private Texture texturaMenuPausa;
    private Texture texturaGamePaused;
    private Texture texturaResumeButton;
    private Texture texturaRestartButton;
    private Texture texturaSettingsButton;
    private Texture texturaMainMenuButton;

    // Puntos del jugador y computadora
    private int score = 0;
    private Texto texto;

    private boolean flag;
    public EstadoNivel estado;


    public PantallaPrincipal(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        sistemaParticulas = new ParticleEffect();
        flag = false;
        sistemaParticulas.load(Gdx.files.internal("pezVanish.pe"),Gdx.files.internal(""));
        if(PantallaAjustes.estadoJugabilidad == PantallaAjustes.EstadoJugabilidad.TOUCH) {
            Gdx.input.setInputProcessor(new GestureDetector(new ProcesadorEntrada()));
        }
        crearObjetos();
    }

    private void crearObjetos() {
        texto = new Texto();
        estado = EstadoNivel.ACTIVE;

        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapaColourless.tmx", TiledMap.class);

        //Cargar audios
        manager.load("bite1.mp3",Sound.class);

        manager.finishLoading();    // Carga los recursos
        mapa = manager.get("mapaColourless.tmx");

        efectoCroqueta = manager.get("bite1.mp3");

        batch = new SpriteBatch();
        renderer = new OrthogonalTiledMapRenderer(mapa, batch);
        renderer.setView(camara);


        crearHUD();
        Gdx.input.setInputProcessor(escenaHUD);
        kai = new Kai(texturaKaiCaminando, texturaKaiReposo,64,128);



        //Boton
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setSize(32,32);
        btnPausa.setPosition(2*ANCHO/3+220,2*ALTO/3-btnPausa.getHeight()+220);
        escenaHUD.addActor(btnPausa);


        //Pantalla pausa
        final Image imgPause = new Image(texturaMenuPausa);
        imgPause.setPosition(ANCHO/2-imgPause.getWidth()/2,ALTO/2-imgPause.getHeight()/2);

        final Image imgGamePaused = new Image(texturaGamePaused);
        imgGamePaused.setPosition(ANCHO/2-imgGamePaused.getWidth()/2,imgPause.getY()+imgPause.getHeight()-2*imgGamePaused.getHeight()-10);

        TextureRegionDrawable trdBtnResume = new TextureRegionDrawable(new TextureRegion(texturaResumeButton));
        final ImageButton btnResume = new ImageButton(trdBtnResume);
        btnResume.setPosition(ANCHO/2-btnResume.getWidth()/2,imgGamePaused.getY()-2*btnResume.getHeight());

        TextureRegionDrawable trdBtnRestart = new TextureRegionDrawable(new TextureRegion(texturaRestartButton));
        final ImageButton btnRestart = new ImageButton(trdBtnRestart);
        btnRestart.setPosition(ANCHO/2-btnRestart.getWidth()/2,btnResume.getY()-35-btnRestart.getHeight());

        TextureRegionDrawable trdBtnSettings = new TextureRegionDrawable(new TextureRegion(texturaSettingsButton));
        final ImageButton btnSettings = new ImageButton(trdBtnSettings);
        btnSettings.setPosition(ANCHO/2-btnSettings.getWidth()/2,btnRestart.getY()-35-btnSettings.getHeight());

        TextureRegionDrawable trdBtnMainMenu = new TextureRegionDrawable(new TextureRegion(texturaMainMenuButton));
        final ImageButton btnMainMenu = new ImageButton(trdBtnMainMenu);
        btnMainMenu.setPosition(ANCHO/2-btnMainMenu.getWidth()/2,btnSettings.getY()-35-btnMainMenu.getHeight());




        // Evento del botón
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                estado = EstadoNivel.PAUSED;
                pad.remove();
                escenaHUD.addActor(imgPause);
                escenaHUD.addActor(imgGamePaused);
                escenaHUD.addActor(btnResume);
                escenaHUD.addActor(btnRestart);
                escenaHUD.addActor(btnSettings);
                escenaHUD.addActor(btnMainMenu);

                btnResume.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        estado = EstadoNivel.ACTIVE;
                        escenaHUD.addActor(pad);
                        clickSound.play();
                        while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                        imgPause.remove();
                        imgGamePaused.remove();
                        btnResume.remove();
                        btnRestart.remove();
                        btnSettings.remove();
                        btnMainMenu.remove();
                        clickSound.stop();
                    }
                });

                btnSettings.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        clickSound.play();
                        while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                        PantallaAjustes.estado = EstadoInvocado.PANTALLA_PRINCIPAL;
                        menu.setScreen(new PantallaAjustes(menu));
                        clickSound.stop();
                    }
                });

                btnRestart.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        clickSound.play();
                        while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                        menu.setScreen(new PantallaPrincipal(menu));
                        clickSound.stop();
                    }
                });

                btnMainMenu.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        clickSound.play();
                        while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                        menu.setScreen(new PantallaMenu(menu));
                        clickSound.stop();
                    }
                });

            }
        });
        Gdx.input.setCatchBackKey(true);
    }

    private void crearHUD() {
        // Cámara HUD
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();

        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        // HUD
        Skin skin = new Skin();
        skin.add("padBack", new Texture("padBack.png"));
        skin.add("padKnob", new Texture("padKnob.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padBack");
        estilo.knob = skin.getDrawable("padKnob");

        pad = new Touchpad(20, estilo);
        pad.setBounds(0, 0, 200, 200);
        pad.setColor(1,1,1,0.5f);

        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                if (pad.getKnobPercentX()>0.20) {
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_DERECHA);
                } else if (pad.getKnobPercentX()<-0.20){
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_IZQUIERDA);
                } else if(pad.getKnobPercentY()>0.20) {
                    kai.saltar();
                } else {
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
                }
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(pad);
    }

    private void cargarTexturas() {
        texturaPrimerPlano = new Texture("primerPlano_01.png");
        texturaBotonPausa = new Texture("pauseButton.png");
        texturaBaba=new Texture("Baba3.png");
        texturaPez=new Texture("pez.png");
        texturaPocion=new Texture("pocion.png");
        texturaScore=new Texture("ingamescore.png");
        texturaMenuPausa=new Texture("fondoMadera.png");
        texturaGamePaused=new Texture("gamePaused.png");
        texturaResumeButton=new Texture("resumeButton.png");
        texturaRestartButton=new Texture("restartButton.png");
        texturaSettingsButton=new Texture("settingsButton.png");
        texturaMainMenuButton=new Texture("mainMenuButton.png");
        texturaKaiCaminando = new Texture("kaiWalkingSprite.png");
        texturaKaiReposo = new Texture("kaiRestingSprite.png");
    }

    @Override
    public void render(float delta) {
        if(estado != EstadoNivel.PAUSED) {
            kai.actualizar(mapa);
            actualizarCamara();
        }

        // 60 x seg
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        renderer.setView(camara);
        renderer.render();

        batch.begin();

        if (flag) {
            sistemaParticulas.update(delta);
            sistemaParticulas.draw(batch);
            if (sistemaParticulas.getEmitters().first().getActiveCount() >= 1)
                sistemaParticulas.allowCompletion();
        }
        if (kai.recolectarItems(mapa)) {
            int x = (int) (kai.sprite.getX());
            int y = (int) (kai.sprite.getY());
            sistemaParticulas = new ParticleEffect();
            sistemaParticulas.load(Gdx.files.internal("pezVanish.pe"), Gdx.files.internal(""));
            sistemaParticulas.getEmitters().first().setPosition(x + kai.sprite.getWidth(), y + kai.sprite.getHeight());
            efectoCroqueta.play();
            score++;
            flag = true;
        }

        if (kai.esAlcanzado(mapa, camara)) {
            //menu.setScreen(new PantallaMenu(menu));
        }
        kai.dibujar(batch);
        batch.end();

        //OTRA CAMARA
        batch.setProjectionMatrix(camaraHUD.combined);

        escenaHUD.draw();

        if(PantallaAjustes.estadoJugabilidad == PantallaAjustes.EstadoJugabilidad.TOUCH)
            pad.remove();
        batch.begin();
        texto.mostrarMensaje(batch, Integer.toString(score), ANCHO / 2 - 600, ALTO / 35 + 680);
        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            menu.setScreen(new PantallaMenu(menu));
        }

    }


    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando está en la primera y última parte del mundo
    private void actualizarCamara() {
        float posX = kai.sprite.getX(); //siempre es el sprite quien me da la x o la y del personaje
        // Si está en la parte 'media'
        /*if (posX>=ANCHO/2 && posX<=ANCHO_MAPA-ANCHO/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO_MAPA-ANCHO/2) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO_MAPA-ANCHO/2, camara.position.y, 0);
        } else if ( posX<ANCHO/2 ) { // La primera mitad
            camara.position.set(ANCHO/2, ALTO/2,0);
        }*/


        camara.position.set(camara.position.x+15*Gdx.graphics.getDeltaTime(), camara.position.y, 0);

        camara.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        escenaHUD.dispose();
        texturaPrimerPlano.dispose();
        texturaBotonPausa.dispose();
        texturaBaba.dispose();
        texturaPez.dispose();
        texturaPocion.dispose();
        texturaScore.dispose();
        texturaMenuPausa.dispose();
        texturaGamePaused.dispose();
        texturaResumeButton.dispose();
        texturaRestartButton.dispose();
        texturaSettingsButton.dispose();
        texturaMainMenuButton.dispose();
        clickSound.dispose();
        texturaKaiCaminando.dispose();
        texturaKaiReposo.dispose();
    }

    private class ProcesadorEntrada implements GestureDetector.GestureListener {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }
/*
        if (pad.getKnobPercentX()>0.20) {
            kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_DERECHA);
        } else if (pad.getKnobPercentX()<-0.20){
            kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_IZQUIERDA);
        } else if(pad.getKnobPercentY()>0.20) {
            kai.saltar();
        } else {
            kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
        }*/

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            Vector3 v = new Vector3(x,y,0);
            camaraHUD.unproject(v); //Transforma de un sistema a otro
            if(v.x<ANCHO/2){
                if(deltaX>0)
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_DERECHA);
                else if (deltaX<0)
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.MOV_IZQUIERDA);
                else
                    kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
            }
            else {

                Gdx.app.log("Mitad:", "DER");
            }
            //Gdx.app.log("Dragged: ",Float.toString(v.x)+", "+Float.toString(v.y)+" deltaX: "+ Float.toString(deltaX));

            return true;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            kai.setEstadoMovimiento(Kai.EstadoMovimiento.QUIETO);
            return true;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }
    }
}