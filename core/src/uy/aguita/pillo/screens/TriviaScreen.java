package uy.aguita.pillo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Json;
import uy.aguita.pillo.game.Assets;
import uy.aguita.pillo.game.objects.Column;
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
    private Image congrats, question, answerA, answerB, answerC;
    private FeteCharacter arm,mouth;
    private Sound[] goodSounds;
    private Column column1,column2;
    private TextureRegion [] q1,q2,q3;
    private float fadeVelocity;


    public TriviaScreen(DirectedGame game, int triviaTypeNr) {
        super(game);
        this.triviaTypeNr = triviaTypeNr; //1,2 or 3
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
        fadeVelocity =1.0f;

        goodSounds = new Sound[]{Assets.instance.sounds.perfect,Assets.instance.sounds.vGood,Assets.instance.sounds.excelent};
        q1 = new TextureRegion[]{Assets.instance.trivia.q1_1,Assets.instance.trivia.q1_2,Assets.instance.trivia.q1_3};
        q2 = new TextureRegion[]{Assets.instance.trivia.q2_1,Assets.instance.trivia.q2_2,Assets.instance.trivia.q2_3};
        q3 = new TextureRegion[]{Assets.instance.trivia.q3_1,Assets.instance.trivia.q3_2};
    }


    @Override
    public void show() {
        initStageAndCamera();
        addBackgroundImage(Assets.instance.trivia.background);
        addBackButton();
        addColumns();

        addMovingArm();
        addPillo();
        addMouth();
        addABC();
        addQuestionAndAnswersBacks();
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


    public void addQuestionAndAnswersBacks(){

        Image backgroundQuestion = new Image(Assets.instance.trivia.questionBack);
        backgroundQuestion.setSize(questionWidth,questionHeight);
        backgroundQuestion.setPosition(answersX-3, questionY);
        stage.addActor(backgroundQuestion);

        // ANSWER A
        Image backgroundO1 = new Image(Assets.instance.trivia.optionBack);
        backgroundO1.setSize(answerWidth,answerHeight);
        backgroundO1.setPosition(answersX, getPosition(0).y);
        stage.addActor(backgroundO1);

        // ANSWER B
        Image backgroundO2 = new Image(Assets.instance.trivia.optionBack);
        backgroundO2.setSize(answerWidth,answerHeight);
        backgroundO2.setPosition(answersX, getPosition(1).y);
        stage.addActor(backgroundO2);


        // ANSWER C
        Image backgroundO3 = new Image(Assets.instance.trivia.optionBack);
        backgroundO3.setSize(493,84);
        backgroundO3.setPosition(answersX, getPosition(2).y);
        stage.addActor(backgroundO3);

    }

    private void addABC(){
        // BTN A
        btnA = new ImageButton(Assets.instance.buttons.answerBtn1Style);
        btnA.setPosition(buttonsX,getPosition(0).y);
        stage.addActor(btnA);
        // BTN B
        btnB = new ImageButton(Assets.instance.buttons.answerBtn2Style);
        btnB.setPosition(buttonsX,getPosition(1).y);
        stage.addActor(btnB);
        // BTN C
        btnC = new ImageButton(Assets.instance.buttons.answerBtn3Style);
        btnC.setPosition(buttonsX,getPosition(2).y);
        stage.addActor(btnC);
    }

    private void addQuestionAndAnswers(){
        loadQuestion();
        loadAnswerA();
        loadAnswerB();
        loadAnswerC();
        loadCorrectAndWrong();

        if(shouldRemoveFirst)
            congrats.toFront();


    }

    private void loadCorrectAndWrong(){

        int nr = 3*(triviaTypeNr-1) + triviaNr; //1 to 8
        Gdx.app.log(TAG," load index "+nr);
        if(nr == 3 || nr == 4 || nr == 7)
            loadCorrectA();
        else
            loadCorrectB();

    }

    private void loadCorrectA(){
        addCorrectAction(btnA);
        addWrongAction(btnB);
        addWrongAction(btnC);
    }

    private void loadCorrectB(){
        addCorrectAction(btnB);
        addWrongAction(btnA);
        addWrongAction(btnC);
    }



    private void loadQuestion(){
        if(triviaTypeNr==1)
            question = new Image(q1[triviaNr-1]);
        else if(triviaTypeNr==2)
            question = new Image(q2[triviaNr-1]);
        else
            question = new Image(q3[triviaNr-1]);

        question.setPosition(answersX-3,questionY);
      //  question.addAction(sequence(alpha(0),alpha(1,fadeVelocity)));
        stage.addActor(question);
    }


    private void loadAnswerA(){
        if(triviaTypeNr==1)
            loadAType1();
        else if(triviaTypeNr==2)
            loadAType2();
        else
            loadAType3();

        answerA.setPosition(answersX,getPosition(0).y);
     //   answerA.addAction(sequence(alpha(0),alpha(1,fadeVelocity)));
        stage.addActor(answerA);
    }

    private void loadAType1(){
        if(triviaNr==1){
            answerA = new Image(Assets.instance.trivia.r1_1);
        }else if(triviaNr==2)
            answerA = new Image(Assets.instance.trivia.r1_4);
        else
            answerA = new Image(Assets.instance.trivia.r1_7);
    }

    private void loadAType2(){
        if(triviaNr==1){
            answerA = new Image(Assets.instance.trivia.r2_1);
        }else if(triviaNr==2)
            answerA = new Image(Assets.instance.trivia.r2_4);
        else
            answerA = new Image(Assets.instance.trivia.r2_7);
    }

    private void loadAType3(){
        if(triviaNr==1){
            answerA = new Image(Assets.instance.trivia.r3_1);
        }else if(triviaNr==2)
            answerA = new Image(Assets.instance.trivia.r3_4);
    }

    private void loadAnswerB(){
        if(triviaTypeNr==1)
            loadBType1();
        else if(triviaTypeNr==2)
            loadBType2();
        else
            loadBType3();

        answerB.setPosition(answersX,getPosition(1).y);
       // answerB.addAction(sequence(alpha(0),alpha(1,fadeVelocity)));
        stage.addActor(answerB);
    }

    private void loadBType1(){
        if(triviaNr==1){
            answerB= new Image(Assets.instance.trivia.r1_2);
        }else if(triviaNr==2)
            answerB = new Image(Assets.instance.trivia.r1_5);
        else
            answerB = new Image(Assets.instance.trivia.r1_8);
    }

    private void loadBType2(){
        if(triviaNr==1){
            answerB = new Image(Assets.instance.trivia.r2_2);
        }else if(triviaNr==2)
            answerB = new Image(Assets.instance.trivia.r2_5);
        else
            answerB = new Image(Assets.instance.trivia.r2_8);
    }

    private void loadBType3(){
        if(triviaNr==1){
            answerB = new Image(Assets.instance.trivia.r3_2);
        }else if(triviaNr==2)
            answerB = new Image(Assets.instance.trivia.r3_5);
    }

    private void loadAnswerC(){
        if(triviaTypeNr==1)
            loadCType1();
        else if(triviaTypeNr==2)
            loadCType2();
        else
            loadCType3();

        answerC.setPosition(answersX,getPosition(2).y);
      //  answerC.addAction(sequence(alpha(0),alpha(1,fadeVelocity)));
        stage.addActor(answerC);
    }

    private void loadCType1(){
        if(triviaNr==1){
            answerC= new Image(Assets.instance.trivia.r1_3);
        }else if(triviaNr==2)
            answerC = new Image(Assets.instance.trivia.r1_6);
        else
            answerC = new Image(Assets.instance.trivia.r1_9);
    }

    private void loadCType2(){
        if(triviaNr==1){
            answerC = new Image(Assets.instance.trivia.r2_3);
        }else if(triviaNr==2)
            answerC = new Image(Assets.instance.trivia.r2_6);
        else
            answerC = new Image(Assets.instance.trivia.r2_9);
    }

    private void loadCType3(){
        if(triviaNr==1){
            answerC = new Image(Assets.instance.trivia.r3_3);
        }else if(triviaNr==2)
            answerC = new Image(Assets.instance.trivia.r3_6);
    }







    private void addPillo(){
        final Image pillo = new Image(Assets.instance.trivia.pillo);
        pillo.setSize(168,269);
        pillo.setPosition(343,-173);
//        pillo.setTouchable(Touchable.enabled);
//        // to move around
//        pillo.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                pillo.setPosition(pillo.getX()+deltaX,pillo.getY()+deltaY);
//                Gdx.app.log(TAG," pillo x "+pillo.getX() +" y "+pillo.getY());
//            }
//
//
//        });

        stage.addActor(pillo);
    }

    private void addMovingArm(){
        arm = new FeteCharacter(Assets.instance.trivia.arm);
        arm.setPosition(244,-162);
        arm.setSize(202,273);

//        arm.setTouchable(Touchable.enabled);
//        // to move around
//        arm.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                arm.setPosition(arm.getX()+deltaX,arm.getY()+deltaY);
//                Gdx.app.log(TAG," arm x "+arm.getX() +" y "+arm.getY());
//            }
//
//
//        });
        stage.addActor(arm);
    }


    private void addMouth(){
        mouth = new FeteCharacter(Assets.instance.trivia.mouth);
        mouth.setPosition(408,-32);
        mouth.setSize(44,47);

//        mouth.setTouchable(Touchable.enabled);
//        // to move around
//        mouth.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                mouth.setPosition(mouth.getX()+deltaX,mouth.getY()+deltaY);
//                Gdx.app.log(TAG," arm x "+mouth.getX() +" y "+mouth.getY());
//            }
//
//
//        });
        stage.addActor(mouth);
    }

    private void addColumns(){
        column1 = new Column(1);
        column1.setPosition(370,-125);
//        column1.setTouchable(Touchable.enabled);
//        // to move around
//        column1.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                column1.setPosition(column1.getX()+deltaX,column1.getY()+deltaY);
//                Gdx.app.log(TAG," column1 x "+column1.getX() +" y "+column1.getY());
//            }
//
//
//        });
        stage.addActor(column1);

        column2 = new Column(2);
        column2.setPosition(-481,-171);
//        column2.setTouchable(Touchable.enabled);
//        // to move around
//        column2.addListener(new ActorGestureListener() {
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                //TODO we should check if the piece is in stage limits or the controller should do this?
//                column2.setPosition(column2.getX()+deltaX,column2.getY()+deltaY);
//                Gdx.app.log(TAG," column2 x "+column2.getX() +" y "+column2.getY());
//            }
//
//
//        });
        stage.addActor(column2);

    }

    private void addCongrats(){
        congrats = new Image(Assets.instance.trivia.congratulation); // TODO change!!
        congrats.setSize(620,175);
       // congrats.setSize(421,124);
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
                AudioManager.instance.play(goodSounds[MathUtils.random(2)]);

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
                        if(!triviaOver() ) {
                            removeQuestionAndAnswers();
                            addABC();
                            addQuestionAndAnswers();
                        }
                        else{
                            goToMenu();
                        }
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

    private boolean triviaOver(){
        return ((triviaNr==3 && triviaTypeNr==3) || triviaNr > 3);
    }

    private void removeQuestionAndAnswers(){
        question.remove();
        answerA.remove();
        answerB.remove();
        answerC.remove();
//        btnA.clearListeners();
//        btnB.clearListeners();
//        btnC.clearListeners();
        btnA.remove();
        btnB.remove();
        btnC.remove();



    }

}
