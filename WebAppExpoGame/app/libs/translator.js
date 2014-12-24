
var credentialFile = require('./credentialTranslator')
var MsTranslator = require('mstranslator');


var client = new MsTranslator({
      client_id: credentialFile.client_id
      , client_secret: credentialFile.client_secret
	}, true);

exports.translate = function translate(params,callback){
	
	client.translate(params, function(err, data) {
         callback(data);
    });
};