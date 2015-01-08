var express = require('express');
var router = express.Router();
var mascotsModel = require('../models/mascots')


/* GET /mascots: List the mascots. */
router.get('/', function(req, res){
  mascotsModel.getMascots(function(mascots){
    console.log('returned mascots '+mascots);
    res.render('mascots', {
    title: 'Mascots',
    mascots: mascots
  });

  });



});
/* POST /mascots:  Routes which handle the creation of a Mascot */
router.post('/',function(req,res){
  var category = req.body.category;
  var latitude = req.body.latitude;
  var longitude = req.body.longitude;
  var image = req.body.image;
  var name = req.body.name;
  console.log('[MascotsRoutes>post]Mascot created:\ncategory '+category+' \nlatitude '+latitude+' \nlongitude '+longitude+' \nimage '+image+' \nname '+name);
  
  mascotsModel.createMascot(name,category, latitude, longitude, image,function(error,mascot){

    //displaying error message which comes from the model validation
    if(error){
        res.render('mascot', {
          title: 'Create Mascot',
          error_message: error
        });
        return;
    }

    console.log('created'+mascot);
    res.redirect('/mascots');

  });


});

/* GET /mascots/new show the form for creating a mascot */
router.get('/new', function(req, res){
    res.render('mascot', {
    title: 'Create Mascot'});
});


module.exports = router;
