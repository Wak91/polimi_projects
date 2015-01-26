var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');

var handleError = function(error){
	console.log(error);
}

/*
function used to insert a new zone in the db
*/
exports.insertZone = function(zone_,imageUrl_,callback){
	
		
		db.modelZone.findOne({"zone":zone_},function(err,zone){
			if(zone){
				console.log("already zone in db")
				callback("The zone is already in the db",zone);
			}else{
				db.modelZone.create({
					zone:zone_,
					imageUrl:imageUrl_
					},function(err,zone){
						var error = undefined;
						if(err){
							error = handleError(err);
						}
						callback(error,zone);
					});
			}
		});
	
}

/*
retrieve all the zones
*/
exports.getZones = function(callback){
	db.modelZone.find({},function(err,list){
		var error = undefined;
		var zoneList = []
		if(err){
			error = handleError(err);
		}else{
			for (var i = list.length - 1; i >= 0; i--) {
				zoneList.push(list[i]['zone'])
			};
			
		}
		callback(error,zoneList);
	});
}