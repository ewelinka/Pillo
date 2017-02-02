package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Json;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.game.objects.FeteCharacter;
import uy.aguita.pillo.util.AudioManager;
import uy.aguita.pillo.util.Constants;
import uy.aguita.pillo.util.TriviaQuestion;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by ewe on 1/15/17.
 */
public class TriviaScreen extends AbstractGameScreen {
    public static final String TAG = TriviaScreen.class.getName();
    private int triviaTypeNr; //0, 1 or 2
    private int triviaNr; // 1,2,3,..

    private ImageButton btnA,btnB,btnC;
    private int buttonsX, answersX, questionY, answerSpaceY, answerWidth, answerHeight,questionWidth,questionHeight;
    private boolean shouldRemoveFirst;
    private Image congrats, question1, question2,question3, answerA, answerB, answerC;
    private FeteCharacter arm,mouth;


    public TriviaScreen(DirectedGame game, int triviaTypeNr) {
        super(game);
        this.triviaTypeNr = triviaTypeNr;
        triviaNr = 1; // start with first question
        buttonsX= -Constants.VIEWPORT_WIDTH/2 + 191;
        answersX = buttonsX +103;
        questionY = 60;

        answerSpaceY = 15;

        answerWidth = 493;
        answerHeight = 90;
        questionWidth = 500;
        questionHeight = 90;

        shouldRemoveFirst = false;
    }


    @Override
    public void show() {
        initStageAndCamera();
        addBackgroundImage();
        addBackButton();

        addMovingArm();
        addPillo();
        addMouth();
        addQuestionAndAnswers();
        addCongrats();

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    private Vector2 getPosition(int randomPos){
        switch (randomPos){
            case 0:
                return new Vector2(answersX,questionY- answerSpaceY - questionHeight );
            case 1:
                return new Vector2(answersX,questionY- answerSpaceY *2 - questionHeight*2 );
            case 2:
                return new Vector2(answersX,questionY- answerSpaceY *3 - questionHeight*3);
            default:
                return new Vector2(0,0);
        }
    }


    public void addQuestionAndAnswers(){
        Image backgroundQuestion = new Image(Assets.instance.trivia.questionBack);
        backgroundQuestion.setSize(questionWidth,questionHeight);
        backgroundQuestion.setPosition(answersX-3, questionY);
        stage.addActor(backgroundQuestion);

        if(shouldRemoveFirst){
            removeAllQuestions();
        }

        question1 = new Image(Assets.instance.trivia.q1_1); // this will change after good answer
        question1.setPosition(answersX-3,questionY);
        stage.addActor(question1);

        // ANSWER A
        Image backgroundO1 = new Image(Assets.instance.trivia.optionBack);
        backgroundO1.setSize(answerWidth,answerHeight);
        backgroundO1.setPosition(answersX, getPosition(0).y);
        stage.addActor(backgroundO1);
        // BTN
        btnA = new ImageButton(Assets.instance.buttons.answerBtn1Style);
        btnA.setPosition(buttonsX,getPosition(0).y);
        stage.addActor(btnA);
        addCorrectAction(btnA);
        // TXT
        answerA = new Image(Assets.instance.trivia.r1_1); // this will change after good answer
        answerA.setPosition(answersX,getPosition(0).y);
        stage.addActor(answerA);


        // ANSWER B
        Image backgroundO2 = new Image(Assets.instance.trivia.optionBack);
        backgroundO2.setSize(answerWidth,answerHeight);
        backgroundO2.setPosition(answersX, getPosition(1).y);
        stage.addActor(backgroundO2);
        // BTN
        btnB = new ImageButton(Assets.instance.buttons.answerBtn2Style);
        btnB.setPosition(buttonsX,getPosition(1).y);
        stage.addActor(btnB);
        // TXT
        answerB = new Image(Assets.instance.trivia.r1_2); // this will change after good answer
        answerB.setPosition(answersX,getPosition(1).y);
        stage.addActor(answerB);


        // ANSWER C
        Image backgroundO3 = new Image(Assets.instance.trivia.optionBack);
        backgroundO3.setSize(493,84);
        backgroundO3.setPosition(answersX, getPosition(2).y);
        stage.addActor(backgroundO3);
        // BTN
        btnC = new ImageButton(Assets.instance.buttons.answerBtn3Style);
        btnC.setPosition(buttonsX,getPosition(2).y);
        stage.addActor(btnC);
        // TXT
        answerC = new Image(Assets.instance.trivia.r1_3); // this will change after good answer
        answerC.setPosition(answersX,getPosition(2).y);
        stage.addActor(answerC);




        if(shouldRemoveFirst)
            congrats.toFront();

    }


    private void addBackgroundImage(){
        Image back = new Image(Assets.instance.trivia.background);
        back.setSize(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        back.setPosition(-Constants.VIEWPORT_WIDTH/2,-Constants.VIEWPORT_HEIGHT/2);

        stage.addActor(back);
    }

    private void addPillo(){
        final Image pillo = new Image(Assets.instance.trivia.pillo);
        pillo.setSize(168,269);
        pillo.setPosition(343,-173);
        pillo.setTouchable(Touchable.enabled);
        // to move around
        pillo.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                pillo.setPosition(pillo.getX()+deltaX,pillo.getY()+deltaY);
                Gdx.app.log(TAG," pillo x "+pillo.getX() +" y "+pillo.getY());
            }


        });

        stage.addActor(pillo);
    }

    private void addMovingArm(){
        arm = new FeteCharacter(Assets.instance.trivia.arm);
        arm.setPosition(244,-162);
        arm.setSize(202,273);

        arm.setTouchable(Touchable.enabled);
        // to move around
        arm.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                arm.setPosition(arm.getX()+deltaX,arm.getY()+deltaY);
                Gdx.app.log(TAG," arm x "+arm.getX() +" y "+arm.getY());
            }


        });
        stage.addActor(arm);
    }


    private void addMouth(){
        mouth = new FeteCharacter(Assets.instance.trivia.mouth);
        mouth.setPosition(408,-32);
        mouth.setSize(44,47);

        mouth.setTouchable(Touchable.enabled);
        // to move around
        mouth.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?
                mouth.setPosition(mouth.getX()+deltaX,mouth.getY()+deltaY);
                Gdx.app.log(TAG," arm x "+mouth.getX() +" y "+mouth.getY());
            }


        });
        stage.addActor(mouth);
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

    private void addCorrectAction(Button btn){

        btn.setTouchable(Touchable.enabled);
        // to move around
        btn.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button){
            //TODO we should check if the piece is in stage limits or the controller should do this?
                AudioManager.instance.play(Assets.instance.sounds.good);
                triviaNr+=1;
                shouldRemoveFirst = true;
                arm.doFete();
                mouth.doFete();
                animateCongrats();

            }


        });

    }


    private void animateCongrats(){


        congrats.setVisible(true);
        congrats.addAction(sequence(
                alpha(1),
                scaleTo(1,1,1.0f, Interpolation.circle),
                delay(0.6f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        addQuestionAndAnswers();
                    }
                }),
                delay(0.6f),
                alpha(0,0.8f),
                scaleTo(0,0),
                run(new Runnable() {
                    @Override
                    public void run() {
                        congrats.setVisible(false);
                    }
                })


        ));


    }
    private void addWrongAction(Button btn){

        btn.setTouchable(Touchable.enabled);
        // to move around
        btn.addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button){
                //TODO we should check if the piece is in stage limits or the controller should do this?
                AudioManager.instance.play(Assets.instance.sounds.bad);
            }


        });

    }

    private void removeAllQuestions(){



    }

}
