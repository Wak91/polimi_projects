var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/ingredients', function(req, res) {
  res.render('ingredients', { title: 'Ingredients' });
});

