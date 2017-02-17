package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class PantallaLogros extends Pantalla {
    private final colourlessSoul menu;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonRetorno;
    private Texture texturaBotonAnterior;
    private Texture texturaBotonSiguiente;
    private Texture texturaLogro;

    // Las 5 pantallas de logros
    private final int NUM_NIVELES = 5;
    private Array<Objeto> arrLogros;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaLogros(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        // Cuando cargan la pantalla
        cargarTexturas();
        crearObjetos();
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

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRetorno));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(0,0);
        escena.addActor(btnBack);

        TextureRegionDrawable trdBtnPrev = new TextureRegionDrawable(new TextureRegion(texturaBotonAnterior));
        ImageButton btnPrev = new ImageButton(trdBtnPrev);
        btnPrev.setPosition(ANCHO/2-texturaLogro.getWidth()/2-texturaBotonAnterior.getWidth(),ALTO/2);
        escena.addActor(btnPrev);

        TextureRegionDrawable trdBtnNext = new TextureRegionDrawable(new TextureRegion(texturaBotonSiguiente));
        ImageButton btnNext = new ImageButton(trdBtnNext);
        btnNext.setPosition(ANCHO/2+texturaLogro.getWidth()/2,ALTO/2);
        escena.addActor(btnNext);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaMenu(menu));
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
                Gdx.app.log("clicked","Hiciste click en Next");
                for(Objeto obj : arrLogros){
                    Logro logro = (Logro)obj;
                    logro.estado=EstadoLogro.CAMBIANDO_DER;
                }
            }
        });


        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("Menu.jpeg");
        texturaBotonRetorno = new Texture("back.png");
        texturaBotonAnterior = new Texture("back.png");
        texturaBotonSiguiente = new Texture("siguiente.png");
        texturaLogro = new Texture("achievsScreen1.png");
    }


    @Override
    public void render(float delta) {
        // 60 x seg
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
    }
}