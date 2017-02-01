package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.game.BacteriaController;
import uy.aguita.pillo.util.Constants;

/**
 * Created by ewe on 1/15/17.
 */
public class ToothScreen extends AbstractGameScreen {
    public static final String TAG = ToothScreen.class.getName();
    private BacteriaController bacteriaController;

    private SpriteBatch batch;


    public ToothScreen(DirectedGame game) {
        super(game);
    }

    @Override
    public void show(){
        Gdx.app.log(TAG, "welcome to tooth screen");
        super.show();
        addBackgroundImage();
        bacteriaController = new BacteriaController(stage);
        addBottomMenu();
        batch = new SpriteBatch();

    }

    @Override
    public void render(float deltaTime) {
        if (bacteriaController.hasCollisions()){
            bacteriaController.stopBacterias();
        }
        if(bacteriaController.tooth.isHurt() &&
                bacteriaController.tooth.animationBad.isAnimationFinished(bacteriaController.tooth.getStateTime())){
            resetBacteriasWorld();
            //TODO should we reset the score or we add points form 3 lifes?
        }


        super.render(deltaTime);

        renderScoreAndLifes();

    }


    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    private void  addBackgroundImage(){


        Image back = new Image(Assets.instance.toothGame.background);
        back.setSize(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        back.setPosition(-Constants.VIEWPORT_WIDTH/2,-Constants.VIEWPORT_HEIGHT/2);

        stage.addActor(back);
    }

    private void addBottomMenu(){
        Image panel = new Image(Assets.instance.toothGame.scoreMenu);
        panel.setPosition(-Assets.instance.toothGame.scoreMenu.getRegionWidth()/2,-Constants.VIEWPORT_HEIGHT/2 +25 );
        stage.addActor(panel);
        addBackButton();
        backButton.setPosition(168, -246);
//
//        backButton.setTouchable(Touchable.enabled);
//        // to move around
//        backButton.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                backButton.setPosition(backButton.getX()+deltaX,backButton.getY()+deltaY);
//                Gdx.app.log(TAG," x "+backButton.getX()+" y "+backButton.getY());
//            }
//
//
//        });
    }

    private void renderScoreAndLifes(){
        //Gdx.app.log(TAG," render now score " + bacteriaController.getActualScore());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.instance.toothGame.scoreTop, -Constants.VIEWPORT_WIDTH/2, Constants.VIEWPORT_HEIGHT/2-91); //91 hardcoded, counter height
        Assets.instance.fonts.defaultBig.draw(batch, "" + bacteriaController.getActualScore(), -Constants.VIEWPORT_WIDTH/2 + 120, Constants.VIEWPORT_HEIGHT/2 -34);

        //render lifes
        int lost = bacteriaController.getLifeLost();
        for(int i = 0;i<lost;i++){
            batch.draw(Assets.instance.toothGame.cross,-264+i*38,-248);

        }

        batch.end();

    }

    private void resetBacteriasWorld(){
        Gdx.app.log(TAG,"reset tooth and bacterias");
        // we go back to normal life
        bacteriaController.resetTooth();
        bacteriaController.resetBacterias();
    }


}
