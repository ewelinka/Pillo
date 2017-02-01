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
import uy.aguita.pillo.util.TriviaQuestion;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by ewe on 1/15/17.
 */
public class TriviaScreen extends AbstractGameScreen {
    public static final String TAG = TriviaScreen.class.getName();
    private int triviaTypeNr; //0, 1 or 2
    private int triviaNr; // 1,2,3,..
    //private String nowQuestion, correct1,wrong,wrong2;
    private TriviaQuestion currentQuestion;
    private TextButton question,correct,wrong1,wrong2;
    private ImageButton correctBtn,wrong1btn,wrong2btn;
    private int buttonsX, answersX, questionY, answerSpaceY, answerWidth, answerHeight;
    private boolean shouldRemoveFirst;
    private Image congrats;
    private FeteCharacter feteCharacter;


    public TriviaScreen(DirectedGame game, int triviaTypeNr) {
        super(game);
        this.triviaTypeNr = triviaTypeNr;
        triviaNr = 1; // start with first question
        buttonsX= -340;
        answersX = buttonsX +96;
        questionY = 140;
        answerSpaceY = 110;
        answerWidth = 500;
        answerHeight = 86;
        shouldRemoveFirst = false;
    }


    @Override
    public void show() {
        initStageAndCamera();
        addBackButton();
        addQuestionAndAnswers();
        addFeteCharacter();
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
                return new Vector2(answersX,questionY- answerSpaceY);
            case 1:
                return new Vector2(answersX,questionY- answerSpaceY *2 );
            case 2:
                return new Vector2(answersX,questionY- answerSpaceY *3 );
            default:
                return new Vector2(0,0);
        }
    }


    public TriviaQuestion getTriviaQuestion(){
        Json json = new Json();
        return json.fromJson(TriviaQuestion.class, Gdx.files.internal("trivia/"+(triviaNr+triviaTypeNr*3)+".json"));
    }

    public void addQuestionAndAnswers(){
        currentQuestion = getTriviaQuestion();

        if(shouldRemoveFirst){
            removeAllQuestions();
        }
        question = new TextButton(currentQuestion.question, Assets.instance.buttons.triviaQuestion);
        question.setPosition(-question.getWidth()/2,questionY);
        question.setDisabled(true);

        int randomPos = MathUtils.random(2);// 0, 1 or 2
        correct = new TextButton(currentQuestion.correct, Assets.instance.buttons.triviaAnswer);
        correct.setPosition(getPosition(randomPos).x,getPosition(randomPos).y); //when x fixed and stars from left to right
        correct.setSize(answerWidth,answerHeight);
        //correct.setPosition(answerCenterX-correct.getWidth()/2,getPosition(randomPos).y+answerYadjustment); // centered answer
       // correct.getLabel().setAlignment(Align.left);
        correctBtn = new ImageButton(Assets.instance.buttons.answerBtnStyle);
        correctBtn.setPosition(buttonsX,getPosition(randomPos).y);
        addCorrectAction(correct);
        addCorrectAction(correctBtn);

        randomPos = (randomPos+1)%3;
        wrong1 = new TextButton(currentQuestion.wrong1, Assets.instance.buttons.triviaAnswer); //when x fixed and stars from left to right
        wrong1.setPosition(getPosition(randomPos).x,getPosition(randomPos).y);
        wrong1.setSize(answerWidth,answerHeight);
        wrong1btn = new ImageButton(Assets.instance.buttons.answerBtnStyle);
        wrong1btn.setPosition(buttonsX, getPosition(randomPos).y);
        addWrongAction(wrong1);
        addWrongAction(wrong1btn);

        randomPos = (randomPos+1)%3;
        wrong2 = new TextButton(currentQuestion.wrong2, Assets.instance.buttons.triviaAnswer); //when x fixed and stars from left to right
        wrong2.setPosition(getPosition(randomPos).x,getPosition(randomPos).y);
        wrong2.setSize(answerWidth,answerHeight);
        wrong2btn = new ImageButton(Assets.instance.buttons.answerBtnStyle);
        wrong2btn.setPosition(buttonsX,getPosition(randomPos).y);
        addWrongAction(wrong2);
        addWrongAction(wrong2btn);

        stage.addActor(question);
        stage.addActor(correct);
        stage.addActor(wrong1);
        stage.addActor(wrong2);

        stage.addActor(correctBtn);
        stage.addActor(wrong1btn);
        stage.addActor(wrong2btn);


        if(shouldRemoveFirst)
            congrats.toFront();

    }

    private void addFeteCharacter(){
        feteCharacter = new FeteCharacter();
        feteCharacter.setPosition(270,-290);
        feteCharacter.setSize(232,355);

        stage.addActor(feteCharacter);
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
                feteCharacter.doFete();
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
        question.remove();
        correct.remove();
        correctBtn.remove();
        wrong1.remove();
        wrong1btn.remove();
        wrong2.remove();
        wrong2btn.remove();


    }

}
