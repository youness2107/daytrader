$(document).on("click", "#okButton", function() {
	fetchStockData();
});

function fetchStockData() {
	$.ajax({
	    type: 'POST',
	    url: '/stock',
	    data: {	        
	        ticker: $('#ticker').val(),
	        date: $('#date').val(),
	        intraday: $('#intraday').val()
	    },
	    beforeSend:function(){
	        // this is where we append usually a loading image
	    },
	    success:function(data){
	    	chartGood(data);
	    },
	    error:function(){
	        // failed request; give feedback to user
	    	alert("Error!!");
	    }
	});
}

function chartGood(table) {
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

	function drawChart() {
	  
	  var data = new google.visualization.DataTable();
	  data.addColumn('string', 'Task');
	  data.addColumn('number', 'min');
	  data.addColumn('number', 'open');
	  data.addColumn('number', 'close');
	  data.addColumn('number', 'high');
	  
	  var rows = new Array ();
	  
	  var arrayLength = table.length;
	  for (var i = 0; i < arrayLength; i++) {
		  table[i][0]  = "'" + table[i][0] + "'";
	  }
	  
	  data.addRows(table);
	  
      var options = {
              legend: 'none',
              bar: { groupWidth: '100%' }, // Remove space between bars.
              candlestick: {
//                fallingColor: { strokeWidth: 0, fill: '#a52714'}, // red
//                risingColor: { strokeWidth: 0, fill: '#0f9d58'}   // green
              },
              explorer: { 
//                  actions: ['dragToZoom', 'rightClickToReset'],
//                  axis: 'horizontal',
//                  keepInBounds: true,
//                  maxZoomIn: 4.0
          }
              
      };
	
	  var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
	  chart.draw(data, options);
	}
}

