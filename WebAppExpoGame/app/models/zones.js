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
		if(err){
			handleError(err);
		}else{
			callback(zone);
		}
	});
}

exports.getZones = function(callback){
	db.modelZone.find({},function(err,list){
		if(err){
			handleError(err);
		}else{
			var zoneList = []
			for (var i = list.length - 1; i >= 0; i--) {
				zoneList.push(list[i]['zone'])
			};
			callback(zoneList);
		}
	});
}