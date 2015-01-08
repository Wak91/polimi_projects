//Author @degrigis

/*UTILITY EXPLOITED HERE-------------------------------

-express-validator ( http://blog.ijasoneverett.com/2013/04/form-validation-in-node-js-with-express-validator/ )

*///-----------------------------------------------------



//INITIAL CONFIGURATION--------------------------------
var express = require('express');
var router = express.Router();
var IngredientModel = require('../models/ingredients');
module.exports = router;
//-----------------------------------------------------


//ROUTE HANDLING ( 3 ) 
//-----------------------------------------------------


/*
(1)
Initial route for the page ingredients , it extract all the ingredients
from mongoDB and save it in 'ingredient' in order to show them later 
*/
router.get('/', function(req, res){
	IngredientModel.getIngredients(function(error,ingredient){
	    console.log('returned ingredients '+ingredient);
	    res.render('ingredients', {
	    title: 'Ingredient',
	    ingredient: ingredient
	  });
	});
});


/*
(2)
Route called to access the page to insert a new 
ingredient ( called from ingredients.jade )
*/
router.get('/new', function(req, res){
		res.render('ingredient');
	});


/*
(3)
Handling the POST from the page ingredient.jade,
it checks via express-validator if the values of 
the forms are ok and then inserts the new ingredient in 
mongoDB using 'IngredientModel.insertIngredient'
*/
router.post('/',function(req,res){

	var name = req.body.name;
	var imageUrl = req.body.imageUrl;       
    
    //let's exploit the express-validator middleware 
 
 	req.assert('name', 'Name is required').notEmpty(); 
 	req.assert('imageUrl', 'Image is required').notEmpty();    


 	var errors = req.validationErrors();  

    if(errors){   //errors found  
        res.render('ingredient', {
			    title: 'Create Ingredients',
			    error_message: "All fields required",
				});
	        return;
       }

	IngredientModel.insertIngredient(name,imageUrl,function(error,ingredient){

		 if(error){
		 	console.log(error);
				res.render('ingredient', {
			    title: 'Create Ingredients',
			    error_message: error,
				});
	        return;
	    }

		console.log("Created Ingredient "+ingredient);
    	res.redirect('/ingredients');
	});
});

//-----------------------------------------------------
