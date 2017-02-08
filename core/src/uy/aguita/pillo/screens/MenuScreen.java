package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.util.Constants;

/**
 * Created by ewe on 1/15/17.
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    //private Stage stage;
    private Button trivia1;
    private Button trivia2;
    private Button trivia3;
    private Button tooth;
    private Button match;


    public MenuScreen(DirectedGame game) {
        super(game);

    }

    @Override
    public void show() {
        initStageAndCamera();
        rebuildStage();
        addBackButton();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    private void rebuildStage(){
        Table buttons = buildGameButtonsLayers();
        // TODO here ewe can add deco layers
        //stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stack.setPosition(-Constants.VIEWPORT_WIDTH/2,-Constants.VIEWPORT_HEIGHT/2);
        stack.add(buttons);
    }

    private Table buildGameButtonsLayers () {
        Table layer = new Table();
        layer.center().center();
        // + Play Button
        trivia1 = new ImageButton(Assets.instance.buttons.trivia1);
        layer.add(trivia1).padRight(20);
        trivia1.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onTrivia1Clicked();
            }
        });


        trivia2 = new ImageButton(Assets.instance.buttons.trivia2);
        layer.add(trivia2).padRight(20);
        trivia2.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onTrivia2Clicked();
            }
        });

        trivia3 = new ImageButton(Assets.instance.buttons.trivia3);
        layer.add(trivia3).padRight(20);
        trivia3.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onTrivia3Clicked();
            }
        });

        tooth = new ImageButton(Assets.instance.buttons.tooth);
        layer.add(tooth).padRight(20);
        tooth.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onToothClicked();
            }
        });

        match = new ImageButton(Assets.instance.buttons.match);
        layer.add(match);
        match.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onMatchClicked();
            }
        });

        if (true) layer.debug();
        return layer;
    }


    private void onTrivia1Clicked () {
        game.setScreen(new TriviaScreen(game,1), transition);
    }
    private void onTrivia2Clicked () {
        game.setScreen(new TriviaScreen(game,2), transition);
    }
    private void onTrivia3Clicked () {
        game.setScreen(new TriviaScreen(game,3), transition);
    }
    private void onToothClicked () {
        Gdx.app.log(TAG,"onToothClicked");
        game.setScreen(new ToothScreen(game), transition);
    }
    private void onMatchClicked () {
        game.setScreen(new MatchScreen(game), transition);
    }

    @Override
    protected void onExitYes(){
        dispose();
        Gdx.app.exit();
    }
}
