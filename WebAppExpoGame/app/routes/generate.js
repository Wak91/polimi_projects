var express = require('express');
var router = express.Router();
var generator = require('../libs/generatorLibrary')

router.get('/',function(req,res){
	res.render('generate', {
		title: 'Generate Files'
	});
});

router.get

router.post('/db',function(req,res){
	console.log("generate");
	generator.generateDbImagesFiles(function(error,allCorrect){
		if(error){
	        res.render('generate', {
	          title: 'Generate Files',
	          error_message: error
	        });
	        return;
	    }else{
	    	console.log('created files '+allCorrect);
    		res.redirect('/generate');

			
	    }
	});

    
});

router.post('/xml',function(req,res){
	console.log("generate");
	generator.generateXmlFiles(function(error,allCorrect){
		if(error){
	        res.render('generate', {
	          title: 'Generate Files',
	          error_message: error
	        });
	        return;
	    }else{
	    	console.log('created files xml '+allCorrect);
    		res.redirect('/generate');

			
	    }
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

router.post('/download/xml',function(req,res){
  
  res.download('./libs/generated/xmlFiles.tar.gz', 'xmlFiles.tar.gz', function(err){
  if (err) {
  	console.log("ERROR, no file to download");
	res.redirect('/generate');
  }
});
	
});

module.exports = router;