package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.screens.transitions.ScreenTransition;
import uy.aguita.pillo.screens.transitions.ScreenTransitionFade;
import uy.aguita.pillo.util.Constants;

/**
 * Created by ewe on 1/15/17.
 */
public abstract class AbstractGameScreen implements Screen {
    public static final String TAG = AbstractGameScreen.class.getName();
    protected ScreenTransition transition;
    protected DirectedGame game;
    protected Stage stage;
    protected OrthographicCamera camera;
    protected ImageButton backButton;

    public AbstractGameScreen (DirectedGame game) {
        this.game = game;
        transition = ScreenTransitionFade.init(0.75f);
    }


    //public abstract void resize (int width, int height);
    //public abstract void show ();
    public abstract void hide ();
    public abstract void pause ();

    public void resume () {

        //Assets.instance.init(new AssetManager());
    }
    public void dispose () {
        Assets.instance.dispose();
    }
    public InputProcessor getInputProcessor (){return stage;}


    public void resize(int width, int height) {
        Gdx.app.log(TAG,"resize!");
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        stage.getViewport().update(width, height, true);
        camera.position.set(0,0,0);
    }

    public void show() {
        Gdx.app.log(TAG, " im here!");
        initStageAndCamera();
    }


    public void render(float deltaTime) {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();

    }

    protected void initStageAndCamera(){
        Gdx.app.log(TAG, " initStageAndCamera ");
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        camera.position.set(0,0,0);
        camera.update();
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,camera));
        stage.setDebugAll(true);
    }

    protected void addBackButton(){
        Gdx.app.log(TAG,"add back!");
        backButton = new ImageButton(Assets.instance.buttons.back);
        backButton.setPosition(-Constants.VIEWPORT_WIDTH/2 +60, -Constants.VIEWPORT_HEIGHT/2 +60);
        //backButton.setPosition(-0, 0);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onBackClicked();
            }
        });
        stage.addActor(backButton);
    }

    protected void onBackClicked(){
        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        game.setScreen(new MenuScreen(game), transition);
    }


}
