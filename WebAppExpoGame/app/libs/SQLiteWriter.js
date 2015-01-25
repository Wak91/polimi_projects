var fs = require("fs");
var sqlite3 = require("sqlite3").verbose();
var dir = require('path');

var path = dir.join(__dirname, 'generated/')

var ingredientsTable = 'Ingredients'
var mascotsTable = 'Mascots'
var dishesTable = 'Dishes'
var ingredientsInDishesTable = 'IngredientsInDishes'
var crypto = require('crypto')


/*
function used in order to create the database
*/
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

/*
insert data ingredients in the sqlite after create ingredients table
*/
var insertDataIngredients = function(databaseInstance, dataIngredients){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, imageUrl TEXT, category TEXT, unlocked NUMERIC, FOREIGN KEY(category) REFERENCES "+mascotsTable+"(category))");
		var stmt = databaseInstance.prepare("INSERT INTO "+ingredientsTable+" (name, imageUrl ,category, unlocked) VALUES (?,?,?,?)");
		dataIngredients.forEach(function(ingredient){
			stmt.run([ingredient["name"],ingredient["imageUrl"],ingredient["category"],0])
		});
		stmt.finalize()
	});
}

/*
insert mascots data after create the mascots table
*/
var insertDataMascots = function(databaseInstance, dataMascots){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+mascotsTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT, latitude TEXT,longitude TEXT, modelUrl TEXT,name TEXT, captured NUMERIC)");
		var stmt = databaseInstance.prepare("INSERT INTO "+mascotsTable+" (category, latitude ,longitude, modelUrl,name,captured) VALUES (?,?,?,?,?,?)");
		dataMascots.forEach(function(mascot){
			stmt.run([mascot["category"],mascot["latitude"],mascot["longitude"],mascot["modelUrl"],mascot["name"],0])
			
		});
		stmt.finalize();
	});
}

/*
insert dishes information in dishes table and create table "IngredientsInDishes" in order
to support the many to many relation between dish and ingredient
*/
var insertDataDishes = function(databaseInstance, dataDishes){
	databaseInstance.serialize(function(){
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+dishesTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT ,nationality TEXT, imageUrl TEXT, description TEXT, zone TEXT,created NUMERIC, hash TEXT)");
		databaseInstance.run("CREATE TABLE IF NOT EXISTS "+ingredientsInDishesTable+" (idDish TEXT , idIngredient TEXT, PRIMARY KEY(idDish,idIngredient))");
		var stmtDish = databaseInstance.prepare("INSERT INTO "+dishesTable+" (name ,nationality , imageUrl , description , zone ,created ) VALUES (?,?,?,?,?,?,?)");
		var stmtRelation = databaseInstance.prepare("INSERT INTO "+ingredientsInDishesTable+" (idDish ,idIngredient) VALUES (?,?)");
		dataDishes.forEach(function(dish){
			//order ingredients in alphabetical order
			dish["ingredients"].sort(function(a,b){
				if(a < b){
					return -1
				}else if(a > b){
					return 1
				}
				return 0;
			});
			
			//concatenate ingredients name
			var stringListIngredient = "";
			dish["ingredients"].forEach(function(ingredient){
				stringListIngredient += ingredient;
				console.log(stringListIngredient)

			});
			//hash concatenation of ingredients name
			var hashIngredients = crypto.createHash('md5').update(stringListIngredient).digest('hex')
			console.log(hashIngredients)
			stmtDish.run([dish["name"],dish["nationality"],dish["imageUrl"],dish["description"],dish["zone"],0,hashIngredients])
			dish["ingredients"].forEach(function(ingredient){
				stmtRelation.run([dish["name"],ingredient]);
			});
		});
		stmtDish.finalize();
		stmtRelation.finalize();
	});
}


/*
exported function the call the prevoius function, create the folder where put the db and 
finalize the file just created
*/
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


	databaseInstance.close()

}

