var express = require('express');
var router = express.Router();
var multer  = require('multer')


var ingredientsUploader = multer({
        dest: './public/upload/ingredients',
        rename: function (fieldname, filename) {
            return filename
            }
});

/* GET users listing. */
router.get('/ingredients', function(req, res) {
  res.render('ingredients', { title: 'Ingredients' });
});

