package mx.sagh.soul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by roberto on 13/03/17.
 */

class PantallaCargando extends Pantalla
{
    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private Sprite spriteCargando;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;

    // AssetManager
    private AssetManager manager;

    private colourlessSoul juego;
    private Pantallas siguientePantalla;
    private int avance; // % de carga
    private Texto texto;

    private Texture texturaCargando;

    public PantallaCargando(colourlessSoul juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("cargando/loading.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        texto = new Texto();
    }

    // Carga los recursos de la siguiente pantalla
    private void cargarRecursosSigPantalla() {
        manager = juego.getAssetManager();
        avance = 0;
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case NIVEL_1:
                cargarRecursosNivel1();
                break;

        }
    }



    private void cargarRecursosNivel1() {
        manager.load("FondosPantalla/fondoGris1.jpg", Texture.class);
        manager.load("FondosPantalla/fondoGris2.jpg", Texture.class);
        manager.load("mapaColourless.tmx",TiledMap.class );
        manager.load("PezGiro/pezGiro.png",Texture.class);
        manager.load("PezGiro/pezVanish.png",Texture.class);
        manager.load("SpritesPocion/pocionBNSprites.png", Texture.class);
        manager.load("SpritesPocion/pocionOroSprites.png", Texture.class);
        manager.load("Baba3.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("bite1.mp3", Sound.class);
        manager.load("pauseButton.png", Texture.class);
        manager.load("Boton sonido.png", Texture.class);
        manager.load("changeButtonL.png", Texture.class);
        manager.load("changeButtonR.png", Texture.class);
        manager.load("click.mp3", Sound.class);
        manager.load("controlsButton.png", Texture.class);
        manager.load("controlsChange.png", Texture.class);
        manager.load("controlsTouch.png", Texture.class);
        manager.load("extrasButton.png", Texture.class);
        manager.load("Fondo opciones.jpg", Texture.class);
        manager.load("fondoMadera.png", Texture.class);
        manager.load("fondoMenu.png", Texture.class);
        manager.load("fondoRojo_01.png", Texture.class);
        manager.load("kaiRestingSprite.png", Texture.class);
        manager.load("kaiWalkingSprite.png", Texture.class);
        manager.load("drop.png", Texture.class);
        manager.load("lluvia2.pe", ParticleEffect.class);
        manager.load("loadButton.png", Texture.class);
        manager.load("mainMenuButton.png", Texture.class);
        manager.load("musicOff.png", Texture.class);
        manager.load("musicOn.png", Texture.class);
        manager.load("musicSounds.png", Texture.class);
        manager.load("negro.jpg", Texture.class);
        manager.load("Next.png", Texture.class);
        manager.load("NewGame.png", Texture.class);
        manager.load("nextButton.png", Texture.class);
        manager.load("padBack.png", Texture.class);
        manager.load("padKnob.png", Texture.class);
        manager.load("primerPlano_01.png", Texture.class);
        manager.load("restartButton.png", Texture.class);
        manager.load("resumeButton.png", Texture.class);
        manager.load("settingsButton.png", Texture.class);
        manager.load("settingsButton1.png", Texture.class);
        manager.load("soundsOff.png", Texture.class);
        manager.load("soundsOn.png", Texture.class);
        manager.load("startButton.png", Texture.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("wind.mp3", Sound.class);
        manager.load("slimeAnimacion.png", Texture.class);

    }

    private void cargarRecursosMenu() {
        manager.load("FondosPantalla/fondoGris1.jpg", Texture.class);
        manager.load("FondosPantalla/fondoGris2.jpg", Texture.class);
        manager.load("mapaColourless.tmx",TiledMap.class );
        manager.load("PezGiro/pezGiro.png",Texture.class);
        manager.load("PezGiro/pezVanish.png",Texture.class);
        manager.load("SpritesPocion/pocionBNSprites.png", Texture.class);
        manager.load("SpritesPocion/pocionOroSprites.png", Texture.class);
        manager.load("Baba3.png", Texture.class);
        manager.load("backButton.png", Texture.class);
        manager.load("bite1.mp3", Sound.class);
        manager.load("pauseButton.png", Texture.class);
        manager.load("Boton sonido.png", Texture.class);
        manager.load("changeButtonL.png", Texture.class);
        manager.load("changeButtonR.png", Texture.class);
        manager.load("click.mp3", Sound.class);
        manager.load("controlsButton.png", Texture.class);
        manager.load("controlsChange.png", Texture.class);
        manager.load("controlsTouch.png", Texture.class);
        manager.load("extrasButton.png", Texture.class);
        manager.load("Fondo opciones.jpg", Texture.class);
        manager.load("fondoMadera.png", Texture.class);
        manager.load("fondoMenu.png", Texture.class);
        manager.load("fondoRojo_01.png", Texture.class);
        manager.load("kaiRestingSprite.png", Texture.class);
        manager.load("kaiWalkingSprite.png", Texture.class);
        manager.load("drop.png", Texture.class);
        manager.load("lluvia2.pe", ParticleEffect.class);
        manager.load("loadButton.png", Texture.class);
        manager.load("mainMenuButton.png", Texture.class);
        manager.load("musicOff.png", Texture.class);
        manager.load("musicOn.png", Texture.class);
        manager.load("musicSounds.png", Texture.class);
        manager.load("negro.jpg", Texture.class);
        manager.load("Next.png", Texture.class);
        manager.load("NewGame.png", Texture.class);
        manager.load("nextButton.png", Texture.class);
        manager.load("padBack.png", Texture.class);
        manager.load("padKnob.png", Texture.class);
        manager.load("primerPlano_01.png", Texture.class);
        manager.load("restartButton.png", Texture.class);
        manager.load("resumeButton.png", Texture.class);
        manager.load("settingsButton.png", Texture.class);
        manager.load("settingsButton1.png", Texture.class);
        manager.load("soundsOff.png", Texture.class);
        manager.load("soundsOn.png", Texture.class);
        manager.load("startButton.png", Texture.class);
        manager.load("thunder.mp3", Sound.class);
        manager.load("wind.mp3", Sound.class);


    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.5f, 0.5f, 0.5f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch,avance+" %",ANCHO/2,ALTO/2);
        batch.end();
        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion<=0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(20);
        }
        // Actualizar carga
        actualizarCargaRecursos();
    }

    private void actualizarCargaRecursos() {
        if (manager.update()) { // Terminó?
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new PantallaMenu(juego));   // 100% de carga
                    break;
                case NIVEL_1:
                    juego.setScreen(new PantallaPrincipal(juego));   // 100% de carga
                    break;
            }
        }
        avance = (int)(manager.getProgress()*100);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
    }
}
