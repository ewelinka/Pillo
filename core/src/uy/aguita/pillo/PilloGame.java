package uy.aguita.pillo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.screens.DirectedGame;
import uy.aguita.pillo.screens.Match;
import uy.aguita.pillo.screens.MenuScreen;
import uy.aguita.pillo.screens.transitions.ScreenTransition;
import uy.aguita.pillo.screens.transitions.ScreenTransitionFade;
import uy.aguita.pillo.util.AudioManager;

public class PilloGame extends DirectedGame {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		AudioManager.instance.play(Assets.instance.music.song01);
		// Start game at menu screen
		ScreenTransition transition = ScreenTransitionFade.init(0.25f);
		setScreen(new MenuScreen(this), transition);
		//setScreen(new TriviaScreen(this,0), transition);
		//setScreen(new Match(this), transition);

	}
}
