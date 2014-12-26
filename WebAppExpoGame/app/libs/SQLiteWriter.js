var fs = require("fs");
var sqlite3 = require("sqlite3").verbose();
var path = '../generated/'

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
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsTable+" (name TEXT PRIMARY KEY, imageUrl TEXT, category TEXT, FOREIGN KEY(category) REFERENCES "+mascotsTable+"(category))");
		var stmt = databaseInstance.prepare("INSERT INTO "+ingredientsTable+" (name, imageUrl ,category) VALUES (?,?,?)");
		dataIngredients.forEach(function(ingredient){
			stmt.run([ingredient["name"],ingredient["imageUrl"],ingredient["category"]])
		});
		stmt.finalize()
	});
}

var insertDataMascots = function(databaseInstance, dataMascots){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+mascotsTable+" (category TEXT PRIMARY KEY, latitude NUMERIC,longitude NUMERIC, modelUrl TEXT,name TEXT)");
		var stmt = databaseInstance.prepare("INSERT INTO "+mascotsTable+" (category, latitude ,longitude, modelUrl,name) VALUES (?,?,?,?,?)");
		stmt.run(["ciao",432,32,"ew","we"])
		dataMascots.forEach(function(mascot){
			stmt.run([mascot["category"],mascot["latitude"],mascot["longitude"],mascot["modelUrl"],mascot["name"]])
		});
		stmt.finalize();
	});
}

var insertDataDishes = function(databaseInstance, dataDishes){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+dishesTable+" (name TEXT PRIMARY KEY,nationality TEXT, imageUrl TEXT, description TEXT, zone TEXT,created NUMERIC)");
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
	var name = path+dbFileName+'.sqlite3'
	var databaseInstance = createDatabase(name);


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

