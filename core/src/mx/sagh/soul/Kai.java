package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by User on 17/03/2017.
 */

public class Kai extends Objeto{
    // Variables para el salto
    private final float G = 98.1f;  // Gravedad
    private final float velocidadInicial = 198;  // Velocidad de salida (hacia arriba)
    private float tiempoVuelo;  // Tiempo de vuelo TOTAL

    private float alturaVolando;  // La posición actual cuando está saltando
    private float tiempoVolando;    // El tiempo que ha transcurrido desde que inició el salto
    private float yInicial;     // Posición donde inicia el salto

    private final float VELOCIDAD_X = 4;      // Velocidad horizontal

    private Animation<TextureRegion> spriteAnimado, spriteReposo, spriteBrincando, spriteCayendo, spriteAsustado;           // Animación caminando, en reposo y parándose
    private float timerAnimacion, timerAnimacion2, timerAnimacion3, timerAnimacion4, timerAnimacion5;                                           // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;

    // Salto
    private EstadoSalto estadoSalto = EstadoSalto.EN_PISO;

    // Sonidos
    private Sound efectoCroqueta, efectoPocion, efectoPowerDown;

    private Preferences prefs = Gdx.app.getPreferences("Settings");

    public Kai(Texture texturaCaminando, Texture texturaReposo, Texture texturaBrincando, Texture texturaCayendo, Texture texturaAsustado, float x, float y) {
        AssetManager manager = new AssetManager();
        //Cargar audios
        manager.load("musicSounds/bite1.mp3",Sound.class);
        manager.load("musicSounds/potion.mp3",Sound.class);
        manager.load("musicSounds/powerDown.mp3",Sound.class);
        manager.finishLoading();
        efectoCroqueta = manager.get("musicSounds/bite1.mp3");
        efectoPocion = manager.get("musicSounds/potion.mp3");;
        efectoPowerDown = manager.get("musicSounds/powerDown.mp3");

        TextureRegion texturaCompleta = new TextureRegion(texturaCaminando);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(120,120);
        spriteAnimado = new Animation(0.15f, texturaPersonaje[0][0], texturaPersonaje[0][1],
                texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4],
                texturaPersonaje[0][5], texturaPersonaje[0][6], texturaPersonaje[0][7],
                texturaPersonaje[0][8], texturaPersonaje[0][9], texturaPersonaje[0][10]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);

        texturaCompleta =  new TextureRegion(texturaReposo);
        texturaPersonaje = texturaCompleta.split(120,120);
        spriteReposo = new Animation(0.25f, texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2],texturaPersonaje[0][1]);
        spriteReposo.setPlayMode(Animation.PlayMode.LOOP);


        texturaCompleta =  new TextureRegion(texturaBrincando);
        texturaPersonaje = texturaCompleta.split(120,120);
        spriteBrincando = new Animation(0.25f, texturaPersonaje[0][0], texturaPersonaje[0][1],
                texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4],
                texturaPersonaje[0][5]);
        spriteBrincando.setPlayMode(Animation.PlayMode.LOOP);

        texturaCompleta =  new TextureRegion(texturaCayendo);
        texturaPersonaje = texturaCompleta.split(120,120);
        spriteCayendo = new Animation(0.25f, texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2]);
        spriteCayendo.setPlayMode(Animation.PlayMode.LOOP);

        texturaCompleta =  new TextureRegion(texturaAsustado);
        texturaPersonaje = texturaCompleta.split(120,120);
        spriteAsustado = new Animation(0.25f, texturaPersonaje[0][0]);

        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        timerAnimacion2 = 0;
        timerAnimacion3 = 0;
        timerAnimacion4 = 0;
        timerAnimacion5 = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        TextureRegion region;
        if(estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO)
            switch (estadoMovimiento) {
                case MOV_DERECHA:
                case MOV_IZQUIERDA:
                    timerAnimacion += Gdx.graphics.getDeltaTime();
                    // Frame que se dibujará
                    region = spriteAnimado.getKeyFrame(timerAnimacion);
                    if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                        if (!region.isFlipX()) {
                            region.flip(true,false);
                        }
                    } else {
                        if (region.isFlipX()) {
                            region.flip(true,false);
                        }
                    }
                    batch.draw(region,sprite.getX(),sprite.getY());
                    break;
                case QUIETO:
                case INICIANDO:
                    timerAnimacion2 += Gdx.graphics.getDeltaTime();
                    region = spriteReposo.getKeyFrame(timerAnimacion2);
                    batch.draw(region,sprite.getX(),sprite.getY());
                    break;
                case ASUSTADO:
                    timerAnimacion5 += Gdx.graphics.getDeltaTime();
                    region = spriteAsustado.getKeyFrame(timerAnimacion5);
                    batch.draw(region,sprite.getX(),sprite.getY());
                    break;
            }
        switch (estadoSalto){
            case SUBIENDO:
                timerAnimacion3 += Gdx.graphics.getDeltaTime();
                region = spriteBrincando.getKeyFrame(timerAnimacion3);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case BAJANDO:
                timerAnimacion4 += Gdx.graphics.getDeltaTime();
                region = spriteCayendo.getKeyFrame(timerAnimacion4);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
        }
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento y estadoSalto
    public void actualizar(float delta, TiledMap mapa, OrthographicCamera camara) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                moverHorizontal(delta, mapa, camara);
                break;
        }
        // Calcula la nueva posición (por ahora cuando está saltando)
        if ( estadoSalto == EstadoSalto.SUBIENDO || estadoSalto ==EstadoSalto.BAJANDO) {
            tiempoVolando += delta * 5;   // El factor DES/ACELERA
            alturaVolando = velocidadInicial * tiempoVolando - 0.5f * G * tiempoVolando * tiempoVolando;
            if(tiempoVolando < tiempoVuelo/2)
                estadoSalto = EstadoSalto.SUBIENDO;
            else
                estadoSalto = EstadoSalto.BAJANDO;

            if (tiempoVolando < tiempoVuelo) {
                //Sigue en el aire
                sprite.setY(yInicial + alturaVolando);
            } else {
                // Termina el salto
                sprite.setY(yInicial);
                estadoSalto = EstadoSalto.EN_PISO;
            }
        }
    }

    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes
    private void moverHorizontal(float delta, TiledMap mapa, OrthographicCamera camara) {
        // Obtiene la primer capa del mapa (en este caso es la única)
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(0);
        // Ejecutar movimiento horizontal
        float nuevaX = sprite.getX();
        // ¿Quiere ir a la Derecha?
        if ( estadoMovimiento==EstadoMovimiento.MOV_DERECHA) {
            // Obtiene el bloque del lado derecho. Asigna null si puede pasar.
            int x = (int) ((sprite.getX() + 32) / 32);   // Convierte coordenadas del mundo en coordenadas del mapa
            int y = (int) (sprite.getY() / 32);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(x, y);
            if (celdaDerecha != null) {
                Object tipo = (String) celdaDerecha.getTile().getProperties().get("tipo");
                if (!"ladrillo".equals(tipo)) {
                    celdaDerecha = null;  // Puede pasar
                }
            }
            if ( celdaDerecha==null) {
                // Ejecutar movimiento horizontal
                nuevaX += VELOCIDAD_X;
                // Prueba que no salga del mundo por la derecha
                if((camara.position.x+Pantalla.ANCHO/2) > (sprite.getX()+sprite.getWidth()))
                    sprite.setX(nuevaX);
            }
        }
        // ¿Quiere ir a la izquierda?
        if ( estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
            int xIzq = (int) ((sprite.getX()) / 32);
            int y = (int) (sprite.getY() / 32);
            // Obtiene el bloque del lado izquierdo. Asigna null si puede pasar.
            TiledMapTileLayer.Cell celdaIzquierda = capa.getCell(xIzq, y);
            if (celdaIzquierda != null) {
                Object tipo = (String) celdaIzquierda.getTile().getProperties().get("tipo");
                if (!"ladrillo".equals(tipo)) {
                    celdaIzquierda = null;  // Puede pasar
                }
            }
            if ( celdaIzquierda==null) {
                // Prueba que no salga del mundo por la izquierda
                nuevaX -= VELOCIDAD_X;
                if (nuevaX >= 0) {
                    sprite.setX(nuevaX);
                }
            }
        }
    }


    // Inicia el salto
    public void saltar() {
        if (estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO) {
            /*
             * Optimización de código -> de 166 milisegundos a 1.6 milisegundos (DOS ÓRDENES DE MAGNITUD!!) - Aldo Reyna G.
             */
            //yMax = (velocidadInicial * velocidadInicial) / (2 * G);
            tiempoVuelo = (2 * velocidadInicial) / G;
            alturaVolando = 0;    // Inicia en el piso
            tiempoVolando = 0;
            yInicial = sprite.getY();
            estadoSalto = EstadoSalto.SUBIENDO;
            //Gdx.app.log("saltar", "ymax=" + yMax + ", tiempoV=" + tiempoVuelo + ", y0=" + yInicial);
        }
    }

    // Accesor de estadoMovimiento y estadoSalto
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }
    public EstadoSalto getEstadoSalto() { return estadoSalto; }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public boolean esAlcanzado(OrthographicCamera camara) {
        return (camara.position.x-Pantalla.ANCHO/2+sprite.getWidth()/2) >= (sprite.getX()+sprite.getWidth());
    }

    // Revisa si toca un item (croqueta o pocion de vida)
    public boolean recolectarItems(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(4); //puedes recuperar una capa del mapa
        int i, j;
        for(i=0; i<4; i++)
            for(j=0; j<3; j++) {
                int x = (int) (sprite.getX() / 32) + i;
                int y = (int) (sprite.getY() / 32) + j;
                TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                if (celda != null) {
                    Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("pez".equals(tipo)) {
                        capa.setCell(x, y, null);    // Borra la croqueta del mapa
                        if(prefs.getBoolean("Sounds",true))
                            efectoCroqueta.play();
                        return true;
                    }
                }
            }
        return false;
    }

    public boolean tomoPocion(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(4); //puedes recuperar una capa del mapa
        int i, j;
        for(i=0; i<4; i++)
            for(j=0; j<3; j++) {
                int x = (int) (sprite.getX() / 32) + i;
                int y = (int) (sprite.getY() / 32) + j;
                TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                if (celda != null) {
                    Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("pocion".equals(tipo)) {
                        capa.setCell(x, y, null);    // Borra la poción del mapa
                        if(prefs.getBoolean("Sounds",true))
                            efectoPocion.play();
                        return true;
                    }
                }
            }
        return false;
    }

    public boolean tocoSlime(Slime slime){
        int x = (int)sprite.getX()-30;
        int y = (int)sprite.getY()+30;
        int width = (int)sprite.getWidth()-30;
        int height = (int)sprite.getHeight()-30;

        Rectangle r = slime.sprite.getBoundingRectangle();
        if(x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y){
            if(prefs.getBoolean("Sounds",true))
                efectoPowerDown.play();
            slime.sprite.setX(0);
            slime.setEstadoMovimiento(Slime.EstadoMovimiento.QUIETO);
            return true;
        }
        return false;
    }

    public boolean recogeGema(TiledMap mapa){
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(4); //puedes recuperar una capa del mapa
        int i, j;
        for(i=0; i<4; i++)
            for(j=0; j<3; j++) {
                int x = (int) (sprite.getX() / 32) + i;
                int y = (int) (sprite.getY() / 32) + j;
                TiledMapTileLayer.Cell celda = capa.getCell(x, y);
                if (celda != null) {
                    Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("gema".equals(tipo)) {
                        capa.setCell(x, y, null);    // Borra la gema del mapa
                        if(prefs.getBoolean("Sounds",true))
                            efectoPocion.play();
                        return true;
                    }
                }
            }
        return false;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
        ASUSTADO
    }

    public enum EstadoSalto {
        SUBIENDO,
        BAJANDO,
        SALTANDO,
        EN_PISO
    }
}
