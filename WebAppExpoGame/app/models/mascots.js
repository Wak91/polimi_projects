/*Import to be used in file*/
var mongoose = require('mongoose');
var db = require('./dbModels')
//var translator = require('../libs/translator');

handleError =function(error){
  console.log('error '+error);
}
exports.getMascots = function(callback){
  console.log('called getMascots');
  db.modelMascot.find({},function (err,list){
    if(err){
      console.log(err);
    }else{
      callback(list);
    }
  });

}

exports.createMascot = function(nome, categoria, latitudine, longitudine, immagine, callback){
  db.modelMascot.create({
    name: nome,
    category: categoria,
    latitude: latitudine,
    longitude: longitudine,
    image: immagine

  },function(err, mascot){
    if (err) return handleError(err);
    console.log('saved'+mascot);
    callback(mascot);

  });


}
