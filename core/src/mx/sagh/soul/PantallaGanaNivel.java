package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
 * Created by Adrian on 18/04/2017.
 */
public class PantallaGanaNivel extends Pantalla {
    private final ColourlessSoul menu;

    //sonidos
    private Music clickSound = Gdx.audio.newMusic(Gdx.files.internal("click.mp3"));
    //texturas
    private Image imgPasar;
    private Texture texturaBotonSigNiv;
    private Texture texturaBotonMenu;
    private Texture texturaSigNiv;

    //Escena
    private Stage escena;
    private SpriteBatch batch;

    public PantallaGanaNivel(ColourlessSoul menu) {
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
        escena = new Stage(vista, batch);

        imgPasar = new Image(texturaSigNiv);
        imgPasar.setPosition(ANCHO/2-imgPasar.getWidth()/2,ALTO/2-imgPasar.getHeight()/2);

        escena.addActor(imgPasar);


        //Botones
        TextureRegionDrawable trdBtnNiv = new TextureRegionDrawable(new TextureRegion(texturaBotonSigNiv));
        ImageButton btnNivel = new ImageButton(trdBtnNiv);
        btnNivel.setPosition(ANCHO/2+150,ALTO/4);
        escena.addActor(btnNivel);

        TextureRegionDrawable trdBtnMain = new TextureRegionDrawable(new TextureRegion(texturaBotonMenu));
        ImageButton btnMenu = new ImageButton(trdBtnMain);
        btnMenu.setPosition(ANCHO/3-150,ALTO/4);
        escena.addActor(btnMenu);

        // Evento del boton
        btnNivel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
                clickSound.stop();
                imgPasar.remove();
            }
        });

        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                clickSound.play();
                while(clickSound.isPlaying()) if(clickSound.getPosition()>0.5f) break;
                menu.setScreen(new PantallaCargando(menu, Pantallas.MENU));
                clickSound.stop();
            }
        });

        Gdx.input.setInputProcessor(escena);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaSigNiv = new Texture("fondoMadera.png");
        texturaBotonSigNiv = new Texture("Next.png");
        texturaBotonMenu = new Texture("mainMenuButton.png");


    }

    @Override
    public void render(float delta) {
        // 60 x seg
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
        texturaSigNiv.dispose();
        texturaBotonMenu.dispose();
        texturaBotonSigNiv.dispose();
        clickSound.dispose();
        clickSound.stop();

    }
}
