var fs = require("fs");
var sqlite3 = require("sqlite3").verbose();
var dir = require('path');

var path = dir.join(__dirname, 'generated/')

var ingredientsTable = 'Ingredients'
var mascotsTable = 'Mascots'
var dishesTable = 'Dishes'
var ingredientsInDishesTable = 'IngredientsInDishes'

var createDatabase = function(dbFileName){
	var exists = fs.existsSync(dbFileName);
	if(!exists) {
	  console.log("Creating DB file.");
	  fs.openSync(dbFileName, "w");
	}
	var db = new sqlite3.Database(dbFileName);
	
	db.run("DROP TABLE IF EXISTS "+ingredientsTable);
	db.run("DROP TABLE IF EXISTS "+mascotsTable);
	db.run("DROP TABLE IF EXISTS "+dishesTable);
	db.run("DROP TABLE IF EXISTS "+ingredientsInDishesTable);


	return db;
}

var insertDataIngredients = function(databaseInstance, dataIngredients){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, imageUrl TEXT, category TEXT, FOREIGN KEY(category) REFERENCES "+mascotsTable+"(category))");
		var stmt = databaseInstance.prepare("INSERT INTO "+ingredientsTable+" (name, imageUrl ,category) VALUES (?,?,?)");
		dataIngredients.forEach(function(ingredient){
			stmt.run([ingredient["name"],ingredient["imageUrl"],ingredient["category"]])
		});
		stmt.finalize()
	});
}

var insertDataMascots = function(databaseInstance, dataMascots){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+mascotsTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT, latitude TEXT,longitude TEXT, modelUrl TEXT,name TEXT)");
		var stmt = databaseInstance.prepare("INSERT INTO "+mascotsTable+" (category, latitude ,longitude, modelUrl,name) VALUES (?,?,?,?,?)");
		dataMascots.forEach(function(mascot){
			stmt.run([mascot["category"],mascot["latitude"],mascot["longitude"],mascot["modelUrl"],mascot["name"]])
		});
		stmt.finalize();
	});
}

var insertDataDishes = function(databaseInstance, dataDishes){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+dishesTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT ,nationality TEXT, imageUrl TEXT, description TEXT, zone TEXT,created NUMERIC)");
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsInDishesTable+" (idDish TEXT , idIngredient TEXT, PRIMARY KEY(idDish,idIngredient))");
		var stmtDish = databaseInstance.prepare("INSERT INTO "+dishesTable+" (name ,nationality , imageUrl , description , zone ,created ) VALUES (?,?,?,?,?,?)");
		var stmtRelation = databaseInstance.prepare("INSERT INTO "+ingredientsInDishesTable+" (idDish ,idIngredient) VALUES (?,?)");
		dataDishes.forEach(function(dish){
			stmtDish.run([dish["name"],dish["nationality"],dish["imageUrl"],dish["description"],dish["zone"],0])
			dish["ingredients"].forEach(function(ingredient){
				stmtRelation.run([dish["name"],ingredient]);
			});
		});
		stmtDish.finalize();
		stmtRelation.finalize();
	});
}



exports.insertData = function(dbFileName, dataIngredients, dataMascots, dataDishes){
	if(!fs.existsSync(path)){
		fs.mkdirSync(path, 0766, function(err){
			if(err){ 
				console.log(err);
				response.send("ERROR! Can't make the directory! \n");    // echo the result back
			}
		});   
 	}
	var name = path+dbFileName+'.sqlite'
	console.log(name);
	var databaseInstance = createDatabase(name);

	console.log("INSERT DATA")
	insertDataMascots(databaseInstance,dataMascots);

	insertDataIngredients(databaseInstance,dataIngredients);

	insertDataDishes(databaseInstance,dataDishes);



	/*db.serialize(function() {
	  
	  db.run("CREATE TABLE IF NOT EXISTS Stuff (thing TEXT)");
	  
	  
	  var stmt = db.prepare("INSERT INTO Stuff VALUES (?)");
	  
	//Insert random data
	  var rnd;
	  for (var i = 0; i < 10; i++) {
	    rnd = Math.floor(Math.random() * 10000000);
	    stmt.run("Thing #" + rnd);
	  }
	  
	stmt.finalize();
	  db.each("SELECT rowid AS id, thing FROM Stuff", function(err, row) {
	    console.log(row.id + ": " + row.thing);
	  });
	});*/
	//db.close();
	databaseInstance.close()

}

