package uy.aguita.pillo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import uy.aguita.pillo.util.Constants;

/**
 * Created by ewe on 1/15/17.
 */
public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetFonts fonts;
    public AssetButtons buttons;
    public AssetTooth toothGame;
    public AssetTrivia trivia;
    public AssetMatch match;

    public AssetSounds sounds;
    public AssetMusic music;
    public AssetGeneral general;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_PACK, TextureAtlas.class);
        // load sounds
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);
        assetManager.load("sounds/excelente.mp3", Sound.class);
        assetManager.load("sounds/muy_bien.mp3", Sound.class);
        assetManager.load("sounds/perfecto.mp3", Sound.class);
        assetManager.load("sounds/m.mp3", Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_PACK);
        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        // create game resource objects
        fonts = new AssetFonts();
        buttons = new AssetButtons(atlas);
        toothGame = new AssetTooth(atlas);
        trivia = new AssetTrivia(atlas);
        general = new AssetGeneral(atlas);
        match = new AssetMatch(atlas);

        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;
        public final BitmapFont trivia39;
        public AssetFonts () {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"), false);
            defaultNormal = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"), false);
            defaultBig = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"),false);
            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            trivia39 = new BitmapFont(Gdx.files.internal("fonts/hanno-32.fnt"),false);
           // trivia39.getData().setScale(0.65f);
            trivia39.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    public class AssetSounds {
        public final Sound good;
        public final Sound bad;
        public final Sound excelent,vGood,perfect;

        public AssetSounds(AssetManager am) {
            good = am.get("sounds/pickup_coin.wav", Sound.class);
            bad = am.get("sounds/live_lost.wav", Sound.class);
            excelent = am.get("sounds/excelente.mp3", Sound.class);
            vGood = am.get("sounds/muy_bien.mp3", Sound.class);
            perfect= am.get("sounds/perfecto.mp3", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("sounds/m.mp3", Music.class);
        }
    }

    public class AssetGeneral{
        public final TextureAtlas.AtlasRegion exitGame;

        public AssetGeneral(TextureAtlas atlas){
            exitGame  = atlas.findRegion("cartel_salir_del_juego");

        }
    }

    public class AssetMatch{
        public final TextureAtlas.AtlasRegion question;
        public final TextureAtlas.AtlasRegion option1, option2,option3;
        public final TextureAtlas.AtlasRegion img1,img2,img3;
        public final TextureAtlas.AtlasRegion back;

        public AssetMatch(TextureAtlas atlas){
            question = atlas.findRegion("preguntaMatch");
            option1 =atlas.findRegion("todos");
            option2 = atlas.findRegion("comida");
            option3 = atlas.findRegion("a_veces");
            img1 = atlas.findRegion("hilo");
            img2 = atlas.findRegion("cepillo");
            img3 = atlas.findRegion("caramelo");
            back = atlas.findRegion("fondoMatch");

        }
    }

    public class AssetButtons{
        // Menu screen
        public final ImageButton.ImageButtonStyle trivia1;
        public final ImageButton.ImageButtonStyle trivia2;
        public final ImageButton.ImageButtonStyle trivia3;
        public final ImageButton.ImageButtonStyle tooth;
        public final ImageButton.ImageButtonStyle match;

        // Trivia screen
        public final TextButton.TextButtonStyle triviaQuestion;
        public final TextButton.TextButtonStyle triviaAnswer;
        public final ImageButton.ImageButtonStyle answerBtn1Style,answerBtn2Style,answerBtn3Style;
        public final ImageButton.ImageButtonStyle back;

        // MatchScreen screen
        public final TextButton.TextButtonStyle matchText; // match
        public final ImageButton.ImageButtonStyle match1;
        public final ImageButton.ImageButtonStyle match2;
        public final ImageButton.ImageButtonStyle match3;

        // Exit
        public final ImageButton.ImageButtonStyle exitYesStyle,exitNoStyle;

        public  AssetButtons(TextureAtlas atlas) {
            // ----------- Menu screen
            trivia1 = new ImageButton.ImageButtonStyle();  //Instaciate
            trivia1.up = new TextureRegionDrawable(atlas.findRegion("btn-trivia1")); //Set image for not pressed button
            trivia1.down= new TextureRegionDrawable(atlas.findRegion("btn"));  //Set image for pressed
            trivia1.over= new TextureRegionDrawable(atlas.findRegion("btn"));
            //TODO change when we have images
            trivia2 = trivia1;
            trivia3 = trivia1;
            tooth = trivia1;
            match = trivia1;

            back  = new ImageButton.ImageButtonStyle();
            back.up = new TextureRegionDrawable(atlas.findRegion("inicio01"));
            back.down= new TextureRegionDrawable(atlas.findRegion("inicio02"));  //Set image for pressed
            back.over= new TextureRegionDrawable(atlas.findRegion("inicio02"));


            // ----------- Trivia screen
            triviaQuestion = new TextButton.TextButtonStyle();
            triviaQuestion.font = fonts.trivia39;
            triviaQuestion.fontColor = Color.WHITE;

            triviaAnswer = new TextButton.TextButtonStyle(); //-->TEXT
           // triviaAnswer.checkedFontColor = Color.WHITE;
            triviaAnswer.font = fonts.trivia39;
            triviaAnswer.fontColor = Color.WHITE;
            triviaAnswer.downFontColor = Color.GREEN;
            triviaAnswer.overFontColor = Color.GREEN;
            triviaAnswer.up = new TextureRegionDrawable(atlas.findRegion("btn"));

            //-->BTN
            answerBtn1Style= new ImageButton.ImageButtonStyle();
            answerBtn1Style.up = new TextureRegionDrawable(atlas.findRegion("01a"));
            answerBtn1Style.down = new TextureRegionDrawable(atlas.findRegion("01b"));
            answerBtn1Style.over = new TextureRegionDrawable(atlas.findRegion("01b"));
            answerBtn2Style= new ImageButton.ImageButtonStyle();
            answerBtn2Style.up = new TextureRegionDrawable(atlas.findRegion("02a"));
            answerBtn2Style.down = new TextureRegionDrawable(atlas.findRegion("02b"));
            answerBtn2Style.over = new TextureRegionDrawable(atlas.findRegion("02b"));
            answerBtn3Style= new ImageButton.ImageButtonStyle();
            answerBtn3Style.up = new TextureRegionDrawable(atlas.findRegion("03a"));
            answerBtn3Style.down = new TextureRegionDrawable(atlas.findRegion("03b"));
            answerBtn3Style.over = new TextureRegionDrawable(atlas.findRegion("03b"));

            // ----------- MatchScreen screen
            matchText = new TextButton.TextButtonStyle();
            matchText.font = fonts.trivia39;
            matchText.up = new TextureRegionDrawable(atlas.findRegion("02b"));

            match1= new ImageButton.ImageButtonStyle();
            match1.up = new TextureRegionDrawable(atlas.findRegion("btn-trivia1")); //Set image for not pressed button
            match1.down= new TextureRegionDrawable(atlas.findRegion("btn"));  //Set image for pressed
            match1.over= new TextureRegionDrawable(atlas.findRegion("btn"));

            match2 = new ImageButton.ImageButtonStyle();
            match2.up = new TextureRegionDrawable(atlas.findRegion("btn-trivia1")); //Set image for not pressed button
            match2.down= new TextureRegionDrawable(atlas.findRegion("btn"));  //Set image for pressed
            match2.over= new TextureRegionDrawable(atlas.findRegion("btn"));

            match3 = new ImageButton.ImageButtonStyle();
            match3.up = new TextureRegionDrawable(atlas.findRegion("btn-trivia1")); //Set image for not pressed button
            match3.down= new TextureRegionDrawable(atlas.findRegion("btn"));  //Set image for pressed
            match3.over= new TextureRegionDrawable(atlas.findRegion("btn"));

            // -------------- exit confirmation
            exitYesStyle = new ImageButton.ImageButtonStyle();
            exitYesStyle.up = new TextureRegionDrawable(atlas.findRegion("salir_si01"));
            exitYesStyle.down= new TextureRegionDrawable(atlas.findRegion("salir_si02"));  //Set image for pressed
            exitYesStyle.over= new TextureRegionDrawable(atlas.findRegion("salir_si02"));

            exitNoStyle = new ImageButton.ImageButtonStyle();
            exitNoStyle.up = new TextureRegionDrawable(atlas.findRegion("salir_no01"));
            exitNoStyle.down= new TextureRegionDrawable(atlas.findRegion("salir_no02"));  //Set image for pressed
            exitNoStyle.over= new TextureRegionDrawable(atlas.findRegion("salir_no02"));

        }

    }

    public class AssetTooth{
        public final TextureAtlas.AtlasRegion tooth;
        public final Animation toothGood;
        public final Animation toothBad;
        public final Animation bacteria1;
        public final Animation bacteria2;
        public final Animation bacteria3;
        public final Animation bacteria4;
        public final TextureAtlas.AtlasRegion bacteriaTexture;
        public final TextureAtlas.AtlasRegion scoreMenu;
        public final TextureAtlas.AtlasRegion scoreTop;
        public final TextureAtlas.AtlasRegion cross;
        public final TextureAtlas.AtlasRegion background;

        public  AssetTooth(TextureAtlas atlas) {
            tooth = atlas.findRegion("muela_bien",1);
            Array<TextureAtlas.AtlasRegion> regions = null;
            regions = atlas.findRegions("bacteria");
            bacteria1 = new Animation(0.1f , regions);
            regions = atlas.findRegions("muela_bien");
            toothGood = new Animation(0.5f , regions);
            regions = atlas.findRegions("muela_mal");
            toothBad = new Animation(0.2f , regions, Animation.PlayMode.NORMAL);

            bacteria2 = bacteria3 = bacteria4 = bacteria1;
            bacteriaTexture = atlas.findRegion("bakteria");

            scoreMenu = atlas.findRegion("pie_pantalla");
            scoreTop = atlas.findRegion("contador");
            cross = atlas.findRegion("cruz");

            background = atlas.findRegion("fondoMuela");
        }
    }

    public class AssetTrivia{
        public final Animation arm;
        public final Animation mouth;
        public final Animation column1, column2;
        public final TextureAtlas.AtlasRegion eyesClosed;
        public final TextureAtlas.AtlasRegion pillo;
        public final TextureAtlas.AtlasRegion stars,logo, background, congratulation;
        public final TextureAtlas.AtlasRegion q1_1,q1_2,q1_3, q2_1,q2_2,q2_3,q3_1,q3_2;
        public final TextureAtlas.AtlasRegion r1_1,r1_2,r1_3,r1_4,r1_5,r1_6,r1_7,r1_8, r1_9;
        public final TextureAtlas.AtlasRegion r2_1,r2_2,r2_3,r2_4,r2_5,r2_6,r2_7,r2_8, r2_9;
        public final TextureAtlas.AtlasRegion r3_1,r3_2,r3_3,r3_4,r3_5,r3_6;
        public final TextureAtlas.AtlasRegion optionBack,questionBack;


        public  AssetTrivia(TextureAtlas atlas) {
            pillo = atlas.findRegion("pilloTrivia");
            eyesClosed = atlas.findRegion("ojos_pillo02");
            stars = atlas.findRegion("estrellas");
            logo = atlas.findRegion("triviaLogo");
            background = atlas.findRegion("fondoTrivia");
            congratulation = atlas.findRegion("cartel_felicitaciones");

            Array<TextureAtlas.AtlasRegion> regions = null;
            regions = atlas.findRegions("brazo_pillo");
            arm = new Animation(0.10f , regions);
            regions = atlas.findRegions("boca_pillo");
            mouth = new Animation(0.15f , regions);
            regions = atlas.findRegions("columna1");
            column1 = new Animation(0.3f , regions, Animation.PlayMode.LOOP_PINGPONG);
            regions = atlas.findRegions("columna2");
            column2 = new Animation(0.3f , regions, Animation.PlayMode.LOOP);

            q1_1  = atlas.findRegion("nivel1_pregunta1");
            q1_2  = atlas.findRegion("nivel1_pregunta2");
            q1_3  = atlas.findRegion("nivel1_pregunta3");
            q2_1  = atlas.findRegion("nivel2_pregunta1");
            q2_2  = atlas.findRegion("nivel2_pregunta2");
            q2_3  = atlas.findRegion("nivel2_pregunta3");
            q3_1  = atlas.findRegion("nivel3_pregunta1");
            q3_2  = atlas.findRegion("nivel3_pregunta2");

            r1_1  = atlas.findRegion("nivel1_respuesta1");
            r1_2  = atlas.findRegion("nivel1_respuesta2");
            r1_3  = atlas.findRegion("nivel1_respuesta3");
            r1_4  = atlas.findRegion("nivel1_respuesta4");
            r1_5  = atlas.findRegion("nivel1_respuesta5");
            r1_6  = atlas.findRegion("nivel1_respuesta6");
            r1_7  = atlas.findRegion("nivel1_respuesta7");
            r1_8  = atlas.findRegion("nivel1_respuesta8");
            r1_9  = atlas.findRegion("nivel1_respuesta9");

            r2_1  = atlas.findRegion("nivel2_respuesta1");
            r2_2  = atlas.findRegion("nivel2_respuesta2");
            r2_3  = atlas.findRegion("nivel2_respuesta3");
            r2_4  = atlas.findRegion("nivel2_respuesta4");
            r2_5  = atlas.findRegion("nivel2_respuesta5");
            r2_6  = atlas.findRegion("nivel2_respuesta6");
            r2_7  = atlas.findRegion("nivel2_respuesta7");
            r2_8  = atlas.findRegion("nivel2_respuesta8");
            r2_9  = atlas.findRegion("nivel2_respuesta9");

            r3_1  = atlas.findRegion("nivel3_respuesta1");
            r3_2  = atlas.findRegion("nivel3_respuesta2");
            r3_3  = atlas.findRegion("nivel3_respuesta3");
            r3_4  = atlas.findRegion("nivel3_respuesta4");
            r3_5  = atlas.findRegion("nivel3_respuesta5");
            r3_6  = atlas.findRegion("nivel3_respuesta6");

            optionBack = atlas.findRegion("opcion-fondo");
            questionBack = atlas.findRegion("pregunta-fondo");


        }
    }
}