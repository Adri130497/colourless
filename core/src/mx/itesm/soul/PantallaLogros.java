package mx.itesm.soul;

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

import static mx.itesm.soul.ColourlessSoul.clickSound;

public class PantallaLogros extends mx.itesm.soul.Pantalla {
    private final ColourlessSoul menu;
    private final AssetManager manager;

    //Preferencias
    Preferences prefs = Gdx.app.getPreferences("Achievements");
    private mx.itesm.soul.Texto texto;
    public static float posX;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRetorno;
    private Texture texturaLogro;

    //botones
    private ImageButton btnBack;

    // Las 5 pantallas de logros
    private final int NUM_NIVELES = 5;
    private Array<mx.itesm.soul.Objeto> arrLogros;

    //Preferencias
    private Preferences settings = Gdx.app.getPreferences("Settings");

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    //Títulos de nivel
    private Array<String> titles;

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
        texto = new mx.itesm.soul.Texto();
        titles = new Array();
        titles.add("The Great Loss");
        titles.add("The Courage");
        titles.add("The Spring");
        titles.add("The Hope");
        titles.add("The Thing");
        // Crea los topos y los guarda en el arreglo
        arrLogros = new Array<mx.itesm.soul.Objeto>(NUM_NIVELES);
        posX = ANCHO / 3 + 50;
        for (int x = 0; x < NUM_NIVELES; x++) {
            float posX = (x* mx.itesm.soul.Pantalla.ANCHO) + (mx.itesm.soul.Pantalla.ANCHO/2 - texturaLogro.getWidth()/2);
            float posY = (mx.itesm.soul.Pantalla.ALTO/2 - texturaLogro.getHeight()/2);
            mx.itesm.soul.Logro logro = new mx.itesm.soul.Logro(texturaLogro, posX, posY);
            arrLogros.add(logro);
        }
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Botones

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        btnBack = new ImageButton(trdBtnBack);
        btnBack.setSize(120,120);
        btnBack.setPosition(0,0);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new mx.itesm.soul.PantallaExtras(menu));
            }
        });
        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = manager.get("fondoPrincipal.jpg");
        texturaBotonRetorno = manager.get("backButton.png");
        texturaLogro = manager.get("fondoFinNivel.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        escena.addActor(btnBack);
        actualizarObjetos(delta);   // mandamos el tiempo para calcular distancia

        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            menu.setScreen(new mx.itesm.soul.PantallaExtras(menu));
        batch.setProjectionMatrix(camara.combined); // Para ajustar la escala con la cámara
        batch.begin();
        dibujarObjetos(arrLogros);
        batch.end();
    }

    private void dibujarObjetos(Array<mx.itesm.soul.Objeto> arrLogros) {
        for (mx.itesm.soul.Objeto objeto : arrLogros) {
            objeto.dibujar(batch);
        }
        for(int x=0; x<NUM_NIVELES; x++) {
            texto.mostrarMensaje(batch, prefs.getString((x+1)+"-Score", "N/A"), (x* mx.itesm.soul.Pantalla.ANCHO) + posX, ALTO / 2 + 125);
            texto.mostrarMensaje(batch, prefs.getString((x+1)+"-Slimes", "N/A"), (x* mx.itesm.soul.Pantalla.ANCHO) + posX, ALTO / 2 - 10);
            texto.mostrarMensaje(batch, titles.get(x), (x* mx.itesm.soul.Pantalla.ANCHO) + posX + 50, ALTO / 2 + 200);
        }
    }

    private void actualizarObjetos(float delta) {
        for (mx.itesm.soul.Objeto logro : arrLogros) {
            ((mx.itesm.soul.Logro)logro).actualizar(delta);
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
        manager.unload("backButton.png");
        manager.unload("fondoFinNivel.png");
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
                if(settings.getBoolean("Sounds",true))
                    clickSound.play();
                menu.setScreen(new mx.itesm.soul.PantallaExtras(menu));
            }
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            for(mx.itesm.soul.Objeto obj : arrLogros){
                mx.itesm.soul.Logro logro = (mx.itesm.soul.Logro)obj;
                if(velocityX<0) logro.estado= mx.itesm.soul.EstadoLogro.CAMBIANDO_IZQ;
                if(velocityX>0) logro.estado= mx.itesm.soul.EstadoLogro.CAMBIANDO_DER;
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