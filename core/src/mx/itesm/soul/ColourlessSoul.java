package mx.itesm.soul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class ColourlessSoul extends Game {

	private final AssetManager assetManager;
	public static Music clickSound;

	public ColourlessSoul(){
		assetManager=new AssetManager();
	}

	@Override
	public void create() {
		// Lo preparamos para que cargue mapas
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		Preferences prefs = Gdx.app.getPreferences("Settings");
		if(prefs.getBoolean("Touch",true)) {
			prefs.putBoolean("Touch", true);
			prefs.flush();
		}
		clickSound = Gdx.audio.newMusic(Gdx.files.internal("musicSounds/click.mp3"));
		// Pone la pantalla inicial (Splash)
		setScreen(new PantallaInicial(this));
	}
	// Para que las otras pantallas usen el assetManager
	public AssetManager getAssetManager() {
		return assetManager;
	}
	@Override
	public void dispose() {
		super.dispose();
		assetManager.clear();
		clickSound.dispose();
	}
}
