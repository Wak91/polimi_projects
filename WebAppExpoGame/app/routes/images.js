var express = require('express');
var router = express.Router();
var async = require('async');
var multer  = require('multer');


/*
Module used to upload a file from the view
*/
var imageUploader = multer({
        dest: './public/upload/others',
        rename: function (fieldname, filename) {
            return filename
            }
});


router.post('/',imageUploader,function(req,res){

    res.render('generate', {
		title: 'Generate Files'
	});
});

router.post('/download',function(req,res){
	res.download('./libs/generated/images.tar.gz', 'images.tar.gz', function(err){
  if (err) {
  	console.log("ERROR, no file to download");
	res.redirect('/generate');
  }
	});
});


module.exports = router;
