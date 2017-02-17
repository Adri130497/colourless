package mx.sagh.soul;

/**
 * Created by Aldo on 14/02/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaPrincipal extends Pantalla {
    private final colourlessSoul menu;

    //texturas
    private Texture texturaFondo;
    private Texture texturaBotonPausa;
    private Texture texturaKai;
    private Texture texturaPez;
    private Texture texturaPocion;
    private Texture texturaBaba;
    private Texture texturaScore;
    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaPrincipal(colourlessSoul menu) {
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
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Boton

        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(2*ANCHO/3+220,2*ALTO/3-btnPausa.getHeight()+220);
        escena.addActor(btnPausa);
        //KAi
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
        //Pocion
        TextureRegionDrawable Pocion = new TextureRegionDrawable(new TextureRegion(texturaPocion));
        ImageButton pociones = new ImageButton(Pocion);
        pociones.setPosition(ANCHO/2,ALTO/35-10);
        escena.addActor(pociones);
        //Score
        TextureRegionDrawable Score = new TextureRegionDrawable(new TextureRegion(texturaScore));
        ImageButton puntaje = new ImageButton(Score);
        puntaje.setPosition(ANCHO/2-600,ALTO/35+680);
        escena.addActor(puntaje);


        // Evento del boton
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaPausa(menu));
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
    }


    @Override
    public void render(float delta) {
        // 60 x seg
        borrarPantalla();
        escena.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
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
        texturaBotonPausa.dispose();
    }
}