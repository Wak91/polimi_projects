var express = require('express');
var router = express.Router();
var generator = require('../libs/generatorLibrary')

router.get('/',function(req,res){
	res.render('generate', {
		title: 'Generate Files'
	});
});

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
    res.redirect('/');

	});
});

module.exports = router;