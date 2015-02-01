var builder = require('xmlbuilder');
var fs = require('fs');
var dir = require('path');
var path = dir.join(__dirname, 'generated/xmls/')




var writeXmlItalian = function(ingredientsList,dishesList,zonesList){

	var rootItalian = builder.create('resources');
	
	rootItalian.com("INGREDIENTS NAMES");
	ingredientsList.forEach(function(ingredient){
		var item = rootItalian.ele('string');
		item.att("name",ingredient["name"].replace(" ", "_"));
		item.txt(ingredient["name"]);

		 
	});

	rootItalian.com("ZONES NAMES");
	zoneList.forEach(function(zone){
		var item = rootItalian.ele('string');
		item.att("name",zone["zone"].replace(" ", "_"));
		item.txt(zone["zone"]);
	});

	rootItalian.com("DISHES NAMES - DESCRIPTIONS - CURIOSITY");
	dishesList.forEach(function(dish){
		var nationality = rootItalian.ele('string');
		nationality.att("name",dish["nationality"].replace(" ", "_"));
		nationality.txt(dish["nationality"]);

		var description = rootItalian.ele('string');
		description.att("name",dish["name"]+"_descr");
		description.txt(dish["description"]);

		var curiosity = rootItalian.ele('string');
		curiosity.att("name",dish["name"]+"_curiosity");
		curiosity.txt(dish["curiosity"]);

	});


	fs.writeFile('./libs/generated/xmls/strings_ita.xml', rootItalian.doc().end({pretty: true, indent: '  ', newline: '\n' }), function (err) {
	  if (err) throw err;
	  console.log('It\'s saved!');
	});
	

}

var writeXmlEnglish = function(ingredientsList,dishesList,zonesList){

	var rootEnglish = builder.create('resources');

	fs.writeFile('./libs/generated/xmls/strings_en.xml', rootEnglish.doc().end({pretty: true, indent: '  ', newline: '\n' }), function (err) {
	  if (err) throw err;
	  console.log('It\'s saved!');
	});

}


exports.writeXml = function(ingredientsList,dishesList,zonesList){

	if(!fs.existsSync(path)){
		fs.mkdirSync(path, 0766, function(err){
			if(err){
				console.log(err);
				response.send("ERROR! Can't make the directory! \n");    // echo the result back
			}
		});
 	}
	writeXmlItalian(ingredientsList,dishesList,zonesList);

	writeXmlEnglish(ingredientsList,dishesList,zonesList);

	

}



