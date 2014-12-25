var fs = require("fs");
var sqlite3 = require("sqlite3").verbose();
var path = '../generated/'

var createDatabase = function(dbFileName){
	var name = path+dbFileName+'.sqlite3'
	var exists = fs.existsSync(name);
	if(!exists) {
	  console.log("Creating DB file.");
	  fs.openSync(name, "w");
	}
	return name;
}


exports.insertData = function(dbFileName, dataIngredients, dataMascots, dataDishes){
	var file = createDatabase(dbFileName);

	var db = new sqlite3.Database(file);
	var exists = fs.existsSync(file);


	db.serialize(function() {
	  
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
	});

	db.close();
}

