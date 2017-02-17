package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
 * Created by Adrian on 17/02/2017.
 */
public class PantallaSettings extends Pantalla {
    private final colourlessSoul menu;

    //Escena
    private Stage escena;
    private SpriteBatch batch;
    //texturas
    private Texture texturaFondo;
    private Texture texturaMusica;
    private Texture texturaSonidos;
    private Texture texturaBotonesEnable;
    private Texture texturaBotonRegreso;



    public PantallaSettings(colourlessSoul menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Botones

        TextureRegionDrawable trdBtnMusic = new TextureRegionDrawable(new TextureRegion(texturaMusica));
        ImageButton btnMusica = new ImageButton(trdBtnMusic);
        btnMusica.setPosition(2*ANCHO/3, 2*ALTO/3-2*btnMusica.getHeight());
        escena.addActor(btnMusica);

        TextureRegionDrawable trdBtnSonidos = new TextureRegionDrawable(new TextureRegion(texturaSonidos));
        ImageButton btnsounds = new ImageButton(trdBtnSonidos);
        btnsounds.setPosition(2*ANCHO/3, 2*ALTO/3-3*btnsounds.getHeight());
        escena.addActor(btnsounds);

        TextureRegionDrawable trdBtnBotonesEnable = new TextureRegionDrawable(new TextureRegion(texturaBotonesEnable));
        ImageButton btnEnable = new ImageButton(trdBtnBotonesEnable);
        btnEnable.setPosition(2*ANCHO/3, 2*ALTO/3-4*btnEnable.getHeight());
        escena.addActor(btnEnable);

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonRegreso));
        ImageButton btnRegreso = new ImageButton(trdBtnBack);
        btnRegreso.setPosition(0,0);
        escena.addActor(btnRegreso);

        // Evento del boton
        btnRegreso.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Hiciste click en Regreasar");
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }



    private void cargarTexturas() {
        texturaFondo = new Texture("fondoPrincipal.jpg");
        texturaBotonesEnable = new Texture("Pausa.png");
        texturaMusica=new Texture("Gato-1.png");
        texturaSonidos=new Texture("Baba3.png");
        texturaBotonRegreso=new Texture("backButton.png");

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
        texturaSonidos.dispose();
        texturaMusica.dispose();
        texturaBotonesEnable.dispose();
        texturaFondo.dispose();
    }
}
