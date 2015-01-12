var mongoose = require( 'mongoose' );
var IP_MONGODB = 'localhost'


//------INGREDIENT SETUP---------------------
//Author: @degrigis

var ingredientCollection = 'ingredients'

var ingredient = new mongoose.Schema({
	name: {type: String, required: true ,min: 3},
	imageUrl: {type: String, required: true},
	category: {type: String, required: true}
},{collection: ingredientCollection});

var modelIngredient = mongoose.model( 'ingredient', ingredient);

//-------------------------------------------

//------MASCOT SETUP---------------------
//Author: @fonz

var mascotCollection = 'mascots'

var mascot = new mongoose.Schema({
	category:{type: String, required: true},
	latitude:{type: Number, required: true},
	longitude:{type: Number, required: true},
	image:{type: String, required: true},
	modelUrl:{type: String, required: true},
	name:{type: String, required: true}

},{collection:mascotCollection});

var modelMascot = mongoose.model( 'mascot' , mascot);

//-------------------------------------------

//------DISH SETUP---------------------
//Author: @acorna

var dishCollection = 'dishes'

var dish = new mongoose.Schema({
	name:{type: String, required: true},
	nationality:{type:String, required: true},
	imageUrl:{type:String, required: true},
	description:{type: String, required: true},
	ingredients:Array,
	zone:{type:String, required: true}
},{collection:dishCollection});

var modelDish = mongoose.model( 'dish' , dish);

//-------------------------------------------


//------ZONES SETUP---------------------
//Author: @acorna

var zoneCollection = 'zones'

var zone = new mongoose.Schema({
	zone:{type: String, required: true}
},{collection:zoneCollection});

var modelZone = mongoose.model('zone',zone);

//-------------------------------------------

//------STATS SETUP---------------------
//Author: @acorna

var modelStatistic = 'statistic'
var statisticCollection = 'statistics'

var statistic = new mongoose.Schema({

},{collection:statisticCollection});

mongoose.model( modelStatistic , statistic);

//-------------------------------------------

//-----CONNECTTION TO MONGODB-----------------------------------------------

mongoose.connect( 'mongodb://'+IP_MONGODB+'/expogame' );

exports.modelIngredient = modelIngredient
exports.ingredients = mongoose.connection.collection(ingredientCollection)

exports.modelMascot = modelMascot
exports.mascots = mongoose.connection.collection(mascotCollection)

exports.modelDish = modelDish
exports.dishes = mongoose.connection.collection(dishCollection)

exports.modelStatistic = modelStatistic
exports.statistics = mongoose.connection.collection(statisticCollection)

exports.modelZone = modelZone
exports.zones = mongoose.connection.collection(zoneCollection)

//--------------------------------------------------------------------------
