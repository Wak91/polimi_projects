var express = require('express');

//A router is an isolated instance of middleware and routes. 
//Routers can be thought of as "mini" applications, capable only of performing middleware and routing functions.

var router = express.Router();
var IngredientModel = require('../models/ingredients');

router.get('/', function(req, res){
	IngredientModel.getIngredients(function(error,ingredient){
	    console.log('returned ingredients '+ingredient);
	    res.render('ingredients', {
	    title: 'Ingredient',
	    ingredient: ingredient
	  });
	});
});


router.get('/new', function(req, res){
		res.render('ingredient');
	});



//POST on /ingredients 
router.post('/',function(req,res){

	var name = req.body.name;
	var imageUrl = req.body.imageUrl;       
    
    //let's exploit the express-validator middleware 
    //( http://blog.ijasoneverett.com/2013/04/form-validation-in-node-js-with-express-validator/ )
 	
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


module.exports = router;
