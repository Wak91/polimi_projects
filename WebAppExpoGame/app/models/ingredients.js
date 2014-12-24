/*Import to be used in file*/
var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');




/*
Function in order to retrieve all ingredients in the db.
*/
exports.getIngredients = function getIngredients(callback){
	var ingredients = mongoose.model(db.modelIngredient);
	ingredients.find({},function (err,list){
		if(err){
			console.log(err);
		}else{
			callback(list);
		}
	});
}

