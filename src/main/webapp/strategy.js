$(document).on("click", "#strategyButton", function() {
	fetchStrategyData();
});

$(document).on("click", "#continuationButton", function() {
	fetchContinuationStrategyData();
});

function fetchStrategyData() {
	$.ajax({
	    type: 'POST',
	    url: '/strategy',
	    data: {	        
	        ticker: $('#ticker').val(),
	        date: $('#date').val()
	    },
	    beforeSend:function(){
	        // this is where we append usually a loading image
	    },
	    success:function(data){
	    	var div = document.getElementById('strategy_div')
	    	div.innerHTML = data;
	    },
	    error:function(){
	        // failed request; give feedback to user
	    	alert("Error!!");
	    }
	});
}

function fetchContinuationStrategyData() {
	$.ajax({
	    type: 'POST',
	    url: '/continuation',
	    data: {	        
	        ticker: $('#ticker').val(),
	        date: $('#date').val(),
	        perc1: $('#perc1').val(),
	        perc2: $('#perc2').val(),
	        perc3: $('#perc3').val()     
	    },
	    beforeSend:function(){
	        // this is where we append usually a loading image
	    },
	    success:function(data){
	    	var div = document.getElementById('continuation_div')
	    	div.innerHTML = data;
	    },
	    error:function(){
	        // failed request; give feedback to user
	    	alert("Error!!");
	    }
	});
}
