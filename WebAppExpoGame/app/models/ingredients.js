//INITIAL CONFIGURATION--------------------------------
var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');
//-----------------------------------------------------

//-----PRIVATE METHODS (1) -------------------

/*
(1)
Private function exploited here to 
handle errors
*/
var handleError = function(error){
	//console.log(error);
	switch (error.name){
	    case 'ValidationError':
	      return 'All the fields are required!';
	      break;
	    default:
	      return 'There was an error submitting your data';
	      break;
    }
}

//---------------------------------------------


//-----EXPORTED METHODS (2) -------------------

/*
(1)
Method exploited to return all the ingredients
in mongoDB exploiting mongoose .find()
*/
exports.getIngredients = function(callback){
	db.modelIngredient.find({},function(err,list){
		var error = undefined;
		if(err){
			error = handleError(err);
		}
		callback(error,list);		
	});
}

/*
(2)
Insert a new ingredient in mongoDB, it performs automatically
the translation of the name exploiting the libs/translator 
*/
exports.insertIngredient = function(name_,imageUrl_,mascot_,callback){


	db.modelIngredient.create({
	    name:name_,
		imageUrl:imageUrl_,
		category:mascot_
	},function(err, ingredient){
		var error = undefined;
	    if (err){
	    	error = handleError(err);
	  	}
	  	callback(error,ingredient)
	  	
	});
		
			
	
}

//------------------------------------------
