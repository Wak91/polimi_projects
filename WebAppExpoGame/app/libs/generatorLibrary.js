
var createSQLiteDatabase = function(){
	return true;
}

var createXmlFiles = function(){
	return true;
}

exports.generateAppFiles = function(callback){
	var error = undefined;
	resultDatabase = createSQLiteDatabase();
	if(!resultDatabase){
		error = "Error creation database SQLite";
		callback(error,"all correct");

	}
	resultXml = createXmlFiles();
	if(!resultXml){
		error = "Error creation XML files";
		callback(error,"all correct");

	}
	callback(error,"all correct");


}