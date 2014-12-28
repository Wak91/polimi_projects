var mongoose = require( 'mongoose' );
var IP_MONGODB = 'localhost'

var modelIngredient = 'ingredient'
var ingredientCollection = 'ingredients'

var ingredient = new mongoose.Schema({
	names: Array,
	imageUrl: String,
	category: mongoose.Schema.Types.ObjectId
},{collection: ingredientCollection});

mongoose.model( modelIngredient, ingredient);

var mascotCollection = 'mascots'

var mascot = new mongoose.Schema({
	category:Array,
	latitude:{type: Number, required: true},
	longitude:{type: Number, required: true},
	modelUrl:{type: String, required: true},
	name:{type: String, required: true}

},{collection:mascotCollection});

var modelMascot = mongoose.model( 'mascot' , mascot);

var dishCollection = 'dishes'

var dish = new mongoose.Schema({
	names:Array,
	nationality:{type:String, required: true},
	imageUrl:{type:String, required: true},
	descriptions:Array,
	ingredients:Array,
	zone:{type:String, required: true}
},{collection:dishCollection});

var modelDish = mongoose.model( 'dish' , dish);

var zoneCollection = 'zones'

var zone = new mongoose.Schema({
	zone:{type:String, required:true}
},{collection:zoneCollection});

var modelZone = mongoose.model('zone',zone);

var modelStatistic = 'statistic'
var statisticCollection = 'statistics'

var statistic = new mongoose.Schema({

},{collection:statisticCollection});

mongoose.model( modelStatistic , statistic);

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
