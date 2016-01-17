/*===================================
    IE DETECTION
====================================*/

 (function(){
	/**
	 * detect IE
	 * returns version of IE or false, if browser is not Internet Explorer
	 */
     function detectIE() {
         var ua = window.navigator.userAgent;

         var msie = ua.indexOf('MSIE ');
         if (msie > 0) {
	        // IE 10 or older => return version number
	        return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
	    }

	    var trident = ua.indexOf('Trident/');
	    if (trident > 0) {
	        // IE 11 => return version number
	        var rv = ua.indexOf('rv:');
	        return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
	    }

	    var edge = ua.indexOf('Edge/');
	    if (edge > 0) {
	       // IE 12 => return version number
	       return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
     }

	    // other browser
	    return false;
	}

	if(detectIE()){
		$('body').addClass("ie");
	}

})();

//mobile detection
var isMobile = false;
if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
    isMobile = true;
}

/*===================================
 Browser Back Button Fix
 ====================================*/
window.onunload = function(){};


/*===================================
	Preloader
====================================*/

 $(document).ready(function() {
  $(".animsition").animsition({
    inClass: 'fade-in',
    outClass: 'fade-out',
    inDuration: 100,
    outDuration: 100,
    //linkElement: '.animsition-link',
    linkElement: '#menuzord a:not([target="_blank"]):not([href^=#]), .animsition-link',
    loading: true,
    loadingParentElement: 'body', //animsition wrapper element
    loadingClass: 'animsition-loading',
    loadingInner: '', // e.g '<img src="loading.svg" />'
    timeout: false,
    timeoutCountdown: 100,
    onLoadEvent: true,
    browser: [ 'animation-duration', '-webkit-animation-duration'],
    // "browser" option allows you to disable the "animsition" in case the css property in the array is not supported by your browser.
    // The default setting is to disable the "animsition" in a browser that does not support "animation-duration".
    overlay : false,
    overlayClass : 'animsition-overlay-slide',
    overlayParentElement : 'body',
    transition: function(url){ window.location.href = url; }
  });
});



/*===================================
	+ Mega Menu
	+ Parallax Scrolling
====================================*/

jQuery(document).ready(function(){
	// Mega Menu
	jQuery("#menuzord").menuzord({
		align: "right"
	});

	// Parallax Scrolling
    if(!isMobile) skrollr.init({
        forceHeight: false,
        smoothScrolling: false
    });
});



/*===================================
	FixedTop Navigation
 ===================================*/

 (function(){

     var $nav = $('#topNavBar');

     function navbarAnimation() {
        if ($(window).scrollTop() > 30) {
        	$nav.addClass('navbar-solid');
        	return;
        }
        
        $nav.removeClass('navbar-solid');
        $(".navbar-nav > li > a").blur();
    }

    navbarAnimation();

    $(window).scroll(function() {
      navbarAnimation();
  });

})();



/*===================================
    On-page navigation smooth scroll
 ===================================*/

$(document).ready(function(){

    $('.menuzord-menu a[href*=#]:not([href=#]), .onPageNav').click(function() {
       
        if (location.pathname.replace(/^\//,'') === this.pathname.replace(/^\//,'') && location.hostname === this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
            if (target.length) {
                $(".navbar-collapse.collapse.in").removeClass("in");
                $('html,body').animate({
                    scrollTop: target.offset().top - 55
                }, 1000, function(){
                  
                });
                return false;
            }
        }
    });


    $("#toggelSearch").click(function(e){
        e.preventDefault();
        $("#searchInput").stop().fadeToggle().find("input").focus();;
        $(this).toggleClass("open");
        $(this).children("i").toggleClass("ion-ios-search-strong ion-android-close");
    });

});



/*===================================
	portfolio
 ===================================*/

//Item Sliders
$(window).load(function () {
    if(!$('.portfolio-slider').html()) return;
    $('.portfolio-slider').flexslider({
        animation: "slide",
        direction: "horizontal",
        slideshowSpeed: 3000,
        start:function(){
            imagesLoaded($(".portfolio"),function(){
                setTimeout(function(){
                    $('.portfolio-filter li:eq(0) a').trigger("click");
                },500);
            });
        }
    });
});



/*===================================
    portfolio filter set active class    
 ===================================*/


 $('.portfolio-filter li').click(function (event) {
    $(this).siblings('.active').removeClass('active');
    $(this).addClass('active');
    event.preventDefault();
});



/*===================================
    portfolio masonry
 ===================================*/


 $(window).load( function() {

    if ($(".portfolio-masonry").length == 0 ) return;

    var $c = $('.portfolio-masonry');
    if(typeof imagesLoaded == 'function') {
        imagesLoaded($c, function () {

            setTimeout(function () {
                $c.isotope({
                    itemSelector: '.portfolio-item',
                    resizesContainer: false,
                    layoutMode: 'masonry',
                    masonry: {
                        // use outer width of grid-sizer for columnWidth
                        columnWidth: '.portfolio-item'
                    },
                    filter: "*"
                });

            }, 500);

        });
    }

});


/*===================================
    portfolio individual gallery
 ===================================*/


$('.portfolio-slider, .portfolio-slider-alt').each(function () { // the containers for all your galleries
    var _items = $(this).find("li > a");
    var items = [];
    for (var i = 0; i < _items.length; i++) {
        items.push({src: $(_items[i]).attr("href"), title: $(_items[i]).attr("title")});
    }
    $(this).parent().find(".action-btn").magnificPopup({
        items: items,
        type: 'image',
        gallery: {
            enabled: true
        }
    });
    $(this).parent().find(".portfolio-description").magnificPopup({
        items: items,
        type: 'image',
        gallery: {
            enabled: true
        }
    });
});


/*===================================
    portfolio gallery
 ===================================*/


$('.portfolio-gallery').each(function () { // the containers for all your galleries
    $(this).find(".popup-gallery").magnificPopup({
        type: 'image',
        gallery: {
            enabled: true
        }
    });
    $(this).find(".popup-gallery2").magnificPopup({
        type: 'image',
        gallery: {
            enabled: true
        }
    });
});



/*===================================
    Popup link
 ===================================*/


$('.popup-link').magnificPopup({
    type: 'image'
});


$('.popup-video').magnificPopup({
    disableOn: 700,
    type: 'iframe',
    mainClass: 'mfp-fade',
    removalDelay: 160,
    preloader: false,
    fixedContentPos: false
});


/*===================================
    portfolio filtering
 ===================================*/


 $('.portfolio-filter').on('click', 'a', function () {
    $('#filters button').removeClass('current');
    $(this).addClass('current');
    var filterValue = $(this).attr('data-filter');
    $(this).parents(".text-center").next().isotope({filter: filterValue});
});



/*===================================
    Section height for parallax image
 ===================================*/

 function setSectionHeight(){
    $('.section-height').each(function(){
        var h = $(this).closest('.section').height() + 160;
        $(this).height(h+'px').show();
    });
}
setSectionHeight();
$(window).on('resize', function(){
    setSectionHeight();
});



/*===================================
    Video Player
 ===================================*/


/*
 * @author       Rob W (http://stackoverflow.com/a/7513356/938089
 * @description  Executes function on a framed YouTube video (see previous link)
 *               For a full list of possible functions, see:
 *               http://code.google.com/apis/youtube/js_api_reference.html
 * @param String frame_id The id of (the div containing) the frame
 * @param String func     Desired function to call, eg. "playVideo"
 * @param Array  args     (optional) List of arguments to pass to function func*/
 function callPlayer(frame_id, func, args) {
    if (window.jQuery && frame_id instanceof jQuery) frame_id = frame_id.get(0).id;
    var iframe = document.getElementById(frame_id);
    if (iframe && iframe.tagName.toUpperCase() != 'IFRAME') {
        iframe = iframe.getElementsByTagName('iframe')[0];
    }
    if (iframe) {
        // Frame exists,
        iframe.contentWindow.postMessage(JSON.stringify({
            "event": "command",
            "func": func,
            "args": args || [],
            "id": frame_id
        }), "*");
    }
}


$(document).ready(function(){
    $("#playVideoSmall").click(function(e){
        e.preventDefault();
        callPlayer("ytPlayerSmall","playVideo");
        $("#video-small").hide();
        $("#video-small-container").fadeIn();
    });

    $("#playVideo").click(function(e){
        e.preventDefault();
        callPlayer("ytPlayer","playVideo");
        $("#video").hide();
        $("#video-container").fadeIn();
    });

});


/*===================================
    Progressbar animation
 ===================================*/

 $(document).ready(function(){

    if($(".progress").length == 0) return;

    $(".progressbars").each(function(){
        var $this = $(this);
        var inview = new Waypoint.Inview({
            element: $this,
            enter: function (direction) {
                $this.find(".progress-bar").each(function () {
                    $(this).css('width', $(this).attr("aria-valuenow") + '%');
                });
            }
        });
    });
});


/*===================================
    Google Map
 ===================================*/

(function(){

    if($("#map").length == 0 || typeof google == 'undefined') return;

    // When the window has finished loading create our google map below
    google.maps.event.addDomListener(window, 'load', init);

    var mkr = new google.maps.LatLng(40.6700, -74.2000);
    var cntr = (isMobile) ? mkr : new google.maps.LatLng(40.6700, -73.9400);

    function init() {
        // Basic options for a simple Google Map
        // For more options see: https://developers.google.com/maps/documentation/javascript/reference#MapOptions
        var mapOptions = {
            // How zoomed in you want the map to start at (always required)
            zoom: 11,
            scrollwheel: false,
            // The latitude and longitude to center the map (always required)
            center: cntr, // New York

            // How you would like to style the map.
            // This is where you would paste any style found on Snazzy Maps.
            styles: [
                {
                    "featureType": "all",
                    "elementType": "geometry.fill",
                    "stylers": [
                        {
                            "visibility": "on"
                        },
                        {
                            "saturation": "-11"
                        }
                    ]
                },
                {
                    "featureType": "administrative",
                    "elementType": "geometry.fill",
                    "stylers": [
                        {
                            "saturation": "22"
                        }
                    ]
                },
                {
                    "featureType": "administrative",
                    "elementType": "geometry.stroke",
                    "stylers": [
                        {
                            "saturation": "-58"
                        },
                        {
                            "color": "#cfcece"
                        }
                    ]
                },
                {
                    "featureType": "administrative",
                    "elementType": "labels.text",
                    "stylers": [
                        {
                            "color": "#f8f8f8"
                        }
                    ]
                },
                {
                    "featureType": "administrative",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#999999"
                        },
                        {
                            "visibility": "on"
                        }
                    ]
                },
                {
                    "featureType": "administrative",
                    "elementType": "labels.text.stroke",
                    "stylers": [
                        {
                            "visibility": "on"
                        }
                    ]
                },
                {
                    "featureType": "administrative.country",
                    "elementType": "geometry.fill",
                    "stylers": [
                        {
                            "color": "#f9f9f9"
                        },
                        {
                            "visibility": "simplified"
                        }
                    ]
                },
                {
                    "featureType": "landscape",
                    "elementType": "all",
                    "stylers": [
                        {
                            "color": "#f2f2f2"
                        }
                    ]
                },
                {
                    "featureType": "landscape",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "saturation": "-19"
                        },
                        {
                            "lightness": "-2"
                        },
                        {
                            "visibility": "on"
                        }
                    ]
                },
                {
                    "featureType": "poi",
                    "elementType": "all",
                    "stylers": [
                        {
                            "visibility": "off"
                        }
                    ]
                },
                {
                    "featureType": "road",
                    "elementType": "all",
                    "stylers": [
                        {
                            "saturation": -100
                        },
                        {
                            "lightness": 45
                        }
                    ]
                },
                {
                    "featureType": "road.highway",
                    "elementType": "all",
                    "stylers": [
                        {
                            "visibility": "simplified"
                        }
                    ]
                },
                {
                    "featureType": "road.arterial",
                    "elementType": "labels.icon",
                    "stylers": [
                        {
                            "visibility": "off"
                        }
                    ]
                },
                {
                    "featureType": "transit",
                    "elementType": "all",
                    "stylers": [
                        {
                            "visibility": "off"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "all",
                    "stylers": [
                        {
                            "color": "#d8e1e5"
                        },
                        {
                            "visibility": "on"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "geometry.fill",
                    "stylers": [
                        {
                            "color": "#dedede"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "labels.text",
                    "stylers": [
                        {
                            "color": "#cbcbcb"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#9c9c9c"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "labels.text.stroke",
                    "stylers": [
                        {
                            "visibility": "off"
                        }
                    ]
                }
            ]
        };

        // Get the HTML DOM element that will contain your map
        // We are using a div with id="map" seen below in the <body>
        var mapElement = document.getElementById('map');

        // Create the Google Map using our element and options defined above
        var map = new google.maps.Map(mapElement, mapOptions);

        // Let's also add a marker while we're at it
        var marker = new google.maps.Marker({
            position: mkr,
            map: map,
            title: 'Snazzy!'
        });
    }

})();

/*===================================
    Countdown Coming Soon
 ===================================*/

$(document).ready(function(){
    if($(".container-countdown").length == 0) return;

    $('.container-countdown').countdown({
        date: "December 15, 2015 00:00:00",
        render: function(data) {
            var el = $(this.el);
            el.empty()
                .append("<div class='countdown-box'><span class='counter'>" + this.leadingZeros(data.days, 2) + "</span><h6>Days</h6></div>")
                .append("<div class='countdown-box'><span class='counter'>" + this.leadingZeros(data.hours, 2) + "</span><h6>Hours</h6></div>")
                .append("<div class='countdown-box'><span class='counter'>" + this.leadingZeros(data.min, 2) + "</span><h6>Minutes</h6></div>")
                .append("<div class='countdown-box'><span class='counter'>" + this.leadingZeros(data.sec, 2) + "</span><h6>Seconds</h6></div>");
        }
    });
});


/*===================================
    Lightbox gallery    
 ===================================*/

$(window).load(function () {
    imagesLoaded('body', function () {
        $(".lightbox-gallery").magnificPopup({
            delegate: 'a',
            gallery:{
                enabled:true
            },
            type:'image',
            zoom: {
                enabled: true,
                duration: 300, // don't foget to change the duration also in CSS
                opener: function(element) {
                    return element.find('img');
                }
            }
        });
    });
});

/*===================================
 Lightbox gallery
 ===================================*/

$(window).load(function () {
    imagesLoaded('body', function () {
        $(".popup-gallery").magnificPopup({
            delegate: 'a',
            gallery:{
                enabled:true
            },
            type:'image',
            zoom: {
                enabled: true,
                duration: 300, // don't foget to change the duration also in CSS
                opener: function(element) {
                    return element.find('img');
                }
            }
        });
    });
});





/*==========================================
        Polygonal Overlay header
=============================================*/

(function() {
    if(!$('#polygonalHeader').html()) return;
    var width, height, largeHeader, canvas, ctx, points, target, animateHeader = true;

    // Main
    initHeader();
    initAnimation();
    addListeners();

    function initHeader() {
        width = window.innerWidth;
        height = window.innerHeight;
        target = {x: width/2, y: height/2};

        largeHeader = document.getElementById('polygonalHeader');
        largeHeader.style.height = height+'px';

        canvas = document.getElementById('demo-canvas');
        canvas.width = width;
        canvas.height = height;
        ctx = canvas.getContext('2d');

        // create points
        points = [];
        for(var x = 0; x < width; x = x + width/20) {
            for(var y = 0; y < height; y = y + height/20) {
                var px = x + Math.random()*width/20;
                var py = y + Math.random()*height/20;
                var p = {x: px, originX: px, y: py, originY: py };
                points.push(p);
            }
        }

        // for each point find the 5 closest points
        for(var i = 0; i < points.length; i++) {
            var closest = [];
            var p1 = points[i];
            for(var j = 0; j < points.length; j++) {
                var p2 = points[j]
                if(!(p1 == p2)) {
                    var placed = false;
                    for(var k = 0; k < 5; k++) {
                        if(!placed) {
                            if(closest[k] == undefined) {
                                closest[k] = p2;
                                placed = true;
                            }
                        }
                    }

                    for(var k = 0; k < 5; k++) {
                        if(!placed) {
                            if(getDistance(p1, p2) < getDistance(p1, closest[k])) {
                                closest[k] = p2;
                                placed = true;
                            }
                        }
                    }
                }
            }
            p1.closest = closest;
        }

        // assign a circle to each point
        for(var i in points) {
            var c = new Circle(points[i], 2+Math.random()*2, 'rgba(255,255,255,0.3)');
            points[i].circle = c;
        }
    }

    // Event handling
    function addListeners() {
        if(!('ontouchstart' in window)) {
            window.addEventListener('mousemove', mouseMove);
        }
        window.addEventListener('scroll', scrollCheck);
        window.addEventListener('resize', resize);
    }

    function mouseMove(e) {
        var posx = posy = 0;
        if (e.pageX || e.pageY) {
            posx = e.pageX;
            posy = e.pageY;
        }
        else if (e.clientX || e.clientY)    {
            posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
            posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
        }
        target.x = posx;
        target.y = posy;
    }

    function scrollCheck() {
        if(document.body.scrollTop > height) animateHeader = false;
        else animateHeader = true;
    }

    function resize() {
        width = window.innerWidth;
        height = window.innerHeight;
        largeHeader.style.height = height+'px';
        canvas.width = width;
        canvas.height = height;
    }

    // animation
    function initAnimation() {
        animate();
        for(var i in points) {
            shiftPoint(points[i]);
        }
    }

    function animate() {
        if(animateHeader) {
            ctx.clearRect(0,0,width,height);
            for(var i in points) {
                // detect points in range
                if(Math.abs(getDistance(target, points[i])) < 4000) {
                    points[i].active = 0.3;
                    points[i].circle.active = 0.6;
                } else if(Math.abs(getDistance(target, points[i])) < 20000) {
                    points[i].active = 0.1;
                    points[i].circle.active = 0.3;
                } else if(Math.abs(getDistance(target, points[i])) < 40000) {
                    points[i].active = 0.02;
                    points[i].circle.active = 0.1;
                } else {
                    points[i].active = 0;
                    points[i].circle.active = 0;
                }

                drawLines(points[i]);
                points[i].circle.draw();
            }
        }
        requestAnimationFrame(animate);
    }

    function shiftPoint(p) {
        TweenLite.to(p, 1+1*Math.random(), {x:p.originX-50+Math.random()*100,
            y: p.originY-50+Math.random()*100, ease:Circ.easeInOut,
            onComplete: function() {
                shiftPoint(p);
            }});
    }

    // Canvas manipulation
    function drawLines(p) {
        if(!p.active) return;
        for(var i in p.closest) {
            ctx.beginPath();
            ctx.moveTo(p.x, p.y);
            ctx.lineTo(p.closest[i].x, p.closest[i].y);
            ctx.strokeStyle = 'rgba(156,217,249,'+ p.active+')';
            ctx.stroke();
        }
    }

    function Circle(pos,rad,color) {
        var _this = this;

        // constructor
        (function() {
            _this.pos = pos || null;
            _this.radius = rad || null;
            _this.color = color || null;
        })();

        this.draw = function() {
            if(!_this.active) return;
            ctx.beginPath();
            ctx.arc(_this.pos.x, _this.pos.y, _this.radius, 0, 2 * Math.PI, false);
            ctx.fillStyle = 'rgba(156,217,249,'+ _this.active+')';
            ctx.fill();
        };
    }

    // Util
    function getDistance(p1, p2) {
        return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
    }
    
})();



/*===================================
    Youtube video background
 ===================================*/

(function(){

    if(!$('#videoBackground').html()) return;
    $("#videoBackground").mb_YTPlayer();
     
    $('#video-play').click(function(event) {
        event.preventDefault();
        if ($(this).hasClass('fa-play')) {
            $('#videoBackground').playYTP();
        } else {
            $('#videoBackground').pauseYTP();
        }
        $(this).toggleClass('fa-play fa-pause');
        return false;
    });

    $('#video-volume').click(function(event) {
        event.preventDefault();
        $('#videoBackground').toggleVolume();
        $(this).toggleClass('fa-volume-off fa-volume-up');
        return false;
    });
})();



/*===================================
    Fun Facts
 ===================================*/


(function () {
    if(!$('#funfacts').html()) return;
    var inview = new Waypoint.Inview({
        element: $('#funfacts')[0],
        enter: function (direction) {
            $('.fact-number').each(function () {
                $(this).prop('Counter', 0).animate({
                    Counter: $(this).text()
                }, {
                    duration: 3000,
                    easing: 'swing',
                    step: function (now) {
                        $(this).text(Math.ceil(now));
                    }
                });
            });
            this.destroy();
        }
    });

})();



/*=====================================================
    Instagram Feed
========================================================*/

(function(){

    if($("#instafeed").length == 0) return;

    var feed = new Instafeed({
        get: 'tagged',
        tagName: 'nature',
        limit: 6,
        clientId: 'fa06c7e2c5d24947907f097b79d0f99a'
    });
    feed.run();


})();




/*===================================
    FlexSlider
 ===================================*/
$(window).load(function() {
    if ($('.flexslider').length == 0) return;

    $('.flexslider').flexslider({
        animation: "fade",
        easing: "swing",
        controlNav: true,
        pauseOnAction: false
    });
});




/*=====================================================
    Dot Navigation
========================================================*/
$(document).ready(function(){
    $('#dotNav > ul> li > a').tooltip({
        placement: 'left'
    });   

    var sectionPosition = [];
    $('.section').each(function() {
        sectionPosition.push($(this).offset().top);
    });
        
    $(document).scroll(function(){
        var position = $(document).scrollTop();
        var index;

        for (var i=0; i<sectionPosition.length; i++) {
          if (position <= sectionPosition[i]-200) {
              index = i;
              break;
          }
        }
        $('#dotNav ul li').removeClass('active');
        $('#dotNav ul li:eq('+index+')').addClass('active');
    });


    $("#dotNav a").click(function(){
        $("#dotNav li").removeClass("active");
        $(this).parent('li').addClass("active");
    });

/* end dot nav */
});





/*=====================================================
    Particle Background
========================================================*/

(function(){

    if($("#particles-js").length == 0) return;

    particlesJS("particles-js", {"particles":{"number":{"value":80,"density":{"enable":true,"value_area":800}},"color":{"value":"#ffffff"},"shape":{"type":"circle","stroke":{"width":1,"color":"#ffffff"},"polygon":{"nb_sides":8},"image":{"src":"img/github.svg","width":100,"height":100}},"opacity":{"value":0.05524033491425909,"random":true,"anim":{"enable":false,"speed":1,"opacity_min":0.1,"sync":false}},"size":{"value":3,"random":true,"anim":{"enable":false,"speed":40,"size_min":0.1,"sync":false}},"line_linked":{"enable":true,"distance":150,"color":"#ffffff","opacity":0.4,"width":1},"move":{"enable":true,"speed":2.5,"direction":"none","random":false,"straight":false,"out_mode":"bounce","bounce":false,"attract":{"enable":false,"rotateX":600,"rotateY":1200}}},"interactivity":{"detect_on":"canvas","events":{"onhover":{"enable":true,"mode":"grab"},"onclick":{"enable":true,"mode":"push"},"resize":true},"modes":{"grab":{"distance":200,"line_linked":{"opacity":1}},"bubble":{"distance":400,"size":40,"duration":2,"opacity":8,"speed":3},"repulse":{"distance":200,"duration":0.4},"push":{"particles_nb":4},"remove":{"particles_nb":2}}},"retina_detect":true});

})();


/*=====================================================
    Fluid Particles
========================================================*/

function initFluidParticles() {
        var $particlsA = $("#particles");
        if ($particlsA.length) {
            $particlsA.particleground({
                minSpeedX: 0.6,
                minSpeedY: 0.6,
                dotColor: '#ffffff',
                lineColor: '#ffffff',
                density: 6000,
                particleRadius: 2, // curvedLines: true,
                parallaxMultiplier: 5.2,
                proximity: 0
            });
        }
    }
initFluidParticles();


/*=====================================================
    Header Owl carousel
========================================================*/
(function(){
    if(!$('#owl-hs-slider-zoom').html()) return;
    $("#owl-hs-slider-zoom").owlCarousel({
        autoPlay : true,
        stopOnHover : false,
        singleItem: true,
        pagination: false,
        transitionStyle : 'fadeUp'
    });
})();



/*=====================================================
    Special Startup Page Intro Text
========================================================*/
(function(){
    if(!$('#introText').html()) return;
    $("#introText").owlCarousel({
        autoPlay : true,
        stopOnHover : false,
        singleItem: true,
        pagination: false,
        transitionStyle : 'fade'
    });
})();



/*=====================================================
    Full screen navigation
========================================================*/


jQuery(document).ready(function($){
    var isLateralNavAnimating = false;
    
    //open/close lateral navigation
    $('.cd-nav-trigger').on('click', function(event){
        event.preventDefault();
        //stop if nav animation is running 
        if( !isLateralNavAnimating ) {
            if($(this).parents('.csstransitions').length > 0 ) isLateralNavAnimating = true; 
            
            $('body').toggleClass('navigation-is-open');
            $('.cd-navigation-wrapper').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
                //animation is over
                isLateralNavAnimating = false;
            });
        }
    });
});


/*=========================================================
    Single product carousel
===========================================================*/
(function(){
    if(!$('#productLarge').html()) return;

    $(document).ready(function() {
     
      var sync1 = $("#productLarge");
      var sync2 = $("#productThumbnail");
     
      sync1.owlCarousel({
        singleItem : true,
        slideSpeed : 1000,
        navigation: false,
        pagination:false,
        afterAction : syncPosition,
        responsiveRefreshRate : 200,
      });
     
      sync2.owlCarousel({
        items : 4,
        itemsDesktopSmall     : [979,5],
        itemsTablet       : [768,4],
        itemsMobile       : [479,4],
        pagination:false,
        responsiveRefreshRate : 100,
        afterInit : function(el){
          el.find(".owl-item").eq(0).addClass("synced");
        }
      });
     
      function syncPosition(el){
        var current = this.currentItem;
        $("#productThumbnail")
          .find(".owl-item")
          .removeClass("synced")
          .eq(current)
          .addClass("synced")
        if($("#productThumbnail").data("owlCarousel") !== undefined){
          center(current)
        }
      }
     
      $("#productThumbnail").on("click", ".owl-item", function(e){
        e.preventDefault();
        var number = $(this).data("owlItem");
        sync1.trigger("owl.goTo",number);
      });
     
      function center(number){
        var sync2visible = sync2.data("owlCarousel").owl.visibleItems;
        var num = number;
        var found = false;
        for(var i in sync2visible){
          if(num === sync2visible[i]){
            var found = true;
          }
        }
     
        if(found===false){
          if(num>sync2visible[sync2visible.length-1]){
            sync2.trigger("owl.goTo", num - sync2visible.length+2)
          }else{
            if(num - 1 === -1){
              num = 0;
            }
            sync2.trigger("owl.goTo", num);
          }
        } else if(num === sync2visible[sync2visible.length-1]){
          sync2.trigger("owl.goTo", sync2visible[1])
        } else if(num === sync2visible[0]){
          sync2.trigger("owl.goTo", num-1)
        }
        
      }
     
    });
    
})();
    


/*=========================================================
    Single portfolio item carousel
===========================================================*/
(function(){

    if(!$('#itemLarge').html()) return;
    $(document).ready(function() {
     
      var sync1 = $("#itemLarge");
      var sync2 = $("#itemThumbnail");
     
      sync1.owlCarousel({
        singleItem : true,
        slideSpeed : 1000,
        navigation: false,
        pagination:false,
        afterAction : syncPosition,
        responsiveRefreshRate : 200,
      });
     
      sync2.owlCarousel({
        items : 6,
        itemsDesktop      : [1199,6],
        itemsDesktopSmall     : [979,5],
        itemsTablet       : [768,4],
        itemsMobile       : [479,3],
        pagination:false,
        responsiveRefreshRate : 100,
        afterInit : function(el){
          el.find(".owl-item").eq(0).addClass("synced");
        }
      });
     
      function syncPosition(el){
        var current = this.currentItem;
        $("#itemThumbnail")
          .find(".owl-item")
          .removeClass("synced")
          .eq(current)
          .addClass("synced")
        if($("#itemThumbnail").data("owlCarousel") !== undefined){
          center(current)
        }
      }
     
      $("#itemThumbnail").on("click", ".owl-item", function(e){
        e.preventDefault();
        var number = $(this).data("owlItem");
        sync1.trigger("owl.goTo",number);
      });
     
      function center(number){
        var sync2visible = sync2.data("owlCarousel").owl.visibleItems;
        var num = number;
        var found = false;
        for(var i in sync2visible){
          if(num === sync2visible[i]){
            var found = true;
          }
        }
     
        if(found===false){
          if(num>sync2visible[sync2visible.length-1]){
            sync2.trigger("owl.goTo", num - sync2visible.length+2)
          }else{
            if(num - 1 === -1){
              num = 0;
            }
            sync2.trigger("owl.goTo", num);
          }
        } else if(num === sync2visible[sync2visible.length-1]){
          sync2.trigger("owl.goTo", sync2visible[1])
        } else if(num === sync2visible[0]){
          sync2.trigger("owl.goTo", num-1)
        }
        
      }
     
    });
})();
    





/*------------------------------------------
 Subscribe form ajax
 ------------------------------------------*/


$('#subscription-form').submit(function(e) {

    e.preventDefault();
    var $form           = $('#subscription-form');
    var submit          = $('#subscribe-button');
    var ajaxResponse    = $('#subscription-response');
    var email           = $('#subscriber-email').val();

    $.ajax({
        type: 'POST',
        url: 'php/subscribe.php',
        dataType: 'json',
        data: {
            email: email
        },
        cache: false,
        beforeSend: function(result) {
            submit.html("Working...");
        },
        success: function(result) {
            if(result.sendstatus == 1) {
                ajaxResponse.html(result.message);
                $form.fadeOut(500);
            } else {
                ajaxResponse.html(result.message);
                submit.html('<i class="ion-heart"></i> Get it');
            }
        },
        error: function(){
            submit.html('<i class="ion-heart"></i> Get it');
        }
    });

});

$(document).ready(function() {
    if(!$('#owl-testimonial').html()) return;
    $("#owl-testimonial").owlCarousel({
        autoPlay: 3000,
        stopOnHover: true,
        pagination: false,
        navigation: true,
        navigationText: false,
        paginationSpeed : 1000,
        goToFirstSpeed : 2000,
        singleItem : true,
        autoHeight : true,
        transitionStyle:"fade"
    });
});