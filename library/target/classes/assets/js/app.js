


$(":button").click(function() {
	var isbn = this.id;
	alert('About to report lost on ISBN ' + isbn);
	// Perform an Ajax call to update the book status to lost
	// http://54.215.210.230:8002/library/v1/books/2?status=lost
	// PUT method
	 sendAjax(isbn,this);
});


function sendAjax(isbn,button) {
	
	var libUrl = "/library/v1/books/"+isbn+"?status=lost";
$.ajax({
	  url: libUrl,
	  type: 'PUT',
	  data: "",
	  success: function(data) {
	    button.disabled = true;
	  }
	});

}
