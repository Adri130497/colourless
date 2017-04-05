package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
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
    private final ColourlessSoul menu;
    public static EstadoInvocado estado;
    public static  EstadoJugabilidad estadoJugabilidad;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));

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


    public PantallaAjustes(ColourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();

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
        if(estadoJugabilidad == EstadoJugabilidad.TOUCH)
            escena.addActor(imgControlT);

        final Image imgControlB = new Image(texturaControlButton);
        imgControlB.setPosition(ANCHO/2-imgControlB.getWidth()/2,imgControlChange.getY()+1);
        if(estadoJugabilidad == EstadoJugabilidad.BOTONES)
            escena.addActor(imgControlB);

        Image imgControlMS = new Image(texturaMusicSounds);
        imgControlMS.setPosition(ANCHO/2-imgControlMS.getWidth()/2,imgSettings.getY()+4*imgControlChange.getHeight());
        escena.addActor(imgControlMS);

        //Botones

        TextureRegionDrawable trdBtnMusicOn = new TextureRegionDrawable(new TextureRegion(texturaMusicaOn));
        final ImageButton btnMusicaOn = new ImageButton(trdBtnMusicOn);
        btnMusicaOn.setSize(120,120);
        btnMusicaOn.setPosition(imgControlMS.getX()-23,imgControlMS.getY()-11.5f);
        escena.addActor(btnMusicaOn);

        TextureRegionDrawable trdBtnMusicOff = new TextureRegionDrawable(new TextureRegion(texturaMusicaOff));
        final ImageButton btnMusicaOff = new ImageButton(trdBtnMusicOff);
        btnMusicaOff.setSize(120,120);
        btnMusicaOff.setPosition(imgControlMS.getX()-23,imgControlMS.getY()-11.5f);
        //escena.addActor(btnMusicaOff);

        TextureRegionDrawable trdBtnSoundsOn = new TextureRegionDrawable(new TextureRegion(texturaSonidosOn));
        final ImageButton btnSoundsOn = new ImageButton(trdBtnSoundsOn);
        btnSoundsOn.setSize(120,120);
        btnSoundsOn.setPosition(imgControlMS.getX()+23+imgControlMS.getWidth()-btnSoundsOn.getWidth(),btnMusicaOn.getY());
        escena.addActor(btnSoundsOn);

        TextureRegionDrawable trdBtnSoundsOff = new TextureRegionDrawable(new TextureRegion(texturaSonidosOff));
        final ImageButton btnSoundsOff = new ImageButton(trdBtnSoundsOff);
        btnSoundsOff.setSize(120,120);
        btnSoundsOff.setPosition(imgControlMS.getX()+23+imgControlMS.getWidth()-btnSoundsOff.getWidth(),btnMusicaOn.getY());
        //escena.addActor(btnSoundsOff);

        TextureRegionDrawable trdBtnL = new TextureRegionDrawable(new TextureRegion(texturaBotonL));
        final ImageButton btnL = new ImageButton(trdBtnL);
        btnL.setSize(120,120);
        btnL.setPosition(imgControlChange.getX()-23,imgControlChange.getY()-11.5f);
        escena.addActor(btnL);

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRegreso));
        ImageButton btnRegreso = new ImageButton(trdBtnBack);
        btnRegreso.setSize(120,120);
        btnRegreso.setPosition(0,0);
        escena.addActor(btnRegreso);


        // Evento del boton
        btnRegreso.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                if(estado == EstadoInvocado.PANTALLA_MENU)
                    menu.setScreen(new PantallaMenu(menu));
                else if(estado ==EstadoInvocado.PANTALLA_PRINCIPAL)
                    menu.setScreen(new PantallaPrincipal(menu));
                clickSound.stop();
            }
        });

        btnMusicaOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escena.addActor(btnMusicaOff);
                btnMusicaOn.remove();
            }
        });

        btnMusicaOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escena.addActor(btnMusicaOn);
                btnMusicaOff.remove();
            }
        });

        btnSoundsOn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escena.addActor(btnSoundsOff);
                btnSoundsOn.remove();
            }
        });

        btnSoundsOff.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escena.addActor(btnSoundsOn);
                btnSoundsOff.remove();
            }
        });

        btnL.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(imgControlB.getStage() == null){
                    estadoJugabilidad = EstadoJugabilidad.BOTONES;
                    Gdx.app.log("LOG","TOUCH OFF");
                    escena.addActor(imgControlB);
                    imgControlT.remove();
                }
                else if(imgControlT.getStage() == null){
                    estadoJugabilidad = EstadoJugabilidad.TOUCH;
                    Gdx.app.log("LOG","BUTTONS OFF");
                    escena.addActor(imgControlT);
                    imgControlB.remove();
                }
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }



    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaSettings = new Texture("fondoMadera.png");
        texturaMusicaOn = new Texture("musicOn.png");
        texturaMusicaOff = new Texture("musicOff.png");
        texturaSonidosOn = new Texture("soundsOn.png");
        texturaSonidosOff = new Texture("soundsOff.png");
        texturaMusicSounds = new Texture("musicSounds.png");
        texturaControlsChange = new Texture("controlsChange.png");
        texturaControlTouch = new Texture("controlsTouch.png");
        texturaControlButton = new Texture("controlsButton.png");
        texturaBotonL = new Texture("changeButtonL.png");
        texturaBotonRegreso = new Texture("backButton.png");
    }
    @Override
    public void render(float delta) {
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
        texturaFondo.dispose();
        texturaSettings.dispose();
        texturaMusicaOn.dispose();
        texturaMusicaOff.dispose();
        texturaSonidosOn.dispose();
        texturaSonidosOff.dispose();
        texturaMusicSounds.dispose();
        texturaControlsChange.dispose();
        texturaBotonL.dispose();
        texturaBotonRegreso.dispose();
        clickSound.dispose();
    }

    public enum EstadoJugabilidad {
        BOTONES,
        TOUCH
    }
}
