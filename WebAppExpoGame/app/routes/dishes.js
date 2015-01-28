var express = require('express');
var router = express.Router();
var dishesModel = require('../models/dishes')
var zonesModel = require('../models/zones')
var ingredientsModel = require('../models/ingredients')
var async = require('async')
var multer  = require('multer')

/*
Module used to upload a file from the view
*/
var dishesUploader = multer({
        dest: './public/upload/dishes',
        rename: function (fieldname, filename) {
            return filename
            }
});

/*
first route used to retrieve the list of all dishes. The information
are collected from the db and passed in "dishes" variable
*/
router.get('/', function(req, res){
	dishesModel.getDishes(function(error,dishes){
	    console.log('returned dishes '+dishes);
	    res.render('dishes', {
	    title: 'List Dishes',
	    dishes: dishes
	  });
	});
});

/*
route use to receive the data used to create a new dish. Call on dish.jade as a post
to this url. inside the express-validator check if all field required has been inserted.
if it is not true, the response is the insert page, otherwise data are saved in the db
*/
router.post('/',dishesUploader,function(req,res){

    req.assert('name', 'Name is required').notEmpty();
    req.assert('nationality', 'Nationality is required').notEmpty();
    req.assert('description', 'Description is required').notEmpty();
    //req.assert('imageUrl', 'imageUrl is required').notNull();
    req.assert('zone', 'zone is required').notEmpty();
    req.assert('components', ' components are required').notEmpty();
    //req.assert('components', 'at least two ingredients').len(2,20);
    req.assert('difficulty', ' difficulty is required').notEmpty();
    req.assert('curiosity', ' curiosity is required').notEmpty();
    var errors = req.validationErrors();

    if(errors){   //errors found
      console.log(errors);
        res.redirect('/dishes/new');
          return;
       }

      var name = req.body.name.toLowerCase();
	var nationality = req.body.nationality.toLowerCase();
	var imageUrl = req.files.imageUrl.name.toLowerCase();
	var description = req.body.description.toLowerCase();
      var curiosity = req.body.curiosity.toLowerCase();
      var difficulty = req.body.difficulty;
	var ingredients = req.body.components;


	var zone = req.body.zone.toLowerCase();
	console.log(req.files)





    /*
	use the async model in order to do two different asyncronous call and wait for their results
    */
	dishesModel.insertDish(name,nationality,imageUrl,description,ingredients,zone,curiosity,difficulty,function(error,dish){
		 if(error){
		 	console.log(error);
		 	async.parallel([
		        function(callback){
		            zonesModel.getZones(function(err,list){
		                callback(null, list);
		            });
		        },
		        function(callback){
		            ingredientsModel.getIngredients(function(ingredients){
		                callback(null, ingredients);
		            });
		        }
		    ],
		    function(err, results){
		      if(err){
		      	//we arrive here if AT LEAST one function fails
		        console.log(err);
		      }else{
		        res.render('dish', {
				    title: 'Create Dish',
					zones:results[0],
					ingredients:results[1],
					error_message: error
				});
		    	}
		    });
	        return;
	    }
		console.log("Created dish "+dish);
    	res.redirect('/dishes');
	});
});

/*
route used to show the insert page. we load from the database the ingredients and zones lists
*/
router.get('/new', function(req, res){

	async.parallel([
        function(callback){
            zonesModel.getZones(function(error,list){
                callback(null, list);
            });
        },
        function(callback){
            ingredientsModel.getIngredients(function(error,ingredients){
                callback(null, ingredients);
            });
        }
    ],
    function(err, results){
      if(err){
        console.log(err);
        res.redirect('/dishes');

      }else{
        res.render('dish', {
		    title: 'Create Dish',
			zones:results[0],
			ingredients:results[1]
		});
    	}
    });


});

/*
route in order to delete a dish
*/
router.delete('/:id', function(req, res){
	dishesModel.deleteDish(req.params.id,function(){
		res.send("ok");
	})
});

module.exports = router;
