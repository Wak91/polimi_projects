var express = require('express');
var router = express.Router();
var dishesModel = require('../models/dishes')
var zonesModel = require('../models/zones')


router.get('/', function(req, res){
	dishesModel.getDishes(function(error,dishes){
	    console.log('returned dishes '+dishes);
	    res.render('dishes', {
	    title: 'Dishes',
	    dishes: dishes
	  });
	});
});

router.post('/',function(req,res){
	var name = req.body.name;
	var nationality = req.body.nationality;
	var imageUrl = req.body.imageUrl;
	var description = req.body.description;
	var ingredients = req.body.ingredients;
	var zone = req.body.zone;
	dishesModel.insertDish(name,nationality,imageUrl,description,ingredients,zone,function(error,dish){
		 if(error){
		 	console.log(error);
		 	zonesModel.getZones(function(errorZone,list){
				res.render('dish', {
			    title: 'Create Dish',
			    error_message: error,
				zones:list});
			});
	        return;
	    }
		console.log("Created dish "+dish);
    	res.redirect('/dishes');
	});
});

router.get('/new', function(req, res){
	zonesModel.getZones(function(error,list){
		res.render('dish', {
	    title: 'Create Dish',
		zones:list});
	});
    
});

module.exports = router;
