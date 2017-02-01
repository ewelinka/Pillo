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

    public AssetSounds sounds;
    public AssetMusic music;

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

        public AssetSounds(AssetManager am) {
            good = am.get("sounds/pickup_coin.wav", Sound.class);
            bad = am.get("sounds/live_lost.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am) {
            song01 = am.get("sounds/m.mp3", Music.class);
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
        public final TextureAtlas.AtlasRegion answerBtn;
        public final ImageButton.ImageButtonStyle answerBtnStyle;
        public final ImageButton.ImageButtonStyle back;

        // Match screen
        public final TextButton.TextButtonStyle matchText; // match
        public final ImageButton.ImageButtonStyle match1;
        public final ImageButton.ImageButtonStyle match2;
        public final ImageButton.ImageButtonStyle match3;

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

            answerBtn = atlas.findRegion("btn"); //-->BTN
            answerBtnStyle= new ImageButton.ImageButtonStyle();
            answerBtnStyle.up = new TextureRegionDrawable(answerBtn);
            answerBtnStyle.down = new TextureRegionDrawable(answerBtn);
            answerBtnStyle.over = new TextureRegionDrawable(answerBtn);

            // ----------- Match screen
            matchText = new TextButton.TextButtonStyle();
            matchText.font = fonts.trivia39;
            matchText.up = new TextureRegionDrawable(answerBtn);

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
        public final Animation fete;

        public  AssetTrivia(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions = null;
            regions = atlas.findRegions("muela_bien"); //change!
            fete = new Animation(0.3f , regions);
        }
    }
}