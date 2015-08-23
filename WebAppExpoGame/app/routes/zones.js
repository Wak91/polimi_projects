var express = require('express');
var router = express.Router();
var zoneModel = require('../models/zones')

var multer  = require('multer')

/*
Module used to upload a file from the view
*/
var dishesUploader = multer({
        dest: './public/upload/zones',
        rename: function (fieldname, filename) {
            return filename
            }
});

router.get('/', function(req, res){
	zoneModel.getZonesData(function(error,zones){
	    console.log('returned dishes '+zones);
	    res.render('zones', {
	    title: 'List Zones',
	    zones: zones
	  });
	});
});



router.post('/',dishesUploader,function(req,res){
	var zone = req.body.zone.toLowerCase();
	console.log(req.files)
	var imageUrl = req.files.imageUrl.name.toLowerCase();

	zoneModel.insertZone(zone,imageUrl,function(error,zone){
		if(error){
			console.log("error zone")
		 	res.render('zone', {
			    title: 'Zone Dish',
				error_message:error});

	    }else{
			console.log("Created zone "+zone);
	    	res.redirect('/zones');
	    }
	});
});

router.get('/new', function(req, res){
		res.render('zone', {
	    title: 'Zone Dish'});
  
});

router.delete('/:id', function(req, res){
	zoneModel.deleteZone(req.params.id,function(){
		res.send("ok");
	})
});
module.exports = router;