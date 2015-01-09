/*Import to be used in file*/
var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');

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

exports.getIngredients = function(callback){
	db.modelIngredient.find({},function(err,list){
		var error = undefined;
		if(err){
			error = handleError(err);
		}
		callback(error,list);		
	});
}

exports.insertIngredient = function(name_,imageUrl_,callback){


	db.modelIngredient.create({
	    name:name_,
		imageUrl:imageUrl_,
	},function(err, ingredient){
		var error = undefined;
	    if (err){
	    	error = handleError(err);
	  	}
	  	callback(error,ingredient)
	  	
	});
		
			
	
}


