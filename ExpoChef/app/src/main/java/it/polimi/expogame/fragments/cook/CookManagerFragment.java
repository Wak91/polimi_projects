package it.polimi.expogame.fragments.cook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.polimi.expogame.R;
import it.polimi.expogame.activities.DetailsActivity;
import it.polimi.expogame.database.DishesTable;
import it.polimi.expogame.providers.DishesProvider;
import it.polimi.expogame.support.Dish;
import it.polimi.expogame.support.ImageAdapterDraggable;
import it.polimi.expogame.support.Ingredient;
import it.polimi.expogame.support.ViewHolder;


public class CookManagerFragment extends Fragment implements  CookFragment.OnDishCreatedListener, IngredientFragment.OnIngredientSelectedListener{


    private ArrayList<Ingredient> ingredientsSelected;
    private ArrayList<Ingredient> ingredientsCombined;
    private GridView gridView;
    private GridView cookerView;
    private ImageAdapterDraggable tovagliaAdapter;
    private ImageAdapterDraggable imageAdapter;
    private ArrayList<String> ingredientsToCombine;
    private ImageView cookerFish;
    private TranslateAnimation enterAnimation;
    private TextView textSpeakMascotte;
    private ImageAdapterDraggable tagliereAdapter;
    private ImageView wasterBinImage;

    private static final String TAG="CookManagerFragment";
    Handler startHandler = new Handler();
    Handler nextHandler = new Handler();
    private ArrayList<String> tutorialStrings;

    private static final long UPDATE_INTERVAL = 2500;


    public static CookManagerFragment newInstance() {
        CookManagerFragment fragment = new CookManagerFragment();

        return fragment;
    }

    public CookManagerFragment() {

        ingredientsSelected = new ArrayList<Ingredient>();
        ingredientsCombined = new ArrayList<Ingredient>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_cook_manager, container, false);
        gridView = (GridView) currentView.findViewById(R.id.ingredient_table);
        tovagliaAdapter = new ImageAdapterDraggable(getActivity(),ingredientsSelected);
        gridView.setAdapter(tovagliaAdapter);
        // Inflate the layout for this fragment
        gridView.setOnDragListener(new MyDragListener());

        cookerView = (GridView) currentView.findViewById(R.id.cooker);
        tagliereAdapter = new ImageAdapterDraggable(getActivity(),ingredientsCombined);
        cookerView.setOnDragListener(new MyDragListener());

        wasterBinImage = (ImageView)currentView.findViewById(R.id.wasterbin);
        wasterBinImage.setOnDragListener(new MyDragListener());

        Log.w(TAG,"grdiviewtag is "+ gridView.getId()); // id = 2131296399
        Log.w(TAG,"cookerView tag is " + cookerView.getId()); // if = 2131296398


        Button cookButton = (Button)currentView.findViewById(R.id.cook_button);
        cookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //on click check if a dish will be unlocked
            public void onClick(View v) {
                ArrayList<String> nameIngredients = reorderListIngredientsToCombine();
                String hash = hashListIngredientsToCombine(nameIngredients);
                checkNewDishUnlocked(hash);
            }
        });
        //
        textSpeakMascotte = (TextView)currentView.findViewById(R.id.speak);
        textSpeakMascotte.setVisibility(View.INVISIBLE);
        //set animation 
        animationCooker(currentView);
        SharedPreferences prefs = getActivity().getSharedPreferences("expogame", Context.MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("firstTime",true);
        if(isFirstTime){

            startAnimation();
            prefs.edit().putBoolean("firstTime",false).commit();

        }
        return currentView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDishCreated(Dish dish) {

    }

    @Override
    public void onIngredientSelected(Ingredient ingredient) {

    }


    public void addIngredientSelected(Ingredient ingredient){
        this.ingredientsSelected.add(ingredient);

        gridView.setAdapter(null);
        tovagliaAdapter.setIngredients(ingredientsSelected);
        gridView.setAdapter(tovagliaAdapter);

        gridView.invalidateViews();
        Log.d("NUMBER", "" + gridView.getAdapter().getViewTypeCount() + " " + tovagliaAdapter.getCount());

    }

    public void removeIngredient(Ingredient ingredient){
        this.ingredientsSelected.remove(ingredient);

        gridView.setAdapter(null);
        tovagliaAdapter.setIngredients(ingredientsSelected);
        gridView.setAdapter(tovagliaAdapter);
        gridView.invalidateViews();

    }



    //method used to sort the arraylist of name of ingredients
    private ArrayList<String> reorderListIngredientsToCombine(){
        ArrayList<String> nameIngredients = new ArrayList<String>();
        for(Ingredient ingredient:ingredientsCombined){
            nameIngredients.add(ingredient.getName());
        }
        Collections.sort(nameIngredients, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        return nameIngredients;
    }

    //get the hash of list ingredients to cook
    private String hashListIngredientsToCombine(ArrayList<String> ingredientsToCombine){
        String stringToHash = "";
        for(String ingredient:ingredientsToCombine){
            stringToHash +=ingredient;
        }
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(stringToHash.getBytes());
        byte[] digest = m.digest();
        BigInteger bi = new BigInteger(1, digest);
        String hashtext =  String.format("%0" + (digest.length << 1) + "x", bi);
        Log.d("HASH",hashtext);

        return hashtext;
    }

    //check if a dish is unlock show details activity and update db
    private void checkNewDishUnlocked(String hash){

        String selection = DishesTable.COLUMN_HASHINGREDIENTS + " = ?";
        String[] selectionArgs = new String[]{hash};
        Cursor cursor = getActivity().getContentResolver().query(DishesProvider.CONTENT_URI,null,selection,selectionArgs,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                cursor.moveToFirst();
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_NAME));
                String nationality = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_NATIONALITY));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_DESCRIPTION));
                String zone = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_ZONE));
                String curiosity = cursor.getString(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_CURIOSITY));
                Integer difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_DIFFICULTY));
                int created = cursor.getInt(cursor.getColumnIndexOrThrow(DishesTable.COLUMN_CREATED));
                boolean createdDish = false;
                if(created == 1){
                    createdDish = true;
                }
                //update db
                String where = DishesTable.COLUMN_NAME + " = ?";
                String[] names = new String[]{name};

                ContentValues values = new ContentValues();

                values.put(DishesTable.COLUMN_CREATED,1);
                getActivity().getContentResolver().update(DishesProvider.CONTENT_URI, values, where, names);
                resetSelectionsIngredients();

                //show details
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                intent.putExtra("idDish",id);
                intent.putExtra("nameDish",name);
                intent.putExtra("nationalityDish",nationality);
                intent.putExtra("imageUrlDish",imageUrl);
                intent.putExtra("descriptionDish",description);
                intent.putExtra("zoneDish",zone);
                intent.putExtra("createdDish",createdDish);
                intent.putExtra("curiosityDish",curiosity);
                intent.putExtra("difficultyDish",difficulty);

                startActivity(intent);
            }else{
                Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.message_toast_cook),Toast.LENGTH_LONG).show();
            }
        }

    }

    //if a dish is created, refresh  combined
    private void resetSelectionsIngredients(){

        ingredientsCombined.clear();

        cookerView.setAdapter(null);
        tagliereAdapter.setIngredients(ingredientsCombined);
        cookerView.setAdapter(tagliereAdapter);

    }



    private class MyDragListener implements View.OnDragListener {

        private void moveToCenter(View parent,View item){
            Log.d(TAG,"Moving back to center from  X= "+ item.getX()+"  Y="+item.getY());
            int X = parent.getWidth()/2;
            int Y = parent.getHeight()/2;
            item.setX(200);
            item.setY(200);
            item.setVisibility(View.VISIBLE);

        }
        private boolean checkOutOfBounds(View item ,float current_x, float current_y){
            Log.d(TAG,"Checking bound");
            Log.w(TAG, " x is " + item.getX()+ " and y is " + item.getY());

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screen_max_x = size.x - item.getWidth();
            int screen_min_x = 0;
            int screen_max_y = size.y -item.getHeight() ;
            int screen_min_y = item.getHeight();
            Log.d(TAG,"screen max x "+screen_max_x);
            Log.d(TAG,"screen min x "+screen_min_x);
         //   Log.d(TAG,"screen max y "+screen_max_y);
         //   Log.d(TAG,"screen min y "+screen_min_y);


            if (current_x<= screen_min_x || current_x>= screen_max_x){
                return true;
            }
            else{
                return false;
            }

        }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED: {

                View ingredient_view = (View) event.getLocalState();
                View parent = (View)ingredient_view.getParent();
                int parent_id  = parent.getId();

                if(parent_id==R.id.cooker)
                  {
                      ingredient_view.setX(((View) event.getLocalState()).getX());
                      ingredient_view.setY(((View) event.getLocalState()).getY());
                      ingredient_view.setVisibility(View.VISIBLE);
                  }
                else
                 {
                     ingredient_view.setX(((View) event.getLocalState()).getX());
                     ingredient_view.setY(((View) event.getLocalState()).getY());
                     ingredient_view.setVisibility(View.VISIBLE);
                 }
            }
            break;
            case DragEvent.ACTION_DROP:

                View ingredient_view = (View) event.getLocalState();
                View parent = (View)ingredient_view.getParent();
                int parent_id  = parent.getId();

                if (parent_id == v.getId()) { // we are moving in the down view
                    if(parent_id==R.id.cooker){

                        ingredient_view.setX(((View) event.getLocalState()).getX());
                        ingredient_view.setY(((View) event.getLocalState()).getY());
                        ingredient_view.setVisibility(View.VISIBLE);

                    }
                    else {
                        if (checkOutOfBounds(ingredient_view, event.getX(), event.getY())) {
                            moveToCenter(v, ingredient_view);
                        } else {
                            float X = event.getX();
                            float Y = event.getY();


                            ingredient_view.setX(X);
                            ingredient_view.setY(Y);
                            ingredient_view.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;

                } else if(v.getId() == R.id.wasterbin){
                    ViewHolder holder = (ViewHolder) ingredient_view.getTag();
                    Ingredient ingredient = holder.getIngredient();
                    if(parent_id == R.id.ingredient_table){
                        ingredientsSelected.remove(ingredient);

                        //refresh adapters of both the grid
                        gridView.setAdapter(null);
                        tovagliaAdapter.setIngredients(ingredientsSelected);
                        gridView.setAdapter(tovagliaAdapter);
                    }else{
                        ingredientsCombined.remove(ingredient);
                        cookerView.setAdapter(null);
                        tagliereAdapter.setIngredients(ingredientsCombined);
                        cookerView.setAdapter(tagliereAdapter);
                    }

                }else { //we are moving from down to up or viceversa

                    int width = ingredient_view.getWidth();
                    int height = ingredient_view.getHeight();
                    //get the object tag that have all the information like ingredient
                    //object, name and picture of ingredient(definition in getView ImageAdapterDraggable)
                    ViewHolder holder = (ViewHolder) ingredient_view.getTag();
                    Ingredient ingredient = holder.getIngredient();

                    Log.d("COOKMANAGER", "NAME INGREDIENT " + ingredient.getName().toString() + " ");

                    if (parent_id==R.id.ingredient_table) { //from down to up ( parent_id is the id of the gridview below )

                        if(ingredientsCombined.size()<4){
                            //add name of ingredient to ingredients selected to cook together
                            ingredientsCombined.add(ingredient);

                            //remove ingredient from selected list in order to pass the correct
                            //list to the new adapter
                            ingredientsSelected.remove(ingredient);

                            //refresh adapters of both the grid
                            gridView.setAdapter(null);
                            tovagliaAdapter.setIngredients(ingredientsSelected);
                            gridView.setAdapter(tovagliaAdapter);



                            cookerView.setAdapter(null);
                            tagliereAdapter.setIngredients(ingredientsCombined);
                            cookerView.setAdapter(tagliereAdapter);
                        }else{

                        }


                    } else { //from up to down


                        ingredientsCombined.remove(ingredient);

                        //add ingredient to selected
                        ingredientsSelected.add(ingredient);

                        //refresh adapter
                        gridView.setAdapter(null);
                        tovagliaAdapter.setIngredients(ingredientsSelected);
                        gridView.setAdapter(tovagliaAdapter);

                        cookerView.setAdapter(null);
                        tagliereAdapter.setIngredients(ingredientsCombined);
                        cookerView.setAdapter(tagliereAdapter);

                    }


                    gridView.invalidateViews();
                    cookerView.invalidateViews();
                    ingredient_view.invalidate();

                    break;

                }

            case DragEvent.ACTION_DRAG_ENDED:
                /*View view = (View) event.getLocalState();
                Log.d
                View parentView = (View)view.getParent();
                int parentId  = parentView.getId();
                if (parentId==R.id.ingredient_table){
                    view.findViewById(R.id.text).setVisibility(View.INVISIBLE);
                }*/

            default:
                break;
        }
        return true;
    }

    }

    /**
     * set animation for the mascotte tutorial
     * @param view
     */
    private void animationCooker(View view){
        cookerFish = new ImageView(getActivity().getApplicationContext());
        cookerFish.setImageDrawable(getResources().getDrawable(R.drawable.cooker));
        cookerFish.setVisibility(View.INVISIBLE);


        Point size = getDimensionScreen();
        int width = size.x;
        float to =  ((float)width)/3;
        enterAnimation = new TranslateAnimation(width, to,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        enterAnimation.setDuration(3000);  // animation duration
        enterAnimation.setFillAfter(true);
        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cookerFish.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadTutorialStrings();
                startHandler.postDelayed(new UpdateTextRunnable(tutorialStrings),UPDATE_INTERVAL);
                textSpeakMascotte.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        FrameLayout layout = (FrameLayout)view.findViewById(R.id.cook_manager_fragment);
        layout.addView(cookerFish);
    }

    public void startAnimation(){
        cookerFish.startAnimation(enterAnimation);
    }

    /**
     * Private class in order to update the text for the tutorial
     */
    private class UpdateTextRunnable implements Runnable{

        private ArrayList<String> texts;

        public UpdateTextRunnable(ArrayList<String> texts){
            this.texts = texts;
        }

        @Override
        public void run() {
            //if i have some text to show update
            if(texts.size() >0) {
                textSpeakMascotte.setText(texts.remove(0));
                nextHandler.postDelayed(new UpdateTextRunnable(texts), UPDATE_INTERVAL);
            }else{
                //else start animation out
                textSpeakMascotte.setVisibility(View.INVISIBLE);
                Point size = getDimensionScreen();
                int width = size.x;
                float to =  ((float)width)/3;
                TranslateAnimation outAnimation = new TranslateAnimation(to, width,
                        0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                outAnimation.setDuration(3000);  // animation duration
                outAnimation.setFillAfter(true);
                cookerFish.startAnimation(outAnimation);
            }
        }
    }

    /**
     * get the dimension of the scree
     * @return point with dimension
     */
    private Point getDimensionScreen(){
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * load text for tutorial
     */
    private void loadTutorialStrings(){
        String tutorialText = getString(R.string.tutorial_text);
        tutorialStrings = new ArrayList<String>();

        String[] parts = tutorialText.split(":");
        for(String item:parts){
                tutorialStrings.add(item);
        }


    }


}
