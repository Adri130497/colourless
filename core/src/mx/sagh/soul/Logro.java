package mx.sagh.soul;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by User on 15/02/2017.
 */

public class Logro extends Objeto{

    public EstadoLogro estado;
    // Movimiento
    private float vx = 40;   // velocidad en y (pixeles por segundo)
    private float xActual; // El tamaño real actual (cambiando)
    private float xOriginal;   // Altura inicial (no cambia)


    public Logro(Texture textura, float x, float y) {
        super(textura, x, y);
        estado = EstadoLogro.ESTATICO;
        xOriginal = sprite.getX();
    }

    // Actualiza la posición del objeto
    public void actualizar(float delta) {
        if(estado == EstadoLogro.CAMBIANDO_IZQ){
            sprite.setX(sprite.getX()-20);
            if(xOriginal-sprite.getX()>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = sprite.getX();
            }
        }
        if(estado == EstadoLogro.CAMBIANDO_DER){
            sprite.setX(sprite.getX()+20);
            if(sprite.getX()-xOriginal>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = sprite.getX();
            }
        }

    }

    public boolean contiene(Vector3 v) {
        return sprite.getBoundingRectangle().contains(v.x,v.y); //Para saber si se está tocando al topo
    }

    public void desplazar(Vector3 v) {
        sprite.setX(sprite.getX()-(-Pantalla.ANCHO/2+v.x)-sprite.getWidth()/2);
    }
}