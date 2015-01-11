var sqliteWriter = require('./SQLiteWriter');
var modelIngredients = require('../models/ingredients');
var modelMascots = require('../models/mascots');
var modelDishes = require('../models/dishes');
var async = require('async')


var createSQLiteDatabase = function(){
	async.parallel([
		        function(callback){
		            modelIngredients.getIngredients(function(error,list){
		            	if(error){
							console.log(error);
						}else{

							ingredientsList = [];
							for (var i = list.length - 1; i >= 0; i--) {
								object = {"name":list[i]["name"],"imageUrl":list[i]["imageUrl"],"category":list[i]["category"]}
								ingredientsList.push(object);
							}

							callback(null,ingredientsList);

						}
						
		            });
		        },
		        function(callback){
		        	modelMascots.getMascots(function(list){
		        		mascotsList = [];
		        		for (var i = list.length - 1; i >= 0; i--) {
								object = {"category":list[i]["category"],"latitude":list[i]["latitude"],"longitude":list[i]["longitude"],"modelUrl":list[i]["modelUrl"],"name":list[i]["name"]}
								mascotsList.push(object);
							}
						callback(null,mascotsList)
		        	});
		        },
		        function(callback){
		            modelDishes.getDishes(function(error,list){
						if(error){
							console.log(error);
						}else{
							dishList = []
							for (var i = list.length - 1; i >= 0; i--) {
								object = {"name":list[i]["name"],"nationality":list[i]["nationality"],"imageUrl":list[i]["imageUrl"],"description":list[i]["name"],"zone":list[i]["zone"],"ingredients":list[i]["ingredients"]}
								dishList.push(object);
							}
							callback(null,dishList)
						}
					});
				}
				
		    ],
			function(err, results){
				if(err){
					console.log(err);
					return false;
				}else{
					console.log("createSQLiteDatabase");
					console.log(results[0])
										console.log(results[1])
					console.log(results[2])

					sqliteWriter.insertData("db",  results[0],results[1],results[2]);
					return true;
				}
		    });
	
	return true;
}


var createXmlFiles = function(){
	return true;
}

exports.generateAppFiles = function(callback){
	var error = undefined;
	resultDatabase = createSQLiteDatabase();
	if(!resultDatabase){
		error = "Error creation database SQLite";
		callback(error,"all correct");

	}
	resultXml = createXmlFiles();
	if(!resultXml){
		error = "Error creation XML files";
		callback(error,"all correct");

	}
	callback(error,"all correct");


}