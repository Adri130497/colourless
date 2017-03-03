package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaPrincipal extends Pantalla {
    private final colourlessSoul menu;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonPausa;
    private Texture texturaKai;
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
    //Escena
    private Stage escena;
    private SpriteBatch batch;
    //HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    private Stage escenaHUD;

    public PantallaPrincipal(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();
        crearHUD();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();

        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Boton
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(2*ANCHO/3+220,2*ALTO/3-btnPausa.getHeight()+220);
        escena.addActor(btnPausa);

        //Kai
        TextureRegionDrawable Kai = new TextureRegionDrawable(new TextureRegion(texturaKai));
        ImageButton personajekai = new ImageButton(Kai);
        personajekai.setPosition(0,0);
        escena.addActor(personajekai);

        //Baba
        TextureRegionDrawable Baba = new TextureRegionDrawable(new TextureRegion(texturaBaba));
        ImageButton personajeBaba = new ImageButton(Baba);
        personajeBaba.setPosition(ANCHO/2+220,ALTO/35-30);
        escena.addActor(personajeBaba);
        //Pez
        TextureRegionDrawable Peces = new TextureRegionDrawable(new TextureRegion(texturaPez));
        ImageButton coinspeces = new ImageButton(Peces);
        coinspeces.setPosition(ANCHO/2-300,ALTO/35-10);
        escena.addActor(coinspeces);

        //Poción
        TextureRegionDrawable Pocion = new TextureRegionDrawable(new TextureRegion(texturaPocion));
        ImageButton pociones = new ImageButton(Pocion);
        pociones.setPosition(ANCHO/2,ALTO/35-10);
        escena.addActor(pociones);

        //Score
        TextureRegionDrawable Score = new TextureRegionDrawable(new TextureRegion(texturaScore));
        ImageButton puntaje = new ImageButton(Score);
        puntaje.setPosition(ANCHO/2-600,ALTO/35+680);
        escena.addActor(puntaje);

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




        // Evento del boton
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escena.addActor(imgPause);
                escena.addActor(imgGamePaused);
                escena.addActor(btnResume);
                escena.addActor(btnRestart);
                escena.addActor(btnSettings);
                escena.addActor(btnMainMenu);

                btnResume.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
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

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaBotonPausa = new Texture("Pausa.png");
        texturaKai=new Texture("Gato-1.png");
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

        Touchpad pad = new Touchpad(20, estilo);
        pad.setBounds(0, 0, 200, 200);
        pad.setColor(1,1,1,.1f);

        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                if (pad.getKnobPercentX()>0.20) {
                    //Kai.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                } else if (pad.getKnobPercentX()<-0.20){
                    //Kai.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else {
                    //Kai.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
            }
        });

        escenaHUD = new Stage(vistaHUD);
        escenaHUD.addActor(pad);
    }

    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
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
        texturaFondo.dispose();
        texturaBotonPausa.dispose();
        texturaKai.dispose();
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
    }
}