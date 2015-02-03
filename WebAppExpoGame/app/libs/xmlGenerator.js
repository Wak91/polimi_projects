var builder = require('xmlbuilder');
var fs = require('fs');
var dir = require('path');
var path = dir.join(__dirname, 'generated/xmls/')
var S=require('string');




var writeXmlItalian = function(ingredientsList,dishesList,zonesList,mascotsList){

	var rootItalian = builder.create('resources');
	
	rootItalian.com("INGREDIENTS NAMES");
	ingredientsList.forEach(function(ingredient){
		var item = rootItalian.ele('string');

		item.att("name",S(ingredient["name"]).replaceAll(" ", "_"));
		item.txt(ingredient["name"]);

		 
	});

	rootItalian.com("ZONES NAMES");
	zoneList.forEach(function(zone){
		var item = rootItalian.ele('string');
		item.att("name",S(zone["zone"]).replaceAll(" ", "_"));
		item.txt(zone["zone"]);
	});

	rootItalian.com("DISHES NAMES - DESCRIPTIONS - CURIOSITY");
	dishesList.forEach(function(dish){
		var nationality = rootItalian.ele('string');
		nationality.att("name",S(dish["nationality"]).replaceAll(" ", "_"));
		nationality.txt(dish["nationality"]);

		var description = rootItalian.ele('string');
		description.att("name",S(dish["name"]).replaceAll(" ", "_")+"_descr");
		description.txt(S(dish["description"]).replaceAll("'","\'"));

		var curiosity = rootItalian.ele('string');
		curiosity.att("name",S(dish["name"]).replaceAll(" ", "_")+"_curiosity");
		curiosity.txt(S(dish["curiosity"]).replaceAll("'","\'"));

	});

	rootItalian.com("MASCOTS CATEGORIES")
	mascotsList.forEach(function(mascot){
		var item = rootItalian.ele('string');
		item.att("name",S(mascot["category"]).replaceAll(" ","_"))
		item.txt(mascot["category"]);
	});


	fs.writeFile('./libs/generated/xmls/strings_ita.xml', rootItalian.doc().end({pretty: true, indent: '  ', newline: '\n' }), function (err) {
	  if (err) throw err;
	  console.log('It\'s saved!');
	});
	

}

var writeXmlEnglish = function(ingredientsList,dishesList,zonesList,mascotsList){

	var rootEnglish = builder.create('resources');

	fs.writeFile('./libs/generated/xmls/strings_en.xml', rootEnglish.doc().end({pretty: true, indent: '  ', newline: '\n' }), function (err) {
	  if (err) throw err;
	  console.log('It\'s saved!');
	});

}


exports.writeXml = function(ingredientsList,dishesList,zonesList,mascotsList){

	if(!fs.existsSync(path)){
		fs.mkdirSync(path, 0766, function(err){
			if(err){
				console.log(err);
				response.send("ERROR! Can't make the directory! \n");    // echo the result back
			}
		});
 	}
	writeXmlItalian(ingredientsList,dishesList,zonesList,mascotsList);

	writeXmlEnglish(ingredientsList,dishesList,zonesList,mascotsList);

	

}



