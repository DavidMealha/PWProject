function getProfiles(){
	var profiles;
	$.ajax({
		method: "GET",
		url: "http://demo1518708.mockable.io/profiles",
		dataType: "json",
		async: false,
		success: function(data){
			profiles = data;
		}
	});
	return profiles;
}

function getResults(){
	var results;
	$.ajax({
		method: "GET",
		url: "http://demo1518708.mockable.io/results",
		dataType: "json",
		async: false,
		success: function(data){
			results = data;
		}
	});
	return results;
}

function addProfilesToSelect(profiles){
	var select = document.getElementById("profilesList");

	profiles.forEach(function(profile){
		var option = document.createElement("option");
		option.id = profile.topid;
		option.text = profile.title;
		select.add(option);
	});
}

function handleForm(){
	var select = document.getElementById("profilesList");
	var selectedOption = select.options[select.selectedIndex];

	var selectedDate = document.getElementById("date").value;
	return [selectedOption.id, changeDateFormat(selectedDate), selectedOption.text];
}

function changeDateFormat(date){
	return date.replace("-", "").replace("-", "");
}

function matchResults(results, formParameters){
	var matchedResults = [];
	results.forEach(function(element, index, results){
		if (element.date == formParameters[1] && element.profileId == formParameters[0]) {
			matchedResults.push(element);
		}
	});
	return matchedResults;
}

function showResults(matchedResults){
	var resultsDiv = document.getElementById("results");
	$(resultsDiv).empty();

	var header = document.createElement("h4");
	header.innerText  = "Twitter Results";
	header.className = "text-center";

	resultsDiv.append(header);

	var result;
	matchedResults.forEach(function(element){
		
		result = document.createElement("div");
		//result.className = "col-md-4";
		result.id = element.tweetId;
		result.className = "ml-15";
		resultsDiv.append(result);

		twttr.widgets.createTweet(
			element.tweetId,
			document.getElementById(element.tweetId)
		);		
	});	
}

//=============================================
//          START OF GUARDIAN FETCH
//=============================================
function getGuardianNews(date, query){
	var API_URL = "https://content.guardianapis.com/search?";
	var API_KEY = "93003d8b-1d93-43dd-98c6-1978823de3e5";
	var PARAMETERIZED_URL = API_URL + "q=" + query + "&api-key=" + API_KEY;

	var results = "";
	$.ajax({
		method: "GET",
		url: PARAMETERIZED_URL,
		dataType: "json",
		async: false,
		success: function(data){
			results = data;
		}
	});
	return results.response.results;
}

function showGuardianNews(results, query){
	var resultsDiv = document.getElementById("otherResults");

	var result;
	results.forEach(function(element){
		
		result = document.createElement("div");
		result.className = "newsDiv";

		var title = document.createElement("h4");
		title.innerText = element.webTitle;

		var date = document.createElement("p");
		var aux = new Date(element.webPublicationDate);
		var month = aux.getMonth() + 1;

		date.innerText = "Created at " + aux.getDate() + "/" + month + "/" + aux.getFullYear();

		var more = document.createElement("a");
		more.href = element.webUrl;
		more.innerText = "Click for more";
		more.className = "click-for-more-button text-center";
		more.setAttribute('target','_blank');

		var divImg = document.createElement("div");
		
		var span = document.createElement("span");
		span.innerText = "Source: ";
		
		var img = document.createElement("img");
		img.src = "images/guardian.png";
		img.className = "small-image";

		divImg.append(span);
		divImg.append(img);

		result.append(title);
		result.append(date);
		result.append(divImg);
		result.append(more);

		resultsDiv.append(result);
	});	
}

function cleanOtherSources(query){
	var resultsDiv = document.getElementById("otherResults");
	$(resultsDiv).empty();

	var header = document.createElement("h4");
	header.innerText  = "Latest News About " + query;
	header.className = "text-center";

	resultsDiv.append(header);
}

//=============================================
//          START OF GUARDIAN FETCH
//=============================================

//=============================================
//          START OF GUARDIAN FETCH
//=============================================

//=============================================
//          START OF GUARDIAN FETCH
//=============================================

$(document).ready(function() {
	var profiles = getProfiles();
	addProfilesToSelect(profiles);
	var results = getResults();

	$('#myform').on("submit", function(evt){
		evt.preventDefault();
		var formParameters = handleForm();			

		//show twitter
		var matchedResults = matchResults(results, formParameters);	
		showResults(matchedResults);

		//get guardian results
		var guardianResults = getGuardianNews(formParameters[1], formParameters[2]);
		cleanOtherSources(formParameters[2]);
		showGuardianNews(guardianResults, formParameters[2]);

		console.log(guardianResults);
	});
});	