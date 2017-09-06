$(document).on("click", "#indicatorsButton", function() {
	fetchIndicators();
});

function fetchIndicators() {
	$.ajax({
	    type: 'POST',
	    url: '/indicator',
	    data: {	        
	        ticker: $('#ticker').val(),
	        date: $('#date').val(),
	        intraday: $('#intraday').val()
	    },
	    beforeSend:function(){
	        // this is where we append usually a loading image
	    },
	    success:function(data){
	    	clearIndicatorsDiv();
	    	for (var key in data) {	    		
	    		createDivAndDraw(key, data[key]);
	    	}
	    },
	    error:function(){
	        // failed request; give feedback to user
	    	alert("Error!!");
	    }
	});
}

function clearIndicatorsDiv() {
	var indicatorsDiv = document.getElementById('indicators');
	// reset div
	indicatorsDiv.innerHTML = "";
}

function createDivAndDraw(indicatorName, indicatorArray) {
	var div = document.createElement('div');
	div.id = indicatorName;
	var indicatorsDiv = document.getElementById('indicators');
	indicatorsDiv.appendChild(div);
	drawIndicators(div, indicatorArray, indicatorName);
	// This covers the basics of DOM manipulation. Remember, element addition to the body 
	// or a body-contained node is required for the newly created node to be visible within 
	// the document.
//	div.innerText = "put dema graph here";
}

function drawIndicators(divElement, table, indicatorName) {
	google.charts.load('current', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawChart);

	function drawChart() {
		
		  var data = new google.visualization.DataTable();
		  data.addColumn('datetime', 'time');
		  var names =   indicatorName.split("-");
	  
		  for (var index in names) {
			  data.addColumn('number', names[index]);  
		  }
		  
		  var arrayLength = table.length;
		  for (var i = 0; i < arrayLength; i++) {
			  table[i][0] = new Date(table[i][0]*1000);	
		  }
		  data.addRows(table);

		  var options = {
		    title: indicatorName,
		    curveType: 'function',
		    legend: { position: 'bottom' },
            explorer: { 
              actions: ['dragToZoom', 'rightClickToReset'],
              axis: 'horizontal',
              keepInBounds: true,
              maxZoomIn: 64.0
            }		    
		  };

		  var chart = new google.visualization.LineChart(divElement);
	
		  chart.draw(data, options); 
	}
}

//
function dateFromEpoch(utcSeconds) {
	var utcSeconds = 1234567890;
	var d = new Date(0); // The 0 there is the key, which sets the date to the epoch
	d.setUTCSeconds(utcSeconds);
	return d;
}
