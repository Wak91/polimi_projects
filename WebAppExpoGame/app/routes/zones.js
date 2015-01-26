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
			console.log("Created dish "+zone);
	    	res.redirect('/dishes');
	    }
	});
});

router.get('/new', function(req, res){
		res.render('zone', {
	    title: 'Zone Dish'});
  
});

module.exports = router;