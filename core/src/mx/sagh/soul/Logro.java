package mx.sagh.soul;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by User on 15/02/2017.
 */

public class Logro extends Objeto{

    public EstadoLogro estado;
    // Movimiento
    private float xOriginal;
    private final int MAX_SWIPES = 4;
    private int swipeCount;


    public Logro(Texture textura, float x, float y) {
        super(textura, x, y);
        estado = EstadoLogro.ESTATICO;
        xOriginal = sprite.getX();
        swipeCount = 0;
    }

    // Actualiza la posición del objeto
    public void actualizar(float delta) {
        if(swipeCount<MAX_SWIPES && estado == EstadoLogro.CAMBIANDO_IZQ){
            sprite.setX(sprite.getX()-20);
            PantallaLogros.posX-=4;// -20 / 5(niveles) pixeles por render
            if(xOriginal-sprite.getX()>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = sprite.getX();
                swipeCount++;
            }
        }
        if(swipeCount>0 && estado == EstadoLogro.CAMBIANDO_DER){
            sprite.setX(sprite.getX()+20);
            PantallaLogros.posX+=4;
            if(sprite.getX()-xOriginal>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = sprite.getX();
                swipeCount--;
            }
        }
    }
}