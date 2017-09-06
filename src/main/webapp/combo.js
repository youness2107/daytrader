$(document).on("click", "#comboButton", function() {
	fetchComboData();
});

function fetchComboData() {
	$.ajax({
	    type: 'POST',
	    url: '/combo',
	    data: {	        
	        ticker: $('#ticker').val(),
	        date: $('#date').val(),
	        intraday: $('#intraday').val()
	    },
	    beforeSend:function(){
	        // this is where we append usually a loading image
	    },
	    success:function(data){
	    	drawCharts(data);
	    },
	    error:function(){
	        // failed request; give feedback to user
	    	alert("Error!!");
	    }
	});
}


function drawCharts(table) {
	google.charts.load('current', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawVisualization);
	
	function drawVisualization() {
		  
		var data = new google.visualization.DataTable();
		data.addColumn('datetime', 'time');
		data.addColumn('number', 'min');
		data.addColumn('number', 'open');
		data.addColumn('number', 'close');
		data.addColumn('number', 'high');
		data.addColumn('number', 'ema8');
		data.addColumn('number', 'ema20');  
		var arrayLength = table.length;
		
		for (var i = 0; i < arrayLength; i++) {
			table[i][0]  = new Date(table[i][0]*1000);
		}
		  
		data.addRows(table);
		var options = {
				title : 'Price + Ema8 + Ema20 (You can zoom in and out!)',
				seriesType: 'candlesticks',
				series: {1: {type: 'line'}, 2: {type: 'line'}},
				explorer: { 
	                actions: ['dragToZoom', 'rightClickToReset'],
	                axis: 'horizontal',
	                keepInBounds: true,
	                maxZoomIn: 64.0
	              }	
		};
		
		var chart = new google.visualization.ComboChart(document.getElementById('combo_div'));
		chart.draw(data, options);
    }		
}
