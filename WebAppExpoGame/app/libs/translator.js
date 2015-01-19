/*
Import for current file.
CredentialFile must be inserted by developer.
*/
var credentialFile = require('./credentialTranslator')
var MsTranslator = require('mstranslator');

/*Initialiation of translator service*/
var client = new MsTranslator({
      client_id: credentialFile.client_id
      , client_secret: credentialFile.client_secret
	}, true);

/*
Function to manage translation requests contained in "params" parameter
The parameter params must be organized as 
{
	text: "text to translate", from: "start language", to: "destination language"
}
*/
exports.translate = function translate(params,callback){
	
	client.translate(params, function(err, data) {
         callback(data);
    });
};