package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.screens.transitions.ScreenTransition;
import uy.aguita.pillo.screens.transitions.ScreenTransitionFade;
import uy.aguita.pillo.util.AudioManager;
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
    protected Group exitGroup;
    protected ImageButton yes,no;

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
        Gdx.app.log(TAG,"DISPOSEEEEEEE");
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
                goToMenu();
            }
        });
        stage.addActor(backButton);
    }

    protected void goToMenu(){
        showExitConfirmation();
        //
    }

    protected void showExitConfirmation(){
        exitGroup = new Group();
        stage.addActor(exitGroup);

        // background
        Image back = new Image(Assets.instance.general.exitGame);
        exitGroup.addActor(back);
        // go to menu
        yes = new ImageButton(Assets.instance.buttons.exitYesStyle);
        yes.setTouchable(Touchable.enabled);
        //-- to move around
        yes.addListener(new ActorGestureListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                onExitYes();
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                yes.setPosition(yes.getX()+deltaX,yes.getY()+deltaY);
                Gdx.app.log(TAG," arm x "+yes.getX() +" y "+yes.getY());
            }

        });
        exitGroup.addActor(yes);

        addExitNo();


        // positions in group
        back.setPosition(0,0);
        yes.setPosition(75,13);
        no.setPosition(279,13);

        // position of the group
        exitGroup.setPosition(0 - back.getWidth() / 2,
                0 - back.getHeight() / 2);
        exitGroup.setOrigin(back.getWidth()/2,back.getHeight()/2);
        exitGroup.setScale(0);
        exitGroup.addAction(Actions.scaleTo(1f,1f,0.5f));
    }

    protected void addExitNo(){
        //--- go back to game
        no = new ImageButton(Assets.instance.buttons.exitNoStyle);
        no.setTouchable(Touchable.enabled);
        //-- to move around
        no.addListener(new ActorGestureListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                exitGroup.remove();
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                no.setPosition(no.getX()+deltaX,no.getY()+deltaY);
                Gdx.app.log(TAG," arm x "+no.getX() +" y "+no.getY());
            }

        });
        exitGroup.addActor(no);
    }

    protected void onExitYes(){
        game.setScreen(new MenuScreen(game), transition);
    }

    protected void addBackgroundImage(TextureAtlas.AtlasRegion tex){
        Image back = new Image(tex);
        back.setSize(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        back.setPosition(-Constants.VIEWPORT_WIDTH/2,-Constants.VIEWPORT_HEIGHT/2);

        stage.addActor(back);
    }


}
