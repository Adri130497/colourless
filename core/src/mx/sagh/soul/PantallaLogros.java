package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class PantallaLogros extends Pantalla {
    private final ColourlessSoul menu;
    private final AssetManager manager;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));

    //Preferencias
    Preferences prefs = Gdx.app.getPreferences("Achievements");
    private Texto texto;
    public static float posX;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRetorno;
    private Texture texturaLogro;

    //botones
    private ImageButton btnBack;

    // Las 5 pantallas de logros
    private final int NUM_NIVELES = 5;
    private Array<Objeto> arrLogros;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaLogros(ColourlessSoul menu) {
        this.menu = menu;
        manager = menu.getAssetManager();
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();

        Gdx.input.setInputProcessor(new GestureDetector(new Procesador()));
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        texto = new Texto();
        // Crea los topos y los guarda en el arreglo
        arrLogros = new Array<Objeto>(NUM_NIVELES);
        posX = ANCHO / 3 + 50;
        for (int x = 0; x < NUM_NIVELES; x++) {
            float posX = (x*Pantalla.ANCHO) + (Pantalla.ANCHO/2 - texturaLogro.getWidth()/2);
            float posY = (Pantalla.ALTO/2 - texturaLogro.getHeight()/2);
            Logro logro = new Logro(texturaLogro, posX, posY);
            arrLogros.add(logro);
        }
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Botones

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(ANCHO/2-texturaBotonRetorno.getWidth()/2,5);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(PantallaAjustes.prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu,Pantallas.MENU));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaBotonRetorno = manager.get("menuButton.png");
        texturaLogro = manager.get("fondoFinNivel.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        escena.addActor(btnBack);
        actualizarObjetos(delta);   // mandamos el tiempo para calcular distancia

        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaCargando(menu,Pantallas.MENU));
        }

        batch.setProjectionMatrix(camara.combined); // Para ajustar la escala con la cámara
        batch.begin();
        dibujarObjetos(arrLogros);
        batch.end();
    }

    private void dibujarObjetos(Array<Objeto> arrLogros) {
        for (Objeto objeto : arrLogros) {
            objeto.dibujar(batch);
        }
        for(int x=0; x<NUM_NIVELES; x++) {
            texto.mostrarMensaje(batch, prefs.getString((x+1)+"-Score", "N/A"), (x*Pantalla.ANCHO) + posX, ALTO / 2 + 125);
            texto.mostrarMensaje(batch, prefs.getString((x+1)+"-Slimes", "N/A"), (x*Pantalla.ANCHO) + posX, ALTO / 2 - 10);
        }
    }

    private void actualizarObjetos(float delta) {
        for (Objeto logro : arrLogros) {
            ((Logro)logro).actualizar(delta);
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
        texturaFondo.dispose();
        manager.unload("menuButton.png");
        manager.unload("backButton.png");
        manager.unload("nextButton.png");
        manager.unload("fondoFinNivel.png");
        clickSound.dispose();
    }

    private class Procesador implements GestureDetector.GestureListener {
        Vector3 v = new Vector3(); //Para convertir coordenadas del touch
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            v.set(x,y,0);
            camara.unproject(v);
            if (v.x>btnBack.getX() && v.x<(btnBack.getX()+btnBack.getWidth())
                    && v.y>btnBack.getY() && v.y<(btnBack.getY()+btnBack.getHeight())) {
                if(PantallaAjustes.prefs.getBoolean("Sounds",true))
                    clickSound.play();
                while (clickSound.isPlaying()) if (clickSound.getPosition() > 0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
            }
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            for(Objeto obj : arrLogros){
                Logro logro = (Logro)obj;
                if(velocityX<0) logro.estado=EstadoLogro.CAMBIANDO_IZQ;
                if(velocityX>0) logro.estado=EstadoLogro.CAMBIANDO_DER;
            }
            return true;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
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