package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Esta clase representa cualquier pantalla que se quiera mostrar
 *
 * Contiene una cámara y la vista (@see StretchViewport)
 */
public abstract class Pantalla implements Screen
{
    public static final float ANCHO = 1170;
    public static final float ALTO = 640;

    // Disponibles en las subclases
    protected OrthographicCamera camara;
    protected Viewport vista;

    public Pantalla() {
        camara = new OrthographicCamera(ANCHO, ALTO);
        camara.position.set(ANCHO/2, ALTO/2, 0);
        camara.update();
        vista = new StretchViewport(ANCHO, ALTO, camara);
    }

    protected void borrarPantalla() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }
}
