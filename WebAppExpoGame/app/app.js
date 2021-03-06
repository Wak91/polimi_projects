var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var expressValidator = require('express-validator'); 

var routes = require('./routes/index');
var users = require('./routes/users');
var mascots = require('./routes/mascots');
var dishes = require('./routes/dishes');
var zones = require('./routes/zones');
var generate = require('./routes/generate')
var ingredients = require('./routes/ingredients');
var api = require('./routes/api')
var images = require('./routes/images')


var multer  = require('multer')

var app = express();


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');


// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(expressValidator());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//mount the middleware functions at the path
app.use('/', routes);
app.use('/users', users);
app.use('/mascots', mascots);
app.use('/dishes',dishes);
app.use('/zones',zones);
app.use('/generate',generate);
app.use('/ingredients',ingredients);
app.use('/api',api);
app.use('/images',images);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


module.exports = app;
