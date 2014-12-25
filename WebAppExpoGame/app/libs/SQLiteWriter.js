var fs = require("fs");
var sqlite3 = require("sqlite3").verbose();
var path = '../generated/'

var ingredientsTable = 'Ingredients'
var mascotsTable = 'Mascots'
var dishesTable = 'Dishes'

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




	return db;
}

var insertDataIngredients = function(databaseInstance, dataIngredients){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsTable+" (thing TEXT)");

	});
}

var insertDataMascots = function(databaseInstance, dataMascots){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+mascotsTable+" (thing TEXT)");

	});
}

var insertDataDishes = function(databaseInstance, dataDishes){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+dishesTable+" (thing TEXT)");

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

