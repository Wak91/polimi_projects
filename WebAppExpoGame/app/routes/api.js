var express = require('express');
var router = express.Router();
var mascots = require('../models/mascots');

module.exports = router;

router.get("/mascots", function(req, res){
	mascots.getMascots(function(listMascots){
		res.json({"list":listMascots});
	});
});


