package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class PantallaLogros extends Pantalla {
    private final ColourlessSoul menu;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRetorno;
    private Texture texturaBotonAnterior;
    private Texture texturaBotonSiguiente;
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

        // Crea los topos y los guarda en el arreglo
        arrLogros = new Array<Objeto>(NUM_NIVELES);
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

        /*TextureRegionDrawable trdBtnPrev = new TextureRegionDrawable(new TextureRegion(texturaBotonAnterior));
        btnPrev = new ImageButton(trdBtnPrev);
        btnPrev.setPosition(ANCHO/2-texturaLogro.getWidth()/2-texturaBotonAnterior.getWidth(),ALTO/2);

        TextureRegionDrawable trdBtnNext = new TextureRegionDrawable(new TextureRegion(texturaBotonSiguiente));
        btnNext = new ImageButton(trdBtnNext);
        btnNext.setPosition(ANCHO/2+texturaLogro.getWidth()/2,ALTO/2);*/

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(ANCHO/2-texturaBotonRetorno.getWidth()/2,5);

        // Evento del boton
        /*btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaExtras(menu));
            }
        });

        btnNext.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Next");
                for(Objeto obj : arrLogros){
                    Logro logro = (Logro)obj;
                    logro.estado=EstadoLogro.CAMBIANDO_IZQ;
                }
            }
        });

        btnPrev.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Prev");
                for(Objeto obj : arrLogros){
                    Logro logro = (Logro)obj;
                    logro.estado=EstadoLogro.CAMBIANDO_DER;
                }
            }
        });*/


        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaBotonRetorno = new Texture("replayButton.png");
        texturaBotonAnterior = new Texture("backButton.png");
        texturaBotonSiguiente = new Texture("nextButton.png");
        texturaLogro = new Texture("achievsScreen1.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        escena.addActor(btnBack);
        actualizarObjetos(delta);   // mandamos el tiempo para calcular distancia

        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
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
        texturaBotonRetorno.dispose();
        texturaBotonAnterior.dispose();
        texturaBotonSiguiente.dispose();
        texturaLogro.dispose();
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
                Gdx.app.log("clicked", "Me hicieron click");
                clickSound.play();
                while (clickSound.isPlaying()) if (clickSound.getPosition() > 0.5f) break;
                menu.setScreen(new PantallaExtras(menu));
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