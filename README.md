jQuery fontSize Plugin
======================
+ Version: 1.0.2
+ Author: Coto Augosto C.
+ URL: http://beecoss.com
+ Twitter: http://twitter.com/coto
+ Created: 2008

Description
-----------
jQuery plugin helps easily to increase or decrease font size of your website. 

## Features
+ change the font size for whole Body content or only specific elements in your page.
+ (optional) specific a limit to increase or decreae

## Sample

```javascript
$(document).ready(function(){
	$('.increase').fontSize({
		action: "up",
		elements: "#content",
		max: 36
	});

	$('.decrease').fontSize({
		action: 'down',
		elements: "#content"
	});
})
```

## Tested
 jQuery 1.0.2 and above

## Demo
http://www.beecoss.com/sandbox/fontsize/sample.html

## Download from jQuery Plugin Site
http://plugins.jquery.com/fontsize

## Licensed under the GPL
http://www.gnu.org/licenses/gpl.html

