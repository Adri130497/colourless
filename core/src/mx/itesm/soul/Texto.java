package mx.itesm.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by User on 24/03/2017.
 */

public class Texto extends Actor {
    private BitmapFont font;
    public EstadoLogro estado;
    private float xOriginal;

    public Texto(){
        font = new BitmapFont(Gdx.files.internal("WorldOfWater.fnt"));
    }



    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y){
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.draw(batch,glyp,x,y);
    }

    // Actualiza la posición del objeto
    public void actualizar() {
        if(estado == EstadoLogro.CAMBIANDO_IZQ){
            this.setX(this.getX()-20);
            if(xOriginal-this.getX()>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = this.getX();
            }
        }
        if(estado == EstadoLogro.CAMBIANDO_DER){
            this.setX(this.getX()+20);
            if(this.getX()-xOriginal>=Pantalla.ANCHO) {
                estado = EstadoLogro.ESTATICO;
                xOriginal = this.getX();
            }
        }

    }
}
