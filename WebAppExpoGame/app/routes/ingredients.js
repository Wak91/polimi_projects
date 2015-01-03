var express = require('express');

//A router is an isolated instance of middleware and routes. 
//Routers can be thought of as "mini" applications, capable only of performing middleware and routing functions.

var router = express.Router();
var IngredientModel = require('../models/ingredients');

router.get('/', function(req, res){
	IngredientModel.getIngredients(function(error,ingredient){
	    console.log('returned ingredients '+ingredient);
	    res.render('ingredient', {
	    title: 'Ingredient',
	    ingredient: ingredient
	  });
	});
});


router.get('/new', function(req, res){
		res.render('ingredient');
	});


router.post('/',function(req,res){
	var name = req.body.name;
	var imageUrl = req.body.imageUrl;
	dishesModel.insertIngredient(name,imageUrl,function(error,ingredient){
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
