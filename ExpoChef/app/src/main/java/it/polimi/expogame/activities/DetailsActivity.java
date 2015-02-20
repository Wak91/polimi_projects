package it.polimi.expogame.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import java.util.HashMap;

import it.polimi.expogame.R;
import it.polimi.expogame.database.IngredientsInDishes;
import it.polimi.expogame.fragments.info.DetailsFragment;
import it.polimi.expogame.fragments.info.HintFragmentDialog;
import it.polimi.expogame.providers.DishesProvider;
import it.polimi.expogame.support.ConverterImageNameToDrawableId;
import it.polimi.expogame.support.Dish;
import it.polimi.expogame.support.Hint;

/**
 * This class implements the activity to show details about a dish unlocked by user
 */
public class DetailsActivity extends ActionBarActivity implements HintFragmentDialog.OnHintUnlockedListener{

    public static final String TAG ="Details Activity";
    private PostObject objectToPost;

    @Override
    /**
     * Load all information from the intent pass by zonefragment to create a dish object
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        long id = getIntent().getLongExtra("idDish",0);
        String name = getIntent().getStringExtra("nameDish");
        String nationality = getIntent().getStringExtra("nationalityDish");
        String imageUrl = getIntent().getStringExtra("imageUrlDish");
        String description = getIntent().getStringExtra("descriptionDish");
        String zone = getIntent().getStringExtra("zoneDish");
        String curiosity = getIntent().getStringExtra("curiosityDish");
        Integer difficulty = getIntent().getIntExtra("difficultyDish", 0);

        boolean created = getIntent().getBooleanExtra("createdDish",false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment(new Dish(getApplicationContext(),id, name, nationality, imageUrl, description, zone, created,null,curiosity,difficulty)))
                    .commit();
        }
        setTitle(getTitle()+" "+name);
        objectToPost = new PostObject(getApplicationContext(),name,imageUrl);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_facebook:
                launchPostActivity();
                return true;
            default:
                onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    private void launchPostActivity(){
        Intent intent = new Intent(getApplicationContext(),FacebookShareActivity.class);
        intent.putExtra("name", objectToPost.getName());
        int id = objectToPost.getImageId();
        intent.putExtra("image",id);
        startActivity(intent);
    }

    @Override
    public void hintUnlocked(String nameDish, String nameIngredient) {
        setIngredientSuggested(nameDish,nameIngredient);
    }

    private void setIngredientSuggested(String nameDish, String nameIngredient){
        Uri uri = Uri.parse(DishesProvider.CONTENT_URI+"/ingredients");
        String where = IngredientsInDishes.COLUMN_ID_DISH + " = ? AND "+IngredientsInDishes.COLUMN_ID_INGREDIENT + " = ?";
        String[] selectionArgs = new String[]{nameDish,nameIngredient};

        ContentValues values = new ContentValues();
        values.put(IngredientsInDishes.COLUMN_HINT_GIVEN, 1);

        getContentResolver().update(uri, values, where, selectionArgs);
    }


    private class PostObject{
        private String name;
        private int imageId;

        public PostObject(Context context,String name,String imageUrl){
            this.name = name;
            this.imageId = ConverterImageNameToDrawableId.convertImageNameToDrawable(context,imageUrl);
        }

        public String getName() {
            return name;
        }

        public int getImageId() {
            return imageId;
        }
    }


}
