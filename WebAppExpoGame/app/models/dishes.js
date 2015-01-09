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

exports.getDishes = function(callback){
	db.modelDish.find({},function(err,list){
		var error = undefined;
		if(err){
			error = handleError(err);
		}
		callback(error,list);
		
	});
}


exports.insertDish = function(name_,nationality_,imageUrl_,description_,ingredients_,zone_,callback){
		var ingredientsList = []
		for (var i = ingredients_.length - 1; i >= 0; i--) {
			console.log(ingredients_[i])
				ingredientsList.push(ingredients_[i]['names'][0]['name'])
			};
		var params = {text: description_, from: 'it', to: 'en'};
		translator.translate(params,function(translation){
			db.modelDish.create({
			    name:name_,
				nationality:nationality_,
				imageUrl:imageUrl_,
				ingredients:ingredientsList,
				descriptions:[{description:description_,country:'it'},{description:translation,country:'en'}],
				zone:zone_
			},function(err, dish){
				var error = undefined;
			    if (err){
			    	error = handleError(err);
			  	}
			  	callback(error,dish)
			  	
			});
		});
			
}

/*
exports.getZones = function(callback){
	db.modelDish.aggregate({ $group: { _id: '$zone'}},{ $project: { zone: 1}},function(err, list) {
        if(err){
			console.log(err);
		}else{
			console.log(list)
			var zoneList = []
			for (var i = list.length - 1; i >= 0; i--) {
				zoneList.push(list[i]['_id'])
			};
			callback(zoneList);
		}
    });
}
*/