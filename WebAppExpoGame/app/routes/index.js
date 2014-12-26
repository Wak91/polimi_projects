var express = require('express');
var router = express.Router();
var sql = require('../libs/SQLiteWriter')
/* GET home page. */
router.get('/', function(req, res) {
	sql.insertData("dbFileName", [{"name":"name", "imageUrl":"imageUrl" ,"category":"category"}], 
		[{"category":"category", "latitude":32 ,"longitude":43, "modelUrl":"modelUrl","name":"name"}], 
		[{"name":"name" ,"nationality":"it" , "imageUrl":"imageUrl" , "description":"fds" , "zone":"w","ingredients":["name","name2"] }])
  res.render('index', { title: 'Express' });
});

module.exports = router;
