var express = require('express');
var router = express.Router();
var dishesModel = require('../models/dishes')
var zonesModel = require('../models/zones')
var ingredientsModel = require('../models/ingredients')
var async = require('async')
var multer  = require('multer')


var dishesUploader = multer({
        dest: './public/upload/dishes',
        rename: function (fieldname, filename) {
            return filename
            }
});


router.get('/', function(req, res){
	dishesModel.getDishes(function(error,dishes){
	    console.log('returned dishes '+dishes);
	    res.render('dishes', {
	    title: 'List Dishes',
	    dishes: dishes
	  });
	});
});

router.post('/',dishesUploader,function(req,res){
	var name = req.body.name;
	var nationality = req.body.nationality;
	var imageUrl = req.body.imageUrl;
	var description = req.body.description;
	var ingredients = req.body.components;

	var zone = req.body.zone;
	console.log(ingredients)

	req.assert('name', 'Name is required').notEmpty();
	req.assert('nationality', 'Nationality is required').notEmpty();
	req.assert('description', 'Description is required').notEmpty();
    req.assert('imageUrl', 'imageUrl is required').notEmpty();
    req.assert('zone', 'zone is required').notEmpty();
    req.assert('components', ' components are required').notEmpty();
    req.assert('components', 'at least two ingredients').len(2,20);

	var errors = req.validationErrors();

    if(errors){   //errors found
    	console.log(errors);
        res.redirect('/dishes/new');
	        return;
       }


	dishesModel.insertDish(name,nationality,imageUrl,description,ingredients,zone,function(error,dish){
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
router.delete('/:id', function(req, res){
	dishesModel.deleteDish(req.params.id,function(){
		res.send("ok");
	})
});

module.exports = router;
