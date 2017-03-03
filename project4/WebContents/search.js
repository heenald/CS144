
var xmlHTTPRequest = new XMLHttpRequest(); 

function AutoSuggestControl(oTextbox, oProvider) {

	alert("In AutoSuggestControl");

    this.layer = null;
    this.provider = oProvider;
    this.textbox = oTextbox;
    this.init(); 
}

AutoSuggestControl.prototype.init = function(){

	autoSuggestControl = this;
	autoSuggestControl.textbox.onkeyup = function(oEvent){
		autoSuggestControl.handleKeyUp(oEvent);
	}
}

AutoSuggestControl.prototype.handleKeyUp = function(oEvent){
	//pass this so that suggestionProvider can append the suggestions
	this.provider.getSuggestions(this);
}


//selecting specific characters in a textbox
AutoSuggestControl.prototype.selectRange = function (iStart, iLength) {
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", iStart); 
        oRange.moveEnd("character", iLength - this.textbox.value.length); 
        oRange.select();
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(iStart, iLength);
    }

    this.textbox.focus(); 
};


AutoSuggestControl.prototype.hideSuggestions = function () {
    this.layer.style.visibility = "hidden";
};


AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {

    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};


AutoSuggestControl.prototype.createDropDown = function () {

	//sets up the layer
    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    document.body.appendChild(this.layer);

    //more code to come
};

AutoSuggestControl.prototype.displaySuggestions=function(oSuggestions){

	//clear previous suggestions, if any
	this.layer.innerHTML = "";

	for(i=0;i<oSuggestions.length;i++){
		var suggestionDiv = document.createElement("div");
		this.layer.appendChild(suggestionDiv.appendChild(document.createTextNode(oSuggestions[i])));
	}

}

function GoogleSuggestionProvider() {
}

GoogleSuggestionProvider.prototype.getSuggestions = function(oAutoSuggestControl){

	var query = oAutoSuggestControl.textbox.value;

	if(query.length > 0){

		var url = "suggest?query="+encodeURI(query);
		xmlHTTPRequest.open("GET", url);

		xmlHTTPRequest.onreadystatechange = processXML(oAutoSuggestControl);
		xmlHTTPRequest.send(null);
	}

}

function processXML(oAutoSuggestControl){

	if(xmlHTTPRequest.readyState == 4){

		var suggestions = new Array();
		var elements = xmlHTTPRequest.responseXML.getElementsByTagName('CompleteSuggestion');
		for(i=0;i<elements.length;i++){
			suggestions.push(elements[i].childNodes[0].getAttribute("data"));
		}

		oAutoSuggestControl.displaySuggestions(suggestions);

	}

}


window.onload = function(){
		new AutoSuggestControl(document.getElementById("query"),new GoogleSuggestionProvider());
}
