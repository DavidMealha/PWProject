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
	return [selectedOption.id, selectedDate, selectedOption.text];
}

function changeDateFormat(date){
	return date.replace("-", "").replace("-", "");
}

function matchResults(results, formParameters){
	var matchedResults = [];
	var inputDate = changeDateFormat(formParameters[1]);

	results.forEach(function(element, index, results){
		if (element.date == inputDate && element.profileId == formParameters[0]) {
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
function getGuardianNews(beginDate, date, query){
	var API_URL = "https://content.guardianapis.com/search?";
	var API_KEY = "API_KEY";
	var pageSize = 2;
	var PARAMETERIZED_URL = API_URL + "q=" + query + "&api-key=" + API_KEY + "&page-size=" + pageSize + "&from-date=" + beginDate + "&to-date=" + date;

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



//=============================================
//          START OF NYT FETCH
//=============================================
function getNYTNews(beginDate, date, query){
	var API_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
	var API_KEY = "API_KEY";
	var PARAMETERIZED_URL = API_URL + "q=" + query + "&api-key=" + API_KEY + "&sort=newest&begin_date=" + changeDateFormat(beginDate) + "&end_date=" + changeDateFormat(date);

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
	return results.response.docs;
}

function showNYTNews(results, query){
	var resultsDiv = document.getElementById("otherResults");

	var result;
	results.splice(0,2).forEach(function(element){
		
		result = document.createElement("div");
		result.className = "newsDiv";

		var title = document.createElement("h4");
		title.innerText = element.lead_paragraph;

		var date = document.createElement("p");
		var aux = new Date(element.pub_date);
		var month = aux.getMonth() + 1;

		date.innerText = "Created at " + aux.getDate() + "/" + month + "/" + aux.getFullYear();

		var more = document.createElement("a");
		more.href = element.web_url;
		more.innerText = "Click for more";
		more.className = "click-for-more-button text-center";
		more.setAttribute('target','_blank');

		var divImg = document.createElement("div");
		
		var span = document.createElement("span");
		span.innerText = "Source: ";
		
		var img = document.createElement("img");
		img.src = "images/nyt.png";
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


//=============================================
//          START OF REDDIT FETCH
//=============================================
function getRedditNews(beginDate, endDate, query){
	var q_with_timestamp = "(and '" + query + "' (and+timestamp%3A" + beginDate + ".." + endDate + "))";

	var API_URL = "https://www.reddit.com/r/news/search.json?";
	var PARAMETERIZED_URL = API_URL + "q=" + q_with_timestamp + "&sort=" + "relevance" + "&syntax=cloudsearch";

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
	return results.data.children;
}

function showRedditNews(results, query){
	var resultsDiv = document.getElementById("otherResults");

	var result;
	results.splice(0,2).forEach(function(el){
		var element = el.data;
		
		result = document.createElement("div");
		result.className = "newsDiv";

		var title = document.createElement("h4");
		title.innerText = element.title;

		var date = document.createElement("p");
		var aux = new Date(element.created * 1000);
		var month = aux.getMonth();

		date.innerText = "Created at " + aux.getDate() + "/" + month + "/" + aux.getFullYear();

		var more = document.createElement("a");
		more.href = element.url;
		more.innerText = "Click for more";
		more.className = "click-for-more-button text-center";
		more.setAttribute('target','_blank');

		var moreReddit = document.createElement("a");
		moreReddit.href = "https://www.reddit.com/" + element.permalink;
		moreReddit.innerText = "See on Reddit";
		moreReddit.className = "click-for-more-reddit-button text-center";
		moreReddit.setAttribute('target','_blank');

		var divImg = document.createElement("div");
		
		var span = document.createElement("span");
		span.innerText = "Source: ";
		
		var img = document.createElement("img");
		img.src = "images/reddit.png";
		img.className = "small-image";

		divImg.append(span);
		divImg.append(img);

		result.append(title);
		result.append(date);
		result.append(divImg);
		result.append(more);
		result.append(moreReddit);

		resultsDiv.append(result);
	});	
}



//=============================================
//              END OF ALL FETCH
//=============================================
function cleanOtherSources(query){
	var resultsDiv = document.getElementById("otherResults");
	$(resultsDiv).empty();

	var header = document.createElement("h4");
	header.innerText  = "Latest News About " + query;
	header.className = "text-center";

	resultsDiv.append(header);
}

$(document).ready(function() {
	var profiles = getProfiles();
	addProfilesToSelect(profiles);
	var results = getResults();

	var beginDate = "2016-08-02";
	var beginDateObj = new Date(2016, 08, 02, 0, 0, 0, 0);
	var beginDateTimestamp = beginDateObj.getTime() / 1000;

	$('#myform').on("submit", function(evt){
		evt.preventDefault();
		var formParameters = handleForm();			

		//show twitter
		var matchedResults = matchResults(results, formParameters);	
		showResults(matchedResults);

		var query = formParameters[2];

		var endDate = formParameters[1];
		var endDateSplitted = endDate.split("-");
		var endDateObj = new Date(endDateSplitted[0], endDateSplitted[1], endDateSplitted[2], 23, 59, 59, 0);
		var endDateTimestamp = endDateObj.getTime() / 1000;

		cleanOtherSources(query);

		//get guardian results
		var guardianResults = getGuardianNews(beginDate, endDate, query);
		showGuardianNews(guardianResults, query);

		//get nyt results
		var nytResults = getNYTNews(beginDate, endDate, query);
		showNYTNews(nytResults);

		//get reddit results
		var redditResults = getRedditNews(beginDateTimestamp, endDateTimestamp, query);
		showRedditNews(redditResults);

		console.log(redditResults);
	});
});	
