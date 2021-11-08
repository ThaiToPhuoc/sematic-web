window.onload = function() {
	fetch("footer.html")
	.then(response => {
		return response.text()
	})
	.then(data => {
		document.querySelector("footer").innerHTML = data;
	});
	
	fetch("carousel.html")
	.then(response => {
		return response.text()
	})
	.then(data => {
		document.querySelector("carousel").innerHTML = data;
	});
}


