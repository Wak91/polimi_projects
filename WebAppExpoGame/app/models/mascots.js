//INITIAL CONFIGURATION--------------------------------
var mongoose = require('mongoose');
var db = require('./dbModels')
var translator = require('../libs/translator');
//-----------------------------------------------------


/*
Function which return the set of all Mascots
*/
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
/*
Function which return a single Mascot
*/
exports.getMascot = function(name,callback){
  db.modelMascot.findOne({name:name},function (err,mascot){
    if(err){
      console.log(err);
    }else{
      callback(mascot);
    }
  });

}



/*
function which handles the different errors raised when creating the Mascot and return
messages which will be displayed by the View
*/
handleError =function(error){
  console.log('[mascotsModel>HandleError] Error '+error);
  switch (error.name){
    case 'CastError':
      return 'Field Latitude and Longitude must be numerical!';
      break;
    case 'ValidationError':
      return 'All the fields are required!';
      break;
    default:
      return 'There was an error submitting your data';
      break;
  }
}

exports.deleteMascot = function(mascotte,callback){
  console.log("in models mascot ")
  db.modelMascot.findOneAndRemove({name:mascotte},callback);

}

exports.updateMascot = function(name,lat,lng,callback){
  console.log("LATITUDE NAD logintude , [...]")
  var conditions = { name: name },
        update = { latitude:  parseFloat(lat),
                        longitude:  parseFloat(lng)};
  db.modelMascot.update(conditions,update,function(error){
    console.log("error in update Mascot model")
    console.log(error);
    callback(error);
  });
}

/*
Function which handle the creation of the Mascots
Errors handled by handleError funcition defined up here
*/
exports.createMascot = function(nome, categoria, latitudine, longitudine, immagine,modelUrl, callback){
  db.modelMascot.create({
    name: nome,
    category: categoria,
    latitude: parseFloat(latitudine),
    longitude: parseFloat(longitudine),
    image: immagine,
    modelUrl:modelUrl

  },function(err, mascot){
    var error = undefined;
    if (err){
        error = handleError(err);
    }
    console.log('saved'+mascot);
    callback(error,mascot);

  });


}
