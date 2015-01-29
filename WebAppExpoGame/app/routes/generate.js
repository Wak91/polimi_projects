var express = require('express');
var router = express.Router();
var generator = require('../libs/generatorLibrary')

router.get('/',function(req,res){
	res.render('generate', {
		title: 'Generate Files'
	});
});

router.get

router.post('/',function(req,res){
	console.log("generate");
	generator.generateAppFiles(function(error,allCorrect){
		if(error){
	        res.render('generate', {
	          title: 'Generate Files',
	          error_message: error
	        });
	        return;
	    }

    console.log('created files '+allCorrect);
    res.redirect('/generate');

	});
});

/*
Route to handle the download of the local.sqlite once
generated with the proper button
*/
router.post('/download',function(req,res){
  
  res.download('./libs/generated/locals.sqlite', 'locals.sqlite', function(err){
  if (err) {
  	console.log("ERROR, no file to download");
	res.redirect('/generate');
  }
});
	
});

module.exports = router;