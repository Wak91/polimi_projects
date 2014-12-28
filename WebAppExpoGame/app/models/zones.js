var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');

var handleError = function(error){
	console.log(error);
}

exports.insertZone = function(zone_,callback){
	db.modelZone.create({
		zone:zone_
	},function(err,zone){
		var error = undefined;
		if(err){
			error = handleError(err);
		}
		callback(error,zone);
	});
}

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