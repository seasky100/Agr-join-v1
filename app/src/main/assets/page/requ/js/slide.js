var Slide = function(_listener, container, items, activeIdx){
	var listener = _listener;
	var wrap = container;
	var slides = items;
	var i = activeIdx;
	var width = wrap.width();
	$(items[activeIdx]).addClass("active");

	slides.on('swipeleft', function(e) {
		if (i === slides.length - 1) { 
		return; }
		slides.eq(i + 1).trigger('activate');
		listener.updateIdx(i + 1);
	})
	.on('swiperight', function(e) {
		if (i === 0) { return; }
		slides.eq(i - 1).trigger('activate');
		listener.updateIdx(i + 1);
	})
	.on('activate', function(e) {
		slides.eq(i).removeClass('active');
		$(e.target).addClass('active');
		// Update the active slide index
		i = slides.index(e.target);
	})
	.on('movestart', function(e) {
		if ((e.distX > e.distY && e.distX < -e.distY) ||(e.distX < e.distY && e.distX > -e.distY)) {
			e.preventDefault();
			return;
		}
		wrap.addClass('notransition');
	})
	.on('move', function(e) {
		var left = 100 * e.distX / width;
		if (e.distX < 0) {
			if (slides[i+1]) {
				slides[i].style.left = left + '%';
				slides[i+1].style.left = (left+100)+'%';
			}else {
				slides[i].style.left = left/4 + '%';
			}
		}
		if (e.distX > 0) {
			if (slides[i-1]) {
				slides[i].style.left = left + '%';
				slides[i-1].style.left = (left-100)+'%';
			}else {
				slides[i].style.left = left/5 + '%';
			}
		}
	})
	.on('moveend', function(e) {
	
		wrap.removeClass('notransition');
		slides[i].style.left = '';
		
		if (slides[i+1]) {
			slides[i+1].style.left = '';
		}
		if (slides[i-1]) {
			slides[i-1].style.left = '';
		}
	});

	$(document).on('click', '.slide_button', function(e) {
		var href = e.currentTarget.hash;

		$(href).trigger('activate');

		e.preventDefault();
	});
}