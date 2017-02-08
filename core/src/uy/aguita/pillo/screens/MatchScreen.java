package uy.aguita.pillo.screens;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.game.objects.MatchButton;
import uy.aguita.pillo.util.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;

/**
 * Created by ewe on 1/15/17.
 */
public class MatchScreen extends AbstractGameScreen {
    public static final String TAG = MatchScreen.class.getName();
    private Image text1, text2, text3,question;
    private MatchButton img1,img2,img3;
    private Image congrats;
    private int topY, distanceY, leftX, distanceX, optionW, optionH, btnW,btnH,adjustButtonY;
    private int correctAnswers, adjustButtosX;
    private boolean weWon;

    public MatchScreen(DirectedGame game) {
        super(game);
        topY = 20;
        distanceY = 130;
        leftX = -382;
        distanceX = 590;
        optionW = 500;
        optionH = 90;
        btnW = 204;
        btnH =121;
        correctAnswers = 0;
        weWon = false;
        adjustButtonY = 0;
        adjustButtosX = -30;
    }



    @Override
    public void show() {
        initStageAndCamera();
        addBackgroundImage(Assets.instance.match.back);
        addBackButton();
        addMatches();
        addCongrats();

    }

    @Override
    public void render(float deltaTime) {

        if(weWon){
            animateCongrats();
            weWon=false;
        }
        super.render(deltaTime);

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    private void addMatches(){
        question = new Image(Assets.instance.match.question);
        question.setSize(816, 107);
        question.setPosition(-question.getWidth()/2,134);


        text1 = new Image(Assets.instance.match.option1);
        text1.setPosition(leftX,topY);
        text1.setSize(optionW, optionH);

        img1 = new MatchButton(Assets.instance.match.img1);
        img1.setPosition(leftX+distanceX,topY+adjustButtonY);
        img1.setSize(btnW, btnH);
        img1.setScale(0.75f);
        img1.setHome(img1.getX(),img1.getY());
        addCorrectAction(img1,text1.getX(),text1.getY(),adjustButtosX);

        text2 = new Image(Assets.instance.match.option2);
        text2.setPosition(leftX,topY-distanceY);
        text2.setSize(optionW, optionH);

        img2 = new MatchButton(Assets.instance.match.img2);
        img2.setPosition(leftX+distanceX,topY-distanceY+adjustButtonY);
        img2.setSize(btnW, btnH);
        img2.setScale(0.75f);
        img2.setHome(img2.getX(),img2.getY());
        addCorrectAction(img2,text2.getX(),text2.getY(),adjustButtosX);

        text3 = new Image(Assets.instance.match.option3);
        text3.setPosition(leftX,topY-distanceY*2);
        text3.setSize(optionW, optionH);

        img3 = new MatchButton(Assets.instance.match.img3);
        img3.setPosition(leftX+distanceX,topY-distanceY*2+adjustButtonY);
        img3.setSize(btnW, btnH);
        img3.setScale(0.75f);
        img3.setHome(img3.getX(),img3.getY());
        addCorrectAction(img3,text3.getX(),text3.getY(),adjustButtosX);

        stage.addActor(question);
        stage.addActor(text1);
        stage.addActor(text2);
        stage.addActor(text3);
        stage.addActor(img1);
        stage.addActor(img2);
        stage.addActor(img3);

    }

    private void addCongrats(){
        congrats = new Image(Assets.instance.toothGame.bacteriaTexture); // TODO change!!
        congrats.setSize(620,175);
        congrats.setOrigin(congrats.getWidth()/2, congrats.getHeight()/2);
        congrats.setPosition(-congrats.getWidth()/2,-congrats.getHeight()/2);
        congrats.setVisible(false);
        congrats.setScale(0);
        stage.addActor(congrats);

    }



    private void addCorrectAction(final MatchButton btn, final float answerX, final float answerY, final int adjustX){

        btn.setTouchable(Touchable.enabled);
        // to move around
        btn.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                btn.setPosition(btn.getX()+deltaX,btn.getY()+deltaY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                //TODO we should check if the piece is in stage limits or the controller should do this?

                Rectangle r1 = new Rectangle(btn.getX(),btn.getY(), optionH, optionH);
                Rectangle r2 = new Rectangle(answerX,answerY, optionW, optionH);
                if(r1.overlaps(r2)){
                    btn.setTouchable(Touchable.disabled);
                    AudioManager.instance.play(Assets.instance.sounds.good);
                    btn.addAction(Actions.moveTo(leftX+ optionW+adjustX,answerY,1.0f));
                    updateScore();
                }else{
                    AudioManager.instance.play(Assets.instance.sounds.bad);
                    btn.goHome();
                }

            }


        });

    }

    private void updateScore(){
        correctAnswers+=1;
        if(correctAnswers>=3)
            weWon = true;
    }

    private void animateCongrats(){
        congrats.setVisible(true);
        congrats.addAction(sequence(
                delay(1.2f),
                alpha(1),
                scaleTo(1,1,1.0f, Interpolation.circle),
                delay(0.6f),
                alpha(0,0.8f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        congrats.setVisible(false);
                        game.setScreen(new MenuScreen(game),transition);
                    }
                })


        ));


    }

}
