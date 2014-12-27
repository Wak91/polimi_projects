var express = require('express');
var router = express.Router();
var mascotsModel = require('../models/mascots')


/* GET mascots listing. */
router.get('/', function(req, res){
  mascotsModel.getMascots(function(mascots){
    console.log('returned mascots '+mascots);
    res.render('mascots', {
    title: 'Mascots',
    mascots: mascots
  });

  });



});

router.post('/',function(req,res){
  var category = req.body.category;
  var latitude = req.body.latitude;
  var longitude = req.body.longitude;
  var image = req.body.image;
  var name = req.body.name;
  console.log('category '+category+' \nlatitude '+latitude+' \nlongitude '+longitude+' \nimage '+image+' \nname '+name);
  mascotsModel.createMascot(name,category, latitude, longitude, image,function(mascot){
    console.log('created'+mascot);
    res.redirect('/mascots');

  });


});

/* New mascot. */
router.get('/new', function(req, res){
    res.render('mascot', {
    title: 'Create Mascot'});
});


module.exports = router;
