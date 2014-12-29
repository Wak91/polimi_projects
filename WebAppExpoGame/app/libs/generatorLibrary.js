var sqliteWriter = require('./SQLiteWriter');
var modelIngredients = require('../models/ingredients');
var modelMascots = require('../models/mascots');
var modelDishes = require('../models/dishes');
var async = require('async')


var createSQLiteDatabase = function(){
	async.parallel([
		        function(callback){
		            callback(null,[])
		        },
		        function(callback){
		            callback(null,[])
		        },
		        function(callback){
		            modelDishes.getDishes(function(error,list){
						if(error){
							console.log(error);
						}else{
							dishList = []
							for (var i = list.length - 1; i >= 0; i--) {
								object = {"name":list[i]["name"],"nationality":list[i]["nationality"],"imageUrl":list[i]["imageUrl"],"description":list[i]["name"]+"_description","zone":list[i]["zone"],"ingredients":list[i]["ingredients"]}
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
					sqliteWriter.insertData("db",  [{"name":"name", "imageUrl":"imageUrl" ,"category":"category"}], 
						[{"category":"category", "latitude":32 ,"longitude":43, "modelUrl":"modelUrl","name":"name"}],results[2]);
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