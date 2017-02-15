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

public class PantallaLogros extends Pantalla {
    private final colourlessSoul menu;

    //texturas
    private Texture texturaFondo;
    private Texture texturaLogroNivel1;
    private Texture texturaBotonPausa;

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
        escena = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escena.addActor(imgFondo);

        //Boton

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBotonPausa));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(0,0);
        escena.addActor(btnBack);

        TextureRegionDrawable trdAchLev1 = new TextureRegionDrawable(new TextureRegion(texturaLogroNivel1));
        ImageButton btnAchLev1 = new ImageButton(trdAchLev1);
        btnAchLev1.setPosition(ANCHO/2-btnAchLev1.getWidth()/2,ALTO/2-btnAchLev1.getHeight()/2);
        escena.addActor(btnAchLev1);

        // Evento del boton
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoMenu.jpg");
        texturaBotonPausa = new Texture("back_button.png");
        texturaLogroNivel1 = new Texture("achievsScreen1.png");
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
